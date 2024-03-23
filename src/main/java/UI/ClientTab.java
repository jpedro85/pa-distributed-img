package UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import Utils.Events.Enums.SeverityLevels;
import Utils.Events.ErrorEvent;
import Utils.Events.ImageStateEvent;
import Utils.Image.ImageReader;
import Utils.Image.ImageTransformer;
import Utils.Image.SplitImage;
import Utils.Observer.Observer;
import Utils.Observer.Subject;
import Utils.Events.Event;


public class ClientTab extends JPanel implements Observer , Subject{

    private JTabbedPane tabbedPane; // Reference to the parent tabbed pane
    private JLabel imageLabel; // Label for displaying the image
    private JFrame floatingFrame; // Reference to the floating frame

    private JTextPane infoText;

    private MainForm parent;

    private BufferedImage buferedImage;

    private enum StylesTypes {
        ERROR,
        WARNING,
        NORMAL
    }

    private ImageIcon defaultIcon;
    private ImageIcon waitingIcon;
    private ImageIcon processingIcon;

    private ImageIcon[][] icons;

    private int columns;
    private int rows;

    private JPanel processingImage;

    private JPanel midel;

    public JButton startButton;

    private JButton closeButton;

    /**
     * Constructs a ClientTab object.
     *
     * @param tabbedPane The parent JTabbedPane where the tab will be added.
     */
    public ClientTab(JTabbedPane tabbedPane,File file,MainForm parent, int columns, int rows) {
        icons = new ImageIcon[rows][columns];
        this.tabbedPane = tabbedPane;
        initComponents();
        updateImage(file);
        this.columns = columns;
        this.rows = rows;
        initIcons();
        tabbedPane.addTab( file.getName(),this);
        this.setName(file.getName());
        this.parent = parent;
    }

    private double w_ratio;
    private double h_ratio;


    private void initIcons(){

        w_ratio = buferedImage.getWidth() / 300.0;
        h_ratio = buferedImage.getHeight() / 300.0;
        int width =  (int)((this.buferedImage.getWidth()/columns) / w_ratio) ;
        int height = (int)((this.buferedImage.getHeight()/rows) / h_ratio) ;

        BufferedImage blankImage = new BufferedImage( width , height , BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = blankImage.createGraphics();

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, width, height);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(1, 1, width-2, height-2);

        g2d.dispose();

        blankImage.getScaledInstance(width,height,Image.SCALE_SMOOTH);

        defaultIcon = new ImageIcon(blankImage);


        BufferedImage blankImage2 = new BufferedImage( width , height , BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d1 = blankImage2.createGraphics();

        g2d.setColor(Color.GREEN);
        g2d.drawRect(0, 0, width, height);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(1, 1, width-2, height-2);

        g2d1.dispose();

        blankImage2.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        waitingIcon = new ImageIcon(blankImage2);


        BufferedImage blankImage3 = new BufferedImage( width , height , BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d2 = blankImage3.createGraphics();

        g2d.setColor(Color.MAGENTA);
        g2d.drawRect(0, 0, width, height);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(1, 1, width-2, height-2);

        g2d2.dispose();
        blankImage3.getScaledInstance(width,height,Image.SCALE_SMOOTH);

        processingIcon = new ImageIcon(blankImage3);

        for (int l = 0; l < rows ; l++) {
            for (int c = 0; c < columns; c++) {
                this.icons[l][c] = defaultIcon;
            }
        }

        processingImage = new JPanel();
        processingImage.setLayout(new GridLayout(rows, columns, 0, 0));
        processingImage.setSize( new Dimension( 300 , 300 ));

//        int n_images =  rows*columns;
//        for (int i = 0; i < n_images ; i++) {
//
//            JLabel label = new JLabel(defaultIcon);
//            processingImage.add(label);
//        }

        for (int l = 0; l < rows ; l++) {
            for (int c = 0; c < columns; c++) {
                JLabel label = new JLabel( icons[l][c]) ;
                processingImage.add(label);
            }
        }

        midel.add(processingImage,BorderLayout.CENTER);

    }

    /**
     * Initializes the components of the ClientTab.
     * This method sets up the layout and adds components such as image label and buttons.
     */
    private void initComponents() {

        midel = new JPanel();
        setLayout(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the image in the JLabel
        //add(imageLabel, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close Tab");
        this.closeButton = closeButton;
        add(closeButton, BorderLayout.NORTH);

        midel.add(imageLabel,BorderLayout.CENTER);
        add(midel,BorderLayout.CENTER);

        closeButton.addActionListener( e -> {
            SwingUtilities.invokeLater( () ->
                    { this.parent.cancelClient(this.getName());
                    closeTab();
                    }
            );
        } );

        // Button "Start" to perform an action related to the image
        JButton startButton = new JButton("Start");
        this.startButton = startButton;

        startButton.addActionListener(  e -> {  SwingUtilities.invokeLater( () -> {
            this.parent.startClient(this.getName());
            this.startButton.setEnabled(false);
            this.startButton.setVisible(false);
        }); }  );

        // Button to pop the tab into a floating frame
        JButton popButton = new JButton("Pop");

        // Button to integrate a floating frame back into a tab
        JButton integrateButton = new JButton("Integrate");
        popButton.addActionListener( e -> {

            SwingUtilities.invokeLater( () -> {
                popTab();
                popButton.setEnabled(false);
                integrateButton.setEnabled(true);
                this.parent.poped.lock();
                this.parent.poped.asyncGet().add(this);
                this.parent.poped.unlock();
                closeButton.setVisible(false);
            });

        } );


        integrateButton.addActionListener(e -> {

            SwingUtilities.invokeLater( () -> {
                integrateTab();
                popButton.setEnabled(true);
                integrateButton.setEnabled(false);
                this.parent.poped.lock();
                this.parent.poped.asyncGet().remove(this);
                this.parent.poped.unlock();
                closeButton.setVisible(true);
            });

        } );

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        btnPanel.add(startButton);
        btnPanel.add(popButton);
        btnPanel.add(integrateButton);
        bottomPanel.add(btnPanel, BorderLayout.NORTH);

        // Text area for displaying client information
        infoText = new JTextPane();
        StyleConstants.setForeground(infoText.addStyle( ClientTab.StylesTypes.ERROR.toString() ,null),Color.RED);
        StyleConstants.setForeground(infoText.addStyle( ClientTab.StylesTypes.WARNING.toString(),null),new Color(255,128,0));
        StyleConstants.setForeground(infoText.addStyle( ClientTab.StylesTypes.NORMAL.toString(),null), Color.BLACK);
        infoText.setEditable(false); // Make it read-only


        JScrollPane scrollPane = new JScrollPane(infoText){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 150);
            }
        };
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);



        add(bottomPanel, BorderLayout.SOUTH);

    }

    /**
     * Performs the action when the "Browse" button is clicked.
     * It allows the user to select an image file and displays it in the image label.
     */
//    public void browseButtonActionPerformed() {
//        JFileChooser fileChooser = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
//        fileChooser.setFileFilter(filter);
//        int returnVal = fileChooser.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
//            Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
//            icon = new ImageIcon(image);
//
//            // Set the image in the JLabel
//            imageLabel.setIcon(icon);
//        }
//    }

    private void updateImage(File file){

        buferedImage = ImageReader.readImage(file.getAbsolutePath());
        ImageIcon icon = new ImageIcon( buferedImage );
        Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);

        // Set the image in the JLabel
        imageLabel.setIcon(icon);
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

    /**
     * Pops the tab into a floating frame.
     */
    private void popTab() {
        if (tabbedPane != null) {
            floatingFrame = new JFrame("Floating Client Tab");
            floatingFrame.getContentPane().add(this);
            floatingFrame.setSize(300, 300);
            floatingFrame.setLocationRelativeTo(null);
            floatingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            floatingFrame.setSize(400, 300);
            floatingFrame.setVisible(true);

            floatingFrame.addWindowListener( new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    integrateTab();
                    closeButton.setVisible(true);
                    closeButton.setEnabled(true);

                }
            });

            // Remove the tab from the tabbed pane
            tabbedPane.remove(this);
        }
    }

    /**
     * Integrates the tab back into the parent tabbed pane from a floating frame.
     */
    private void integrateTab() {
        if (floatingFrame != null) {
            // Add the tab back to the tabbed pane
            tabbedPane.addTab("Client Tab", this);

            // Close the floating frame
            floatingFrame.dispose();
        }
    }

    @Override
    public void update(Subject subject, Event event) {

        switch ( event.getType() )
        {
            case ERROR -> { this.updateHandleErrors( (ErrorEvent) event); }

            case IMAGE -> { this.updateHandleImage( (ImageStateEvent) event); }
        }

    }

    private void updateHandleImage(ImageStateEvent event)
    {
        try
        {
            StyledDocument text = infoText.getStyledDocument();
            switch (event.getImageState())
            {
                case WAITING_FOR_PROCESSING -> {
                    String msg = String.format("Part L:%d C:%d \n", ((ImageStateEvent) event).getSplitImage().getLineNumber() , ((ImageStateEvent) event).getSplitImage().getColumnNumber() );
                    text.insertString( text.getLength(), "Waiting For Processing: ", infoText.getStyle(ClientTab.StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength(), msg , infoText.getStyle(ClientTab.StylesTypes.NORMAL.toString()) );

                    updateHandleImage(((ImageStateEvent) event).getSplitImage().getLineNumber(),((ImageStateEvent) event).getSplitImage().getColumnNumber(), ((ImageStateEvent) event).getSplitImage() );
                }

                case WAITING_FOR_MERGE -> {

                    String msg = String.format("Part L:%d C:%d \n", ((ImageStateEvent) event).getSplitImage().getLineNumber() , ((ImageStateEvent) event).getSplitImage().getColumnNumber() );
                    text.insertString( text.getLength(), "Waiting For Merge: ", infoText.getStyle(ClientTab.StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength(), msg , infoText.getStyle(ClientTab.StylesTypes.NORMAL.toString()) );

                    updateHandleImage(((ImageStateEvent) event).getSplitImage().getLineNumber(),((ImageStateEvent) event).getSplitImage().getColumnNumber(), ((ImageStateEvent) event).getSplitImage()  );
                }

                case PREPARED_FOR_PROCESSING -> {
                    text.insertString( text.getLength(),"Divided:", infoText.getStyle(ClientTab.StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength(), event.getMessage() + "\n" , infoText.getStyle(ClientTab.StylesTypes.NORMAL.toString()) );
                }

                case MERGED -> {
                    text.insertString( text.getLength(),"Merged:", infoText.getStyle(ClientTab.StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength(), event.getMessage() + "\n" , infoText.getStyle(ClientTab.StylesTypes.NORMAL.toString()) );
                }

                case SAVED -> {
                    text.insertString( text.getLength(),"SAVE:", infoText.getStyle(ClientTab.StylesTypes.WARNING.toString()) );
                    text.insertString( text.getLength(), event.getMessage() + "\n", infoText.getStyle(ClientTab.StylesTypes.NORMAL.toString()) );
                }
            }

        }catch (BadLocationException e)
        {
            e.printStackTrace();
        }

    }

    private void updateHandleImage(int row,int column, ImageIcon imageIcon )
    {

        SwingUtilities.invokeLater( () -> {

            icons[row][column] = imageIcon;
            this.processingImage.removeAll();

            for (int l = 0; l < rows ; l++) {
                for (int c = 0; c < columns; c++) {
                    JLabel label = new JLabel( icons[l][c]) ;
                    processingImage.add(label);
                }
            }

            processingImage.revalidate();
            processingImage.repaint();

        });
    }

    private void updateHandleImage(int row,int column, SplitImage splitImage )
    {
        this.updateHandleImage(row,column,new ImageIcon( splitImage.getImage().getScaledInstance((int)(splitImage.getImage().getWidth()/w_ratio),(int)(splitImage.getImage().getHeight()/h_ratio) ,Image.SCALE_SMOOTH) ) );
    }

    private void updateHandleErrors(ErrorEvent event){
        try
        {
            StyledDocument text = infoText.getStyledDocument();

            if( event.getSeverityLevel() == SeverityLevels.ERROR)
                text.insertString( text.getLength(),"Error:", infoText.getStyle(ClientTab.StylesTypes.ERROR.toString()) );
            else
                text.insertString( text.getLength(),"Warning:", infoText.getStyle(ClientTab.StylesTypes.WARNING.toString()) );

            text.insertString( text.getLength(),event.getMessage() + "\n", infoText.getStyle(ClientTab.StylesTypes.NORMAL.toString()) );

        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        System.out.println("Not implemented ");
    }

    @Override
    public void removeObserver(Observer observer) {
        System.out.println("Not implemented ");
    }

    @Override
    public void notify(Event event) {
        System.out.println("Not implemented ");
    }
}
