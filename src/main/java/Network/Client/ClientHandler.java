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

import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.ArrayList;


public class ClientHandler implements Observer {

    private final VarSync< ArrayList<MasterClient> > clients;

    private String Config; //todo:ReplaceString type with Config
    private int nColumns = 2; //todo:ReplaceString type with Config
    private int nRows = 2; //todo:ReplaceString type with Config

    public ClientHandler() {
        this.clients = new VarSync< ArrayList<MasterClient> >( new ArrayList<MasterClient>(1) );
    }

    public void createNewClient( String clientName , BufferedImage image )
    {
        this.clients.lock();
        this.clients.asyncGet().add( new MasterClient( clientName, image, this.nColumns , this.nRows ) );
        this.clients.unlock();
    }

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

    @Override
    public void update(Subject subject, Event event)
    {
        if ( event.getType() == EventTypes.INTERFACE)
        {
            InterfaceEvent iEvent = ((InterfaceEvent)event);
            switch ( iEvent.getEvent() )
            {
                case LOADED_IMAGE -> this.createNewClient( ((LoadedImageEvent)iEvent).getName() , ((LoadedImageEvent)iEvent).getImage() );

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
