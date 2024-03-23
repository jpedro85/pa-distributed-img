package Network.Client;

import Network.Server.LoadTrackerReader;
import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.ImageStates;
import Utils.Events.Enums.SeverityLevels;
import Utils.Events.Event;
import Utils.Events.EventFactory;
import Utils.Image.ImageSaver;
import Utils.Image.ImageTransformer;
import Utils.Image.SplitImage;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.VarSync;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * The MasterClient class orchestrates the parallel processing of an image by dividing it into smaller segments
 * and distributing the processing tasks among multiple SlaveClient instances. It provides functionalities for
 * starting, cancelling, and monitoring the progress of the processing task.
 * <p>
 * Key Features:
 * - Parallel Image Processing: Divides the original image into smaller segments and processes them concurrently
 *   using multiple SlaveClient instances, optimizing the processing time.
 * - Task Management: Allows starting and cancelling the processing task, providing control over the execution
 *   flow based on user requirements.
 * - Event Notification: Utilizes the Observer pattern to notify registered observers about significant events
 *   occurring during the processing task, facilitating real-time monitoring and feedback.
 * - Load Tracking: Integrates with a LoadTrackerReader to monitor server load information, enabling dynamic
 *   load balancing and resource optimization.
 */
public class MasterClient extends Thread implements Subject, Observer {

    private final VarSync< ArrayList<Observer> > observers;
    private final LoadTrackerReader loadTrackerReader;
    private BufferedImage originalImage;
    private BufferedImage[][] splittedOriginalImage;
    private final BufferedImage[][] splittedFinalImage;
    private ArrayList<SlaveClient> slaveClientsList;
    private VarSync<Boolean> isCancel;

    private final String savePhat;

    /**
     * Constructs a MasterClient object with the provided parameters.
     *
     * @param name              The name of the MasterClient (e.g. name of the image to process).
     * @param originalImage     The original image to be processed.
     * @param nRows             The number of rows to divide the image into.
     * @param nColumns          The number of columns to divide the image into.
     * @param loadTrackerReader The reader for tracking load information from servers.
     * @throws IllegalArgumentException If nRows or nColumns is less than 1.
     */
    public MasterClient(String name, BufferedImage originalImage, int nRows, int nColumns, LoadTrackerReader loadTrackerReader, String savePhat) throws IllegalArgumentException {

        if ( nRows < 1 || nColumns < 1 )
            throw new IllegalArgumentException("MasterClient nRows and nColumns new to be >= 1");

        this.originalImage = originalImage;
        this.splittedFinalImage = new BufferedImage[nRows][nColumns];

        this.initializeSplittedOriginalImage(nRows,nColumns);

        this.observers = new VarSync< ArrayList<Observer> >( new ArrayList<Observer>() );
        this.setName( name );
        this.slaveClientsList = new ArrayList<SlaveClient>();
        this.isCancel = new VarSync<>(true);
        this.loadTrackerReader = loadTrackerReader;
        this.savePhat = savePhat;

    }

    /**
     * Starts the processing task.
     */
    @Override
    public void start()
    {
        boolean allInterrupted = this.isAllSlavesInterrupted();
        this.isCancel.lock();
        if ( this.isCancel.asyncGet() && allInterrupted)
        {
            this.isCancel.asyncSet(false);
            this.isCancel.unlock();
            super.start();
        }
        else if ( this.isCancel.asyncGet() )
        {
            Event event = EventFactory.createErrorEvent( String.format( "Cannot start %s because is waiting for slaves.", this.getName() ), EventTypes.ERROR, SeverityLevels.WARNING );
            this.notify(event);
            this.isCancel.unlock();
        }
    }

    /**
     * Runs the processing task.
     */
    @Override
    public void run() {
        this.initSlaves();
        this.waitSlaves();
        if ( this.isCancel.syncGet() ) return;
        this.finishTask();
    }

    /**
     * Splits the original image into smaller segments.
     *
     * @throws IllegalArgumentException If the dimensions of the original image are invalid.
     */
    private void initializeSplittedOriginalImage(int nRows,int nColumns) throws IllegalArgumentException{
        this.splittedOriginalImage = ImageTransformer.splitImage(originalImage,nRows,nColumns );
    }

    /**
     * Initializes the SlaveClient instances for processing image segments.
     */
    private void initSlaves() {

        for (short line = 0; line < this.getNumberOfRows(); line++)
        {
            for (short column = 0; column < this.getNumberOfColumns(); column++)
            {
                SplitImage splitImage = new SplitImage(column, line, splittedOriginalImage[line][column]);
                String name = String.format("Slave %d%d for %s",line,column,this.getName() );
                SlaveClient slaveClient = new SlaveClient(splittedFinalImage, splitImage, name, this.loadTrackerReader );
                slaveClientsList.add(slaveClient);

                slaveClient.addObserver(this);

                slaveClient.start();
            }
        }

        this.notify( EventFactory.createImageStateEvent( "Image divided", EventTypes.IMAGE, ImageStates.PREPARED_FOR_PROCESSING));

    }

    /**
     * Waits for all SlaveClient instances to finish processing.
     */
    private void waitSlaves()
    {
        for ( SlaveClient slave :  this.slaveClientsList )
        {
            try {
                slave.join();
            } catch (Exception e) {
                this.notify( EventFactory.createErrorEvent( String.format("%s %s %s","Thread",slave.getName(),"was interrupted" ), EventTypes.ERROR, SeverityLevels.ERROR ) );
            }
        }
    }

    /**
     * Finishes the processing task. Merges the final image and saves it.
     */
    private void finishTask()
    {
        BufferedImage finalImage = ImageTransformer.joinImages(this.splittedFinalImage,originalImage.getWidth(), originalImage.getHeight(), originalImage.getType() );
        this.notify( EventFactory.createImageStateEvent( "Image Finished", EventTypes.IMAGE, ImageStates.MERGED) );

        String path = String.format("%s/%s_edited.png",this.savePhat,this.getName());
        ImageSaver.saveImage(finalImage,"png",path);
        this.notify( EventFactory.createImageStateEvent( "Image Saved as " + path, EventTypes.IMAGE, ImageStates.SAVED) );

    }

    /**
     * Cancels the processing task.
     */
    public void cancel()
    {
        this.isCancel.lock();
        if ( ! this.isCancel.asyncGet() ) {
            for (SlaveClient slave : this.slaveClientsList) {
                slave.interrupt();
            }
        }
        this.isCancel.unlock();
    }

    /**
     * Retrieves the number of columns in the original image grid.
     *
     * @return The number of columns in the original image grid.
     */
    public int getNumberOfColumns() { return this.splittedOriginalImage.length; }

    /**
     * Retrieves the number of rows in the original image grid.
     *
     * @return The number of rows in the original image grid.
     */
    public int getNumberOfRows() { return this.splittedOriginalImage[0].length; }

    /**
     * Checks whether all SlaveClient instances have finished processing and are interrupted.
     *
     * @return {@code true} if all SlaveClient instances are interrupted, indicating completion of processing;
     * {@code false} otherwise.
     */
    private boolean isAllSlavesInterrupted(){
        boolean allInterrupted = true;
        for (SlaveClient slave : this.slaveClientsList )
        {
            allInterrupted = allInterrupted && !slave.isAlive();
        }
        return allInterrupted;
    }

    /**
     * Checks if the processing task is cancelled.
     *
     * @return True if the processing task is cancelled, false otherwise.
     */
    public boolean isCancel()
    {
        return this.isCancel.syncGet() && this.isAllSlavesInterrupted();
    }

    @Override
    public void addObserver(Observer observer) {

        this.observers.lock();
        if ( !this.observers.asyncGet().contains(observer) )
            this.observers.asyncGet().add(observer);
        this.observers.unlock();

    }

    @Override
    public void removeObserver(Observer observer) {

        this.observers.lock();
        this.observers.asyncGet().remove(observer);
        this.observers.unlock();
    }

    @Override
    public void notify(Event event) {

        this.observers.lock();
        for ( Observer observer : this.observers.asyncGet() )
        {
            observer.update(this, event);
        }
        this.observers.unlock();
    }

    @Override
    public void update(Subject subject, Event event) {
        this.notify(event);
    }
}
