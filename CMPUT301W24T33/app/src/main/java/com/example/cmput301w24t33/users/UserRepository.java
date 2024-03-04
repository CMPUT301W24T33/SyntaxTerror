package com.example.cmput301w24t33.users;
import com.google.firebase.firestore.CollectionReference;
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

    public interface UserCallback {
        void onUsersLoaded(List<User> users);
        void onFailure(Exception e);
    }

    public void setUserCallback(UserCallback callback) {
        this.userCallback = callback;
    }

    public void setUserSnapshotListener(UserCallback callback) {
        this.userCallback = callback;
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

    public void setCheckedInUsersSnapshotListener(String eventId, UserCallback callback) {
        this.userCallback = callback;
        db.collection("events").document(eventId).collection("attendees").
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
     * This method sets the MutableLiveData array in UserViewModel to hold all users that have signed up
     * for the event matching the eventId parameter.
     * @param eventId
     */
    public void setSignedUpUsersSnapshotListener(String eventId, UserCallback callback) {
        this.userCallback = callback;
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
}
