package Utils.Events;

import Utils.Events.Enums.*;
import Utils.Events.InterfaceEvents.InterfaceEventWithName;
import Utils.Events.InterfaceEvents.InterfaceEventWithNames;
import Utils.Events.InterfaceEvents.LoadedImageEvent;
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

    @Test
    @DisplayName("Create a LoadedImageEvent and validate its properties")
    void testCreateLoadedImageEvent_ValidInput() {
        String message = "Test LoadedImageEvent";
        EventTypes type = EventTypes.IMAGE;
        String name = "Test Image";
        BufferedImage loadedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        Event event = EventFactory.createLoadedImageEvent(message, type, name, loadedImage);

        assertNotNull(event);
        assertTrue(event instanceof LoadedImageEvent);
        assertEquals(message, event.getMessage());
        assertEquals(type, event.getType());
        LoadedImageEvent loadedImageEvent = (LoadedImageEvent) event;
        assertEquals(name, loadedImageEvent.getName());
        assertEquals(loadedImage, loadedImageEvent.getImage());
    }

    @Test
    @DisplayName("Create a LoadedImageEvent but with Null Message")
    void testCreateLoadedImageEvent_NullMessage() {
        EventTypes type = EventTypes.IMAGE;
        String name = "Test Image";
        BufferedImage loadedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createLoadedImageEvent(null, type, name, loadedImage);
        });
    }

    @Test
    @DisplayName("Create a LoadedImageEvent but with Null Name")
    void testCreateLoadedImageEvent_NullName() {
        String message = "Test LoadedImageEvent";
        EventTypes type = EventTypes.IMAGE;
        BufferedImage loadedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createLoadedImageEvent(message, type, null, loadedImage);
        });
    }

    @Test
    @DisplayName("Create a LoadedImageEvent but with Null LoadedImage")
    void testCreateLoadedImageEvent_NullLoadedImage() {
        String message = "Test LoadedImageEvent";
        EventTypes type = EventTypes.IMAGE;
        String name = "Test Image";

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createLoadedImageEvent(message, type, name, null);
        });
    }

    @Test
    @DisplayName("Create a LoadedImageEvent but with Incorrect EventType")
    void testCreateLoadedImageEvent_IncorrectEventType() {
        String message = "Test LoadedImageEvent";
        EventTypes type = EventTypes.REGULAR;
        String name = "Test Image";
        BufferedImage loadedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createLoadedImageEvent(message, type, name, loadedImage);
        });
    }

    // Test createInterfaceEventWithNames method
    @Test
    @DisplayName("Create an InterfaceEventWithNames and validate its properties")
    void testCreateInterfaceEventWithNames_ValidInput() {
        String message = "Test InterfaceEventWithNames";
        EventTypes type = EventTypes.INTERFACE;
        InterfaceEvents events = InterfaceEvents.START_ALL;
        String[] names = {"Name1", "Name2", "Name3"};

        Event event = EventFactory.createInterfaceEventWithNames(message, type, events, names);

        assertNotNull(event);
        assertTrue(event instanceof InterfaceEventWithNames);
        assertEquals(message, event.getMessage());
        assertEquals(type, event.getType());
        InterfaceEventWithNames interfaceEventWithNames = (InterfaceEventWithNames) event;
        assertEquals(events, interfaceEventWithNames.getEvent());
        assertArrayEquals(names, interfaceEventWithNames.getNames());
    }

    @Test
    @DisplayName("Create an InterfaceEventWithNames but with Null Message")
    void testCreateInterfaceEventWithNames_NullMessage() {
        EventTypes type = EventTypes.IMAGE;
        InterfaceEvents events = InterfaceEvents.START_ALL;
        String[] names = {"Name1", "Name2", "Name3"};

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createInterfaceEventWithNames(null, type, events, names);
        });
    }

    @Test
    @DisplayName("Create an InterfaceEventWithNames but with Null Names")
    void testCreateInterfaceEventWithNames_NullNames() {
        String message = "Test InterfaceEventWithNames";
        EventTypes type = EventTypes.IMAGE;
        InterfaceEvents events = InterfaceEvents.START_ALL;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createInterfaceEventWithNames(message, type, events, null);
        });
    }

    @Test
    @DisplayName("Create an InterfaceEventWithNames but with Empty Names")
    void testCreateInterfaceEventWithNames_EmptyNames() {
        String message = "Test InterfaceEventWithNames";
        EventTypes type = EventTypes.IMAGE;
        InterfaceEvents events = InterfaceEvents.START_ALL;
        String[] names = {};

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createInterfaceEventWithNames(message, type, events, names);
        });
    }

    @Test
    @DisplayName("Create an InterfaceEventWithNames but with Incorrect EventType")
    void testCreateInterfaceEventWithNames_IncorrectEventType() {
        String message = "Test InterfaceEventWithNames";
        EventTypes type = EventTypes.REGULAR;
        InterfaceEvents events = InterfaceEvents.START_ALL;
        String[] names = {"Name1", "Name2", "Name3"};

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createInterfaceEventWithNames(message, type, events, names);
        });
    }

    // Test createInterfaceEventWithName method
    @Test
    @DisplayName("Create an InterfaceEventWithName and validate its properties")
    void testCreateInterfaceEventWithName_ValidInput() {
        String message = "Test InterfaceEventWithName";
        EventTypes type = EventTypes.INTERFACE;
        InterfaceEvents events = InterfaceEvents.START;
        String name = "Name1";

        Event event = EventFactory.createInterfaceEventWithName(message, type, events, name);

        assertNotNull(event);
        assertTrue(event instanceof InterfaceEventWithName);
        assertEquals(message, event.getMessage());
        assertEquals(type, event.getType());
        InterfaceEventWithName interfaceEventWithName = (InterfaceEventWithName) event;
        assertEquals(events, interfaceEventWithName.getEvent());
        assertEquals(name, interfaceEventWithName.getName());
    }

    @Test
    @DisplayName("Create an InterfaceEventWithName but with Null Message")
    void testCreateInterfaceEventWithName_NullMessage() {
        EventTypes type = EventTypes.IMAGE;
        InterfaceEvents events = InterfaceEvents.START;
        String name = "Name1";

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createInterfaceEventWithName(null, type, events, name);
        });
    }

    @Test
    @DisplayName("Create an InterfaceEventWithName but with Null Name")
    void testCreateInterfaceEventWithName_NullName() {
        String message = "Test InterfaceEventWithName";
        EventTypes type = EventTypes.IMAGE;
        InterfaceEvents events = InterfaceEvents.START;

        assertThrows(IllegalArgumentException.class, () -> {
            EventFactory.createInterfaceEventWithName(message, type, events, null);
        });
    }



}
