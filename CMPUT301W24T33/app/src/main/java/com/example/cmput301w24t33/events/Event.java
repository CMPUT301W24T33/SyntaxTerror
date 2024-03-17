package com.example.cmput301w24t33.events;

import com.example.cmput301w24t33.users.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents an event, including details such as date, time, location, and participants.
 * This class implements Serializable to allow event objects to be passed between activities or fragments.
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
    private String locationData;
    private boolean geoTracking;
    private boolean active;
    private int maxOccupancy;
    private int maxSignup;
    //private ArrayList<String> attendees = new ArrayList<>();
    //private ArrayList<String> signedUp = new ArrayList<>();

    /**
     * Constructs a new Event with specified name, organizerId, and eventDescription.
     *
     * @param name The name of the event.
     * @param organizerId The identifier of the event organizer.
     * @param eventDescription A description of the event.
     */
    public Event(String name, String organizerId, String eventDescription) {
        this.name = name;
        this.organizerId = organizerId;
        this.eventDescription = eventDescription;
        this.geoTracking = false;
    }

    public Event() {
    }

    /**
     * Gets the unique identifier of the event.
     * @return A string representing the event's unique identifier.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the unique identifier of the event.
     * @param eventId A string representing the event's unique identifier.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the start date of the event.
     * @return A string representing the start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the event.
     * @param startDate A string representing the start date.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the event.
     * @return A string representing the end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the event.
     * @param endDate A string representing the end date.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the start time of the event.
     * @return A string representing the start time.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event.
     * @param startTime A string representing the start time.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the event.
     * @return A string representing the end time.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event.
     * @param endTime A string representing the end time.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the name of the event.
     * @return The event name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     * @param name The event name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organizer's identifier.
     * @return A string representing the organizer's identifier.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the organizer's identifier.
     * @param organizerId A string representing the organizer's identifier.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Gets the event description.
     * @return A string containing the event description.
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the event description.
     * @param eventDescription A string containing the event description.
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the QR code for the event poster.
     * @return A string representing the QR code for the event poster.
     */
    public String getPosterQR() {
        return posterQR;
    }

    /**
     * Sets the QR code for the event poster.
     * @param posterQR A string representing the QR code for the event poster.
     */
    public void setPosterQR(String posterQR) {
        this.posterQR = posterQR;
    }

    /**
     * Gets the QR code used for checking in to the event.
     * @return A string representing the check-in QR code.
     */
    public String getCheckInQR() {
        return checkInQR;
    }

    /**
     * Sets the QR code used for checking in to the event.
     * @param checkInQR A string representing the check-in QR code.
     */
    public void setCheckInQR(String checkInQR) {
        this.checkInQR = checkInQR;
    }

    /**
     * Gets the address of the event location.
     * @return A string representing the event location address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the event location.
     * @param address A string representing the event location address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets additional location data for the event.
     * @return A string containing additional location data.
     */
    public String getLocationData() {
        return locationData;
    }

    /**
     * Sets additional location data for the event.
     * @param locationData A string containing additional location data.
     */
    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    /**
     * Checks if geo-tracking is enabled for the event.
     * @return True if geo-tracking is enabled, otherwise false.
     */
    public boolean getGeoTracking() {
        return geoTracking;
    }

    /**
     * Enables or disables geo-tracking for the event.
     * @param geoTracking A boolean value indicating whether geo-tracking should be enabled.
     */
    public void setGeoTracking(boolean geoTracking) {
        this.geoTracking = geoTracking;
    }

    /**
     * Checks if the event is currently active.
     * @return True if the event is active, otherwise false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the event's active status.
     * @param active A boolean value indicating whether the event is active.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the maximum occupancy for the event.
     * @return An integer representing the maximum number of attendees.
     */
    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    /**
     * Sets the maximum occupancy for the event.
     * @param maxOccupancy An integer representing the maximum number of attendees.
     */
    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    /**
     * Gets the maximum number of signups allowed for the event.
     * @return An integer representing the maximum number of signups.
     */
    public int getMaxSignup() {
        return maxSignup;
    }

    /**
     * Sets the maximum number of signups allowed for the event.
     * @param maxSignup An integer representing the maximum number of signups.
     */
    public void setMaxSignup(int maxSignup) {
        this.maxSignup = maxSignup;
    }

}

