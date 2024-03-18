// Purpose:
// Responsible for providing the functionality for creating a new user profile, including input
// validation for the user's name and email and preparing the new user data for database insertion.
// Only called once when a new android id is used and persists until user fills in profile info
//
// Issues: None
//

package com.example.cmput301w24t33.users;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.fileUpload.ImageHandler;
import com.example.cmput301w24t33.activities.Attendee;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String imageRef;
    private String imageUrl;
    /**
     * Initializes the fragment and user repository when the fragment is first created.
     *
     * @param savedInstanceState If the fragment is re-created from a previous state, this Bundle contains the data it most recently supplied.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepo = new UserRepository(FirebaseFirestore.getInstance());
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
            //Create Users Deterministic Identicon
            byte[] hash = IdenticonGenerator.generateHash(fName);
            Bitmap identicon = IdenticonGenerator.generateIdenticonBitmap(hash);

            // Attempt to save the identicon as a JPEG
            try {
                // Creates a file in the app's cache directory
                File identiconFile = new File(getContext().getCacheDir(), "userIdenticon.jpg");

                // Convert bitmap to byte array and write to the file
                try (FileOutputStream outputStream = new FileOutputStream(identiconFile)) {
                    identicon.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
                // Set the ImageView to the identicon Bitmap
                ImageHandler.uploadFile(Uri.fromFile(new File("/data/user/0/com.example.cmput301w24t33/cache/userIdenticon.jpg")), storage, new ImageHandler.UploadCallback() {
                    @Override
                    public void onSuccess(Pair<String, String> result) {
                        // Handle the success case here
                        // For example, store the result.first as the image URL and result.second as the image name
                        String userId = getAndroidId();
                        Log.d("Upload Success", "URL: " + result.first + ", Name: " + result.second);
                        User newUser = new User(userId, fName, lName, email, false, result.first,result.second);
                        userRepo.setUser(newUser, getAndroidId());
                        getParentFragmentManager().popBackStack();

                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle the failure case here
                        Log.d("Upload Failure", e.toString());
                    }
                });

                // TODO: Do something with the file if needed, such as uploading it to a server

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error saving userIdenticon", Toast.LENGTH_SHORT).show();
            }


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
