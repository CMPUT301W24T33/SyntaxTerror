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
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the retrieval and updates of Event data from Firebase Firestore.
 * This repository is responsible for querying Firestore for event data, listening for real-time updates,
 * and providing callbacks for successful data retrieval or errors.
 */
public class EventRepository {
    private final FirebaseFirestore db;
    private final CollectionReference eventsCollection;
    private EventCallback eventCallback;

    /**
     * Constructs an EventRepository and initializes Firestore and events collection references.
     */
    public EventRepository() {
        db = FirebaseFirestore.getInstance();
        eventsCollection = db.collection("events");
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
     * Registers a listener for real-time updates to events a specific user has signed up for.
     *
     * @param userId The ID of the organizer to filter events by.
     */
    public void setEventBySignUpSnapshotListener(String userId) {
        eventsCollection.whereArrayContains("signedUp", userId)
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

    public void setEventByCheckedInSnapshotListener(String eventId){
        eventsCollection.whereEqualTo("eventId", eventId)
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
