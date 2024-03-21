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

public class ClientTab extends JPanel implements Observer, Subject{

    private JTabbedPane tabbedPane;
    private JLabel imageLabel;

    public ClientTab(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza a imagem na JLabel
        add(imageLabel, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close Tab");
        add(closeButton, BorderLayout.NORTH);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeTab();
            }
        });
        // Botão "Start" para iniciar uma ação relacionada à imagem
        JButton startButton = new JButton("Start");

    }
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

            // Define a imagem na JLabel
            imageLabel.setIcon(icon);
            // Cria o botão "Start"
            JButton startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Lógica para a ação "Start" aqui
                }
            });

            // Painel para conter o botão "Start"
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(startButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    private JLabel getImageLabel() {
        if (getComponentCount() > 0) {
            Component component = getComponent(0);
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getComponentCount() > 0) {
                    Component innerComponent = panel.getComponent(0);
                    if (innerComponent instanceof JLabel) {
                        return (JLabel) innerComponent;
                    }
                }
            }
        }
        return null;
    }
    private void closeTab() {
        int tabIndex = tabbedPane.indexOfComponent(this);
        if (tabIndex != -1) {
            tabbedPane.removeTabAt(tabIndex);
        }
    }
    private void startButtonActionPerformed() {
        // Lógica para a ação do botão "Start"
        // Implemente aqui o que deseja fazer ao clicar no botão "Start"
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
