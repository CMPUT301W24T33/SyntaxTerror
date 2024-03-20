package com.example.cmput301w24t33.notifications;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
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

    public static synchronized NotificationManager getInstance(Application application) {
        if (instance == null) {
            instance = new NotificationManager(application);
        }
        return instance;
    }

    public void updateEventIdsOfInterest(List<String> eventIds) {
        repository.listenForEventNotificationUpdates(eventIds);
    }

    private void dispatchNotificationUpdate(Event event, Notification notification) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(application, "New notification for " + event.getName() + ": " + notification.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    public void addNotification(String eventId, Notification notification, OnCompleteListener<DocumentReference> onCompleteListener) {
        repository.addNotification(eventId, notification, onCompleteListener);
    }

    public void deleteNotification(String eventId, String notificationId, OnCompleteListener<Void> onCompleteListener) {
        repository.deleteNotification(eventId, notificationId, onCompleteListener);
    }

    public void fetchNotificationsForEvent(String eventId, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        repository.fetchNotificationsForEvent(eventId, onCompleteListener);
    }
}


//    List<String> eventIds = Arrays.asList("eventId1", "eventId2", "eventId3"); // Example event IDs
//NotificationManager.getInstance(this.getApplication()).updateEventIdsOfInterest(eventIds);
