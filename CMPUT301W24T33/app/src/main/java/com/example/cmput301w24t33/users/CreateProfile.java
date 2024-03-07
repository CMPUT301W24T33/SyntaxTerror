package com.example.cmput301w24t33.users;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProfile #newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateProfile extends Fragment {
    private UserRepository userRepo;
    private EditText addFnameEditText;
    private EditText addLnameEditText;
    private String fName;
    private String lName;
    private String email;
    private EditText addEmailEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_create_fragment, container, false);
        setupClickListeners(view);
        setupActionBar(view);

        return view;
    }
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Create Profile");

        ImageButton back = view.findViewById(R.id.back_arrow_img);
        back.setVisibility(View.GONE);


    }

    private void setupClickListeners(View view) {
        addFnameEditText = view.findViewById(R.id.first_name_edit_text);
        addLnameEditText = view.findViewById(R.id.last_name_edit_text);
        addEmailEditText = view.findViewById(R.id.email_edit_text);


        // Saves user to DB
        Button createButton = view.findViewById(R.id.profile_save_button);
        createButton.setOnClickListener(v -> {
            // Implement save profile editing logic here
            fName = addFnameEditText.getText().toString().trim();
            lName = addLnameEditText.getText().toString().trim();
            email = addEmailEditText.getText().toString().trim();

            //Checks if user passes a string, if not it asks the user again
            if (fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both your first and last name", Toast.LENGTH_LONG).show();
                return;
            }

            //Checks if there are numbers in the name
            if (fName.matches(".*\\d+.*") || lName.matches(".*\\d+.*")) {
                Toast.makeText(getContext(), "Please enter a valid name", Toast.LENGTH_LONG).show();
                return;
            }

            //Checks if email is valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
                return;
            }
            createUser();

            // Navigate back to the previous fragment
            getParentFragmentManager().popBackStack();
        });
    }

    private String getAndroidId() {
        String androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    private void createUser() {
        userRepo = new UserRepository();
        String userId = getAndroidId();
        Log.d(TAG, userId);
        User newUser = new User(userId, fName, lName, email, false);
        userRepo.setUser(newUser);
    }
}