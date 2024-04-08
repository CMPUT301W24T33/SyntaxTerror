// Purpose:
// Facilitates the display of attendees in a list using a RecyclerView to efficiently manage and p
// resent a dynamic list of User objects representing attendees.
//
// Issues: Make the attendees list look better
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Adapter for RecyclerView to display a list of attendees.
 */
public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.MyViewHolder> {

    private final ArrayList<User> attendeesList;
    private boolean countFlag = true;

    /**
     * Constructs an AttendeeAdapter with a list of User objects representing attendees.
     * @param attendeesList The list of attendees to be displayed.
     */
    public AttendeeAdapter(ArrayList<User> attendeesList) {
        this.attendeesList = attendeesList;

    }

    /**
     * Constructs and AttendeeAdapter with a list of User objects representing attendees.
     * @param attendeesList The list of attendees to be displayed
     * @param countFlag a flag determining if the count should be displayed next to the attendee
     */
    public AttendeeAdapter(ArrayList<User> attendeesList, boolean countFlag){
        this.attendeesList = attendeesList;
        this.countFlag = countFlag;
    }

    /**
     * Inflates the layout for each attendee item when the RecyclerView needs a new ViewHolder.
     * This method creates a new ViewHolder instance by inflating the attendee item layout.
     *
     * @param parent The ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view, for handling different types of items in the RecyclerView.
     * @return A new ViewHolder instance that holds the View for the attendee item.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendees, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Binds the attendee data to the ViewHolder for a specific position in the RecyclerView.
     * This method updates the contents of the ViewHolder's itemView to reflect the attendee
     * information at the given position in the dataset.
     *
     * @param holder The ViewHolder which should be updated to represent the attendee data.
     * @param position The position of the attendee within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = attendeesList.get(position);
        holder.firstNameText.setText(user.getFirstName());
        holder.lastNameText.setText(user.getLastName());
        if(countFlag) {
            holder.countText.setText(String.format(Locale.CANADA, "%d", getUserCount(user)));
        }
    }

    /**
     * Returns the total number of attendees in the dataset.
     * This method helps the RecyclerView determine the number of items it needs to display.
     *
     * @return The size of the attendeesList, representing the total number of attendees in the dataset.
     */
    @Override
    public int getItemCount() {
        int count = 0;
        ArrayList<User> temp = new ArrayList<>();
        for(User user: attendeesList){
            if(!temp.contains(user)){
                temp.add(user);
                count++;
            }
        }
        return count;
    }

    private int getUserCount(User user){
        int count = 0;
        for(User listUser: attendeesList){
            if (listUser.equals(user)){
                count++;
            }
        }
        return count;
    }

    /**
     * ViewHolder class for attendee items in the RecyclerView.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView countText;
        TextView firstNameText;
        TextView lastNameText;

        /**
         * Constructs a ViewHolder for an attendee item.
         * @param itemView The view of the attendee item.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstNameText = itemView.findViewById(R.id.firstNameText);
            lastNameText = itemView.findViewById(R.id.lastNameText);
            countText = itemView.findViewById(R.id.check_in_count);
        }
    }
}
