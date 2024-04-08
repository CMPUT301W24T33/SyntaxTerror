package com.example.cmput301w24t33;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;

import android.app.Application;
import android.content.Intent;
import android.widget.DatePicker;

import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.AttendeeActivity;
import com.example.cmput301w24t33.activities.OrganizerActivity;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.example.cmput301w24t33.users.UserViewModel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
// Tests limited as no DB connectivity for RecyclerView population
public class OrganizerActivityUITest {

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), OrganizerActivity.class);
        intent.putExtra("user",new User("1","","","",true,"",""));
        intent.putExtra("eventViewModel", Mockito.mock(EventViewModel.class));}

    @Before
    public void setUp() {
        // Get the application context
        Application application = ApplicationProvider.getApplicationContext();
        EventRepository mockEventRepo = Mockito.mock(EventRepository.class);

        // Creating a MutableLiveData instance for testing
        MutableLiveData<List<Event>> testEvent = new MutableLiveData<>();

        // Initialize the EventViewModel with the mock components
        EventViewModel.initialize(application, mockEventRepo, testEvent);

        Event mockEvent = new Event("Current Event","0000", "university days");

    }

    @Rule
    public ActivityScenarioRule<AttendeeActivity> scenario = new
            ActivityScenarioRule<AttendeeActivity>(intent);

    @Test
    public void testUIOrganizerLayoutDisplay() {
        //Checking if layout is displayed with its components
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
        onView(withId(R.id.organizer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_organizer)).check(matches(isDisplayed()));
        onView(withId(R.id.organized_events)).check(matches(isDisplayed()));
        onView(withId(R.id.bottom_nav_organizer)).check(matches(isDisplayed()));
        onView(withId(R.id.button_create_event)).check(matches(isDisplayed()));
        //Checks text layout
        onView(withText("To Attendee")).check(matches(isDisplayed()));
        onView(withText("New Event")).check(matches(isDisplayed()));

    }

    @Test
    public void testUIOrganizerCreateEventLayoutDisplay() {
        //Checks if layout is organizer activity
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));

        //Transition to Organizer Create Event
        onView(withId(R.id.button_create_event)).perform(click());

        //Checks layout is displayed with its components
        onView(withText("Create/Edit Event")).check(matches(isDisplayed()));
        onView(withId(R.id.organizer_create_event_activity)).check(matches(isDisplayed()));

        //Checks Event Name Layout
        onView(withText("Event Name")).check(matches(isDisplayed()));
        onView(withId(R.id.event_name_edit_text)).check(matches(isDisplayed()));

        //Checks Event Location Layout
        onView(withText("Event Location")).check(matches(isDisplayed()));
        onView(withId(R.id.autocomplete_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.event_location_cords_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.event_location_cords_text)).check(matches(isDisplayed()));

        //Checks Event Event Description Layout
        onView(withText("Event Description")).check(matches(isDisplayed()));
        onView(withId(R.id.event_description_input_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.event_description_edit_text)).check(matches(isDisplayed()));

        //Checks Event Date and Time Layout
        onView(withText("Date and Time")).check(matches(isDisplayed()));
        onView(withId(R.id.date_time_container)).check(matches(isDisplayed()));
        onView(withText("Start:")).check(matches(isDisplayed()));
        onView(withId(R.id.start_time_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.start_time_text)).check(matches(isDisplayed()));

        onView(withText("End:")).check(matches(isDisplayed()));
        onView(withId(R.id.end_time_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.end_time_text)).check(matches(isDisplayed()));

        //Checks Event Options Layout
        onView(withText("Options")).check(matches(isDisplayed()));
        onView(withText("Max Attendees:")).check(matches(isDisplayed()));
        onView(withId(R.id.max_attendees_container)).check(matches(isDisplayed()));
        onView(withId(R.id.max_attendees_input_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.max_attendees_edit_text)).check(matches(isDisplayed()));

        //Checks Geo Tracking Layout
        onView(withText("Enable Geo Tracking:")).check(matches(isDisplayed()));
        onView(withId(R.id.geo_tracking_container)).check(matches(isDisplayed()));
        onView(withId(R.id.geo_tracking_switch)).check(matches(isDisplayed()));

        //Checks Upload Poster Button
        onView(withText("Confirm")).perform(ViewActions.scrollTo());
        onView(withText("UPLOAD POSTER")).check(matches(isDisplayed()));
        onView(withId(R.id.upload_poster_button)).check(matches(isDisplayed()));

        ////Checks Upload QR Code Button
        onView(withText("SELECT QR CODE")).check(matches(isDisplayed()));
        onView(withId(R.id.generate_qr_code_button)).check(matches(isDisplayed()));

        //Checks cancel and confirm buttons
        onView(withText("CANCEL")).check(matches(isDisplayed()));
        onView(withText("CONFIRM")).check(matches(isDisplayed()));


    }

    // Firestore error after config implementation "androidx.test.espresso:espresso-contrib:3.5.1"

//    @Test
//    public void testOrganizerCreateEventDateAndTime(){
//        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
//        onView(withId(R.id.button_create_event)).perform(click());
//
//        onView(withId(R.id.start_time_text)).perform(click());
//
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
//                .inRoot(isDialog()) // <— Makes sure this is in the dialog
//                .perform(PickerActions.setDate(1999, 2010, 7));
//        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
//    }

    @Test
    public void testOrganizerCreateEventFragment() {
        // check if on organizer activity
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));

        //Tests Event Creation
        onView(withId(R.id.button_create_event)).perform(click());
        onView(withId(R.id.event_name_edit_text)).perform(typeText("Uni Days!"), closeSoftKeyboard());
        onView(withId(R.id.event_location_cords_text)).perform(typeText("53.5232, 113.5263"), closeSoftKeyboard());
        onView(withId(R.id.event_description_edit_text)).perform(typeText("University of Alberta Exam Days"), closeSoftKeyboard());
        onView(withId(R.id.max_attendees_edit_text)).perform(replaceText("2"),closeSoftKeyboard());
        onView(withText("Confirm")).perform(ViewActions.scrollTo());
        onView(withId(R.id.confirm_button)).perform(click());

    }

    @Test
    public void testOrganizerEventCancelButton(){
        // Checks if on organizer activity
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
        // Press create event
        onView(withId(R.id.button_create_event)).perform(click());
        // Scrolls to cancel
        onView(withText("Cancel")).perform(ViewActions.scrollTo());
        // Click cancel
        onView(withId(R.id.cancel_button)).perform(click());

    }

    @Test
    public void testOrganizerEventGeoLocationSwitch(){
        // Checks if on organizer activity
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
        // Press create event
        onView(withId(R.id.button_create_event)).perform(click());
        //Scrolls Down to GeoTracking Button
        onView(withText("Cancel")).perform(ViewActions.scrollTo());
        onView(withId(R.id.geo_tracking_switch)).perform(click());
        // Click cancel
        onView(withId(R.id.confirm_button)).perform(click());

    }

    @Test
    public void testOrganizerEventMaxAttendees(){
        // Checks if on organizer activity
        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
        // Press create event
        onView(withId(R.id.button_create_event)).perform(click());
        //Scrolls Down to end of screen
        onView(withText("Cancel")).perform(ViewActions.scrollTo());
        //Sets Max Attendees
        onView(withId(R.id.max_attendees_edit_text)).perform(replaceText("2"),closeSoftKeyboard());
        // Click cancel
        onView(withId(R.id.confirm_button)).perform(click());

    }


//    @Test
//    public void testOrganizerAttendeeSwitch(){
//        // press button to switch to organizer
//        onView(withId(R.id.button_user_mode)).perform(click());
//        // check if on organizer activity
//        onView(withId(R.id.attendee_activity)).check(matches(isDisplayed()));
//        // press button to switch back to attendee
//        onView(withId(R.id.button_user_mode)).perform(click());
//        // check if on attendee view
//        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testProfileOrganizer(){
//        // Click on profile button
//        onView(withId(R.id.profile_image)).perform(click());
//        // check if id of entire fragment xml exists
//        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
//        // click on back arrow
//        onView(withId(R.id.back_arrow_img)).perform(click());
//        // check if attendee activity is displayed
//        onView(withId(R.id.organizer_activity)).check(matches(isDisplayed()));
//    }

}
