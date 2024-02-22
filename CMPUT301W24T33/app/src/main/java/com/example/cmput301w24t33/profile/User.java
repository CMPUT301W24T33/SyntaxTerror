package com.example.cmput301w24t33.profile;

import com.example.cmput301w24t33.events.Event;

import java.util.ArrayList;

/**
 *
 */
public abstract class User {
    private String firstName;
    private String lastName;
    private String uID;
    private ArrayList<Event> signedUpEvents = new ArrayList<>();

    public User(String firstName, String lastName, String uID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uID = uID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getFullName() {
        return this.firstName + ' ' + this.lastName;
    }
    public void addEvent(Event event) {
        this.signedUpEvents.add(event);
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}