package com.example.cmput301w24t33.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {


    ArrayList<Event> eventsArrayList;
    AdapterEventClickListener eventClickListener;

    public EventAdapter(ArrayList<Event> eventsArrayList, AdapterEventClickListener
                        eventClickListener){

        this.eventsArrayList = eventsArrayList;
        this.eventClickListener = eventClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_event,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = eventsArrayList.get(position);
        holder.eventText.setText(event.getEventText());


        holder.itemView.setOnClickListener(v -> {
            eventClickListener.onEventClickListener(event,position);
        });
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView eventText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventText = itemView.findViewById(R.id.event_text);
        }
    }

}
