// Purpose:
// Facilitates the viewing and editing of user profile details, including name and email, with
// validation checks to ensure that the data entered is appropriate. It provides an interface for
// users to interact with their profile.
//
// Issues:
//

package com.example.cmput301w24t33.users;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.activities.Attendee;

/**
 * A fragment class for displaying and editing the profile of a user.
 * Allows users to view and update their first name, last name, and email address.
 */
public class Profile extends Fragment {
    private EditText addFnameEditText;
    private EditText addLnameEditText;
    private String fName;
    private String lName;
    private String email;
    private EditText addEmailEditText;
    private UserViewModel userViewModel;
    private UserRepository userRepo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        setupClickListeners(view);
        setupActionBar(view);
        userRepo = new UserRepository();

        return view;
    }

    /**
     * Sets up the action bar for the profile editing screen, including setting the title.
     *
     * @param view The current view where the action bar is located.
     */
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Edit Profile");
    }

    /**
     * Initializes click listeners for the profile editing screen, including
     * logic for updating user profile information.
     *
     * @param view The current view where the EditText and Button are located.
     */
    private void setupClickListeners(View view) {
        // Initialize views
        addFnameEditText = view.findViewById(R.id.first_name_edit_text);
        addLnameEditText = view.findViewById(R.id.last_name_edit_text);
        addEmailEditText = view.findViewById(R.id.email_edit_text);

        // Back button listener
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Profile image listener
        ImageView profileImage = view.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> {
            // Placeholder for profile picture editing logic
        });

        // Cancel button listener
        Button cancelButton = view.findViewById(R.id.profile_cancel_button);
        cancelButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Save button listener
        Button saveButton = view.findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(v -> {

            // Save profile logic
            saveProfile();
        });
    }

    /**
     * Saves the profile after validating the input data and updates the user data in the database.
     */
    private void saveProfile() {
        // Extract text from EditTexts
        fName = addFnameEditText.getText().toString().trim();
        lName = addLnameEditText.getText().toString().trim();
        email = addEmailEditText.getText().toString().trim();

        // Validate inputs
        if (fName.isEmpty() || lName.isEmpty() || fName.matches(".*\\d+.*") || lName.matches(".*\\d+.*") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Please enter valid information", Toast.LENGTH_LONG).show();
            return;
        }

        // Create new User object
        String userId = getAndroidId();
        User newUser = new User(userId, fName, lName, email, false);

        // Navigate back
        getParentFragmentManager().popBackStack();
    }

    /**
     * Retrieves the unique Android device ID to be used as a user identifier.
     *
     * @return A string representing the Android ID.
     */
    private String getAndroidId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
