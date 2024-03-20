// Purpose:
// A fragment designed for attendees to view detailed information about an event, allowing them to
// interact by registering their attendance status and accessing notifications.
//
// Issues:
// record attending status, current just a toggle button
// implement a poster from event with a way to view it

package com.example.cmput301w24t33.attendeeFragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AttendeeEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.qrCode.QRCode;
import com.example.cmput301w24t33.qrCode.ShareQRFragment;
import com.example.cmput301w24t33.users.User;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * A fragment for displaying event details to an attendee. Includes viewing event notifications and sharing QR codes.
 */
public class EventDetailsAttendee extends Fragment implements ShareQRFragment.ShareQRDialogListener, Serializable {

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
            ShareQRFragment
                    .newInstance(event,this)
                    .show(getActivity().getSupportFragmentManager(), "Share QR Code");
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

        boolean isGoing = event.getSignedUp().contains(user);
        if (isGoing) {
            binding.toggleButtonGroup.check(R.id.goingButton);
        } else {
            binding.toggleButtonGroup.check(R.id.notGoingButton);
        }

        // TODO: LOAD EVENT IMAGE
    }

    private void updateSignedUpStatus(int checkedId, boolean isChecked) {
        if (checkedId == R.id.goingButton) {
            if (isChecked) {
                // User has selected "Going"
                if(!validateSignUp()) {
                    Toast.makeText(getContext(),"Error: Event's Max Signup reached", Toast.LENGTH_SHORT).show();
                    return;
                }
                event.getSignedUp().add(user);
            }
        } else if (checkedId == R.id.notGoingButton) {
            if (isChecked) {
                // User has selected "Not Going"
                event.getSignedUp().remove(user);
            }
        }
        EventRepository eventRepo = new EventRepository();
        eventRepo.updateEvent(event);
    }

    private boolean validateSignUp(){
        return event.getMaxSignup() != event.getSignedUp().size() || event.getMaxSignup() == 0;
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

    @Override
    public void ShareSelectedQRCode(QRCode qrCode) {
        if(qrCode == null) { //nothing to share
            return;
        }

        // Save image to internal Storage
        //  (see https://stackoverflow.com/questions/56904485/how-to-save-an-image-in-android-q-using-mediastore)
        String path = Environment.DIRECTORY_DCIM + "/images"; // path to images DCIM folder

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, qrCode.getQrCode());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, path);

        ContentResolver resolver = getContext().getContentResolver();
        Uri uri = null;
        try {
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, values);
            OutputStream stream = resolver.openOutputStream(uri);

            // saves QRCode image to internal storage
            qrCode.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (IOException e) {
            if (uri != null) {
                resolver.delete(uri, null, null);
            }
            e.printStackTrace();
        }

        // Handles share activity
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("image/png");

        Intent shareIntent = Intent.createChooser(sendIntent, null);

        // start share activity
        startActivity(shareIntent);
    }
}

