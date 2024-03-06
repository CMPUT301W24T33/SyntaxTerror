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

/**
 * Fragment for displaying details of an event to an attendee. It offers actions like viewing event notifications and sharing QR codes.
 */
public class EventDetailsAttendee extends Fragment {

    private AttendeeEventFragmentBinding binding;

    /**
     * Factory method to create a new instance of this fragment using the provided event details.
     *
     * @param event The event whose details are to be displayed.
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AttendeeEventFragmentBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        Event event = (Event) getArguments().getSerializable("event");
        setClickListeners();
        assert event != null;
        loadData(event);
        return binding.getRoot();
    }

    /**
     * Initializes click listeners for UI elements in the fragment.
     */
    private void setClickListeners() {
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.notificationsButton.setOnClickListener(v -> replaceFragment(new NotificationsAttendee()));
        binding.shareQrCodeButton.setOnClickListener(v -> {
            // TODO: Placeholder for QR code sharing functionality
        });
        binding.toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            updateAttendanceStatus(checkedId, isChecked);
        });
    }

    /**
     * Loads the event data into the UI elements of the fragment.
     *
     * @param event The event to display.
     */
    private void loadData(Event event) {
        String eventDateTime = String.format("Start: %s on %s\nEnd:   %s on %s",
                event.getStartTime(), event.getStartDate(),
                event.getEndTime(), event.getEndDate());

        binding.eventNameTextView.setText(event.getEventDescription());
        binding.eventLocationTextView.setText(event.getLocationData());
        binding.eventDescriptionTextView.setText(event.getEventDescription());
        binding.eventStartEndDateTimeTextView.setText(eventDateTime);

        // TODO: Load event attendance status and image from database
        boolean isGoing = false; // Example status, replace with actual data fetch
        binding.toggleButtonGroup.check(isGoing ? R.id.goingButton : R.id.notGoingButton);
    }

    /**
     * Updates the visual attendance status based on the user's selection.
     *
     * @param checkedId The ID of the button that was clicked.
     * @param isChecked Whether the button is checked.
     */
    private void updateAttendanceStatus(int checkedId, boolean isChecked) {
        if (isChecked) {
            int colorGoing = ContextCompat.getColor(requireContext(), R.color.going_button_background);
            int colorNotGoing = ContextCompat.getColor(requireContext(), R.color.not_going_button_background);

            // Update the tint for Going and Not Going buttons based on which button is checked
            if (checkedId == R.id.goingButton) {
                binding.goingButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(colorGoing));
                binding.notGoingButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(colorNotGoing));
            } else if (checkedId == R.id.notGoingButton) {
                binding.goingButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(colorNotGoing));
                binding.notGoingButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(colorGoing));
            }
        }
    }

    /**
     * Replaces the current fragment with the specified fragment.
     *
     * @param fragment The fragment to display next.
     */
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
