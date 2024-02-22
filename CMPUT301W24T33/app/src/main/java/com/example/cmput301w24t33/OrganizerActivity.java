package com.example.cmput301w24t33;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrganizerActivity extends AppCompatActivity implements AdapterEventClickListener{
    private ArrayList<Event> organizedEvents;
    private RecyclerView eventRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_activity);
        eventRecyclerView = findViewById(R.id.organized_events);
        setEvents();
        setAdapter();
        ImageView profileButton = findViewById(R.id.profile_image);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.attendee_layout,fragment);
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
//        replaceFragment(new OrganizerEventFragment());
    }
}
