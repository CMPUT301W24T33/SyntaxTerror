package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.attendeeFragments.EventDetailsAttendee;
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.qrCode.QRScanner;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class Attendee extends AppCompatActivity implements AdapterEventClickListener {
    private FirebaseFirestore db;
    private CollectionReference events;
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private RecyclerView eventRecyclerView;

    private QRScanner qrScanner = new QRScanner();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity);
        eventRecyclerView = findViewById(R.id.event_recyclerview);
        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
        setOnClickListeners();
        setDbListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
        authorizeUser();
    }




    /**
     *
     */
    private ActivityResultLauncher<Intent> anonymousAuthLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                String userId = data.getStringExtra("USER_ID");
                Log.d(TAG, "Received user ID: " + userId);
                // Use userId to create user in database
                registerUser();
            });

    /**
     * This method checks to see if user is signed in and authorized.
     * If not, it launches AnonymousAuthActivity to authorize and sign in user
     */
    private void authorizeUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // If user is not signed in, launch AnonymousAuthActivity to sign user in
        if (currentUser == null) {
            Intent intent = new Intent(this, AnonymousAuthActivity.class);
            anonymousAuthLauncher.launch(intent);

        }
    }
    private void registerUser() {
        replaceFragment(new Profile());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.attendee_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void setAdapter(){
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(eventList,this);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }

    public void setUserDb(User newUser){
        db.collection("users")
                .add(newUser)
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

    // Adds events to DB
    private void setDB(String name, String description) {
        Event event = new Event(name, description);
        Log.d(TAG, "setDB");

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

    public void onEventClickListener(Event event, int position){
        replaceFragment(new EventDetailsAttendee());
    }

    public void setDbListeners() {
        setAdapter();
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    if (eventList != null) { eventList.clear(); }
                    for (QueryDocumentSnapshot doc : querySnapshots) {
                        String name = doc.getString("name");
                        String description = doc.getString("description");
                        Log.d("Firestore", String.format("Event(%s) fetched", name));
                        eventList.add(new Event(name, description));
                    }

                    eventAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setOnClickListeners(){

        // Profile button click listener
        ImageView profileButton = findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));

        // Check in button click listener
        // used reviewgrower.com/button-and-badge-generator/ to quickly make a button
        ImageView checkInButton = findViewById(R.id.check_in_img);
        checkInButton.setOnClickListener(v -> {
            // fill in a fragment or whatever is decided for checkin
            qrScanner.scanQRCode(Attendee.this);
       });

        // Usermode click listener
        ImageButton userMode = findViewById(R.id.button_user_mode);
        userMode.setOnClickListener(v -> {
            Intent intent = new Intent(Attendee.this, Organizer.class);
            startActivity(intent);
            finish();
        });
    }

}