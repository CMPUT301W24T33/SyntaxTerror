package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.organizerFragments.EventCreateEdit;
import com.example.cmput301w24t33.organizerFragments.EventDetails;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * The Organizer activity serves as the interface for event organizers. It allows organizers
 * to view, create, and edit events. This class also provides functionality for switching
 * between Organizer and Attendee modes, as well as to an Admin mode for users with the
 * appropriate access.
 */
public class Organizer extends AppCompatActivity implements AdapterEventClickListener {
    private ArrayList<Event> organizedEvents;
    private RecyclerView eventRecyclerView;
    private EventViewModel eventViewModel;
    private EventAdapter eventAdapter;
    private String userId;

    /**
     * Called when the activity is first created. Initializes the RecyclerView for displaying
     * organized events, sets up the EventViewModel for observing event data, and configures
     * the action bar and click listeners for UI elements.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down, this Bundle contains the data it most recently supplied
     *                           in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_activity);
        eventRecyclerView = findViewById(R.id.organized_events);
        organizedEvents = new ArrayList<>();
        userId = getIntent().getStringExtra("uId");
        setAdapter();

        // Creates a new EventViewModel so we can display Events
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, events -> {
            updateUI(events);
        });

        View view = findViewById(R.id.organizer_activity);
        setupActionBar(view);
        setOnClickListeners(); // function to set on click listeners to keep oncreate clean
    }

    /**
     * Updates event adapter with Events organized by our current user
     * @param events is a live representation of Events in our events collection as a List
     */
    private void updateUI(List<Event> events) {
        eventAdapter.setEvents(events);
    }

    /**
     * This method loads and displays Events organized by current user
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
        eventViewModel.loadOrganizerEvents(userId);
    }

    /**
     * Replaces the current fragment in the Organizer activity with the specified fragment.
     * This method allows for dynamic navigation between different parts of the Organizer interface.
     *
     * @param fragment The new Fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.organizer_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Initializes the EventAdapter and configures the RecyclerView used for displaying
     * the list of organized events.
     */
    private void setAdapter(){
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(organizedEvents,this, this);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }


    /**
     * Handles click events on individual events in the list. This method is triggered when
     * an event is selected by the user, typically leading to the event's edit screen.
     *
     * @param event    The Event that was clicked.
     * @param position The position of the clicked event in the list.
     */
    @Override


    public static EventCreateEdit newInstance(Event event) {
        EventCreateEdit fragment = new EventCreateEdit();
        Log.d(MotionEffect.TAG, "EventCreateEdit NewInstance");
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    public void onEventClickListener(Event event, int position) {
       replaceFragment(EventDetails.newInstance(event));
    }

    /**
     * Sets up click listeners for various UI elements in the Organizer activity, such as
     * the profile button, create event button, and mode switch buttons.
     */
    private void setOnClickListeners(){
        ImageView profileButton = findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));

        // Create event click listener
        FloatingActionButton createEvent = findViewById(R.id.button_create_event);
        createEvent.setOnClickListener(v -> replaceFragment(new EventCreateEdit()));

        // User Mode click listener - switches to attendee activity
        ImageButton userMode = findViewById(R.id.button_user_mode);
        userMode.setOnClickListener(v -> {
            Intent intent = new Intent(Organizer.this, Attendee.class);
            startActivity(intent);
            finish();
        });

        // User Mode click listener - switches to admin activity
        userMode.setOnLongClickListener(v -> {
            Intent intent = new Intent(Organizer.this, Admin.class);
            startActivity(intent);
            finish();
            return true;
        });
    }

    /**
     * Configures the action bar specific to the Organizer activity, including setting the
     * background color to distinguish it from other modes.
     *
     * @param view The current view context used for finding and modifying the action bar components.
     */
    private void setupActionBar(View view) {
        // update color of actionbar
        RelativeLayout attendeeOrganizerActionbar = view.findViewById(R.id.organizer_attendee_actionbar);
        int color = ContextCompat.getColor(this,R.color.organizer_actionbar);
        attendeeOrganizerActionbar.setBackgroundColor(color);
    }
}
