package com.example.cmput301w24t33;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.cmput301w24t33.activities.AdminActivity;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.example.cmput301w24t33.users.UserViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;
/**
 * Tests for {@link AdminActivity} to verify the display and functionality of the admin interface.
 * These tests simulate user interactions with the AdminActivity UI and verify the visibility
 * and behavior of UI components. The tests operate within an isolated environment with mock data
 * for {@link EventRepository} and {@link UserRepository} to ensure reliability and speed.
 * <p>
 * Note: Tests are conducted without actual database connectivity, focusing on UI and ViewModel
 * interaction.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
// Tests limited as no DB connectivity for RecyclerView population
public class AdminActivityUITest {
    @Rule
    public ActivityScenarioRule<AdminActivity> scenario = new
            ActivityScenarioRule<AdminActivity>(AdminActivity.class);

    /**
     * Sets up the testing environment before each test execution. Initializes {@link EventViewModel}
     * and {@link UserViewModel} with mocked repositories and LiveData to simulate a controlled
     * test scenario. This method ensures that each test starts with a consistent and isolated
     * environment.
     */
    @Before
    public void setUp() {
        // Get the application context
        Application application = ApplicationProvider.getApplicationContext();
        EventRepository mockEventRepo = Mockito.mock(EventRepository.class);
        UserRepository mockProfileRepo = Mockito.mock(UserRepository.class);

        // Creating a MutableLiveData instance for testing
        MutableLiveData<List<Event>> testEvent = new MutableLiveData<>();
        MutableLiveData<List<com.example.cmput301w24t33.users.User>> testProfile = new MutableLiveData<>();
        MutableLiveData<User> testSingleUser = new MutableLiveData<>();

        // Initialize the EventViewModel with the mock components
        EventViewModel.initialize(application, mockEventRepo, testEvent);
        UserViewModel.initialize(application,mockProfileRepo,testProfile,testSingleUser);
    }

    /**
     * Verifies that the main admin layout and its components are displayed as expected. This test
     * checks the visibility of various UI elements in {@link AdminActivity}, including button layouts
     * and the action bar, ensuring that the initial state of the admin interface is correct.
     */
    @Test
    public void testAdminDisplay(){
        //Checks if Admin Layout is displayed
        onView(withId(R.id.admin_layout)).check(matches(isDisplayed()));

        //Checks if ButtonLayouts are displayed
        onView(withId(R.id.event_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.image_layout)).check(matches(isDisplayed()));

        //Checks if Buttons are displayed
        onView(withId(R.id.browse_events_button)).check(matches(isDisplayed()));
        onView(withId(R.id.browse_profiles_button)).check(matches(isDisplayed()));
        onView(withId(R.id.browse_images_button)).check(matches(isDisplayed()));

        //Checks if top ActionBar is displayed
        onView(withId(R.id.organizer_attendee_actionbar)).check(matches(isDisplayed()));
        onView(withId(R.id.button_user_mode)).check(matches(isDisplayed()));

        //Checks if Image layout is displayed
        onView(withId(R.id.image_background)).check(matches(isDisplayed()));

    }

    /**
     * Tests the display of the events view within the admin interface. It simulates a click on the
     * "Browse Events" button and verifies that the events list and related UI components are displayed
     * correctly. This test ensures that the admin can view the events as intended.
     */
    @Test
    public void testAdminViewEventsDisplay(){
        //Button from Admin view to event fragment
        onView(withId(R.id.browse_events_button)).perform(click());

        //Checks Admin events fragment is display
        onView(withId(R.id.admin_events_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.general_actionbar)).check(matches(isDisplayed()));

        //Checks list of events are displayed
        onView(withId(R.id.event_recyclerview)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_view_event_fragment)).check(matches(isDisplayed()));

        //Checks if top ActionBar is displayed
        onView(withId(R.id.general_actionbar_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.view_events_back_button)).check(matches(isDisplayed()));

    }

    /**
     * Tests the display of the profiles view within the admin interface. It simulates a click on the
     * "Browse Profiles" button and verifies that the profiles list and related UI components are displayed
     * correctly. This test ensures that the admin can view user profiles as intended.
     */
    @Test
    public void testAdminViewProfileDisplay(){
        //Button from Admin view to Profile fragment
        onView(withId(R.id.browse_profiles_button)).perform(click());

        //Checks Admin profile fragment is displayed
        onView(withId(R.id.admin_view_profiles_fragment)).check(matches(isDisplayed()));

        //Checks list of profiles are displayed
        onView(withId(R.id.profiles_admin)).check(matches(isDisplayed()));

        //Checks if top ActionBar is displayed
        onView(withId(R.id.general_actionbar)).check(matches(isDisplayed()));
        onView(withId(R.id.general_actionbar_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.view_profiles_back_button)).check(matches(isDisplayed()));

    }

    /**
     * Tests the display of the event images view within the admin interface. It simulates a click on the
     * "Browse Images" button and verifies that the event images layout and its components are displayed
     * correctly. This test ensures that the admin can view event images as intended.
     */
    @Test
    public void testAdminViewEventImageDisplay(){
        //Button from Admin view to Image fragment
        onView(withId(R.id.browse_images_button)).perform(click());

        //Checks Admin event images fragment is displayed
        onView(withId(R.id.admin_view_images_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.switch_image_type_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.event_poster_recyclerview)).check(matches(isDisplayed()));

    }

    /**
     * Tests the functionality of switching between event images and user images within the image view
     * of the admin interface. It verifies that the correct layouts are displayed after toggling the view
     * type and that the layout returns to the initial state upon toggling again.
     */
    @Test
    public void testAdminViewUserImageDisplayUser(){
        //Button from Admin view to Image fragment
        onView(withId(R.id.browse_images_button)).perform(click());

        //Switch to User Images
        onView(withId(R.id.switch_image_type_layout)).perform(click());
        onView(withId(R.id.user_image_recyclerview)).check(matches(isDisplayed()));

        //Check to see if layout is back to original fragment
        onView(withId(R.id.switch_image_type_layout)).perform(click());
        onView(withId(R.id.admin_view_images_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.switch_image_type_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.event_poster_recyclerview)).check(matches(isDisplayed()));


    }

    /**
     * Tests the back button functionality from various admin views. It verifies that navigating back from
     * event, profile, and image views returns the user to the correct initial admin layout, ensuring
     * the navigation within the admin interface works as intended.
     */
    @Test
    public void testAdminEventBackButton(){
        //Tests Admin fragments and if layout is correct
        onView(withId(R.id.admin_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.browse_events_button)).perform(click());
        onView(withId(R.id.view_events_back_button)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_layout)).check(matches(isDisplayed()));

        //Tests Profile fragment
        onView(withId(R.id.admin_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.browse_profiles_button)).perform(click());
        onView(withId(R.id.view_events_back_button)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_layout)).check(matches(isDisplayed()));

        //Tests Images fragment
        onView(withId(R.id.admin_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.browse_images_button)).perform(click());
        onView(withId(R.id.view_events_back_button)).check(matches(isDisplayed()));
        onView(withId(R.id.admin_layout)).check(matches(isDisplayed()));

    }


}
