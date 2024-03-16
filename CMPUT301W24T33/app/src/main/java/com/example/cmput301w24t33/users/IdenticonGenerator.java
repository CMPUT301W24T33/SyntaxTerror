package com.example.cmput301w24t33.users;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IdenticonGenerator {

    public static byte[] generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap generateIdenticonBitmap(byte[] hash) {
        final int width = 5, height = 5;
        final int blockSize = 100;
        Bitmap identicon = Bitmap.createBitmap(width * blockSize, height * blockSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(identicon);
        Paint paint = new Paint();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                paint.setColor((hash[x] >> y & 1) == 1 ? Color.WHITE :  Color.BLUE);
                canvas.drawRect(x * blockSize, y * blockSize, (x + 1) * blockSize, (y + 1) * blockSize, paint);
            }
        }

        return identicon;
    }
}
