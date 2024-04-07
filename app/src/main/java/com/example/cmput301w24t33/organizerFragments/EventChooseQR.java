// Purpose:
// Provides an interface for selecting an existing event to generate or reuse a QR code
//
// Issues: Populate the recyclerview with reusable qr codes
//

package com.example.cmput301w24t33.organizerFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.databinding.OrganizerChooseQrFragmentBinding;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventRepository;
import com.example.cmput301w24t33.events.EventViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment for choosing an event to generate or reuse a QR code.
 * Allows users to select from existing events or create a new QR code.
 */
public class EventChooseQR extends Fragment implements EventRepository.EventCallback, EventAdapter.AdapterEventClickListener {
    private Button confirmButton;
    private RecyclerView eventView;
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private String userId;
    private RadioGroup radioButton;
    private RadioButton selectPreviousButton;
    private RadioButton createNewButton;
    private TextView selectedEventView;
    private Event selectedEvent;
    private ChooseQRFragmentListener listener;
    private EventRepository eventRepo = EventRepository.getInstance();

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


    public EventChooseQR(String userId){
        this.userId = userId;
        Log.d("ChoseQR", "organizer id: "+userId);
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
        eventRepo.setEventCallback(this);
        eventRepo.setEventByOrganizerSnapshotListener(userId);
        eventView.setVisibility(View.GONE);
        getView().findViewById(R.id.selected_event_text).setVisibility(View.GONE);

        radioButton = view.findViewById(R.id.choose_qr_radio_group);
        selectPreviousButton = view.findViewById(R.id.reuse_qr_radio_button);
        createNewButton = view.findViewById(R.id.new_qr_radio_button);
        selectedEventView = view.findViewById(R.id.selected_event);
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
            } else {
                if(selectedEvent != null) {
                    listener.setQRCode(selectedEvent.getCheckInQR());
                    selectedEvent.setCheckInQR(null);
                    eventRepo.updateEvent(selectedEvent);
                }
            }
            EventViewModel.getInstance().restoreEventCallback();
            getParentFragmentManager().popBackStack();
        });

        selectPreviousButton.setOnClickListener(v->{
            eventView.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.selected_event_text).setVisibility(View.VISIBLE);
            selectedEventView.setVisibility(View.VISIBLE);
        });

        createNewButton.setOnClickListener(v->{
            eventView.setVisibility(View.GONE);
            getView().findViewById(R.id.selected_event_text).setVisibility(View.GONE);
            selectedEventView.setVisibility(View.GONE);
        });
    }

    /**
     * Attaches a listener for fragment events.
     * @param listener The listener to attach.
     */
    public void setListener(ChooseQRFragmentListener listener){
        this.listener = listener;
    }

    /**
     * Populates RecyclerView with loaded events.
     * @param events events belonging to the user.
     */
    @Override
    public void onEventsLoaded(List<Event> events) {

        Log.d("EventCallback", "Events loaded: "+ (events.size()>0 ? events.get(0).getName():""));
        eventList = new ArrayList<>();
        for(Event event: events){
            if (event.getEndDateTIme().compareTo(Timestamp.now()) < 0){
                eventList.add(event);
            }
        }
        eventAdapter = new EventAdapter(eventList, this);
        eventView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventView.setHasFixedSize(true);
        eventView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Handles event loading failure.
     * @param e Exception resulting from attempt to load events.
     */
    @Override
    public void onFailure(Exception e) {
        Log.e("EventCallback", "failed to retrieve events: " + e.getMessage());
        e.printStackTrace();
    }


    @Override
    public void onEventClickListener(Event event, int position) {
        selectedEvent = event;
        selectedEventView.setText(event.getName());
    }

}
