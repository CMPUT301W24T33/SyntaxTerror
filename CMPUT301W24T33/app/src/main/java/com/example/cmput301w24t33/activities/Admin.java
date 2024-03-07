// Purpose:
// Facilitates administrative tasks within the application, offering a user interface for
// managing events and profiles. It provides navigation options for admins to access various
// administrative functionalities.
//
// Issues:
//

package com.example.cmput301w24t33.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.adminFragments.ViewEventsAdmin;
import com.example.cmput301w24t33.adminFragments.ViewProfilesAdmin;

/**
 * Represents the administrative area of the application, allowing navigation between administrative functionalities such as viewing events and profiles.
 */
public class Admin extends AppCompatActivity {

    /**
     * Called when the activity is starting. Initializes the activity, view components, and event listeners.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        View view = findViewById(R.id.admin_activity);
        setupActionBar(view);
        setOnClickListeners();
    }

    /**
     * Called after {@link #onStart()} when the activity is being re-initialized from a previously saved state, given here in savedInstanceState.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ADMIN RESUME");
    }

    /**
     * Sets click listeners for UI elements to navigate through the app's administrative functionalities.
     */
    private void setOnClickListeners() {
        // Event List button click listener
        ImageButton eventButton = findViewById(R.id.event_arrow_button);
        eventButton.setOnClickListener(v -> {
            replaceFragment(new ViewEventsAdmin());
        });

        // Profile View button click listener
        ImageButton profileButton = findViewById(R.id.profile_arrow_button);
        profileButton.setOnClickListener(v -> {
            replaceFragment(new ViewProfilesAdmin());
        });

        // Placeholder for future functionality
        ImageButton imageButton = findViewById(R.id.image_arrow_button);
        imageButton.setOnClickListener(v -> {
            // Future functionality here
        });
    }

    /**
     * Replaces the current fragment with the given fragment.
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.admin_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Sets up the custom ActionBar for the Admin activity with specific visual styles and visibility settings.
     * @param view The current view that contains the ActionBar elements.
     */
    private void setupActionBar(View view) {
        // Setting the action bar text
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Admin");

        // Making the profile button invisible
        CardView profileButton = view.findViewById(R.id.profile_button);
        profileButton.setVisibility(View.GONE);

        // Making the back arrow button invisible
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setVisibility(View.GONE);

        // Changing the background color of the action bar
        RelativeLayout generalActionbar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(this,R.color.admin_actionbar);
        generalActionbar.setBackgroundColor(color);
    }
}
