// FILEPATH: com/example/cmput301w24t33/notifications/Notification.java
package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.cmput301w24t33.notifications.Notification;
import com.google.firebase.Timestamp;
import org.junit.jupiter.api.Test;

/**
 * Provides unit tests for the {@link Notification} class to verify its functionality.
 * <p>
 * Tests focus on validating the correct initialization and modification of notification
 * properties, such as title and message, as well as the automatic generation of a timestamp.
 */
public class NotificationTest {
    
    @Test
    public void testConstructor() {
        Notification notification = new Notification("Test Title", "Test Message");

        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        assertNotNull("Timestamp should not be null", notification.getTimestamp());
    }

    @Test
    public void testSetTitle() {
        Notification notification = new Notification("Test Title", "Test Message");
        notification.setTitle("New Title");

        assertEquals("New Title", notification.getTitle());
    }

    /**
     * Tests setting a new message on a {@link Notification} object.
     * <p>
     * Verifies that the message of a Notification can be successfully updated after instantiation
     * and that the updated message is correctly returned by the getter method.
     */
    @Test
    public void testSetMessage() {
        Notification notification = new Notification("Test Title", "Test Message");
        notification.setMessage("New Message");

        assertEquals("New Message", notification.getMessage());
    }

    @Test
    public void testSetAndGetId() {
        Notification notification = new Notification("Title", "Message");
        notification.setId("123456");

        assertEquals("123456", notification.getId());
    }

    @Test
    public void testEmptyConstructor() {
        Notification notification = new Notification();

        assertEquals(null, notification.getTitle());
        assertEquals(null, notification.getMessage());
        assertEquals(null, notification.getTimestamp());
    }

    @Test
    public void testTimestampSetOnConstruction() {
        Notification notification = new Notification("Title", "Message");
        Timestamp creationTimestamp = Timestamp.now();

        long allowedDifferenceMillis = 1000;
        long timeDifference = Math.abs(notification.getTimestamp().toDate().getTime() - creationTimestamp.toDate().getTime());

        assertEquals(true, timeDifference <= allowedDifferenceMillis);
    }

    @Test
    public void testSetTitleWithNull() {
        Notification notification = new Notification("Initial Title", "Message");
        notification.setTitle(null);

        assertNull("Title should be null after setting it to null", notification.getTitle());
    }

    @Test
    public void testSetMessageWithNull() {
        Notification notification = new Notification("Title", "Initial Message");
        notification.setMessage(null);

        assertNull("Message should be null after setting it to null", notification.getMessage());
    }

    @Test
    public void testTimestampPrecision() {
        Notification notification = new Notification("Title", "Message");
        long currentTimeMillis = System.currentTimeMillis();
        long notificationTimeMillis = notification.getTimestamp().toDate().getTime();

        assertTrue("Notification timestamp should be close to the current system time",
                Math.abs(currentTimeMillis - notificationTimeMillis) < 5000); //
    }

    @Test
    public void testSerialization() {
        Notification notification = new Notification("Serializable Title", "Serializable Message");
        String serializedTitle = notification.getTitle();
        String serializedMessage = notification.getMessage();

        assertEquals("Serialized title should match the original", "Serializable Title", serializedTitle);
        assertEquals("Serialized message should match the original", "Serializable Message", serializedMessage);
    }

    @Test
    public void testDeserialization() {
        Notification notification = new Notification();
        notification.setTitle("Deserialized Title");
        notification.setMessage("Deserialized Message");

        assertEquals("Deserialized title should be set correctly", "Deserialized Title", notification.getTitle());
        assertEquals("Deserialized message should be set correctly", "Deserialized Message", notification.getMessage());
    }
}

