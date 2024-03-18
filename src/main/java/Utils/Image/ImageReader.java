import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The ImageReader class implements a file reader. The path of the file is specified as an argument of each method.
 */
public class ImageReader {

    /**
     * @param path the path of the image to be read
     *
     * @return a BufferedImage containing the decoded contents of the path, or null
     */
    public static BufferedImage readImage ( String path ) {
        BufferedImage result = null;
        try {
            result = ImageIO.read ( new File ( path ) );
        } catch ( IOException e ) {
            e.printStackTrace ( );
        }
        return result;
    }
}
