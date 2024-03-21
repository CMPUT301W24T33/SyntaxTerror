package com.example.cmput301w24t33.organizerFragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
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
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.example.cmput301w24t33.users.Profile;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;


public class NotificationsOrganizer extends Fragment implements NotificationAdapter.OnNotificationListener {

    private OrganizerNotificationsFragmentBinding binding;
    private NotificationAdapter adapter;
    private Event event;
    private int selectedNotificationPosition = -1;

    /**
     * Initializes a new instance of the NotificationsOrganizer fragment with the specified event data.
     * @param event The event for which notifications are being organized.
     * @return Returns a new instance of NotificationsOrganizer with event data bundled.
     */
    public static NotificationsOrganizer newInstance(Event event) {
        NotificationsOrganizer fragment = new NotificationsOrganizer();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes the fragment view, event data, and sets up UI components and event listeners.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrganizerNotificationsFragmentBinding.inflate(inflater, container, false);
        event = (Event) getArguments().get("event");
        adapter = new NotificationAdapter(this);
        setupActionBar();
        fetchNotifications();
        setupNewNotificationButton();
        setupClickListeners();
        return binding.getRoot();
    }

    /**
     * Sets up the button to create new notifications, including visibility management and click listeners.
     */
    private void setupNewNotificationButton() {
        binding.buttonNewNotification.setOnClickListener(v -> newNotificationDialog());
        binding.buttonDeleteNotification.setVisibility(View.GONE);
        binding.buttonDeleteNotification.setOnClickListener(v -> deleteSelectedNotification());
    }

    /**
     * Handles click events on notifications, updating UI components based on the selected notification.
     * @param position The position of the clicked notification in the adapter.
     */
    public void onNotificationClick(int position) {
        selectedNotificationPosition = position;
        binding.buttonDeleteNotification.setVisibility(View.VISIBLE);
    }

    /**
     * Deletes the selected notification from the event and updates the UI accordingly.
     */
    private void deleteSelectedNotification() {
        if (selectedNotificationPosition != -1) {
            String notificationId = adapter.getNotifications().get(selectedNotificationPosition).getId();
            String eventId = event.getEventId();

            NotificationManager.getInstance().deleteNotification(eventId, notificationId, task -> {
                if (task.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        adapter.removeNotificationAt(selectedNotificationPosition);
                        binding.buttonDeleteNotification.setVisibility(View.GONE);
                        selectedNotificationPosition = -1;
                    });
                } else {
                    Log.e(TAG, "Error deleting notification", task.getException());
                }
            });
        }
    }

    /**
     * Configures the action bar with the title and color settings for the notifications screen.
     */
    private void setupActionBar() {
        binding.actionbar.generalActionbarTextview.setText("Notifications");
        int color = ContextCompat.getColor(getContext(), R.color.organizer_actionbar);
        binding.actionbar.generalActionbar.setBackgroundColor(color);
    }

    /**
     * Establishes click listeners for UI components like the back arrow and profile image in the action bar.
     */
    private void setupClickListeners() {
        binding.actionbar.backArrowImg.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.actionbar.profileImage.setOnClickListener(v -> replaceFragment(new Profile()));
    }

    /**
     * Fetches notifications for the current event and updates the adapter with the retrieved data.
     */
    private void fetchNotifications() {
        binding.notificationsOrganizer.setLayoutManager(new LinearLayoutManager(getContext()));
        NotificationManager.getInstance().fetchNotificationsForEvent(event.getEventId(), this::updateAdapter);
    }

    /**
     * Updates the adapter with a list of notifications and refreshes the RecyclerView to display the latest data.
     * @param notifications The list of notifications to be displayed.
     */
    private void updateAdapter(List<Notification> notifications) {
        adapter.addNotifications(notifications);
        binding.notificationsOrganizer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * Displays a dialog allowing the user to input details for creating a new notification.
     */
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

    /**
     * Handles the creation of a new notification by displaying a dialog for input and then creating the notification.
     * @param dialogView The view of the dialog containing the input fields for the notification title and message.
     */
    private void createNotificationFromDialog(View dialogView) {
        String title = ((EditText) dialogView.findViewById(R.id.etNotificationTitle)).getText().toString().trim();
        String message = ((EditText) dialogView.findViewById(R.id.etNotificationMessage)).getText().toString().trim();
        // TODO: GET PROPER TIME FROM TIME CLASS
        createNotification(title, message);
    }

    /**
     * Creates and sends a new notification with the specified title and message for the current event.
     * @param title   The title of the notification to be created.
     * @param message The message body of the notification.
     */
    private void createNotification(String title, String message) {
        Notification notification = new Notification(title, message);
        String eventId = event.getEventId();

        NotificationManager.getInstance().addNotification(eventId, notification, task -> {
            if (task.isSuccessful()) {
                fetchNotifications();
            } else {
                Log.e(TAG, "Error adding notification", task.getException());
            }
        });
    }

    /**
     * Replaces the current fragment with the specified fragment within the parent fragment manager's transaction.
     * @param fragment The fragment to display, replacing the current fragment.
     */
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.organizer_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Cleans up resources and bindings when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
