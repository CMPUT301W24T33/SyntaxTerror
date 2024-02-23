package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.notifications.Notification;
import com.example.cmput301w24t33.notifications.NotificationAdapter;
import com.example.cmput301w24t33.users.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.Contract;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationsOrganizer extends Fragment implements NotificationAdapter.OnNotificationListener {

    private List<Notification> notifications;
    private NotificationAdapter adapter;
    private int selectedNotificationPosition = -1;

    public NotificationsOrganizer() {
        // Required empty public constructor
    }

    @NonNull
    @Contract(" -> new")
    public static NotificationsOrganizer newInstance() {
        return new NotificationsOrganizer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_notifications_fragment, container, false);
        setupActionBar(view);
        setupNotificationsList(view);
        setupFloatingActionButtons(view);
        setupClickListeners(view);
        return view;
    }

    private void setupFloatingActionButtons(@NonNull View view) {
        FloatingActionButton fabNewNotification = view.findViewById(R.id.button_new_notification);
        fabNewNotification.setOnClickListener(v -> newNotificationDialog());

        FloatingActionButton fabDeleteNotification = view.findViewById(R.id.button_delete_notification);
        fabDeleteNotification.setVisibility(View.GONE); // Initially hide the delete button
        fabDeleteNotification.setOnClickListener(v -> {
            if (selectedNotificationPosition != -1 && selectedNotificationPosition < notifications.size()) {
                notifications.remove(selectedNotificationPosition);
                adapter.notifyItemRemoved(selectedNotificationPosition);
                adapter.notifyItemRangeChanged(selectedNotificationPosition, notifications.size());
                fabDeleteNotification.setVisibility(View.GONE); // Hide the delete button after deletion
                selectedNotificationPosition = -1; // Reset selection
            }
        });
    }

    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Notifications");
    }

    private void setupClickListeners(@NonNull View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView profileButton = view.findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));
    }

    private void setupNotificationsList(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.notifications_organizer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notifications = createSampleNotifications();
        adapter = new NotificationAdapter(notifications, this);
        recyclerView.setAdapter(adapter);
    }

    private void newNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.organizer_notification_createnew_dialog, null);

        EditText titleInput = dialogView.findViewById(R.id.etNotificationTitle);
        EditText messageInput = dialogView.findViewById(R.id.etNotificationMessage);

        builder.setView(dialogView)
                .setTitle("New Notification")
                .setPositiveButton("Create", (dialog, id) -> {
                    String title = titleInput.getText().toString().trim();
                    String message = messageInput.getText().toString().trim();
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    createNotification(title, message, timestamp);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createNotification(String title, String message, String timestamp) {
        Notification newNotification = new Notification(title, message, timestamp);
        notifications.add(newNotification);
        adapter.notifyDataSetChanged();
    }

    @NonNull
    private List<Notification> createSampleNotifications() {
        List<Notification> sampleNotifications = new ArrayList<>();
        sampleNotifications.add(new Notification("Welcome", "Thanks for joining our event!", "10:00 AM"));
        sampleNotifications.add(new Notification("Session Start", "Don't miss the keynote speech.", "11:00 AM"));
        sampleNotifications.add(new Notification("Lunch Break", "Lunch is served at the main hall.", "1:00 PM"));
        return sampleNotifications;
    }

    @Override
    public void onNotificationClick(int position) {
        selectedNotificationPosition = position;
        View view = getView();
        if (view != null) {
            FloatingActionButton fabDeleteNotification = view.findViewById(R.id.button_delete_notification);
            fabDeleteNotification.setVisibility(View.VISIBLE); // Show the delete button when an item is selected
        }
    }

    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.organizer_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
