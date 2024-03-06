package com.example.cmput301w24t33.organizerFragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerCreateEditEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventChooseQR;
import com.example.cmput301w24t33.events.EventRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EventCreateEdit extends Fragment {
    private EventRepository eventRepo;
    private Event eventToEdit;
    private OrganizerCreateEditEventFragmentBinding binding;
    private FirebaseAuth mAuth;

    public static EventCreateEdit newInstance(Event event) {
        EventCreateEdit fragment = new EventCreateEdit();
        Log.d(TAG, "EventCreateEdit NewInstance");
        Bundle args = new Bundle();
        args.putSerializable("event", event);
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
            Bundle eventBundle = getArguments();
            eventToEdit = (Event) eventBundle.getSerializable("event");
            loadData();
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

    private void loadData() {
        // Load data into relevant field
        binding.eventNameEditText.setText(eventToEdit.getName());
        binding.eventLocationEditText.setText(eventToEdit.getAddress());
        binding.eventDescriptionEditText.setText(eventToEdit.getEventDescription());
        binding.startDateEditText.setText(eventToEdit.getStartDate());
        binding.endDateEditText.setText(eventToEdit.getEndDate());
        binding.startTimeEditText.setText(eventToEdit.getStartTime());
        binding.endTimeEditText.setText(eventToEdit.getEndTime());
        binding.maxAttendeesEditText.setText(String.valueOf(eventToEdit.getMaxOccupancy()));
        binding.geoTrackingSwitch.setChecked(eventToEdit.getGeoTracking());

        // Removes QR Code button
        binding.generateQrCodeButton.setVisibility(View.INVISIBLE);
    }

    private void onConfirm() {
        // Collect data from input fields
        /*
        String eventName = Objects.requireNonNull(binding.eventNameEditText.getText()).toString().trim();
        String eventDescription = Objects.requireNonNull(binding.eventDescriptionEditText.getText()).toString().trim();
        String startDate = Objects.requireNonNull(binding.startDateEditText.getText()).toString().trim();
        String startTime = Objects.requireNonNull(binding.startTimeEditText.getText()).toString().trim();
        String endDate = Objects.requireNonNull(binding.endDateEditText.getText()).toString().trim();
        String endTime = Objects.requireNonNull(binding.endTimeEditText.getText()).toString().trim();
        String maxAttendees = Objects.requireNonNull(binding.maxAttendeesEditText.getText()).toString().trim();
        boolean geoLocationTracking = binding.geoTrackingSwitch.isChecked();
         */

 /*
        // Validate input
        if (eventName.isEmpty() || eventDescription.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Please fill in all required fields", Snackbar.LENGTH_LONG).show();
            return;
        }
 */

        // DATABASE CODE GOES HERE
        saveEvent();

        Snackbar.make(binding.getRoot(), "Event Created", Snackbar.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    private void saveEvent() {
        eventRepo = new EventRepository();

        // Checks if Event is being edited to prevent creating new Event with updated information
        if (eventToEdit != null) {
            // Edits existing event
            setEventEdits(eventToEdit);
            eventRepo.updateEvent(eventToEdit);
        } else {
            // Creates new event
            mAuth = FirebaseAuth.getInstance();
            String userId = mAuth.getUid();
            Event newEvent = new Event();
            newEvent.setOrganizerId(userId);
            setEventEdits(newEvent);
            eventRepo.createEvent(newEvent);
        }

    }
    private void setEventEdits(Event event) {
        event.setName(Objects.requireNonNull(binding.eventNameEditText.getText()).toString().trim());
        event.setAddress(Objects.requireNonNull(binding.eventLocationEditText.getText()).toString().trim());
        event.setEventDescription(Objects.requireNonNull(binding.eventDescriptionEditText.getText()).toString().trim());
        event.setStartDate(Objects.requireNonNull(binding.startDateEditText.getText()).toString().trim());
        event.setStartTime(Objects.requireNonNull(binding.startTimeEditText.getText()).toString().trim());
        event.setEndDate(Objects.requireNonNull(binding.endDateEditText.getText()).toString().trim());
        event.setEndTime(Objects.requireNonNull(binding.endTimeEditText.getText()).toString().trim());
        event.setMaxOccupancy(Integer.parseInt(Objects.requireNonNull(binding.maxAttendeesEditText.getText()).toString().trim()));
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