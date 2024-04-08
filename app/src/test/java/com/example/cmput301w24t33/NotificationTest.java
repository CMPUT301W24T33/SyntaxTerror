// FILEPATH: com/example/cmput301w24t33/notifications/Notification.java

package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;

import com.example.cmput301w24t33.notifications.Notification;
import com.google.firebase.Timestamp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;


/**
 * Provides unit tests for the {@link Notification} class to verify its functionality.
 * <p>
 * Tests focus on validating the correct initialization and modification of notification
 * properties, such as title and message, as well as the automatic generation of a timestamp.
 */
public class NotificationTest {

    /**
     * Tests the {@link Notification} constructor for proper initialization.
     * <p>
     * Verifies that the title and message properties are correctly set based on constructor
     * parameters and that a timestamp is generated (not null) upon instantiation.
     */
    @Test
    public void testConstructor() {
        Notification notification = new Notification("Test Title", "Test Message");

        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        // Assert that the timestamp is not null, indicating it has been set
        assertNotNull("Timestamp should not be null", notification.getTimestamp());
    }

    /**
     * Tests setting a new title on a {@link Notification} object.
     * <p>
     * Verifies that the title of a Notification can be successfully updated after instantiation
     * and that the updated title is correctly returned by the getter method.
     */
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
}