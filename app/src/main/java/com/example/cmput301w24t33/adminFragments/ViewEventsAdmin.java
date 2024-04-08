// Purpose:
// Provides an interface for administrators to view events and select individual events to open a
// more detailed view.


package com.example.cmput301w24t33.adminFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.adminFragments.DeleteEventAdmin;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment class for administrators to view and manage events.
 * It displays a list of events and allows administrators to select an event for deletion.
 */
public class ViewEventsAdmin extends Fragment {
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private EventViewModel eventViewModel;
    private RecyclerView eventRecyclerView;

    /**
     * Called to have the fragment instantiate its user interface view. This method inflates the layout for the fragment's view, sets up the action bar, click listeners, and initializes the display of events.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_view_events_fragment, container, false);
        eventViewModel = EventViewModel.getInstance();
        setupActionBar(view);
        setupClickListeners(view);
        displayEvents(view);
        return view;
    }

    /**
     * Refreshes the events list from the ViewModel when the fragment resumes, ensuring the data displayed is up-to-date.
     */
    @Override
    public void onResume() {
        super.onResume();
        eventViewModel.loadEvents();
    }

    /**
     * Callback method to be invoked when an event in the RecyclerView is clicked. It navigates to the DeleteEventAdmin fragment for the selected event.
     *
     * @param event The Event object associated with the clicked item.
     * @param position The position of the clicked item in the adapter.
     */
    public void onEventClickListener(Event event, int position) {
        replaceFragment(DeleteEventAdmin.newInstance(event));
    }

    /**
     * Sets up the action bar for the fragment, including setting the title and hiding unnecessary buttons.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("All Events");

        RelativeLayout generalActionBar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        generalActionBar.setBackgroundColor(color);
    }

    /**
     * Sets up click listeners for the fragment, such as the back button.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupClickListeners(View view) {
        ImageButton backButton = view.findViewById(R.id.view_events_back_button);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }


    /**
     * Configures the RecyclerView and initializes the event list and adapter.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void displayEvents(View view) {
        eventRecyclerView = view.findViewById(R.id.event_recyclerview);
        eventList = new ArrayList<>();
        setAdapter();

        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), this::updateUI);
    }

    /**
     * Updates the event adapter with the current list of events.
     *
     * @param events List of events to display.
     */
    private void updateUI(List<Event> events) {
        eventAdapter.setEvents(events);
    }


    /**
     * Initializes the event adapter and sets it on the RecyclerView.
     */

    private void setAdapter() {
        Context context = getContext();
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(eventList, this::onEventClickListener);
        eventRecyclerView.setAdapter(eventAdapter);
    }


    /**
     * Replaces the current fragment with a new fragment for event deletion.
     *
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.admin_events_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
