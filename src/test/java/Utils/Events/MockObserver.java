package Utils.Events;

import Utils.Observer.Observer;
import Utils.Observer.Subject;

import java.util.ArrayList;

public  class MockObserver implements Observer {
    private final ArrayList<Event> events = new ArrayList<>();

    @Override
    public void update(Subject subject, Event event) {
        events.add(event);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
