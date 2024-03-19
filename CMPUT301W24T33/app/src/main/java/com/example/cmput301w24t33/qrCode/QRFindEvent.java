package com.example.cmput301w24t33.qrCode;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.activities.Attendee;
import com.example.cmput301w24t33.attendeeFragments.EventDetailsAttendee;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QRFindEvent implements QRScanner.ScanResultsListener{
    private Context context;
    private ArrayList<Event> events;
    private FusedLocationProviderClient fusedLocationProvider;
    private User currentUser;

    @Override
    public void onScanResult(QRCode qrCode) {
        for(Event event: events){
            if (event.getCheckInQR().equals(qrCode.getQrCode())){
                ((Attendee)context).onFindEventResult(event);
                return;
            }
        }
    }

    public QRFindEvent(@NonNull Context context, @NonNull ArrayList<Event> events){
        this.context = context;
        this.events = events;
    }
}
