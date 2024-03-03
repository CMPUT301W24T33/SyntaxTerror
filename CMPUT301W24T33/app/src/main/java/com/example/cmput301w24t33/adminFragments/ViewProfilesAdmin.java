package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.notifications.Notification;
import com.example.cmput301w24t33.notifications.NotificationAdapter;
import com.example.cmput301w24t33.users.Profile;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewProfilesAdmin extends Fragment implements UserAdapter.OnUserListener {

    private List<User> users;
    private UserAdapter adapter;
    private int selectedUserPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_view_profiles_fragment, container, false);
        setupActionBar(view);
        setupUsersList(view);
        setupClickListeners(view);
        return view;
    }

    @Override
    public void onUserClick(int position) {
        selectedUserPosition = position;
        View view = getView();
        if (view != null) {
            replaceFragment(new EditProfileAdmin());
        }
    }
    private void setupActionBar(@NonNull View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("All Profiles");
    }

    private void setupUsersList(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.profiles_admin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users = createSampleUsers();
        adapter = new UserAdapter(users, this);
        recyclerView.setAdapter(adapter);
    }

    private List<User> createSampleUsers() {
        List<User> sampleUsers = new ArrayList<>();
        sampleUsers.add(new User("User 1", "a", "1"));
        sampleUsers.add(new User("User 2", "b", "2"));
        sampleUsers.add(new User("User 3", "c", "3"));
        return sampleUsers;
    }
    private void setupClickListeners(@NonNull View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        RelativeLayout generalActionBar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(getContext(),R.color.admin_actionbar);
        generalActionBar.setBackgroundColor(color);
    }
    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.admin_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}