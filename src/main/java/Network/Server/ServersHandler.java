package Network.Server;

import UI.ClientTab;
import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.InterfaceEvents;
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

    private final ArrayList<Observer> observers;
    private final ArrayList<Server> servers;
    private final Config config;
    private final LoadTrackerEdit loadTrackerEdit;
    private final VarSync<Integer> nextAvailablePort ;

    /**
     * Constructs a new ServersHandler object.
     *
     * @param config Configuration settings for servers.
     * @param loadTrackerEdit LoadTrackerEdit instance for load tracking.
     */
    public ServersHandler( Config config, LoadTrackerEdit loadTrackerEdit)
    {
        this.observers = new ArrayList<Observer>();
        this.servers = new ArrayList<Server>();
        this.config = config;
        this.loadTrackerEdit = loadTrackerEdit;
        this.nextAvailablePort = new VarSync<>( this.config.getStartPort() );
    }

    /**
     * Create and starts a new server.
     *
     * @param serverName The name of the server to create.
     */
    public void addServer(String serverName)
    {
        this.nextAvailablePort.lock();
        if ( this.config.getStartPort() - ( this.nextAvailablePort.asyncGet() + 1 ) >= 0 )
        {
            Server newServer = new Server( serverName, this.nextAvailablePort.asyncGet(), config.getTaskPoolSize(), loadTrackerEdit);
            this.servers.add( newServer );
            newServer.start();

            this.nextAvailablePort.unlock();

            return;
        }
        this.nextAvailablePort.unlock();

        this.notify(
                EventFactory.createErrorEvent( String.format("Cannot add a new server max servers numbers reached. %d ",
                this.config.getMaxServersNumber() ),EventTypes.ERROR, SeverityLevels.ERROR )
        );

    }

    /**
     * Stops and remover the last added server.
     */
    public void removeLastServer()
    {
        this.nextAvailablePort.lock();
        if (this.servers.size() -1 >= config.getServerAmount() )
        {
            Server serverToRemove = this.servers.remove(this.servers.size()-1);
            serverToRemove.close();
            this.nextAvailablePort.asyncSet( serverToRemove.getPort() );
            this.nextAvailablePort.unlock();
        }
        else{
            this.nextAvailablePort.unlock();
            this.notify( EventFactory.createErrorEvent( String.format( "Cannot remove more servers config minium is %d", this.config.getServerAmount() ), EventTypes.ERROR, SeverityLevels.WARNING ) );
        }

    }

    public void addObserver(Observer observer) {

        if ( !this.observers.contains(observer) )
            this.observers.add(observer);
    }

    /**
     * Shuts down all servers managed by this handler.
     */
    public void closeAllServers()
    {
        for ( Server server : this.servers)
        {
            server.close();
        }
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
