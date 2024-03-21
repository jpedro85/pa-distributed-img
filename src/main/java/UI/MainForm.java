package UI;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainForm extends JPanel {
    private JTabbedPane tabbedPane;
    private JTextArea serverInfoTextArea;

    public MainForm() {
        initComponents();
    }

    private void initComponents() {
        // Component initialization
        tabbedPane = new JTabbedPane();
        JButton addTabButton = new JButton("Add Tab");

        // Set layout for MainForm
        setLayout(new BorderLayout());

        // Add tabbedPane to the MainForm at the center
        add(tabbedPane, BorderLayout.CENTER);

        // Add addTabButton to the MainForm at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTabButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Attach action listener to addTabButton
        addTabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTab();
            }
        });

        // Create menu panel with buttons and server info text area
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JButton addServerButton = new JButton("Add Server");
        JButton removeServerButton = new JButton("Remove Server");
        JButton startAllButton = new JButton("Start All");
        JButton browseButton = new JButton("Browse");

        // Add action listeners to buttons (you can customize these as needed)
        addServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add Server logic here
            }
        });

        removeServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove Server logic here
            }
        });

        startAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start All logic here
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseActionPerformed();
            }
        });

        // Create server info text area
        serverInfoTextArea = new JTextArea(5, 20); // 5 rows, 20 columns
        serverInfoTextArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(serverInfoTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add buttons and server info text area to menu panel
        menuPanel.add(addServerButton);
        menuPanel.add(removeServerButton);
        menuPanel.add(startAllButton);
        menuPanel.add(browseButton);
        menuPanel.add(scrollPane); // Add the scroll pane containing the text area

        // Add menu panel to the MainForm at the left
        add(menuPanel, BorderLayout.WEST);

        // Initialize ClientTab
        addNewTab(); // Add a default tab
    }

    // Método para adicionar uma nova aba
    private void addNewTab() {
        tabbedPane.addTab("New Client", new ClientTab(tabbedPane));
    }

    // Método chamado quando o botão "Browse" é clicado
    private void browseActionPerformed() {
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex != -1) { // Verifica se uma aba está selecionada
            Component selectedComponent = tabbedPane.getComponentAt(selectedTabIndex);
            if (selectedComponent instanceof ClientTab) {
                ClientTab clientTab = (ClientTab) selectedComponent;
                clientTab.browseButtonActionPerformed();
            }
        }
    }
}
