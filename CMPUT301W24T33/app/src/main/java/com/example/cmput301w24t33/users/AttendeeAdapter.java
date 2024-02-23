package com.example.cmput301w24t33.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;

import java.util.ArrayList;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.MyViewHolder> {

    ArrayList<User> attendeesList; // Renamed to better reflect the usage
    AttendeeClickListener attendeeClickListener; // Renamed interface

    // Renamed interface to be more descriptive of its purpose
    public interface AttendeeClickListener {
        void onAttendeeClick(User attendee, int position);
    }

    // Constructor now explicitly expects an AttendeeClickListener
    public AttendeeAdapter(ArrayList<User> attendeesList, AttendeeClickListener attendeeClickListener) {
        this.attendeesList = attendeesList;
        this.attendeeClickListener = attendeeClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_attendees, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = attendeesList.get(position);
        holder.firstNameText.setText(user.getFirstName());
        holder.lastNameText.setText(user.getLastName());

        // Correct usage of the click listener
        holder.itemView.setOnClickListener(v -> {
            if (attendeeClickListener != null) {
                attendeeClickListener.onAttendeeClick(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendeesList.size();
    }

    // ViewHolder class name remains the same as it still represents a single item view
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstNameText;
        TextView lastNameText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstNameText = itemView.findViewById(R.id.firstNameText);
            lastNameText = itemView.findViewById(R.id.lastNameText);
        }
    }

}
