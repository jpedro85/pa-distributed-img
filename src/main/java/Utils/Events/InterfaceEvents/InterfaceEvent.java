package Utils.Events.InterfaceEvents;

import Utils.Events.Enums.InterfaceEvents;
import Utils.Events.Event;

/**
 * This interface definesthe methods needes for an InterfaceEvent of {@link InterfaceEvents}
 */
public interface InterfaceEvent extends Event {

    /**
     * @return the {@link InterfaceEvents} type of this event
     */
    public InterfaceEvents getEvent();

}
