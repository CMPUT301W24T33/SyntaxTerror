package com.example.eventalate;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference events;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Anonymous sign in
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        Log.d(TAG, "UsEr Id: " + uid);

        // Test user and event
        Organizer creator = new Organizer("Dale", "Dillback", uid);
        event = new Event("FolkFest", "Folky", creator);

        db = FirebaseFirestore.getInstance();

        // Creates new collection for Events
        events = db.collection("events");
        db.collection("events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });



    }

}