// FILEPATH: com/example/cmput301w24t33/notifications/Notification.java

package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;

import com.example.cmput301w24t33.notifications.Notification;
import com.google.firebase.Timestamp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;


/**
 * Unit tests for the Notification class.
 */
public class NotificationTest {



    @Test
    public void testConstructor() {
        Notification notification = new Notification("Test Title", "Test Message");

        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        // Assert that the timestamp is not null, indicating it has been set
        assertNotNull("Timestamp should not be null", notification.getTimestamp());
    }




    @Test
    public void testSetTitle() {
        Notification notification = new Notification("Test Title", "Test Message");
        notification.setTitle("New Title");

        assertEquals("New Title", notification.getTitle());
    }

    @Test
    public void testSetMessage() {
        Notification notification = new Notification("Test Title", "Test Message");
        notification.setMessage("New Message");

        assertEquals("New Message", notification.getMessage());
    }
}