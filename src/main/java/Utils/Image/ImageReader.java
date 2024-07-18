package Utils.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The ImageReader class provides static methods for reading images from disk.
 * It utilizes the {@link javax.imageio.ImageIO} class for decoding images from
 * various formats. This class is focused on simplicity and ease of use for
 * reading images into {@link java.awt.image.BufferedImage} objects.
 */
public class ImageReader {

    private ImageReader() {
    }

    /**
     * Reads an image from the specified path and returns it as a
     * {@link BufferedImage}. If the image cannot be read due to an
     * {@link IOException} (e.g., file not found, unsupported format), the exception
     * is caught, its stack trace is printed, and {@code null} is returned.
     *
     * @param path The path of the image file to be read. This path should be
     *             absolute relative to the application's working directory.
     * @return A {@link BufferedImage} containing the decoded contents of the image
     *         file, or {@code null} if the image cannot be read due to an I/O error
     *         or unsupported format.
     */
    public static BufferedImage readImage(String path) {
        BufferedImage result = null;
        try {
            result = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
