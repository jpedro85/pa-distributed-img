package Network.Client;

import Network.Server.LoadTrackerReader;
import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.ImageStates;
import Utils.Events.Enums.SeverityLevels;
import Utils.Events.Event;
import Utils.Events.EventFactory;
import Utils.Image.ImageTransformer;
import Utils.Image.SplitImage;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.VarSync;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

// TODO: Documentation

/**
 *
 */
public class MasterClient extends Thread implements Subject, Observer {

    private final VarSync< ArrayList<Observer> > observers;

   // private final LoadTrackerReader loadTrackerReader; TODO:complete
    private BufferedImage originalImage;
    private BufferedImage[][] splittedOriginalImage;
    private final BufferedImage[][] splittedFinalImage;
    private ArrayList<SlaveClient> slaveClientsList;

    private VarSync<Boolean> isCancel;

    public MasterClient(String name, BufferedImage originalImage, int nRows, int nColumns) throws IllegalArgumentException {

        if ( nRows < 1 || nColumns < 1 )
            throw new IllegalArgumentException("MasterClient nRows and nColumns new to be >= 1");

        this.splitOriginalImage();

        this.splittedFinalImage = new BufferedImage[nRows][nColumns];
        this.observers = new VarSync< ArrayList<Observer> >( new ArrayList<Observer>() );
        this.setName("MasterClient " + name );
        this.slaveClientsList = new ArrayList<SlaveClient>();
        this.isCancel = new VarSync<>(true);
        this.originalImage = originalImage;
    }

    @Override
    public void start()
    {
        boolean allInterrupted = this.isAllSlavesInterrupted();
        this.isCancel.lock();
        if ( this.isCancel.asyncGet() && allInterrupted)
        {
            this.isCancel.asyncSet(false);
            super.start();
        }
        else if ( this.isCancel.asyncGet() )
        {
            Event event = EventFactory.createErrorEvent( String.format( "%s %s", this.getName(), " is waiting." ), EventTypes.ERROR, SeverityLevels.WARNING );
            this.notify(event);
        }
        this.isCancel.unlock();
    }

    @Override
    public void run() {
        this.initSlaves();
        this.waitSlaves();
        if ( this.isCancel.syncGet() ) return;
        this.finishTask();
    }

    private void splitOriginalImage() throws IllegalArgumentException{
        this.splittedOriginalImage = ImageTransformer.splitImage(originalImage,this.getNumberOfRows(),this.getNumberOfColumns() );
    }

    private void initSlaves() {

        for (short line = 0; line < this.getNumberOfRows(); line++)
        {
            for (short column = 0; column < this.getNumberOfColumns(); column++)
            {
                SplitImage splitImage = new SplitImage(column, line, splittedOriginalImage[line][column]);
                SlaveClient slaveClient = new SlaveClient(splittedFinalImage, splitImage);
                slaveClientsList.add(slaveClient);
                slaveClient.start();
            }
        }

        Event event = EventFactory.createImageStateEvent( "Image divided", EventTypes.IMAGE, ImageStates.PREPARED_FOR_PROCESSING, null);
        this.notify(event);

    }

    private void waitSlaves()
    {
        for ( SlaveClient slave :  this.slaveClientsList )
        {
            try {
                slave.join();
            } catch (Exception e) {
                Event event = EventFactory.createErrorEvent( String.format("%s %s %s","Thread",slave.getName(),"was interrupted" ), EventTypes.ERROR, SeverityLevels.ERROR );
                this.notify(event);
            }
        }
    }

    private void finishTask()
    {
        BufferedImage finalImage = ImageTransformer.joinImages(this.splittedFinalImage,originalImage.getWidth(), originalImage.getHeight(), originalImage.getType() );
        Event event = EventFactory.createImageStateEvent( "Image Finished", EventTypes.IMAGE, ImageStates.MERGED, null);
        this.notify(event);
        // TODO: save Image notyfy save

    }

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

    public int getNumberOfColumns() { return this.splittedOriginalImage.length; }

    public int getNumberOfRows() { return this.splittedOriginalImage[0].length; }

    private boolean isAllSlavesInterrupted(){
        boolean allInterrupted = true;
        for (SlaveClient slave : this.slaveClientsList )
        {
            allInterrupted = allInterrupted && !slave.isAlive();
        }
        return allInterrupted;
    }
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
