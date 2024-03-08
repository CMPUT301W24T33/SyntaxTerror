package com.example.cmput301w24t33;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.Admin;
import com.example.cmput301w24t33.activities.Attendee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
// Tests limited as no DB connectivity for RecyclerView population
public class AdminUITest {
    @Rule
    public ActivityScenarioRule<Admin> scenario = new
            ActivityScenarioRule<Admin>(Admin.class);

    @Test
    public void testAdminViewEvent(){
        // press button go to admin events
        onView(withId(R.id.event_arrow_button)).perform(click());
        // check if on organizer activity
        onView(withId(R.id.admin_events_layout)).check(matches(isDisplayed()));
        // press button to switch back to attendee
        onView(withId(R.id.view_events_back_button)).perform(click());
        // check if on attendee view
        onView(withId(R.id.admin_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testAdminViewProfiles(){
        // press button go to admin profiles
        onView(withId(R.id.profile_arrow_button)).perform(click());
        // check if on organizer activity
        onView(withId(R.id.admin_view_profiles_fragment)).check(matches(isDisplayed()));
        // press button to switch back to attendee
        onView(withId(R.id.view_profiles_back_button)).perform(click());
        // check if on attendee view
        onView(withId(R.id.admin_activity)).check(matches(isDisplayed()));
    }
}
