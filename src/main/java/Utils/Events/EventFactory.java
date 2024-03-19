package Utils.Events;

import Utils.Events.Enums.*;
import Utils.Image.SplitImage;

/**
 * A factory class for creating instances of various types of events in the
 * system. This class provides static methods to create specific event objects,
 * such as {@link RegularEvent}, {@link ErrorEvent}, {@link ImageStateEvent},
 * and {@link ServerEvent}, based on the provided parameters. This approach
 * encapsulates the instantiation logic of events, promoting loose coupling and
 * enhancing code maintainability.
 *
 * <p>
 * Each method within the factory is dedicated to a specific event type,
 * ensuring that the necessary data for each event type is correctly provided
 * and encapsulated within the new instance. This facilitates a consistent and
 * straightforward way to generate events throughout the system.
 * </p>
 */
public class EventFactory {

    private EventFactory() {
    };

    /**
     * Creates a {@link RegularEvent} for logging and tracking standard operations.
     * This event type is suitable for routine activities, such as user actions or
     * system updates, that do not require special handling or categorization as
     * errors or system states.
     *
     * @param message The message describing the event, not null or empty.
     * @param type    The type of the event, as defined by {@link EventTypes}, not
     *                null.
     * @return A new instance of {@link RegularEvent}, never null.
     * @throws IllegalArgumentException if any argument is null or if message is
     *                                  empty.
     */
    public static Event createRegularEvent(String message, EventTypes type) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message must not be null or empty.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Event type must not be null.");
        }
        if (type != EventTypes.REGULAR) {
            throw new IllegalArgumentException("The type of event is not valid for this Event.");
        }
        return new RegularEvent(message, type);
    }

    /**
     * Creates an {@link ErrorEvent} for logging errors with a specified severity
     * level. Use this event type for exceptions or system errors that need
     * attention or special handling. The severity level helps in categorizing the
     * urgency of the error.
     *
     * @param message       The message describing the event, not null or empty.
     * @param type          The type of the event, as defined by {@link EventTypes},
     *                      not null.
     * @param severityLevel The severity level of the event, as defined by
     *                      {@link SeverityLevels}, not null.
     * @return A new instance of {@link ErrorEvent}, never null.
     * @throws IllegalArgumentException if any argument is null, or if message is
     *                                  empty.
     */
    public static Event createErrorEvent(String message, EventTypes type, SeverityLevels severityLevel) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message must not be null or empty.");
        }
        if (type == null || severityLevel == null) {
            throw new IllegalArgumentException("Event type and severity level must not be null.");
        }
        if (type != EventTypes.ERROR) {
            throw new IllegalArgumentException("The type of event is not valid for this Event.");
        }
        return new ErrorEvent(message, type, severityLevel);
    }

    /**
     * Creates an {@link ImageStateEvent} with the specified message, event type,
     * image state,
     * and split image object.
     *
     * @param message    The message describing the event.
     * @param type       The type of the event, as defined by {@link EventTypes}.
     * @param imageState The current state of the image processing task, as defined
     *                   by {@link ImageStates}.
     * @param splitImage The {@link SplitImage} object associated with the event.
     * @return A new instance of {@link ImageStateEvent}.
     */
    public static Event createImageStateEvent(String message, EventTypes type, ImageStates imageState, SplitImage splitImage) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message must not be null or empty.");
        }
        if (type == null || imageState == null || splitImage == null) {
            throw new IllegalArgumentException("Event type, imageState and splitImage must not be null.");
        }
        if (type != EventTypes.IMAGE) {
            throw new IllegalArgumentException("The type of event is not valid for this Event.");
        }
        return new ImageStateEvent(message, type, imageState, splitImage);
    }

    /**
     * Creates a {@link ServerEvent} with the specified message, event type, server
     * state, and identifier.
     *
     * @param message     The message describing the event.
     * @param type        The type of the event, as defined by {@link EventTypes}.
     * @param serverState The current state of the server, as defined by
     *                    {@link ServerStates}.
     * @param identifier  An identifier associated with the event (e.g., server ID).
     * @return A new instance of {@link ServerEvent}.
     */
    public static Event createServerEvent(String message, EventTypes type, ServerStates serverState, int identifier) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message must not be null or empty.");
        }
        if (type == null || serverState == null) {
            throw new IllegalArgumentException("Event type, imageState and splitImage must not be null.");
        }
        if (type != EventTypes.SERVER) {
            throw new IllegalArgumentException("The type of event is not valid for this Event.");
        }
        return new ServerEvent(message, type, serverState, identifier);
    }
}
