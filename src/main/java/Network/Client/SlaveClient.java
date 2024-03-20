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

// TODO: Documentation
// TODO: Implement Unit tests;
public class SlaveClient extends Thread implements Subject {

    private final VarSync<ArrayList<Observer>> observers;
    private LoadTrackerReader serverLoadTrackerReader;//todo:
    private final SplitImage splitImage;
    private final BufferedImage[][] resultSplittedImage;

    public SlaveClient(BufferedImage[][] resultSplittedImage,SplitImage splitImage)
    {
        this.splitImage = splitImage;
        this.resultSplittedImage = resultSplittedImage;
        this.setName( "SlaveClient L:" + splitImage.getLineNumber() + "  C:" + splitImage.getColumnNumber() );
        this.observers = new VarSync<ArrayList<Observer>>( new ArrayList<Observer>() );
    }

    @Override
    public void run() {
        // TODO: Implement LoadTrackerReader
        // TODO: Pass the config from the loadconfig.
        // sendRequestAndReceiveResponse(host, port, request);
        //todo: put result in array
        System.out.println("Not Implemented Yet");
//        Response response = sendRequestAndReceiveResponse(host, port, request);
//        BufferedImage image = ImageTransformer.createImageFromBytes(response.getImageSection() );
//        SplitImage receivedSplitImage = new SplitImage(this.getSplitImage().getColumnNumber(), this.splitImage.getLineNumber(), image);
//        Event eventWaitingForMerge = EventFactory.createImageStateEvent( "Image Finished", EventTypes.IMAGE, ImageStates.WAITING_FOR_MERGE, receivedSplitImage);
//        this.notify(eventWaitingForMerge);
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
