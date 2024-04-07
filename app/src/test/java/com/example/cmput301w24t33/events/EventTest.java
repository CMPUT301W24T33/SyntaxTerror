package com.example.cmput301w24t33.events;

import com.example.cmput301w24t33.users.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;
    private final String eventId = "event123";
    private final String name = "Sample Event";
    private final String organizerId = "org123";
    private final String eventDescription = "This is a sample event description.";
    private final boolean geoTracking = true;
    private final boolean active = true;
    private final int maxOccupancy = 100;
    private final int maxSignup = 150;
    private final String locationName = "Sample Location";
    private final String locationCoord = "123.456, 789.012";
    private final Timestamp startDateTime = new Timestamp(1, 4);
    private final Timestamp endDateTIme = new Timestamp(21, 4);

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setEventId(eventId);
        event.setName(name);
        event.setOrganizerId(organizerId);
        event.setEventDescription(eventDescription);
        event.setGeoTracking(geoTracking);
        event.setActive(active);
        event.setMaxOccupancy(maxOccupancy);
        event.setMaxSignup(maxSignup);
        event.setLocationName(locationName);
        event.setLocationCoord(locationCoord);
        event.setStartDateTime(startDateTime);
        event.setEndDateTIme(endDateTIme);
    }

    @Test
    void testEventId() {
        assertEquals(eventId, event.getEventId());
    }

    @Test
    void testName() {
        assertEquals(name, event.getName());
    }

    @Test
    void testOrganizerId() {
        assertEquals(organizerId, event.getOrganizerId());
    }

    @Test
    void testEventDescription() {
        assertEquals(eventDescription, event.getEventDescription());
    }

    @Test
    void testGeoTracking() {
        assertEquals(geoTracking, event.getGeoTracking());
    }

    @Test
    void testActive() {
        assertEquals(active, event.isActive());
    }

    @Test
    void testMaxOccupancy() {
        assertEquals(maxOccupancy, event.getMaxOccupancy());
    }

    @Test
    void testMaxSignup() {
        assertEquals(maxSignup, event.getMaxSignup());
    }

    @Test
    void testLocationName() {
        assertEquals(locationName, event.getLocationName());
    }

    @Test
    void testLocationCoord() {
        assertEquals(locationCoord, event.getLocationCoord());
    }

    @Test
    void testStartDateTime() {
        assertEquals(startDateTime, event.getStartDateTime());
    }

    @Test
    void testEndDateTIme() {
        assertEquals(endDateTIme, event.getEndDateTIme());
    }

    @Test
    void testAttendeesManagement() {
        User user = new User();
        ArrayList<User> attendees = new ArrayList<>();
        attendees.add(user);
        event.setAttendees(attendees);
        assertTrue(event.getAttendees().contains(user));
    }

    @Test
    void testSignedUpManagement() {
        User user = new User();
        ArrayList<User> signedUp = new ArrayList<>();
        signedUp.add(user);
        event.setSignedUp(signedUp);
        assertTrue(event.getSignedUp().contains(user));
    }

    @Test
    void testCheckInLocationsManagement() {
        String location = "123.456,789.012";
        ArrayList<String> checkInLocations = new ArrayList<>();
        checkInLocations.add(location);
        event.setCheckInLocations(checkInLocations);
        assertTrue(event.getCheckInLocations().contains(location));
    }

    @Test
    void testMilestonesManagement() {
        event.setMilestones("half", true);
        Map<String, Boolean> milestones = event.getMilestones();
        assertTrue(milestones.containsKey("half") && milestones.get("half"));
    }

    @Test
    void testCheckInLocationsGeoPointConversion() {
        String location = "123.456,789.012";
        ArrayList<String> checkInLocations = new ArrayList<>();
        checkInLocations.add(location);
        event.setCheckInLocations(checkInLocations);
        ArrayList<GeoPoint> geoPoints = event.checkInLocationsGeoPoint();
        GeoPoint expectedGeoPoint = new GeoPoint(123.456, 789.012);
        assertTrue(geoPoints.contains(expectedGeoPoint));
    }
}