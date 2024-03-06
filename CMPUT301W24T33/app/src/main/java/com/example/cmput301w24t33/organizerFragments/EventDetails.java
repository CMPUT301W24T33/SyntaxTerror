package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerEventDetailsFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.qrCode.QRCode;
import com.example.cmput301w24t33.qrCode.ShareQRFragment;

public class EventDetails extends Fragment {

    private OrganizerEventDetailsFragmentBinding binding;

    public static EventDetails newInstance(Event event) {
        EventDetails fragment = new EventDetails();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = OrganizerEventDetailsFragmentBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        Bundle bundle = getArguments();
        Event event = (Event) bundle.getSerializable("event");
        setupActionButtons(event);
        loadData(event);
        return binding.getRoot();
    }

    private void setupActionButtons(Event event) {
        binding.shareQrCodeButton.setOnClickListener(v -> {});  // ADD METHOD TO SHARE QR CODE
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.checkInsButton.setOnClickListener(v -> replaceFragment(new EventAttendees()));
//        binding.signUpsButton.setOnClickListener(v -> replaceFragment(new EventSignedUp()));      EVENT SIGNED-UP FRAGMENT NOT MADE YET
        binding.notificationsButton.setOnClickListener(v -> replaceFragment(new NotificationsOrganizer()));
        binding.editEventButton.setOnClickListener(v -> replaceFragment(EventCreateEdit.newInstance(event)));
        binding.shareQrCodeButton.setOnClickListener(v -> {
            // TODO: Share QR Code
            //  1: Open Share QR code fragment
            QRCode checkInCode = new QRCode(event.getCheckInQR());
            ShareQRFragment.newInstance(checkInCode, null).show(getActivity().getSupportFragmentManager(), "Share QR Code");
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
        // Need to bind image here still

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.organizer_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}