package UI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * The MainForm class represents the main user interface panel for managing servers and clients.
 * It consists of a tabbed pane for managing client tabs, a menu panel on the left containing
 * buttons for server management, a server information text area, and a table displaying server
 * status. This class provides functionalities for adding/removing servers, starting all servers,
 * browsing in client tabs, and updating the total count of servers.
 */
public class MainForm extends JPanel {
    private JTabbedPane tabbedPane; // Tabbed pane for managing client tabs
    private JTextArea serverInfoTextArea; // Text area for displaying server information
    private JLabel totalServersLabel; // Label for displaying the total count of servers

    /**
     * Constructs a MainForm object by initializing its components.
     */
    public MainForm() {
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

        // Attach action listener to addTabButton
        addTabButton.addActionListener(e -> addNewTab());

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
        addServerButton.addActionListener(e -> {
            // Add Server logic here
        });

        removeServerButton.addActionListener(e -> {
            // Remove Server logic here
        });

        startAllButton.addActionListener(e -> {
            // Start All logic here
        });

        browseButton.addActionListener(e -> browseActionPerformed());

        // Create server info text area
        serverInfoTextArea = new JTextArea(5, 20);
        serverInfoTextArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(serverInfoTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create table model and table
        DefaultTableModel tableModel = new DefaultTableModel(
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

        // Initialize ClientTab
        addNewTab(); // Add a default tab
    }

    /**
     * Adds a new client tab to the tabbed pane.
     */
    private void addNewTab() {
        tabbedPane.addTab("New Client", new ClientTab(tabbedPane));
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
    private void browseActionPerformed() {
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex != -1) { // Check if a tab is selected
            Component selectedComponent = tabbedPane.getComponentAt(selectedTabIndex);
            if (selectedComponent instanceof ClientTab) {
                ClientTab clientTab = (ClientTab) selectedComponent;
                clientTab.browseButtonActionPerformed();
            }
        }
    }
}
