package com.example.eventalate;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 */
public class Event implements Comparable {

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE.LLLL.yyyy KK:mm:ss aaa z");
    private String startDateTime;
    private String endDateTime;
    private String name;
    private String eventDescription;
    private final Organizer creator;
    private String posterQR;
    private String checkInQR;
    private String locationData;    // Not sure if this is the correct date type
    private boolean geoTracking;
    private boolean active;
    private int maxOccupancy;
    private int maxSignup;
    private ArrayList<String> attendees = new ArrayList<>();

    public Event(String name, String eventDescription, Organizer creator) {
        this.name = name;
        this.eventDescription = eventDescription;
        this.creator = creator;
    }

    /**
     * Gets start date and time
     * @return
     *      Returns start date and time as a String
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets start date and time for event
     * @param startDateTime
     *      The start date and time for event
     */
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets end date and time
     * @return
     *      Returns end date and time as a String
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets end date and time for event
     * @param endDateTime
     *      The end date and time for event
     */
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the name of the event
     * @return
     *      Returns the name of the event as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the event
     * @param name
     *      The new name for the event
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the Event Description
     * @return
     *      Returns the Event Description as a String
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the Event Description for the Event
     * @param eventDescription
     *      The Event Description to be set for the Event
     */
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

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }


    @Override
    public int compareTo(Object o) {
        Event event = (Event) o;

        return 0;
    }

    public Organizer getCreator() {
        return creator;
    }
}
