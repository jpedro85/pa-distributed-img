package Network.Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import Utils.VarSync;

/**
 * A singleton class for tracking server load information.
 */
public class ServerLoadTracker {
    private static ServerLoadTracker instance;
    private final VarSync<File> fileVarSync;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private ServerLoadTracker() {
        this.fileVarSync = new VarSync<>(null);
    }

    /**
     * Returns the singleton instance of ServerLoadTracker.
     * @return the singleton instance
     */
    public static synchronized ServerLoadTracker getInstance() {
        if (instance == null) {
            instance = new ServerLoadTracker();
        }
        return instance;
    }

    /**
     * Sets the file path for storing load information.
     * @param path the file path
     */
    public void setFilePath(String path) {
        fileVarSync.syncSet(new File(path));
        checkFileCreation(); // Check if the file is created when setting the file path
    }

    /**
     * Writes load information for a server to the tracked data.
     * @param serverPort the port of the server
     * @param load the load value to be written
     */
    public void writeLoadInfo(String serverPort, double load) {
        fileVarSync.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileVarSync.asyncGet(), true))) {
            writer.write(serverPort + "=" + load + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileVarSync.unlock();
        }
    }

    /**
     * Reads the tracked load information from the file.
     * @return a string containing the server port and corresponding load values
     */
    public synchronized String readLoadInfo() {
        StringBuilder loadInfoBuilder = new StringBuilder();
        fileVarSync.lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileVarSync.asyncGet()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadInfoBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileVarSync.unlock();
        }
        return loadInfoBuilder.toString();
    }

    /**
     * Checks if the file is created and prints a message.
     */
    private void checkFileCreation() {
        File file = fileVarSync.asyncGet();
        if (file.exists()) {
            System.out.println("File already exists: " + file.getAbsolutePath());
        } else {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created successfully: " + file.getAbsolutePath());
                } else {
                    System.out.println("Failed to create file.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public static void main(String[] args) {
//        // Instanciando o ServerLoadTracker
//        ServerLoadTracker loadTracker = ServerLoadTracker.getInstance();
//
//        // Definindo o caminho do arquivo
//        String filePath = "load_info.txt";
//        loadTracker.setFilePath(filePath);
//
//        // Escrevendo alguns dados de carga
//        loadTracker.writeLoadInfo("server1", 0.75);
//        loadTracker.writeLoadInfo("server2", 0.85);
//        loadTracker.writeLoadInfo("server3", 0.65);
//
//        // Lendo os dados de carga
//        String loadInfo = loadTracker.readLoadInfo();
//        System.out.println("Dados de carga lidos:\n" + loadInfo);
//    }
}
