package Utils.Parser;

import org.ini4j.Ini;
import java.io.File;
import java.io.IOException;

public class IniFileReader {

    private static final IniFileReader instance = new IniFileReader();

    private IniFileReader() {
    }

    public static IniFileReader getInstance() {
        return instance;
    }

    public Ini readIniFile(String filePath) throws IOException {
        File configFile = new File(filePath);
        return new Ini(configFile);
    }
}
