// Purpose:
// A fragment designed for attendees to view notifications for an event.
//
// Issues: Populate recyclerview from database for the event

package com.example.cmput301w24t33.attendeeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AttendeeNotificationsFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.notifications.Notification;
import com.example.cmput301w24t33.notifications.NotificationAdapter;
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.example.cmput301w24t33.users.Profile;
import java.util.List;

/**
 * A fragment class that displays notifications to attendees.
 * It includes a list of notifications and provides navigation to view a user's profile.
 */
public class NotificationsAttendee extends Fragment {
    AttendeeNotificationsFragmentBinding binding;
    Event event;
    NotificationAdapter adapter;

    /**
     * Initializes a new instance of the NotificationsAttendee fragment with the specified event details.
     * This static method prepares the fragment for displaying notifications related to the provided event.
     *
     * @param event The event object containing details relevant to the notifications to be displayed.
     * @return A new instance of NotificationsAttendee with event data passed as arguments.
     */
    public static NotificationsAttendee newInstance(Event event) {
        NotificationsAttendee fragment = new NotificationsAttendee();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method inflates the layout for the fragment's view, sets up the action bar, click listeners, and initializes the notifications list display.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AttendeeNotificationsFragmentBinding.inflate(inflater, container, false);
        event = (Event) getArguments().get("event");
        adapter = new NotificationAdapter(null);
        setupActionBar();
        fetchNotifications();
        return binding.getRoot();
    }

    /**
     * Sets up the action bar for the fragment.
     */
    private void setupActionBar() {
        binding.actionBar.generalActionbarTextview.setText("Notifications");
        binding.actionBar.backArrowImg.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    /**
     * Fetches the list of notifications associated with the event from the database and updates the UI.
     * This method calls the NotificationManager to retrieve notifications for the specified event and updates
     * the RecyclerView adapter with the fetched data.
     */
    private void fetchNotifications() {
        binding.notificationsAttendee.setLayoutManager(new LinearLayoutManager(getContext()));
        NotificationManager.getInstance().fetchNotificationsForEvent(event.getEventId(), this::updateAdapter);
    }

    /**
     * Updates the adapter with a new list of notifications and refreshes the RecyclerView to display the latest notifications.
     * This method is typically called after notifications have been fetched from the database.
     *
     * @param notifications The list of notifications to be displayed in the RecyclerView.
     */
    private void updateAdapter(List<Notification> notifications) {
        adapter.addNotifications(notifications);
        binding.notificationsAttendee.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * Replaces the current fragment with a new fragment, facilitating navigation within the app.
     *
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.attendee_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Cleans up the fragment's resources, particularly the binding, to prevent memory leaks.
     * This method is called when the fragment's view is being destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
