// Purpose:
// The EventRepository class manages the retrieval and updates of Event data from Firebase Firestore.
// It is responsible for querying Firestore for event data, listening for real-time updates, and
// providing callbacks for successful data retrieval or errors.

package com.example.cmput301w24t33.events;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the retrieval, updating, and real-time monitoring of event data from Firebase Firestore.
 */
public class EventRepository {
    private static EventRepository instance;
    private final FirebaseFirestore db;
    private final CollectionReference eventsCollection;
    private Application application;
    private EventCallback eventCallback;
    private Map<String, ListenerRegistration> activeListeners = new HashMap<>();

    private EventRepository(Application application, FirebaseFirestore db) {
        this.db = db;
        this.application = application;
        eventsCollection = this.db.collection("events");
    }

    /**
     * Initializes a singleton instance of the EventRepository with a given application context and Firestore database reference.
     * @param application The application context used for initializing the repository.
     * @param db The Firestore database reference.
     */
    public static synchronized void initialize(Application application, FirebaseFirestore db) {
        if (instance == null) {
            instance = new EventRepository(application, db);
        }
    }

    /**
     * Retrieves the singleton instance of EventRepository.
     * @return The singleton instance of EventRepository.
     * @throws IllegalStateException if the repository has not been initialized before use.
     */
    public static synchronized EventRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Event Repository must be initialized before use.");
        }
        return instance;
    }

    /**
     * Interface defining callbacks for event data operations.
     */
    public interface EventCallback {
        void onEventsLoaded(List<Event> events);
        void onFailure(Exception e);
    }

    /**
     * Sets the callback to be used for event data operations.
     * @param eventCallback The callback implementation.
     */
    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    /**
     * Registers a Firestore listener for real-time updates to all events, notifying the EventCallback on data changes.
     */
    public void setEventsSnapshotListener() {
        removeAllListeners();
        ListenerRegistration listener = eventsCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                eventCallback.onFailure(e);
                return;
            }
            List<Event> events = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Event event = doc.toObject(Event.class);
                events.add(event);
            }
            eventCallback.onEventsLoaded(events);
        });
        activeListeners.put("allEvents", listener);
    }

    /**
     * Registers a Firestore listener for real-time updates to events organized by a specific user.
     * @param organizerId The ID of the event organizer.
     */
    public void setEventByOrganizerSnapshotListener(String organizerId) {
        removeAllListeners();
        ListenerRegistration listener = eventsCollection.whereEqualTo("organizerId", organizerId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        eventCallback.onFailure(e);
                        return;
                    }
                    List<Event> events = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Event event = doc.toObject(Event.class);
                        events.add(event);
                    }
                    eventCallback.onEventsLoaded(events);
                });
        activeListeners.put("organizer_" + organizerId, listener);
    }

    /**
     * Sets a Firestore listener for real-time updates to a single event, identified by its ID.
     * @param eventId The unique ID of the event to monitor.
     */
    public void setEventListener(String eventId) {
        removeAllListeners();
        ListenerRegistration listener = eventsCollection.document(eventId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        eventCallback.onFailure(e);
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Event event = documentSnapshot.toObject(Event.class);
                        List<Event> singleEventList = new ArrayList<>();
                        singleEventList.add(event);
                        eventCallback.onEventsLoaded(singleEventList);
                    } else {
                        eventCallback.onFailure(new Exception("Document does not exist"));
                    }
                });
        activeListeners.put("event_" + eventId, listener);
    }

    /**
     * Removes all active Firestore listeners to prevent memory leaks and unnecessary network usage.
     */
    public void removeAllListeners() {
        for (ListenerRegistration listener : activeListeners.values()) {
            if (listener != null) {
                listener.remove();
            }
        }
        activeListeners.clear();
    }


    /**
     * Updates an existing event document in Firestore with new data from an Event object.
     *
     * @param event The event object containing updated information.
     */
    public void updateEvent(Event event) {
        String eventId = event.getEventId();
        DocumentReference docRef = eventsCollection.document(eventId);

        docRef.set(event)
                .addOnSuccessListener(aVoid -> Log.d("document", "Document update success: " + eventId))
                .addOnFailureListener(e -> Log.w("document", "Document update failed", e));
    }

    /**
     * Creates a new event document in Firestore and sets the document ID on the event object.
     *
     * @param event The new event to add to Firestore.
     */
    public void createEvent(Event event) {
        eventsCollection.add(event)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    event.setEventId(documentId);
                    eventsCollection.document(documentId)
                            .update("eventId", documentId)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "eventId added to document: " + documentId))
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to add eventId", e));
                    NotificationManager.getInstance().trackAttendeeUpdatesForEvent(event.getEventId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Create document failed", e));
    }

    /**
     * deletes an event from the repository
     * @param event event to be deleted
     */
    public void deleteEvent(Event event) {
        eventsCollection.document(event.getEventId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}
