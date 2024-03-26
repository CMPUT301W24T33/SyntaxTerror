package com.example.cmput301w24t33.qrCode;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QRCheckIn implements QRScanner.ScanResultsListener {
    private Context context;
    private ArrayList<Event> events;
    private FusedLocationProviderClient fusedLocationProvider;
    private User currentUser;
    private float GEOFENCE_RADIUS = 100;

    @Override
    public void onScanResult(QRCode qrCode) {
        for (Event event : events) {
            Log.d("CheckIn", "Event: " + event.getName());
            if (event.getCheckInQR() != null && event.getCheckInQR().equals(qrCode.getQrCode())) {
                if (!validateCheckIn(event)) { // invalid check in
                    return;
                }
                checkIn(event);
                return;
            }
        }
    }

    /**
     * Constructs new QRCheckIn instance
     *
     * @param context     Activity context
     * @param currentUser User that wants to check into event
     */
    public QRCheckIn(@NonNull Context context, @NonNull User currentUser, ArrayList<Event> events) {
        this.context = context;
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient((Activity) context);
        this.currentUser = currentUser;
        this.events = events;
    }

    /**
     * Checks user into event
     *
     * @param event event to be checked into
     */
    private void checkIn(Event event) {
        // TODO: Determine if event has GeoTracking enabled
        //  1: If GeoTracking is enabled
        //      1.1: nothing needed
        //  2: If GeoTethering is enabled
        //      2.1: prevent user from checking if they are not within some fixed distance of event
        //      2.2: what should this fixed distance be?
        //  3: If GeoTracking is disabled
        //      3.1: Don't store user's location (just set it to null?)
        //  4: Explain to user why location is being asked for
        //      4.1: Note that location is only required if the organizer has geo-tethering on
        Toast checkInFailedToast = new Toast(context);
        checkInFailedToast
                .setText("Check In Failed: Please Try Again");

        Toast locationPermissionsToast = new Toast(context);
        locationPermissionsToast.setText("Please Enable Location Settings And Try Again");

        Toast checkInSuccessfulToast = new Toast(context);
        checkInSuccessfulToast.setText("Successfully Checked In");
        if (event.getGeoTracking()) {
            // gets location permission
            ((Activity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Log.d("Location", "requesting location permission");
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                // Retrieves Current Location

                fusedLocationProvider.getCurrentLocation(new CurrentLocationRequest.Builder().build(), null).addOnSuccessListener((Activity) context, location -> {
                    if (location != null) {
                        // Logic to handle location object
                        event.addAttendee(currentUser, location);
                        EventRepository eventRepo = new EventRepository();
                        eventRepo.updateEvent(event);
                        checkInSuccessfulToast.show();

                    } else {
                        checkInFailedToast.show();
                    }
                }).addOnFailureListener((Activity) context, e -> {
                    Log.d("Location", "Could not retrieve new location: " + e.getMessage());
                    locationPermissionsToast.show();
                });
                Log.d("Location", "Permission Granted");
            } else {
                locationPermissionsToast.show();
            }
        } else { // geoTracking dissabled
            event.addAttendee(currentUser, null);
            EventRepository eventRepo = new EventRepository();
            eventRepo.updateEvent(event);
            checkInSuccessfulToast.show();
        }
    }

    private boolean validateCheckIn(Event event) {
        //Checks for max attendees at event
        if (event.getMaxOccupancy() == event.getAttendees().size() && event.getMaxOccupancy() != 0) { // max occupancy reached
            Log.d("CheckIn", "Max occupancy reached for event: " + event.getName());
            Toast.makeText(context, "Max Occupancy for this event has been reached", Toast.LENGTH_SHORT).show();
            return false; // Stop the check-in process
        }

        if (event.getGeoTracking()) {
            // GeoTracking is enabled, fetch the current location
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true; // what --> come back to this
            }

            fusedLocationProvider.getCurrentLocation(new CurrentLocationRequest.Builder().build(), null)
                    .addOnSuccessListener((Activity) context, location -> {
                        // If location is not null and is near GeofenceArea
                        if (location != null && GeofenceArea(event.getLocationData(), location)) {
                            // User is within geofence radius, proceed with check-in
                            Toast.makeText(context, "Welcome to event!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Location is good!");
                        } else {
                            Toast.makeText(context, "Not close enough to the event location.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((Activity) context, e -> {
                        // Handle failure to get location
                        Toast.makeText(context, "Failed to retrieve location.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // GeoTracking is disabled, proceed without location validation
            return true;
        }
        return true;
    }
    private boolean GeofenceArea(String eventLocationData, Location userLocation) {
        String[] parts = eventLocationData.split(",");
        double eventLatitude = Double.parseDouble(parts[0]);
        double eventLongitude = Double.parseDouble(parts[1]);
        float[] results = new float[1];

        //distanceBetween computes the shortest distance from point to user
        Location.distanceBetween(eventLatitude, eventLongitude, userLocation.getLatitude(), userLocation.getLongitude(), results);
        return results[0] < GEOFENCE_RADIUS;
    }
}




//    private boolean validateCheckIn(Event event){
//        // TODO: Validate user is within check in radius of event
//        if (event.getMaxOccupancy() == event.getAttendees().size() && event.getMaxOccupancy() != 0) { // max occupancy reached
//            Log.d("CheckIn", "Max occupancy reached for event: " +event.getName());
//            Toast.makeText(context,"Max Occupancy for this event has been reached",Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (event.getGeoTracking()) {
//            Log.d("CheckIn", "Not close enough to event: " +event.getName());
////            String[] latLong = event.getLocationData().split(",");
////            int lat = Integer.parseInt(latLong[0]);
////            int lon = Integer.parseInt(latLong[1]);
//            return true; //this needs to check if the user is within range to check into the event
//        }
//        Log.d("CheckIn", "Valid Check In");
//        return true;
//    }
//}