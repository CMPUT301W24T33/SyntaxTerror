package com.example.cmput301w24t33.qrCode;

import android.content.Context;
import android.widget.Toast;

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

    /**
     * Handles scan result and directs user to event details page (if successful)
     * @param qrCode scanned QR code
     */
    @Override
    public void onScanResult(QRCode qrCode) {
        for(Event event: events){
            if (event.getCheckInQR().equals(qrCode.getQrCode())){
                ((AttendeeActivity) context).onFindEventResult(event);
                return;
            }
        }
        Toast.makeText(context,"Invalid QR Code", Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates new instance of QRFindEvent class
     * @param context calling context
     * @param events list of events
     */
    public QRFindEvent(@NonNull Context context, @NonNull ArrayList<Event> events){
        this.context = context;
        this.events = events;
    }
}
