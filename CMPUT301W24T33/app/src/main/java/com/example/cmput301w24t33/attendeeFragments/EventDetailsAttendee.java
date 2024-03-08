// Purpose:
// A fragment designed for attendees to view detailed information about an event, allowing them to
// interact by registering their attendance status and accessing notifications.
//
// Issues:
//
//

package com.example.cmput301w24t33.attendeeFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.attendeeFragments.NotificationsAttendee;
import com.example.cmput301w24t33.databinding.AttendeeEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;

/**
 * A fragment for displaying event details to an attendee. Includes viewing event notifications and sharing QR codes.
 */
public class EventDetailsAttendee extends Fragment {

    private AttendeeEventFragmentBinding binding;

    /**
     * Creates a new instance of EventDetailsAttendee fragment with event details.
     * @param event The event to display.
     * @return A new instance of EventDetailsAttendee.
     */
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
        Event event = getArguments() != null ? (Event) getArguments().getSerializable("event") : null;
        setClickListeners();
        if (event != null) {
            loadData(event);
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Sets click listeners for UI interactions.
     */
    private void setClickListeners() {
        // Click listeners for notifications, navigation, and QR code sharing.
        binding.notificationsButton.setOnClickListener(v -> replaceFragment(new NotificationsAttendee()));
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.shareQrCodeButton.setOnClickListener(v -> {
            // QR code sharing functionality
        });
        binding.toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            updateAttendanceStatus(checkedId, isChecked);
        });
    }

    /**
     * Loads the event data into the fragment's UI elements.
     * @param event The event whose details are to be displayed.
     */
    private void loadData(Event event) {
        String eventStartDate = event.getStartDate();
        String eventEndDate = event.getEndDate();
        String eventStartTime = event.getStartTime();
        String eventEndTime = event.getEndTime();
        String eventDateTime = "Start: " + eventStartTime + " on " + eventStartDate + "\nEnd:   " + eventEndTime + " on " + eventEndDate;

        binding.eventNameTextView.setText(event.getName());
        binding.eventLocationTextView.setText(event.getAddress());
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

    /**
     * Updates attendance status based on user selection.
     *
     * @param checkedId The ID of the checked button.
     * @param isChecked Whether the button is checked.
     */
    private void updateAttendanceStatus(int checkedId, boolean isChecked) {
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
    }

    /**
     * Replaces the current fragment with another fragment.
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
