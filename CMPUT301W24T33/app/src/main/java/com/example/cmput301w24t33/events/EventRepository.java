package com.example.cmput301w24t33.events;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
     * Sets database listener to check and reflect any changes to events in our Firestore database.
     * <p>
     *     This method send Firestore error message as a parameter to the EventCallback function if the EventListener
     *     encounters any Firestore exceptions.
     *     If no errors/exceptions are encountered, each event from the "events" collection is parsed into a
     *     new Event object and added to the eventList. eventAdapter is then notified of changes.
     * </p>
     * @see Event
     * @see EventCallback
     */
    public void setEventSnapshotListener(EventCallback callback) {
        this.eventCallback = callback;

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
}
