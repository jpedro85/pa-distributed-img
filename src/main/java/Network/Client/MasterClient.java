package Network.Client;

import Network.Server.LoadTrackerReader;
import Utils.Image.ImageTransformer;
import Utils.Image.SplitImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// TODO: Documentation
public class MasterClient extends Thread {

    private String clientName;
    private LoadTrackerReader loadTrackerReader;
    private BufferedImage originalImage;
    private BufferedImage[][] splittedOriginalImage;
    private List<SlaveClient> slaveClientsList;

    public MasterClient(String name) {
        this.clientName = name;
        this.slaveClientsList = new ArrayList<>();
    }

    @Override
    public void run() {

        int imageHeight = originalImage.getHeight();
        int imageWidth = originalImage.getWidth();
        this.splittedOriginalImage = ImageTransformer.splitImage(originalImage, imageHeight, imageWidth);

        for (short line = 0; line < imageHeight; line++) {
            for (short column = 0; column < imageWidth; column++) {
                BufferedImage currentImageSegment = splittedOriginalImage[line][column];
                SplitImage splitImage = new SplitImage(column, line, currentImageSegment);
                SlaveClient slaveClient = new SlaveClient(splitImage);
                slaveClientsList.add(slaveClient);
                slaveClient.start();
            }
        }

        for (int i = 0; i < slaveClientsList.size(); i++) {
            try {
                slaveClientsList.get(i).join();
            } catch (Exception e) {
                System.out.println("One of the SlaveClient threads were interrupted");
            }
        }

    }

    public String getClientName() {
        return this.clientName;
    }

    public LoadTrackerReader getLoadTrackerReader() {
        return this.loadTrackerReader;
    }
}
