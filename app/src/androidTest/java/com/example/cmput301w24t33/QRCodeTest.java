package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.graphics.Bitmap;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.qrCode.QRCheckIn;
import com.example.cmput301w24t33.qrCode.QRCode;
import com.example.cmput301w24t33.qrCode.QRFindEvent;

import org.junit.Test;

/**
 * This class represents a QR code that can be used in the application for various purposes such as sharing, event check-ins, and more.
 * It encapsulates the data needed to generate and manage a QR code, including its textual representation and bitmap form.
 */
public class QRCodeTest {

    /**
     * Constructs a new QRCode object and checks to see if its not NULL
     *
     */
    @Test
    public void testQRCodeConstructor() {
        QRCode qrCode = new QRCode("test");
        assertNotNull(qrCode);

    }

    /**
     * Returns the bitmap representation of the QR code.
     *
     * @return A Bitmap object representing the QR code.
     */
    @Test
    public void testGetBitmap() {
        QRCode qrCode = new QRCode("test");
        Bitmap bitmap = qrCode.getBitmap();
        assertNotNull(bitmap);
        assertEquals(512, bitmap.getWidth());
        assertEquals(512, bitmap.getHeight());

    }

    /**
     * Returns the data encoded in the QR code.
     *
     * @return The string representation of the QR code's data.
     */
    @Test
    public void testGetQrCode() {
        String code = "test";
        QRCode qrCode = new QRCode(code);

    }

    
}