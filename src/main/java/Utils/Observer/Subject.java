package Utils.Observer;

import Utils.Events.Event;

/**
 * Defines the operations for managing observers and notifying them of events in
 * an implementation of the observer pattern. The {@code Subject} interface is a
 * core component of the observer pattern, where subjects maintain a list of
 * their observers and notify them of state changes or significant occurrences.
 *
 * <p>
 * This interface is designed to be implemented by any class that acts as a
 * "subject" within the observer pattern. Implementing classes will manage a
 * list of {@link Observer} instances and provide mechanisms to add and remove
 * observers. When an event occurs that observers should be aware of, the
 * implementing class will notify each observer by calling their {@code update}
 * method, passing along relevant event information.
 * </p>
 *
 * <p>
 * Use of this interface allows for a decoupled system where subjects and
 * observers can operate independently, only connected through the observer
 * interface. This enhances modularity, facilitates easy expansion, and improves
 * maintainability of the system.
 * </p>
 */
public interface Subject {

    /**
     * Registers an observer to be notified of events. Observers added through this
     * method will receive notifications when the subject's state changes or when
     * significant events occur that the observer should be aware of.
     *
     * @param observer The {@link Observer} instance to be added to the subject's
     *                 list of observers.
     */
    public void addObserver(Observer observer);

    /**
     * Unregisters an observer, removing it from the list of observers that are
     * notified of events. After removal, the observer will no longer receive
     * notifications from this subject.
     *
     * @param observer The {@link Observer} instance to be removed from the
     *                 subject's list of observers.
     */
    public void removeObserver(Observer observer);

    /**
     * Notifies all registered observers of an event. This method is called to
     * inform observers about state changes or significant events, allowing
     * them to update their state in response.
     *
     * @param event The {@link Event} that has occurred, to be passed to observers
     *              during notification.
     */
    public void notify(Event event);
}
