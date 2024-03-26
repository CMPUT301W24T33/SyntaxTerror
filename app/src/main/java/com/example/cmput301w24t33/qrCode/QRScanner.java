// Purpose: (NOT USED)
// facilitates the scanning of QR codes using ML Kit's barcode scanning functionality and stores the
// scanned contents for further processing
//
// Issues: None
//


package com.example.cmput301w24t33.qrCode;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cmput301w24t33.events.Event;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;

/**
 * This scans a QR code and stores its contents
 * */
public class QRScanner {
    private Barcode barcode;
    private ScanResultsListener listener;

    /**
     * Listens to QRScanner and does some action on Scan Result
     */
    public interface ScanResultsListener{
        /**
         * Does some action on Scan Result
         * @param qrCode scanned QR code
         */
        void onScanResult(QRCode qrCode);
    }

    public QRScanner(ScanResultsListener listener){
        this.listener = listener;
    }

    /**
     * Opens camera to scan QR code and stores contents
     * @param context parent context*/
    public void scanQRCode(@NonNull Context context){
        Toast toast = new Toast(context);
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(context);
        scanner
                .startScan()
                .addOnSuccessListener(
                        barcode -> {
                            this.setQRCode(barcode);
                            listener.onScanResult(new QRCode(barcode.getRawValue()));

                            Log.d("SCAN", "Scan Successful");
                        })
                .addOnCanceledListener(
                        () -> {
                            // Task canceled
                            Log.d("SCAN", "Scan canceled");
                        })
                .addOnFailureListener(
                        e -> {
                            // Task failed with an exception
                            Log.d("SCAN","Scan failed, try again");
                        });
    }

    /**
     * Sets QR code to scanned code
     * @param barcode scanned Barcode
     * @see Barcode*/
    private void setQRCode(Barcode barcode){
        this.barcode = barcode;
    }

    /**
     * returns scanned barcode
     * @return scanned Barcode
     * @see Barcode*/
    public Barcode getBarcode(){
        return barcode;
    }
}