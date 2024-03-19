package Utils.Events;

import Utils.Events.Enums.*;
import Utils.Image.SplitImage;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventFactoryTest {

    @Test
    @DisplayName("Create a Regular Event and validate its properties")
    public void testCreateRegularEvent_ValidInput() {
        String message = "Test Message";
        EventTypes type = EventTypes.REGULAR;

        Event event = EventFactory.createRegularEvent("Test Message", EventTypes.REGULAR);

        assertNotNull(event);
        assertInstanceOf(RegularEvent.class, event);
        assertAll(() -> {
            assertEquals(message, event.getMessage());
            assertEquals(type, event.getType());
        });
    }

    @Test
    @DisplayName("Create a Regular Event but with Null Message")
    public void testCreateRegularEvent_NullMessage() {
        EventTypes type = EventTypes.REGULAR;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createRegularEvent(null, type);
        });
    }

    @Test
    @DisplayName("Create a Regular Event but with Empty Message")
    public void testCreateRegularEvent_EmptyMessage() {
        EventTypes type = EventTypes.REGULAR;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createRegularEvent("", type);
        });
    }

    @Test
    @DisplayName("Create a Regular Event but with the type as null")
    public void testCreateRegularEvent_NullType() {
        String message = "Test Message";

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createRegularEvent(message, null);
        });
    }

    @Test
    @DisplayName("Create an Error Event and validate its properties")
    void testCreateErrorEvents_ValidInput() {
        String expectedMessage = "Test error event message";
        EventTypes expectedType = EventTypes.ERROR;
        SeverityLevels expectedSeverityLevel = SeverityLevels.WARNING;

        Event event = EventFactory.createErrorEvent(expectedMessage, expectedType, expectedSeverityLevel);

        assertNotNull(event);
        assertInstanceOf(ErrorEvent.class, event);
        assertAll(() -> {
            assertEquals(expectedMessage, event.getMessage());
            assertEquals(expectedType, event.getType());
            assertEquals(expectedSeverityLevel, ((ErrorEvent) event).getSeverityLevel());
        });
    }

    @Test
    @DisplayName("Create a Regular Event but with Null Message")
    public void testCreateErrorEvent_NullMessage() {
        EventTypes type = EventTypes.ERROR;
        SeverityLevels severityLevel = SeverityLevels.WARNING;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createErrorEvent(null, type, severityLevel);
        });
    }

    @Test
    @DisplayName("Create a Regular Event but with Empty Message")
    public void testCreateErrorEvent_EmptyMessage() {
        EventTypes type = EventTypes.ERROR;
        SeverityLevels severityLevel = SeverityLevels.WARNING;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createErrorEvent("", type, severityLevel);
        });
    }

    @Test
    @DisplayName("Create a Regular Event but with Null Type")
    public void testCreateErrorEvent_NullType() {
        String message = "Test Message";
        SeverityLevels severityLevel = SeverityLevels.WARNING;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createErrorEvent(message, null, severityLevel);
        });
    }

    @Test
    @DisplayName("Create a Regular Event but with Null Severity Level")
    public void testCreateErrorEvent_NullSeverityLevel() {
        String message = "Test Message";
        EventTypes type = EventTypes.ERROR;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createErrorEvent(message, type, null);
        });
    }

    @Test
    @DisplayName("Create an Image State Event and validate its properties")
    void testCreateImageStateEvent_ValidInput() {
        String expectedMessage = "Test ImageState message";
        EventTypes expectedType = EventTypes.IMAGE;
        ImageStates expectedState = ImageStates.MERGED;
        BufferedImage simpleImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        SplitImage expectedSplit = new SplitImage((short) 2, (short) 2, simpleImage);

        Event event = EventFactory.createImageStateEvent(expectedMessage, expectedType, expectedState, expectedSplit);

        assertNotNull(event);
        assertInstanceOf(ImageStateEvent.class, event);
        assertAll(() -> {
            assertEquals(expectedMessage, event.getMessage());
            assertEquals(expectedType, event.getType());
            assertEquals(expectedState, ((ImageStateEvent) event).getImageState());
            assertEquals(expectedSplit, ((ImageStateEvent) event).getSplitImage());
        });
    }

    @Test
    @DisplayName("Create an ImageState Event but with Null Message")
    public void testCreateImageStateEvent_NullMessage() {
        EventTypes type = EventTypes.IMAGE;
        ImageStates imageState = ImageStates.MERGED;
        SplitImage splitImage = new SplitImage((short) 2, (short) 2,
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createImageStateEvent(null, type, imageState, splitImage);
        });
    }

    @Test
    @DisplayName("Create an ImageState Event but with Empty Message")
    public void testCreateImageStateEvent_EmptyMessage() {
        EventTypes type = EventTypes.IMAGE;
        ImageStates imageState = ImageStates.MERGED;
        SplitImage splitImage = new SplitImage((short) 2, (short) 2,
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createImageStateEvent("", type, imageState, splitImage);
        });
    }

    @Test
    @DisplayName("Create an ImageState Event but with Null Type")
    public void testCreateImageStateEvent_NullType() {
        String message = "Test Message";
        ImageStates imageState = ImageStates.MERGED;
        SplitImage splitImage = new SplitImage((short) 2, (short) 2,
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createImageStateEvent(message, null, imageState, splitImage);
        });
    }

    @Test
    @DisplayName("Create an ImageState Event but with Null ImageState")
    public void testCreateImageStateEvent_NullImageState() {
        String message = "Test Message";
        EventTypes type = EventTypes.IMAGE;
        BufferedImage simpleImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        SplitImage splitImage = new SplitImage((short) 2, (short) 2, simpleImage);

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createImageStateEvent(message, type, null, splitImage);
        });
    }

    @Test
    @DisplayName("Create an ImageState Event but with Null ImageState")
    public void testCreateImageStateEvent_NullSplitImage() {
        String message = "Test Message";
        EventTypes type = EventTypes.IMAGE;
        ImageStates imageState = ImageStates.MERGED;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createImageStateEvent(message, type, imageState, null);
        });
    }

    @Test
    @DisplayName("Create a Server Event and validate its properties")
    void testCreateServerEvent_ValidInput() {
        String expectedMessage = "Test Server message";
        EventTypes expectedType = EventTypes.SERVER;
        ServerStates expectedServerState = ServerStates.RUNNING;
        int expectedIdentifier = 42;

        Event event = EventFactory.createServerEvent(expectedMessage, expectedType, expectedServerState,
                expectedIdentifier);

        assertNotNull(event);
        assertInstanceOf(ServerEvent.class, event);
        assertAll(() -> {
            assertEquals(expectedMessage, event.getMessage());
            assertEquals(expectedType, event.getType());
            assertEquals(expectedServerState, ((ServerEvent) event).getServerState());
            assertEquals(expectedIdentifier, ((ServerEvent) event).getServerIdentifier());
        });
    }

    @Test
    @DisplayName("Create a Server Event but with Null Message")
    public void testCreateServerEvent_NullMessage() {
        EventTypes type = EventTypes.SERVER;
        ServerStates serverState = ServerStates.RUNNING;
        int identifier = 42;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createServerEvent(null, type, serverState, identifier);
        });
    }

    @Test
    @DisplayName("Create a Server Event but with Empty Message")
    public void testCreateServerEvent_EmptyMessage() {
        EventTypes type = EventTypes.SERVER;
        ServerStates serverState = ServerStates.RUNNING;
        int identifier = 42;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createServerEvent("", type, serverState, identifier);
        });
    }

    @Test
    @DisplayName("Create a Server Event but with Null Type")
    public void testCreateServerEvent_NullType() {
        String message = "Test Message";
        ServerStates serverState = ServerStates.RUNNING;
        int identifier = 42;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createServerEvent(message, null, serverState, identifier);
        });
    }

    @Test
    @DisplayName("Create a Server Event but with Null ServerState")
    public void testCreateServerEvent_NullServerState() {
        String message = "Test Message";
        EventTypes type = EventTypes.SERVER;
        int identifier = 42;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createServerEvent(message, type, null, identifier);
        });
    }
}
