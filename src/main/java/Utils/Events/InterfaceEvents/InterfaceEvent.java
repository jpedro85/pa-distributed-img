package Utils.Events.InterfaceEvents;

import Utils.Events.Enums.InterfaceEvents;
import Utils.Events.Event;

/**
 * This interface defines the methods needs for an InterfaceEvent of
 * {@link InterfaceEvents}
 */
public interface InterfaceEvent extends Event {

    /**
     * Retrieves the specific type of interface event.
     *
     * @return The {@link InterfaceEvents} type of this event, indicating the
     *         specific UI action or state change.
     */
    public InterfaceEvents getEvent();

}
