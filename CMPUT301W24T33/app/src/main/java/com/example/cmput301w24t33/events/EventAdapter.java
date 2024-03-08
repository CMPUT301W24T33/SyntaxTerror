// Purpose:
// The EventAdapter class serves as an adapter for managing the display of Event items within a
// RecyclerView, converting Event objects into UI elements and defining interactions with them
// through a click listener.
//
// Issues:
//

package com.example.cmput301w24t33.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for managing the display of Event items within a RecyclerView.
 * This class is responsible for converting each Event object into a UI element and defining the interactions with them.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private final ArrayList<Event> eventsArrayList;
    private final AdapterEventClickListener eventClickListener;

    /**
     * Constructs an EventAdapter with a specific dataset of events and a click listener.
     *
     * @param eventsArrayList The list of events to be displayed.
     * @param eventClickListener The listener to handle click events on the RecyclerView items.
     */
    public EventAdapter(ArrayList<Event> eventsArrayList, AdapterEventClickListener eventClickListener) {
        this.eventsArrayList = eventsArrayList;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_event, parent, false);
        return new MyViewHolder(view, eventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = eventsArrayList.get(position);
        holder.bind(event, eventClickListener);
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    /**
     * Updates the dataset of the adapter and refreshes the RecyclerView to display the new data.
     *
     * @param newEvents The new list of events to display.
     */
    public void setEvents(List<Event> newEvents) {
        eventsArrayList.clear();
        eventsArrayList.addAll(newEvents);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for event items within the RecyclerView.
     * This class holds the view for each event item and binds the event data to the view.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventText;

        public MyViewHolder(@NonNull View itemView, AdapterEventClickListener listener) {
            super(itemView);
            eventText = itemView.findViewById(R.id.event_text);
        }

        /**
         * Binds an event to the view held by this ViewHolder.
         *
         * @param event The event object to bind to the view.
         * @param listener The listener to handle click events on this view.
         */
        void bind(final Event event, final AdapterEventClickListener listener) {
            eventText.setText(event.getName());
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEventClickListener(event, position);
                }
            });
        }
    }

    /**
     * Interface for handling click events on items within the RecyclerView.
     */
    public interface AdapterEventClickListener {
        /**
         * Callback method to be invoked when an item in the RecyclerView is clicked.
         *
         * @param event The event associated with the clicked item.
         * @param position The position of the clicked item in the dataset.
         */
        void onEventClickListener(Event event, int position);
    }
}
