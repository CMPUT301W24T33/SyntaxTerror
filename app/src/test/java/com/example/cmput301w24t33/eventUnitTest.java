package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.users.User;

import org.junit.Before;
import org.junit.Test;

public class eventUnitTest {
    private Event event;
    private final String initialName = "Test Event";
    private final String initialOrganizerId = "Organizer123";
    private final String initialDescription = "This is a test event";

    @Before
    public void setUp() {
        event = new Event(initialName, initialOrganizerId, initialDescription);
    }

    @Test
    public void testEventInstantiation() {
        assertNotNull("Event object should not be null", event);
        assertEquals("Name should match initial value", initialName, event.getName());
        assertEquals("OrganizerActivity ID should match initial value", initialOrganizerId, event.getOrganizerId());
        assertEquals("Description should match initial value", initialDescription, event.getEventDescription());
    }

    @Test
    public void testSettingAndGettingEventId() {
        String eventId = "Event001";
        event.setEventId(eventId);
        assertEquals("Event ID should match set value", eventId, event.getEventId());
    }

    @Test
    public void testAttendeeManagement() {
        User user1 = new User();
        User user2 = new User();

        event.getAttendees().add(user1);
        event.getSignedUp().add(user2);

        assertTrue("User1 should be in attendees list", event.getAttendees().contains(user1));
        assertTrue("User2 should be in signedUp list", event.getSignedUp().contains(user2));
    }



}
