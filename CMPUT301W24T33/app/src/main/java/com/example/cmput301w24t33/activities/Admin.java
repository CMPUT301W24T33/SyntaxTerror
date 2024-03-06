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
 * The Admin activity is responsible for handling the administrative user interface
 * and interactions within the application. It allows navigation to different
 * administrative functionalities like viewing events and managing profiles.
 */
public class Admin extends AppCompatActivity {

    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, using findViewById(int)
     * to programmatically interact with widgets in the UI, setting up any initial fragment
     * transactions, and general setup.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
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
     * Called after {@link #onStart} when the activity is being re-initialized from
     * a previously saved state, given here in savedInstanceState. Most implementations
     * will simply use onCreate(Bundle) to restore their state, but it is sometimes
     * convenient to do it here after all of the initialization has been done or to
     * allow subclasses to decide whether to use your default implementation. The
     * default implementation of this method performs a restore of any view state that
     * had previously been frozen by onSaveInstanceState(Bundle).
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ADMIN RESUME");
    }

    /**
     * Initializes onClickListeners for various buttons in the Admin activity.
     * This includes setting up navigation to the event management and profile
     * management sections via fragment replacement.
     */
    private void setOnClickListeners() {
        ImageButton eventButton = findViewById(R.id.event_arrow_button);
        eventButton.setOnClickListener(v -> {
            replaceFragment(new ViewEventsAdmin());
        });

        ImageButton profileButton = findViewById(R.id.profile_arrow_button);
        profileButton.setOnClickListener(v -> {
            replaceFragment(new ViewProfilesAdmin());
        });

        // Note: The imageButton does not have an associated action in the provided code.
        ImageButton imageButton = findViewById(R.id.image_arrow_button);
        imageButton.setOnClickListener(v -> {
            // Future implementation here
        });
    }

    /**
     * Replaces the current fragment displayed in the Admin activity with the
     * specified fragment. This allows for dynamic switching between different
     * administrative views without leaving the activity.
     *
     * @param fragment The new fragment to display in the {@code R.id.admin_layout} container.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.admin_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Sets up the action bar for the Admin activity. This includes setting the
     * title text, hiding unnecessary buttons like the profile button and the
     * back button, and changing the background color to indicate the admin context.
     *
     * @param view The view from which to find and modify the action bar components.
     */
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Admin");

        CardView profileButton = view.findViewById(R.id.profile_button);
        profileButton.setVisibility(View.GONE);

        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setVisibility(View.GONE);

        RelativeLayout generalActionbar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(this, R.color.admin_actionbar);
        generalActionbar.setBackgroundColor(color);
    }
}


