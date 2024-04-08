package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AdminDeleteEventFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import java.util.Locale;
import java.text.SimpleDateFormat;

/**
 * Fragment for the Admin to delete existing events
 */
public class DeleteEventAdmin extends Fragment {
    private AdminDeleteEventFragmentBinding binding;
    private Event eventToDelete;
    private EventRepository eventRepo;


    /**
     * Creates a new Instance of DeleteEventAdmin fragment
     * @param event event to be deleted
     * @return new DeleteEventAdmin fragment
     */
    public static DeleteEventAdmin newInstance(Event event) {
        DeleteEventAdmin fragment = new DeleteEventAdmin();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when view is created
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AdminDeleteEventFragmentBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            eventToDelete = (Event) getArguments().getSerializable("event");
            loadData();
        }
        setupActionBar();
        eventRepo = EventRepository.getInstance();
        setupActionButtons();
        return binding.getRoot();
    }

    private void setupActionBar() {
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        binding.actionbar.setBackgroundColor(color);
        binding.actionBarTextview.setText("Delete Event");
    }

    /**
     *
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupActionButtons() {
        binding.cancelButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.deleteButton.setOnClickListener(v -> onDelete());
        binding.backArrowImg.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void onDelete() {
        eventRepo.deleteEvent(eventToDelete); // Assume deleteEvent method requires event ID
        getParentFragmentManager().popBackStack();
    }

    private void loadData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String startDateTimeStr = dateFormat.format(eventToDelete.getStartDateTime().toDate());
        String endDateTimeStr = dateFormat.format(eventToDelete.getEndDateTIme().toDate());

        binding.eventNameTextView.setText(eventToDelete.getName());
        binding.eventLocationTextView.setText(eventToDelete.getLocationName());
        binding.eventDescriptionTextView.setText(eventToDelete.getEventDescription());
        binding.eventStartEndDateTimeTextView.setText(startDateTimeStr + " - " + endDateTimeStr);
        Glide.with(this).load(eventToDelete.getImageUrl()).into(binding.eventPosterImageView);

    }
}
