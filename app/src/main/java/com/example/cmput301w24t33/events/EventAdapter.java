// Purpose:
// The EventAdapter class serves as an adapter for managing the display of Event items within a
// RecyclerView, converting Event objects into UI elements and defining interactions with them
// through a click listener.
//
// Issues: decide on other event information that displays in recyclerview
//

package com.example.cmput301w24t33.events;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for managing the display of Event items within a RecyclerView.
 * This class is responsible for converting each Event object into a UI element and defining the interactions with them.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    public static final int DESCRIPTION_LENGTH = 40;
    private final ArrayList<Event> eventsArrayList;
    private final AdapterEventClickListener eventClickListener;

    /**
     * Constructs an adapter for displaying events in a RecyclerView.
     * This adapter is responsible for converting event objects into viewable elements
     * and managing interactions through a defined click listener.
     *
     * @param eventsArrayList The dataset of Event objects to be displayed.
     * @param eventClickListener An interface callback for handling clicks on each event item.
     */
    public EventAdapter(ArrayList<Event> eventsArrayList, AdapterEventClickListener eventClickListener) {
        this.eventsArrayList = eventsArrayList;
        this.eventClickListener = eventClickListener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_event, parent, false);
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
        holder.bind(event, eventClickListener);
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
        TextView eventDetailsText;
        ImageView eventPoster;

        public MyViewHolder(@NonNull View itemView, AdapterEventClickListener listener) {
            super(itemView);
            eventText = itemView.findViewById(R.id.event_text);
            eventDetailsText = itemView.findViewById(R.id.event_description);
            eventPoster = itemView.findViewById(R.id.event_poster_image_view);
        }

        /**
         * Binds an event to the view held by this ViewHolder.
         *
         * @param event The event object to bind to the view.
         * @param listener The listener to handle click events on this view.
         */
        void bind(final Event event, final AdapterEventClickListener listener) {
            Log.d("EventBinding", "Event: " + event.getName());
            if(event.getImageUrl() != null) {
                Glide.with(itemView).load(event.getImageUrl()).into(eventPoster);
                eventPoster.setForeground(null);
                eventPoster.setClipToOutline(true);
            } else {
                Glide.with(itemView).clear(eventPoster);
                eventPoster.setImageResource(R.drawable.no_image);
//                eventPoster.setForeground(ContextCompat.getDrawable(null,R.drawable.no_image_foreground));
            }
            eventText.setText(event.getName());
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEventClickListener(event, position);
                }
            });
            String eventDesc = event.getEventDescription();
            if(eventDesc.contains("\n")) {
                eventDesc = event.getEventDescription().split("\n")[0];
            } if (eventDesc.length() > DESCRIPTION_LENGTH) {
                eventDesc = eventDesc.substring(0,DESCRIPTION_LENGTH) + ". . .";
            }
            eventDetailsText.setText(eventDesc);
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
