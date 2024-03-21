package Network.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerLoadTrackerTest {
    private static final String TEST_FILE_PATH = "load_info.txt";
    private static final String TEST_CONTENT = "8888=4,5\n10000=7,8\n";

    @BeforeEach
    public void setUp() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE_PATH))) {
            writer.write(TEST_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            if (!file.delete()) {
                System.err.println("Failed to delete test file.");
            }
        }
    }

    @Test
    @DisplayName("Running testReadLoadInfo")
    public void testReadLoadInfo() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        String actualLoadInfo = serverLoadTracker.readLoadInfo();
        assertEquals(TEST_CONTENT, actualLoadInfo);
    }


    @Test
    @DisplayName("Running testUpdate")
    public void testUpdate() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        serverLoadTracker.update(8888, 6, 9);
        String expectedLoadInfo = "8888=6,9\n10000=7,8\n";
        String actualLoadInfo = serverLoadTracker.readLoadInfo();
        assertEquals(expectedLoadInfo, actualLoadInfo);
    }

    @Test
    @DisplayName("Running testAddEntry")
    public void testAddEntry() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        serverLoadTracker.addEntry(789, 3, 6);
        String expectedLoadInfo = TEST_CONTENT + "789=3,6\n";
        String actualLoadInfo = serverLoadTracker.readLoadInfo();
        assertEquals(expectedLoadInfo, actualLoadInfo);
    }

    @Test
    @DisplayName("Running testRemoveEntry")
    public void testRemoveEntry() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        serverLoadTracker.removeEntry(8888);
        String expectedLoadInfo = "10000=7,8\n";
        serverLoadTracker.addEntry(789, 3, 6);
        serverLoadTracker.removeEntry(789);
        String actualLoadInfo = serverLoadTracker.readLoadInfo();
        assertEquals(expectedLoadInfo, actualLoadInfo);
    }

    @Test
    @DisplayName("Running testGetLoad")
    public void testGetLoad() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        assertEquals(9, serverLoadTracker.getLoad(8888));
    }

    @Test
    @DisplayName("Running testGetLowestLow")
    public void testGetLowestLow() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        assertEquals(8888, serverLoadTracker.getServerWithLessLoad());
        serverLoadTracker.addEntry(123,1,0);
        assertEquals(123, serverLoadTracker.getServerWithLessLoad());
    }

}
