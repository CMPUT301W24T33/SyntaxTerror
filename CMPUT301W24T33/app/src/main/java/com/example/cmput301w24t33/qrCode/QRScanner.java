package com.example.cmput301w24t33.qrCode;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This scans a QR code and stores its contents*/
public class QRScanner{
    private Barcode barcode;

    /**
     * Opens camera to scan QR code and stores contents
     * @param context parent context*/
    public String scanQRCode(@NonNull Context context){
        Toast toast = new Toast(context);
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(context);
        AtomicReference<String> qrCode = new AtomicReference<>();
        scanner
                .startScan()
                .addOnSuccessListener(
                        barcode -> {
                            // Task completed successfully
                            this.setQRCode(barcode);
//                            toast.setText(this.getBarcode().getRawValue());
//                            toast.show();
                            qrCode.set(this.getBarcode().getRawValue());
                        })
                .addOnCanceledListener(
                        () -> {
                            // Task canceled
                            toast.setText("Scan canceled");
                            toast.show();
                            qrCode.set(null);
                        })
                .addOnFailureListener(
                        e -> {
                            // Task failed with an exception
                            toast.setText("Scan failed, try again");
                            toast.show();
                            qrCode.set(null);
                        });
        return qrCode.get();
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
