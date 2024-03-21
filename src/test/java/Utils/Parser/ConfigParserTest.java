package Utils.Parser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import org.ini4j.Ini;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConfigParserTest {

    @Mock
    private IniFileReader mockIniFileReader;

    @BeforeEach
    public void setUp() throws IOException {
        mockIniFileReader = Mockito.mock(IniFileReader.class);
    }

    @Test
    public void parseFromIniToConfig_ValidConfig_ReturnsConfigObject() throws IOException {

        Ini mockIni = new Ini();
        mockIni.put("server", "serverAmount", "2");
        mockIni.put("server", "taskPoolSize", "10");
        mockIni.put("image", "columns", "5");
        mockIni.put("image", "rows", "4");
        when(mockIniFileReader.readIniFile("validConfig")).thenReturn(mockIni);

        ConfigParser configParser = ConfigParser.getInstance();
        configParser.setIniFileReader(mockIniFileReader);

        Config result = configParser.parseFromIniToConfig("validConfig");

        assertNotNull(result);
        assertInstanceOf(Config.class, result);
        assertEquals(2, result.getServerAmount());
        assertEquals(10, result.getTaskPoolSize());
        assertEquals(5, result.getColumns());
        assertEquals(4, result.getRows());
    }

    @Test
    public void parseFromIniToConfig_MissingSection_ThrowsIllegalArgumentException() throws IOException {
        Ini mockIniMissing = new Ini();

        when(mockIniFileReader.readIniFile("configWithInvalidValue")).thenReturn(mockIniMissing);

        ConfigParser configParser = ConfigParser.getInstance();
        configParser.setIniFileReader(mockIniFileReader);

        assertThrows(IllegalArgumentException.class, () -> configParser.parseFromIniToConfig("configWithInvalidValue"),
                "Expected to throw due to invalid integer value.");
    }
}
