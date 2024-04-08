package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.example.cmput301w24t33.users.CreateProfile;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.example.cmput301w24t33.users.UserViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity which initializes the app and all its static classes
 */
public class AppInitialization extends AppCompatActivity implements CreateProfile.OnUserCreatedListener {

    private User currentUser;
    private UserRepository userRepo;
    private UserViewModel userViewModel;
    private EventRepository eventRepo;
    private EventViewModel eventViewModel;

    /**
     * Called when the activity is starting. This method is where you perform initialization such as calling NotificationManager.initialize
     * to set up notifications and setting the notification initialization timestamp. It then launches the main activity of the app.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserRepository.initialize(this.getApplication(), FirebaseFirestore.getInstance());
        userRepo = UserRepository.getInstance();
        UserViewModel.initialize(this.getApplication(), userRepo, new MutableLiveData<>(), new MutableLiveData<>());
        userViewModel = UserViewModel.getInstance();

        EventRepository.initialize(this.getApplication(), FirebaseFirestore.getInstance());
        eventRepo = EventRepository.getInstance();
        EventViewModel.initialize(this.getApplication(), eventRepo, new MutableLiveData<>());
        eventViewModel = EventViewModel.getInstance();

        NotificationManager.initialize(this.getApplication());
        authenticateUser();
    }

    /**
     * Launches the main activity of the application.
     */
    private void launchApp() {
        Intent intent = new Intent(AppInitialization.this, AttendeeActivity.class);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    /**
     * Authenticates the user based on the device's Android ID, checking if a user profile already exists or initiating profile creation if not.
     */
    private void authenticateUser() {
        String userId = getAndroidId();
        userViewModel.getUser(userId).observe(this, user -> {
            if (user != null) {
                // User has a profile
                Log.d(TAG, "User Authenticated: " + user.getUserId());
                currentUser = user;
                launchApp();
                finish();
            } else {
                // New User
                replaceFragment(new CreateProfile());
            }
        });
    }

    /**
     * Retrieves the unique Android ID for the device. This ID is used to identify the user's device uniquely.
     * @return A string representing the Android ID of the device.
     */
    public String getAndroidId() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * Replaces the current fragment with a new one.
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Handles results of user creation
     * @param user user that has been created
     */
    @Override
    public void onUserCreated(User user) {
        userViewModel.setUser(user);
        currentUser = user;
    }
}