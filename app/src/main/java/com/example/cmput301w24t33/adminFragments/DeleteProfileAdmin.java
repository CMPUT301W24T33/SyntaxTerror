// Purpose:
// Provides an interface for administrators to view and delete user profiles.
//
// Issues:
// deletion logic not implemented

package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A fragment class for administrators to delete user profiles.
 * It displays the user's details and provides an option to delete the profile.
 */
public class DeleteProfileAdmin extends Fragment {

    private UserRepository userRepo;
    private User profileToDelete;

    /**
     * Creates a new instance of DeleteProfileAdmin fragment using the provided user.
     *
     * @param user The user whose profile is to be deleted.
     * @return A new instance of DeleteProfileAdmin.
     */
    public static DeleteProfileAdmin newInstance(User user) {
        DeleteProfileAdmin fragment = new DeleteProfileAdmin();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method inflates the layout for the fragment's view and initializes the UI elements.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_delete_profile_fragment, container, false);
        if (getArguments() != null) {
            profileToDelete = (User) getArguments().getSerializable("user");
            Log.w("Profile  ", profileToDelete.getFirstName());
            loadData(view);
        }
        Log.w("No profile to delete", "sad");

        userRepo = new UserRepository(FirebaseFirestore.getInstance());
        setupActionBar(view);
        setupClickListeners(view);
        return view;
    }

    /**
     * Configures the action bar with the appropriate title and background color.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Profile");

        RelativeLayout generalActionBar = view.findViewById(R.id.edit_profile_actionbar);
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        generalActionBar.setBackgroundColor(color);
    }

    /**
     * Sets up click listeners for the back and delete buttons, allowing for navigation
     * and deletion actions.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupClickListeners(View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button deleteButton = view.findViewById(R.id.profile_delete_button);
        deleteButton.setOnClickListener(v -> {
            // Implement delete profile logic here
            userRepo.deleteUser(profileToDelete);
            getParentFragmentManager().popBackStack();
        });
    }

    /**
     * Populates the UI with the user's data, allowing the administrator to review the details
     * before proceeding with the deletion.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void loadData(View view) {
        EditText firstName = view.findViewById(R.id.first_name_edit_text);
        EditText lastName = view.findViewById(R.id.last_name_edit_text);
        EditText email = view.findViewById(R.id.email_edit_text);
        firstName.setText(profileToDelete.getFirstName());
        lastName.setText(profileToDelete.getLastName());
        email.setText(profileToDelete.getEmail());
    }
}
