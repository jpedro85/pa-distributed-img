package Utils.Image;

import java.awt.image.BufferedImage;

/**
 * Represents a segment of an image, typically used when an image is divided
 * into smaller parts for processing or transmission. This class captures a
 * portion of an image along with its designated column and line position,
 * facilitating the handling and reassembly of image parts.
 *
 * <p>
 * The {@code SplitImage} class is particularly useful in scenarios where large
 * images need to be processed in smaller segments to optimize for performance
 * or to comply with server upload constraints. Each instance of this class
 * represents a single segment of a larger image, including metadata about its
 * position (column and line number) within the original image, as well as the
 * image data itself.
 * </p>
 *
 * <p>
 * This approach allows for efficient parallel processing of image segments or
 * their sequential transmission to a server, where they can be reassembled or
 * processed individually as needed.
 * </p>
 */
public class SplitImage {

    private final short columnNumber;
    private final short lineNumber;
    private BufferedImage image;

    /**
     * Constructs a new SplitImage and saving the line and column position of the
     * image segment with lineNumber and columnNumber.
     *
     * @param columnNumber The column number of this image segment within the larger
     *                     image. This helps identify its position for reassembly.
     * @param lineNumber   The line number of this image segment within the larger
     *                     image, further assisting in its correct placement upon
     *                     reassembly.
     * @param image        The {@link BufferedImage} containing the image segment
     *                     data.
     */
    public SplitImage(short columnNumber, short lineNumber, BufferedImage image) {
        this.columnNumber = columnNumber;
        this.lineNumber = lineNumber;
        this.image = image;
    }

    /**
     * Returns the column number of this image segment.
     *
     * @return The column number indicating the segment's horizontal position in the
     *         original image.
     */
    public short getColumnNumber() {
        return columnNumber;
    }

    /**
     * Returns the line number of this image segment.
     *
     * @return The line number indicating the segment's vertical position in the
     *         original image.
     */
    public short getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the {@link BufferedImage} object containing the data for this image
     * segment.
     *
     * @return The {@link BufferedImage} representing this segment of the image.
     */
    public BufferedImage getImage() {
        return image;
    }
}
