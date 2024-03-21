package Utils.Events.InterfaceEvents;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.InterfaceEvents;
import java.awt.image.BufferedImage;

/**
 * Represents an event indicating the loading of a new image.
 * This event provides information such as the type of event, message, event type, name of the image, and the image itself.
 */
public class LoadedImageEvent implements InterfaceEvent{

    private final EventTypes eventType;
    private final String message;
    private final InterfaceEvents event;
    private final String name;
    private final BufferedImage image;

    /**
     * Construct
     *   @param message     The message describing the event.
     *   @param eventType   The general category of the event, as defined by {@link EventTypes}.
     *   @param name        A String that identifies the image.(e.g. the path)
     *   @param image       The image that as been loaded
     */
    public LoadedImageEvent(EventTypes eventType, String message, String name, BufferedImage image) {
        this.eventType = eventType;
        this.message = message;
        this.event = InterfaceEvents.LOADED_IMAGE;
        this.name = name;
        this.image = image;
    }

    /**
     * @return The type of event (e.g., SUCCESS, ERROR).
     */
    @Override
    public EventTypes getType() {
        return this.eventType;
    }

    /**
     * @return A message associated with the event.
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * @return The specific interface event of {@link InterfaceEvents}.
     */
    @Override
    public InterfaceEvents getEvent() {
        return this.event;
    }

    /**
     * @return Name of the image path of the image
     */
    public String getName() {
        return name;
    }

    /**
     * @return BufferedImage the loaded image
     */
    public BufferedImage getImage() {
        return image;
    }
}
