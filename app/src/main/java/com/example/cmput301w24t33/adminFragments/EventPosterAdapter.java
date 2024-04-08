package com.example.cmput301w24t33.adminFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.Event;


import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for displaying Event Posters in a RecyclerView
 */
public class EventPosterAdapter extends RecyclerView.Adapter<EventPosterAdapter.MyViewHolder>{


    private final ArrayList<Event> eventsArrayList;
    private final EventPosterAdapter.AdapterEventClickListener eventClickListener;

    private final Fragment fragment;

    /**
     * Constructs an adapter for displaying events in a RecyclerView.
     * This adapter is responsible for converting event objects into viewable elements
     * and managing interactions through a defined click listener.
     *
     * @param eventsArrayList The dataset of Event objects to be displayed.
     * @param eventClickListener An interface callback for handling clicks on each event item.
     */
    public EventPosterAdapter(ArrayList<Event> eventsArrayList, EventPosterAdapter.AdapterEventClickListener eventClickListener, Fragment fragment) {
        this.eventsArrayList = eventsArrayList;
        this.eventClickListener = eventClickListener;
        this.fragment = fragment;
    }

    /**
     * Inflates the view for each event item within the RecyclerView. This method is called when
     * the RecyclerView needs a new ViewHolder to represent an event item.
     *
     * @param parent The ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view, for different types of items if any.
     * @return A new instance of MyViewHolder that holds the view for the event item.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_event_poster, parent, false);
        return new MyViewHolder(view, eventClickListener);
    }

    /**
     * Binds the data from the event object at a specific position in the dataset to the ViewHolder.
     * This method sets the content of the event item's view to reflect the event data it represents.
     *
     * @param holder The ViewHolder which should be updated to represent the event content.
     * @param position The position of the event item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = eventsArrayList.get(position);
        holder.bind(event, eventClickListener,fragment);
    }

    /**
     * Returns the total number of event items in the dataset. This method helps the RecyclerView
     * determine the number of items it needs to display.
     *
     * @return The size of the eventsArrayList, representing the total number of event items in the dataset.
     */
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
        ImageView eventPoster;

        public MyViewHolder(@NonNull View itemView, AdapterEventClickListener listener) {
            super(itemView);
            eventText = itemView.findViewById(R.id.event_text);
            eventPoster = itemView.findViewById(R.id.event_poster_image_view);
        }

        /**
         * Binds an event to the view held by this ViewHolder.
         *
         * @param event The event object to bind to the view.
         * @param listener The listener to handle click events on this view.
         */
        void bind(final Event event, final AdapterEventClickListener listener,Fragment fragment) {
            eventText.setText(event.getName());
            String posterURL = event.getImageUrl();
            if (posterURL != null) {
                Glide.with(fragment).load(event.getImageUrl()).into(eventPoster);
            }
            else{
                Glide.with(fragment).clear(eventPoster);
                eventPoster.setImageResource(R.drawable.no_image);
            }
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
