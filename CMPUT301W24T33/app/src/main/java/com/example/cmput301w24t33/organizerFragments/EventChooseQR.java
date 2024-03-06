package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.adminFragments.DeleteEventAdmin;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventChooseQR#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventChooseQR extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button confirmButton;
    private RecyclerView eventView;
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private RadioGroup radioButton;
    private String selectedEvent;

    // TODO: Rename and change types of parameters
    private String param1;
    private String param2;


    /**
     * Listens to EventChooseQR fragment and updates depending on the results
     */
    public interface ChooseQRFragmentListener {
        /**
         * Sets the Listener's qrCode attribute to given qrCode string
         * @param qrCode qrCode to be set
         */
        public void setQRCode(String qrCode);
    }

    private ChooseQRFragmentListener listener;

    public EventChooseQR() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventChooseQR.
     */
    // TODO: Rename and change types and number of parameters
    public static EventChooseQR newInstance(String param1, String param2) {
        EventChooseQR fragment = new EventChooseQR();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        eventAdapter = new EventAdapter(eventList, null);
        radioButton = view.findViewById(R.id.choose_qr_radio_group);

        setOnClickListeners();
    }

    // TODO: Implement QR code reusability
    //  1: populate recycler view with organizer's past events
    //  2: set onItemClickListener to handle user input
    //  3: return selected event to parent fragment view listener.setQRCode(qrCode)
    //  4: profit $$$
    private void setOnClickListeners() {
        confirmButton.setOnClickListener(v->{
            int option = radioButton.getCheckedRadioButtonId() == R.id.new_qr_radio_button ? 1 : 2;
            if(option == 1) {
                listener.setQRCode(null);
            }
            getParentFragmentManager().popBackStack();
        });


    }

    /**
     * Attaches ChooseQRFragmentListener to this instance
     * @param listener object waiting for results
     */
    public void setListener(ChooseQRFragmentListener listener){
        this.listener = listener;
    }
}