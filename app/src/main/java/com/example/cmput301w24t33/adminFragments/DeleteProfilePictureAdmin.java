package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.AdminDeleteProfilePictureFragmentBinding;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteProfilePictureAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteProfilePictureAdmin extends Fragment {


    private UserRepository userRepo;
    private AdminDeleteProfilePictureFragmentBinding binding;
    private User userProfile;



    public DeleteProfilePictureAdmin() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of DeleteProfilePictureAdmin fragment
     * using the provided parameters.
     *
     * @param user The user to remove image from.
     * @return A new instance of DeleteProfilePictureAdmin.
     */
    public static DeleteProfilePictureAdmin newInstance(User user) {
        DeleteProfilePictureAdmin fragment = new DeleteProfilePictureAdmin();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = AdminDeleteProfilePictureFragmentBinding.inflate(inflater,container,false);
        if (getArguments() != null) {
            userProfile = (User) getArguments().getSerializable("user");
        }
        loadData();
        setupActionBar();
        userRepo = new UserRepository(FirebaseFirestore.getInstance());
        setupActionButtons();
        return binding.getRoot();
    }

    private void setupActionBar() {
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        binding.deleteProfilePictureActionbar.setBackgroundColor(color);
        binding.backActionbarTextview.setText("Delete Profile Pic");
    }

    /**
     * Called when the view previously created by onCreateView(LayoutInflater, ViewGroup, Bundle) has
     * been detached from the fragment. The binding is nullified here to avoid memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks by nullifying the reference to the binding
    }

    /**
     * Initializes the action buttons with their respective click listeners.
     */
    private void setupActionButtons() {
        // Sets up listeners for both cancel and delete buttons
        binding.cancelButton.setOnClickListener(v -> onCancel());
        binding.deleteButton.setOnClickListener(v -> onDelete());
        binding.backArrowImg.setOnClickListener(v -> onCancel());
    }

    /**
     * Handles the action to cancel the deletion, typically by popping the current fragment
     * off the stack to return to the previous screen.
     */
    private void onCancel() {
        // Cancel the deletion and return to the previous screen
        getParentFragmentManager().popBackStack();
    }

    /**
     * Handles the deletion action. Implement the actual deletion logic here.
     */
    private void onDelete() {
        // Implement event deletion logic here
        userProfile.setImageRef(null);
        userProfile.setImageUrl(null);
        userRepo.updateUser(userProfile);
        getParentFragmentManager().popBackStack(); // Placeholder action
    }
    /**
     * Loads picture into UI to allow user to view before deleting
     * before confirming deletion.
     */
    private void loadData() {
        binding.profileFirstNameEditText.setText(userProfile.getFirstName());
        binding.profileLastNameEditText.setText(userProfile.getLastName());
        Glide.with(this).load(userProfile.getImageUrl()).into(binding.profilePictureImageView);
    }
}
