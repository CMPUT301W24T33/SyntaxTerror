// Purpose:
// Represents a dialog fragment used to select a desired QR code type to share with other apps.
// It allows the user to choose from different QR codes (e.g., check-in code, event code, poster code)
// and shares the selected QR code back to the parent fragment through a listener interface.
//
// Issues:
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cmput301w24t33.R;

import java.io.Serializable;

/**
 * Used to select a desired QR code type to share with other apps
 */
public class ShareQRFragment extends DialogFragment {
    private QRCode checkInCode;
    private QRCode eventCode;
    private QRCode posterCode;
    private String downloadPath;
    private RadioGroup radioGroup;
    private ImageView qrView;
    private ShareQRDialogListener listener;

    /**
     * Provides Contract between parent fragment and ShareQRFragment to Share a selected QR code
     */
    public interface ShareQRDialogListener {
        public void ShareSelectedQRCode(QRCode qrCode);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.organizer_share_qr_fragment, null);

        // gets data from bundle
        assert getArguments() != null;
        checkInCode = (QRCode) getArguments().get("CHECKIN");
        eventCode = (QRCode) getArguments().get("EVENTQR");
        posterCode = (QRCode) getArguments().get("POSTERQR");
        listener = (ShareQRDialogListener) getArguments().get("LISTENER");

        assert checkInCode != null;

        downloadPath = checkInCode.getQrCode();
        radioGroup = view.findViewById(R.id.share_qr_code_select_option);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Share QR Code")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (d,w)->{

                    // determines which code to share and passes it back to listener
                    if(radioGroup.getCheckedRadioButtonId() == R.id.share_check_in_code) {
                        listener.ShareSelectedQRCode(checkInCode);
                    } else if (radioGroup.getCheckedRadioButtonId() == R.id.share_event_code) {
                        listener.ShareSelectedQRCode(eventCode);
                    } else {
                        listener.ShareSelectedQRCode(posterCode);
                    }

                } ).create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        listener = (ShareQRDialogListener) context;
    }

    /**
     * Creates a new Instance of ShareQRFragment with given arguments
     * @param checkInCode QR code for attendee event check in
     * @param eventQRCode QR code for linking to event in app
     * @param posterQRCode QR code for sharing poster with outside apps
     * @param listener ShareQRFragment dialogue listener
     * @return new ShareQRFragment instance
     */
    public static ShareQRFragment newInstance(QRCode checkInCode, QRCode eventQRCode, QRCode posterQRCode, Serializable listener) {
        ShareQRFragment shareQRFragment = new ShareQRFragment();
        Bundle args = new Bundle();
        args.putSerializable("CHECKIN", checkInCode);
        args.putSerializable("EVENTQR", eventQRCode);
        args.putSerializable("POSTERQR", posterQRCode);
        args.putSerializable("LISTENER", listener);
        shareQRFragment.setArguments(args);
        return shareQRFragment;
    }


}

