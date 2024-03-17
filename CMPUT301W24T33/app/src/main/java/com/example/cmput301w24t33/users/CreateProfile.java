// Purpose:
// Responsible for providing the functionality for creating a new user profile, including input
// validation for the user's name and email and preparing the new user data for database insertion.
// Only called once when a new android id is used and persists until user fills in profile info
//
// Issues: None
//

package com.example.cmput301w24t33.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.activities.Attendee;

/**
 * Fragment for creating a new user profile within the application. It captures
 * user information such as first name, last name, and email, validating each
 * before creating a new User instance.
 */
public class CreateProfile extends Fragment {
    private UserRepository userRepo;
    private EditText addFnameEditText;
    private EditText addLnameEditText;
    private String fName;
    private String lName;
    private String email;
    private EditText addEmailEditText;

    /**
     * Initializes the fragment and user repository when the fragment is first created.
     *
     * @param savedInstanceState If the fragment is re-created from a previous state, this Bundle contains the data it most recently supplied.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepo = new UserRepository();
    }

    /**
     * Inflates the fragment's view and sets up the UI components and action bar for creating a new user profile.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_create_fragment, container, false);
        setupClickListeners(view);
        setupActionBar(view);
        return view;
    }

    /**
     * Sets up the action bar for the create profile screen, including setting the
     * title and hiding the back button.
     *
     * @param view The current view where the action bar is located.
     */
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Create Profile");

        ImageButton back = view.findViewById(R.id.back_arrow_img);
        back.setVisibility(View.GONE);
    }

    /**
     * Initializes click listeners for the create profile screen, including
     * logic for creating a new user profile.
     *
     * @param view The current view where the EditText and Button are located.
     */
    private void setupClickListeners(View view) {
        addFnameEditText = view.findViewById(R.id.first_name_edit_text);
        addLnameEditText = view.findViewById(R.id.last_name_edit_text);
        addEmailEditText = view.findViewById(R.id.email_edit_text);

        Button createButton = view.findViewById(R.id.profile_save_button);
        createButton.setOnClickListener(v -> {
            fName = addFnameEditText.getText().toString().trim();
            lName = addLnameEditText.getText().toString().trim();
            email = addEmailEditText.getText().toString().trim();

            if (fName.isEmpty() || lName.isEmpty() || fName.matches(".*\\d+.*") || lName.matches(".*\\d+.*") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Please enter valid information", Toast.LENGTH_LONG).show();
                return;
            }

            String userId = getAndroidId();
            User newUser = new User(userId, fName, lName, email, false, "","");
            userRepo.setUser(newUser, userId);

            getParentFragmentManager().popBackStack();
        });
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
