package com.example.cmput301w24t33.attendeeFragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.Profile;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Objects;

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
        setGoingToggleClickListener(view);
        setBackArrowClickListener(view);
        setNotificationClickListener(view);
    }

    private void setNotificationClickListener(View view) {
        MaterialButton notificationButton = view.findViewById(R.id.notifications_button);
        notificationButton.setOnClickListener(v -> replaceFragment(NotificationsAttendee.newInstance()));
    }

    private void setBackArrowClickListener(View view) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void setGoingToggleClickListener(View view) {
        MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.toggleButtonGroup);
        MaterialButton goingButton = view.findViewById(R.id.goingButton);
        MaterialButton notGoingButton = view.findViewById(R.id.notGoingButton);

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.goingButton) {
                if (isChecked) {
                    // User has selected "Going"
                    goingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.going_button_background));
                    notGoingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.not_going_button_background));
                }
            } else if (checkedId == R.id.notGoingButton) {
                if (isChecked) {
                    // User has selected "Not Going"
                    goingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.going_button_background));
                    notGoingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.not_going_button_background));
                }
            }
        });
    }

    // Replace the current fragment with another fragment
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
