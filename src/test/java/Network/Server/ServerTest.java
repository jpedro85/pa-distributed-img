package Network.Server;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.ServerStates;
import Utils.Events.MockObserver;
import Utils.Events.Event;

import Utils.Events.ServerEvent;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {


    private Server server;
    private LoadTrackerEdit loadTraker;
    private LoadTrackerReader loadTrakerReader;

    @BeforeEach
    public void setUp() {

        loadTraker = ServerLoadTracker.getInstance();
        loadTrakerReader = ServerLoadTracker.getInstance();
        ServerLoadTracker.getInstance().setFilePath("load_info.temp");
        server = new Server("TestServer", 8888, 3, loadTraker);
    }

    @Test
    @DisplayName("testing open and close.")
    public void testServerStartsAndStopsCorrectly() throws IOException
    {
        server.start();

        assertEquals( loadTrakerReader.getServerWithLessLoad(), 8888  );

        MockClient client = new MockClient("localhost",8888);

        try {
            client.start();
        }catch ( Exception e ){
            fail("Exception occurred when connecting to server: " + e.getMessage());
        }


        server.close();

        assertEquals( -1  ,loadTrakerReader.getServerWithLessLoad() );

    }

    @Test
    @DisplayName("testing server initialization.")
    public void testServerInitialization() {

        assertNotNull(server);
        assertEquals("TestServer", server.getName());
        assertEquals(8888, server.getPort());
        assertEquals(3, server.getCapacity());

    }

    @Test
    @DisplayName("Testing executors")
    public void testExecutorsAddRemoveCapacity(){

        server.start();

        assertEquals(3,server.getCapacity() );

        server.addExecutor();

        assertEquals( 4,server.getCapacity() );

        server.removeExecutor();

        assertEquals( 3,server.getCapacity() );

        server.close();
    }

    @Test
    @DisplayName("Testing Event emission")
    public void testServerEvents()
    {
        MockObserver observer = new MockObserver();

        server.addObserver(observer);


        server.start();

        try { Thread.sleep(1000);  } catch (InterruptedException e) {}

        // Close the server
        server.close();


        // Check the events captured by the observer
        ArrayList<Event> capturedEvents = observer.getEvents();

        for ( Event  event: capturedEvents){
            System.out.print( event.getMessage() + "\n");
        }

        assertAll(
                () -> {assertEquals( EventTypes.SERVER, observer.getEvents().get(0) .getType() );},
                () -> {assertEquals( ServerStates.STARTING,  ((ServerEvent)observer.getEvents().get(0)).getServerState() );},
                () -> {assertEquals( EventTypes.SERVER, observer.getEvents().get(1) .getType() );},
                () -> {assertEquals( ServerStates.RUNNING,  ((ServerEvent)observer.getEvents().get(1)).getServerState() );},
                () -> {assertEquals( EventTypes.SERVER, observer.getEvents().get(2) .getType() );},
                () -> {assertEquals( ServerStates.CLOSED,  ((ServerEvent)observer.getEvents().get(2)).getServerState() );}
        );

    }

}
