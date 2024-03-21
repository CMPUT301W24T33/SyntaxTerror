package com.example.cmput301w24t33.notifications;

import static android.content.ContentValues.TAG;

import android.util.Log;
import com.example.cmput301w24t33.events.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NotificationUpdateListener updateListener;
    private Map<String, ListenerRegistration> activeListeners = new HashMap<>();

    /**
     * Constructs a NotificationRepository with a specific update listener. This repository manages notifications and their updates.
     * @param updateListener The listener that handles notification updates.
     */
    public NotificationRepository(NotificationUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    /**
     * Interface for handling notification updates. Implementations of this interface will receive updates for notifications.
     */
    public interface NotificationUpdateListener {
        void onNotificationUpdate(Event event, Notification notification);
    }

    /**
     * Interface for handling the fetch result of notifications. Implementations of this interface will receive a list of fetched notifications.
     */
    public interface NotificationsFetchListener {
        void onFetched(List<Notification> notifications);
    }

    /**
     * Listens for updates to notifications for the specified events. It adds snapshot listeners to each event's notifications and updates the UI through the NotificationUpdateListener.
     * @param eventIds A list of event IDs to listen for notification updates.
     */
    public void listenForEventNotificationUpdates(List<String> eventIds) {
        // Remove listeners for event IDs that are no longer active
        new ArrayList<>(activeListeners.keySet()).forEach(existingEventId -> {
            if (!eventIds.contains(existingEventId)) {
                ListenerRegistration registration = activeListeners.remove(existingEventId);
                if (registration != null) {
                    registration.remove();
                }
            }
        });

        // Add new listeners for event IDs that don't have one yet
        eventIds.forEach(eventId -> {
            if (!activeListeners.containsKey(eventId)) {
                final boolean[] isFirstInvocation = {true};

                ListenerRegistration listener = db.collection("events").document(eventId)
                        .collection("notifications")
                        .addSnapshotListener((snapshots, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (isFirstInvocation[0]) {
                                isFirstInvocation[0] = false;
                                return;
                            }
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    Notification newNotification = dc.getDocument().toObject(Notification.class);
                                    // Fetch the corresponding event for each notification
                                    db.collection("events").document(eventId).get()
                                            .addOnSuccessListener(eventSnapshot -> {
                                                Event event = eventSnapshot.toObject(Event.class);
                                                if (event != null && newNotification.getTimestamp() != null) {
                                                    updateListener.onNotificationUpdate(event, newNotification);
                                                }
                                            })
                                            .addOnFailureListener(e -> Log.e(TAG, "Error fetching event details", e));
                                }
                            }
                        });
                activeListeners.put(eventId, listener);
            }
        });
    }



    /**
     * Adds a new notification to the specified event. Upon completion, it calls the provided OnCompleteListener.
     * @param eventId The ID of the event to which the notification will be added.
     * @param notification The notification object to be added.
     * @param onCompleteListener The listener to be called upon the operation's completion.
     */
    // Add a new notification to an event
    public void addNotification(String eventId, Notification notification, OnCompleteListener<DocumentReference> onCompleteListener) {
        db.collection("events").document(eventId).collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> {
                    // Successfully added the notification, now set the ID field
                    String notificationId = documentReference.getId();
                    notification.setId(notificationId);
                    documentReference.update("id", notificationId)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Notification ID set in the document: " + notificationId))
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to set notification ID in the document", e));
                })
                .addOnFailureListener(e -> Log.w(TAG, "Create notification failed", e))
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * Deletes a specified notification from an event. Upon completion, it calls the provided OnCompleteListener.
     * @param eventId The ID of the event from which the notification will be deleted.
     * @param notificationId The ID of the notification to be deleted.
     * @param onCompleteListener The listener to be called upon the operation's completion.
     */
    public void deleteNotification(String eventId, String notificationId, OnCompleteListener<Void> onCompleteListener) {
        if (eventId == null || notificationId == null) {
            Log.e(TAG, "Event ID or Notification ID is null. Deletion cannot proceed.");
            return;
        }
        db.collection("events").document(eventId).collection("notifications").document(notificationId)
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }


    /**
     * Fetches all notifications for a given event and passes them to the NotificationsFetchListener.
     * @param eventId The ID of the event for which notifications are being fetched.
     * @param listener The listener that will receive the fetched notifications.
     */
    public void fetchNotificationsForEvent(String eventId, NotificationsFetchListener listener) {
        db.collection("events").document(eventId).collection("notifications")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Notification> notifications = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            notifications.add(document.toObject(Notification.class));
                        }
                        listener.onFetched(notifications);
                    } else {
                        Log.e(TAG, "Error fetching notifications", task.getException());
                        listener.onFetched(Collections.emptyList()); // Or handle the error as you see fit
                    }
                });
    }

}
