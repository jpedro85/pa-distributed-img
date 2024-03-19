package Network.Client;

import Network.Server.Response;
import Utils.Image.SplitImage;
import Network.Server.LoadTrackerReader;
import Network.Server.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

// TODO: Documentation
// TODO: Implement Unit tests;
public class SlaveClient extends Thread {

    private LoadTrackerReader serverLoadTrackerReader;
    private SplitImage splitImage;

    public SlaveClient(SplitImage splitImage) {
        this.splitImage = splitImage;
    }

    @Override
    public void run() {
        // TODO: Implement LoadTrackerReader
        // TODO: Pass the config from the loadconfig.
        // sendRequestAndReceiveResponse(host, port, request);
        System.out.println("Not Implemented Yet");
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
            System.out.println("Connecting to server at " + host + ":" + port);
            System.out.println("Sending: " + request);
            out.writeObject(request);

            // Wait for and return the response from the server
            Response response = (Response) in.readObject();
            System.out.println("Received: " + response);
            return response;

        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
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
}
