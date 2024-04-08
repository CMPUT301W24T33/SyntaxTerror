// Purpose:
// Represents a notification entity in an application, containing details such as title, message,
// and timestamp, facilitating storage and retrieval of notification information.
//
// Issues: None
//

package com.example.cmput301w24t33.notifications;

import com.google.firebase.Timestamp;


/**
 * Represents a notification with a title, message, and timestamp.
 * This class can be used to model notifications in an application,
 * allowing for easy storage and retrieval of notification details.
 */
public class Notification {
    private String id;
    private String title;
    private String message;
    private Timestamp timestamp;

    /**
     * Constructs a new Notification with specified title, message, and timestamp.
     *
     * @param title The title of the notification.
     * @param message The message body of the notification.
     */
    public Notification(String title, String message) {
        this.title = title;
        this.message = message;
        this.timestamp = Timestamp.now();
    }

    // Empty constructor for Firebase
    public Notification() {}

    /**
     * Returns the ID of the notification.
     *
     * @return The notification title.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the notification..
     */
    public void setId(String id) {
        this.id = id;
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
    public Timestamp getTimestamp() {
        return timestamp;
    }

}
