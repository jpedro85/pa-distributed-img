package Utils.Observer;

import Utils.Events.Event;

/**
 * Represents an observer in the observer pattern. This interface is designed to
 * be implemented by any class that needs to receive updates about changes in
 * the subject's state or other significant events. Observers subscribe to
 * subjects, and upon certain events or state changes, the subjects notify their
 * observers by invoking their {@code update} method.
 *
 * <p>
 * This mechanism allows for a decoupled system where subjects and observers are
 * loosely connected through the observer interface. Implementing this interface
 * enables objects to act as observers, responding to events or state changes
 * without being tightly integrated into the subject's internal structure.
 * </p>
 *
 * <p>
 * Typical use involves the observer implementing this interface to define how
 * it should update its state in response to notifications received from the
 * subjects it is observing.
 * </p>
 */
public interface Observer {

    /**
     * Called by the subject to notify this observer of an event or state change.
     * This method defines how the observer should respond to the notification.
     *
     * <p>
     * Implementations of this method can perform actions such as updating internal
     * state, initiating further processing, or simply logging the event, based on
     * the specific needs and responsibilities of the observer.
     * </p>
     *
     * @param subject The subject from which this observer is receiving updates.
     *                This parameter allows the observer to distinguish between
     *                notifications from multiple subjects.
     * @param event   The event or state change that has occurred, providing context
     *                for the update.
     */
    public void update(Subject subject, Event event);
}
