package Utils.Events.InterfaceEvents;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.InterfaceEvents;
import java.awt.image.BufferedImage;

/**
 * Represents an event indicating the loading of a new image.
 * This event provides information such as the type of event, message, event type, name of the image, and the image itself.
 */
public class NewLoadedImageEvent implements InterfaceEvent{

    private final EventTypes eventType;
    private final String message;
    private final InterfaceEvents event;
    private final String name;
    private final BufferedImage image;

    /**
     * Construct
     * @param eventType
     * @param message
     * @param event
     * @param name
     * @param image
     */
    public NewLoadedImageEvent(EventTypes eventType, String message, InterfaceEvents event, String name, BufferedImage image) {
        this.eventType = eventType;
        this.message = message;
        this.event = event;
        this.name = name;
        this.image = image;
    }

    /**
     * The type of event (e.g., SUCCESS, ERROR).
     */
    @Override
    public EventTypes getType() {
        return this.eventType;
    }

    /**
     * A message associated with the event.
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public InterfaceEvents getEvent() {
        return this.event;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }
}
