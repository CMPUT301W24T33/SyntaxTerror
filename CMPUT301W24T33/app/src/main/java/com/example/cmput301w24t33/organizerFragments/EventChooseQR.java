// Purpose:
// Provides an interface for selecting an existing event to generate or reuse a QR code
//
// Issues: Populate the recyclerview with reusable qr codes
//

package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    /**
     * Initializes the fragment. Called when the fragment is first created.
     * Use this method for any one-time initializations and retrieving passed arguments.
     *
     * @param savedInstanceState Contains data supplied in onSaveInstanceState(Bundle) if the fragment is being recreated.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method inflates the layout for this fragment,
     * initializing the UI components that allow an organizer to choose an event for QR code generation or to opt for creating a new QR code.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.organizer_choose_qr_fragment, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored into the view.
     * This method is used to finalize the fragment's UI initialization by setting up RecyclerView for event selection and the onClickListeners for UI interaction.
     */
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
