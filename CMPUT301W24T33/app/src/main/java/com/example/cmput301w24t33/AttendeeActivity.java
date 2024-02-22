package com.example.cmput301w24t33;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.util.ArrayList;

public class AttendeeActivity extends AppCompatActivity implements AdapterEventClickListener {

    private ArrayList<Event> eventList;
    private RecyclerView eventRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity);
        eventRecyclerView = findViewById(R.id.event_recyclerview);
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
        eventList = new ArrayList<>();
        eventList.add(new Event("Test1"));
        eventList.add(new Event("Test2"));
        eventList.add(new Event("Test3"));
        eventList.add(new Event("Test4"));
        eventList.add(new Event("Test5"));
        eventList.add(new Event("Test6"));
        eventList.add(new Event("Test7"));
        eventList.add(new Event("Test8"));
        eventList.add(new Event("Test9"));
        eventList.add(new Event("Test10"));
        eventList.add(new Event("Test11"));
        eventList.add(new Event("Test12"));
    }

    private void setAdapter(){
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        EventAdapter eventAdapter = new EventAdapter(eventList,this);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }

    public void onEventClickListener(Event event, int position){
        replaceFragment(new AttendeeEventFragment());
    }

}