package Utils.Observer;

import Utils.Events.Event;

public interface Subject {

    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);

    public void notify(Event event);
}
