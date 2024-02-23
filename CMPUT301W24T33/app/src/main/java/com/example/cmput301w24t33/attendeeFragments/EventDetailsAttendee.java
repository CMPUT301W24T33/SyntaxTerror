package com.example.cmput301w24t33.attendeeFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.profile.Profile;

public class EventDetailsAttendee extends Fragment {

    public EventDetailsAttendee() {
        // Required empty public constructor
    }

    public static EventDetailsAttendee newInstance() {
        return new EventDetailsAttendee();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendee_event_fragment, container, false);
        setOnClickListeners(view);
        return view;
    }

    // Sets onClick listeners for the actionable elements within the fragment
    private void setOnClickListeners(View view){
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Event Details");

        // Back button click listener to return to the previous screen
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Profile button to navigate to the Profile fragment
        ImageView profileButton = view.findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));

        // Notifications button to navigate to the NotificationsAttendee fragment
        ImageButton notificationButton = view.findViewById(R.id.notifications_button);
        notificationButton.setOnClickListener(v -> replaceFragment(NotificationsAttendee.newInstance()));
    }

    // Replace the current fragment with another fragment
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
