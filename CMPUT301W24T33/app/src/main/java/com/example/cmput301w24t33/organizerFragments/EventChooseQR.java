package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventChooseQR#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventChooseQR extends Fragment implements AdapterEventClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EVENT_ID = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button confirmButton;
    private RecyclerView eventView;
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private RadioGroup radioButton;
    private String selectedEvent;

    // TODO: Rename and change types of parameters
    private String organizerId;
    private String eventId;
    private String mParam2;

    public EventChooseQR() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventId Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventChooseQR.
     */
    // TODO: Rename and change types and number of parameters
    public static EventChooseQR newInstance(String eventId, String param2) {
        EventChooseQR fragment = new EventChooseQR();
        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventId);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(EVENT_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        organizerId = mAuth.getUid();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.organizer_choose_qr_fragment, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();

        confirmButton = view.findViewById(R.id.button_choose_qr_confirm);
        eventView = view.findViewById(R.id.event_recyclerview);
        eventList = new ArrayList<Event>();
        eventAdapter = new EventAdapter(eventList, this, getContext());
        radioButton = view.findViewById(R.id.choose_qr_radio_group);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        confirmButton.setOnClickListener(v->{
            int option = radioButton.getCheckedRadioButtonId() == R.id.new_qr_radio_button ? 1 : 2;
            if(option == 1) {
                HashMap<String, String> data = new HashMap<>();
                data.put("organizerId", organizerId);
                data.put("eventId", eventId);
                db.collection("checkInCodes").document().set(data);
            }
            getParentFragmentManager().popBackStack();
        });
    }

    @Override
    public void onEventClickListener(Event event, int position) {

    }
}