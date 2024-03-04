package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerEventDetailsFragmentBinding;

public class EventDetails extends Fragment {

    private OrganizerEventDetailsFragmentBinding binding;

    public static EventDetails newInstance(String eventID) {
        EventDetails fragment = new EventDetails();
        Bundle args = new Bundle();
        args.putString("EVENT_ID", eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = OrganizerEventDetailsFragmentBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        String eventID = getArguments().getString("EVENT_ID");
        setupActionButtons(eventID);
        loadData();
        return binding.getRoot();
    }

    private void setupActionButtons(String eventID) {
        binding.shareQrCodeButton.setOnClickListener(v -> {});  // ADD METHOD TO SHARE QR CODE
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.checkInsButton.setOnClickListener(v -> replaceFragment(new EventAttendees()));
//        binding.signUpsButton.setOnClickListener(v -> replaceFragment(new EventSignedUp()));      EVENT SIGNED-UP FRAGMENT NOT MADE YET
        binding.notificationsButton.setOnClickListener(v -> replaceFragment(new NotificationsOrganizer()));
        binding.editEventButton.setOnClickListener(v -> replaceFragment(EventCreateEdit.newInstance(eventID)));
    }

    private void loadData() {
        String eventName = "";          // Get From Database
        String eventLocation = "";      // Get From Database
        String eventDescription = "";   // Get From Database
        String eventDateTime = "";      // Get From Database - Note: probably have to make this from date/time parts
        // Also need to get image here

        binding.eventNameTextView.setText(eventName);
        binding.eventLocationTextView.setText(eventLocation);
        binding.eventDescriptionTextView.setText(eventDescription);
        binding.eventStartEndDateTimeTextView.setText(eventDateTime);
        // Need to bind image here too

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}