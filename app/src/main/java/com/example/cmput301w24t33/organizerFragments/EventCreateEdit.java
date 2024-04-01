// Purpose:
// Manages the creation and editing of events providing the interface for inputting event details
// and handling QR code generation or selection for event check-ins.
//
// Issues:
// Put parameters on input such as max size, cannot be blank etc, end date after start date etc.
//

package com.example.cmput301w24t33.organizerFragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerCreateEditEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.fileUpload.ImageHandler;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Fragment for creating and editing events within the application.
 * This fragment allows users to input event details, choose or generate a QR code for event check-ins,
 * and handles both creating a new event and updating an existing one.
 */
public class EventCreateEdit extends Fragment implements EventChooseQR.ChooseQRFragmentListener {
    private EventRepository eventRepo;
    private Event eventToEdit;
    private OrganizerCreateEditEventFragmentBinding binding;
    FirebaseFirestore db;
    private String qrcode = null;
    private FirebaseStorage storage;
    private String eventImageRef;
    private String eventImageUrl;
    // set to false if entering image select from gallery activity, turns true if upload or exit
    private boolean doneImageUpload = true;
    private Calendar tempStartDateTime = Calendar.getInstance();
    private Calendar tempEndDateTime = Calendar.getInstance();

    private String locationName;
    private String locationCoord;

    private ActivityResultLauncher<Intent> launcher = defineLauncher();

    /**
     * Initializes the activity result launcher for handling the result of the image selection intent.
     * If an image is successfully selected, it converts the image URI to a Bitmap, sets it to the profile image view,
     * and uploads the image to Firebase Storage.
     */
    private ActivityResultLauncher<Intent> defineLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {
                        storage = FirebaseStorage.getInstance();
                        Uri photoUri = result.getData().getData();
                        Log.d("returned url",photoUri.toString());

                        ImageHandler.uploadFile(photoUri, storage, new ImageHandler.UploadCallback() {
                            @Override
                            public void onSuccess(Pair<String, String> result) {
                                // Handle the success case here
                                // For example, store the result.first as the image URL and result.second as the image name
                                Log.d("Upload Success", "URL: " + result.first + ", Name: " + result.second);
                                eventImageRef = result.second;
                                eventImageUrl = result.first;
                                doneImageUpload = true;
                            }
                            @Override
                            public void onFailure(Exception e) {
                                // Handle the failure case here
                                Log.d("Upload Failure", e.toString());
                            }
                        });
                    }
                    // result code shows activity cancelled, happens when back button pressed
                    else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        doneImageUpload = true;
                    }
                }
        );
    }


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


    /**
     * Called when the fragment is first created. Prepares the fragment by initializing
     * the Firestore database instance and handling any arguments passed to the fragment,
     * such as an event ID for editing purposes.
     *
     * @param savedInstanceState If the fragment is re-created from a previous state, this bundle
     *                           contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getContext().getApplicationContext(), getString(R.string.places));
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Configures action buttons, date and time pickers, and loads existing event data if editing an event.
     * This is called to prepare the fragment's user interface.
     *
     * @param inflater The LayoutInflater object to inflate views in the fragment.
     * @param container If non-null, this is the parent view the fragment's UI should attach to.
     * @param savedInstanceState If non-null, the fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrganizerCreateEditEventFragmentBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            // Used when editing an event
            Bundle eventBundle = getArguments();
            eventToEdit = (Event) eventBundle.getSerializable("event");
            loadData();
        }
        setupActionButtons();
        setupDateTimePickers();
        setupPlacesAutocomplete();

        return binding.getRoot();
    }

    /**
     * Cleans up resources associated with the view hierarchy. This is called when the view previously created by onCreateView has been detached.
     */
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
        binding.startTimeText.setOnClickListener(v -> showDatePickerDialog(true));
        binding.endTimeText.setOnClickListener(v -> showDatePickerDialog(false));
    }

    /**
     * Initializes and sets up the Places Autocomplete search fragment.
     * Configures the fragment to display place suggestions as the user types and handles
     * the selection of a place by updating the event's location input field with the selected
     * place's address and storing the latitude and longitude coordinates.
     */
    private void setupPlacesAutocomplete() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    LatLng latLng = place.getLatLng();
                    if (latLng != null) {
                        locationCoord = latLng.latitude + "," + latLng.longitude;
                        locationName = (place.getName() != null && !place.getName().isEmpty()) ? place.getName() :
                                (place.getAddress() != null && !place.getAddress().isEmpty()) ? place.getAddress() :
                                        "Unknown Location";
                    }
                }
                @Override
                public void onError(@NonNull Status status) {
                    Log.i("Places", "An error occurred: " + status);
                }
            });
        }
        if (eventToEdit != null) {
            autocompleteFragment.setHint(eventToEdit.getLocationName());
        }
    }


    /**
     * Loads existing event data into the input fields if an event is being edited.
     */
    private void loadData() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String startDateTime = eventToEdit.getStartDateTime() != null ?
                dateTimeFormat.format(eventToEdit.getStartDateTime().toDate()) : "";
        String endDateTime = eventToEdit.getEndDateTIme() != null ?
                dateTimeFormat.format(eventToEdit.getEndDateTIme().toDate()) : "";

        // Load data into relevant field
        binding.eventNameEditText.setText(eventToEdit.getName());
        binding.eventDescriptionEditText.setText(eventToEdit.getEventDescription());
        binding.startTimeText.setText(startDateTime);
        binding.endTimeText.setText(endDateTime);
        binding.maxAttendeesEditText.setText(String.valueOf(eventToEdit.getMaxOccupancy()));
        binding.geoTrackingSwitch.setChecked(eventToEdit.getGeoTracking());
        eventImageRef = eventToEdit.getImageRef();
        eventImageUrl = eventToEdit.getImageUrl();
        locationCoord = eventToEdit.getLocationCoord();
        locationName = eventToEdit.getLocationName();
        tempStartDateTime.setTime(eventToEdit.getStartDateTime().toDate());
        tempEndDateTime.setTime(eventToEdit.getEndDateTIme().toDate());
        // Removes QR Code button
        binding.generateQrCodeButton.setVisibility(View.INVISIBLE);
        qrcode = eventToEdit.getCheckInQR();
    }

    /**
     * Handles the confirm action, validates input data, and either updates an existing event or creates a new one.
     */
    private void onConfirm() {
        if (doneImageUpload) {
            saveEvent();
        }
        else{
            Snackbar.make(binding.getRoot(), "Uploading Poster", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Saves the event to the database. Updates the event if it's being edited, or creates a new event otherwise.
     */
    private void saveEvent() {
        if (validInput()){
            eventRepo = new EventRepository();
            // Checks if Event is being edited to prevent creating new Event with updated information
            if (eventToEdit != null) {
                // Edits existing event
                setEventEdits(eventToEdit);
                Log.d(TAG, "after set event:" + eventToEdit.getEventId());
                eventRepo.updateEvent(eventToEdit);
                Snackbar.make(binding.getRoot(), "Event Changed", Snackbar.LENGTH_SHORT).show();
            } else {
                // Creates new event
                //mAuth = FirebaseAuth.getInstance();
                String userId = getAndroidId();
                Event newEvent = new Event();
                newEvent.setOrganizerId(userId);
                setEventEdits(newEvent);
                eventRepo.createEvent(newEvent);
                Snackbar.make(binding.getRoot(), "Event Created", Snackbar.LENGTH_SHORT).show();
            }
            getParentFragmentManager().popBackStack();
        }
    }
    /**
     * Checks if user input is valid, produces a snackbar if input is not valid explaining reason
     */
    private boolean validInput() {
        // Event Name editview is empty
        if(binding.eventNameEditText.getText().toString().trim().isEmpty()){
            Snackbar.make(binding.getRoot(), "Event Name cannot be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        // Event description editview is empty
        else if(binding.eventDescriptionEditText.getText().toString().trim().isEmpty()) {
            Snackbar.make(binding.getRoot(), "Event Description cannot be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        // geoTracking is checked but no location is selected
        else if((locationCoord == null || locationName == null) && binding.geoTrackingSwitch.isChecked()){
            Snackbar.make(binding.getRoot(), "Geo-Tracking is on, please choose a location", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        // start time not set
        else if(tempStartDateTime == null){
            Snackbar.make(binding.getRoot(), "Please choose event start time", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(tempEndDateTime == null){
            Snackbar.make(binding.getRoot(), "Please choose event end time", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        Log.d("End time", tempEndDateTime.getTime().toString());
        return true;
    }

    /**
     * Sets or updates the event's details based on user input.
     * @param event The event to be edited or created.
     */
    private void setEventEdits(Event event) {
        event.setName(Objects.requireNonNull(binding.eventNameEditText.getText()).toString().trim());
        event.setLocationName(locationName);
        event.setLocationCoord(locationCoord);
        event.setEventDescription(Objects.requireNonNull(binding.eventDescriptionEditText.getText()).toString().trim());
        event.setStartDateTime(new Timestamp(tempStartDateTime.getTime()));
        event.setEndDateTIme(new Timestamp(tempEndDateTime.getTime()));
        event.setGeoTracking(binding.geoTrackingSwitch.isChecked());
        Log.d("setURL","a"+eventImageUrl);

        // if event already has a poster then delete it from db before updating it
        if (event.getImageUrl() != null){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference fileRef = storage.getReferenceFromUrl(event.getImageUrl());
            fileRef.delete().addOnSuccessListener(aVoid -> {
                // File deleted successfully
                Log.d("FirebaseStorage", "File deleted successfully");

            }).addOnFailureListener(exception -> {
                // Uh-oh, an error occurred!
                Log.d("FirebaseStorage", "Error deleting file", exception);
            });
        }

        event.setImageUrl(eventImageUrl);
        event.setImageRef(eventImageRef);
        event.setMaxOccupancy(Integer.parseInt(Objects.requireNonNull(binding.maxAttendeesEditText.getText()).toString().trim()));


        // when no QR code is being reused
        if (qrcode == null) {
            // create new uuid for qrcode
            qrcode = UUID.randomUUID().toString();
            Log.d("QRCODE", "new QR code: " + qrcode);
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
        doneImageUpload = false;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }

    /**
     * Initiates the QR code selection or generation process.
     */
    private void onSelectQRCode() {
        // Handle QR Code selection
        EventChooseQR chooseQrFragment = new EventChooseQR(getAndroidId());

        // Attaches this Listener to EventChooseQR fragment
        chooseQrFragment.setListener(this);

        replaceFragment(chooseQrFragment);
    }

    /**
     * Displays a date picker dialog for selecting either the event's start or end date.
     *
     * @param isStart A boolean flag indicating whether the start date (true) or the end date (false) is being set.
     */
    private void showDatePickerDialog(boolean isStart) {
        final Calendar initialCalendar = isStart ? tempStartDateTime : tempEndDateTime;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar chosenDate = (Calendar) initialCalendar.clone();
                    chosenDate.set(year, monthOfYear, dayOfMonth, chosenDate.get(Calendar.HOUR_OF_DAY), chosenDate.get(Calendar.MINUTE));
                    if (isStart) {
                        tempStartDateTime.set(year, monthOfYear, dayOfMonth);
                        showTimePickerDialog(true);
                    } else {
                        tempEndDateTime.set(year, monthOfYear, dayOfMonth);
                        showTimePickerDialog(false);
                    }
                },
                initialCalendar.get(Calendar.YEAR),
                initialCalendar.get(Calendar.MONTH),
                initialCalendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(isStart ? System.currentTimeMillis() - 1000 : tempStartDateTime.getTimeInMillis());
        datePickerDialog.show();
    }

    /**
     * Displays a time picker dialog for selecting either the event's start or end time.
     * It validates the selected time to ensure that the start time is not in the past
     * and that the end time is after the start time on the same day.
     * @param isStart A boolean flag indicating whether the start time (true) or the end time (false) is being set.
     */
    private void showTimePickerDialog(boolean isStart) {
        final Calendar now = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    Calendar chosenDateTime = (Calendar) (isStart ? tempStartDateTime.clone() : tempEndDateTime.clone());
                    chosenDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    chosenDateTime.set(Calendar.MINUTE, minuteOfHour);
                    if (isStart) {
                        if (chosenDateTime.before(now)) {
                            Toast.makeText(getContext(), "Start time cannot be in the past.", Toast.LENGTH_LONG).show();
                        } else {
                            tempStartDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            tempStartDateTime.set(Calendar.MINUTE, minuteOfHour);
                        }
                    } else {
                        if (chosenDateTime.before(tempStartDateTime) && isSameDay(chosenDateTime, tempStartDateTime)) {
                            Toast.makeText(getContext(), "End time must be after start time on the same day.", Toast.LENGTH_LONG).show();
                            Calendar fallbackEndTime = (Calendar) tempStartDateTime.clone();
                            fallbackEndTime.add(Calendar.MINUTE, 1);
                            tempEndDateTime.set(Calendar.HOUR_OF_DAY, fallbackEndTime.get(Calendar.HOUR_OF_DAY));
                            tempEndDateTime.set(Calendar.MINUTE, fallbackEndTime.get(Calendar.MINUTE));
                        } else {
                            tempEndDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            tempEndDateTime.set(Calendar.MINUTE, minuteOfHour);
                        }
                    }
                    updateDateTimeUI(isStart);
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getContext())
        );

        timePickerDialog.show();
    }

    /**
     * Checks if two {@link Calendar} instances represent the same day.
     * @param cal1 The first calendar instance to compare.
     * @param cal2 The second calendar instance to compare.
     * @return true if both {@link Calendar} instances represent the same day; false otherwise.
     */
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Updates the UI to display the selected start or end date and time.
     * It formats the date and time into a string and sets the text of the
     * appropriate TextView based on whether the start or end date/time is being updated.
     * @param isStart A boolean flag indicating whether the start date/time (true) or the end date/time (false) is being updated.
     */
    private void updateDateTimeUI(boolean isStart) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        if (isStart) {
            binding.startTimeText.setText(dateTimeFormat.format(tempStartDateTime.getTime()));
        } else {
            binding.endTimeText.setText(dateTimeFormat.format(tempEndDateTime.getTime()));
        }
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


    private String getAndroidId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}