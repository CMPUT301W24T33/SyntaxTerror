package com.example.cmput301w24t33.events;

import com.example.cmput301w24t33.users.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Event} class.
 * This class provides unit tests to verify the correct behavior of
 * the event's properties and their management methods, ensuring that
 * event data is handled correctly throughout its lifecycle.
 */
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

    /**
     * Sets up the test environment for each test method.
     * Initializes a new {@link Event} instance and sets its properties
     * to predefined values.
     */
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

    /**
     * Tests the retrieval of the event's ID.
     */
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

    /**
     * Tests the management and retrieval of event attendees.
     * Verifies that attendees added to the event are correctly
     * returned by the getter method.
     */
    @Test
    void testAttendeesManagement() {
        User user = new User();
        ArrayList<User> attendees = new ArrayList<>();
        attendees.add(user);
        event.setAttendees(attendees);
        assertTrue(event.getAttendees().contains(user));
    }

    /**
     * Tests the management and retrieval of users signed up for the event.
     * Verifies that users added to the event's signed-up list are correctly
     * returned by the getter method.
     */
    @Test
    void testSignedUpManagement() {
        User user = new User();
        ArrayList<User> signedUp = new ArrayList<>();
        signedUp.add(user);
        event.setSignedUp(signedUp);
        assertTrue(event.getSignedUp().contains(user));
    }


    /**
     * Tests the management and retrieval of event milestones.
     * Verifies that milestones added to the event are correctly
     * managed and queried.
     */
    @Test
    void testMilestonesManagement() {
        event.setMilestones("half", true);
        Map<String, Boolean> milestones = event.getMilestones();
        assertTrue(milestones.containsKey("half") && milestones.get("half"));
    }


}