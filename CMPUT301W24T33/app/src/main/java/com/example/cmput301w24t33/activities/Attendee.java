// Purpose:
// An activity for attendees to view events, authenticate users, and navigate to profiles or event
// details, emphasizing a user-friendly interface for event interaction.
//
// Issues: Populate users events they will attend
//

package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AttendeeActivityBinding;
import com.example.cmput301w24t33.attendeeFragments.EventDetailsAttendee;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.qrCode.QRCheckIn;
import com.example.cmput301w24t33.qrCode.QRCode;
import com.example.cmput301w24t33.qrCode.QRFindEvent;
import com.example.cmput301w24t33.qrCode.QRScanner;
import com.example.cmput301w24t33.users.CreateProfile;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.example.cmput301w24t33.users.UserViewModel;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Activity class for attendee users, managing event display, user authentication, and profile interaction.
 */
public class Attendee extends AppCompatActivity implements CreateProfile.OnUserCreatedListener{
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

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        userId = getAndroidId();

        authenticateUser();
        setupRecyclerView();
        setupViewModel();
        setupActionbar();
        setOnClickListeners();
    }

    /**
     * Switches between viewing all events and events the user has signed up for.
     * Updates the UI to reflect the current view state.
     */
    private void switchEventView() {
        viewingAllEvents = !viewingAllEvents;
        updateDisplayedEvents();
        binding.switchEventsButton.setText(viewingAllEvents ? "Browse Your Events" : "Browse All Events");
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
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, events -> {
            allEvents.clear();
            allEvents.addAll(events);
            userSignUpFilter(events);
            updateDisplayedEvents();
        });
        eventViewModel.loadEvents();
    }

    /**
     * Filters the list of all events to find events that the user has signed up for.
     * Updates the local list of signed-up events.
     * @param events The full list of events to filter from.
     */
    private void userSignUpFilter(List<Event> events) {
        signedUpEvents.clear();
        for (Event event : events) {
            if (event.getSignedUp().contains(currentUser)) {
                signedUpEvents.add(event);
            }
        }
    }

    /**
     * Configures the ActionBar with specific settings for this activity, setting the text to indicate the user can attend events.
     */
    private void setupActionbar() {
        TextView actionBarText = findViewById(R.id.attendee_organizer_textview);
        actionBarText.setText("Attend Events");
    }

    /**
     * Handles actions to be taken when the activity resumes, including user authorization and events loading.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
        eventViewModel.loadEvents();
    }

    public String getAndroidId() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * Authenticates the user based on the device's Android ID, checking if a user profile already exists or initiating profile creation if not.
     */
    public void authenticateUser() {
        userId = getAndroidId();
        Log.d(TAG, "Attendee Android ID: " + userId);

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


    private void fetchInfo(ImageView profileButton ) {
        userImageURL = currentUser.getImageUrl();
        Picasso.get().load(userImageURL).into(profileButton);
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
            // Switch to Organizer activity
            Intent intent = new Intent(Attendee.this, Organizer.class);
            intent.putExtra("uId", userId);
            startActivity(intent);
            finish();
        });

        userMode.setOnLongClickListener(v -> {
            // Switch to Admin activity
            Intent intent = new Intent(Attendee.this, Admin.class);
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

    public void onFindEventResult(Event event){
        replaceFragment(EventDetailsAttendee.newInstance(event, currentUser));
    }

    @Override
    public void onUserCreated(User user) {
        userViewModel.setUser(user);
        currentUser = user;
        fetchInfo(findViewById(R.id.profile_image));
    }
}

