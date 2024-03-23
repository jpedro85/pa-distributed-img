package Network.Clients;

import Network.Client.ClientsHandler;
import Network.Server.Server;
import Network.Server.ServerLoadTracker;
import UI.ClientTab;
import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.InterfaceEvents;
import Utils.Events.EventFactory;
import Utils.Events.MockSubject;
import Utils.Image.ImageReader;
import Utils.Image.ImageTransformer;
import Utils.Observer.Observer;
import Utils.Parser.Config;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientHandlersTest {

    private static final ServerLoadTracker SERVER_LOAD_TRACKER = ServerLoadTracker.getInstance();
    private static final Config CONFIG = new Config();
    private static final String PATH = "load_info_ClientHandlersTest.temp";
    private static final Server SERVER = new Server("TEST_SERVER",8888,2,SERVER_LOAD_TRACKER);

    private BufferedImage image;

    @Mock
    private ClientTab tab;

    @BeforeAll
    static void startUp()
    {
        ServerLoadTracker.getInstance().setFilePath( PATH );
        startConfig();
        SERVER.start();
    }

    @AfterAll
    static void cleanUp() throws IOException {
        Files.delete( Paths.get(PATH) );
        Files.delete( Paths.get("src","results","Client1_edited.png") );
        SERVER.start();
    }

    private static void startConfig()
    {
        CONFIG.setMaxServersNumber(3);
        CONFIG.setStartPort(8888);
        CONFIG.setServerAmount(2);
        CONFIG.setColumns(2);
        CONFIG.setRows(2);
        CONFIG.setTaskPoolSize(3);
    }

    @BeforeEach
    void prepare(){
        image = ImageReader.readImage("sample.png");
        tab = mock(ClientTab.class);
    }

    public void addObs(Observer obs){

    }

    @AfterEach
    void clean(){

    }

    @Test
    @DisplayName("Test creat  new Client")
    void addNewClientTest()
    {
        ClientsHandler myClientsHandler = new ClientsHandler(CONFIG,SERVER_LOAD_TRACKER);
        myClientsHandler.createNewClient(tab,"Client 1", image);
        assertEquals(1,myClientsHandler.getNumberOfClients());
        myClientsHandler.createNewClient(tab,"Client 2", image);
        assertEquals(2,myClientsHandler.getNumberOfClients());
    }

    @Test
    @DisplayName("Test remove Client")
    void removeClientTest()
    {
        ClientsHandler myClientsHandler = new ClientsHandler(CONFIG,SERVER_LOAD_TRACKER);
        myClientsHandler.createNewClient(tab,"Client 1", image);
        assertEquals(1,myClientsHandler.getNumberOfClients());
        myClientsHandler.createNewClient(tab,"Client 2", image);
        assertEquals(2,myClientsHandler.getNumberOfClients());

        myClientsHandler.removeClient("Client 1");
        assertEquals(1,myClientsHandler.getNumberOfClients());
    }

    @Test
    @DisplayName("Test remove Clients")
    void removeClientsTest()
    {
        String[] clients = {"Client 1","Client 2"};
        ClientsHandler myClientsHandler = new ClientsHandler(CONFIG,SERVER_LOAD_TRACKER);
        myClientsHandler.createNewClient(tab,clients[0], image);
        assertEquals(1,myClientsHandler.getNumberOfClients());
        myClientsHandler.createNewClient(tab,clients[1], image);
        assertEquals(2,myClientsHandler.getNumberOfClients());

        myClientsHandler.removeClients( clients );
        assertEquals(0,myClientsHandler.getNumberOfClients());
    }

    @Test
    @DisplayName("Test start Client")
    void startClientTest() throws InterruptedException {

        ClientsHandler myClientsHandler = new ClientsHandler(CONFIG,SERVER_LOAD_TRACKER);
        myClientsHandler.createNewClient(tab,"Client1", image);
        assertEquals(1,myClientsHandler.getNumberOfClients());
        myClientsHandler.createNewClient(tab,"Client2", image);
        assertEquals(2,myClientsHandler.getNumberOfClients());

        myClientsHandler.startClient("Client1");

        Thread.sleep(4000);

        Path a = Paths.get("src/results/Client1_edited.png");
        assertTrue( Files.exists(a) && !Files.isDirectory(a) );
    }

    @Test
    @DisplayName("Test start Clients")
    void startClientsTest() throws InterruptedException, IOException {

        ClientsHandler myClientsHandler = new ClientsHandler(CONFIG,SERVER_LOAD_TRACKER);
        myClientsHandler.createNewClient(tab,"Client1", image);
        assertEquals(1,myClientsHandler.getNumberOfClients());
        myClientsHandler.createNewClient(tab,"Client2", image);
        assertEquals(2,myClientsHandler.getNumberOfClients());
        myClientsHandler.createNewClient(tab,"Client2", image);
        assertEquals(3,myClientsHandler.getNumberOfClients());

        String[] clients = {"Client1","Client2"};
        myClientsHandler.startClients( clients );

        Thread.sleep(15000);

        File file = new File("src/results/Client1_edited.png");
        assertTrue( file.exists() );

        file = new File("src/results/Client2_edited.png");
        assertTrue( file.exists() );

        Files.delete( Paths.get("src","results","Client2_edited.png") );
    }

    @Test
    @DisplayName("Test update(Observer)")
    void testUpdateSTART() throws InterruptedException, IOException {
        ClientsHandler myClientsHandler = new ClientsHandler(CONFIG,SERVER_LOAD_TRACKER);

        MockSubject subject = new MockSubject();
        subject.addObserver(myClientsHandler);

        myClientsHandler.createNewClient(tab,"Client1", image);
        assertEquals(1,myClientsHandler.getNumberOfClients());

        subject.notify(EventFactory.createInterfaceEventWithName("StartClient",EventTypes.INTERFACE,InterfaceEvents.START,"Client1"));
        
        Thread.sleep(15000);

        File file = new File("src/results/Client1_edited.png");
        assertTrue( file.exists() );

        subject.notify(EventFactory.createInterfaceEventWithName("StartClient",EventTypes.INTERFACE,InterfaceEvents.CANCEL,"Client1"));

    }
}
