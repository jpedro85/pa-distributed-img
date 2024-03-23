package Network.Server;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.SeverityLevels;
import Utils.Events.Event;
import Utils.Events.InterfaceEvents.InterfaceEvent;
import Utils.Events.InterfaceEvents.InterfaceEventWithName;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.Parser.Config;
import Utils.Events.EventFactory;
import Utils.VarSync;

import java.util.ArrayList;

/**
 * Manages the creation, removal, and monitoring of servers within the system.
 * This class acts as a Subject in the Observer design pattern and also serves
 * as an Observer to receive events from other components.
 * <p>
 * Emits:
 * - Server events
 * - Errors Events
 */
public class ServersHandler implements Subject, Observer {

    private final ArrayList<Observer> OBSERVERS;
    private final ArrayList<Server> SERVERS;
    private final Config CONFIG;
    private final LoadTrackerEdit LOADTRACKEREDIT;
    private final VarSync<Integer> nextAvailablePort ;

    /**
     * Constructs a new ServersHandler object.
     *
     * @param config Configuration settings for servers.
     * @param loadTrackerEdit LoadTrackerEdit instance for load tracking.
     */
    public ServersHandler( Config config, LoadTrackerEdit loadTrackerEdit)
    {
        this.OBSERVERS = new ArrayList<Observer>();
        this.SERVERS = new ArrayList<Server>();
        this.CONFIG = config;
        this.LOADTRACKEREDIT = loadTrackerEdit;
        this.nextAvailablePort = new VarSync<>( this.CONFIG.getStartPort() );

        //startConfigServer();
    }

    /**
     * Starts default servers
     */
    public void startConfigServer(){
        for (int i = 0; i < this.CONFIG.getServerAmount() ; i++) {
            this.addServer( "Server " + i);
        }
    }

    /**
     * Create and starts a new server.
     *
     * @param serverName The name of the server to create.
     */
    public void addServer(String serverName)
    {
        this.nextAvailablePort.lock();
       //int numberOfServers = ( this.nextAvailablePort.asyncGet() ) - this.CONFIG.getStartPort();
        if ( this.SERVERS.size() + 1 <= CONFIG.getMaxServersNumber() )
        {
            Server newServer = new Server( serverName, this.nextAvailablePort.asyncGet(), CONFIG.getTaskPoolSize(), LOADTRACKEREDIT);
            this.SERVERS.add( newServer );
            newServer.addObserver(this);
            newServer.start();
            this.nextAvailablePort.asyncSet( this.nextAvailablePort.asyncGet() + 1 );


            this.nextAvailablePort.unlock();

            return;
        }
        this.nextAvailablePort.unlock();

        this.notify(
                EventFactory.createErrorEvent( String.format("Cannot add a new server max servers numbers reached. %d ",
                        this.CONFIG.getMaxServersNumber() ),EventTypes.ERROR, SeverityLevels.ERROR )
        );

    }

    /**
     * Stops and remover the last added server.
     */
    public void removeLastServer()
    {
        this.nextAvailablePort.lock();
        if (this.SERVERS.size() -1 >= CONFIG.getServerAmount() )
        {
            Server serverToRemove = this.SERVERS.remove(this.SERVERS.size()-1);
            serverToRemove.close();
            serverToRemove.removeObserver(this);
            this.nextAvailablePort.asyncSet( serverToRemove.getPort() );
            this.nextAvailablePort.unlock();
        }
        else{
            this.nextAvailablePort.unlock();
            this.notify(
                    EventFactory.createErrorEvent( String.format( "Cannot remove more servers config minimum is %d", this.CONFIG.getServerAmount() ),
                            EventTypes.ERROR, SeverityLevels.WARNING )
            );
        }

    }

    public void addObserver(Observer observer) {

        if ( !this.OBSERVERS.contains(observer) )
            this.OBSERVERS.add(observer);
    }

    /**
     * Shuts down all servers managed by this handler.
     */
    public void closeAllServers()
    {
        for ( Server server : this.SERVERS)
        {
            server.close();
        }
    }

    /**
     * @return The number of current servers
     */
    public int getNUmberOfSevers() {

        int size;
        this.nextAvailablePort.lock();
        size = SERVERS.size();
        nextAvailablePort.unlock();
        return size;
    }

    @Override
    public void removeObserver(Observer observer)
    {
        this.OBSERVERS.remove(observer);
    }

    @Override
    public void notify(Event event)
    {
        for ( Observer observer : this.OBSERVERS)
        {
            observer.update(this, event);
        }
    }

    @Override
    public void update(Subject subject, Event event) {

        if ( event.getType() == EventTypes.SERVER || event.getType() == EventTypes.ERROR)
        {
            // propagate server events.
            this.notify(event);
        }
        else if ( event.getType() == EventTypes.INTERFACE )
        {
            switch ( ((InterfaceEvent)event).getEvent() ) {

                case ADD_SERVER -> this.addServer( ((InterfaceEventWithName)event).getName() );

                case REMOVE_SERVER -> this.removeLastServer();

                case CLOSING_INTERFACE -> this.closeAllServers();

                default -> throw new IllegalArgumentException("ServersHandler only supports {ADD_SERVER,REMOVE_SERVER,CLOSING_INTERFACE, ALL Server EVENTS } not " + event.getType() );
            }
        }
        else
            throw new IllegalArgumentException( "ServersHandler Invalid event received Event: " + event.getMessage() );

    }
}
