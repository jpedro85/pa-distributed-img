package Utils.Parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ini4j.Ini;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Add this annotation
class IniFileReaderTest {

    private String testFilePath = "testConfig.ini";
    private File configFile;

    @BeforeAll
    public void setUp() throws IOException {
        Ini testFile = new Ini();

        testFile.put("section1", "key1", "value1");
        testFile.put("section1", "key2", "value2");

        configFile = new File(testFilePath);
        testFile.store(configFile);
    }

    @BeforeAll
    public static void setupInvalidFormatFile() throws IOException {
        Files.writeString(Paths.get("invalidFormat.ini"), "This is not a valid INI content!");
    }

    @Test
    public void testReadIniFileSuccess() throws IOException {

        IniFileReader reader = IniFileReader.getInstance();
        Ini result = reader.readIniFile(testFilePath);

        assertNotNull(result);
        assertEquals("value1", result.get("section1", "key1"));
        assertEquals("value2", result.get("section1", "key2"));
    }

    @Test
    public void testReadIniFileNotFound() {
        IniFileReader reader = IniFileReader.getInstance();
        Exception exception = assertThrows(IOException.class, () -> {
            reader.readIniFile("nonexistent.ini");
        });

        String expectedMessage = "File not found";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testReadInvalidIniFileFormat() {
        IniFileReader reader = IniFileReader.getInstance();
        assertThrows(Exception.class, () -> {
            reader.readIniFile("invalidFormat.ini");
        });
    }

    @Test
    public void testSingletonBehavior() {
        IniFileReader firstInstance = IniFileReader.getInstance();
        IniFileReader secondInstance = IniFileReader.getInstance();
        assertSame(firstInstance, secondInstance, "IniFileReader should return the same instance.");

    }

    @Test
    public void testThreadSafety() throws InterruptedException {
        final Set<IniFileReader> instances = ConcurrentHashMap.newKeySet();
        int numberOfThreads = 100;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                IniFileReader instance = IniFileReader.getInstance();
                instances.add(instance);
            });
        }

        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(1, instances.size(), "Multiple instances were created in a multithreaded environment.");

    }

}
