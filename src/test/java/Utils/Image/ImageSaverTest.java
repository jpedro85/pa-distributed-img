package Utils.Image;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageSaverTest {

    private BufferedImage testImage;
    private String testImagePath;
    private String format;

    @BeforeEach
    void setUp() {
        testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        testImagePath = "test_image.png";
        format = "png";
    }

    @AfterEach
    void tearDown() {
        File file = new File(testImagePath);

        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void saveImage_ValidImageAndPath_ReturnsTrue() {

        boolean result = ImageSaver.saveImage(testImage, format, testImagePath);

        assertTrue(result);
    }

    @Test
    public void saveImage_InvalidPath_ReturnsFalse() {

        String filePath = "/invalid/path/test_image.png";

        boolean result = ImageSaver.saveImage(testImage, format, filePath);

        assertFalse(result, "(No such file or directory)");
    }
}
