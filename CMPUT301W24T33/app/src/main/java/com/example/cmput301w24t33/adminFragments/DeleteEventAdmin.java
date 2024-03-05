package com.example.cmput301w24t33.adminFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cmput301w24t33.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteEventAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteEventAdmin extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.admin_delete_event_fragment, container, false);
        setOnClickListeners(view);
        return view;
    }

    public void setOnClickListeners(View view){
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> {
            // put logic for deleting event here
        });
    }
}