package UI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainForm extends JPanel {
    private JButton addTabButton;
    private JButton removeTabButton;
    private JTabbedPane tabbedPane;
    private JTextArea serverInfoTextArea; // Nova caixa de texto para informações dos servidores

    public MainForm() {
        initComponents();
    }

    private void initComponents() {
        // Component initialization
        tabbedPane = new JTabbedPane();
        addTabButton = new JButton("Add Tab");
        removeTabButton = new JButton("Remove Last Tab");

        // Set layout for MainForm
        setLayout(new BorderLayout());

        // Add tabbedPane to the MainForm at the center
        add(tabbedPane, BorderLayout.CENTER);

        // Add addTabButton and removeTabButton to the MainForm at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTabButton);
        buttonPanel.add(removeTabButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Attach action listener to addTabButton
        addTabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTab();
            }
        });

        // Attach action listener to removeTabButton
        removeTabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeLastTab();
            }
        });

        // Create menu panel with buttons and server info text area
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JButton addServerButton = new JButton("Add Server");
        JButton removeServerButton = new JButton("Remove Server");
        JButton startAllButton = new JButton("Start All");

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

        // Create server info text area
        serverInfoTextArea = new JTextArea(5, 20); // 5 rows, 20 columns
        serverInfoTextArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(serverInfoTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add buttons and server info text area to menu panel
        menuPanel.add(addServerButton);
        menuPanel.add(removeServerButton);
        menuPanel.add(startAllButton);
        menuPanel.add(scrollPane); // Add the scroll pane containing the text area

        // Add menu panel to the MainForm at the left
        add(menuPanel, BorderLayout.WEST);
    }

    // Método para adicionar uma nova aba
    private void addNewTab() {
        JPanel newPanel = new JPanel(new BorderLayout());
        JButton newBrowseBtn = new JButton("Browse");
        newPanel.add(newBrowseBtn, BorderLayout.NORTH);
        tabbedPane.addTab("New Client", newPanel);
        newBrowseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseBtnActionPerformed(e, newPanel);
            }
        });
    }

    // Método para remover a última aba
    private void removeLastTab() {
        int tabCount = tabbedPane.getTabCount();
        if (tabCount > 0) {
            tabbedPane.removeTabAt(tabCount - 1);
        }
    }

    // Método chamado quando o botão "Browse" é clicado
    private void BrowseBtnActionPerformed(ActionEvent e, JPanel selectedTabPanel) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fnwf = new FileNameExtensionFilter("PNG JPG and JPEG", "png", "jpeg", "jpg");
        fileChooser.addChoosableFileFilter(fnwf);
        int load = fileChooser.showOpenDialog(null);
        if (load == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            String path = f.getAbsolutePath();
            ImageIcon icon = new ImageIcon(path);
            Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Resize the image
            icon = new ImageIcon(image);
            JLabel imageLabel = new JLabel(icon);

            // Cria um botão "Start"
            JButton startButton = new JButton("Start");

            // Cria uma nova barra de progresso
            JProgressBar progressBar = new JProgressBar();
            progressBar.setStringPainted(true); // Exibe a porcentagem na barra de progresso
            progressBar.setMaximum(100); // Define o máximo da barra de progresso

            // Adiciona a ação ao botão "Start"
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startButtonActionPerformed(e, progressBar);
                }
            });

            // Adiciona os componentes ao painel
            JPanel controlPanel = new JPanel();
            controlPanel.add(startButton);
            controlPanel.add(progressBar);

            selectedTabPanel.removeAll();
            selectedTabPanel.setLayout(new BorderLayout());
            selectedTabPanel.add(imageLabel, BorderLayout.CENTER);
            selectedTabPanel.add(controlPanel, BorderLayout.SOUTH);
            selectedTabPanel.revalidate();
            selectedTabPanel.repaint();
        }
    }

    // Método chamado quando o botão "Start" é clicado
    private void startButtonActionPerformed(ActionEvent e, JProgressBar progressBar) {
        Timer timer = new Timer(100, new ActionListener() {
            int progress = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 5; // Incrementa o progresso em 5 a cada 100 milissegundos
                if (progress > 100) {
                    ((Timer) e.getSource()).stop(); // Para o timer quando o progresso atinge 100%
                }
                progressBar.setValue(progress); // Define o valor da barra de progresso
            }
        });
        timer.start(); // Inicia o timer
    }
}
