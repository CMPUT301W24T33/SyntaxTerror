// Purpose:
//
//
// Issues:
//

package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.databinding.OrganizerEventSignedUpFragmentBinding;

import java.util.ArrayList;

public class EventSignedUp extends Fragment {
    private OrganizerEventSignedUpFragmentBinding binding;
    public static EventSignedUp newInstance(ArrayList<User> signedUpList) {
        Bundle args = new Bundle();
        args.putSerializable("signedUpList", signedUpList);
        EventSignedUp frag = new EventSignedUp();
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrganizerEventSignedUpFragmentBinding.inflate(inflater, container, false);
        setupActionBar();
        setupEventSignUpsRecyclerView();
        return binding.getRoot();
    }
    private void setupEventSignUpsRecyclerView() {
        assert getArguments() != null;
        ArrayList<User> signedUpList = (ArrayList<User>) getArguments().getSerializable("signedUpList");
        binding.eventSignupsList.setLayoutManager(new LinearLayoutManager(getContext()));
        AttendeeAdapter adapter = new AttendeeAdapter(signedUpList);
        binding.eventSignupsList.setAdapter(adapter);
        binding.signedupCount.setText(String.valueOf(signedUpList.size()));
    }
    private void setupActionBar() {
        TextView actionBarText = binding.actionbar.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Signed Up");

        int color = ContextCompat.getColor(getContext(), R.color.organizer_actionbar_day);

        ImageButton backButton = binding.actionbar.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}