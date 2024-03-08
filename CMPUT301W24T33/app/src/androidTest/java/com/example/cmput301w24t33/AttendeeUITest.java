package com.example.cmput301w24t33;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.Attendee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
// Tests limited as no DB connectivity for RecyclerView population
public class AttendeeUITest {
    @Rule
    public ActivityScenarioRule<Attendee> scenario = new
            ActivityScenarioRule<Attendee>(Attendee.class);
    @Test
    public void testProfileAttendee(){
        // Click on profile button
        onView(withId(R.id.profile_image)).perform(click());
        // check if id of entire fragment xml exists
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
        // click on back arrow
        onView(withId(R.id.back_arrow_img)).perform(click());
        // check if attendee activity is displayed
        onView(withId(R.id.attendee_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testAttendeeOrganizerSwitch(){
        // press button to switch to organizer
        onView(withId(R.id.button_user_mode)).perform(click());
        // check if on organizer activity
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
        // press button to switch back to attendee
        onView(withId(R.id.button_user_mode)).perform(click());
        // check if on attendee view
        onView(withId(R.id.attendee_activity)).check(matches(isDisplayed()));
    }
}
