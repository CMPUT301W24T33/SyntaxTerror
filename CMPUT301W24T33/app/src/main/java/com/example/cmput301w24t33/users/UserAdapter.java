package com.example.cmput301w24t33.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.notifications.Notification;
import com.example.cmput301w24t33.notifications.NotificationAdapter;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final List<User> users;
    private final UserAdapter.OnUserListener listener; // Listener can be null

    // Interface for click events, remains unchanged
    public interface OnUserListener {
        void onUserClick(int position);
    }

    // Constructor accepts a nullable listener
    public UserAdapter(List<User> users, UserAdapter.OnUserListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public void setUsers(List<User> newUsers) {
        users.clear();
        users.addAll(newUsers);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_user, parent, false);
        return new UserAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName;

        public ViewHolder(View itemView,UserAdapter.OnUserListener listener) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name_textview);
            lastName = itemView.findViewById(R.id.last_name_textview);

            // Only set the click listener if 'listener' is not null
            if (listener != null) {
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUserClick(position);
                    }
                });
            }
        }
    }
}
