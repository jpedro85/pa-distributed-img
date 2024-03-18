package Utils.Events.Enums;

/**
 * Enumerates the severity levels for events within the system. This enumeration
 * is used to categorize events by their level of severity, allowing for
 * appropriate response mechanisms to be triggered based on the severity of an
 * event.
 *
 * <p>
 * The {@code SeverityLevels} are typically used in conjunction with event
 * classes, such as {@link Utils.Events.ErrorEvent}, to specify how critical an
 * event is.This can influence logging, user notifications, and how the system
 * might attempt to recover or respond to specific events.
 * </p>
 *
 * <p>
 * Available Severity Levels:
 * </p>
 *
 * <ul>
 * <li>{@code WARNING} - Indicates a situation that is not immediately harmful
 * but should be addressed to prevent potential future errors. Use this level
 * for event that require attention but do not stop the system from continuing
 * it's operation.
 * </li>
 * <li>{@code ERROR} - Represents a significant problem that has occurred within
 * the system. An event marked with this severity level indicates a failure that
 * could potentially stop or hinder system operations. Use this level for events
 * that need immediate attention and might require intervention to restore
 * normal functionality.
 * </li>
 * </ul>
 *
 * @see Utils.Events.ErrorEvent
 * @see Utils.Events.Enums.EventTypes
 */
public enum SeverityLevels {
    /**
     * Indicates a situation that should be addressed but is not immediately
     * harmful.
     */
    WARNING,
    /**
     * Indicates a significant problem impacting system operations
     */
    ERROR,
}
