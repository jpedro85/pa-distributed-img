package Network.Server;

public interface LoadTrackerReader {

    /**
     * @param serverIdentifier the identifier of the server to the get the load.
     * @return the load of a server with the specified identifier ;
     */
    public int getLoad(int serverIdentifier);

    /**
     *
     * @return server identifier that has the less load. -1 if no server exists.
     */
    public int getServerWithLessLoad();

}
