package Utils.Events.InterfaceEvents;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.InterfaceEvents;

/**
 * Represents an interfaceEvent of {@link InterfaceEvents} with a single name
 * associated with it.
 * This class implements the InterfaceEvent interface.
 */
public class InterfaceEventWithName implements InterfaceEvent {
    private final EventTypes eventType;
    private final String message;
    private final InterfaceEvents event;
    private final String name;

    /**
     * Construct
     *
     * @param message   The message describing the event.
     * @param eventType The general category of the event, as defined by
     *                  {@link EventTypes}.
     * @param event     The event inteface that corresponds to a user action
     * @param name      A String that identifies the client images to notify .(e.g.
     *                  the path)
     */
    public InterfaceEventWithName(EventTypes eventType, InterfaceEvents event, String message, String name) {
        this.eventType = eventType;
        this.message = message;
        this.event = event;
        this.name = name;
    }

    /**
     * @return The type of event (e.g., SUCCESS, ERROR).
     */
    @Override
    public EventTypes getType() {
        return this.eventType;
    }

    /**
     * @return A message associated with the event.
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * @return The specific interface event of {@link InterfaceEvents}.
     */
    @Override
    public InterfaceEvents getEvent() {
        return this.event;
    }

    /**
     * @return Names of the images to change state. (e.g. path names)
     */
    public String getName() {
        return this.name;
    }

}
