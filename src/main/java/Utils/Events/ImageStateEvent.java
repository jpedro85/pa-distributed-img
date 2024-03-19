package Utils.Events;

import Utils.Events.Enums.EventTypes;
import Utils.Events.Enums.ImageStates;
import Utils.Image.SplitImage;

/**
 * Represents an event related to the state of an image processing task within
 * the system. This class captures information about the specific stage an image
 * is in within its processing lifecycle, from initial receipt through to final
 * processing stages.
 *
 * <p>
 * An {@code ImageStateEvent} is used to notify interested parts of the system
 * about changes or updates in the state of image processing tasks. It
 * encapsulates details such as a descriptive message, the general type of the
 * event, the specific image processing state, and a reference to the
 * {@link SplitImage} object being processed.
 * </p>
 *
 * <p>
 * This class implements the {@link Event} interface, enabling it to be
 * processed by the system's event handling infrastructure. This allows for a
 * consistent approach to event management across different types of events
 * within the system.
 * </p>
 *
 * @see Event
 * @see EventTypes
 * @see ImageStates
 * @see SplitImage
 */
public class ImageStateEvent implements Event {

    private String message;
    private EventTypes eventType;
    private ImageStates imageState;
    private SplitImage splitImage;

    /**
     * Constructs a new ImageStateEvent with the specified details.
     *
     * @param message    The message describing the event.
     * @param type       The general category of the event, as defined by
     *                   {@link EventTypes}.
     * @param imageState The current state of the image processing task, as defined
     *                   by {@link ImageStates}.
     * @param splitImage The {@link SplitImage} object that this event pertains to.
     */
    public ImageStateEvent(String message, EventTypes type, ImageStates imageState, SplitImage splitImage) {
        this.message = message;
        this.eventType = type;
        this.imageState = imageState;
        this.splitImage = splitImage;
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
