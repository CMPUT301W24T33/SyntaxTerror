package com.example.cmput301w24t33.qrCode;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

/**
 * This scans a QR code and stores its contents*/
public class QRScanner{
    private Barcode barcode;

    /**
     * */
    public void scanQRCode(@NonNull Context context){
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(context);
        scanner
                .startScan()
                .addOnSuccessListener(
                        barcode -> {
                            // Task completed successfully
                            this.setQRCode(barcode);
                            Toast toast = new Toast(context);
                            toast.setText(this.getBarcode().getRawValue());
                            toast.show();
                        })
                .addOnCanceledListener(
                        () -> {
                            // Task canceled
                        })
                .addOnFailureListener(
                        e -> {
                            // Task failed with an exception
                        });
    }

    private void setQRCode(Barcode barcode){
        this.barcode = barcode;
    }

    public Barcode getBarcode(){
        return barcode;
    }
}