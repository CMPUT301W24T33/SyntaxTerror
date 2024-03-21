package com.example.cmput301w24t33.notifications;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.example.cmput301w24t33.events.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NotificationManager {
    private static NotificationManager instance;
    private Application application;
    private NotificationRepository repository;

    private NotificationManager(Application application) {
        this.application = application;
        repository = new NotificationRepository(this::dispatchNotificationUpdate);
    }

    /**
     * Initializes the singleton instance of NotificationManager with the provided application context. This method should be called once, typically in your Application class.
     * @param application The application context used for initializing NotificationManager.
     */
    public static synchronized void initialize(Application application) {
        if (instance == null) {
            instance = new NotificationManager(application);
        }
    }

    /**
     * Retrieves the singleton instance of NotificationManager. If the instance has not been initialized yet, this method throws an IllegalStateException.
     * @return The singleton instance of NotificationManager.
     * @throws IllegalStateException if NotificationManager has not been initialized prior to calling getInstance().
     */
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("NotificationManager must be initialized in the Application class before use");
        }
        return instance;
    }

    /**
     * Updates the list of event IDs for which the NotificationManager should listen for notifications. This method configures the repository to listen for notifications related to the specified events.
     * @param eventIds A list of event IDs to listen for updates.
     */
    public void updateEventIdsOfInterest(List<String> eventIds) {
        repository.listenForEventNotificationUpdates(eventIds);
    }

    /**
     * Dispatches a notification update. If the notification's timestamp is after the app's initialization timestamp, it shows a toast with the event's name and notification message.
     * @param event The event associated with the notification.
     * @param notification The notification to be dispatched.
     */
    private void dispatchNotificationUpdate(Event event, Notification notification) {
        // Get the initialization timestamp
        long initTimestamp = getNotificationInitTimestamp();
        // Get notification timestamp
        long notificationTimestamp = notification.getTimestamp().toDate().getTime();
        // Check if the notification is newer than the app initialization
        if (notificationTimestamp > initTimestamp) {
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(application, event.getName() + ": " + notification.getMessage(), Toast.LENGTH_SHORT).show()
            );
        }
    }

    /**
     * Adds a new notification to the specified event and invokes the onCompleteListener upon completion.
     * @param eventId The ID of the event to which the notification should be added.
     * @param notification The notification object to add.
     * @param onCompleteListener The listener to be called upon completion of the add operation.
     */
    public void addNotification(String eventId, Notification notification, OnCompleteListener<DocumentReference> onCompleteListener) {
        repository.addNotification(eventId, notification, onCompleteListener);
    }

    /**
     * Deletes a notification from the specified event and invokes the onCompleteListener upon completion.
     * @param eventId The ID of the event from which the notification should be deleted.
     * @param notificationId The ID of the notification to delete.
     * @param onCompleteListener The listener to be called upon completion of the delete operation.
     */
    public void deleteNotification(String eventId, String notificationId, OnCompleteListener<Void> onCompleteListener) {
        repository.deleteNotification(eventId, notificationId, onCompleteListener);
    }


    /**
     * Fetches notifications for a specific event and invokes the provided listener with the results.
     * @param eventId The ID of the event for which to fetch notifications.
     * @param listener The listener to be invoked with the fetched notifications.
     */
    public void fetchNotificationsForEvent(String eventId, NotificationRepository.NotificationsFetchListener listener) {
        repository.fetchNotificationsForEvent(eventId, listener);
    }

    /**
     * Retrieves the app initialization timestamp from shared preferences. This timestamp is used to determine if a notification is new since the app was last initialized.
     * @return The timestamp of app initialization.
     */
    public long getNotificationInitTimestamp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        return prefs.getLong("notificationInitTimestamp", 0);
    }
}

