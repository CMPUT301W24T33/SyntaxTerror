// Purpose:
// Represents a dialog fragment used to select a desired QR code type to share with other apps.
// It allows the user to choose from different QR codes (e.g., check-in code, event code, poster code)
// and shares the selected QR code back to the parent fragment through a listener interface.
//
// Issues: None
//


package com.example.cmput301w24t33.qrCode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.Event;

import java.io.Serializable;

/**
 * Used to select a desired QR code type to share with other apps
 */
public class ShareQRFragment extends DialogFragment {
    private String checkInCode;
    private String posterCode;
    private String downloadPath;
    private RadioGroup radioGroup;
    private ShareQRDialogListener listener;

    /**
     * Provides Contract between parent fragment and ShareQRFragment to Share a selected QR code
     */
    public interface ShareQRDialogListener {
        void ShareSelectedQRCode(QRCode qrCode);
    }

    /**
     * Creates and returns a dialog for selecting a QR code type to share. Initializes the dialog with options
     * for sharing check-in, event, or poster QR codes and sets up the positive and negative buttons with appropriate actions.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     * @return An AlertDialog object ready to be displayed.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // TODO: Sharing events whose check in code has been reused will cause issues if we use the
        //      same QR code for both. It would be better (and easier) to just have 3 separate QR
        //      codes for all fields. To do this you need to:
        //          1: Add an event qr code to this class (use eventId)
        //          2: Update the find event functionality in Attendee to query for event ID instead of check In code
        View view = LayoutInflater.from(getContext()).inflate(R.layout.organizer_share_qr_fragment, null);

        // gets data from bundle
        assert getArguments() != null;
        checkInCode = (String) getArguments().get("CHECKIN");
        posterCode = (String) getArguments().get("POSTERQR");
        listener = (ShareQRDialogListener) getArguments().get("LISTENER");


        downloadPath = checkInCode;
        radioGroup = view.findViewById(R.id.share_qr_code_select_option);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Share QR Code")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (d,w)->{

                    // determines which code to share and passes it back to listener
                    if(radioGroup.getCheckedRadioButtonId() == R.id.share_check_in_code) {
                        listener.ShareSelectedQRCode(new QRCode(checkInCode));
                    } else if (radioGroup.getCheckedRadioButtonId() == R.id.share_event_code) {
                        listener.ShareSelectedQRCode(new QRCode(checkInCode));
                    } else {
                        listener.ShareSelectedQRCode(new QRCode(posterCode));
                    }

                } ).create();
    }

    /**
     * Attaches the fragment to its context, ensuring that it can communicate with the parent fragment or activity.
     * This method was originally intended to establish the ShareQRDialogListener interface with the context.
     *
     * @param context The context in which the fragment is being attached.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        listener = (ShareQRDialogListener) context;
    }

    /**
     * Creates a new Instance of ShareQRFragment with given arguments
     * @param event selected Event to share details of
     * @param listener ShareQRFragment dialogue listener
     * @return new ShareQRFragment instance
     */
    public static ShareQRFragment newInstance(Event event, Serializable listener) {
        ShareQRFragment shareQRFragment = new ShareQRFragment();
        Bundle args = new Bundle();

        args.putSerializable("CHECKIN", event.getCheckInQR());
        args.putSerializable("EVENTQR", event.getCheckInQR());
        args.putSerializable("POSTERQR", event.getImageUrl());
        args.putSerializable("LISTENER", listener);
        shareQRFragment.setArguments(args);
        return shareQRFragment;
    }


}

