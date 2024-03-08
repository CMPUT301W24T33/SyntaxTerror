package com.example.cmput301w24t33;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;

import com.example.cmput301w24t33.adminFragments.DeleteEventAdmin;
import com.example.cmput301w24t33.events.Event;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeleteEventAdminTest {

    private Event mockEvent;

    @Before
    public void setUp() {
        // Create a mock Event to use for testing
        mockEvent = Mockito.mock(Event.class);
        Mockito.when(mockEvent.getName()).thenReturn("Test Event");
        Mockito.when(mockEvent.getAddress()).thenReturn("123 Test St.");
        Mockito.when(mockEvent.getEventDescription()).thenReturn("This is a test event.");
        Mockito.when(mockEvent.getStartDate()).thenReturn("2024-03-07");
        Mockito.when(mockEvent.getEndDate()).thenReturn("2024-03-08");
        Mockito.when(mockEvent.getStartTime()).thenReturn("10:00");
        Mockito.when(mockEvent.getEndTime()).thenReturn("18:00");
        Mockito.when(mockEvent.getMaxOccupancy()).thenReturn(50);
    }

    @Test
    public void testFragmentWithData() {
        // Launch the fragment with the mock event
        FragmentScenario<DeleteEventAdmin> scenario = FragmentScenario.launchInContainer(
                DeleteEventAdmin.class,
                DeleteEventAdmin.newInstance(mockEvent).getArguments()
        );

        // Verify that the event data is displayed correctly
        Espresso.onView(withId(R.id.event_name_edit_text)).check(matches(withText("Test Event")));
        Espresso.onView(withId(R.id.event_location_edit_text)).check(matches(withText("123 Test St.")));
        // Add more checks as necessary for each UI element
    }

    @Test
    public void testCancelButton() {
        // Launch the fragment
        FragmentScenario<DeleteEventAdmin> scenario = FragmentScenario.launchInContainer(
                DeleteEventAdmin.class,
                DeleteEventAdmin.newInstance(mockEvent).getArguments()
        );

        // Perform a click on the cancel button
        Espresso.onView(withId(R.id.cancel_button)).perform(click());

        // You would need a way to verify that the fragment has been popped from the stack.
        // This might involve checking the current fragment displayed or using a mocked FragmentManager.
    }

    // More tests can be added for the delete functionality, especially once the deletion logic is implemented.
}
