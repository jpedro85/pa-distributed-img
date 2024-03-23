package Utils.Events.InterfaceEvents;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Event;

/**
 * Class Represents a server update event
 */
public class LoadUpdateEvent implements Event {

    private final EventTypes TYPE;

    private final int RUNNING;

    private final int WAITING;

    private final String MSG;

    private final int ID;

    /**
     * Creates a new Instance of LoadUpdateEvent;
     *
     * @param ID the identifier of the server
     * @param RUNNING the number of running tasks in the server
     * @param WAITING the number of waiting tasks in the server
     * @param MSG a msg if necessary
     */
    public LoadUpdateEvent(int ID, int RUNNING, int WAITING, String MSG) {
        this.ID = ID;
        this.TYPE = EventTypes.LOAD_UPDATE;
        this.RUNNING = RUNNING;
        this.WAITING = WAITING;
        this.MSG = MSG;
    }

    /**
     * @return identifier of the server.
     */
    public int getID() {
        return ID;
    }

    /**
     * @return the number of running tasks in the server.
     */
    public int getRUNNING() {
        return RUNNING;
    }

    /**
     * @return the number of waiting tasks in the server.
     */
    public int getWAITING() {
        return WAITING;
    }

    @Override
    public EventTypes getType() {
        return this.TYPE;
    }

    @Override
    public String getMessage() {
        return this.MSG;
    }
}
