package com.example.cmput301w24t33.attendeeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AttendeeEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.attendeeFragments.EventDetailsAttendee;

public class EventDetailsAttendee extends Fragment {

    private AttendeeEventFragmentBinding binding;

    public static EventDetailsAttendee newInstance(Event event) {
        EventDetailsAttendee fragment = new EventDetailsAttendee();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = AttendeeEventFragmentBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        Bundle bundle = getArguments();
        Event event = (Event) bundle.getSerializable("event");
        setClickListeners();
        loadData(event);
        return binding.getRoot();
    }

    private void setClickListeners() {
        binding.notificationsButton.setOnClickListener(v -> replaceFragment(NotificationsAttendee.newInstance()));
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.shareQrCodeButton.setOnClickListener(v -> {
            // TODO:
        });
        binding.toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.goingButton) {
                if (isChecked) {
                    // User has selected "Going"
                    binding.goingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.going_button_background));
                    binding.notGoingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.not_going_button_background));
                }
            } else if (checkedId == R.id.notGoingButton) {
                if (isChecked) {
                    // User has selected "Not Going"
                    binding.goingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.going_button_background));
                    binding.notGoingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.not_going_button_background));
                }
            }
        });
    }

    private void loadData(Event event) {
        String eventStartDate = event.getStartDate();
        String eventEndDate = event.getEndDate();
        String eventStartTime = event.getStartTime();
        String eventEndTime = event.getEndTime();
        String eventDateTime = "Start: " +eventStartTime + " on " + eventStartDate + "\nEnd:   " + eventEndTime + " on " + eventEndDate;

        binding.eventNameTextView.setText(event.getEventDescription());
        binding.eventLocationTextView.setText(event.getLocationData());
        binding.eventDescriptionTextView.setText(event.getEventDescription());
        binding.eventStartEndDateTimeTextView.setText(eventDateTime);

        // STILL  NEED TO LOAD IMAGE AND GOING/NOT GOING STATUS FROM DATABASE
        boolean isGoing = false; // Placeholder status
         if (isGoing) {
             binding.toggleButtonGroup.check(R.id.goingButton);
         } else {
             binding.toggleButtonGroup.check(R.id.notGoingButton);
         }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Replace the current fragment with another fragment
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
