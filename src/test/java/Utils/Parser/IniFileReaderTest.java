package Utils.Parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
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

    @Test
    public void testReadIniFileSuccess() throws IOException {

        IniFileReader reader = IniFileReader.getInstance();
        Ini result = reader.readIniFile(testFilePath);

        assertNotNull(result);
        assertEquals("value1", result.get("section1", "key1"));
        assertEquals("value2", result.get("section1", "key2"));
    }

    @AfterAll
    public void tearDown() {
        configFile.delete();
    }
}
