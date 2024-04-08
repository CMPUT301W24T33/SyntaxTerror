package com.example.cmput301w24t33.fileUpload;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A utility class for handling image operations including uploading to Firebase Storage,
 * updating image references in Firebase Database, and removing images from Firebase.
 */
public class ImageHandler {

    public interface UploadCallback {
        void onSuccess(Pair<String, String> result);
        void onFailure(Exception e);
    }

    /**
     * Uploads a file to the database
     * @param filepath path to file on local device
     * @param storage database that hosts file storage
     * @param callback listener to upload completion
     */
    public static void uploadFile(Uri filepath, FirebaseStorage storage, UploadCallback callback) {
        String generatedString = UUID.randomUUID().toString();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("images/" + generatedString);
        UploadTask uploadTask = fileRef.putFile(filepath);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                callback.onSuccess(new Pair<>(downloadUri.toString(), generatedString));
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    /**
     * updates event picture
     * @param eventID ID of event to update
     * @param imageRef remote image reference
     * @param URL remote image url
     * @param database database hosting image upload
     */
    public static void updateAddEventPicture(String eventID, String imageRef, String URL, FirebaseDatabase database) {
        DatabaseReference eventsRef = database.getReference("events").child(eventID);
        Map<String, Object> updates = new HashMap<>();
        updates.put("eventImage", URL);
        updates.put("imageRef", imageRef);

        eventsRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Event picture updated successfully.");
            } else {
                System.err.println("Failed to update event picture.");
            }
        });
    }

    /**
     * Removes the specified user's picture from Firebase Storage and clears related fields in Firebase Database.
     * This method asynchronously deletes the image from Firebase Storage and updates the 'userImage', 'imageRef',
     * and 'userUpload' fields for a specified user in Firebase Database.
     *
     * @param userID   The ID of the user whose picture is to be removed.
     * @param imageName The name of the image file to remove.
     * @param storage  The {@link FirebaseStorage} instance to use for deletion.
     * @param database The {@link FirebaseDatabase} instance to use for updating.
     */
    public static void removeUserPicture(String userID, String imageName, FirebaseStorage storage, FirebaseDatabase database) {
        // Remove file from Firebase Storage
        StorageReference storageRef = storage.getReference().child("images/" + imageName);
        storageRef.delete().addOnSuccessListener(aVoid -> {
            System.out.println("File deleted successfully from Firebase Storage.");
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });

        // Clear fields in FirebaseDatabase
        DatabaseReference usersRef = database.getReference("users").child(userID);
        Map<String, Object> updates = new HashMap<>();
        updates.put("userImage", null);
        updates.put("imageRef", null);
        updates.put("userUpload", false);

        usersRef.updateChildren(updates);
    }

    /**
     * Removes the specified event's picture from Firebase Storage and clears related fields in Firebase Database.
     * This method asynchronously deletes the image from Firebase Storage and updates the 'eventImage' and 'imageRef'
     * fields for a specified event in Firebase Database.
     *
     * @param eventID   The ID of the event whose picture is to be removed.
     * @param imageName The name of the image file to remove.
     * @param storage  The {@link FirebaseStorage} instance to use for deletion.
     * @param database The {@link FirebaseDatabase} instance to use for updating.
     */
    public static void removeEventPicture(String eventID, String imageName, FirebaseStorage storage, FirebaseDatabase database) {
        // Remove file from Firebase Storage
        StorageReference storageRef = storage.getReference().child("images/" + imageName);
        storageRef.delete().addOnSuccessListener(aVoid -> {
            System.out.println("File deleted successfully from Firebase Storage.");
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });

        // Clear fields in FirebaseDatabase
        DatabaseReference eventsRef = database.getReference("events").child(eventID);
        Map<String, Object> updates = new HashMap<>();
        updates.put("eventImage", null);
        updates.put("imageRef", null);

        eventsRef.updateChildren(updates);
    }
}
