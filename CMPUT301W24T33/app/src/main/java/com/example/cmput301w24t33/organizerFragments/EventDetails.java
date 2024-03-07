package com.example.cmput301w24t33.organizerFragments;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerEventDetailsFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.qrCode.QRCode;
import com.example.cmput301w24t33.qrCode.ShareQRFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Objects;

public class EventDetails extends Fragment implements ShareQRFragment.ShareQRDialogListener, Serializable {

    private OrganizerEventDetailsFragmentBinding binding;
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();
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
            ShareQRFragment
                    .newInstance(new QRCode(event.getCheckInQR()), new QRCode(event.getEventId()), null,this)
                    .show(getActivity().getSupportFragmentManager(), "Share QR Code");
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

    /**
     * Shares Selected QR code with outside apps
     * @param qrCode desired QR code to be shared
     */
    @Override
    public void ShareSelectedQRCode(QRCode qrCode) {

        // Save image to internal Storage
        //  (see https://stackoverflow.com/questions/56904485/how-to-save-an-image-in-android-q-using-mediastore)
        String path = Environment.DIRECTORY_DCIM + "/images"; // path to images DCIM folder

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "displayName");
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