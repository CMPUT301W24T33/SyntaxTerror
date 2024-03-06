package com.example.cmput301w24t33.adminFragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.cmput301w24t33.databinding.AdminDeleteEventFragmentBinding;


import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerCreateEditEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.organizerFragments.EventCreateEdit;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteEventAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteEventAdmin extends Fragment {

    private AdminDeleteEventFragmentBinding binding;
    private Event eventToDelete;
    private EventRepository eventRepo;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static DeleteEventAdmin newInstance(Event event) {
        DeleteEventAdmin fragment = new DeleteEventAdmin();
        Log.d(TAG, "DeleteEventAdmin NewInstance");
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AdminDeleteEventFragmentBinding.inflate(inflater, container, false);
        setupActionButtons();
        if (getArguments() != null) {
            // Used when editing an event
            Bundle eventBundle = getArguments();
            eventToDelete = (Event) eventBundle.getSerializable("event");
            loadData();
        }
        return binding.getRoot();
    }

    private void setupActionButtons() {
        binding.cancelButton.setOnClickListener(v -> onCancel());
        binding.deleteButton.setOnClickListener(v -> onDelete());
    }

    private void loadData() {
        // Load data into relevant field
        binding.eventNameEditText.setText(eventToDelete.getName());
        binding.eventLocationEditText.setText(eventToDelete.getAddress());
        binding.eventDescriptionEditText.setText(eventToDelete.getEventDescription());
        binding.startDateEditText.setText(eventToDelete.getStartDate());
        binding.endDateEditText.setText(eventToDelete.getEndDate());
        binding.startTimeEditText.setText(eventToDelete.getStartTime());
        binding.endTimeEditText.setText(eventToDelete.getEndTime());
        binding.maxAttendeesEditText.setText(String.valueOf(eventToDelete.getMaxOccupancy()));
    }

    private void onCancel() {
        // Handle the cancel action
        getParentFragmentManager().popBackStack();
    }
    private void onDelete() {
        // handle on delete here
        getParentFragmentManager().popBackStack();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}