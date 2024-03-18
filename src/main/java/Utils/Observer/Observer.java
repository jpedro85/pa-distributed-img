package Utils.Observer;

import Utils.Events.Event;

public interface Observer {

    public void update(Subject subject, Event event);
}
