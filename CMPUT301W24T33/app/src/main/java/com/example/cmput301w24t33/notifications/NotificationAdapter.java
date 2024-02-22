package com.example.cmput301w24t33.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cmput301w24t33.R;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> notifications;
    private final OnNotificationListener listener; // Listener can be null

    // Interface for click events, remains unchanged
    public interface OnNotificationListener {
        void onNotificationClick(int position);
    }

    // Constructor accepts a nullable listener
    public NotificationAdapter(List<Notification> notifications, OnNotificationListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_notification, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notificationTitle.setText(notification.getTitle());
        holder.notificationMessage.setText(notification.getMessage());
        holder.notificationTimestamp.setText(notification.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle, notificationMessage, notificationTimestamp;

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
