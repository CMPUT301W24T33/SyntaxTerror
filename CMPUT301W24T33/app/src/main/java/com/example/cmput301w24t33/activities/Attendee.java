package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.qrCode.QRScanner;
import com.example.cmput301w24t33.users.CreateProfile;

import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.UserViewModel;


import java.util.ArrayList;
import java.util.List;

public class Attendee extends AppCompatActivity implements AdapterEventClickListener {
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private RecyclerView eventRecyclerView;
    private String userId;
    private QRScanner qrScanner = new QRScanner();
    private EventViewModel eventViewModel;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity);
        eventRecyclerView = findViewById(R.id.event_recyclerview);

        authenticateUser();

        displayEvents();

        setOnClickListeners();
    }

    /**
     * Updates event adapter with current contents in our events collection
     * @param events is a live representation of Events in our events collection as a List
     */
    private void updateUI(List<Event> events) {
        eventAdapter.setEvents(events);
    }

    /**
     * This method authorizes current user and loads up-to-date events for display
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
        eventViewModel.loadEvents();
    }

    private String getAndroidId() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public void authenticateUser() {
        userId = getAndroidId();
        Log.d(TAG, "Attendee Android ID: " + userId);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.queryUser(userId);
        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                // User has a profile
                Log.d(TAG, "User Authenticated: " + user.getUserId());
            } else {
                // New User
                replaceFragment(new CreateProfile());
            }
        });
    }

    private void displayEvents() {
        eventList = new ArrayList<>();
        setAdapter();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, events -> {
            updateUI(events);
        });
    }
    private void setAdapter(){
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(eventList,this, this);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }
    public void onEventClickListener(Event event, int position){
        replaceFragment(EventDetailsAttendee.newInstance(event));
    }


    private void setOnClickListeners(){

        // Profile button click listener
        ImageView profileButton = findViewById(R.id.profile_image);

        profileButton.setOnClickListener(v -> {
            replaceFragment(new Profile());
        });


        // Check in button click listener
        // used reviewgrower.com/button-and-badge-generator/ to quickly make a button
        ImageView checkInButton = findViewById(R.id.check_in_img);
        checkInButton.setOnClickListener(v -> {
            // fill in a fragment or whatever is decided for checkin
            qrScanner.scanQRCode(Attendee.this);
       });

        // User Mode click listener - switches to organizer activity
        ImageButton userMode = findViewById(R.id.button_user_mode);
        userMode.setOnClickListener(v -> {
            Intent intent = new Intent(Attendee.this, Organizer.class);
            intent.putExtra("uId", userId);
            startActivity(intent);
            finish();
        });

        // User Mode click listener - switches to admin activity
        userMode.setOnLongClickListener(v -> {
            // code for navigating to admin activity
            Intent intent = new Intent(Attendee.this, Admin.class);
            startActivity(intent);
            finish();
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.attendee_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}