package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.users.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link Event} class.
 * <p>
 * This test suite aims to verify the basic functionality of event creation and
 * manipulation, ensuring that event properties are correctly initialized, set,
 * and retrieved.
 */
public class EventUnitTest {
    private Event event;
    private final String initialName = "Test Event";
    private final String initialOrganizerId = "Organizer123";
    private final String initialDescription = "This is a test event";

    /**
     * Sets up the test environment before each test.
     * <p>
     * Initializes an {@link Event} object with predefined name, organizer ID,
     * and description for testing.
     */
    @Before
    public void setUp() {
        event = new Event(initialName, initialOrganizerId, initialDescription);
    }

    /**
     * Tests the instantiation of an {@link Event} object.
     * <p>
     * Verifies that the event object is not null and its properties match the
     * initial values passed to the constructor.
     */
    @Test
    public void testEventInstantiation() {
        assertNotNull("Event object should not be null", event);
        assertEquals("Name should match initial value", initialName, event.getName());
        assertEquals("OrganizerActivity ID should match initial value", initialOrganizerId, event.getOrganizerId());
        assertEquals("Description should match initial value", initialDescription, event.getEventDescription());
    }

    /**
     * Tests setting and getting the event ID.
     * <p>
     * Verifies that the event ID can be set and retrieved correctly.
     */
    @Test
    public void testSettingAndGettingEventId() {
        String eventId = "Event001";
        event.setEventId(eventId);
        assertEquals("Event ID should match set value", eventId, event.getEventId());
    }

    /**
     * Tests the management of attendees and users signed up for the event.
     * <p>
     * Verifies that users can be added to the attendees and signed-up lists,
     * and that these lists correctly contain the added users.
     */
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
