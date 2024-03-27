package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AdminDeletePosterFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;


public class DeletePosterAdmin extends Fragment {

    private EventRepository eventRepo;
    private AdminDeletePosterFragmentBinding binding;
    private Event eventToRemovePoster;



    public DeletePosterAdmin() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of DeleteEventAdmin fragment
     * using the provided parameters.
     *
     * @param event The event to remove image from.
     * @return A new instance of DeleteEventAdmin.
     */
    public static DeletePosterAdmin newInstance(Event event) {
        DeletePosterAdmin fragment = new DeletePosterAdmin();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AdminDeletePosterFragmentBinding.inflate(inflater,container,false);
        if (getArguments() != null) {
            eventToRemovePoster = (Event) getArguments().getSerializable("event");
        }
        loadData();
        setupActionBar();
        eventRepo = new EventRepository();
        setupActionButtons();
        return binding.getRoot();
    }

    private void setupActionBar() {
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        binding.editEventActionbar.setBackgroundColor(color);
        binding.backActionbarTextview.setText("Delete Poster");
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
        eventToRemovePoster.setImageRef(null);
        eventToRemovePoster.setImageUrl(null);
        eventRepo.updateEvent(eventToRemovePoster);
        getParentFragmentManager().popBackStack(); // Placeholder action
    }
    /**
     * Loads event data into the UI, allowing the user to review the event details
     * before confirming deletion.
     */
    private void loadData() {
        binding.eventNameEditText.setText(eventToRemovePoster.getName());
        Glide.with(this).load(eventToRemovePoster.getImageUrl()).into(binding.eventPosterImageView);
    }
}