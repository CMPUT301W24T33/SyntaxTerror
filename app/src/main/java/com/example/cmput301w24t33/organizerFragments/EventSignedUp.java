// Purpose:
//
//


package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.databinding.OrganizerEventSignedUpFragmentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment class to display the list of Signed Up users to an event
 */
public class EventSignedUp extends Fragment implements EventRepository.EventCallback {
    private static final String TAG = "butts";
    private OrganizerEventSignedUpFragmentBinding binding;
    private EventRepository eventRepository;
    private AttendeeAdapter adapter;
    private ArrayList<User> signedUpList = new ArrayList<>();
    private String eventId;

    /**
     * Creates a new instance of this class
     * @param event event whose signed up list is to be viewed
     * @return new EventSignedUp instance
     */
    public static EventSignedUp newInstance(Event event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        EventSignedUp frag = new EventSignedUp();
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrganizerEventSignedUpFragmentBinding.inflate(inflater, container, false);
        eventRepository = EventRepository.getInstance();
        eventRepository.setEventCallback(this);
        if (getArguments() != null) {
            Event event = (Event) getArguments().getSerializable("event");
            if (event != null) {
                eventId = event.getEventId();
                eventRepository.setEventListener(eventId);
            }
        }
        setupActionBar();
        setupEventSignUpsRecyclerView();
        return binding.getRoot();
    }

    private void setupEventSignUpsRecyclerView() {
        binding.eventSignupsList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttendeeAdapter(signedUpList, false);
        binding.eventSignupsList.setAdapter(adapter);
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        if (!events.isEmpty()) {
            Event event = events.get(0);
            signedUpList.clear();
            signedUpList.addAll(event.getSignedUp());
            adapter.notifyDataSetChanged();
            if (binding != null) {
                binding.signedupCount.setText(String.valueOf(signedUpList.size()));
            } else {
                Log.e(TAG, "Binding is null. Cannot update UI.");
            }
        }
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    private void setupActionBar() {
        TextView actionBarText = binding.actionbar.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Signed Up");

        ImageButton backButton = binding.actionbar.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventRepository != null) {
            eventRepository.removeAllListeners();
        }
        binding = null;
    }
}