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

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.AttendeeActivity;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.users.User;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Tests for {@link AttendeeActivity} to verify the functionality and display of the attendee interface.
 * These tests simulate user interactions with the AttendeeActivity UI, verifying the visibility
 * and behavior of UI components. The tests are designed to work in an isolated environment with mock data
 * and without actual database connectivity.
 *
 * <p>Note: The tests assume the absence of real database connectivity and rely on mocked data.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
// Tests limited as no DB connectivity for RecyclerView population
public class AttendeeActivityUITest {

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), AttendeeActivity.class);
        intent.putExtra("user",new User("","","","",true,"",""));
        intent.putExtra("eventViewModel", Mockito.mock(EventViewModel.class));
    }


    @Rule
    public ActivityScenarioRule<AttendeeActivity> scenario = new
            ActivityScenarioRule<AttendeeActivity>(intent);

    /**
     * Verifies that the main layout of the AttendeeActivity and its UI components are properly displayed.
     */
    @Test
    public void testUIAttendeeDisplay() {
        //Checking if layout is displayed with its components
        onView(withId(R.id.attendee_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_top_nav)).check(matches(isDisplayed()));
        onView(withId(R.id.bottom_nav)).check(matches(isDisplayed()));
        onView(withId(R.id.check_in_img)).check(matches(isDisplayed()));
        onView(withId(R.id.find_event_img)).check(matches(isDisplayed()));
        onView(withId(R.id.eventrecyclerview)).check(matches(isDisplayed()));
        onView(withId(R.id.switch_events_button)).check(matches(isDisplayed()));

        //Checks text is displayed correctly
        onView(withText("To Organizer")).check(matches(isDisplayed()));
        onView(withText("Browse All")).check(matches(isDisplayed()));
        onView(withText("Check In")).check(matches(isDisplayed()));
        onView(withText("Find Event")).check(matches(isDisplayed()));

    }

    /**
     * Tests the functionality of the 'Find Event' feature within the AttendeeActivity.
     */
    @Test
    public void testUIAttendeeFindEvent() {
        //Checks if find_event button works
        onView(withId(R.id.attendee_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.bottom_nav)).check(matches(isDisplayed()));
        onView(withId(R.id.find_event_img)).perform(click());

    }

    /**
     * Verifies the functionality of the button used to switch event views within the AttendeeActivity.
     */
    @Test
    public void testUIAttendeeSwitchEventButton() {
        //Checks if switching events view button
        onView(withId(R.id.attendee_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.bottom_nav)).check(matches(isDisplayed()));
        onView(withId(R.id.switch_events_button)).perform(click());
        onView(withId(R.id.eventrecyclerview)).check(matches(isDisplayed()));

    }

    /**
     * Tests the profile view within the AttendeeActivity, including editing and saving profile information.
     */
//    @Test
//    public void testProfileAttendee(){
//        // Click on profile button
//        onView(withId(R.id.profile_image)).perform(click());
//        // check if id of entire fragment xml exists
//        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
//        // Edit profile criteria
//        onView(withId(R.id.first_name_edit_text)).perform(replaceText("John"),closeSoftKeyboard());
//        onView(withId(R.id.last_name_edit_text)).perform(replaceText("Deer"),closeSoftKeyboard());
//        onView(withId(R.id.email_edit_text)).perform(replaceText("Yeehaw@gmail.com"),closeSoftKeyboard());
//
//        //Save the profile // Error here
//        //onView(withId(R.id.profile_save_button)).perform(click());
//
//        //click on back arrow
//        onView(withId(R.id.back_arrow_img)).perform(click());
//        // check if attendee activity is displayed
//        onView(withId(R.id.attendee_activity)).check(matches(isDisplayed()));
//    }

    /**
     * Verifies the functionality of switching between Attendee and Organizer modes within the application.
     */
//    @Test
//    public void testAttendeeOrganizerSwitch(){
//        // press button to switch to organizer
//        onView(withId(R.id.button_user_mode)).perform(click());
//        // check if on organizer activity
//        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
//        // press button to switch back to attendee
//        onView(withId(R.id.button_user_mode)).perform(click());
//        // check if on attendee view
//        onView(withId(R.id.attendee_activity)).check(matches(isDisplayed()));
//    }


}
