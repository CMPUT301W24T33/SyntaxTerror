package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.notifications.Notification;
import com.example.cmput301w24t33.notifications.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying notifications to organizers.
 * Handles UI setup for a custom action bar and a RecyclerView for notifications.
 */
public class NotificationsOrganizer extends Fragment {

    /**
     * Required empty constructor for fragment initialization.
     */
    public NotificationsOrganizer() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of OrganizerNotificationsFragment.
     *
     * @return A new instance of fragment OrganizerNotificationsFragment.
     */
    public static NotificationsOrganizer newInstance() {
        return new NotificationsOrganizer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_notifications_fragment, container, false);
        setupActionBar(view);
        setupNotificationsList(view);
        return view;
    }

    /**
     * Sets up the custom action bar with a title and back button for navigation.
     *
     * @param view The root view of the fragment.
     */
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Notifications");
        ImageView backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    /**
     * Prepares and initializes the RecyclerView for displaying notifications.
     * Note: Actual data binding will depend on future implementation details.
     *
     * @param view The root view of the fragment.
     */
    private void setupNotificationsList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.notifications_organizer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create sample notifications. To be replaced/modified.
        List<Notification> notifications = createSampleNotifications();

        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
    }

    // SAMPLE NOTIFICATIONS - DELETE WHEN ACTUAL NOTIFICATIONS FROM DATABASE ARE IMPLMEMENTED
    private List<Notification> createSampleNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Welcome", "Thanks for joining our event!", "10:00 AM"));
        notifications.add(new Notification("Session Start", "Don't miss the keynote speech.", "11:00 AM"));
        notifications.add(new Notification("Lunch Break", "Lunch is served at the main hall.", "1:00 PM"));
        return notifications;
    }
}