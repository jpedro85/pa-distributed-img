import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A simple TCP/IP client that connects to a server, sends an object, and receives a response.
 */
public class Client {

    // The name of the client
    private final String name;

    /**
     * Constructs a new client with the specified name.
     *
     * @param name The name of the client.
     */
    public Client ( String name ) {
        this.name = name;
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
    public Response sendRequestAndReceiveResponse ( String host , int port , Request request ) {
        try ( Socket socket = new Socket ( host , port ) ) {

            // Create and initialize the streams for sending and receiving objects
            ObjectOutputStream out = new ObjectOutputStream ( socket.getOutputStream ( ) );
            ObjectInputStream in = new ObjectInputStream ( socket.getInputStream ( ) );

            // Send the request to the server
            System.out.println ( "Connecting to server at " + host + ":" + port );
            System.out.println ( "Sending: " + request );
            out.writeObject ( request );

            // Wait for and return the response from the server
            Response response = ( Response ) in.readObject ( );
            System.out.println ( "Received: " + response );
            return response;

        } catch ( UnknownHostException e ) {
            System.err.println ( "Server not found: " + e.getMessage ( ) );
        } catch ( IOException e ) {
            System.err.println ( "I/O Error: " + e.getMessage ( ) );
        } catch ( ClassNotFoundException e ) {
            System.err.println ( "Class not found: " + e.getMessage ( ) );
        }
        // Return null or consider a better error handling/return strategy
        return null;
    }

    /**
     * Returns the name of the client.
     *
     * @return The name of the client.
     */
    public String getName ( ) {
        return name;
    }

}