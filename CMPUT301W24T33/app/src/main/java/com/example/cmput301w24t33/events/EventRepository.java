package com.example.cmput301w24t33.events;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventRepository {
    private final FirebaseFirestore db;
    private final CollectionReference eventsCollection;
    private EventCallback eventCallback;

    /**
     *
     */
    public EventRepository() {
        db = FirebaseFirestore.getInstance();
        eventsCollection = db.collection("events");
    }

    /**
     * Interface provides options for different event loading and event fetch failures
     */
    public interface EventCallback {
        void onEventsLoaded(List<Event> events);
        void onFailure(Exception e);
    }

    /**
     * Initializes eventCallback
     * @param eventCallback an EventCallback to initialize for facilitating query result returns
     */
    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    /**
     * Sets database listener to check and reflect any changes to events in our Firestore database.
     * <p>
     *     This method send Firestore error message as a parameter to the EventCallback function if the EventListener
     *     encounters any Firestore exceptions.
     *     If no errors/exceptions are encountered, each document from the "events" collection is parsed into a
     *     new Event object and added to the eventList. eventAdapter is then notified of changes.
     * </p>
     * @see Event
     * @see EventCallback
     */
    public void setEventsSnapshotListener() {
        eventsCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Firebase error
                eventCallback.onFailure(e);
            }
            List<Event> events = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                // Adds each event in our collection to events
                Event event = doc.toObject(Event.class);
                events.add(event);
            }
            // Sets MutableLiveData<List<Event>> with current list of Events
            eventCallback.onEventsLoaded(events);
        });
    }

    /**
     *
     */
    public void setSingleEventSnapshotListener(String eventId) {

    }

    /**
     * Sets database listener to check and reflect any changes to current user's organized events in our Firestore database.
     * <p>
     *     This method send Firestore error message as a parameter to the EventCallback function if the EventListener
     *     encounters any Firestore exceptions.
     *     If no errors/exceptions are encountered, each event from the "events" collection is parsed into a
     *     new Event object and added to the eventList. eventAdapter is then notified of changes.
     * </p>
     * @see Event
     * @see EventCallback
     */
    public void setEventByOrganizerSnapshotListener(String organizerId) {
        eventsCollection.whereEqualTo("organizerId", organizerId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Error encountered
                        eventCallback.onFailure(e);
                    }
                    List<Event> events = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        // Adds each event in our collection with matching organizerId to events
                        Event event = doc.toObject(Event.class);
                        events.add(event);
                    }
                    // Sets MutableLiveData<List<Event>> with current list of Events
                    eventCallback.onEventsLoaded(events);
                });
    }

    public void updateEvent(Event event) {
        String eventId = event.getEventId();
        Log.d(TAG, "Edit Event: " + eventId);
        DocumentReference docRef = eventsCollection.document(eventId);

        docRef.set(event)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Document update success" + eventId);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Document update failed", e);
                });
    }

    public void createEvent(Event event) {
        eventsCollection.add(event)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    event.setEventId(documentId);
                    eventsCollection.document(documentId)
                            .update("eventId", documentId)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "eventId added to document");
                            }).addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to add eventId", e);
                            });
                    Log.d(TAG, "Create Document success: " + documentId);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Create Document failed", e);
                });
    }

}
