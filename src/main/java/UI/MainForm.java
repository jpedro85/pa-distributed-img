package UI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*;
import java.util.Map;
import java.util.HashMap;


public class MainForm extends JPanel {
    private JPanel panel1;
    private JButton BrowseBtn;
    private JButton addTabButton;
    private JButton removeTabButton;
    private JTextField ImagePath;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JPanel panel2;
    private JLabel lableImage;
    private JLabel currentTabLabel;
    private Map<JPanel, String> tabLastSelectedPath = new HashMap<>();


    public MainForm() {
        initComponents();
    }

    private void BrowseBtnActionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fnwf = new FileNameExtensionFilter("PNG JPG and JPEG", "png", "jpeg", "jpg");
        fileChooser.addChoosableFileFilter(fnwf);
        int load = fileChooser.showOpenDialog(null);
        if (load == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            String path = f.getAbsolutePath();
            ImagePath.setText(path);
            ImageIcon ii = new ImageIcon(path);
            Image img = ii.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lableImage.setIcon(new ImageIcon(img));
            // Atualiza a JLabel da aba atual com a imagem selecionada
            currentTabLabel.setIcon(new ImageIcon(img));
            // Atualiza o último caminho de arquivo selecionado para a aba atual
            JPanel currentTabPanel = (JPanel) tabbedPane1.getSelectedComponent();
            tabLastSelectedPath.put(currentTabPanel, path);
        }
    }

    private void initComponents() {
        // Component initialization
        lableImage = new JLabel();
        BrowseBtn = new JButton("Browse");
        ImagePath = new JTextField();
        tabbedPane1 = new JTabbedPane();
        addTabButton = new JButton("Add Tab");
        removeTabButton = new JButton("Remove Last Tab");

        // Set layout for MainForm
        setLayout(new BorderLayout());

        // Add components to a panel for BrowseBtn and ImagePath
        JPanel browsePanel = new JPanel();
        browsePanel.add(BrowseBtn);
        browsePanel.add(ImagePath);

        // Add BrowseBtn and ImagePath panel to the MainForm at the top
        add(browsePanel, BorderLayout.NORTH);

        // Add tabbedPane1 to the center of the MainForm
        add(tabbedPane1, BorderLayout.CENTER);

        // Add addTabButton and removeTabButton to the MainForm at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTabButton);
        buttonPanel.add(removeTabButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Attach action listener to BrowseBtn
        BrowseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseBtnActionPerformed(e);
            }
        });

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

        // Attach change listener to tabbedPane1 to clear ImagePath when a new tab is selected
        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ImagePath.setText(""); // Clear ImagePath when a new tab is selected
            }
        });
        // Adiciona um ChangeListener ao tabbedPane1 para monitorar mudanças de aba
        // Adiciona um ChangeListener ao tabbedPane1 para monitorar mudanças de aba
        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Obtém o painel da aba selecionada
                JPanel selectedTabPanel = (JPanel) tabbedPane1.getSelectedComponent();

                // Verifica se há um último caminho de arquivo associado a este painel
                if (tabLastSelectedPath.containsKey(selectedTabPanel)) {
                    // Obtém o último caminho de arquivo associado a este painel
                    String lastSelectedPath = tabLastSelectedPath.get(selectedTabPanel);

                    // Define o último caminho de arquivo como texto no campo ImagePath
                    ImagePath.setText(lastSelectedPath);
                } else {
                    // Se não houver um último caminho de arquivo associado a este painel, limpa o campo ImagePath
                    ImagePath.setText("");
                }
            }
        });


        // Add the original tab with lableImage to tabbedPane1
        JPanel originalPanel = new JPanel(new BorderLayout());
        originalPanel.add(lableImage, BorderLayout.CENTER);
        tabbedPane1.addTab("Original Tab", originalPanel);
    }

    // Method to add a new tab
    private void addNewTab() {
        JPanel newPanel = new JPanel(new BorderLayout());
        JLabel newLabel = new JLabel("New Tab Content");
        newPanel.add(newLabel, BorderLayout.CENTER);
        tabbedPane1.addTab("New Tab", newPanel);
        currentTabLabel = newLabel; // Atualiza a referência da JLabel da aba atual
    }

    // Method to remove the last tab
    private void removeLastTab() {
        int tabCount = tabbedPane1.getTabCount();
        if (tabCount > 1) { // Check if there is more than one tab (keep at least one tab)
            tabbedPane1.removeTabAt(tabCount - 1); // Remove the last tab
        }
    }
}

