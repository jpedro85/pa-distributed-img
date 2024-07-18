package Utils.Events.Enums;

/**
 * Enumerates the types of events that can occur within the system, providing a
 * standardized set of categories for all events. This enumeration allows for
 * consistent processing and handling of events based on their type.
 *
 * <p>
 * The {@code EventTypes} are used in conjunction with the
 * {@link Utils.Events.Event} interface and its implementations, such as
 * {@link Utils.Events.ErrorEvent}, to classify events. This classification
 * facilitates targeted responses and processing strategies for different kinds
 * of events, enhancing the system's ability to manage events effectively.
 * </p>
 *
 * <p>
 * Event Types:
 * </p>
 * <ul>
 * <li>
 * {@code IMAGE} - Represents events related to image processing or
 * manipulation, such as uploading or editing images. Use this type for events
 * that involve image data.
 * </li>
 *
 * <li>
 * {@code ERROR} - Indicates events that are errors or exceptions. This type
 * is used for events that signal a malfunction or an unexpected issue that
 * requires attention.
 * </li>
 *
 * <li>
 * {@code REGULAR} - Denotes standard or routine events that occur during
 * normal operation of the system. These events might include user actions that
 * don't fall into more specific categories.
 * </li>
 *
 * <li>
 * {@code SERVER} - Specifically categorizes events related to server
 * operations, as status changes, maintenance activities, or any event directly
 * involving server management and monitoring. This type enhances the
 * specificity and granularity of event management, particularly for
 * server-related activities.
 * </li>
 *
 * </ul>
 *
 * <p>
 * By categorizing events with {@code EventTypes}, the system can easily route
 * and handle them according to their specific nature and requirements.
 * </p>
 *
 * @see Utils.Events.Event
 */
public enum EventTypes {
    /**
     * Represents events related to image processing or manipulation.
     */
    IMAGE,
    /**
     * Indicates events that are errors or exceptions.
     */
    ERROR,
    /**
     * Denotes standard or routine events during normal system operation.
     */
    REGULAR,
    /**
     * A general category for events not specifically covered by other types.
     */
    SERVER,
    /**
     * Indicates that a user initiated an event.
     */
    INTERFACE,
    /**
     * Event launch when updater loadTracker
     */
    LOAD_UPDATE

}
