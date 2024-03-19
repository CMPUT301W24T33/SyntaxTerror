// Purpose:
// Provides an interface for administrators to view and delete events.
//
// Issues:
// Event deletion logic is not implemented in the onDelete method
//

package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AdminDeleteEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;

/**
 * A fragment for administrators to delete events. Provides UI for confirming deletion
 * and performs the deletion process.
 */
public class DeleteEventAdmin extends Fragment {
    private EventRepository eventRepo;
    private AdminDeleteEventFragmentBinding binding;
    private Event eventToDelete;

    /**
     * Factory method to create a new instance of DeleteEventAdmin fragment
     * using the provided parameters.
     *
     * @param event The event to delete.
     * @return A new instance of DeleteEventAdmin.
     */
    public static DeleteEventAdmin newInstance(Event event) {
        DeleteEventAdmin fragment = new DeleteEventAdmin();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * Initializes the fragment's binding with the layout inflater and sets up the UI components.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return A View corresponding to the fragment UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AdminDeleteEventFragmentBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            eventToDelete = (Event) getArguments().getSerializable("event");
            loadData();
        }
        setupActionBar();
        eventRepo = new EventRepository();
        setupActionButtons();
        return binding.getRoot();
    }

    private void setupActionBar() {
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        binding.editEventActionbar.setBackgroundColor(color);
        binding.backActionbarTextview.setText("Delete Event");
    }

    /**
     * Called when the view previously created by onCreateView(LayoutInflater, ViewGroup, Bundle) has
     * been detached from the fragment. The binding is nullified here to avoid memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks by nullifying the reference to the binding
    }

    /**
     * Initializes the action buttons with their respective click listeners.
     */
    private void setupActionButtons() {
        // Sets up listeners for both cancel and delete buttons
        binding.cancelButton.setOnClickListener(v -> onCancel());
        binding.deleteButton.setOnClickListener(v -> onDelete());
        binding.backArrowImg.setOnClickListener(v -> onCancel());
    }

    /**
     * Handles the action to cancel the deletion, typically by popping the current fragment
     * off the stack to return to the previous screen.
     */
    private void onCancel() {
        // Cancel the deletion and return to the previous screen
        getParentFragmentManager().popBackStack();
    }

    /**
     * Handles the deletion action. Implement the actual deletion logic here.
     */
    private void onDelete() {
        // Implement event deletion logic here
        eventRepo.deleteEvent(eventToDelete);
        getParentFragmentManager().popBackStack(); // Placeholder action
    }

    /**
     * Loads event data into the UI, allowing the user to review the event details
     * before confirming deletion.
     */
    private void loadData() {
        // Populates the text fields with the event details for review
        binding.eventNameEditText.setText(eventToDelete.getName());
        binding.eventLocationEditText.setText(eventToDelete.getAddress());
        binding.eventDescriptionEditText.setText(eventToDelete.getEventDescription());
        binding.startDateEditText.setText(eventToDelete.getStartDate());
        binding.endDateEditText.setText(eventToDelete.getEndDate());
        binding.startTimeEditText.setText(eventToDelete.getStartTime());
        binding.endTimeEditText.setText(eventToDelete.getEndTime());
        binding.maxAttendeesEditText.setText(String.valueOf(eventToDelete.getMaxOccupancy()));
    }
}
