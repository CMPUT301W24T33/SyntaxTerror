// Purpose:
// Represents a scheduled event, storing details such as date, time, location, and
// participants, with functionalities for managing event properties and attendee lists.


package com.example.cmput301w24t33.events;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cmput301w24t33.users.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an event, including details such as date, time, location, and participants.
 * This class implements Serializable to allow event objects to be passed between activities or fragments.
 */
public class Event implements Serializable, Parcelable {
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE.LLLL.yyyy KK:mm:ss aaa z");
    private String eventId;
    private Timestamp startDateTime;
    private Timestamp endDateTIme;
    private String name;
    private String organizerId;
    private String eventDescription;
    private String posterQR;
    private String checkInQR;
    private String locationCoord;
    private String locationName;
    private boolean geoTracking;
    private boolean active;
    private int maxOccupancy;
    private int maxSignup;
    private ArrayList<User> attendees = new ArrayList<>();
    private ArrayList<String> checkInLocations = new ArrayList<>();
    private ArrayList<User> signedUp = new ArrayList<>();
    private String imageRef;
    private String imageUrl;
    private Map<String, Boolean> milestones = new HashMap<>();

    public static final Parcelable.Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            Log.d("EventParcel", "");
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    /**
     * Constructor for event parceling
     * @param src parcel source
     */
    protected Event(Parcel src){
        Bundle args = src.readBundle(getClass().getClassLoader());
        Log.d("EventParcel", args.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startDateTime = args.getParcelable("StartTime", Timestamp.class);
            endDateTIme = args.getParcelable("EndTime", Timestamp.class);
            Log.d("Timestamp", "start: " + startDateTime.toString() + " end: " + endDateTIme.toString());
        }
        attendees = (ArrayList<User>) args.getSerializable("Attendees");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkInLocations = args.getSerializable("Locations", (ArrayList.class));
        }

        signedUp = (ArrayList<User>) args.getSerializable("SignedUp");
        imageRef = args.getString("ImageRef");
        imageUrl = args.getString("ImageUrl");
        maxSignup = args.getInt("MaxSignUp");
        maxOccupancy = args.getInt("MaxOccupancy");
        geoTracking = args.getBoolean("GeoTracking");
        active = args.getBoolean("Active");
        locationName = args.getString("LocationName");
        locationCoord = args.getString("LocationCoord");
        checkInQR = args.getString("CheckInQR");
        posterQR = args.getString("PosterQR");
        eventDescription = args.getString("EventDescription");
        organizerId = args.getString("OrganizerId");
        name = args.getString("Name");
        eventId = args.getString("EventId");
        milestones.put("half", args.getBoolean("MilestoneHalf"));
        milestones.put("full", args.getBoolean("MilestoneFull"));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        Bundle args = new Bundle();
        args.putParcelable("StartTime", startDateTime);
        args.putParcelable("EndTime", endDateTIme);
        args.putSerializable("Attendees", attendees);
        args.putSerializable("Locations", checkInLocations);
        args.putSerializable("SignedUp", signedUp);
        args.putString("ImageRef", imageRef);
        args.putString("ImageUrl",imageUrl);
        args.putInt("MaxSignUp", maxSignup);
        args.putInt("MaxOccupancy", maxOccupancy);
        args.putBoolean("GeoTracking", geoTracking);
        args.putBoolean("Active", active);
        args.putString("LocationName", locationName);
        args.putString("LocationCoord", locationCoord);
        args.putString("CheckInQR", checkInQR);
        args.putString("PosterQR", posterQR);
        args.putString("EventDescription", eventDescription);
        args.putString("OrganizerId", organizerId);
        args.putString("Name", name);
        args.putString("EventId", eventId);
        args.putBoolean("MilestoneHalf", Boolean.TRUE.equals(milestones.get("half")));
        args.putBoolean("MilestoneFull", Boolean.TRUE.equals(milestones.get("full")));
        dest.writeBundle(args);
    }



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
        this.imageRef = "";
        this.imageUrl = "";
        this.milestones.put("half", false);
        this.milestones.put("full", false);
    }

    /**
     * Empty Constructor
     */
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
    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the start date of the event.
     * @param startDateTime A string representing the start date.
     */
    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the end date of the event.
     * @return A string representing the end date.
     */
    public Timestamp getEndDateTIme() {
        return endDateTIme;
    }

    /**
     * Sets the end date of the event.
     * @param endDateTIme A string representing the end date.
     */
    public void setEndDateTIme(Timestamp endDateTIme) {
        this.endDateTIme = endDateTIme;
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
    public String getLocationName() {
        return locationName;
    }

    /**
     * Sets the address of the event location.
     * @param locationName A string representing the event location address.
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * Gets additional location data for the event.
     * @return A string containing additional location data.
     */
    public String getLocationCoord() {
        return locationCoord;
    }

    /**
     * Sets additional location data for the event.
     * @param locationCoord A string containing additional location data.
     */
    public void setLocationCoord(String locationCoord) {
        this.locationCoord = locationCoord;
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

    /**
     * Gets the list of attendees currently signed up for the event.
     * @return An ArrayList of User objects representing the attendees.
     */
    public ArrayList<User> getAttendees() {
        if (attendees.isEmpty()) {
            return attendees;
        }
        return attendees;
    }

    /**
     * Sets the list of attendees for the event.
     * @param attendees An ArrayList of User objects representing the attendees.
     */
    public void setAttendees(ArrayList<User> attendees) {
        this.attendees = attendees;
    }

    /**
     * Gets the list of users who have signed up for the event but are not yet attendees.
     * @return An ArrayList of User objects representing users who have signed up.
     */
    public ArrayList<User> getSignedUp() {
        return signedUp;
    }

    public void addAttendee(User attendee, Location location) {
        attendees.add(attendee);
        if (location != null) {
            checkInLocations.add(location.getLatitude() + "," +location.getLongitude());
        }
    }

    /**
     * Sets the list of users who have signed up for the event.
     * @param signedUp An ArrayList of User objects representing users who have signed up.
     */
    public void setSignedUp(ArrayList<User> signedUp) {
        this.signedUp = signedUp;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getCheckInLocations() {
        ArrayList<GeoPoint> geoPointLocations = new ArrayList<>();
        for (String location: checkInLocations){
            double lat = Double.parseDouble(location.split(",")[0]);
            double lon = Double.parseDouble(location.split(",")[1]);
            geoPointLocations.add(new GeoPoint(lat,lon));
        }
        return checkInLocations;
    }

    public void setCheckInLocations(ArrayList<String> checkInLocations) {
        this.checkInLocations = checkInLocations;
    }

    public ArrayList<GeoPoint> checkInLocationsGeoPoint(){
        ArrayList<GeoPoint> geoPointLocations = new ArrayList<>();
        for (String location: checkInLocations){
            double lat = Double.parseDouble(location.split(",")[0]);
            double lon = Double.parseDouble(location.split(",")[1]);
            geoPointLocations.add(new GeoPoint(lat,lon));
        }
        return geoPointLocations;
    }

    public Map<String, Boolean> getMilestones() {
        return milestones;
    }

    public void setMilestones(String milestoneName, Boolean value) {
        this.milestones.put(milestoneName, value);
    }
}
