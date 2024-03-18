package Utils.Events;

import Utils.Events.Enums.EventTypes;

/**
 * Represents a regular event within the system, typically used for standard
 * operational notifications or actions that do not fall under specialized
 * categories such as errors or image processing states. This class captures
 * information relevant to routine events, including a descriptive message and
 * the event's type.
 *
 * <p>
 * A {@code RegularEvent} is used to convey information about non-critical
 * actions or occurrences within the system, serving as a means of communication
 * between different components or for logging purposes. It implements the
 * {@link Event} interface, allowing it to be processed alongside other types of
 * events in a consistent manner.
 * </p>
 *
 * <p>
 * This class is particularly useful for representing events that are part of
 * the normal operation of the system but still require tracking or logging.
 * Examples could include user actions, system state changes that are part of
 * normal operations, or any event that does not signify an error condition or a
 * specific state in a processing workflow.
 * </p>
 *
 * @see Event
 * @see EventTypes
 */
public class RegularEvent implements Event {

    private String message;
    private EventTypes eventType;

    /**
     * Constructs a new RegularEvent with the specified message and event type.
     *
     * @param message   The message describing the event, intended to provide
     *                  detailed
     *                  information about the nature and context of the event.
     * @param eventType The type of the event, categorizing it within the broader
     *                  system operations. This type aids in event handling and
     *                  processing strategies.
     */
    RegularEvent(String message, EventTypes eventType) {
        this.message = message;
        this.eventType = eventType;
    }

    /**
     * Returns the type of this event, providing a categorization that aids in its
     * processing and handling within the system.
     *
     * @return The event type as an instance of {@link EventTypes}.
     */
    @Override
    public EventTypes getType() {
        return eventType;
    }

    /**
     * Returns the descriptive message associated with this event.
     *
     * @return A string message describing the event.
     */
    @Override
    public String getMessage() {
        return message;
    }
}
