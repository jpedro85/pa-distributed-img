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

    /**
     * Creates a {@link RegularEvent} with the specified message and event type.
     *
     * @param message The message describing the event.
     * @param type    The type of the event, as defined by {@link EventTypes}.
     * @return A new instance of {@link RegularEvent}.
     */
    public static Event createRegularEvent(String message, EventTypes type) {
        return new RegularEvent(message, type);
    }

    /**
     * Creates an {@link ErrorEvent} with the specified message, event type, and
     * severity level.
     *
     * @param message       The message describing the event.
     * @param type          The type of the event, as defined by {@link EventTypes}.
     * @param severityLevel The severity level of the event, as defined by
     *                      {@link SeverityLevels}.
     * @return A new instance of {@link ErrorEvent}.
     */
    public static Event createErrorEvent(String message, EventTypes type, SeverityLevels severityLevel) {
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
        return new ServerEvent(message, type, serverState, identifier);
    }
}
