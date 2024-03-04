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

public class EventDetailsAttendee extends Fragment {

    private AttendeeEventFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = AttendeeEventFragmentBinding.inflate(inflater, container, false);
        setClickListeners();
        loadData();
        return binding.getRoot();
    }

    private void setClickListeners() {
        // Notification button
        binding.notificationsButton.setOnClickListener(v -> replaceFragment(NotificationsAttendee.newInstance()));
        // Back button
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        // Share QR button
        binding.shareQrCodeButton.setOnClickListener(v -> {});  // THIS NEEDS CODE TO ADD FUNCTIONALITY
        // Going or not going
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

    private void loadData() {
        String eventName = "";         // "Get From Database";
        String eventLocation = "";     // "Get From Database";
        String eventDateTime = "";     // "Get From Database";
        String eventDescription = "";  // "Get From Database";
        boolean isGoing = false;       // "Get From Database";
        // Also load poster... somehow

        binding.eventNameTextView.setText(eventName);
        binding.eventLocationTextView.setText(eventLocation);
        binding.eventStartEndDateTimeTextView.setText(eventDateTime);
        binding.eventDescriptionTextView.setText(eventDescription);

         if (isGoing) {
             binding.toggleButtonGroup.check(R.id.goingButton);
         } else {
             binding.toggleButtonGroup.check(R.id.notGoingButton);
         }

         // Also need code to load image
    }

    // Replace the current fragment with another fragment
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
