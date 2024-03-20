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
public class ServerLoadTracker implements LoadTrackerEdit {
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
        checkFileCreation();
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


    /**
     * Updates the load information for a specific server.
     * @param serverIdentifier the identifier of the server
     * @param running the number of running processes
     * @param waiting the number of waiting processes
     */
    @Override
    public void update(int serverIdentifier, int running, int waiting) {
        // Create a temporary file to store updated data
        File tempFile = new File("temp_load_info.txt");

        // Lock the fileVarSync to prevent concurrent access
        fileVarSync.lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileVarSync.asyncGet()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line to get serverIdentifier, running, and waiting values
                String[] parts = line.split("=");
                int currentServerIdentifier = Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // Extract numeric part from server identifier
                if (currentServerIdentifier == serverIdentifier) {
                    // Update the load information for the server
                    writer.write(serverIdentifier + "=" + running + "," + waiting + "\n");
                } else {
                    // Write the unchanged line to the temporary file
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Unlock the fileVarSync
            fileVarSync.unlock();
        }

        // Replace the original file with the temporary file
        File originalFile = fileVarSync.asyncGet();
        if (!originalFile.delete()) {
            System.err.println("Failed to delete original file.");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.err.println("Failed to rename temporary file.");
        }
    }

    /**
     * Adds a new entry to the load information for a server.
     * @param serverIdentifier the identifier of the server
     * @param running the number of running processes
     * @param waiting the number of waiting processes
     */
    @Override
    public void addEntry(int serverIdentifier, int running, int waiting) {
        fileVarSync.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileVarSync.asyncGet(), true))) {
            writer.write(serverIdentifier + "=" + running + "," + waiting + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileVarSync.unlock();
        }
    }

    /**
     * Removes an entry from the load information for a server.
     * @param serverIdentifier the identifier of the server
     */
    @Override
    public void removeEntry(int serverIdentifier) {
        // Create a temporary file to store updated data
        File tempFile = new File("temp_load_info.txt");

        // Lock the fileVarSync to prevent concurrent access
        fileVarSync.lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileVarSync.asyncGet()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line to get serverIdentifier
                String[] parts = line.split("=");
                int currentServerIdentifier = Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // Extract numeric part from server identifier
                if (currentServerIdentifier != serverIdentifier) {
                    // Write the unchanged line to the temporary file
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Unlock the fileVarSync
            fileVarSync.unlock();
        }

        // Replace the original file with the temporary file
        File originalFile = fileVarSync.asyncGet();
        if (!originalFile.delete()) {
            System.err.println("Failed to delete original file.");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.err.println("Failed to rename temporary file.");
        }
    }
//    /**
//     * Gets the load for a specific server.
//     * @param serverIdentifier the identifier of the server
//     * @return the load value for the specified server
//     */
//    @Override
//    public int getLoad(int serverIdentifier) {
//        // Lock the fileVarSync to prevent concurrent access
//        fileVarSync.lock();
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileVarSync.asyncGet()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("=");
//                int currentServerIdentifier = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
//                if (currentServerIdentifier == serverIdentifier) {
//                    // Extract the load value for the specified server
//                    return Integer.parseInt(parts[1].split(",")[0]); // Assuming load is stored before ","
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Unlock the fileVarSync
//            fileVarSync.unlock();
//        }
//        return -1; // Return -1 if the server identifier is not found
//    }
//
//    /**
//     * Gets the identifier of the server with the least load.
//     * @return the identifier of the server with the least load
//     */
//    @Override
//    public int getServerWithLessLoad() {
//        int minLoad = Integer.MAX_VALUE;
//        int serverWithLessLoad = -1;
//
//        // Lock the fileVarSync to prevent concurrent access
//        fileVarSync.lock();
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileVarSync.asyncGet()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("=");
//                int currentLoad = Integer.parseInt(parts[1].split(",")[0]); // Assuming load is stored before ","
//                if (currentLoad < minLoad) {
//                    minLoad = currentLoad;
//                    serverWithLessLoad = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Unlock the fileVarSync
//            fileVarSync.unlock();
//        }
//        return serverWithLessLoad;
//    }
}
