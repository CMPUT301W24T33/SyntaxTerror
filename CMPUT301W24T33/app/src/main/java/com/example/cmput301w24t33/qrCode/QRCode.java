package com.example.cmput301w24t33.qrCode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import java.io.FileOutputStream;

public class QRCode {
    public final int WIDTH = 512;
    public final int HEIGHT = 512;
    private String qrCode;
    private Bitmap bmp;
    private BitMatrix bitMatrix;
    private QRCodeWriter writer = new QRCodeWriter();
    public QRCode(String args) {
        com.google.zxing.qrcode.encoder.QRCode qrCode = new com.google.zxing.qrcode.encoder.QRCode();
        try {
            bitMatrix = writer.encode(args, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
        } catch (WriterException e) {
            Log.e("QRCodeGenerator", "Error generating QR code", e);
        }
    }

    public Bitmap getBitmap() {
        return bmp;
    }

    public void saveAsPNG(String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
        } catch(Exception e) {
            Log.e("SAVEFILE", "invalid path argument:" + filepath);
        }
    }

    public void saveAsJPEG(String filepath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
        } catch(Exception e) {
            Log.e("SAVEFILE", "invalid path argument:" + filepath);
        }
    }
}
