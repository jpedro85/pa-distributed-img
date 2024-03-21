package Network.Server;

import java.io.*;
import Utils.VarSync;

/**
 * A singleton class for tracking server load information.
 */
public class ServerLoadTracker implements LoadTrackerEdit, LoadTrackerReader {
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
    public static ServerLoadTracker getInstance() {
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

//    /**
//     * Writes load information for a server to the tracked data.
//     * @param serverPort the port of the server
//     * @param load the load value to be written
//     */
//    public void writeLoadInfo(String serverPort, double load) {
//        fileVarSync.lock();
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileVarSync.asyncGet(), true))) {
//            writer.write(serverPort + "=" + load + "\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            fileVarSync.unlock();
//        }
////    }
//

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
     *
     * @param serverIdentifier the identifier of the server
     * @param running the number of running processes
     * @param waiting the number of waiting processes
     */
    @Override
    public void update(int serverIdentifier, int running, int waiting) {
        File originalFile = fileVarSync.asyncGet();
        File tempFile = new File(originalFile.getAbsolutePath() + ".temp");

        fileVarSync.lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("=");
                int currentServerIdentifier = Integer.parseInt( parts[0] );

                if (currentServerIdentifier == serverIdentifier)
                {
                    writer.write(serverIdentifier + "=" + running + "," + waiting + "\n");
                }
                else
                {
                    writer.write(line + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace the original file with the temporary file
        if (!originalFile.delete()) {
            System.err.println("Failed to delete original file.");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.err.println("Failed to rename temporary file.");
        }

        fileVarSync.unlock();
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

        fileVarSync.lock();

        File originalFile = fileVarSync.asyncGet();
        File tempFile = new File(originalFile.getAbsolutePath() + ".temp");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("=");
                int currentServerIdentifier = Integer.parseInt( parts[0] );

                if (currentServerIdentifier != serverIdentifier)
                {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace the original file with the temporary file
        if (!originalFile.delete()) {
            System.err.println("Failed to delete original file.");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.err.println("Failed to rename temporary file.");
        }
        fileVarSync.unlock();
    }

    @Override
    public int getLoad(int serverIdentifier)
    {
        fileVarSync.lock();

        try ( BufferedReader reader = new BufferedReader(new FileReader( this.fileVarSync.asyncGet() )) ) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");

                if ( Integer.parseInt(parts[0]) == serverIdentifier )
                {
                    fileVarSync.unlock();
                    String[] loads = parts[1].split(",");
                    return Integer.parseInt( loads[0] ) + Integer.parseInt( loads[1] );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        fileVarSync.unlock();

        return -1;
    }

    @Override
    public int getServerWithLessLoad()
    {
        fileVarSync.lock();

        int port = -1;
        int lowestLoad = -1;

        try ( BufferedReader reader = new BufferedReader(new FileReader( this.fileVarSync.asyncGet() )) ) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");
                String[] loads = parts[1].split(",");

                int load = Integer.parseInt( loads[0] ) + Integer.parseInt( loads[1] );

                if ( load <= lowestLoad || lowestLoad == -1 )
                {
                    port = Integer.parseInt(parts[0]);
                    lowestLoad = load;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        fileVarSync.unlock();

        return port;
    }
}
