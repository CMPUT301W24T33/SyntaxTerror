package com.example.cmput301w24t33.users;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utility class for generating identicons.
 * Identicons are hash values, used to deterministically generate users image.
 */
public class IdenticonGenerator {
    /**
     * Generates a hash from the input string using the MD5 hashing algorithm.
     * This hash is used for generating the identicon.
     *
     * @param input The input string to hash. This is typically the user's unique identifier.
     * @return A byte array representing the MD5 hash of the input string. Returns null if the hashing algorithm is not found.
     */
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

    /**
     * Generates a bitmap image of an identicon based on the given hash.
     * The identicon is a 5x5 grid where each cell's color is determined by the hash's bits,
     * creating a unique pattern for each input.
     *
     * @param hash The hash used to generate the identicon pattern. This should be generated using {@link #generateHash(String)}.
     * @return A Bitmap object of the generated identicon.
     */

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
