package Utils.Events;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.InterfaceEvents;
import Utils.Events.Enums.ImageStates;

public class InterfaceEvent implements Event{

    private final String message;
    private final EventTypes eventType;
    private final InterfaceEvents event;

    /**
     * Constructs a new ImageStateEvent with the specified details.
     *
     * @param message    The message describing the event.
     * @param type       The general category of the event, as defined by
     *                   {@link EventTypes}.
     * @param event      The event category as defined by
     *                   {@link InterfaceEvents}.
     */
    public InterfaceEvent(String message, EventTypes type , InterfaceEvents event) {
        this.message = message;
        this.eventType = type;
        this.event = event;
    }

    @Override
    public EventTypes getType() {
        return this.eventType;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns the current state of the image processing task.
     *
     * @return The image processing state as an instance of {@link ImageStates}.
     */
    public ImageStates getImageState() {
        return imageState;
    }

    /**
     * Returns the {@link SplitImage} object that this event pertains to.
     *
     * @return The {@link SplitImage} object associated with this event.
     */
    public SplitImage getSplitImage() {
        return splitImage;
    }
}
