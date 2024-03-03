package com.example.cmput301w24t33.organizerFragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.EventChooseQR;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CreateEvent extends Fragment {

    public static CreateEvent editEvent(String param1) {
        CreateEvent fragment = new CreateEvent();
        Bundle args = new Bundle();
        // Add data to bundle here as follows
        // args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_create_event_fragment, container, false);
        setupClickListeners(view);
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.organizer_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupClickListeners(View view){
        setupDateTimeClickListeners(view);

        // Cancel Event Creation
        MaterialButton cancel = view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Confirm Event Creation
        MaterialButton confirm = view.findViewById(R.id.confirm_button);
        confirm.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Upload Event Poster
        MaterialButton uploadPoster = view.findViewById(R.id.upload_poster_button);
//        uploadPoster.setOnClickListener();

        // Generate Event QR code
        MaterialButton generateQR = view.findViewById(R.id.generate_qr_code_button);
        generateQR.setOnClickListener(v -> replaceFragment(new EventChooseQR()));
    }

    private void setupDateTimeClickListeners(View view) {
        TextInputEditText startDate = view.findViewById(R.id.start_date_edit_text);
        TextInputEditText startTime = view.findViewById(R.id.start_time_edit_text);
        TextInputEditText endDate = view.findViewById(R.id.end_date_edit_text);
        TextInputEditText endTime = view.findViewById(R.id.end_time_edit_text);

        // Set click listeners
        startDate.setOnClickListener(v -> showDatePickerDialog(startDate));
        startTime.setOnClickListener(v -> showTimePickerDialog(startTime));
        endDate.setOnClickListener(v -> showDatePickerDialog(endDate));
        endTime.setOnClickListener(v -> showTimePickerDialog(endTime));
    }

    private void showDatePickerDialog(TextInputEditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Format the date as you like
                    String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    editText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(TextInputEditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute1) -> {
                    // Format the time as you like
                    String selectedTime = hourOfDay + ":" + minute1;
                    editText.setText(selectedTime);
                }, hour, minute, DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

}

