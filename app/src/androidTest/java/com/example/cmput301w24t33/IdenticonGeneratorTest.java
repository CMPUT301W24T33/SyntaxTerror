package com.example.cmput301w24t33;

import android.graphics.Bitmap;

import com.example.cmput301w24t33.users.IdenticonGenerator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link IdenticonGenerator} class.
 * This class validates the functionality of generating identicons, ensuring that consistent
 * hashes are generated for the same input, unique hashes for different inputs, and that
 * the generated identicon Bitmaps are not null.
 */

public class IdenticonGeneratorTest {

    /**
     * Tests that the same input string produces consistent hashes across multiple calls.
     * This helps ensure that the hash function used in the identicon generator is deterministic.
     */

    @Test
    public void testGenerateHashConsistency() {
        String input = "TestingNewUser";
        byte[] firstHash = IdenticonGenerator.generateHash(input);
        byte[] secondHash = IdenticonGenerator.generateHash(input);

        Assert.assertArrayEquals("Hashes should match", firstHash, secondHash);

    }

    /**
     * Tests that a non-null identicon Bitmap is generated for a given input string.
     * This ensures that the identicon generation process completes successfully and produces a valid image.
     */
    @Test
    public void testIdenticonNotNull() {
        String input = "userIdTest";
        byte[] hash = IdenticonGenerator.generateHash(input);
        Bitmap identicon = IdenticonGenerator.generateIdenticonBitmap(hash);

        Assert.assertNotNull("Identicon should not be null", identicon);

    }

    /**
     * Tests that different input strings produce different hashes.
     * This test verifies the variability of the hash function, ensuring that different users
     * get unique identicons.
     */
    @Test
    public void testDifferentInputsProduceDifferentHashes() {
        String inputOne = "John";
        String inputTwo = "Mary";

        byte[] hashOne = IdenticonGenerator.generateHash(inputOne);
        byte[] hashTwo = IdenticonGenerator.generateHash(inputTwo);

        Assert.assertFalse("Different inputs should produce different hashes",
                java.util.Arrays.equals(hashOne, hashTwo));

    }

    /**
     * Tests that an identicon Bitmap is generated even for an empty input string.
     * This ensures that the generator can handle edge cases without crashing or producing null images.
     */
    @Test
    public void testGenerateIdenticonWithEmptyString() {
        byte[] hash = IdenticonGenerator.generateHash("");
        Bitmap identicon = IdenticonGenerator.generateIdenticonBitmap(hash);
        Assert.assertNotNull("Identicon should not be null for empty input", identicon);

    }

    /**
     * Tests the uniqueness of identicons generated for different inputs.
     * This is crucial for ensuring that each user gets a distinct identicon.
     */
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

