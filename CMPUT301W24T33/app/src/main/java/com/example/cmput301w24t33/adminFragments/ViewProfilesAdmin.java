// Purpose:
// Provides an interface for viewing and deleting user profiles.
//
// Issues: None

package com.example.cmput301w24t33.adminFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserAdapter;
import com.example.cmput301w24t33.users.UserViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment for administrators to view and manage user profiles.
 * It displays a list of user profiles and allows administrators to interact with them, including deletion.
 */
public class ViewProfilesAdmin extends Fragment implements UserAdapter.OnUserListener {
    private List<User> userList;
    private UserAdapter userAdapter;
    private RecyclerView userRecyclerView;
    private UserViewModel userViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_view_profiles_fragment, container, false);
        setupActionBar(view);
        setupClickListeners(view);
        displayUsers(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.loadUsers();
    }

    /**
     * Sets up the action bar, including the title and background color.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("All Profiles");

        RelativeLayout generalActionBar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        generalActionBar.setBackgroundColor(color);
    }

    /**
     * Sets up click listeners for interaction elements like the back button.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupClickListeners(@NonNull View view) {
        ImageButton backButton = view.findViewById(R.id.view_profiles_back_button);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }


    /**
     * Prepares and displays the list of user profiles.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void displayUsers(View view) {
        userRecyclerView = view.findViewById(R.id.profiles_admin);
        userList = new ArrayList<>();
        setAdapter();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), this::updateUI);
    }

    /**
     * Initializes and sets the RecyclerView adapter for displaying user profiles.
     */
    private void setAdapter() {
        Context context = getContext();
        userRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        userRecyclerView.setHasFixedSize(true);
        userAdapter = new UserAdapter(userList, this);
        userRecyclerView.setAdapter(userAdapter);
    }

    /**
     * Updates the UI by refreshing the list of user profiles shown.
     *
     * @param users The updated list of users.
     */
    private void updateUI(List<User> users) {
        userAdapter.setUsers(users);
    }

    /**
     * Handles user profile selection, setting the current selected position and potentially
     * replacing the fragment to view or edit the selected user's profile.
     *
     * @param position The position of the user clicked in the list.
     */
    @Override
    public void onUserClick(int position) {
        replaceFragment(new DeleteProfileAdmin()); // Placeholder for fragment replacement logic
    }

    /**
     * Replaces the current fragment with a new fragment, allowing for navigation between views.
     *
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.admin_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}
