// The EventAttendees fragment is designed to show a list of attendees for an event and a map view
// indicating a location, providing a visual and interactive component for event organizers within
// an Android application.

package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A Fragment to display event attendees and a map view location.
 */
public class EventAttendees extends Fragment {

    private RecyclerView attendeesRecyclerView;
    private final ArrayList<User> attendeesList = new ArrayList<>();
    private AttendeeAdapter attendeeAdapter;
    private MapView mapView;
    private GoogleMap gMap;

    /**
     * Required empty public constructor
     */
    public EventAttendees() {
    }

    /**
     * Creates a new instance of EventAttendees.
     * @return A new instance of fragment EventAttendees.
     */
    public static EventAttendees newInstance() {
        return new EventAttendees();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_event_attendees_fragment, container, false);
        setupActionBar(view);
        setupClickListeners(view);
        setupAttendeesRecyclerView(view);
        setupMapView(view, savedInstanceState);

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
    }
    // Map view set to uAlberta location
    private void setupMapView(@NonNull View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.attendee_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            gMap = googleMap;
            LatLng uAlberta = new LatLng(53.5232, -113.5263);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uAlberta, 15));
            gMap.addMarker(new MarkerOptions().position(uAlberta).title("University of Alberta"));
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
            LatLng uAlberta = new LatLng(53.5232, -113.5263);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uAlberta, 15));
            gMap.addMarker(new MarkerOptions().position(uAlberta).title("University of Alberta"));
        });
    }

    /**
     * Sets up the action bar with a title and background color.
     * @param view The current view.
     */
    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Attendees");

        RelativeLayout generalActionBar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(getContext(),R.color.organizer_actionbar);
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