package com.example.cmput301w24t33.qrCode;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Manages event Check-ins
 */
public class QRCheckIn implements QRScanner.ScanResultsListener {
    private Context context;
    private ArrayList<Event> events;
    private FusedLocationProviderClient fusedLocationProvider;
    private EventRepository eventRepo;
    private User currentUser;
    private float GEOFENCE_RADIUS = 1000;

    @Override
    public void onScanResult(QRCode qrCode) {
        for (Event event : events) {
            Log.d("CheckIn", "Event: " + event.getName());
            if (event.getCheckInQR() != null && event.getCheckInQR().equals(qrCode.getQrCode())) {
                if (!validateCheckIn(event)) { // invalid check in
                    Toast.makeText(context, "Cannot Check Into Event",Toast.LENGTH_SHORT).show();
                    return;
                }
                checkIn(event);
                return;
            }
        }
        Toast.makeText(context,"Invalid QR Code",Toast.LENGTH_SHORT).show();
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
        eventRepo = EventRepository.getInstance();
    }

    /**
     * Checks user into event
     *
     * @param event event to be checked into
     */
    private void checkIn(Event event) {
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
                    if (location != null && GeofenceArea(event.getLocationCoord(), location)) {
                        // Logic to handle location object
                        event.addAttendee(currentUser, location);
                        eventRepo.updateEvent(event);
                        checkInSuccessfulToast.show();

                    } else {
                        Toast.makeText(context, "Not close enough to the event location; Check In Failed", Toast.LENGTH_SHORT).show();
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
            eventRepo.updateEvent(event);
            checkInSuccessfulToast.show();
        }
    }

    private boolean validateCheckIn(Event event) {
        //Checks for max attendees at event
        if (event.getMaxOccupancy() == event.getAttendees().size() && event.getMaxOccupancy() >= 0) { // max occupancy reached
            Log.d("CheckIn", "Max occupancy reached for event: " + event.getName());
            Toast.makeText(context, "Max Occupancy for this event has been reached", Toast.LENGTH_SHORT).show();
            return false; // Stop the check-in process
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