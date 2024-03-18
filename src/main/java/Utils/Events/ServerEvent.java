package Utils.Events;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.ServerStates;

/**
 * Represents an event related to server operations within the system. This
 * class is used to encapsulate information about events that concern server
 * state changes or significant server activities. It includes details such as a
 * descriptive message, the event type, the specific state of the server at the
 * time of the event, and an identifier for the server involved.
 *
 * <p>
 * This class implements the {@link Event} interface, allowing server-related
 * events to be processed alongside other types of events in a consistent
 * manner. It is particularly useful for monitoring server health, performing
 * server management tasks, and responding to server state changes
 * programmatically.
 * </p>
 *
 * @see Event
 * @see EventTypes
 * @see ServerStates
 */
public class ServerEvent implements Event {

    private String message;
    private EventTypes eventType;
    private ServerStates serverState;
    private int serverIdentifier;

    /**
     * Constructs a new ServerEvent with the specified message, event type, server
     * state, and server identifier.
     *
     * @param message     The message describing the event, which should provide
     *                    context or details about the server event.
     * @param type        The type of the event, as defined by {@link EventTypes},
     *                    indicating the category of the event.
     * @param serverState The current state of the server, as defined by
     *                    {@link ServerStates}, indicating the specific state change
     *                    or condition.
     * @param identifier  An integer identifier for the server to which this event
     *                    pertains, facilitating identification and tracking of
     *                    server-specific events.
     */
    ServerEvent(String message, EventTypes type, ServerStates serverState, int identifier) {
        this.message = message;
        this.eventType = type;
        this.serverState = serverState;
        this.serverIdentifier = identifier;
    }

    /**
     * Returns the type of this event.
     *
     * @return The event type as an instance of {@link EventTypes}.
     */
    @Override
    public EventTypes getType() {
        return this.eventType;
    }

    /**
     * Returns the descriptive message associated with this event.
     *
     * @return A string message describing the server event.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Returns the current state of the server related to this event.
     *
     * @return The server state as an instance of {@link ServerStates}, indicating
     *         the specific server condition or state change.
     */
    public ServerStates getServerState() {
        return serverState;
    }

    /**
     * Returns the identifier of the server related to this event.
     *
     * @return An integer server identifier, facilitating the tracking and
     *         management of server-specific events.
     */
    public int getServerIdentifier() {
        return serverIdentifier;
    }
}
