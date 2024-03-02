package com.example.cmput301w24t33.adminFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.organizerFragments.EventDetailsOrganizer;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;

import java.util.ArrayList;
import java.util.List;

public class Events extends Fragment implements AdapterEventClickListener{
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private EventViewModel eventViewModel;
    private RecyclerView eventRecyclerView;

    public Events(){}

    public static Events newInstance() {
        Events fragment = new Events();
        Bundle args = new Bundle();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_fragment, container, false);
        setupClickListeners(view);
        setupActionBar(view);
        displayEvents(view);
        return view;

    }

    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("All Events");
    }

    private void setupClickListeners(View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
    /**
     * Updates event adapter with current contents in our events collection
     * @param events is a live representation of Events in our events collection as a List
     */
    private void updateUI(List<Event> events) {
        eventAdapter.setEvents(events);
    }

    private void setAdapter(){
        Context context = getContext();
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(eventList,this);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }
    private void displayEvents(View view) {
        Log.d(TAG, "EVENTS FRAGMENT: DISPLAY EVENTS");
        eventRecyclerView = view.findViewById(R.id.event_recyclerview);

        eventList = new ArrayList<>();
        setAdapter();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), events -> {
            updateUI(events);
        });
    }

    @Override
    public void onEventClickListener(Event event, int position) {

    }
}
