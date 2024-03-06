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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends Fragment {
    private EditText addFnameEditText;
    private EditText addLnameEditText;
    private String fName;
    private String lName;
    private String email;
    private EditText addEmailEditText;
    private UserViewModel userViewModel;



    public Profile() {
        // Required empty public constructor
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        setupClickListeners(view);
        setupActionBar(view);

        return view;
    }

    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Edit Profile");
    }

    private void setupClickListeners(View view) {
        addFnameEditText = view.findViewById(R.id.first_name_edit_text);
        addLnameEditText = view.findViewById(R.id.last_name_edit_text);
        addEmailEditText = view.findViewById(R.id.email_edit_text);
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView profileImage = view.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> {
            // Implement profile picture editing logic here
        });

        Button cancelButton = view.findViewById(R.id.profile_cancel_button);
        cancelButton.setOnClickListener(v -> getParentFragmentManager().popBackStack()
        );

        // Saves user to DB
        Button saveButton = view.findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(v -> {
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

            String androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            User newUser = new User(androidId, fName, lName, email, false);
            Attendee activity = (Attendee) getActivity();
            activity.setUserDb(newUser);

            // Navigate back to the previous fragment
            getParentFragmentManager().popBackStack();
        });
    }

}
