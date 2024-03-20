package Utils.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSaver {

    private ImageSaver() {
    };

    /**
     * Saves a BufferedImage to the specified file path.
     *
     * @param image    The BufferedImage to save.
     * @param format   The format to save the image in (e.g., "png", "jpg").
     * @param filePath The file path to save the image to, including file name and
     *                 extension.
     * @return True if the image was saved successfully, false otherwise.
     */
    public static boolean saveImage(BufferedImage image, String format, String filePath) {
        try {
            File outputFile = new File(filePath);
            ImageIO.write(image, format, outputFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
