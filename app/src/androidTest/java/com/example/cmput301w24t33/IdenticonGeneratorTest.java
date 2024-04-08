package com.example.cmput301w24t33;

import android.graphics.Bitmap;

import com.example.cmput301w24t33.users.IdenticonGenerator;

import org.junit.Assert;
import org.junit.Test;

public class IdenticonGeneratorTest {

    @Test
    public void testGenerateHashConsistency() {
        String input = "TestingNewUser";
        byte[] firstHash = IdenticonGenerator.generateHash(input);
        byte[] secondHash = IdenticonGenerator.generateHash(input);

        Assert.assertArrayEquals("Hashes should match", firstHash, secondHash);

    }

    @Test
    public void testIdenticonNotNull() {
        String input = "userIdTest";
        byte[] hash = IdenticonGenerator.generateHash(input);
        Bitmap identicon = IdenticonGenerator.generateIdenticonBitmap(hash);

        Assert.assertNotNull("Identicon should not be null", identicon);

    }

    @Test
    public void testDifferentInputsProduceDifferentHashes() {
        String inputOne = "John";
        String inputTwo = "Mary";

        byte[] hashOne = IdenticonGenerator.generateHash(inputOne);
        byte[] hashTwo = IdenticonGenerator.generateHash(inputTwo);

        Assert.assertFalse("Different inputs should produce different hashes",
                java.util.Arrays.equals(hashOne, hashTwo));

    }

    @Test
    public void testGenerateIdenticonWithEmptyString() {
        byte[] hash = IdenticonGenerator.generateHash("");
        Bitmap identicon = IdenticonGenerator.generateIdenticonBitmap(hash);
        Assert.assertNotNull("Identicon should not be null for empty input", identicon);

    }

    @Test
    public void testIdenticonUniquenessForDifferentInputs() {
        String inputOne = "TestOne";
        String inputTwo = "TestTwo";

        Bitmap identiconOne = IdenticonGenerator.generateIdenticonBitmap(IdenticonGenerator.generateHash(inputOne));
        Bitmap identiconTwo = IdenticonGenerator.generateIdenticonBitmap(IdenticonGenerator.generateHash(inputTwo));

        Assert.assertNotEquals("The two Identicons should not equal",
                identiconOne, identiconTwo);

    }


}

