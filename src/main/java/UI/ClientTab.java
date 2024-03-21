package UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.Events.Event;

/**
 * The ClientTab class represents a tab for managing client-related actions.
 * It allows users to browse and display images, and perform actions related to the displayed image.
 * This class also implements the Observer and Subject interfaces for observing and notifying events.
 */
public class ClientTab extends JPanel implements Observer, Subject {

    private JTabbedPane tabbedPane; // Reference to the parent tabbed pane
    private JLabel imageLabel; // Label for displaying the image

    /**
     * Constructs a ClientTab object.
     *
     * @param tabbedPane The parent JTabbedPane where the tab will be added.
     */
    public ClientTab(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        initComponents();
    }

    /**
     * Initializes the components of the ClientTab.
     * This method sets up the layout and adds components such as image label and buttons.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the image in the JLabel
        add(imageLabel, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close Tab");
        add(closeButton, BorderLayout.NORTH);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeTab();
            }
        });

        // Button "Start" to perform an action related to the image
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButtonActionPerformed();
            }
        });

        // Browse button to select an image
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButtonActionPerformed();
            }
        });

        // Panel to contain the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(browseButton);
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Performs the action when the "Browse" button is clicked.
     * It allows the user to select an image file and displays it in the image label.
     */
    public void browseButtonActionPerformed() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);

            // Set the image in the JLabel
            imageLabel.setIcon(icon);
        }
    }

    /**
     * Closes the current tab.
     */
    private void closeTab() {
        int tabIndex = tabbedPane.indexOfComponent(this);
        if (tabIndex != -1) {
            tabbedPane.removeTabAt(tabIndex);
        }
    }

    /**
     * Performs the action when the "Start" button is clicked.
     * Implement the desired action here.
     */
    private void startButtonActionPerformed() {
        // Logic for the "Start" button action
        // Implement what you want to do when the "Start" button is clicked
    }

    @Override
    public void addObserver(Observer observer) {
        System.out.println("Not implemented yet");
    }

    @Override
    public void removeObserver(Observer observer) {
        System.out.println("Not implemented yet");
    }

    @Override
    public void notify(Event event) {
        System.out.println("Not implemented yet");
    }

    @Override
    public void update(Subject subject, Event event) {
        System.out.println("Not implemented yet");
    }
}
