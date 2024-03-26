// Purpose:
// Manages UI-related data for events, ensuring persistence through lifecycle changes and
// facilitating interaction between the UI and the EventRepository.
//
// Issues:
//

package com.example.cmput301w24t33.events;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * ViewModel class for managing UI-related data in a lifecycle-conscious way.
 * This class encapsulates the data for the events to survive configuration changes such as screen rotations.
 */
public class EventViewModel extends ViewModel {
    private final EventRepository eventRepo;
    private final MutableLiveData<List<Event>> eventsLiveData;

    /**
     * Constructor for EventViewModel. Initializes the EventRepository and sets up the event callback.
     */
    public EventViewModel() {
        eventRepo = new EventRepository();
        eventsLiveData = new MutableLiveData<>();
        setEventCallback(eventRepo);
    }

    /**
     * Sets the event callback on the repository to listen for data changes and errors.
     * @param eventRepo An instance of EventRepository to set the callback on.
     */
    private void setEventCallback(EventRepository eventRepo) {
        eventRepo.setEventCallback(new EventRepository.EventCallback() {
            @Override
            public void onEventsLoaded(List<Event> events) {
                // When events are successfully loaded, updates LiveData with the list of events.
                eventsLiveData.setValue(events);
            }

            @Override
            public void onFailure(Exception e) {
                // Logs the error encountered during event data fetching.
                Log.d(TAG, "Error loading events: ", e);
            }
        });
    }

    /**
     * Initiates loading of all events from the Firestore database.
     * The loaded events are observed by the LiveData within this ViewModel.
     */
    public void loadEvents() {
        eventRepo.setEventsSnapshotListener();
    }

    /**
     * Initiates loading of events organized by a specific user from the Firestore database.
     * @param organizerId The identifier of the user whose events are to be loaded.
     */
    public void loadOrganizerEvents(String organizerId) {
        eventRepo.setEventByOrganizerSnapshotListener(organizerId);
    }

    /**
     * Returns LiveData containing the list of events.
     * The LiveData can be observed by the UI components to respond to data changes.
     * @return A LiveData object containing a list of Event objects.
     */
    public LiveData<List<Event>> getEventsLiveData() {
        return eventsLiveData;
    }
}