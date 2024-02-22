package com.example.cmput301w24t33.attendeeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAttendee extends Fragment {

    public NotificationsAttendee() {
        // Required empty public constructor
    }

    @NonNull
    @Contract(" -> new")
    public static NotificationsAttendee newInstance() {
        return new NotificationsAttendee();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendee_notifications_fragment, container, false);
        setupActionBar(view);
        setupNotificationsList(view);
        return view;
    }

    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Notifications");
        ImageView backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void setupNotificationsList(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.notifications_attendee);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Notification> notifications = createSampleNotifications();
        NotificationAdapter adapter = new NotificationAdapter(notifications, null); // Pass null for listener
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private List<Notification> createSampleNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Welcome", "Thanks for joining our event!", "10:00 AM"));
        notifications.add(new Notification("Session Start", "Don't miss the keynote speech.", "11:00 AM"));
        notifications.add(new Notification("Lunch Break", "Lunch is served at the main hall.", "1:00 PM"));
        return notifications;
    }
}
