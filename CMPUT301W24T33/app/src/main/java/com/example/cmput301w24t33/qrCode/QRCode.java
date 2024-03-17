// Purpose:
// Generates and manages QR codes, allowing for the creation of QR code images from input data.
//
// Issues:
// None
//
// License:
// 	This Source code file uses classes from https://github.com/zxing/zxing
// 	The following is a copy of their licence header and a description of it's use
//	
/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// The above package is used in this source code file primarilly for 
// generating/converting a string to a QR code bitmap

package com.example.cmput301w24t33.qrCode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.Serializable;

/**
 * Represents a QR code and provides Bitmap generation for said code
 * @see Bitmap
 */
public class QRCode implements Serializable {
    public final int WIDTH = 512;
    public final int HEIGHT = 512;
    private String qrCode;
    private Bitmap bmp;
    private BitMatrix bitMatrix;
    private QRCodeWriter writer = new QRCodeWriter();

    /**
     * Constructs a new QR code with a given string to store within
     * @param code desired value to store in QR code
     */
    public QRCode(String code) {
        qrCode = code;
        com.google.zxing.qrcode.encoder.QRCode qrCode = new com.google.zxing.qrcode.encoder.QRCode();
        bmp = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        try {
            bitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
        } catch (WriterException e) {
            Log.e("QRCodeGenerator", "Error generating QR code", e);
        }
        for(int x = 0; x<WIDTH; x++) {
            for(int y = 0; y<HEIGHT; y++) {
                bmp.setPixel(x,y, bitMatrix.get(x,y)? Color.BLACK: Color.WHITE);
            }
        }
    }

    /**
     * Gets the bitmap version of the QR code
     * @return QR code bitmap
     */
    public Bitmap getBitmap() {
        return bmp;
    }

//
//    public void saveAsPNG(String filename){
//        File sdCard = Environment.getExternalStorageDirectory();
//        File dir = new File(sdCard.getAbsoluteFile() + "/Download/" + filename + ".png");
//        try {
//            FileOutputStream fileOut = new FileOutputStream(dir);
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
//            fileOut.flush();
//            fileOut.close();
//        } catch(Exception e) {
//            Log.e("SAVEFILE", "invalid path argument:" + dir.getPath());
//        }
//    }
//
//    public void saveAsJPEG(String filename) {
//        try {
//            File sdCard = Environment.getExternalStorageDirectory();
//            File dir = new File(sdCard.getAbsoluteFile() + "/storage/emulated/0/Download");
//            FileOutputStream fileOut = new FileOutputStream(dir);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
//            fileOut.flush();
//            fileOut.close();
//        } catch(Exception e) {
//            Log.e("SAVEFILE", "invalid path argument:" + filename);
//        }
//    }

    /**
     * Returns stored QR code string value
     * @return QR code string
     */
    public String getQrCode(){
        return qrCode;
    }
}
