package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.notifications.NotificationManager;
import com.example.cmput301w24t33.users.CreateProfile;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.example.cmput301w24t33.users.UserViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppInitialization extends AppCompatActivity {

    private User currentUser;
    private UserRepository userRepo;
    private UserViewModel userViewModel;

    /**
     * Called when the activity is starting. This method is where you perform initialization such as calling NotificationManager.initialize
     * to set up notifications and setting the notification initialization timestamp. It then launches the main activity of the app.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        NotificationManager.initialize(this.getApplication());
        authenticateUser();
        //launchApp();
        //finish();
    }

    /**
     * Launches the main activity of the application.
     */
    private void launchApp() {
        Intent intent = new Intent(AppInitialization.this, Attendee.class);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    private void authenticateUser() {
        String userId = getAndroidId();
        userRepo = new UserRepository(FirebaseFirestore.getInstance());
        userViewModel = new UserViewModel(userRepo, new MutableLiveData<>(), new MutableLiveData<>(), new User());
        //userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser(userId).observe(this, user -> {
            if (user != null) {
                // User has a profile
                Log.d(TAG, "User Authenticated: " + user.getUserId());
                currentUser = user;
                launchApp();
                finish();
                //fetchInfo(findViewById(R.id.profile_image));
            } else {
                // New User
                replaceFragment(new CreateProfile());
            }
            //setupViewModel();
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
        transaction.replace(R.id.attendee_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

}