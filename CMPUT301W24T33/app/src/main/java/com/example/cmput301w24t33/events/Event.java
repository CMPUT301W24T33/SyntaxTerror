package com.example.cmput301w24t33.events;

import com.example.cmput301w24t33.users.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class Event implements Serializable {
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE.LLLL.yyyy KK:mm:ss aaa z");
    private String eventId;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String name;
    private String organizerId;
    private String eventDescription;
    private String posterQR;
    private String checkInQR;
    private String address;
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
        this.geoTracking = false;
    }
    public Event() {

    }
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    public boolean getGeoTracking() {
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