import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * The ImageTransformer class implements a set of methods for performing transformations (split, join, and red removal)
 * in a specified image.
 */
public class ImageTransformer {

    /**
     * Splits a given image in sub-images according to the number of rows and columns specified in the arguments.
     *
     * @param image    the BufferedImage containing the image
     * @param nRows    the number of rows to split the image
     * @param nColumns the number of columns to split the image
     *
     * @return a BufferedImage array containing the sub-images
     */
    public static BufferedImage[][] splitImage ( BufferedImage image , int nRows , int nColumns ) {
        if ( ( image.getHeight ( ) % nRows != 0 ) || ( image.getWidth ( ) % nColumns != 0 ) ) {
            throw new IllegalArgumentException ( "Invalid number of rows or columns" );
        }
        int subImageHeight = image.getHeight ( ) / nRows;
        int subImageWidth = image.getWidth ( ) / nColumns;
        BufferedImage[][] images = new BufferedImage[ nRows ][ nColumns ];
        int column = 0;
        for ( int i = 0 ; i < image.getWidth ( ) ; i += subImageWidth ) {
            int row = 0;
            for ( int j = 0 ; j < image.getHeight ( ) ; j += subImageHeight ) {
                images[ row ][ column ] = image.getSubimage ( i , j , subImageWidth , subImageHeight );
                row = row + 1;
            }
            column = column + 1;
        }
        return images;
    }

    /**
     * Removes the red component of a given image.
     *
     * @param image the BufferedImage containing the image
     *
     * @return a BufferedImage without the red component
     */
    public static BufferedImage removeReds ( BufferedImage image ) {
        int width = image.getWidth ( );
        int height = image.getHeight ( );
        Color c;
        BufferedImage resultingImage = new BufferedImage ( width , height , BufferedImage.TYPE_INT_RGB );
        for ( int i = 0 ; i < width ; i++ ) {
            for ( int j = 0 ; j < height ; j++ ) {
                c = new Color ( image.getRGB ( i , j ) );
                int g = c.getGreen ( );
                int b = c.getBlue ( );
                resultingImage.setRGB ( i , j , new Color ( 0 , g , b ).getRGB ( ) );
            }
        }
        return resultingImage;
    }

    /**
     * Joins a given array of BufferedImage in one final image. This method should be called after splitting the images
     * using, for example, the method {@link ImageTransformer#splitImage(BufferedImage , int , int)}.
     *
     * @param splitImages the BufferedImage array containing the sub-images
     * @param width       the width of the final image
     * @param height      the height of the final image
     * @param type        the type of the final image
     *
     * @return a BufferedImage array containing the image joined
     */
    public static BufferedImage joinImages ( BufferedImage[][] splitImages , int width , int height , int type ) {
        BufferedImage resultingImage = new BufferedImage ( width , height , type );
        int nRows = splitImages.length;
        int nColumns = splitImages[ 0 ].length;
        for ( int i = 0 ; i < nRows ; i++ ) {
            for ( int j = 0 ; j < nColumns ; j++ ) {
                BufferedImage split = splitImages[ i ][ j ];
                resultingImage.createGraphics ( ).drawImage ( split , split.getWidth ( ) * j , split.getHeight ( ) * i , null );
            }
        }
        return resultingImage;
    }

    /**
     *  Creates a Buffered image from a byte array
     * @param imageData - Byte array of with the image
     * @return - Image as a BufferedItem object
     */
    public static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes an image as a byte array so it can be sent through a socket. It is important to define the type of image
     * @param image - image to be converted as a byte[]
     * @return - image as an array of bytes
     */
    public static byte[] createBytesFromImage(BufferedImage image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }
}