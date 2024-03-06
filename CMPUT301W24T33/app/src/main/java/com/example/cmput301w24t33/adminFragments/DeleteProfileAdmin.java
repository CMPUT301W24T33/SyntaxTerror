package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.users.User;



import com.example.cmput301w24t33.R;

public class DeleteProfileAdmin extends Fragment {


    private User profileToDelete;

    public DeleteProfileAdmin() {
        // Required empty public constructor
    }

    public static DeleteProfileAdmin newInstance(User user) {
        DeleteProfileAdmin fragment = new DeleteProfileAdmin();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_delete_profile, container, false);
        setupClickListeners(view);
        setupActionBar(view);
        if (getArguments() != null) {
            // Used when editing an event
            Bundle eventBundle = getArguments();
            profileToDelete = (User) eventBundle.getSerializable("user");
            loadData(view);
        }
        return view;
    }

    private void loadData(View view) {
        EditText firstName = view.findViewById(R.id.first_name_edit_text);
        EditText lastName = view.findViewById(R.id.last_name_edit_text);
        EditText email = view.findViewById(R.id.email_edit_text);
        firstName.setText(profileToDelete.getFirstName());
        lastName.setText(profileToDelete.getLastName());
        email.setText(profileToDelete.getEmail());
    }

    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Edit Profile");

        RelativeLayout generalActionBar = view.findViewById(R.id.edit_profile_actionbar);
        int color = ContextCompat.getColor(getContext(),R.color.admin_actionbar);
        generalActionBar.setBackgroundColor(color);
    }

    private void setupClickListeners(View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button deleteButton = view.findViewById(R.id.profile_delete_button);
        deleteButton.setOnClickListener(v -> {
            // implement delete profile logic here
        });
    }

}
