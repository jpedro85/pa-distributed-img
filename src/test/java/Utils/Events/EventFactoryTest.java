package Utils.Events;

import Utils.Events.Enums.*;
import Utils.Image.SplitImage;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventFactoryTest {

    @Test
    @DisplayName("Create a Regular Event and validate its properties")
    void testCreateRegularEvent() {
        Event event = EventFactory.createRegularEvent("Test Message", EventTypes.REGULAR);
        Assertions.assertAll(() -> {
            Assertions.assertTrue(event instanceof RegularEvent);
            Assertions.assertEquals("Test Message", event.getMessage());
            Assertions.assertEquals(EventTypes.REGULAR, event.getType());
        });
    }

    @Test
    @DisplayName("Create an Error Event and validate its properties")
    void testCreateErrorEvents() {
        String expectedMessage = "Test error event message";
        EventTypes expectedType = EventTypes.ERROR;
        SeverityLevels expectedSeverityLevel = SeverityLevels.WARNING;

        Event event = EventFactory.createErrorEvent(expectedMessage, expectedType, expectedSeverityLevel);

        Assertions.assertTrue(event instanceof ErrorEvent);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(expectedMessage, event.getMessage());
            Assertions.assertEquals(expectedType, event.getType());
            Assertions.assertEquals(expectedSeverityLevel, ((ErrorEvent) event).getSeverityLevel());
        });
    }

    @Test
    @DisplayName("Create an Image State Event and validate its properties")
    void testCreateImageStateEvent() {
        String expectedMessage = "Test ImageState message";
        EventTypes expectedType = EventTypes.IMAGE;
        ImageStates expectedImageState = ImageStates.MERGED;
        BufferedImage simpleImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        SplitImage expectedSplit = new SplitImage((short) 2, (short) 2, simpleImage);

        Event event = EventFactory.createImageStateEvent(expectedMessage, expectedType, expectedImageState,
                expectedSplit);

        Assertions.assertTrue(event instanceof ImageStateEvent);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(expectedMessage, event.getMessage());
            Assertions.assertEquals(expectedType, event.getType());
            Assertions.assertEquals(expectedImageState, ((ImageStateEvent) event).getImageState());
            Assertions.assertEquals(expectedSplit, ((ImageStateEvent) event).getSplitImage());
        });
    }

    @Test
    @DisplayName("Create a Server Event and validate its properties")
    void testCreateServerEvent() {
        String expectedMessage = "Test Server message";
        EventTypes expectedType = EventTypes.SERVER;
        ServerStates expectedServerState = ServerStates.RUNNING;
        int expectedIdentifier = 42;

        Event event = EventFactory.createServerEvent(expectedMessage, expectedType, expectedServerState,
                expectedIdentifier);

        Assertions.assertTrue(event instanceof ServerEvent);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(expectedMessage, event.getMessage());
            Assertions.assertEquals(expectedType, event.getType());
            Assertions.assertEquals(expectedServerState, ((ServerEvent) event).getServerState());
            Assertions.assertEquals(expectedIdentifier, ((ServerEvent) event).getServerIdentifier());
        });
    }
}
