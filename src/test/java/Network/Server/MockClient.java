package Network.Server;

import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MockClient {
    private final String host;
    private final int port;

    public MockClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {

        BufferedImage image = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB );

            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Example request
            Request request = new Request("Ask","Test" ,image);
            out.writeObject(request);

            // Wait for response
            Response response = (Response) in.readObject();
            System.out.println("Server responded: " + response.getStatus());

            socket.close();

    }
}
