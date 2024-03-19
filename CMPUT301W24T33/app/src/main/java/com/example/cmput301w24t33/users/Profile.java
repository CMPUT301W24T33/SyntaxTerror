// Purpose:
// Facilitates the viewing and editing of user profile details, including name and email, with
// validation checks to ensure that the data entered is appropriate. It provides an interface for
// users to interact with their profile.
//
// Issues: Fill in user profile info when navigating to this fragment
//

package com.example.cmput301w24t33.users;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.activities.Attendee;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.cmput301w24t33.fileUpload.ImageHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

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
    private ImageView profileImageView;
<<<<<<< Updated upstream
    private String imageRef;
    private String imageUrl;
    private User profileToEdit;
=======
    public static String imageRef;
    public static String imageUrl;
>>>>>>> Stashed changes

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private void fetchInfo() {
        String androidId = getAndroidId(); // Ensure this method correctly retrieves the ID
        db.collection("users").document(androidId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            email = documentSnapshot.getString("email");
                            fName = documentSnapshot.getString("firstName");
                            lName = documentSnapshot.getString("lastName");
                            imageUrl = documentSnapshot.getString("imageUrl");

                            // Now that we have the data, we can log it
                            addFnameEditText.setText(fName);
                            addEmailEditText.setText(email);
                            addLnameEditText.setText(lName);
                            if (imageUrl != "") {
                                Picasso.get().load(imageUrl).into(profileImageView);
                            }
                            else{

                                //Create Users Deterministic Identicon
                                byte[] hash = IdenticonGenerator.generateHash(fName);
                                Bitmap identicon = IdenticonGenerator.generateIdenticonBitmap(hash);

                                // Attempt to save the identicon as a JPEG
                                try {
                                    // Creates a file in the app's cache directory
                                    File identiconFile = new File(getContext().getCacheDir(), "userIdenticon.jpg");
                                    Log.d("profile", String.valueOf(getContext().getCacheDir()));
                                    // Convert bitmap to byte array and write to the file
                                    try (FileOutputStream outputStream = new FileOutputStream(identiconFile)) {
                                        identicon.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                                    }

                                    ImageHandler.uploadFile(Uri.fromFile(new File("/data/user/0/com.example.cmput301w24t33/cache/userIdenticon.jpg")), storage, new ImageHandler.UploadCallback() {
                                        @Override
                                        public void onSuccess(Pair<String, String> result) {
                                            // Handle the success case here
                                            // For example, store the result.first as the image URL and result.second as the image name
                                            Log.d("Upload Success", "URL: " + result.first + ", Name: " + result.second);
                                            imageRef = result.second;
                                            imageUrl = result.first;
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            // Handle the failure case here
                                            Log.d("Upload Failure", e.toString());
                                        }
                                    });
                                    // Set the ImageView to the identicon Bitmap
                                    profileImageView.setImageBitmap(identicon);

                                    // TODO: Do something with the file if needed, such as uploading it to a server

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Error saving userIdenticon", Toast.LENGTH_SHORT).show();
                                }
                                profileImageView.setImageBitmap(identicon);
                            }

                        } else {
                            System.out.println("No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error getting documents: " + e);
                    }
                });
    }

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {

                    Uri photoUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), photoUri);
                        profileImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("returned url",photoUri.toString());

                    ImageHandler.uploadFile(photoUri, storage, new ImageHandler.UploadCallback() {
                        @Override
                        public void onSuccess(Pair<String, String> result) {
                            // Handle the success case here
                            // For example, store the result.first as the image URL and result.second as the image name
                            Log.d("Upload Success", "URL: " + result.first + ", Name: " + result.second);
                            imageRef = result.second;
                            imageUrl = result.first;
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Handle the failure case here
                            Log.d("Upload Failure", e.toString());
                        }
                    });
                }
            }
    );


    public static Profile newInstance(User user) {
        Profile fragment = new Profile();
        Log.d(TAG, "Profile NewInstance");
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }



    /**
     * Inflates the layout for the profile editing screen and initializes UI components, including setting up click listeners and the action bar.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        setupClickListeners(view);
        setupActionBar(view);
        userRepo = new UserRepository(FirebaseFirestore.getInstance());

        if (getArguments() != null) {
            Bundle userBundle = getArguments();
            profileToEdit = (User) userBundle.getSerializable("user");
            loadData(view, profileToEdit);
        }

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
        profileImageView = view.findViewById(R.id.profile_image);
        fetchInfo();
        // Back button listener
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Profile image listener
        ImageView profileImage = view.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> {
            selectImage();
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

    private void loadData(View view, User profile) {
        /*
        EditText editFirstName = view.findViewById(R.id.first_name_edit_text);
        EditText editLastName = view.findViewById(R.id.last_name_edit_text);
        EditText editEmail = view.findViewById(R.id.email_edit_text);
        */
        addFnameEditText.setText(profile.getFirstName());
        addLnameEditText.setText(profile.getLastName());
        addEmailEditText.setText(profile.getEmail());


    }
    /**
     * Select an image as a profile image
     */
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }



    /**
     * Saves the profile after validating the input data and updates the user data in the database.
     */
    private void saveProfile() {

        userRepo = new UserRepository(FirebaseFirestore.getInstance());
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
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", fName);
        updates.put("lastName", lName);
        updates.put("email",email);
        updates.put("imageRef",imageRef);
        updates.put("imageUrl",imageUrl);

        db.collection("users").document(getAndroidId())
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating document", e));
        profileToEdit.setFirstName(addFnameEditText.getText().toString());
        profileToEdit.setLastName(addLnameEditText.getText().toString());
        profileToEdit.setEmail(addEmailEditText.getText().toString());

        userRepo.updateUser(profileToEdit);

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
