package Utils.Events;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.SeverityLevels;

/**
 * Represents an error event within the system. This class encapsulates
 * information about errors that occur, including the error message, type of
 * event, and severity level. Instances of this class are typically created
 * through an EventFactory to ensure consistency and potentially facilitate
 * additional processing or logging mechanisms.
 *
 * <p>
 * Usage of this class involves specifying the error message, the type of the
 * event (as defined in {@link EventTypes}), and the severity level of the error
 * (as defined in {@link SeverityLevels}).
 * </p>
 *
 * <p>
 * This class implements the {@link Event} interface, enabling it to be used
 * polymorphically with other event types within the system.
 * </p>
 *
 * @see Event
 * @see EventTypes
 * @see SeverityLevels
 */
public class ErrorEvent implements Event {

    private final SeverityLevels severityLevel;
    private String message;
    private EventTypes eventType;

    /**
     * Constructs a new ErrorEvent with the specified message, event type, and
     * severity level.
     *
     * @param message       the detail message associated with the error event.
     * @param type          the type of the event, indicating the category or
     *                      context of the error.
     * @param severityLevel the severity level of the error, indicating its
     *                      potential impact.
     */
    ErrorEvent(String message, EventTypes type, SeverityLevels severityLevel) {

        this.message = message;
        this.eventType = type;
        this.severityLevel = severityLevel;
    }

    /**
     * Returns the type of this event.
     *
     * @return the event type as an instance of {@link EventTypes}.
     */
    @Override
    public EventTypes getType() {
        return eventType;
    }

    /**
     * Returns the message associated with this error event.
     *
     * @return the detail message of the error.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Returns the severity level of this error event.
     *
     * @return the severity level as an instance of {@link SeverityLevels}.
     */
    public SeverityLevels getSeverityLevel() {
        return severityLevel;
    }
}
