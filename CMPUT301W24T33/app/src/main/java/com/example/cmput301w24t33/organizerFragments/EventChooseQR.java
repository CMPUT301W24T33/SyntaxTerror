// Purpose:
// Provides an interface for selecting an existing event to generate or reuse a QR code
//
// Issues: Populate the recyclerview with reusable qr codes
//

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
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;

import java.util.ArrayList;

/**
 * A fragment for choosing an event to generate or reuse a QR code.
 * Allows users to select from existing events or create a new QR code.
 */
public class EventChooseQR extends Fragment {
    private Button confirmButton;
    private RecyclerView eventView;
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private RadioGroup radioButton;
    private String selectedEvent;
    private ChooseQRFragmentListener listener;

    /**
     * Interface for communication with fragments or activities that host this fragment.
     */
    public interface ChooseQRFragmentListener {
        /**
         * Invoked when a QR code is selected or generated.
         * @param qrCode The QR code that was selected or generated.
         */
        void setQRCode(String qrCode);
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
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, null);
        radioButton = view.findViewById(R.id.choose_qr_radio_group);

        setOnClickListeners();
    }

    /**
     * Sets onClick listeners for the UI elements in the fragment.
     */
    private void setOnClickListeners() {
        confirmButton.setOnClickListener(v -> {
            int option = radioButton.getCheckedRadioButtonId() == R.id.new_qr_radio_button ? 1 : 2;
            if(option == 1) {
                listener.setQRCode(null);
            }
            getParentFragmentManager().popBackStack();
        });
    }

    /**
     * Attaches a listener for fragment events.
     * @param listener The listener to attach.
     */
    public void setListener(ChooseQRFragmentListener listener){
        this.listener = listener;
    }
}
