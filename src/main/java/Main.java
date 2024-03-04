import javax.swing.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main ( String[] args ) {

        Server server = new Server ( 8888 );
        server.start ( );
        BufferedImage sampleImage = ImageReader.readImage("sample.png");
        //Java Swing stuff
        JFrame frame = new JFrame ( "pa-distributed-img" );
        frame.setSize ( 400 , 400 );
        JPanel panel = new JPanel ( );
        ImageIcon icon = new ImageIcon ( sampleImage );
        JLabel label = new JLabel ( icon );
        panel.add ( label );

        JButton button = new JButton ( );
        button.setText ( "Remove red" );
        panel.add ( button );


        button.addActionListener( e -> {
            Client client = new Client ( "Client A" );
            Request request = new Request ( "greeting" , "Hello, Server!",sampleImage );
            Response response = client.sendRequestAndReceiveResponse ( "localhost" , 8888 , request );
            icon.setImage(ImageTransformer.createImageFromBytes(response.getImageSection()));
            panel.repaint();
        });

        frame.add ( panel );
        frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        frame.pack ( );
        frame.setVisible ( true );


    }

}