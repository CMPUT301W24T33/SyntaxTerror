package com.example.cmput301w24t33;

import java.util.ArrayList;
import com.google.firebase.auth.FirebaseUser;

/**
 *
 */
public class Organizer extends User{
    private boolean organizer;
    private ArrayList<Event> myEvents = new ArrayList<>();
    public Organizer(String firstName, String lastName, String uID) {
        super(firstName, lastName, uID);
        //this.organizer = true;
    }
    public void addEvent(Event event) {
        this.myEvents.add(event);
    }

    public void deleteEvent(Event event) {

    }
}
