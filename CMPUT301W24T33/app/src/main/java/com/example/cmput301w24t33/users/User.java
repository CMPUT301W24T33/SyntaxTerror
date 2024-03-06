package com.example.cmput301w24t33.users;

import com.example.cmput301w24t33.events.Event;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class User implements Serializable {


    private Boolean adminview;
    private String email;

    private String firstName;
    private String lastName;

    private ArrayList<Event> signedUpEvents = new ArrayList<>();

    public User(String firstName, String lastName,String email,Boolean adminview ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.adminview = adminview;
    }
    public User() {

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

    public Boolean getAdminview() {
        return adminview;
    }

    public void setAdminview(Boolean adminview) {
        this.adminview = adminview;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}