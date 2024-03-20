// Purpose:
// Shows a list of attendees and how many their are for an event and a map view indicating a location
// of attendees when they check in.
//
// Issues: Have a way to pin sign in locations on map
//

package com.example.cmput301w24t33.organizerFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A Fragment to display event attendees and a map view location.
 */
public class EventAttendees extends Fragment {

    private RecyclerView attendeesRecyclerView;
    private ArrayList<User> attendeesList = new ArrayList<>();
    private AttendeeAdapter attendeeAdapter;
    private TextView attendeeNumberView;
    private FirebaseFirestore db;
    private String eventId;
    private MapView mapView;
    private Event selectedEvent;
    private GoogleMap gMap;
    private float GEOFENCE_RADIUS = 100;

    /**
     * Required empty public constructor
     */
    public EventAttendees() {
    }

    /**
     * Creates a new instance of EventAttendees.
     * @return A new instance of fragment EventAttendees.
     */
    public static EventAttendees newInstance(Event event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        EventAttendees frag = new EventAttendees();
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
//            Log.d(TAG, "Event Passed: " + selectedEvent.getEventId());
        }

        //String address = selectedEvent.getAddress();

        setupActionBar(view);
        setupClickListeners(view);
        setupMapView(view, savedInstanceState);
        setupAttendeesRecyclerView(view);

        attendeeNumberView = view.findViewById(R.id.attendees_count);
        attendeeNumberView.setText("0");

        return view;
    }

    /**
     * Called when the fragment is being created. Initializes the Firestore database instance and retrieves
     * the event ID from the fragment arguments to set up data retrieval for the event attendees.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
//        assert getArguments() != null;
//        eventId = getArguments().get("eventId").toString();
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

        // is this necessary? yes
        DocumentReference eventRef = db.collection("events").document(selectedEvent.getEventId());

        eventRef
                .addSnapshotListener((eventSnapshot, eventError) ->{
                    Log.d("AttendeeSnapshot", "snaped: " + eventRef.getPath());
            if (eventError != null) {
                Log.d("AttendeeSnapshot", eventError.toString());
            } else if (eventSnapshot != null) {
                attendeesList.clear();
                Log.d("AttendeeSnapshot", "not null");
                if (!eventSnapshot.toObject(Event.class).getAttendees().isEmpty()) {

                    for (User user : eventSnapshot.toObject(Event.class).getAttendees()) {
                        attendeesList.add(user);

                    }
                }
                attendeeAdapter.notifyDataSetChanged();
                attendeeNumberView.setText(String.format(Locale.CANADA, "%d", attendeesList.size()));
            }
        });
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
                String locationData = selectedEvent.getLocationData();
                // We are parsing that string from Location Data
                if (locationData != null && !locationData.isEmpty()) {
                    String[] parts = locationData.split(",");
                    double latitude = Double.parseDouble(parts[0]);
                    double longitude = Double.parseDouble(parts[1]);

                    //Moving camera to location
                    LatLng eventLocation = new LatLng(latitude, longitude);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15));
                    gMap.addMarker(new MarkerOptions().position(eventLocation).title("Event"));
                    addCircle(eventLocation,GEOFENCE_RADIUS);

                } else {
                // Default to Edmonton if location is not Specified
                LatLng eventEdmontonDefault = new LatLng(53.5461, -113.4938);
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventEdmontonDefault, 10));
            }


                // Add markers for each check-in location
                for(GeoPoint checkInPoint : selectedEvent.getCheckInLocations()) {
                    gMap.addMarker(new MarkerOptions().position(new LatLng(checkInPoint.getLatitude(),checkInPoint.getLongitude())));
                }
            }
        });
    }

    private void addCircle(LatLng latLng,float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(4);
        gMap.addCircle(circleOptions);

    }

    /**
     * Sets up the action bar with a title and background color.
     * @param view The current view.
     */
    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Attendees");

        RelativeLayout generalActionBar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(getContext(), R.color.organizer_actionbar);
        generalActionBar.setBackgroundColor(color);
    }

    /**
     * Configures click listeners for back navigation and profile viewing.
     * @param view The current view.
     */
    private void setupClickListeners(@NonNull View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView profileButton = view.findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));
    }

    /**
     * Replaces the current fragment with another fragment.
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.organizer_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}