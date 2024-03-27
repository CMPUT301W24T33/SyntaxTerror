package com.example.cmput301w24t33;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.Attendee;
import com.example.cmput301w24t33.activities.Organizer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@LargeTest
// Tests limited as no DB connectivity for RecyclerView population
public class OrganizerUITest {

    @Rule
    public ActivityScenarioRule<Organizer> scenario = new
            ActivityScenarioRule<Organizer>(Organizer.class);

    @Test
    public void testOrganizerCreateEventFragment() {
        // Testing out if cancelling event works
        onView(withId(R.id.button_create_event)).perform(click());
        // scroll to cancel
        onView(withText("Cancel")).perform(ViewActions.scrollTo());
        // click cancel
        onView(withId(R.id.cancel_button)).perform(click());
        // check if on organizer activity
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));

        //Testing out if creating event works
        onView(withId(R.id.button_create_event)).perform(click());
        onView(withId(R.id.event_name_edit_text)).perform(typeText("Uni Days!"), closeSoftKeyboard());
        onView(withId(R.id.event_description_edit_text)).perform(typeText("University of Alberta"), closeSoftKeyboard());
        onView(withId(R.id.max_attendees_edit_text)).perform(replaceText("2"),closeSoftKeyboard());
        onView(withText("Confirm")).perform(ViewActions.scrollTo());
        onView(withId(R.id.confirm_button)).perform(click());

//        onView(withId(R.id.organized_events))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void testOrganizerAttendeeSwitch(){
        // press button to switch to organizer
        onView(withId(R.id.button_user_mode)).perform(click());
        // check if on organizer activity
        onView(withId(R.id.attendee_activity)).check(matches(isDisplayed()));
        // press button to switch back to attendee
        onView(withId(R.id.button_user_mode)).perform(click());
        // check if on attendee view
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
    }

    @Test
    public void testProfileOrganizer(){
        // Click on profile button
        onView(withId(R.id.profile_image)).perform(click());
        // check if id of entire fragment xml exists
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
        // click on back arrow
        onView(withId(R.id.back_arrow_img)).perform(click());
        // check if attendee activity is displayed
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
    }

}
