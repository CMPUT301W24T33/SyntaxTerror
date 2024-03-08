// FILEPATH: com/example/cmput301w24t33/notifications/Notification.java

package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;

import com.example.cmput301w24t33.notifications.Notification;

import org.junit.Test;


/**
 * Unit tests for the Notification class.
 */
public class NotificationTest {

    @Test
    public void testConstructor() {
        Notification notification = new Notification("Test Title", "Test Message", "Test Timestamp");

        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        assertEquals("Test Timestamp", notification.getTimestamp());
    }

    @Test
    public void testSetTitle() {
        Notification notification = new Notification("Test Title", "Test Message", "Test Timestamp");
        notification.setTitle("New Title");

        assertEquals("New Title", notification.getTitle());
    }

    @Test
    public void testSetMessage() {
        Notification notification = new Notification("Test Title", "Test Message", "Test Timestamp");
        notification.setMessage("New Message");

        assertEquals("New Message", notification.getMessage());
    }

    @Test
    public void testSetTimestamp() {
        Notification notification = new Notification("Test Title", "Test Message", "Test Timestamp");
        notification.setTimestamp("New Timestamp");

        assertEquals("New Timestamp", notification.getTimestamp());
    }
}