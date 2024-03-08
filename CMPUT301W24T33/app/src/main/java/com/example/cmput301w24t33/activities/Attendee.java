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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.attendeeFragments.EventDetailsAttendee;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.qrCode.QRScanner;
import com.example.cmput301w24t33.users.CreateProfile;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserViewModel;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Activity class for attendee users, managing event display, user authentication, and profile interaction.
 */
public class Attendee extends AppCompatActivity {
    private FirebaseFirestore db;

    private ArrayList<Event> eventList;
    private EventAdapter allEventAdapter;
    private EventAdapter userEventAdapter;
    private RecyclerView allEventRecyclerView;
    private RecyclerView userEventRecyclerView;
    private String userId;
    private QRScanner qrScanner = new QRScanner();
    private EventViewModel eventViewModel;

    private User currentUser;
    private FusedLocationProviderClient fusedLocationProvider;
    private UserViewModel userViewModel;


    /**
     * Initializes the activity, setting up Firebase, RecyclerView for events, and listeners.
     * @param savedInstanceState Contains data from onSaveInstanceState(Bundle) if the activity is re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity);
        allEventRecyclerView = findViewById(R.id.all_event_recyclerview);
        userEventRecyclerView = findViewById(R.id.user_event_recyclerview);

        db = FirebaseFirestore.getInstance();
        
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        setupActionbar();
        authenticateUser();
        displayEvents();
        setOnClickListeners();
    }

    /**
     * Configures the ActionBar with specific settings for this activity, setting the text to indicate the user can attend events.
     */
    private void setupActionbar() {
        TextView actionBarText = findViewById(R.id.attendee_organizer_textview);
        actionBarText.setText("Attend Events");
    }

    /**
     * Updates the UI to display the latest events data.
     * @param events A list of events to be displayed.
     */
    private void updateUI(List<Event> events) {
        allEventAdapter.setEvents(events);
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

    /**
     * Navigates to the profile creation fragment.
     */
    private void registerUser() {
        replaceFragment(new CreateProfile());
    }

    /**
     * Sets up the RecyclerView adapter for displaying events.
     */
    private void setAdapter() {
        allEventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allEventRecyclerView.setHasFixedSize(true);
        allEventAdapter = new EventAdapter(eventList, this::onEventClickListener);
        allEventRecyclerView.setAdapter(allEventAdapter);
        allEventAdapter.notifyDataSetChanged();

        ArrayList<Event> testEventList = new ArrayList<Event>();
        userEventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userEventRecyclerView.setHasFixedSize(true);
        userEventAdapter = new EventAdapter(testEventList, this::onEventClickListener);
        userEventRecyclerView.setAdapter(userEventAdapter);
        userEventAdapter.notifyDataSetChanged();

    }

    /**
     * Switches between displaying all events and events the user is attending based on the current state, updating the button text accordingly.
     *
     * @param switchButton The button that triggers the switch between the all events view and the user's events view.
     */
    private void switchRecyclerView(Button switchButton) {
        boolean allEventsVisible = allEventRecyclerView.isClickable();

        switchButton.setText(allEventsVisible ?  "Click To Browse All Events" : "Click To Browse Your Events");

        // Toggle visibility and interactivity based on allEventsVisible
        allEventRecyclerView.setClickable(!allEventsVisible);
        allEventRecyclerView.setFocusable(!allEventsVisible);
        allEventRecyclerView.setVisibility(allEventsVisible ? View.GONE : View.VISIBLE);

        userEventRecyclerView.setClickable(allEventsVisible);
        userEventRecyclerView.setFocusable(allEventsVisible);
        userEventRecyclerView.setVisibility(allEventsVisible ? View.VISIBLE : View.GONE);
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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.queryUser(userId);
        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                // User has a profile
                Log.d(TAG, "User Authenticated: " + user.getUserId());
                currentUser = user;
            } else {
                // New User
                replaceFragment(new CreateProfile());
            }
        });
    }

    /**
     * Initializes and displays the list of events, setting up the adapter and handling live data updates from the ViewModel.
     */
    private void displayEvents() {
        eventList = new ArrayList<>();
        setAdapter();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, this::updateUI);
    }

    /**
     * Handles click events on individual events, navigating to the event details.
     * @param event The clicked event object.
     * @param position The position in the adapter of the clicked event.
     */
    public void onEventClickListener(Event event, int position) {
        replaceFragment(EventDetailsAttendee.newInstance(event));
    }

    /**
     * Sets click listeners for various UI components like profile and check-in.
     */
    private void setOnClickListeners() {
        ImageView profileButton = findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> {
            replaceFragment(new Profile());
        });

        ImageView checkInButton = findViewById(R.id.check_in_img);

        checkInButton.setOnClickListener(v -> {
            GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(this);

            // initializes scanner
            scanner
                    .startScan()
                    .addOnSuccessListener(
                            barcode -> {
                                this.HandleScannerResult(barcode.getRawValue());
                                // Task successful
                                Log.d("SCAN", "Scan Successful");
                            })
                    .addOnCanceledListener(
                            () -> {
                                // Task canceled
                                Log.d("SCAN", "Scan canceled");
                            })
                    .addOnFailureListener(
                            e -> {
                                // Task failed with an exception
                                Log.d("SCAN","Scan failed, try again: " + e.getMessage());
                            });
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

        Button switchRecyclerviewButton = findViewById(R.id.switch_recycler_view_button);
        switchRecyclerviewButton.setOnClickListener(v -> {
            switchRecyclerView(switchRecyclerviewButton);
        });
    }


    /**
     * Handles result of QR scanner
     * @param qrCode scanned qr code
     */
    private void HandleScannerResult(String qrCode){
        if (qrCode != null) {
            db.collection("events")
                    .whereEqualTo("checkInQR", qrCode)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot doc : task.getResult()) {
                                    Log.d("QRCheckIn", doc.getId() + "->" + doc.getData());
                                    String eventId = doc.getId();//doc.getData().get("checkInQR").toString();
                                    Log.d("QRCheckIn", eventId);
                                    Attendee.this.CheckIn(eventId);
                                }
                            }
                        }
                    });
        } else {
            Log.d("CheckIn", "Check in failed");
        }
    }

    /**
     * Checks user into event
     * @param eventId ID of event to be checked into
     */
    private void CheckIn(String eventId) {
        // TODO: Determine if event has GeoTracking enabled
        //  1: If GeoTracking is enabled
        //      1.1: nothing needed
        //  2: If GeoTethering is enabled
        //      2.1: prevent user from checking if they are not within some fixed distance of event
        //  3: If GeoTracking is disabled
        //      3.1: Don't store user's location (just set it to null?)
        Map<String, GeoPoint> update = new HashMap<>();
        update.put("test", new GeoPoint(1,1));
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        // gets location permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            // You can use the API that requires the permission.
            fusedLocationProvider.getLastLocation().addOnSuccessListener(this, location -> {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    update.put("location", new GeoPoint(location.getLatitude(),location.getLongitude()));
                } else {
                    update.put("location", null);
                }
            }).addOnFailureListener(this, v->{
                Log.d("Location", "Could not retrieve cached location");
            });

            // Retrieves Current Location
            fusedLocationProvider.getCurrentLocation(new CurrentLocationRequest.Builder().build(), null).addOnSuccessListener(this, location -> {
                if (location != null) {
                    // Logic to handle location object
                    update.put("location", new GeoPoint(location.getLatitude(),location.getLongitude()));
                    Log.d("Location", "Retrieved new Location");
                    db.collection("events").document(eventId)
                            .collection("attendees")
                            .document(userId)
                            .set(update).addOnSuccessListener(v->{
                                Log.d("CheckIn", "User Successfully checked in");
                            });
                } else {
                    update.put("location", null);
                }
            }).addOnFailureListener(this, u->{
                Log.d("Location", "Could not retrieve new location");
            });


            Log.d("CheckIn", "Permission Granted");





        }
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
        transaction.commit();
    }
}

