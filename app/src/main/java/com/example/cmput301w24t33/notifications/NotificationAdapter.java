// Purpose:
// Serves as a bridge between a RecyclerView and a data set of Notification objects, enabling
// notifications to be displayed in a list.


package com.example.cmput301w24t33.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Collections;
import java.util.Comparator;

/**
 * Adapter class for RecyclerView to display notifications.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notifications;
    private final OnNotificationListener listener; // Listener can be null

    /**
     * Interface for handling click events on notifications.
     */
    public interface OnNotificationListener {
        void onNotificationClick(int position);
    }

    /**
     * Constructs a NotificationAdapter with a list of notifications and a listener for click events.
     * @param listener The listener for handling notification click events.
     */
    public NotificationAdapter(OnNotificationListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the adapter's dataset with a new list of notifications and notifies any observers of the item range changed.
     * @param notifications The new list of notifications to be displayed.
     */
    /**
     * Updates the adapter's dataset with a new list of notifications, sorts them from most recent to least recent,
     * and notifies any observers of the item range changed.
     * @param notifications The new list of notifications to be displayed.
     */
    public void addNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        // Sort notifications by their timestamp from most recent to least recent
        Collections.sort(this.notifications, new Comparator<Notification>() {
            @Override
            public int compare(Notification o1, Notification o2) {
                return o2.getTimestamp().compareTo(o1.getTimestamp());
            }
        });
        notifyDataSetChanged();
    }

    /**
     * Removes a notification from the adapter's dataset at the specified position and notifies any observers of the item removed.
     * @param position The position of the notification in the dataset to be removed.
     */
    public void removeNotificationAt(int position) {
        notifications.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notifications.size());
    }

    /**
     * Returns the list of notifications currently held by the adapter.
     * @return The current list of notifications.
     */
    public List<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Inflates the layout for each notification item when needed. This method is called when the RecyclerView needs a new ViewHolder to represent a notification.
     *
     * @param parent The ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view, used for creating different types of views in the same RecyclerView.
     * @return A new instance of ViewHolder that holds the View for each notification item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_notification, parent, false);
        return new ViewHolder(view, listener);
    }

    /**
     * Binds the data from a Notification object to the ViewHolder. This method updates the contents of the ViewHolder to reflect the notification at the given position in the list.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationTitle.setText(notification.getTitle());
        holder.notificationMessage.setText(notification.getMessage());
        holder.notificationTimestamp.setText(convertTime(notification.getTimestamp()));
    }

    /**
     * Converts a Timestamp to a formatted string representing the date and time of the notification.
     * @param timestamp The Timestamp to convert.
     * @return A string representing the formatted date and time.
     */
    // TODO: This class doesnt belong here and should be moved to a more appropriate class
    private String convertTime(Timestamp timestamp) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDate = dateFormat.format(timestamp.toDate());
            return formattedDate;
    }

    /**
     * Returns the total number of items in the data set held by the adapter. This method helps the RecyclerView determine how many items it needs to display.
     *
     * @return The size of the notifications list, representing the total number of notifications in the data set.
     */
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * ViewHolder class to hold and bind views for each notification item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle, notificationMessage, notificationTimestamp;

        /**
         * Constructs a ViewHolder for a notification item.
         * @param itemView The view of the notification item.
         * @param listener The listener for handling clicks on the notification item.
         */
        public ViewHolder(View itemView, OnNotificationListener listener) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationMessage = itemView.findViewById(R.id.notification_message);
            notificationTimestamp = itemView.findViewById(R.id.notification_timestamp);

            // Only set the click listener if 'listener' is not null
            if (listener != null) {
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onNotificationClick(position);
                    }
                });
            }
        }
    }
}
