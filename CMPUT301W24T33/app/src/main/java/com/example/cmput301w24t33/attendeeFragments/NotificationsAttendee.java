// Purpose:
// A fragment designed for attendees to view notifications for an event.
//
// Issues: Populate recyclerview from database for the event

package com.example.cmput301w24t33.attendeeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.notifications.Notification;
import com.example.cmput301w24t33.notifications.NotificationAdapter;
import com.example.cmput301w24t33.users.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment class that displays notifications to attendees.
 * It includes a list of notifications and provides navigation to view a user's profile.
 */
public class NotificationsAttendee extends Fragment {

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
        View view = inflater.inflate(R.layout.attendee_notifications_fragment, container, false);
        setupActionBar(view);
        setupClickListeners(view);
        setupNotificationsList(view);
        return view;
    }

    /**
     * Sets up the action bar for the fragment.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Notifications");
    }

    /**
     * Sets up click listeners for UI elements in the fragment, including navigation back and to the user profile.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupClickListeners(@NonNull View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView profileButton = view.findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));
    }

    /**
     * Initializes and displays the list of notifications in a RecyclerView.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupNotificationsList(@NonNull View view) {
//        RecyclerView recyclerView = view.findViewById(R.id.notifications_attendee);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        List<Notification> notifications = createSampleNotifications();
//        NotificationAdapter adapter = new NotificationAdapter(notifications, null);
//        recyclerView.setAdapter(adapter);
    }

    /**
     * Creates a list of sample notifications for demonstration purposes.
     *
     * @return A list of Notification objects.
     */
    @NonNull
    private List<Notification> createSampleNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Welcome", "Thanks for joining our event!", "10:00 AM"));
        notifications.add(new Notification("Session Start", "Don't miss the keynote speech.", "11:00 AM"));
        notifications.add(new Notification("Lunch Break", "Lunch is served at the main hall.", "1:00 PM"));
        return notifications;
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
}
