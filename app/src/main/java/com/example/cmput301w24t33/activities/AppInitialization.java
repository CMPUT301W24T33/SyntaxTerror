package com.example.cmput301w24t33.activities;

import android.content.Intent;
import android.os.Bundle;

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

        launchApp();
        finish();
    }

    /**
     * Launches the main activity of the application.
     */
    private void launchApp() {
        Intent intent = new Intent(AppInitialization.this, AttendeeActivity.class);
        startActivity(intent);
    }
}