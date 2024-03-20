package Network.Server;

public interface LoadTrackerEdit {
        /**
         * Updates the load information for a server with the given identifier.
         * @param serverIdentifier the identifier of the server
         * @param running the number of tasks currently running on the server
         * @param waiting the number of tasks waiting in the queue of the server
         */
        void update(int serverIdentifier, int running, int waiting);

        /**
         * Adds an entry for a server with the given identifier and load information.
         * @param serverIdentifier the identifier of the server
         * @param running the number of tasks currently running on the server
         * @param waiting the number of tasks waiting in the queue of the server
         */
        void addEntry(int serverIdentifier, int running, int waiting);

        /**
         * Removes the load information entry for a server with the given identifier.
         * @param serverIdentifier the identifier of the server
         */
        void removeEntry(int serverIdentifier);
}
