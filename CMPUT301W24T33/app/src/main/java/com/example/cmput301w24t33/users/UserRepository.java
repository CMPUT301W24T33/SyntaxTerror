//  Responsible for managing user data within the app, interfacing with Firebase Firestore to
//  retrieve, listen for changes, and update user data in real time. It uses a callback interface
//  to communicate data fetching success or failure back to the caller, enabling asynchronous data
//  handling and UI updates based on user data operations.

package com.example.cmput301w24t33.users;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles data operations for Users, interacting with Firebase Firestore to
 * perform CRUD operations on user data.
 */
public class UserRepository {
    private final FirebaseFirestore db;
    private final CollectionReference userCollection;
    private UserCallback userCallback;

    /**
     * Constructor for UserRepository. Initializes connection to Firestore and the users collection.
     */
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("users");
    }

    /**
     * Interface for callbacks when user data is loaded or an error occurs.
     */
    public interface UserCallback {
        void onUsersLoaded(List<User> users);
        User onUsersLoaded(User user);
        void onFailure(@NonNull Exception e);

    }

    /**
     * Sets the UserCallback for handling responses from Firestore.
     *
     * @param userCallback Callback to handle Firestore responses.
     */
    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }

    /**
     * Sets a snapshot listener on the entire user collection to receive real-time updates.
     */
    public void setUserSnapshotListener() {
        userCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                userCallback.onFailure(e);
                return;
            }
            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                User user = doc.toObject(User.class);
                users.add(user);
            }
            userCallback.onUsersLoaded(users);
        });
    }

    /**
     * Sets a snapshot listener for users checked into a specific event, reflecting real-time updates.
     *
     * @param eventId The ID of the event to listen for check-ins.
     */
    public void setCheckedInUsersSnapshotListener(String eventId) {
        DocumentReference docRef = db.collection("events").document(eventId);

        docRef.collection("attendees").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                userCallback.onFailure(e);
                return;
            }
            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                User user = doc.toObject(User.class);
                users.add(user);
            }
            userCallback.onUsersLoaded(users);
        });
    }

    /**
     * Sets a snapshot listener for users signed up for a specific event, reflecting real-time updates.
     *
     * @param eventId The ID of the event to listen for sign-ups.
     */
    public void setSignedUpUsersSnapshotListener(String eventId) {
        db.collection("events").document(eventId).collection("signedUp")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        userCallback.onFailure(e);
                        return;
                    }
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        users.add(user);
                    }
                    userCallback.onUsersLoaded(users);
                });
    }

    /**
     * Retrieves a single user by their ID and reflects any changes in real-time.
     *
     * @param userId The unique ID of the user to retrieve.
     */
    public void getUser(String userId) {
        DocumentReference userRef = userCollection.document(userId);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //Document/User found
                        User currentUser = documentSnapshot.toObject(User.class);
                        // Notifies callback with User object
                        Log.d(TAG, "HEY ITS HERE: " + currentUser.getUserId());
                        userCallback.onUsersLoaded(currentUser);
                    } else {
                        // Document/User does not exist
                        Log.d(TAG, "No such document");
                        userCallback.onUsersLoaded((User)null);
                    }
                }).addOnFailureListener(e -> {
                    // Error in fetching document
                    Log.e(TAG, "Error getting document", e);
                    userCallback.onFailure(e);
                });
    }

    /**
     * Adds a new user to the Firestore database and updates their userId with the generated document ID.
     *
     * @param user The User object to add to Firestore.
     */

    public void setUser(User user, String docId) {
        userCollection.document(docId).set(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d(MotionEffect.TAG, "Create User Document success: " + user.getUserId());
                })
                .addOnFailureListener(e -> {
                    Log.w(MotionEffect.TAG, "Create User Document failed", e);
                });
    }
}
