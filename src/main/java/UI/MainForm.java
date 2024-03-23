package UI;
import Network.Client.ClientsHandler;
import Network.Server.ServerLoadTracker;
import Network.Server.ServersHandler;
import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.InterfaceEvents;
import Utils.Events.Enums.ServerStates;
import Utils.Events.Enums.SeverityLevels;
import Utils.Events.ErrorEvent;
import Utils.Events.EventFactory;
import Utils.Events.InterfaceEvents.LoadUpdateEvent;
import Utils.Events.ServerEvent;
import Utils.Image.ImageReader;
import Utils.Observer.Observer;
import Utils.Events.Event;
import Utils.Observer.Subject;
import Utils.Parser.Config;
import Utils.VarSync;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;


/**
 * The MainForm class represents the main user interface panel for managing servers and clients.
 * It consists of a tabbed pane for managing client tabs, a menu panel on the left containing
 * buttons for server management, a server information text area, and a table displaying server
 * status. This class provides functionalities for adding/removing servers, starting all servers,
 * browsing in client tabs, and updating the total count of servers.
 */
public class MainForm extends JPanel implements Observer, Subject {
    private JTabbedPane tabbedPane; // Tabbed pane for managing client tabs
    private JTextPane serversNotfifycations; // Text area for displaying server information
    private JLabel totalServersLabel; // Label for displaying the total count of servers

    private DefaultTableModel tableModel;

    private Observer serversHandler;
    private Observer clientsHandler;

    private enum StylesTypes {
        ERROR,
        WARNING,
        NORMAL
    }

    private Config config;

    private int totalServers = 0;

    public final VarSync<ArrayList<ClientTab> > poped;

    /**
     * Constructs a MainForm object by initializing its components.
     */
    public MainForm(ServersHandler serversHandler, ClientsHandler clientsHandler, Config config)
    {
        this.serversHandler = serversHandler;
        this.clientsHandler = clientsHandler;
        this.config = config;
        this.poped = new VarSync<>(new ArrayList<>());
        serversHandler.addObserver(this);
        initComponents();
    }

    /**
     * Initializes the components of the MainForm.
     * This method sets up the layout, adds components to the MainForm,
     * attaches action listeners to buttons, and initializes the default tab.
     */
    private void initComponents() {
        // Component initialization
        tabbedPane = new JTabbedPane();

        // Set layout for MainForm
        setLayout(new BorderLayout());

        // Add tabbedPane to the MainForm at the center
        add(tabbedPane, BorderLayout.CENTER);

        // Add addTabButton to the MainForm at the bottom
        JButton addTabButton = new JButton("Add Tab");
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(addTabButton, BorderLayout.LINE_END);
        add(bottomPanel, BorderLayout.SOUTH);

        // Create menu panel with buttons and server info text area
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JButton addServerButton = new JButton("Add Server");
        JButton removeServerButton = new JButton("Remove Server");
        JButton startAllButton = new JButton("Start All");
        JButton browseButton = new JButton("Load");

        // Set minimum size for buttons
        addServerButton.setMinimumSize(new Dimension(150, 30));
        removeServerButton.setMinimumSize(new Dimension(150, 30));
        startAllButton.setMinimumSize(new Dimension(150, 30));
        browseButton.setMinimumSize(new Dimension(150, 30));

        // Add action listeners to buttons
        addServerButton.addActionListener( e ->
        {

            SwingUtilities.invokeLater( () -> {
                this.notifyAddServer();
            });

        });

        removeServerButton.addActionListener( e -> {  SwingUtilities.invokeLater( () -> { this.notifyRemoveServer(); }); } );

        startAllButton.addActionListener( e -> {  SwingUtilities.invokeLater( () -> { this.notifyStarAll(); });    });

        browseButton.addActionListener( e -> {  SwingUtilities.invokeLater( () -> browseActionPerformed()  ); } );

        // Create server info text area
        serversNotfifycations = new JTextPane();
        StyleConstants.setForeground(serversNotfifycations.addStyle( StylesTypes.ERROR.toString() ,null),Color.RED);
        StyleConstants.setForeground(serversNotfifycations.addStyle( StylesTypes.WARNING.toString(),null),new Color(255,128,0));
        StyleConstants.setForeground(serversNotfifycations.addStyle( StylesTypes.NORMAL.toString(),null), Color.BLACK);

        serversNotfifycations.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(serversNotfifycations){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 300);
            }
        };
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create table model and table
        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Server", "Status", "Waiting", "Running"}
        );
        JTable serverTable = new JTable(tableModel);

        // Add buttons to a horizontal panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
        buttonPanel.add(addServerButton);
        buttonPanel.add(removeServerButton);
        buttonPanel.add(startAllButton);
        buttonPanel.add(browseButton);

        // Add table to the menu panel
        menuPanel.add(new JScrollPane(serverTable));

        // Create label to show total servers count
        totalServersLabel = new JLabel("Total Servers: " + tableModel.getRowCount());

        // Add buttons and server info text area to menu panel
        menuPanel.add(buttonPanel);
        menuPanel.add(totalServersLabel);
        menuPanel.add(scrollPane);

        // Add menu panel to the MainForm at the left
        add(menuPanel, BorderLayout.WEST);

    }


    /**
     * Updates the total count of servers displayed in the label.
     *
     * @param tableModel The table model containing server data.
     */
    private void updateTotalServersCount(DefaultTableModel tableModel) {
        totalServersLabel.setText("Total Servers: " + tableModel.getRowCount());
    }

    /**
     * Performs the action when the "Browse" button is clicked.
     * It retrieves the selected client tab and invokes its browse action.
     */
    private void browseActionPerformed () {

        File fileResult = this.browse();
        if ( fileResult != null )
        {
            this.notifyLoadedImage(fileResult, new ClientTab(tabbedPane,fileResult,this,config.getColumns(),config.getRows()));
        }
        else
        {
            try
            {
                StyledDocument text = serversNotfifycations.getStyledDocument();
                text.insertString( text.getLength(),"Error:", serversNotfifycations.getStyle(StylesTypes.ERROR.toString()) );
                text.insertString( text.getLength(),"Invalid image\n", serversNotfifycations.getStyle(StylesTypes.NORMAL.toString()) );
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
            }
        }

    }



    private File browse() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }else
            return null;
    }


    private void notifyLoadedImage(File file, ClientTab tab)
    {
        ImageReader.readImage(file.getAbsolutePath());
        Event event = EventFactory.createLoadedImageEvent("New Loaded Image", EventTypes.INTERFACE , file.getName(),  ImageReader.readImage(file.getAbsolutePath()) );
        this.clientsHandler.update( tab, event);
    }

    private void notifyStarAll()
    {

        poped.lock();

        String[] names = new String[tabbedPane.getTabCount() + poped.asyncGet().size() ];
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            names[i] =  tabbedPane.getTitleAt(i);

            ClientTab tab = (ClientTab)((JPanel)tabbedPane.getComponentAt(i));
            tab.startButton.setEnabled(false);
            tab.startButton.setVisible(false);

        }


        for (int i = 0; i < poped.asyncGet().size(); i++)
        {
            ClientTab tab = poped.asyncGet().get(i);
            names[i] =  tab.getName();
            tab.startButton.setEnabled(false);
            tab.startButton.setVisible(false);
        }


        Event event = EventFactory.createInterfaceEventWithNames("New Loaded Image", EventTypes.INTERFACE , InterfaceEvents.START_ALL, names );
        this.clientsHandler.update( this, event);

        poped.unlock();
    }

    private void notifyAddServer()
    {
        Event event = EventFactory.createInterfaceEventWithName("Add new server", EventTypes.INTERFACE, InterfaceEvents.ADD_SERVER, "Server");
        this.serversHandler.update( this, event);
    }

    private void notifyRemoveServer()
    {
        Event event = EventFactory.createInterfaceEventWithName("Remove last server", EventTypes.INTERFACE, InterfaceEvents.REMOVE_SERVER, "Server");
        this.serversHandler.update( this, event);
    }


    public void startClient(String name)
    {
        Event event = EventFactory.createInterfaceEventWithName("start", EventTypes.INTERFACE, InterfaceEvents.START, name);
        this.clientsHandler.update( this, event);
    }

    public void cancelClient(String name)
    {
        Event event = EventFactory.createInterfaceEventWithName("cancel", EventTypes.INTERFACE, InterfaceEvents.CANCEL, name);
        this.clientsHandler.update( this, event);
    }

    @Override
    public void update(Subject subject, Event event) {


        switch (event.getType())
        {
            case SERVER -> { updateServerEvents( (ServerEvent) event); }

            case LOAD_UPDATE -> { updateLoadServerEvent( (LoadUpdateEvent) event); }

            case ERROR -> { updateHandleErrors( (ErrorEvent) event); }

            default -> {
                throw new InvalidParameterException("Main form update only receives. types permitted SERVER and ERROR ");
            }
        }
    }

    private void  updateServerEvents(ServerEvent event)
    {
        try {
            StyledDocument text = serversNotfifycations.getStyledDocument();

            switch (event.getServerState()) {
                case STARTING -> {

                    Object[] row = { event.getServerIdentifier() , "STARTING", 0, 0};
                    tableModel.addRow( row );
                    text.insertString( text.getLength(),"Warning:", serversNotfifycations.getStyle(StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength()," server:" + event.getServerIdentifier() + " is ", serversNotfifycations.getStyle(StylesTypes.NORMAL.toString()) );
                    text.insertString( text.getLength(),"Starting\n", serversNotfifycations.getStyle(StylesTypes.WARNING.toString()) );

                    totalServers++;
                    totalServersLabel.setText("Total Servers: " + totalServers);
                }

                case RUNNING -> {

                    tableModel.setValueAt("Running", getIndexOfServer( event.getServerIdentifier() ), 1);
                    text.insertString( text.getLength(),"Warning:", serversNotfifycations.getStyle(StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength()," server:" + event.getServerIdentifier() + " is now ", serversNotfifycations.getStyle(StylesTypes.NORMAL.toString()) );
                    text.insertString( text.getLength(),"Running\n", serversNotfifycations.getStyle(StylesTypes.WARNING.toString()) );
                }

                case CLOSED -> {

                    tableModel.removeRow( getIndexOfServer(event.getServerIdentifier()));

                    text.insertString( text.getLength(),"Warning:", serversNotfifycations.getStyle(StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength()," server:" + event.getServerIdentifier() + " is now ", serversNotfifycations.getStyle(StylesTypes.NORMAL.toString()) );
                    text.insertString( text.getLength(),"Closed\n", serversNotfifycations.getStyle(StylesTypes.WARNING.toString()) );

                    totalServers--;
                    totalServersLabel.setText("Total Servers: " + totalServers);
                }

                case UPDATE -> {
                    System.out.println("UPDATE " + event.getServerIdentifier());
                }
            }

        }catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    private void updateLoadServerEvent(LoadUpdateEvent event)
    {
        int row = getIndexOfServer( event.getID() );
        tableModel.setValueAt( event.getWAITING() , row, 2);
        tableModel.setValueAt( event.getRUNNING() , row, 3);
    }


    private int getIndexOfServer(int id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int currentId = (int) tableModel.getValueAt(i, 0) ;
            if (currentId == id) {
                return i;
            }
        }
        return -1;
    }

    private void updateHandleErrors(ErrorEvent event){
        try
        {
            StyledDocument text = serversNotfifycations.getStyledDocument();

            if( event.getSeverityLevel() == SeverityLevels.ERROR)
                text.insertString( text.getLength(),"Error:", serversNotfifycations.getStyle(StylesTypes.ERROR.toString()) );
            else
                text.insertString( text.getLength(),"Warning:", serversNotfifycations.getStyle(StylesTypes.WARNING.toString()) );

            text.insertString( text.getLength(),event.getMessage() + "\n", serversNotfifycations.getStyle(StylesTypes.NORMAL.toString()) );

        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notify(Event event) {

    }
}
