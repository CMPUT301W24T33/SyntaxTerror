package com.example.cmput301w24t33.fileUpload;

import android.net.Uri;
import android.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageHandler {

    // Takes in a filepath of a image and uploads it to firestorage. It will return a url to
    // be updated in the database later and the name of the imageuploaded
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


    // This take in a url, Users id, imageName, and the database instance and updates there fields
    // uaserImage gets updated with the url,imageRef gets updated with imageName and updates the userUpload to be true
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
    //This take in a url, Users id, imageName, and the database instance and updates two fields
    //uaserEvent gets updated with the url,imageRef gets updated with imageName
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

    // This take in a imageref, Users id,firebasestorage instance  and the database instance
    // it removes the file from firestore
    // clears the filed of both image ref and userImage
    // updates the field userUpload to false
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

    // This take in a imageref, event id,firebasestorage instance  and the database instance
    // it removes the file from firestore
    // clears the filed of both image ref and eventImage
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
