package com.example.cmput301w24t33.users;
import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.constraintlayout.helper.widget.MotionEffect;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
public class UserRepository {
    private final FirebaseFirestore db;
    private final CollectionReference userCollection;
    private UserCallback userCallback;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        userCollection = db.collection("users");
    }
    /**
     * Interface provides options for different user query results
     */
    public interface UserCallback {
        void onUsersLoaded(List<User> users);
        User onUsersLoaded(User user);
        void onFailure(Exception e);
    }

    /**
     * Initializes userCallback
     * @param userCallback a UserCallback to initialize for facilitating query result returns
     */
    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }

    /**
     * Sets database listener to query for all users and reflect any changes to users in our Firestore database.
     * <p>
     *     This method send Firestore error message as a parameter to the UserCallback function if the UserListener
     *     encounters any Firestore exceptions.
     *     If no errors/exceptions are encountered, each document from the "users" collection is parsed into a
     *     new User object and added to the users. userAdapter is then notified of changes.
     * </p>
     * @see User
     * @see UserRepository.UserCallback
     */
    public void setUserSnapshotListener() {
        userCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                //Firebase error
                userCallback.onFailure(e);
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
     * Sets database listener to query for all users checking into an event and reflect any changes
     * to said users in our Firestore database.
     * <p>
     *     This method send Firestore error message as a parameter to the UserCallback function if the UserListener
     *     encounters any Firestore exceptions.
     *     If no errors/exceptions are encountered, each document from the "attendees" collection in the document with
     *     the matching eventId is parsed into a new User object and added to the users list.
     *     userAdapter is then notified of changes.
     * </p>
     * @see User
     * @see UserRepository.UserCallback
     */
    // Maybe this should be in the EventsRepository as its technically querying the "events" collection...
    public void setCheckedInUsersSnapshotListener(String eventId) {
        DocumentReference docRef = db.collection("events").document(eventId);

        docRef.collection("attendees").
                addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        //Firebase error
                        userCallback.onFailure(e);
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
     * Sets database listener to query for all users signed up for an event and reflect any changes
     * to said users in our Firestore database.
     * <p>
     *     This method sends a Firestore error message as a parameter to the UserCallback function if the UserListener
     *     encounters any Firestore exceptions.
     *     If no errors/exceptions are encountered, each document from the "signedUp" collection in the document
     *     with the matching eventId is parsed into a new User object and added to the users list.
     *     userAdapter is then notified of changes.
     * </p>
     * @see User
     * @see UserRepository.UserCallback
     */
    // Maybe this should be in the EventsRepository as its technically querying the "events" collection...
    public void setSignedUpUsersSnapshotListener(String eventId) {
        db.collection("events").document(eventId).collection("signedUp").
                addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        //Firebase error
                        userCallback.onFailure(e);
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
     * Sets database listener to query for the user with the corresponding userId and reflect any changes
     * to said user in our Firestore database.
     * <p>
     *     This method sends a Firestore error message as a parameter to the UserCallback function if the UserListener
     *     encounters any Firestore exceptions.
     *     If no errors/exceptions are encountered, each document from the "signedUp" collection in the document
     *     with the matching eventId is parsed into a new User object and added to the users list.
     *     userAdapter is then notified of changes.
     * </p>
     * @see User
     * @see UserRepository.UserCallback
     */
    public void getUser(String userId) {
        DocumentReference userRef = userCollection.document(userId);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document/User found
                        User currentUser = documentSnapshot.toObject(User.class);
                        // Notifies callback with User object
                        userCallback.onUsersLoaded(currentUser);
                    } else {
                        // Document/User does not exist
                        Log.d(TAG, "No such document");
                        userCallback.onUsersLoaded((User) null);
                    }
                }).addOnFailureListener(e -> {
                    // Error in fetching document
                    Log.e(TAG, "Error getting document", e);
                    userCallback.onFailure(e);
                });
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        userCollection.add(user)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    user.setUserId(documentId);
                    userCollection.document(documentId)
                            .update("userId", documentId)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(MotionEffect.TAG, "userId added to document");
                            }).addOnFailureListener(e -> {
                                Log.e(MotionEffect.TAG, "Failed to add userId", e);
                            });
                    Log.d(MotionEffect.TAG, "Create User Document success: " + documentId);
                })
                .addOnFailureListener(e -> {
                    Log.w(MotionEffect.TAG, "Create User Document failed", e);
                });
    }
}
