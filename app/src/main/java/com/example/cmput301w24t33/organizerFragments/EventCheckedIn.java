// Purpose:
// Shows a list of attendees and how many their are for an event and a map view indicating a location
// of attendees when they check in.


package com.example.cmput301w24t33.organizerFragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A Fragment to display event attendees and a map view location.
 */
public class EventCheckedIn extends Fragment implements EventRepository.EventCallback {

    private RecyclerView attendeesRecyclerView;
    private ArrayList<User> attendeesList = new ArrayList<>();
    private AttendeeAdapter attendeeAdapter;
    private TextView attendeeNumberView;
    private final EventRepository eventRepository = EventRepository.getInstance();
    private String eventId;
    private MapView mapView;
    private Event selectedEvent;
    private GoogleMap gMap;
    private float GEOFENCE_RADIUS = 100;

    /**
     * Required empty public constructor
     */
    public EventCheckedIn() {
    }

    /**
     * Creates a new instance of EventCheckedIn.
     * @return A new instance of fragment EventCheckedIn.
     */
    public static EventCheckedIn newInstance(Event event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        EventCheckedIn frag = new EventCheckedIn();
        frag.setArguments(args);
        return frag;
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method sets up the
     * action bar, click listeners, attendees RecyclerView, and map view. It also initializes
     * the attendees count TextView.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_event_attendees_fragment, container, false);

        if (getArguments() != null) {
            selectedEvent = (Event) getArguments().getSerializable("event");
        }

        //String address = selectedEvent.getLocationName();

        setupActionBar(view);
        setupClickListeners(view);
        setupMapView(view, savedInstanceState);


        attendeeNumberView = view.findViewById(R.id.attendees_count);
        attendeeNumberView.setText("0");

        setupAttendeesRecyclerView(view);
        return view;
    }

    /**
     * Sets up the RecyclerView for displaying attendees.
     * @param view The current view.
     */
    private void setupAttendeesRecyclerView(@NonNull View view) {
        attendeesRecyclerView = view.findViewById(R.id.event_attendees_list);
        attendeesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        attendeeAdapter = new AttendeeAdapter(attendeesList);
        attendeesRecyclerView.setAdapter(attendeeAdapter);

        eventRepository.setEventCallback(this);
        eventRepository.setEventListener(selectedEvent.getEventId());
    }

    /**
     * Initializes and sets up the map view to a specific location.
     * @param view The current view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    private void setupMapView(@NonNull View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.attendee_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            gMap = googleMap;
            if (selectedEvent != null) {
                String locationData = selectedEvent.getLocationCoord();
                // We are parsing that string from Location Data
                if (locationData != null && !locationData.isEmpty()) {
                    String[] parts = locationData.split(",");
                    double latitude = Double.parseDouble(parts[0]);
                    double longitude = Double.parseDouble(parts[1]);

                    //Moving camera to location
                    LatLng eventLocation = new LatLng(latitude, longitude);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15));
                    gMap.addMarker(new MarkerOptions().position(eventLocation).title("Event"));

                    if (selectedEvent.getGeoTracking()== true){
                        addCircle(eventLocation,GEOFENCE_RADIUS);
                    } else if (selectedEvent.getGeoTracking() == false) {
                        gMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)));
                    }

                } else {
                // Default to Edmonton if location is not Specified
                LatLng eventEdmontonDefault = new LatLng(53.5461, -113.4938);
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventEdmontonDefault, 10));
            }


                // Add markers for each check-in location
                for(String checkInPoint : selectedEvent.getCheckInLocations()) {
                    double lat = Double.parseDouble(checkInPoint.split(",")[0]);
                    double lon = Double.parseDouble(checkInPoint.split(",")[1]);
                    gMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)));
                }
            }
        });
    }

    /**
     * Adds a circle to the Google Map centered at the given latitude and longitude.
     * @param latLng The center point of the circle on the map, specified as a LatLng object containing latitude and longitude.
     * @param radius The radius of the circle in meters.
     */
    private void addCircle(LatLng latLng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 0, 128, 128));
        circleOptions.fillColor(Color.argb(64, 0, 128, 128));
        circleOptions.strokeWidth(4);
        gMap.addCircle(circleOptions);
    }


    /**
     * Fills out Recycler view with loaded event's attendees
     * @param events list of events with selected event's event Id (should be only 1)
     */
    @Override
    public void onEventsLoaded(List<Event> events) {
        View view = getView();
        if (view != null) {
            ProgressBar progressBar = getView().findViewById(R.id.attendee_progress_bar);
            Event event = events.get(0);
            attendeesList.clear();
            attendeesList.addAll(event.getAttendees());
            attendeeNumberView.setText(String.format(Locale.CANADA, "%d", getUniqueAttendeeCount()));
            if (event.getMaxOccupancy() > 0) {
                progressBar.setVisibility(View.VISIBLE);
                int progress = (int) (((float) getUniqueAttendeeCount()) / ((float) event.getMaxOccupancy()) * 100); // why?
                progressBar.setProgress(progress);
            } else if (event.getMaxOccupancy() == 0) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(100);
            }
            attendeeAdapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "VIEW IS NUUULLLL");
        }
    }

    /**
     * Handles case when EventRepository failed to load events
     * @param e exception caused by database query
     */
    @Override
    public void onFailure(Exception e){
        e.printStackTrace();
    }

    /**
     * gets the number of unique users checked into this event
     * @return total number of unique attendees
     */
    private int getUniqueAttendeeCount(){
        int count = 0;
        ArrayList<User> temp = new ArrayList<>();
        for (User user : attendeesList){
            if(!temp.contains(user)){
                temp.add(user);
                count++;
            }
        }
        return count;
    }

    /**
     * Sets up the action bar with a title and background color.
     * @param view The current view.
     */
    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Attendees");
    }

    /**
     * Configures click listeners for back navigation and profile viewing.
     * @param view The current view.
     */
    private void setupClickListeners(@NonNull View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (eventRepository != null) {
            eventRepository.removeEventListener();
        }
    }

}