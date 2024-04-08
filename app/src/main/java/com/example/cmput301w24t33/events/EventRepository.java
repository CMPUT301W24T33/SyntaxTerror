// Purpose:
// The EventRepository class manages the retrieval and updates of Event data from Firebase Firestore.
// It is responsible for querying Firestore for event data, listening for real-time updates, and
// providing callbacks for successful data retrieval or errors.
//
// Issues: Create method to allow events to be deleted
//         Create method to populate a users events they have signed up to attend
//         Create method that limits number of attendees to an event on optional limit
//         Create method to obtain poster for event
//         Create method to obtain an organizer users reusable qr codes from events already over
//

package com.example.cmput301w24t33.events;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;

import android.app.Application;
import android.util.Log;

import com.example.cmput301w24t33.notifications.NotificationManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the retrieval and updates of Event data from Firebase Firestore.
 * This repository is responsible for querying Firestore for event data, listening for real-time updates,
 * and providing callbacks for successful data retrieval or errors.
 */
public class EventRepository {
    private static EventRepository instance;
    private final FirebaseFirestore db;
    private final CollectionReference eventsCollection;
    private Application applicaiton;
    private EventCallback eventCallback;
    private ListenerRegistration eventListener;

    /**
     * Constructs an EventRepository and initializes Firestore and events collection references.
     */
    public EventRepository(Application applicaiton, FirebaseFirestore db) {
        this.db = db;
        this.applicaiton = applicaiton;
        eventsCollection = this.db.collection("events");
    }

    public static synchronized  void initialize(Application application, FirebaseFirestore db) {
        if (instance == null) {
            instance = new EventRepository(application, db);
        }
    }

    public static synchronized EventRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Event Repository must be initialized in the Application class before use.");
        }
        return instance;
    }
    /**
     * Interface defining callbacks for event data loading operations.
     */
    public interface EventCallback {
        void onEventsLoaded(List<Event> events); // Called when events are successfully loaded.

        void onFailure(Exception e); // Called when an error occurs during event data fetching.
    }

    /**
     * Sets the callback to be used for event data operations.
     *
     * @param eventCallback The callback implementation to handle event data operations.
     */
    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    /**
     * Registers a listener for real-time updates to all events in the Firestore database.
     * Notifies the set EventCallback on successful data retrieval or failure.
     */
    public void setEventsSnapshotListener() {
        eventsCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
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
    }

    /**
     * Registers a listener for real-time updates to events organized by a specific user.
     *
     * @param organizerId The ID of the organizer to filter events by.
     */
    public void setEventByOrganizerSnapshotListener(String organizerId) {
        eventsCollection.whereEqualTo("organizerId", organizerId)
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
    }

    /**
     * Sets a listener for real-time updates to a single event by its ID.
     * This method can be used by any fragment to observe changes to the event's data.
     *
     * @param eventId The ID of the event to observe.
     */
    public void setEventListener(String eventId) {
        if (eventListener != null) {
            eventListener.remove();
        }
        eventListener = eventsCollection.document(eventId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        eventCallback.onFailure(e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        List<Event> events = new ArrayList<>();
                        Event event = documentSnapshot.toObject(Event.class);
                        events.add(event);
                        eventCallback.onEventsLoaded(events);
                    } else {
                        Log.d(TAG, "Current data: null");
                        eventCallback.onFailure(new Exception("Document does not exist"));
                    }
                });
    }

    /**
     * Removes the single event listener if it exists.
     */
    public void removeEventListener() {
        if (eventListener != null) {
            eventListener.remove();
            eventListener = null;
            Log.d(TAG, "Single event listener removed successfully");
        }
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
