package com.example.cmput301w24t33.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.organizerFragments.EventDetailsOrganizer;
import com.example.cmput301w24t33.profile.Profile;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;

import java.util.ArrayList;

public class Organizer extends AppCompatActivity implements AdapterEventClickListener {
    private ArrayList<Event> organizedEvents;
    private RecyclerView eventRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_activity);
        eventRecyclerView = findViewById(R.id.organized_events);
        setEvents();
        setAdapter();
        setOnClickListeners(); // function to set on click listeners to keep oncreate clean
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.organizer_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setEvents(){
        organizedEvents = new ArrayList<>();
        organizedEvents.add(new Event("Test1", "Party"));
        organizedEvents.add(new Event("Test2", "Party"));
        organizedEvents.add(new Event("Test3", "Party"));
        organizedEvents.add(new Event("Test4", "Party"));
        organizedEvents.add(new Event("Test5", "Party"));
        organizedEvents.add(new Event("Test6", "Party"));
        organizedEvents.add(new Event("Test7", "Party"));
        organizedEvents.add(new Event("Test8", "Party"));
        organizedEvents.add(new Event("Test9", "Party"));
        organizedEvents.add(new Event("Test10", "Party"));
        organizedEvents.add(new Event("Test11", "Party"));
        organizedEvents.add(new Event("Test12", "Party"));
    }

    private void setAdapter(){
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        EventAdapter eventAdapter = new EventAdapter(organizedEvents,this);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onEventClickListener(Event event, int position) {
       replaceFragment(new EventDetailsOrganizer());
    }
    private void setOnClickListeners(){
        ImageView profileButton = findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));

        ImageButton userMode = findViewById(R.id.button_user_mode);
        userMode.setOnClickListener(v -> {
            Intent intent = new Intent(Organizer.this, Attendee.class);
            startActivity(intent);
            finish();
        });
    }
}
