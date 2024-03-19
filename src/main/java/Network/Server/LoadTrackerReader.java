package Network.Server;

// TODO: Documentation
public interface LoadTrackerReader {

    public int getLoad(int serverIdentifier);

    public int getServerWithLessLoad();

}
