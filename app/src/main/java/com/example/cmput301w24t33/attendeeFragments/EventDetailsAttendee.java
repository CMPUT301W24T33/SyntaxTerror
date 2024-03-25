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

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AttendeeEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.example.cmput301w24t33.qrCode.QRCode;
import com.example.cmput301w24t33.qrCode.ShareQRFragment;
import com.example.cmput301w24t33.users.User;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A fragment for displaying event details to an attendee. Includes viewing event notifications and sharing QR codes.
 */
public class EventDetailsAttendee extends Fragment implements ShareQRFragment.ShareQRDialogListener, Serializable {
    private AttendeeEventFragmentBinding binding;
    private Event event;
    private User user;

    /**
     * Creates a new instance of the EventDetailsAttendee fragment with specified event and user details.
     * It packages these details into a Bundle for retrieval in onCreateView.
     * @param event The Event object containing details about the event.
     * @param user The User object representing the current user.
     * @return An EventDetailsAttendee instance with event and user data.
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
     * Initializes and returns the fragment's UI view. Sets up data binding, retrieves event and user from arguments,
     * initializes UI components with event data, and sets click listeners for interaction.
     * @param inflater The LayoutInflater object to inflate views in the fragment.
     * @param container The parent view to attach the fragment's UI to.
     * @param savedInstanceState If non-null, this fragment is reconstructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
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
     * Cleans up the resources associated with the fragment, particularly nullifying the view binding to avoid memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Sets click listeners for various UI components, including the notifications button, navigation, and sharing QR code.
     */
    private void setClickListeners() {
        // Click listeners for notifications, navigation, and QR code sharing.
        binding.notificationsButton.setOnClickListener(v -> replaceFragment(NotificationsAttendee.newInstance(event)));
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
     * Updates the fragment UI with the details of the event. This includes setting text views with event information
     * and updating the toggle button group based on the user's attending status.
     */
    private void loadData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String startDateTimeStr = dateFormat.format(event.getStartDateTime().toDate());
        String endDateTimeStr = dateFormat.format(event.getEndDateTIme().toDate());
        String eventDateTime = startDateTimeStr + " - " + endDateTimeStr;

        binding.eventNameTextView.setText(event.getName());
        binding.eventLocationTextView.setText(event.getAddress());
        binding.eventDescriptionTextView.setText(event.getEventDescription());
        binding.eventStartEndDateTimeTextView.setText(eventDateTime);
        if(event.getImageUrl() != null && event.getImageUrl() != ""){
            Glide.with(this).load(event.getImageUrl()).into(binding.eventPosterImageView);
            //Picasso.get().load(event.getImageUrl()).fit().into(binding.eventPosterImageView);   // load poster image
        }
        else{
            binding.eventPosterImageView.setImageResource(R.drawable.ic_event_poster_placeholder); // set image default
        }

        boolean isGoing = event.getSignedUp().contains(user);
        if (isGoing) {
            binding.toggleButtonGroup.check(R.id.goingButton);
        } else {
            binding.toggleButtonGroup.check(R.id.notGoingButton);
        }
    }

    /**
     * Updates the user's sign-up status for the event based on the selected option in the toggle button group.
     * Performs validation before updating to ensure the event's maximum sign-up limit hasn't been reached.
     * @param checkedId The ID of the checked button in the toggle group.
     * @param isChecked The new checked state of the button.
     */
    private void updateSignedUpStatus(int checkedId, boolean isChecked) {
        if (checkedId == R.id.goingButton) {
            if (isChecked) {
                // User has selected "Going"
                if(!validateSignUp()) {
                    Toast.makeText(getContext(),"Error: Event's Max Signup reached", Toast.LENGTH_SHORT).show();
                    return;
                }
                NotificationManager.getInstance().trackEventNotification(event.getEventId());
                event.getSignedUp().add(user);
            }
        } else if (checkedId == R.id.notGoingButton) {
            if (isChecked) {
                // User has selected "Not Going"
                NotificationManager.getInstance().stopTrackingEventNotification(event.getEventId());
                event.getSignedUp().remove(user);
            }
        }
        EventRepository eventRepo = new EventRepository();
        eventRepo.updateEvent(event);
    }

    /**
     * Validates if the user can sign up for the event based on the maximum number of sign-ups allowed.
     * @return true if the user can sign up, false otherwise.
     */
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

    /**
     * Handles sharing the selected QR code. Saves the QR code image to storage and initiates a share intent
     * for sharing the image.
     * @param qrCode The QRCode object containing the QR code to share.
     */
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

