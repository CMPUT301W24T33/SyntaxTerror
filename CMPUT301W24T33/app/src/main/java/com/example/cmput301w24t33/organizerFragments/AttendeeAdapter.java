// Purpose:
// Facilitates the display of attendees in a list using a RecyclerView to efficiently manage and p
// resent a dynamic list of User objects representing attendees.
//
// Issues:
//

package com.example.cmput301w24t33.organizerFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.User;

import java.util.ArrayList;

/**
 * Adapter for RecyclerView to display a list of attendees.
 */
public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.MyViewHolder> {

    private final ArrayList<User> attendeesList;

    /**
     * Constructs an AttendeeAdapter with a list of User objects representing attendees.
     * @param attendeesList The list of attendees to be displayed.
     */
    public AttendeeAdapter(ArrayList<User> attendeesList) {
        this.attendeesList = attendeesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendees, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = attendeesList.get(position);
        holder.firstNameText.setText(user.getFirstName());
        holder.lastNameText.setText(user.getLastName());
    }

    @Override
    public int getItemCount() {
        return attendeesList.size();
    }

    /**
     * ViewHolder class for attendee items in the RecyclerView.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView firstNameText;
        public TextView lastNameText;

        /**
         * Constructs a ViewHolder for an attendee item.
         * @param itemView The view of the attendee item.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstNameText = itemView.findViewById(R.id.firstNameText);
            lastNameText = itemView.findViewById(R.id.lastNameText);
        }
    }
}
