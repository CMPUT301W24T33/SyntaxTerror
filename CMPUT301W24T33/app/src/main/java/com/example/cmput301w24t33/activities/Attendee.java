// Purpose:
// An activity for attendees to view events, authenticate users, and navigate to profiles or event
// details, emphasizing a user-friendly interface for event interaction.
//
// Issues:
//

package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.cmput301w24t33.users.GetUserCallback;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class for attendee users, managing event display, user authentication, and profile interaction.
 */
public class Attendee extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private RecyclerView eventRecyclerView;
    private FirebaseAuth mAuth;
    private String userId;
    private QRScanner qrScanner = new QRScanner();
    private EventViewModel eventViewModel;

    /**
     * Initializes the activity, setting up Firebase, RecyclerView for events, and listeners.
     * @param savedInstanceState Contains data from onSaveInstanceState(Bundle) if the activity is re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity);
        db = FirebaseFirestore.getInstance();
        eventRecyclerView = findViewById(R.id.event_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        eventList = new ArrayList<>();
        setAdapter();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, this::updateUI);
        setOnClickListeners();
    }

    /**
     * Updates the UI to display the latest events data.
     * @param events A list of events to be displayed.
     */
    private void updateUI(List<Event> events) {
        eventAdapter.setEvents(events);
    }

    /**
     * Handles actions to be taken when the activity resumes, including user authorization and events loading.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
        authorizeUser();
        eventViewModel.loadEvents();
    }

    /**
     * Launches activity for anonymous authentication and registers a new user upon completion.
     */
    private ActivityResultLauncher<Intent> anonymousAuthLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (data != null) {
                    userId = data.getStringExtra("USER_ID");
                    Log.d(TAG, "Received user ID: " + userId);
                    registerUser();
                }
            });

    /**
     * Checks if the user is signed in and authorized; otherwise, launches the AnonymousAuthActivity for sign-in.
     */
    private void authorizeUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "User not signed in. Launch anonAuth");
            Intent intent = new Intent(this, AnonymousAuthActivity.class);
            anonymousAuthLauncher.launch(intent);
        } else {
            userId = currentUser.getUid();
            Log.d(TAG, userId);
        }
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
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(eventList, this::onEventClickListener);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Saves a new user to the Firestore database.
     * @param newUser The new User object to be saved.
     */
    public void setUserDb(User newUser) {
        db.collection("users")
                .document(userId)
                .set(newUser)
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
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
     * Queries Firestore for a user by their document ID and handles the result through a callback interface.
     * @param callback The callback to handle the user query result.
     */
    private void queryUserByDocId(GetUserCallback callback) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User currentUser = documentSnapshot.toObject(User.class);
                callback.onUserReceived(currentUser);
            } else {
                Log.d(TAG, "No such document");
                callback.onUserReceived(null);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error getting document", e);
            callback.onFailure(e);
        });
    }

    /**
     * Sets click listeners for various UI components like profile and check-in.
     */
    private void setOnClickListeners() {
        ImageView profileButton = findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> {
            // Implementation for profile interaction
            queryUserByDocId(new GetUserCallback() {
                @Override
                public void onUserReceived(User user) {
                    // Handle user object, e.g., display user profile
                }

                @Override
                public void onFailure(Exception e) {
                    // Handle failure to retrieve user
                }
            });
            replaceFragment(new Profile());
        });

        ImageView checkInButton = findViewById(R.id.check_in_img);
        checkInButton.setOnClickListener(v -> qrScanner.scanQRCode(Attendee.this));

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
        transaction.commit();
    }
}

