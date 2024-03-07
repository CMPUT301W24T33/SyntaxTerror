// dedicated to generating QR codes from strings and converting these QR codes into bitmap images
// for use within applications, facilitating a variety of QR code-based functionalities.

package com.example.cmput301w24t33.qrCode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * Provides functionality for generating QR codes and converting them into bitmap images.
 */
public class QRGenerator {
    QRCodeWriter writer = new QRCodeWriter();

    /**
     * Generates a QR code from the given string.
     *
     * @param args The string to encode in the QR code.
     * @param width The width of the QR code in pixels.
     * @param height The height of the QR code in pixels.
     * @return A QRCode object representing the generated QR code.
     */
    public QRCode generate(String args, int width, int height) {
        QRCode qrCode = new QRCode();
        BitMatrix bitMatrix;
        ByteMatrix byteMatrix = new ByteMatrix(width, height);
        try {
            bitMatrix = writer.encode(args, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            Log.e("QRCodeGenerator", "Error generating QR code", e);
            return null;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                byteMatrix.set(x, y, bitMatrix.get(x, y) ? (byte) 1 : (byte) 0);
            }
        }

        qrCode.setMatrix(byteMatrix);
        return qrCode;
    }

    /**
     * Converts a QRCode object into a bitmap image.
     *
     * @param qrCode The QRCode object to convert into a bitmap.
     * @return A Bitmap object representing the QR code.
     */
    public Bitmap bitmap(QRCode qrCode) {
        ByteMatrix byteMatrix = qrCode.getMatrix();
        int width = byteMatrix.getWidth();
        int height = byteMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, byteMatrix.get(x, y) == 0 ? Color.WHITE : Color.BLACK);
            }
        }
        return bmp;
    }
}
