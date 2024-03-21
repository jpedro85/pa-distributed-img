package Network.Client;

import Network.Server.Response;
import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.ImageStates;
import Utils.Events.Enums.SeverityLevels;
import Utils.Events.Event;
import Utils.Events.EventFactory;
import Utils.Image.ImageTransformer;
import Utils.Image.SplitImage;
import Network.Server.LoadTrackerReader;
import Network.Server.Request;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.VarSync;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * The {@code SlaveClient} class represents a client responsible for processing a portion of an image
 * by communicating with servers and managing observers for event notifications.
 * It extends {@code Thread} and implements the {@code Subject} interface.
 */
public class SlaveClient extends Thread implements Subject {

    private final VarSync<ArrayList<Observer>> observers;
    private LoadTrackerReader serverLoadTrackerReader;
    private final SplitImage splitImage;
    private final BufferedImage[][] resultSplittedImage;
    private final LoadTrackerReader loadTrackerReader;
    private final String name;

    public SlaveClient(BufferedImage[][] resultSplittedImage,SplitImage splitImage, String name,LoadTrackerReader loadTrackerReader)
    {
        this.splitImage = splitImage;
        this.resultSplittedImage = resultSplittedImage;
        this.setName( "SlaveClient L:" + splitImage.getLineNumber() + "  C:" + splitImage.getColumnNumber() );
        this.observers = new VarSync<ArrayList<Observer>>( new ArrayList<Observer>() );
        this.loadTrackerReader = loadTrackerReader;
        this.name = name;
    }

    @Override
    public void run()
    {
        // create request
        String message = String.format("Image:%s SubImage(L:%d C:%d )", this.name, this.splitImage.getLineNumber(), this.splitImage.getColumnNumber() );
        Request request = new Request("Ask to process", message, this.splitImage.getImage());

        int lastErrorPort = 0;
        int errorCount = 0;
        int port;

        // send request, retry if fail until all  try with all server and repeat the same twice and fail
        do
        {
            port = this.loadTrackerReader.getServerWithLessLoad();
            Response response = sendRequestAndReceiveResponse("localhost", port, request);
            if (response != null)
            {
                this.handleResponse(response, message);
                break;
            }
            else
            {
                if (port == lastErrorPort)
                    errorCount++;
                else
                {
                    lastErrorPort = port;
                    errorCount = 1;
                }
            }
        }
        while( errorCount <=2 );
    }

    private void handleResponse( Response response, String message)
    {
        if( response.getMessage().equals( message ) )
        {
            BufferedImage image = ImageTransformer.createImageFromBytes(response.getImageSection() );
            SplitImage receivedSplitImage = new SplitImage(this.splitImage.getColumnNumber(), this.splitImage.getLineNumber(), image);

            this.resultSplittedImage[receivedSplitImage.getLineNumber()][receivedSplitImage.getColumnNumber()] = receivedSplitImage.getImage();

            this.notify( EventFactory.createImageStateEvent( "Image Finished Processing", EventTypes.IMAGE, ImageStates.WAITING_FOR_MERGE, receivedSplitImage) );
        }
        else
            this.notify( EventFactory.createErrorEvent("The received response does not correspond to sent request.", EventTypes.ERROR,SeverityLevels.ERROR ) );
    }


    /**
     * Sends an object to a specified server and waits for a response.
     *
     * @param host    The hostname or IP address of the server.
     * @param port    The port number of the server.
     * @param request The request object to send to the server.
     *
     * @return The response object from the server, or null in case of an error.
     */
    public Response sendRequestAndReceiveResponse(String host, int port, Request request) {
        try (Socket socket = new Socket(host, port)) {

            // Create and initialize the streams for sending and receiving objects
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Send the request to the server
            Event eventWaitingForServer = EventFactory.createImageStateEvent( "Image Finished", EventTypes.IMAGE, ImageStates.WAITING_FOR_PROCESSING, null);
            this.notify(eventWaitingForServer);
            out.writeObject(request);

            // Wait for and return the response from the server
            Response response = (Response) in.readObject();
            return response;

        } catch (Exception e) {
            Event event = EventFactory.createErrorEvent( e.getMessage() , EventTypes.ERROR, SeverityLevels.ERROR );
            this.notify(event);
        }
        // Return null or consider a better error handling/return strategy
        return null;
    }

    public LoadTrackerReader getServerLoadTrackerReader() {
        return serverLoadTrackerReader;
    }

    public SplitImage getSplitImage() {
        return splitImage;
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
}
