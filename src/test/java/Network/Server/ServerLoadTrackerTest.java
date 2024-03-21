package Network.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerLoadTrackerTest {
    private static final String TEST_FILE_PATH = "load_info.txt";
    private static final String TEST_CONTENT = "123=4.5\n456=7.8\n";

    @BeforeEach
    public void setUp() {
        // Escrever dados de teste no arquivo antes de cada teste
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE_PATH))) {
            writer.write(TEST_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        // Excluir o arquivo após cada teste
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
    @DisplayName("Running testWriteLoadInfo")
    public void testWriteLoadInfo() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        serverLoadTracker.writeLoadInfo("789", 1.2);
        String expectedLoadInfo = TEST_CONTENT + "789=1.2\n";
        String actualLoadInfo = serverLoadTracker.readLoadInfo();
        assertEquals(expectedLoadInfo, actualLoadInfo);
    }

    @Test
    @DisplayName("Running testUpdate")
    public void testUpdate() {
        ServerLoadTracker serverLoadTracker = ServerLoadTracker.getInstance();
        serverLoadTracker.setFilePath(TEST_FILE_PATH);
        serverLoadTracker.update(123, 6, 9);
        String expectedLoadInfo = "123=6,9\n456=7.8\n";
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
        serverLoadTracker.removeEntry(456);
        String expectedLoadInfo = "123=4.5\n";
        String actualLoadInfo = serverLoadTracker.readLoadInfo();
        assertEquals(expectedLoadInfo, actualLoadInfo);
    }
}
