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
    private JButton addTabButton;
    private JButton removeTabButton;
    private JTabbedPane tabbedPane;
    private Map<JPanel, ImageIcon> tabImageIcons;

    public MainForm() {
        initComponents();
    }

    private void initComponents() {
        tabImageIcons = new HashMap<>();
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
    }

    // Método para adicionar uma nova aba
    private void addNewTab() {
        JPanel newPanel = new JPanel(new BorderLayout());
        JButton newBrowseBtn = new JButton("Browse");
        newPanel.add(newBrowseBtn, BorderLayout.NORTH);
        tabbedPane.addTab("New Tab", newPanel);
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
            JLabel imageLabel = new JLabel(icon);
            selectedTabPanel.add(imageLabel, BorderLayout.CENTER);
            selectedTabPanel.revalidate();
            selectedTabPanel.repaint();
            tabImageIcons.put(selectedTabPanel, icon); // Armazenar o ícone da imagem para esta aba
        }
    }
}
