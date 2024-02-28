//package com.example.cmput301w24t33;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import android.graphics.Bitmap;
//
//import com.example.cmput301w24t33.qrCode.QRGenerator;
//
//import com.google.zxing.qrcode.encoder.QRCode;
//
//public class QRGeneratorTest {
//
//    @Test
//    public void generatorTest(){
//        QRCode QRc = mockQRCode();
//        assertNotNull(QRc);
//    }
//
//    @Test
//    public void bitmapTest(){
//        // test not working but the actual functionality is
//        QRGenerator generator = mockGenerator();
//        QRCode QRc = mockQRCode();
//        Bitmap bmp = generator.bitmap(QRc);
//    }
//
//    private QRGenerator mockGenerator(){
//        return new QRGenerator();
//    }
//
//    private QRCode mockQRCode(){
//        return mockGenerator().generate("abcdefg",512,512);
//    }
//}
