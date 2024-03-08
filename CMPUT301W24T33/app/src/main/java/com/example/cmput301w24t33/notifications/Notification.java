// Purpose:
// Represents a notification entity in an application, containing details such as title, message,
// and timestamp, facilitating storage and retrieval of notification information.
//
// Issues: None
//

package com.example.cmput301w24t33.notifications;

/**
 * Represents a notification with a title, message, and timestamp.
 * This class can be used to model notifications in an application,
 * allowing for easy storage and retrieval of notification details.
 */
public class Notification {
    private String title;
    private String message;
    private String timestamp;

    /**
     * Constructs a new Notification with specified title, message, and timestamp.
     *
     * @param title The title of the notification.
     * @param message The message body of the notification.
     * @param timestamp The timestamp when the notification was created or received.
     */
    public Notification(String title, String message, String timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Returns the title of the notification.
     *
     * @return The notification title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the notification.
     *
     * @param title The new title of the notification.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the message of the notification.
     *
     * @return The notification message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the notification.
     *
     * @param message The new message of the notification.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the timestamp of the notification.
     *
     * @return The notification timestamp.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the notification.
     *
     * @param timestamp The new timestamp of the notification.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
