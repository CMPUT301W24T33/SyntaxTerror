// Purpose:
// A fragment designed for attendees to view detailed information about an event, allowing them to
// interact by registering their attendance status and accessing notifications.
//
// Issues:
// record attending status, current just a toggle button
// implement a poster from event with a way to view it

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
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.users.User;

/**
 * A fragment for displaying event details to an attendee. Includes viewing event notifications and sharing QR codes.
 */
public class EventDetailsAttendee extends Fragment {

    private AttendeeEventFragmentBinding binding;
    private Event event;
    private User user;
    /**
     * Creates a new instance of EventDetailsAttendee fragment with event details.
     *
     * @param event  The event to display.
     * @param user The current user.
     * @return A new instance of EventDetailsAttendee.
     */
    public static EventDetailsAttendee newInstance(Event event, User user) {
        EventDetailsAttendee fragment = new EventDetailsAttendee();
        Bundle args = new Bundle();
        args.putSerializable("userId", user);
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment. Initializes the binding, sets up click listeners, and loads event data into UI components.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = AttendeeEventFragmentBinding.inflate(inflater, container, false);
        event = getArguments() != null ? (Event) getArguments().getSerializable("event") : null;
        user = getArguments() != null ? (User) getArguments().getSerializable("userId") : null;
        setClickListeners();
        if (event != null) {
            loadData();
        }
        return binding.getRoot();
    }

    /**
     * Cleans up resources associated with the view hierarchy. This method is called when the view previously created by onCreateView is about to be destroyed.
     */
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
            updateSignedUpStatus(checkedId, isChecked);
        });
    }

    /**
     * Loads the event data into the fragment's UI elements.
     */
    private void loadData() {
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
        boolean isGoing = event.checkSignedUp(user);
        if (isGoing) {
            binding.toggleButtonGroup.check(R.id.goingButton);
        } else {
            binding.toggleButtonGroup.check(R.id.notGoingButton);
        }
    }

    private void updateSignedUpStatus(int checkedId, boolean isChecked) {
        if (checkedId == R.id.goingButton) {
            if (isChecked) {
                // User has selected "Going"
                event.setSignedUp(user);
                binding.goingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.going_button_background));
                binding.notGoingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.not_going_button_background));
            }
        } else if (checkedId == R.id.notGoingButton) {
            if (isChecked) {
                // User has selected "Not Going"
                event.removeSignedUp(user);
                binding.goingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.going_button_background));
                binding.notGoingButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.not_going_button_background));
            }
        }
        EventRepository eventRepo = new EventRepository();
        eventRepo.updateEvent(event);
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
