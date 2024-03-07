// Purpose:
// Supports event organizers by enabling event creation, editing, and detail viewing, alongside
// user mode switching for enhanced application navigation.
//
// Issues:
//

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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.organizerFragments.EventCreateEdit;
import com.example.cmput301w24t33.organizerFragments.EventDetails;
import com.example.cmput301w24t33.users.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for the event organizer, managing the creation, editing, and viewing of events.
 */
public class Organizer extends AppCompatActivity {
    private ArrayList<Event> organizedEvents;
    private RecyclerView eventRecyclerView;
    private EventViewModel eventViewModel;
    private EventAdapter eventAdapter;
    private String userId;

    /**
     * Sets up the activity, including the RecyclerView for events, ViewModel for event data, and action bar.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_activity);
        eventRecyclerView = findViewById(R.id.organized_events);
        organizedEvents = new ArrayList<>();
        userId = getIntent().getStringExtra("uId");
        setAdapter();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, this::updateUI);

        View view = findViewById(R.id.organizer_activity);
        setupActionBar(view);
        setOnClickListeners();
    }

    /**
     * Updates the UI with a list of events.
     * @param events List of Event objects to be displayed.
     */
    private void updateUI(List<Event> events) {
        eventAdapter.setEvents(events);
    }

    /**
     * Loads and displays events organized by the current user upon resuming the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
        eventViewModel.loadOrganizerEvents(userId);
    }

    /**
     * Replaces the current fragment with the specified fragment.
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.organizer_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Initializes the RecyclerView adapter for displaying events.
     */
    private void setAdapter() {
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(organizedEvents, this::onEventClickListener);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Handles click events on individual events, directing to event details or editing.
     * @param event The event that was clicked.
     * @param position The position in the adapter of the clicked event.
     */
    public void onEventClickListener(Event event, int position) {
        replaceFragment(EventDetails.newInstance(event));
    }

    /**
     * Sets click listeners for UI components like profile view, event creation, and user mode switch.
     */
    private void setOnClickListeners() {
        ImageView profileButton = findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));

        FloatingActionButton createEvent = findViewById(R.id.button_create_event);
        createEvent.setOnClickListener(v -> replaceFragment(new EventCreateEdit()));

        ImageButton userMode = findViewById(R.id.button_user_mode);
        userMode.setOnClickListener(v -> {
            Intent intent = new Intent(Organizer.this, Attendee.class);
            startActivity(intent);
            finish();
        });

        userMode.setOnLongClickListener(v -> {
            Intent intent = new Intent(Organizer.this, Admin.class);
            startActivity(intent);
            finish();
            return true;
        });
    }

    /**
     * Customizes the action bar's appearance to match the organizer theme.
     * @param view The current view that includes the action bar.
     */
    private void setupActionBar(View view) {
        RelativeLayout attendeeOrganizerActionbar = view.findViewById(R.id.organizer_attendee_actionbar);
        int color = ContextCompat.getColor(this, R.color.organizer_actionbar);
        attendeeOrganizerActionbar.setBackgroundColor(color);
    }
}
