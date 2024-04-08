// Purpose:
// Manages a collection of User objects to be displayed in a RecyclerView, handling the binding of
// each user's data to a view within the RecyclerView.
//


package com.example.cmput301w24t33.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cmput301w24t33.R;

import java.util.List;

/**
 * Adapter for a RecyclerView that displays a list of Users. It provides
 * the connection between the data and the RecyclerView displaying the users.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final List<User> users;
    private final UserAdapter.OnUserListener listener; // Listener can be null

    /**
     * Interface for handling click events on users.
     */
    public interface OnUserListener {
        void onUserClick(int position);
    }

    /**
     * Constructs a UserAdapter with a list of users and a listener for click events.
     *
     * @param users    List of User objects to be displayed.
     * @param listener Listener for handling user click events.
     */
    public UserAdapter(List<User> users, UserAdapter.OnUserListener listener) {
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
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_user, parent, false);
        return new UserAdapter.ViewHolder(view, listener);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
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

        /**
         * Constructs a ViewHolder for the UserAdapter.
         *
         * @param itemView The View representing the RecyclerView item.
         * @param listener The listener for click events on users.
         */
        public ViewHolder(View itemView, UserAdapter.OnUserListener listener) {
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
