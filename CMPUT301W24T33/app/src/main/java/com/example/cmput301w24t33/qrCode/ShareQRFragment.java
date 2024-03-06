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

public class ShareQRFragment extends DialogFragment {
    private QRCode checkInCode;
    private final String downloadPath = "/storage/emulated/0/Download";
    private RadioGroup radioGroup;
    private ImageView qrView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.organizer_share_qr_fragment, null);
        assert getArguments() != null;
        checkInCode = (QRCode) getArguments().get("CHECKIN");
        assert checkInCode != null;
        qrView = view.findViewById(R.id.qr_code_image_view);
        qrView.setImageBitmap(checkInCode.getBitmap());
        radioGroup = view.findViewById(R.id.share_qr_code_select_option);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Share QR Code")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (d,w)->{
                    if(radioGroup.getCheckedRadioButtonId() == R.id.save_check_in_code) {
                        checkInCode.saveAsPNG(downloadPath);
                        Toast toast = new Toast(getContext());
                        toast.setText("QR Code Saved");
                        toast.show();
                    }
                } ).create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public static ShareQRFragment newInstance(QRCode checkInCode, QRCode eventQRCode) {
        ShareQRFragment shareQRFragment = new ShareQRFragment();
        Bundle args = new Bundle();
        args.putSerializable("CHECKIN", checkInCode);
        shareQRFragment.setArguments(args);
        return shareQRFragment;
    }


}

