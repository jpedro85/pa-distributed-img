package Network.Server;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.ServerStates;
import Utils.Events.Enums.SeverityLevels;
import Utils.Events.Event;
import Utils.Events.EventFactory;
import Utils.Image.ImageTransformer;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.TaskPool;
import Utils.VarSync;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A TCP/IP server that listens for connections on a specified port and handles each client connection in a separate thread.
 * <p>
 * This class emits several events to notify observers about its state and actions.
 * </p>
 * <p>
 * The following events are emitted:
 * <ul>
 *     <li>Server Starting Event: Signifies the initiation of the server's operation.</li>
 *     <li>Server Running Event: Indicates that the server is actively accepting client connections.</li>
 *     <li>Server Closed Event: Signifies the closure of the server and the termination of client connections.</li>
 *     <li>Error Event: Indicates occurrences of errors during the server's operation.</li>
 * </ul>
 * </p>
 */
public class Server extends Thread implements Subject {

    private final ArrayList<Observer> observers;
    private final int port;
    private final TaskPool taskPool;
    private final VarSync<Boolean> isOpen;
    private ServerSocket socket;
    private LoadTrackerEdit loadTrackerEdit;

    /**
     * Constructs a new Server instance.
     *
     * @param port            The port number on which the server will listen for incoming connections.
     * @param capacity        The capacity of the task pool for managing client connections.
     * @param loadTrackerEdit The load tracker for monitoring server load.
     */
    public Server (String name, int port, int capacity, LoadTrackerEdit loadTrackerEdit)
    {
        this.setName( name );
        this.port = port;
        this.taskPool = new TaskPool( capacity );
        this.isOpen = new VarSync<Boolean>(false);
        this.observers = new ArrayList<>();
    }

    /**
     * Starts the server thread. Only if its current state is paused, otherwise has no effect.
     */
    @Override
    public void start()
    {
        this.isOpen.lock();
        if ( !this.isOpen.syncGet() )
        {
            this.isOpen.asyncSet(true);
            this.isOpen.unlock();
            this.loadTrackerEdit.addEntry(this.port, 0, 0);
            super.start();
        }
        this.isOpen.unlock();
    }

    /**
     * The entry point of the server thread. Starts the server to accept and handle client connections.
     */
    @Override
    public void run ( )
    {
        try
        {
            this.startServer ( );

            this.notify( EventFactory.createServerEvent( String.format("Serve %s is Running", this.getName()), EventTypes.SERVER, ServerStates.RUNNING, this.port ));

            while(true)
            {
                Socket clientSocket = this.socket.accept ( );
                this.taskPool.addTask( new ServerClientHandler ( clientSocket, this ) );
                this.loadTrackerEdit.update(this.port, this.taskPool.getNumberOfRunningTasks(), this.taskPool.getNumberOfWaitingTasks());
            }

        } catch ( Exception e )
        {
            this.notify( EventFactory.createErrorEvent( e.getMessage(), EventTypes.ERROR, SeverityLevels.ERROR ) );
        }
    }

    /**
     * Closes the server, waits for current running tasks in the taskPool finishes.
     */
    public void close()
    {
        this.isOpen.lock();
        if ( this.isOpen.asyncGet() )
        {
            this.isOpen.asyncSet(false);
            this.taskPool.pause();


            // finish the current running task
            while ( !this.taskPool.isPaused() );

            try
            {
                this.socket.close();
            }
            catch (IOException e) {} ;

            this.loadTrackerEdit.removeEntry(this.port);
            this.notify( EventFactory.createServerEvent( String.format("Serve %s is CLOSED", this.getName()), EventTypes.SERVER, ServerStates.CLOSED, this.port ));
        }
        this.isOpen.unlock();
    }

    /**
     * Initializes the server socket and the TaskPool.
     *
     * @throws IOException If an I/O error occurs when opening the socket.
     */
    private void startServer ( ) throws IOException {

        this.socket = new ServerSocket ( port );
        this.taskPool.start();

        this.notify( EventFactory.createServerEvent( String.format("Serve %s is starting", this.getName()), EventTypes.SERVER, ServerStates.STARTING, this.port ));
    }

    @Override
    public void addObserver(Observer observer)
    {
        if ( !this.observers.contains(observer) )
            this.observers.add(observer);

    }

    @Override
    public void removeObserver(Observer observer)
    {
        this.observers.remove(observer);
    }

    @Override
    public void notify(Event event)
    {
        for ( Observer observer : this.observers )
        {
            observer.update(this, event);
        }
    }


    /**
     * Handles client connections. Reads objects from the client, processes them, and sends a response back.
     */
    private static class ServerClientHandler implements Runnable {

        private final Socket clientSocket;
        private final Server server;

        /**
         * Constructs a new ClientHandler instance.
         *
         * @param socket The client socket.
         */
        public ServerClientHandler ( Socket socket, Server server)
        {
            this.clientSocket = socket;
            this.server = server;
        }

        /**
         * The entry point of the client handler thread. Manages input and output streams for communication with the
         * client.
         */
        @Override
        public void run ( )
        {
            try ( ObjectOutputStream out = new ObjectOutputStream ( clientSocket.getOutputStream ( ) ) ;
                  ObjectInputStream in = new ObjectInputStream ( clientSocket.getInputStream ( ) ) ) {

                Request request;
                while ( ( request = (Request)in.readObject() ) != null ) {
                    out.writeObject ( handleRequest( request ) );
                }

            } catch ( EOFException e )  { /*finished clients requests*/ }
            catch ( IOException | ClassNotFoundException e )
            {
               server.notify( EventFactory.createErrorEvent("A error occored when handling a Client",EventTypes.ERROR, SeverityLevels.ERROR) );

            }
            finally {
                try
                {
                    clientSocket.close ( );
                }
                catch ( IOException e )
                {
                    server.notify( EventFactory.createErrorEvent("Error closing client socket: " + e.getMessage ( ), EventTypes.ERROR, SeverityLevels.ERROR) );
                }
            }
        }

        /**
         * Processes the client's request and generates a response.
         *
         * @param request The object received from the client.
         *
         * @return The response object to be sent back to the client.
         */
        private Response handleRequest ( Request request )
        {
            BufferedImage editedImage = ImageTransformer.convertToGrayScale( ImageTransformer.createImageFromBytes(request.getImageSection()) );
            Response response = new Response ( "OK" , request.getMessageContent() ,editedImage);

            server.loadTrackerEdit.update(server.port, server.taskPool.getNumberOfRunningTasks(), server.taskPool.getNumberOfWaitingTasks());

            return response;
        }

    }

}