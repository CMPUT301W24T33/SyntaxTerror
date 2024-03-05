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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EventCreateEdit extends Fragment {
    private EventRepository eventRepo;
    private Event eventToEdit;
    private OrganizerCreateEditEventFragmentBinding binding;

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
        String eventName = "";          // Get From Database
        String eventLocation = "";      // Get From Database
        String eventDescription = "";   // Get From Database
        String startDate = "";          // Get From Database
        String endDate = "";            // Get From Database
        String startTime = "";          // Get From Database
        String endTime = "";            // Get From Database
        String maxAttendees = "";       // Get From Database
        //boolean geoTracking = false;    // Get From Database

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
        binding.generateQrCodeButton.setVisibility(View.GONE);
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
 */


        saveEvent();


        // DATABASE CODE GOES HERE

        Snackbar.make(binding.getRoot(), "Event Created", Snackbar.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    private void saveEvent() {
        /*
        Map<String, Object> eventEdits = new HashMap<>();
        eventEdits.put("name", Objects.requireNonNull(binding.eventNameEditText.getText()).toString().trim());
        eventEdits.put("eventDescription", Objects.requireNonNull(binding.eventDescriptionEditText.getText()).toString().trim());
        eventEdits.put("startDate", Objects.requireNonNull(binding.startDateEditText.getText()).toString().trim());
        eventEdits.put("startTime", Objects.requireNonNull(binding.startTimeEditText.getText()).toString().trim());
        eventEdits.put("endDate", Objects.requireNonNull(binding.endDateEditText.getText()).toString().trim());
        eventEdits.put("endTime", Objects.requireNonNull(binding.endTimeEditText.getText()).toString().trim());
        eventEdits.put("maxOccupancy", Objects.requireNonNull(binding.maxAttendeesEditText.getText()).toString().trim());
         */

        // Checks if Event is being edited to prevent creating new Event with updated information
        if (eventToEdit != null) {
            // Event is an edit
            eventToEdit.setName(Objects.requireNonNull(binding.eventNameEditText.getText()).toString().trim());
            eventToEdit.setEventDescription(Objects.requireNonNull(binding.eventDescriptionEditText.getText()).toString().trim());
            eventToEdit.setStartDate(Objects.requireNonNull(binding.startDateEditText.getText()).toString().trim());
            eventToEdit.setStartTime(Objects.requireNonNull(binding.startTimeEditText.getText()).toString().trim());
            eventToEdit.setEndDate(Objects.requireNonNull(binding.endDateEditText.getText()).toString().trim());
            eventToEdit.setEndTime(Objects.requireNonNull(binding.endTimeEditText.getText()).toString().trim());
            eventToEdit.setMaxOccupancy(Integer.parseInt(Objects.requireNonNull(binding.maxAttendeesEditText.getText()).toString().trim()));
            //eventRepo.updateEvent();
        }

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