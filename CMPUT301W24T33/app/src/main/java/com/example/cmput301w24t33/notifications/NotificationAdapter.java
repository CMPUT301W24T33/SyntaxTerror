// Purpose:
// Serves as a bridge between a RecyclerView and a data set of Notification objects, enabling
// notifications to be displayed in a list.
//
// Issues: None
//

package com.example.cmput301w24t33.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cmput301w24t33.R;
import java.util.List;

/**
 * Adapter class for RecyclerView to display notifications.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> notifications;
    private final OnNotificationListener listener; // Listener can be null

    /**
     * Interface for handling click events on notifications.
     */
    public interface OnNotificationListener {
        void onNotificationClick(int position);
    }

    /**
     * Constructs a NotificationAdapter with a list of notifications and a listener for click events.
     * @param notifications The list of Notification objects to be displayed.
     * @param listener The listener for handling notification click events.
     */
    public NotificationAdapter(List<Notification> notifications, OnNotificationListener listener) {
        this.notifications = notifications;
        this.listener = listener;
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
        holder.notificationTimestamp.setText(notification.getTimestamp());
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
