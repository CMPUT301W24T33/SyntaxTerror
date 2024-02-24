package com.example.cmput301w24t33.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.cmput301w24t33.R;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        setOnClickListeners();
    }

    private void setOnClickListeners(){
        // Event List click listener
        ImageButton eventButton = findViewById(R.id.event_arrow_button);
        eventButton.setOnClickListener(v -> {
        });

        ImageButton profileButton = findViewById(R.id.event_arrow_button);
        profileButton.setOnClickListener(v -> {
        });

        ImageButton imageButton = findViewById(R.id.image_arrow_button);
        eventButton.setOnClickListener(v -> {
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.attendee_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}