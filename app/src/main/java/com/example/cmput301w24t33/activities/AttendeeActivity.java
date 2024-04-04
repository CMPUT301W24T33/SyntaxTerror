// Purpose:
// An activity for attendees to view events, authenticate users, and navigate to profiles or event
// details, emphasizing a user-friendly interface for event interaction.
//
// Issues: Populate users events they will attend
//

package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.attendeeFragments.EventDetailsAttendee;
import com.example.cmput301w24t33.databinding.AttendeeActivityBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.example.cmput301w24t33.qrCode.QRCheckIn;
import com.example.cmput301w24t33.qrCode.QRFindEvent;
import com.example.cmput301w24t33.qrCode.QRScanner;
import com.example.cmput301w24t33.users.CreateProfile;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import java.util.Set;


/**
 * Activity class for attendee users, managing event display, user authentication, and profile interaction.
 */
public class AttendeeActivity extends AppCompatActivity implements CreateProfile.OnUserCreatedListener{
    private EventAdapter eventAdapter;
    private boolean viewingAllEvents = false;
    private AttendeeActivityBinding binding;
    private User currentUser;
    private String userId;
    private String userImageURL;
    private FusedLocationProviderClient fusedLocationProvider;
    private QRScanner qrScanner;
    private UserViewModel userViewModel;
    private EventViewModel eventViewModel;
    private ArrayList<Event> allEvents = new ArrayList<>();
    private ArrayList<Event> signedUpEvents = new ArrayList<>();

    /**
     * Initializes the activity, setting up Firebase, RecyclerView for events, and listeners.
     * @param savedInstanceState Contains data from onSaveInstanceState(Bundle) if the activity is re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AttendeeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up Background Animation
        AnimationDrawable animation = (AnimationDrawable) binding.getRoot().getBackground();
        animation.setEnterFadeDuration(10);
        animation.setExitFadeDuration(5000);
        animation.start();


        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        userId = getAndroidId();

        currentUser = (User) getIntent().getSerializableExtra("user");
        Log.w("HECK", currentUser.getFirstName());
        Log.w("PIC", currentUser.getImageUrl());
        //authenticateUser();
        //fetchInfo(findViewById(R.id.profile_image));
        setupRecyclerView();
        setupActionbar();
        setOnClickListeners();
    }

    /**
     * Handles actions to be taken when the activity resumes, including user authorization and events loading.
     */
    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager.initialize(this.getApplication());
        setupViewModel();
        setupActionbar();
        Log.d(TAG, "RESUME");
        eventViewModel.loadEvents();
    }


    /**
     * Switches between viewing all events and events the user has signed up for.
     * Updates the UI to reflect the current view state.
     */
    private void switchEventView() {
        // Recycler View Animations
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        binding.eventrecyclerview.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                viewingAllEvents = !viewingAllEvents;
                updateDisplayedEvents();
                binding.switchEventsButton.setText(viewingAllEvents ? "Browse Your Events" : "Browse All Events");
                binding.eventrecyclerview.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
    }


    /**
     * Updates the RecyclerView to display either all events or only the events the user has signed up for,
     * based on the current view state.
     */
    private void updateDisplayedEvents() {
        List<Event> eventsToShow = viewingAllEvents ? allEvents : signedUpEvents;
        eventAdapter.setEvents(eventsToShow);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and an EventAdapter.
     */
    private void setupRecyclerView() {
        eventAdapter = new EventAdapter(new ArrayList<>(), this::onEventClickListener);
        binding.eventrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.eventrecyclerview.setAdapter(eventAdapter);
    }

    /**
     * Initializes the ViewModel and sets up an observer for the events LiveData.
     * Updates the local lists of all events and signed-up events whenever the LiveData changes.
     */
    private void setupViewModel() {
        eventViewModel = EventViewModel.getInstance();
        eventViewModel.getEventsLiveData().observe(this, events -> {
            allEvents.clear();
            allEvents.addAll(events);
            eventListsFilter(events);
            updateDisplayedEvents();
        });
        eventViewModel.loadEvents();

    }

    /**
     * Filters the list of all events to find events that the user has signed up for.
     * Updates the local list of signed-up events.
     * @param events The full list of events to filter from.
     */
    private void eventListsFilter(List<Event> events) {
        signedUpEvents.clear();
        Set<String> notificationTrackedEvents = new HashSet<>();
        for (Event event : events) {
            if (event.getSignedUp().contains(currentUser)) {
                signedUpEvents.add(event);
                notificationTrackedEvents.add(event.getEventId());
            }
            if (event.getAttendees().contains(currentUser)) {
                notificationTrackedEvents.add(event.getEventId());
            }
            if (Objects.equals(event.getOrganizerId(), userId)) {
                NotificationManager.getInstance().trackAttendeeUpdatesForEvent(event.getEventId());
            }
        }
        NotificationManager.getInstance().trackMultipleEventsNotifications(notificationTrackedEvents);
    }

    /**
     * Configures the ActionBar with specific settings for this activity, setting the text to indicate the user can attend events.
     */
    private void setupActionbar() {
        TextView actionBarText = findViewById(R.id.attendee_organizer_textview);
        fetchInfo(findViewById(R.id.profile_image));
        actionBarText.setText("Attend Events");
    }

    /**
     * Retrieves the unique Android ID for the device. This ID is used to identify the user's device uniquely.
     * @return A string representing the Android ID of the device.
     */
    public String getAndroidId() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * Authenticates the user based on the device's Android ID, checking if a user profile already exists or initiating profile creation if not.
     */
    public void authenticateUser() {
        userId = getAndroidId();
        Log.d(TAG, "AttendeeActivity Android ID: " + userId);

        //userViewModel = new UserViewModel(userRepo, new MutableLiveData<>(), new MutableLiveData<>(), new User());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser(userId).observe(this, user -> {
            if (user != null) {
                // User has a profile
                Log.d(TAG, "User Authenticated: " + user.getUserId());
                currentUser = user;
                fetchInfo(findViewById(R.id.profile_image));
            } else {
                // New User
                replaceFragment(new CreateProfile());
            }
            setupViewModel();
        });
    }

    /**
     * Updates the profile button with the user's image from the URL provided in the user's profile.
     * @param profileButton The ImageView component used as the profile button.
     */
    private void fetchInfo(ImageView profileButton ) {
        userImageURL = currentUser.getImageUrl();

        Glide.with(this).load(userImageURL).into(profileButton);
    }

    /**
     * Handles click events on individual events, navigating to the event details.
     * @param event The clicked event object.
     * @param position The position in the adapter of the clicked event.
     */
    public void onEventClickListener(Event event, int position) {
        replaceFragment(EventDetailsAttendee.newInstance(event, currentUser));
    }

    /**
     * Sets click listeners for various UI components like profile and check-in.
     */
    private void setOnClickListeners() {
        ImageView profileButton = findViewById(R.id.profile_image);

        profileButton.setOnClickListener(v -> {
            replaceFragment(Profile.newInstance(currentUser));
        });
        binding.switchEventsButton.setOnClickListener(v -> switchEventView());

        ImageView checkInButton = findViewById(R.id.check_in_img);

        checkInButton.setOnClickListener(v -> {
            QRCheckIn qrCheckIn = new QRCheckIn(this, currentUser, allEvents);
            qrScanner = new QRScanner(qrCheckIn);
            qrScanner.scanQRCode(this);
           });

        MaterialButton switchEventsButton = findViewById(R.id.switch_events_button);

        switchEventsButton.setOnClickListener(v -> {
            switchEventView();
        });

        ImageView findEventButton = findViewById(R.id.find_event_img);

        findEventButton.setOnClickListener(v ->{
            QRFindEvent qrFindEvent = new QRFindEvent(this, allEvents);
            qrScanner = new QRScanner((qrFindEvent));
            qrScanner.scanQRCode(this);

        });

        ImageButton userMode = findViewById(R.id.button_user_mode);
        userMode.setOnClickListener(v -> {
            // Switch to OrganizerActivity activity
            Intent intent = new Intent(AttendeeActivity.this, OrganizerActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            finish();
        });

        userMode.setOnLongClickListener(v -> {
            // Switch to AdminActivity activity
            Intent intent = new Intent(AttendeeActivity.this, AdminActivity.class);
            intent.putExtra("uId", userId);
            intent.putExtra("user", currentUser);
            startActivity(intent);
            finish();
            return true;
        });
    }

    /**
     * Replaces the current fragment with a new one.
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.attendee_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Handles the result of a QR code scan to find an event, navigating to the event details if an event is found.
     * @param event The event found as a result of the QR code scan.
     */
    public void onFindEventResult(Event event){
        replaceFragment(EventDetailsAttendee.newInstance(event, currentUser));
    }

    /**
     * Called when a new user profile is created. It updates the ViewModel with the new user and updates the UI accordingly.
     * @param user The newly created user.
     */
    @Override
    public void onUserCreated(User user) {
        userViewModel.setUser(user);
        currentUser = user;
        fetchInfo(findViewById(R.id.profile_image));
    }
}

