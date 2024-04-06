package com.example.cmput301w24t33;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.AttendeeActivity;
import com.example.cmput301w24t33.activities.OrganizerActivity;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.users.User;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
@LargeTest
// Tests limited as no DB connectivity for RecyclerView population
public class OrganizerActivityUITest {

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), OrganizerActivity.class);
        intent.putExtra("user",new User("1","","","",true,"",""));
        intent.putExtra("eventViewModel", Mockito.mock(EventViewModel.class));}


    @Rule
    public ActivityScenarioRule<AttendeeActivity> scenario = new
            ActivityScenarioRule<AttendeeActivity>(intent);

    @Test
    public void testUIOrganizerDisplay() {
        //Checking if layout is displayed with its components
        onView(withId(R.id.organizer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_organizer)).check(matches(isDisplayed()));
        onView(withId(R.id.organized_events)).check(matches(isDisplayed()));
        onView(withId(R.id.bottom_nav_organizer)).check(matches(isDisplayed()));
        onView(withId(R.id.button_create_event)).check(matches(isDisplayed()));

    }
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
