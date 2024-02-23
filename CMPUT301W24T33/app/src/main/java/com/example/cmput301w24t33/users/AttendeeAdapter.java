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

    private final ArrayList<User> attendeesList;

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
