package com.example.cmput301w24t33.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cmput301w24t33.R;

public class Profile extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView profileImage = view.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> {
            // Implement profile picture editing logic here
        });

        Button cancelButton = view.findViewById(R.id.profile_cancel_button);
        cancelButton.setOnClickListener(v -> {
            // Implement cancel profile editing logic here
        });

        Button saveButton = view.findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(v -> {
            // Implement save profile editing logic here
        });
    }
}
