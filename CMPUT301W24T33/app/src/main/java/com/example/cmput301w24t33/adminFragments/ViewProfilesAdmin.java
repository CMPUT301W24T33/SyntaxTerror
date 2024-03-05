package com.example.cmput301w24t33.adminFragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

public class ViewProfilesAdmin extends Fragment implements UserAdapter.OnUserListener {

    private List<User> userList;
    private UserAdapter userAdapter;
    private RecyclerView userRecyclerView;
    private UserViewModel userViewModel;
    private int selectedUserPosition = -1;

    public ViewProfilesAdmin(){}
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
        setupClickListeners(view);
        displayUsers(view);
        return view;
    }
    public void onResume() {
        super.onResume();
        Log.d(ContentValues.TAG, "VIEW PROFILES ADMIN FRAG RESUME");
        // Loads users for display
        userViewModel.loadUsers();

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

    private void updateUI(List<User> users) {
        userAdapter.setUsers(users);
    }

    private void setAdapter() {
        Context context = getContext();
        userRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        userRecyclerView.setHasFixedSize(true);
        userAdapter = new UserAdapter(userList, this);
        userRecyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    private void displayUsers(View view) {
        Log.d(TAG, "USER FRAGMENT: DISPLAY USERS");
        userRecyclerView = view.findViewById(R.id.profiles_admin);

        userList = new ArrayList<>();
        setAdapter();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            updateUI(users);
        });
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