import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import Network.Client.*;
import Network.Server.*;
import UI.MainForm;
import Utils.Image.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Main Form");
        MainForm mainForm = new MainForm();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainForm);

        // Obtém as dimensões da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Define o tamanho mínimo da janela com base nas dimensões da tela
        int minWidth = Math.min(1440, screenWidth);
        int minHeight = Math.min(1080, screenHeight);
        frame.setMinimumSize(new Dimension(minWidth, minHeight));

        frame.pack();
        frame.setVisible(true);
    }
}

// BufferedImage sampleImage = ImageReader.readImage("sample.png");
// //Java Swing stuff
// JFrame frame = new JFrame ( "pa-distributed-img" );
// frame.setSize ( 400 , 400 );
// JPanel panel = new JPanel ( );
// ImageIcon icon = new ImageIcon ( sampleImage );
// JLabel label = new JLabel ( icon );
// panel.add ( label );
//
// JButton button = new JButton ( );
// button.setText ( "Remove red" );
// panel.add ( button );
//
//
// button.addActionListener( e -> {
// Client client = new Client ( "Client A" );
// Request request = new Request ( "greeting" , "Hello, Server!",sampleImage );
// Response response = client.sendRequestAndReceiveResponse ( "localhost" , 8888
// , request );
// icon.setImage(ImageTransformer.createImageFromBytes(response.getImageSection()));
// panel.repaint();
// });
//
// frame.add ( panel );
// frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
// frame.pack ( );
// frame.setVisible ( true );
