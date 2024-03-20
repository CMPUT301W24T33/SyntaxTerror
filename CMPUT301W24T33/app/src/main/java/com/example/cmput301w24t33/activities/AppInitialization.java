package com.example.cmput301w24t33.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cmput301w24t33.notifications.NotificationManager;

public class AppInitialization extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NotificationManager.getInstance(this.getApplication());
        launchApp();
        finish();
    }

    private void launchApp() {
        Intent intent = new Intent(AppInitialization.this, Attendee.class);
        startActivity(intent);
    }
}