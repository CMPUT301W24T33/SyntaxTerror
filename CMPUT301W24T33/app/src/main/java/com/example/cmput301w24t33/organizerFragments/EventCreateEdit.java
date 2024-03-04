package com.example.cmput301w24t33.organizerFragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerCreateEditEventFragmentBinding;
import com.example.cmput301w24t33.events.EventChooseQR;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EventCreateEdit extends Fragment {

    private OrganizerCreateEditEventFragmentBinding binding;

    public static EventCreateEdit newInstance(String eventID) {
        EventCreateEdit fragment = new EventCreateEdit();
        Bundle args = new Bundle();
        args.putString("EVENT_ID", eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrganizerCreateEditEventFragmentBinding.inflate(inflater, container, false);
        setupActionButtons();
        setupDateTimePickers();
        if (getArguments() != null) {
            // Used when editing an event
            String eventID = getArguments().getString("EVENT_ID");
            loadData(eventID);
        }
        return binding.getRoot();
    }

    private void setupActionButtons() {
        binding.cancelButton.setOnClickListener(v -> onCancel());
        binding.confirmButton.setOnClickListener(v -> onConfirm());
        binding.uploadPosterButton.setOnClickListener(v -> onUploadPoster());
        binding.generateQrCodeButton.setOnClickListener(v -> onSelectQRCode());
    }

    private void setupDateTimePickers() {
        binding.startDateEditText.setOnClickListener(v -> showDatePickerDialog(true));
        binding.startTimeEditText.setOnClickListener(v -> showTimePickerDialog(true));
        binding.endDateEditText.setOnClickListener(v -> showDatePickerDialog(false));
        binding.endTimeEditText.setOnClickListener(v -> showTimePickerDialog(false));
    }

    private void loadData(String eventID) {
        String eventName = "";          // Get From Database
        String eventLocation = "";      // Get From Database
        String eventDescription = "";   // Get From Database
        String startDate = "";          // Get From Database
        String endDate = "";            // Get From Database
        String startTime = "";          // Get From Database
        String endTime = "";            // Get From Database
        String maxAttendees = "";       // Get From Database
        boolean geoTracking = false;    // Get From Database

        // Load data into relevant field
        binding.eventNameEditText.setText(eventName);
        binding.eventLocationEditText.setText(eventLocation);
        binding.eventDescriptionEditText.setText(eventDescription);
        binding.startDateEditText.setText(startDate);
        binding.endDateEditText.setText(endDate);
        binding.startTimeEditText.setText(startTime);
        binding.endTimeEditText.setText(endTime);
        binding.maxAttendeesEditText.setText(maxAttendees);
        binding.geoTrackingSwitch.setChecked(geoTracking);

        // Removes QR Code button
        binding.generateQrCodeButton.setVisibility(View.GONE);
    }

    private void onConfirm() {
        // Collect data from input fields
        String eventName = Objects.requireNonNull(binding.eventNameEditText.getText()).toString().trim();
        String eventDescription = Objects.requireNonNull(binding.eventDescriptionEditText.getText()).toString().trim();
        String startDate = Objects.requireNonNull(binding.startDateEditText.getText()).toString().trim();
        String startTime = Objects.requireNonNull(binding.startTimeEditText.getText()).toString().trim();
        String endDate = Objects.requireNonNull(binding.endDateEditText.getText()).toString().trim();
        String endTime = Objects.requireNonNull(binding.endTimeEditText.getText()).toString().trim();
        String maxAttendees = Objects.requireNonNull(binding.maxAttendeesEditText.getText()).toString().trim();
        boolean geoLocationTracking = binding.geoTrackingSwitch.isChecked();

        // Validate input
        if (eventName.isEmpty() || eventDescription.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Please fill in all required fields", Snackbar.LENGTH_LONG).show();
            return;
        }

        // DATABASE CODE GOES HERE

        Snackbar.make(binding.getRoot(), "Event Created", Snackbar.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    private void onCancel() {
        // Handle the cancel action
        getParentFragmentManager().popBackStack();
    }

    private void onUploadPoster() {
        // Handle the poster upload
    }

    private void onSelectQRCode() {
        // Handle QR Code selection
        replaceFragment(new EventChooseQR());
    }

    private void showDatePickerDialog(boolean isStart) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
            if (isStart) {
                binding.startDateEditText.setText(selectedDate);
            } else {
                binding.endDateEditText.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog(boolean isStart) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minuteOfHour) -> {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
            if (isStart) {
                binding.startTimeEditText.setText(selectedTime);
            } else {
                binding.endTimeEditText.setText(selectedTime);
            }
        }, hour, minute, DateFormat.is24HourFormat(getContext()));

        timePickerDialog.show();
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