package com.example.cmput301w24t33.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import com.example.cmput301w24t33.notifications.NotificationManager;

public class AppInitialization extends AppCompatActivity {

    /**
     * Called when the activity is starting. This method is where you perform initialization such as calling NotificationManager.initialize
     * to set up notifications and setting the notification initialization timestamp. It then launches the main activity of the app.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManager.initialize(this.getApplication());
        setNotificationInitTimestamp();

        launchApp();
        finish();
    }

    /**
     * Stores the current system time as the initialization timestamp for notifications in the app's shared preferences.
     * This timestamp can be used for tracking purposes or to manage notification behaviors based on the app initialization time.
     */    private void setNotificationInitTimestamp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long initTimestamp = System.currentTimeMillis();
        prefs.edit().putLong("notificationInitTimestamp", initTimestamp).apply();
    }

    /**
     * Launches the main activity of the application.
     */
    private void launchApp() {
        Intent intent = new Intent(AppInitialization.this, Attendee.class);
        startActivity(intent);
    }
}