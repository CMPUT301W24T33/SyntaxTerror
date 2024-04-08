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

import android.app.Application;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.AttendeeActivity;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
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

    @Before
    public void setUp() {
        FirebaseFirestore mockDb = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollection = Mockito.mock(CollectionReference.class);
        DocumentReference mockDocumentRef = Mockito.mock(DocumentReference.class);
        Task<Void> mockTask = Mockito.mock(Task.class);

        // Mock the document() call to return a mock DocumentReference
        Mockito.when(mockDb.collection("users")).thenReturn(mockCollection);
        Mockito.when(mockCollection.document(Mockito.anyString())).thenReturn(mockDocumentRef);

        // Mock the set() call to return a mock Task
        Mockito.when(mockDocumentRef.set(Mockito.any())).thenReturn(mockTask);

        //Mock the Task to return itself on addOnSuccessListener and addOnFailureListener
        Mockito.when(mockTask.addOnSuccessListener(Mockito.any())).thenReturn(mockTask);
        Mockito.when(mockTask.addOnFailureListener(Mockito.any())).thenReturn(mockTask);

        Application mockApplication = Mockito.mock(Application.class);
        UserRepository.initialize(mockApplication, mockDb);
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
    @Test
    public void testProfileAttendee(){
        // Click on profile button
        onView(withId(R.id.profile_image)).perform(click());

        // check if id of entire fragment xml exists
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));

        // Edit profile criteria
        onView(withId(R.id.first_name_edit_text)).perform(replaceText("John"),closeSoftKeyboard());
        onView(withId(R.id.last_name_edit_text)).perform(replaceText("Deer"),closeSoftKeyboard());
        onView(withId(R.id.email_edit_text)).perform(replaceText("Yeehaw@gmail.com"),closeSoftKeyboard());

        //Saves profile
        onView(withId(R.id.profile_save_button)).perform(click());

    }

    /**
     * Tests the profile view within the AttendeeActivity, to see if layout of profile is displayed correctly
     */
    @Test
    public void testProfileAttendeeLayoutDisplay(){
        // Click on profile button
        onView(withId(R.id.profile_image)).perform(click());

        //Checks if profile fragment is displayed
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));

        //Checks email layout
        onView(withId(R.id.email_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.email_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.email_edit_text)).check(matches(isDisplayed()));

        //Checks first name layout
        onView(withId(R.id.first_name_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.first_name_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.first_name_edit_text)).check(matches(isDisplayed()));

        //Checks last name layout
        onView(withId(R.id.last_name_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.last_name_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.last_name_edit_text)).check(matches(isDisplayed()));

        //Checks button layout
        onView(withId(R.id.cancel_save_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.remove_profile_img_button)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_save_button)).check(matches(isDisplayed()));

        //Checks Texts are displayed correctly
        onView(withText("First Name:")).check(matches(isDisplayed()));
        onView(withText("Last Name:")).check(matches(isDisplayed()));
        onView(withText("E-Mail:")).check(matches(isDisplayed()));

        onView(withText("CANCEL")).check(matches(isDisplayed()));
        onView(withText("REMOVE IMAGE")).check(matches(isDisplayed()));
        onView(withText("SAVE")).check(matches(isDisplayed()));

    }

    /**
     *  Tests the profile view within the AttendeeActivity, to see if cancel button works or not
     */
    @Test
    public void testProfileCancelButton(){
        // Click on profile button
        onView(withId(R.id.profile_image)).perform(click());

        //Checks if profile fragment is displayed
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));

        //Cancel Button pressed
        onView(withId(R.id.profile_cancel_button)).perform(click());

        //Checks if on attendee layout
        onView(withId(R.id.attendee_layout)).check(matches(isDisplayed()));
    }

    /**
     *  Tests the profile view within the AttendeeActivity, to see input validation
     */
    @Test
    public void testProfileTextInput(){
        // Click on profile button
        onView(withId(R.id.profile_image)).perform(click());

        //Checks if profile fragment is displayed
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));

        //Checks first name input text
        onView(withId(R.id.first_name_edit_text)).perform(replaceText("John"),closeSoftKeyboard());
        onView(withId(R.id.profile_save_button)).perform(click());

        //Checks last name input text
        onView(withId(R.id.last_name_edit_text)).perform(replaceText("Deer"),closeSoftKeyboard());
        onView(withId(R.id.profile_save_button)).perform(click());

        //Checks email input text
        onView(withId(R.id.email_edit_text)).perform(replaceText("JD@Gmail.com"),closeSoftKeyboard());
        onView(withId(R.id.profile_save_button)).perform(click());

        //Checks if on attendee layout
        onView(withId(R.id.attendee_layout)).check(matches(isDisplayed()));
    }

    /**
     *  Tests the profile view within the AttendeeActivity, to see if save button works or not
     */
    @Test
    public void testProfileSaveButton(){
        // Click on profile button
        onView(withId(R.id.profile_image)).perform(click());

        //Checks if profile fragment is displayed
        onView(withId(R.id.profile_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_view)).check(matches(isDisplayed()));

        //Save Button pressed
        onView(withId(R.id.profile_save_button)).perform(click());

        //Checks if on attendee layout
        onView(withId(R.id.attendee_layout)).check(matches(isDisplayed()));
    }


}
