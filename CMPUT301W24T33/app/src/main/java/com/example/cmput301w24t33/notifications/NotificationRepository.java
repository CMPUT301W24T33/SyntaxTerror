package com.example.cmput301w24t33.notifications;

import static android.content.ContentValues.TAG;

import android.util.Log;
import com.example.cmput301w24t33.events.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NotificationRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NotificationUpdateListener updateListener;

    public NotificationRepository(NotificationUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public interface NotificationUpdateListener {
        void onNotificationUpdate(Event event, Notification notification);
    }

    public interface NotificationsFetchListener {
        void onFetched(List<Notification> notifications);
    }


    public void listenForEventNotificationUpdates(List<String> eventIds) {
        for (String eventId : eventIds) {
            db.collection("events").document(eventId)
                    .addSnapshotListener((eventDocumentSnapshot, e) -> {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        if (eventDocumentSnapshot != null && eventDocumentSnapshot.exists()) {
                            Event event = eventDocumentSnapshot.toObject(Event.class);
                            eventDocumentSnapshot.getReference().collection("notifications")
                                    .addSnapshotListener((snapshots, error) -> {
                                        if (error != null) {
                                            Log.w(TAG, "Listen failed.", error);
                                            return;
                                        }
                                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                                Notification newNotification = dc.getDocument().toObject(Notification.class);
                                                updateListener.onNotificationUpdate(event, newNotification);
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

    // Add a new notification to an event
    public void addNotification(String eventId, Notification notification, OnCompleteListener<DocumentReference> onCompleteListener) {
        db.collection("events").document(eventId).collection("notifications")
                .add(notification)
                .addOnCompleteListener(onCompleteListener);
    }

    // Delete a notification from an event
    public void deleteNotification(String eventId, String notificationId, OnCompleteListener<Void> onCompleteListener) {
        db.collection("events").document(eventId).collection("notifications").document(notificationId)
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }

    // Fetch all notifications for a given event
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



/* Example usage of addnotification

// Make sure to use NotificationManager to call addnotification, not notification repo
Notification notification = new Notification("New Event", "Event starting soon", "2021-12-01T15:00:00");
notificationRepository.addNotification(eventId, notification, task -> {
    if (task.isSuccessful()) {
        // Successfully added the notification
        String notificationId = task.getResult().getId();
        // Optionally, update the notification with its ID or perform other actions
        Log.d(TAG, "Notification added with ID: " + notificationId);
    } else {
        // Handle failure
        Log.e(TAG, "Error adding notification", task.getException());
    }
});

 */
