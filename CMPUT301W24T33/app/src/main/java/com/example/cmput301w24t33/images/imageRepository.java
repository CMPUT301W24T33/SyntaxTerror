package com.example.cmput301w24t33.images;

import android.net.Uri;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class imageRepository {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Method to upload a file to Firebase Storage and update the database
    public void uploadFile(Uri fileUri, boolean isProfilePicture,String id) {
        // Create a reference to 'users/userId/profile.jpg' or 'events/eventId/poster.jpg'
        String path = (isProfilePicture ? "users/userId/profile.jpg" : "events/eventId/poster.jpg");
        StorageReference storageRef = storage.getReference().child(path);

        // Upload file to Firebase Storage
        storageRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            // Update the database
                            updateDatabase(isProfilePicture, downloadUri.toString(),id);
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                });
    }

    private void updateDatabase(boolean isProfilePicture, String downloadUrl, String id) {
        // Define the child path and the field to update based on whether it's a profile picture or an event
        String childPath = (isProfilePicture ? "users/" + id : "events/" + id);
        String fieldToUpdate = (isProfilePicture ? "userURL" : "posterURL");

        // Update the specified field in the database
        database.getReference(childPath).child(fieldToUpdate).setValue(downloadUrl)
                .addOnSuccessListener(aVoid -> {
                    // Handle successful database update
                    // TODO: Implement success handling
                })
                .addOnFailureListener(e -> {
                    // Handle failed database update
                    // TODO: Implement failure handling
                });
    }
}
}
