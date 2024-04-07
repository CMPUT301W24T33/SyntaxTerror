package com.example.cmput301w24t33.adminFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.User;

import java.util.List;

/**
 * Adapter class for displaying user profile pictures in a RecyclerView
 */
public class UserImageAdapter extends RecyclerView.Adapter<UserImageAdapter.ViewHolder>{

    private final List<User> users;
    private final UserImageAdapter.OnUserListener listener; // Listener can be null

    /**
     * Interface for handling click events on users.
     */
    public interface OnUserListener {
        /**
         * Handles user click
         * @param position list position index
         */
        void onUserClick(int position);
    }

    /**
     * Constructs a UserImageAdapter with a list of users and a listener for click events.
     *
     * @param users    List of User objects to be displayed.
     * @param listener Listener for handling user click events.
     */
    public UserImageAdapter(List<User> users, UserImageAdapter.OnUserListener listener) {
        this.users = users;
        this.listener = listener;
    }

    /**
     * Updates the list of users displayed by the RecyclerView.
     *
     * @param newUsers List of new User objects to be displayed.
     */
    public void setUsers(List<User> newUsers) {
        users.clear();
        users.addAll(newUsers);
        notifyDataSetChanged();
    }

    /**
     * Called when RecyclerView needs a new {@link UserImageAdapter.ViewHolder} of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public UserImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_admin_view_user_images, parent, false);
        return new UserImageAdapter.ViewHolder(view, listener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull UserImageAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        String userURL = user.getImageUrl();
        if (userURL != null && userURL != "") {
            Glide.with(holder.userImage.getContext()).load(userURL).into(holder.userImage);
        }
        else{
            Glide.with(holder.userImage.getContext()).clear(holder.userImage);
            holder.userImage.setImageResource(R.drawable.no_image);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The size of the users list.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * ViewHolder class for RecyclerView items, representing individual users.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName;
        ImageView userImage;

        /**
         * Constructs a ViewHolder for the UserImageAdapter.
         *
         * @param itemView The View representing the RecyclerView item.
         * @param listener The listener for click events on users.
         */
        public ViewHolder(View itemView, UserImageAdapter.OnUserListener listener) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name_textview);
            lastName = itemView.findViewById(R.id.last_name_textview);
            userImage = itemView.findViewById(R.id.user_image_view);

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

