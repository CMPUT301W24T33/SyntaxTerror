package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.attendeeFragments.EventDetailsAttendee;
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.qrCode.QRScanner;
import com.example.cmput301w24t33.users.GetUserCallback;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.GetDocumentRequestOrBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendee extends AppCompatActivity implements AdapterEventClickListener {
    private FirebaseFirestore db;
    //private CollectionReference events;
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private RecyclerView eventRecyclerView;
    private FirebaseAuth mAuth;
    private String userId;

    private QRScanner qrScanner = new QRScanner();
    private EventViewModel eventViewModel;
//
    private FusedLocationProviderClient fusedLocationProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_activity);
        db = FirebaseFirestore.getInstance();
        eventRecyclerView = findViewById(R.id.event_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        eventList = new ArrayList<>();
        setAdapter();
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, events -> {
            updateUI(events);
        });
        setOnClickListeners();
    }

    /**
     * Updates event adapter with current contents in our events collection
     * @param events is a live representation of Events in our events collection as a List
     */
    private void updateUI(List<Event> events) {
        eventAdapter.setEvents(events);
    }

    /**
     * This method authorizes current user and loads up-to-date events for display
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
        authorizeUser();
        eventViewModel.loadEvents();
    }


    /**
     * This is responsible for launching our Anonymous Authorization and Registration of a new user
     */
    private ActivityResultLauncher<Intent> anonymousAuthLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                userId = data.getStringExtra("USER_ID");
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
            Log.d(TAG, "User not signed in. Launch anonAuth");
            Intent intent = new Intent(this, AnonymousAuthActivity.class);
            anonymousAuthLauncher.launch(intent);
        } else {
            userId = currentUser.getUid();
            Log.d(TAG, userId);
        }

    }
    private void registerUser() {
        replaceFragment(new Profile());
    }

    private void setAdapter(){
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(eventList,this, this);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Saves a provided User into the "users" collection in our database
     * <p>If successful, Log document id (docId == uId)
     * Else, Log error, e, that was encountered</p>
     * @param newUser   User object to be saved to database
     * @see User
     */
    public void setUserDb(User newUser){
        db.collection("users")
                .document(userId)
                .set(newUser)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void onEventClickListener(Event event, int position){
        replaceFragment(EventDetailsAttendee.newInstance(event));
    }

    /**
     * This method queries our database's "users" collection for a specific docId (docId == uId), then uses a callback function
     * to handle the query result
     * <p>
     *     The GetUserCallback interface is used to handle the query results
     *     <ul>
     *         <li>Document/User exists in the collection: currentUser is passed into the callback function
     *         to be handled by the calling function.</li>
     *         <li>Document/User does NOT exist in the collection: null is passed into the callback function
     *         to be handled by the calling function.</li>
     *         <li>Error in the .get() function in fetching User: The error encountered, e, is passed into the
     *         callback function to be handled by the calling function.</li>
     *     </ul>
     * </p>
     * @param callback  the callback interface to handle results of the query
     *
     * @see GetUserCallback
     */
    private void queryUserByDocId( GetUserCallback callback) {
        // Get the reference to the user's document using the provided docId
        DocumentReference userRef = db.collection("users").document(userId);

        // Retrieve the user's data
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document/User found
                        User currentUser = documentSnapshot.toObject(User.class);
                        // Notifies callback with User object
                        callback.onUserReceived(currentUser);
                    } else {
                        // Document/User does not exist
                        Log.d(TAG, "No such document");
                        callback.onUserReceived(null);
                    }
        }).addOnFailureListener(e -> {
            // Error in fetching document
            Log.e(TAG, "Error getting document", e);
            callback.onFailure(e);
        });
    }

    private void setOnClickListeners(){

        // Profile button click listener
        ImageView profileButton = findViewById(R.id.profile_image);

        profileButton.setOnClickListener(v -> {
            // Use queryUserByDocId to get current user object!
            FirebaseUser currentUser = mAuth.getCurrentUser();

            queryUserByDocId( new GetUserCallback() {
                @Override
                public void onUserReceived(User user) {
                    if (user != null) {
                        // update Profile fragment with user object
                    } else {
                        // User not found logged in query function
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    // Error logged in query function
                }
            });
            replaceFragment(new Profile());
        });


        // Check in button click listener
        // used reviewgrower.com/button-and-badge-generator/ to quickly make a button
        ImageView checkInButton = findViewById(R.id.check_in_img);
        checkInButton.setOnClickListener(v -> {
            // fill in a fragment or whatever is decided for checkin

            String qrCode = qrScanner.scanQRCode(Attendee.this);
            if (qrCode != null) {
                final String[] eventId = new String[1];
                Toast toast = new Toast(this);
                toast.setText(qrCode);
                toast.show();

                db.collection("events")
                        .whereEqualTo("checkInQR", qrCode)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot doc : task.getResult()) {
                                        Log.d("QRCheckIn", doc.getId() + "->" + doc.getData());
                                        eventId[0] = doc.getData().get("checkInQR").toString();
                                        Map<String, String> update = new HashMap<>();

                                        update.put(userId, userId); // should be geo-location

                                        // add attendee id to attendees array (or collection... shouldn't it be a collection?)
                                        // creates new document in attendees sub-collection in event doc with
                                        //
                                        db.collection("events").document(eventId[0])
                                                .collection("attendees")
                                                .document(userId)
                                                .set(update);
                                    }
                                }
                            }
                        });
            } else {
                Toast toast = new Toast(this);
                toast.setText("Scan canceled/failed");
                toast.show();
            }

       });

        // User Mode click listener - switches to organizer activity
        ImageButton userMode = findViewById(R.id.button_user_mode);
        userMode.setOnClickListener(v -> {
            Intent intent = new Intent(Attendee.this, Organizer.class);
            intent.putExtra("uId", userId);
            startActivity(intent);
            finish();
        });

        // User Mode click listener - switches to admin activity
        userMode.setOnLongClickListener(v -> {
            // code for navigating to admin activity
            Intent intent = new Intent(Attendee.this, Admin.class);
            startActivity(intent);
            finish();
            return true;
        });
    }


    private void CheckIn(String eventId, String userId) {
        Map<String, GeoPoint> update = new HashMap<>();
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1 );
//        enforcePermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            fusedLocationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        update.put(userId, new GeoPoint(location.getLatitude(),location.getLongitude()));
                    }
                }
            });

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.

        }

//        update.put("userId", new GeoPoint(53.1111, 0.01));
        db.collection("events").document(eventId)
                .collection("attendees")
                .document(userId)
                .set(update);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.attendee_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}