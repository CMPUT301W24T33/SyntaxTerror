package com.example.cmput301w24t33.fileUpload;

import android.net.Uri;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * A utility class for handling image operations including uploading to Firebase Storage,
 * updating image references in Firebase Database, and removing images from Firebase.
 */
public class ImageHandler {
    /**
     * Uploads an image file to Firebase Storage and returns a {@link Pair} containing the download URL
     * and the name of the uploaded image. Note that the method signature suggests a synchronous operation,
     * but Firebase Storage operations are asynchronous. This method serves as an example and should be adapted
     * to fit asynchronous execution patterns in your application.
     *
     * @param filepath The path to the image file to upload.
     * @param storage  The {@link FirebaseStorage} instance to use for uploading.
     * @return A {@link Pair} where the first element is the download URL as a {@link String} and the second
     * element is the name of the uploaded file. This is a placeholder return value and should be replaced
     * with appropriate asynchronous handling mechanisms.
     */
    public static Pair<String, String> uploadFile(String filepath, FirebaseStorage storage) {
        // Extract the name of the file from the filepath
        String fileName = filepath.substring(filepath.lastIndexOf('/') + 1);
        Uri file = Uri.fromFile(new File(filepath));
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("images/" + fileName);
        UploadTask uploadTask = fileRef.putFile(file);

        // Initialize a TaskCompletionSource to manage the return value
        TaskCompletionSource<Pair<String, String>> taskCompletionSource = new TaskCompletionSource<>();
        Task<Pair<String, String>> customTask = taskCompletionSource.getTask();

        // Start the upload and listen for completion
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            // Continue with the task to get the download URL
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                // Use TaskCompletionSource to set the result
                taskCompletionSource.setResult(new Pair<>(downloadUri.toString(), fileName));
            } else {
                taskCompletionSource.setException(task.getException());
            }
        });

        // Blocking on the main thread is discouraged; consider using an asynchronous pattern instead
        // This example assumes you're handling the result asynchronously
        customTask.addOnSuccessListener(pair -> {
            // Handle success - pair contains the URL and file name
            System.out.println("Upload successful. URL: " + pair.first + ", Name: " + pair.second);
        }).addOnFailureListener(e -> {
            // Handle failure
        });

        // Return type needs to be changed to accommodate asynchronous execution
        // For now, this is a placeholder to fit the method signature
        return new Pair<>("", "");
    }

    /**
     * Updates the user's profile with the provided image URL and image name. This method asynchronously
     * updates the 'userImage', 'imageRef', and 'userUpload' fields for a specified user in Firebase Database.
     *
     * @param userID   The ID of the user to update.
     * @param imageName The name of the image file.
     * @param URL      The download URL of the uploaded image.
     * @param database The {@link FirebaseDatabase} instance to use for updating.
     */
    public static void updateAddUserPicture(String userID, String imageName, String URL, FirebaseDatabase database) {
        DatabaseReference usersRef = database.getReference("users").child(userID);
        Map<String, Object> updates = new HashMap<>();
        updates.put("userImage", URL);
        updates.put("imageRef", imageName);
        updates.put("userUpload", true);

        usersRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("User picture updated successfully.");
            } else {
                System.err.println("Failed to update user picture.");
            }
        });
    }

    /**
     * Updates the specified event with the provided image URL and image name. This method asynchronously
     * updates the 'eventImage' and 'imageRef' fields for a specified event in Firebase Database.
     *
     * @param eventID   The ID of the event to update.
     * @param imageRef The name of the image file.
     * @param URL      The download URL of the uploaded image.
     * @param database The {@link FirebaseDatabase} instance to use for updating.
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
