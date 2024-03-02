package com.example.cmput301w24t33.events;

import com.example.cmput301w24t33.users.User;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class Event {

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE.LLLL.yyyy KK:mm:ss aaa z");
    private String startDateTime;
    private String endDateTime;
    private String name;
    private String organizerId;
    private String eventDescription;
    private String posterQR;
    private String checkInQR;
    private String locationData;    // Not sure if this is the correct date type
    private boolean geoTracking;
    private boolean active;
    private int maxOccupancy;
    private int maxSignup;
    private ArrayList<User> attendees = new ArrayList<>();
    private ArrayList<User> signedUp = new ArrayList<>();


    public Event(String name, String organizerId, String eventDescription) {
        this.name = name;
        this.organizerId = organizerId;
        this.eventDescription = eventDescription;
    }
    public Event() {

    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getPosterQR() {
        return posterQR;
    }

    public void setPosterQR(String posterQR) {
        this.posterQR = posterQR;
    }

    public String getCheckInQR() {
        return checkInQR;
    }

    public void setCheckInQR(String checkInQR) {
        this.checkInQR = checkInQR;
    }

    public String getLocationData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    public boolean isGeoTracking() {
        return geoTracking;
    }

    public void setGeoTracking(boolean geoTracking) {
        this.geoTracking = geoTracking;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public int getMaxSignup() {
        return maxSignup;
    }

    public void setMaxSignup(int maxSignup) {
        this.maxSignup = maxSignup;
    }

    public ArrayList<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<User> attendees) {
        this.attendees = attendees;
    }


    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public ArrayList<User> getSignedUp() {
        return signedUp;
    }

    public void setSignedUp(ArrayList<User> signedUp) {
        this.signedUp = signedUp;
    }
}