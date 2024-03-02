package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.adminFragments.ViewEventsAdmin;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        setOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ADMIN RESUME");

    }


    private void setOnClickListeners() {
        // Event List click listener
        // Button seems to not work? No Logging is done on button press
        ImageButton eventButton = findViewById(R.id.event_arrow_button);
        eventButton.setOnClickListener(v -> {
            Log.d(TAG, "ADMIN: Events button clicked");
            replaceFragment(new ViewEventsAdmin());
        });

        ImageButton profileButton = findViewById(R.id.profile_arrow_button);
        profileButton.setOnClickListener(v -> {
        });

        ImageButton imageButton = findViewById(R.id.image_arrow_button);
        imageButton.setOnClickListener(v -> {
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.admin_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

