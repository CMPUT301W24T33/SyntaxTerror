// This class is designed for managing the creation and editing of events in an Android application,
// providing the interface for inputting event details and handling QR code generation or selection
// for event check-ins.

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerCreateEditEventFragmentBinding;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.organizerFragments.EventChooseQR;
import com.example.cmput301w24t33.events.EventRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Fragment for creating and editing events within the application.
 * This fragment allows users to input event details, choose or generate a QR code for event check-ins,
 * and handles both creating a new event and updating an existing one.
 */
public class EventCreateEdit extends Fragment implements EventChooseQR.ChooseQRFragmentListener {
    private EventRepository eventRepo;
    private Event eventToEdit;
    private OrganizerCreateEditEventFragmentBinding binding;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private String qrcode = null;


    /**
     * Creates a new instance of the EventCreateEdit fragment with an optional event to edit.
     * @param event Event object to be edited, null if creating a new event.
     * @return A new instance of EventCreateEdit.
     */
    public static EventCreateEdit newInstance(Event event) {
        EventCreateEdit fragment = new EventCreateEdit();
        Log.d(TAG, "EventCreateEdit NewInstance");
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Sets up listeners for action buttons including cancel, confirm, upload poster, and select QR code.
     */
    private void setupActionButtons() {
        binding.cancelButton.setOnClickListener(v -> onCancel());
        binding.confirmButton.setOnClickListener(v -> onConfirm());
        binding.uploadPosterButton.setOnClickListener(v -> onUploadPoster());
        binding.generateQrCodeButton.setOnClickListener(v -> onSelectQRCode());
    }

    /**
     * Initializes date and time picker dialogs for event start and end times.
     */
    private void setupDateTimePickers() {
        binding.startDateEditText.setOnClickListener(v -> showDatePickerDialog(true));
        binding.startTimeEditText.setOnClickListener(v -> showTimePickerDialog(true));
        binding.endDateEditText.setOnClickListener(v -> showDatePickerDialog(false));
        binding.endTimeEditText.setOnClickListener(v -> showTimePickerDialog(false));
    }

    /**
     * Loads existing event data into the input fields if an event is being edited.
     */
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

    /**
     * Handles the confirm action, validates input data, and either updates an existing event or creates a new one.
     */
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

    /**
     * Saves the event to the database. Updates the event if it's being edited, or creates a new event otherwise.
     */
    private void saveEvent() {
        eventRepo = new EventRepository();

        // Checks if Event is being edited to prevent creating new Event with updated information
        if (eventToEdit != null) {
            // Edits existing event
            setEventEdits(eventToEdit);
            Log.d(TAG, "after set event:" + eventToEdit.getEventId());
            eventRepo.updateEvent(eventToEdit);
        } else {
            // Creates new event
            //mAuth = FirebaseAuth.getInstance();
            String userId = mAuth.getUid();
            Event newEvent = new Event();
            newEvent.setOrganizerId(userId);
            setEventEdits(newEvent);
            eventRepo.createEvent(newEvent);
        }

    }

    /**
     * Sets or updates the event's details based on user input.
     * @param event The event to be edited or created.
     */
    private void setEventEdits(Event event) {
        event.setName(Objects.requireNonNull(binding.eventNameEditText.getText()).toString().trim());
        event.setAddress(Objects.requireNonNull(binding.eventLocationEditText.getText()).toString().trim());
        event.setEventDescription(Objects.requireNonNull(binding.eventDescriptionEditText.getText()).toString().trim());
        event.setStartDate(Objects.requireNonNull(binding.startDateEditText.getText()).toString().trim());
        event.setStartTime(Objects.requireNonNull(binding.startTimeEditText.getText()).toString().trim());
        event.setEndDate(Objects.requireNonNull(binding.endDateEditText.getText()).toString().trim());
        event.setEndTime(Objects.requireNonNull(binding.endTimeEditText.getText()).toString().trim());
        //event.setMaxOccupancy(Integer.parseInt(Objects.requireNonNull(binding.maxAttendeesEditText.getText()).toString().trim()));


        // when no QR code is being reused
        if (qrcode == null) {
            // reference to new QR code document
            DocumentReference docRef = db.collection("checkInCodes").document();

            // sets organizerId field to organizer Id
            Map<String, String> map = new HashMap<>();
            map.put("organizerId", mAuth.getUid());
            docRef.set(map);

            // sets qrcode value to doc name
            qrcode = docRef.getPath().split("/")[1];
            Log.d("QRCODE", "null qr code");
        }

        event.setCheckInQR(Objects.requireNonNull(qrcode));

    }

    /**
     * Handles the cancel action and returns to the previous fragment.
     */
    private void onCancel() {
        // Handle the cancel action
        getParentFragmentManager().popBackStack();
    }

    /**
     * Placeholder for poster upload functionality.
     */
    private void onUploadPoster() {
        // Handle the poster upload
    }

    /**
     * Initiates the QR code selection or generation process.
     */
    private void onSelectQRCode() {
        // Handle QR Code selection
        EventChooseQR chooseQrFragment = new EventChooseQR();

        // Attaches this Listener to EventChooseQR fragment
        chooseQrFragment.setListener(this);

        replaceFragment(chooseQrFragment);
    }

    /**
     * Displays a date picker dialog for selecting the event's start or end date.
     * @param isStart Indicates whether the date picker is for the start or end date.
     */
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

    /**
     * Displays a time picker dialog for selecting the event's start or end time.
     * @param isStart Indicates whether the time picker is for the start or end time.
     */
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

    /**
     * Replaces the current fragment with the specified fragment.
     * @param fragment The fragment to replace the current one.
     */
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.organizer_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Sets this Listener's qrCode attribute to desired value
     * @param qrCode qrCode to be set
     */
    @Override
    public void setQRCode(String qrCode) {
        this.qrcode = qrCode;
    }
}