package Utils.Parser;

import java.io.IOException;

import org.ini4j.Ini;

public class ConfigParser {

    private static final ConfigParser instance = new ConfigParser();
    private IniFileReader iniFileReader;

    private ConfigParser() {
    }

    public Config parseFromIniToConfig(String filePath) throws IOException {
        if (this.iniFileReader == null) {
            this.iniFileReader = IniFileReader.getInstance();
        }
        Ini configFile = this.iniFileReader.readIniFile(filePath);

        Config config = new Config();

        validateSectionExists(configFile, "server");
        config.setServerAmount(parseInteger(configFile, "server", "serverAmount"));
        config.setTaskPoolSize(parseInteger(configFile, "server", "taskPoolSize"));

        validateSectionExists(configFile, "image");
        config.setColumns(parseInteger(configFile, "image", "columns"));
        config.setRows(parseInteger(configFile, "image", "rows"));

        return config;
    }

    private void validateSectionExists(Ini configFile, String section) {
        if (!configFile.containsKey(section)) {
            throw new IllegalArgumentException("Missing [" + section + "] section in INI file.");
        }
    }

    private int parseInteger(Ini configFile, String section, String key) {
        String valueStr = configFile.get(section, key);
        if (valueStr == null) {
            throw new IllegalArgumentException("Missing key '" + key + "' in [" + section + "] section.");
        }
        try {
            return Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for '" + key + "' in [" + section + "] section.");
        }
    }

    public static ConfigParser getInstance() {
        return instance;
    }

    // Method to inject a mock IniFileReader
    public void setIniFileReader(IniFileReader iniFileReader) {
        this.iniFileReader = iniFileReader;
    }
}
