package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.AttendeeAdapter;

import java.util.ArrayList;

public class EventAttendees extends Fragment {

    private RecyclerView attendeesRecyclerView;
    private final ArrayList<User> attendeesList = new ArrayList<>();
    private AttendeeAdapter attendeeAdapter;

    public EventAttendees() {
        // Required empty public constructor
    }

    public static EventAttendees newInstance() {
        return new EventAttendees();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_event_attendees_fragment, container, false);
        setupActionBar(view);
        setupClickListeners(view);
        setupAttendeesRecyclerView(view);
        loadSampleAttendees();
        return view;
    }

    private void setupAttendeesRecyclerView(@NonNull View view) {
        attendeesRecyclerView = view.findViewById(R.id.event_attendees_list);
        attendeesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Updated to reflect the removal of the click listener from the adapter's constructor
        attendeeAdapter = new AttendeeAdapter(attendeesList);

        attendeesRecyclerView.setAdapter(attendeeAdapter);
    }

    private void loadSampleAttendees() {
        attendeesList.add(new User("John", "Doe", "1"));
        attendeesList.add(new User("Jane", "Smith", "2"));
        // Notify the adapter of data changes
        attendeeAdapter.notifyDataSetChanged();
    }

    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Attendees");
    }

    private void setupClickListeners(@NonNull View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView profileButton = view.findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));
    }

    // Method to replace the current fragment with another, specified by the 'fragment' parameter
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.organizer_layout, fragment) // Ensure this container ID matches your layout
                .addToBackStack(null)
                .commit();
    }
}