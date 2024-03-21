package Network.Client;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Event;
import Utils.Events.InterfaceEvents.InterfaceEvent;
import Utils.Events.InterfaceEvents.InterfaceEventWithName;
import Utils.Events.InterfaceEvents.InterfaceEventWithNames;
import Utils.Events.InterfaceEvents.LoadedImageEvent;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.VarSync;
import Utils.Parser.Config;
import Network.Server.LoadTrackerReader;

import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.ArrayList;


/**
 * The ClientHandler class manages the creation, removal, and control of MasterClient instances,
 * enabling parallel processing of images from multiple clients. It acts as an observer to receive
 * notifications from observed subjects regarding interface events, and accordingly triggers actions
 * such as creating new clients, starting or cancelling processing, and removing clients.
 * <p>
 * Key Features:
 * - Dynamic Client Management: Allows the creation and removal of MasterClient instances based on interface events,
 *   enabling flexible handling of client requests and efficient resource management.
 * - Event-Driven Processing: Listens for interface events such as LOADED_IMAGE, START, CANCEL, etc., to initiate
 *   appropriate actions, ensuring responsive and synchronized image processing across clients.
 * - Parallel Processing Support: Facilitates concurrent processing of images from multiple clients by managing
 *   individual MasterClient instances, thereby maximizing processing throughput and resource utilization.
 * - Configuration Flexibility: Integrates with configuration settings to adjust parameters such as image segmentation
 *   dimensions and server load tracking, providing customizable options to optimize processing performance.
 */
public class ClientHandler implements Observer {

    private final VarSync< ArrayList<MasterClient> > clients;

    private final Config config;

    private final LoadTrackerReader loadTrackerReader;

    /**
     * Constructs a ClientHandler object with the specified configuration and load tracker reader.
     *
     * @param config            The configuration for image processing.
     * @param loadTrackerReader The reader for load tracking information.
     */
    public ClientHandler(Config config,LoadTrackerReader loadTrackerReader)
    {
        this.clients = new VarSync< ArrayList<MasterClient> >( new ArrayList<MasterClient>(1) );
        this.config = config;
        this.loadTrackerReader = loadTrackerReader;
    }

    /**
     * Creates a new MasterClient instance with the provided client name and image.
     *
     * @param clientName The name of the client.
     * @param image      The image to be processed.
     */
    public void createNewClient(ClienTab tab, String clientName , BufferedImage image )
    {
        this.clients.lock();
        MasterClient masterClient = new MasterClient( clientName, image, this.config.getRows(), this.config.getColumns(), this.loadTrackerReader, config.getSavePath());

        // establish event communication
        masterClient.addObserver(tab);
        tab.addObserver(masterClient);

        this.clients.asyncGet().add( masterClient );
        this.clients.unlock();
    }

    /**
     * Removes a MasterClient instance with the specified client name.
     *
     * @param clientName The name of the client to be removed.
     */
    public void removeClient( String clientName )
    {
        this.clients.lock();
        for ( MasterClient masterClient  : this.clients.asyncGet())
        {
            if ( masterClient.getName().equals(clientName) )
            {
                if ( masterClient.isAlive() ) masterClient.cancel();
                this.clients.asyncGet().remove(masterClient);
                break;
            }
        }
        this.clients.unlock();
    }

    /**
     * Removes multiple MasterClient instances with the specified client names.
     *
     * @param clientNames The names of the clients to be removed.
     */
    public void removeClients( String[] clientNames )
    {
        this.clients.lock();
        for ( MasterClient masterClient  : this.clients.asyncGet())
        {
            for (String clientName : clientNames )
            {
                if ( masterClient.getName().equals(clientName) )
                {
                    if ( masterClient.isAlive() ) masterClient.cancel();
                    this.clients.asyncGet().remove(masterClient);
                    break;
                }
            }
        }
        this.clients.unlock();
    }

    /**
     * Starts processing for multiple MasterClient instances with the specified client names.
     *
     * @param clientNames The names of the clients to start processing.
     */
    public void startClients( String[] clientNames )
    {
        this.clients.lock();
        for ( MasterClient masterClient  : this.clients.asyncGet())
        {
            for ( String clientName : clientNames )
            {
                if ( masterClient.getName().equals(clientName) && !masterClient.isAlive() )
                {
                    masterClient.start();
                    break;
                }
            }

        }
        this.clients.unlock();
    }

    /**
     * Starts processing for a MasterClient instance with the specified client name.
     *
     * @param clientName The name of the client to start processing.
     */
    public void startClient( String clientName )
    {
        this.clients.lock();
        for ( MasterClient masterClient  : this.clients.asyncGet())
        {
            if ( masterClient.getName().equals(clientName) && !masterClient.isAlive() )
            {
                masterClient.start();
                break;
            }
        }
        this.clients.unlock();
    }

    /**
     * Cancels processing for a MasterClient instance with the specified client name.
     *
     * @param clientName The name of the client to cancel processing.
     */
    public void cancelClient( String clientName )
    {
        this.clients.lock();
        for ( MasterClient masterClient  : this.clients.asyncGet())
        {
            if (masterClient.getName().equals(clientName) )
            {
                masterClient.cancel();
            }
        }
        this.clients.unlock();
    }

    /**
     * Cancels processing for multiple MasterClient instances with the specified client names.
     *
     * @param clientNames The names of the clients to cancel processing.
     */
    public void cancelClients( String[] clientNames )
    {
        this.clients.lock();
        for ( MasterClient masterClient  : this.clients.asyncGet())
        {
            for ( String clientName : clientNames )
            {
                if ( masterClient.isAlive() && masterClient.getName().equals(clientName) ) {
                    masterClient.cancel();
                }
            }

        }
        this.clients.unlock();
    }

    /**
     * Updates the ClientHandler based on notifications from observed subjects.
     * Expected InterfaceEvents include LOADED_IMAGE, START, CANCEL, START_ALL, CANCEL_ALL, UNLOADED_IMAGE, and CLOSING_INTERFACE.
     *
     * @param subject The subject being observed.
     * @param event   The event notification received.
     * @throws InvalidParameterException If the event type is not recognized, or if the subject is not of the expected type for certain events.
     */
    @Override
    public void update(Subject subject, Event event)
    {
        if ( event.getType() == EventTypes.INTERFACE)
        {
            InterfaceEvent iEvent = ((InterfaceEvent)event);
            switch ( iEvent.getEvent() )
            {
                case LOADED_IMAGE -> {

                    if( subject.getClass() == ClienTab.class )
                        this.createNewClient( (ClienTab) subject, ((LoadedImageEvent)iEvent).getName() , ((LoadedImageEvent)iEvent).getImage() );
                    else
                        throw new InvalidParameterException( "InterfaceEvent " + ((InterfaceEvent)event).getEvent().toString() );
                }

                case START -> this.startClient( ((InterfaceEventWithName)iEvent).getName() );

                case CANCEL -> this.cancelClient( ((InterfaceEventWithName)iEvent).getName() );

                case START_ALL -> this.startClients( ((InterfaceEventWithNames)iEvent).getNames() );

                case CANCEL_ALL -> this.cancelClients( ((InterfaceEventWithNames)iEvent).getNames() );

                case UNLOADED_IMAGE -> this.removeClient( ((InterfaceEventWithName)iEvent).getName() );

                case CLOSING_INTERFACE -> this.removeClients( ((InterfaceEventWithNames)iEvent).getNames() );

                default -> throw new InvalidParameterException( "InterfaceEvent " + ((InterfaceEvent)event).getEvent().toString() );
            }

        } else {
             throw new InvalidParameterException("");
        }
    }

}
