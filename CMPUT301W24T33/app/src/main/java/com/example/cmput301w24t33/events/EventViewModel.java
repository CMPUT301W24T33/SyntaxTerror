package com.example.cmput301w24t33.events;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class EventViewModel extends ViewModel {
    private final EventRepository eventRepo;
    private final MutableLiveData<List<Event>> eventsLiveData;

    public EventViewModel() {
        eventRepo = new EventRepository();
        eventsLiveData = new MutableLiveData<>();
        setEventCallback(eventRepo);
    }


    /**Initializes EventCallback function and sets listener on our "events" collection
     *
     */

    private void setEventCallback(EventRepository eventRepo) {
        eventRepo.setEventCallback(new EventRepository.EventCallback() {
            // When events are successfully loaded, set our Live Data list to our query results
            @Override
            public void onEventsLoaded(List<Event> events) {
                eventsLiveData.setValue(events);
            }
            // When Firebase encounters an error, log it
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, String.valueOf(e));
            }

        });

    }



    /**
     * This method loads events from our "events" collection by creating a new EventRepository
     * and setting the list of documents (events) to eventsLiveData.
     * Any errors/exceptions encountered are Logged.
     */
    public void loadEvents() {
        // Defines UserCallback functions
        eventRepo.setEventSnapshotListener(new EventRepository.EventCallback() {
            // When events are successfully loaded, set our Live Data list to our query results
            @Override
            public void onEventsLoaded(List<Event> events) {
                eventsLiveData.setValue(events);
            }
            // When Firebase encounters an error, log it
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, String.valueOf(e));
            }
        });
    }
    /**
     * This method loads events organized by the current user from our "events" collection
     * by creating a new EventRepository and setting the list of documents (events) to eventsLiveData.
     * Any errors/exceptions encountered are Logged.
     */
    public void loadOrganizerEvents(String organizerId) {
        eventRepo.setEventByOrganizerSnapshotListener(organizerId);
    }

    /**
     * Getter method to return events in our events collection
     * @return eventsLiveData as a MutableLiveData List of Events, which contains a live reflection of
     * our events collection in our Firestore Database
     */
    public LiveData<List<Event>> getEventsLiveData() {
        return eventsLiveData;
    }

}