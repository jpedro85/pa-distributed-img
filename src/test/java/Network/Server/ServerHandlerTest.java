package Network.Server;

import Utils.Events.Enums.InterfaceEvents;
import Utils.Events.Event;
import Utils.Events.Enums.EventTypes;
import Utils.Events.EventFactory;
import Utils.Events.MockObserver;
import Utils.Events.MockSubject;
import Utils.Parser.Config;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ServerHandlerTest {

    private ServersHandler serversHandler;
    private Config config;
    private LoadTrackerEdit loadTrackerEdit;
    private MockObserver mockObserver;

    void creatConfig()
    {
        config = new Config();
        config.setMaxServersNumber(3);
        config.setStartPort(8888);
        config.setServerAmount(2);
        config.setColumns(2);
        config.setRows(2);
        config.setTaskPoolSize(3);
    }

    @BeforeEach
    void setUp()
    {
        creatConfig();
        loadTrackerEdit = ServerLoadTracker.getInstance();
        ServerLoadTracker.getInstance().setFilePath("load_infoTestHandler.temp");

        serversHandler = new ServersHandler(config, loadTrackerEdit);
        mockObserver = new MockObserver();
        serversHandler.addObserver(mockObserver);

    }

    @AfterEach
    void clearObserver(){
        mockObserver.getEvents().clear();
        serversHandler.closeAllServers();
    }

    @Test
    @DisplayName("Testing add server")
    void testAddServer() {
        serversHandler.addServer("TestServer");
        assertEquals(3, serversHandler.getNUmberOfSevers());
    }

    @Test
    @DisplayName("Testing remove server")
    void testRemoveLastServer() {

        serversHandler.removeLastServer();

        for (Event e : mockObserver.getEvents()){
            if (e.getType() == EventTypes.ERROR)
                assertEquals("Cannot remove more servers config minimum is 2", e.getMessage());
        }

        serversHandler.addServer("TestServer2");
        assertEquals(3, serversHandler.getNUmberOfSevers());

        serversHandler.removeLastServer();
        assertEquals( 2 , this.serversHandler.getNUmberOfSevers() );

    }

    @Test
    @DisplayName("Closing all server")
    void testCloseAllServers() {
        serversHandler.closeAllServers();

        int closedCount = 0;
        for (Event event: mockObserver.getEvents()){

            if( event.getMessage().equals("Serve Server 0 is CLOSED") || event.getMessage().equals("Serve Server 1 is CLOSED") )
                closedCount++;
        }

        assertEquals(2,closedCount);
    }

    @Test
    @DisplayName("testing notification")
    void testNotify() {
        this.serversHandler.addServer("B");
        assertFalse( mockObserver.getEvents().isEmpty());
    }

    @Test
    @DisplayName("testing update")
    void testUpdate()
    {

        MockSubject subject =new MockSubject();
        subject.addObserver(serversHandler);

        subject.notify( EventFactory.createInterfaceEventWithName( "add new server",EventTypes.INTERFACE,InterfaceEvents.ADD_SERVER,"Server ADD1" ));
        assertEquals(3, serversHandler.getNUmberOfSevers());

        subject.notify( EventFactory.createInterfaceEventWithName( "add new server",EventTypes.INTERFACE,InterfaceEvents.REMOVE_SERVER,"a" ));
        assertEquals(2, serversHandler.getNUmberOfSevers());

        subject.notify( EventFactory.createInterfaceEventWithName( "add new server",EventTypes.INTERFACE,InterfaceEvents.CLOSING_INTERFACE,"a" ));
        assertEquals(2, serversHandler.getNUmberOfSevers());

        int closedCount = 0;
        for (Event event: mockObserver.getEvents()){

            if( event.getMessage().equals("Serve Server 0 is CLOSED") || event.getMessage().equals("Serve Server 1 is CLOSED") )
                closedCount++;
        }

    }

    @AfterAll
    static void cleanUp() throws IOException {

        Files.delete( Paths.get("load_infoTestHandler.temp") );
    }
}

