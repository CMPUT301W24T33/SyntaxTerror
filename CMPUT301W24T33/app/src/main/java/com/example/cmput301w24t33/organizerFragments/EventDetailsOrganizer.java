package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.users.Profile;

public class EventDetailsOrganizer extends Fragment {

//    private String mParam1;
//    private String mParam2;

    public EventDetailsOrganizer() {
        // Required empty public constructor
    }

    public static EventDetailsOrganizer newInstance(String param1, String param2) {
        EventDetailsOrganizer fragment = new EventDetailsOrganizer();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.organizer_event_fragment,null);
        setOnClickListeners(view);
        setupActionBar(view);
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.organizer_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("Event Details");
    }

    private void setOnClickListeners(View view){
        ImageButton shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> {
            // set share functionality here
        });

        ImageButton notificationButton = view.findViewById(R.id.notifications_button);
        notificationButton.setOnClickListener(v -> replaceFragment(new NotificationsOrganizer()));

        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        ImageView profileButton = view.findViewById(R.id.profile_image);
        profileButton.setOnClickListener(v -> replaceFragment(new Profile()));

        ImageButton attendeesButton = view.findViewById(R.id.attendees_Button);
        attendeesButton.setOnClickListener(v -> replaceFragment(new EventAttendees()));
    }
}
