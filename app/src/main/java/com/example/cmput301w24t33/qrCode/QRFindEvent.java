package com.example.cmput301w24t33.qrCode;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.cmput301w24t33.activities.AttendeeActivity;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

public class QRFindEvent implements QRScanner.ScanResultsListener{
    private Context context;
    private ArrayList<Event> events;
    private FusedLocationProviderClient fusedLocationProvider;
    private User currentUser;

    @Override
    public void onScanResult(QRCode qrCode) {
        for(Event event: events){
            if (event.getCheckInQR().equals(qrCode.getQrCode())){
                ((AttendeeActivity)context).onFindEventResult(event);
                return;
            }
        }
    }

    public QRFindEvent(@NonNull Context context, @NonNull ArrayList<Event> events){
        this.context = context;
        this.events = events;
    }
}
