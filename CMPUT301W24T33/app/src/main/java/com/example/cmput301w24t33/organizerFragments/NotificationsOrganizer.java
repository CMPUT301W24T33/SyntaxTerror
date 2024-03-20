package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerNotificationsFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.notifications.Notification;
import com.example.cmput301w24t33.notifications.NotificationAdapter;
import com.example.cmput301w24t33.users.Profile;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationsOrganizer extends Fragment implements NotificationAdapter.OnNotificationListener {

    private OrganizerNotificationsFragmentBinding binding;
    private List<Notification> notifications;
    private NotificationAdapter adapter;
    private int selectedNotificationPosition = -1;

    public static NotificationsOrganizer newInstance(Event event) {
        NotificationsOrganizer fragment = new NotificationsOrganizer();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrganizerNotificationsFragmentBinding.inflate(inflater, container, false);
        setupActionBar();
        setupNotificationsList();
        setupNewNotificationButton();
        setupClickListeners();
        return binding.getRoot();
    }

    private void setupNewNotificationButton() {
        binding.buttonNewNotification.setOnClickListener(v -> newNotificationDialog());
        binding.buttonDeleteNotification.setVisibility(View.GONE);
        binding.buttonDeleteNotification.setOnClickListener(v -> deleteSelectedNotification());
    }

    private void deleteSelectedNotification() {
        if (selectedNotificationPosition != -1 && selectedNotificationPosition < notifications.size()) {
            notifications.remove(selectedNotificationPosition);
            adapter.notifyItemRemoved(selectedNotificationPosition);
            adapter.notifyItemRangeChanged(selectedNotificationPosition, notifications.size());
            binding.buttonDeleteNotification.setVisibility(View.GONE);
            selectedNotificationPosition = -1;
        }
    }

    private void setupActionBar() {
        binding.actionbar.generalActionbarTextview.setText("Notifications");
        int color = ContextCompat.getColor(getContext(), R.color.organizer_actionbar);
        binding.actionbar.generalActionbarTextview.setBackgroundColor(color);
    }

    private void setupClickListeners() {
        binding.actionbar.backArrowImg.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.actionbar.profileImage.setOnClickListener(v -> replaceFragment(new Profile()));
    }

    private void setupNotificationsList() {
        binding.notificationsOrganizer.setLayoutManager(new LinearLayoutManager(getContext()));
        notifications = createSampleNotifications();
        adapter = new NotificationAdapter(notifications, this);
        binding.notificationsOrganizer.setAdapter(adapter);
    }

    private void newNotificationDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.organizer_notification_createnew_dialog, null);
        builder.setView(dialogView)
                .setTitle("New Notification")
                .setPositiveButton("Create", (dialog, id) -> createNotificationFromDialog(dialogView))
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel())
                .show();
    }

    private void createNotificationFromDialog(View dialogView) {
        String title = ((EditText) dialogView.findViewById(R.id.etNotificationTitle)).getText().toString().trim();
        String message = ((EditText) dialogView.findViewById(R.id.etNotificationMessage)).getText().toString().trim();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        createNotification(title, message, timestamp);
    }

    private void createNotification(String title, String message, String timestamp) {
        Notification newNotification = new Notification(title, message, timestamp);
        notifications.add(newNotification);
        adapter.notifyDataSetChanged();
    }

    private List<Notification> createSampleNotifications() {
        List<Notification> sampleNotifications = new ArrayList<>();
        sampleNotifications.add(new Notification("Welcome", "Thanks for joining our event!", "10:00 AM"));
        sampleNotifications.add(new Notification("Session Start", "Don't miss the keynote speech.", "11:00 AM"));
        sampleNotifications.add(new Notification("Lunch Break", "Lunch is served at the main hall.", "1:00 PM"));
        return sampleNotifications;
    }

    public void onNotificationClick(int position) {
        selectedNotificationPosition = position;
        binding.buttonDeleteNotification.setVisibility(View.VISIBLE);
    }

    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.organizer_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leak
    }
}
