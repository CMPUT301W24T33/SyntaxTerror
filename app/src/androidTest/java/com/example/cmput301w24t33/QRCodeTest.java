package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.graphics.Bitmap;

import com.example.cmput301w24t33.qrCode.QRCode;

import org.junit.Test;

public class QRCodeTest {

    @Test
    public void testQRCodeConstructor() {
        QRCode qrCode = new QRCode("test");
        assertNotNull(qrCode);
    }

    @Test
    public void testGetBitmap() {
        QRCode qrCode = new QRCode("test");
        Bitmap bitmap = qrCode.getBitmap();
        assertNotNull(bitmap);
        assertEquals(512, bitmap.getWidth());
        assertEquals(512, bitmap.getHeight());
    }

    @Test
    public void testGetQrCode() {
        String code = "test";
        QRCode qrCode = new QRCode(code);
        assertEquals(code, qrCode.getQrCode());
    }

}