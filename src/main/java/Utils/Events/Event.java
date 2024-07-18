package Utils.Events;

import Utils.Events.Enums.EventTypes;

/**
 * Represents a generic event within the system. This interface is designed to
 * be implemented by various types of events that can occur, providing a
 * standardized way to handle and process these events regardless of their
 * specific details.
 *
 * <p>
 * Implementations of this interface are expected to provide details about the
 * event's type and a message describing the event. The {@code EventTypes}
 * enumeration is used to categorize the event into a predefined set of types,
 * facilitating easy identification and processing of events based on their
 * type.
 * </p>
 *
 * <p>
 * This interface is crucial for creating a polymorphic events system where
 * different kinds of events can be processed in a unified manner. It enables
 * the system to react to events of various types without needing to know the
 * exact class of the event object, only that it adheres to the {@code Event}
 * interface.
 * </p>
 *
 * @see Utils.Events.Enums.EventTypes
 */
public interface Event {

    /**
     * Returns the type of the event.
     *
     * @return The type of the event as an instance of {@link EventTypes},
     *         categorizing the event into a predefined set of types for easy
     *         identification and processing.
     */
    public EventTypes getType();

    /**
     * Returns a message describing the event. This message should provide enough
     * detail to understand the context and significance of the event.
     *
     * @return A string message describing the event.
     */
    public String getMessage();
}
