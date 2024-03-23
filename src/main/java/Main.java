import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import Network.Client.*;
import Network.Server.*;
import UI.MainForm;
import Utils.Image.*;
import Utils.Parser.Config;
import Utils.Parser.ConfigParser;

public class Main {

    private static ServerLoadTracker serverLoadTracker;
    private static MainForm mainForm;

    private static JFrame mainFrame;

    public static void main(String[] args) throws IOException {

        Config config = ConfigParser.getInstance().parseFromIniToConfig("config.ini");
        intitializeServerLoadTracker();
        ServersHandler serversHandler = new ServersHandler(config,serverLoadTracker);
        ClientsHandler clientsHandler = new ClientsHandler(config,serverLoadTracker);

        createAndShowGUI(serversHandler,clientsHandler,config);

        serverLoadTracker.addObserver(mainForm);
        mainFrame.setVisible(true);

        serversHandler.startConfigServer();


    }

    private static void createAndShowGUI(ServersHandler serversHandler,ClientsHandler clientsHandler,Config config)
    {
        mainFrame = new JFrame("Main Form");
        mainForm = new MainForm(serversHandler,clientsHandler,config);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().add(mainForm);

        // Obtém as dimensões da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Define o tamanho mínimo da janela com base nas dimensões da tela
        int minWidth = Math.min(1440, screenWidth);
        int minHeight = Math.min(1080, screenHeight);
        mainFrame.setMinimumSize(new Dimension(minWidth, minHeight));

        mainFrame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                serversHandler.closeAllServers();
                System.out.println("Closing Servers ... ");
                System.out.println("Shutting Down ... ");
                try {
                    Files.delete( Paths.get("./load_info.temp") );
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        mainFrame.pack();
    }


    private static void intitializeServerLoadTracker()
    {
        serverLoadTracker = ServerLoadTracker.getInstance();
        ServerLoadTracker.getInstance().setFilePath("load_info.temp");
    }
}