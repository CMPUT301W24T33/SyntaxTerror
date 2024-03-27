// Generated by view binder compiler. Do not edit!
package com.example.cmput301w24t33.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cmput301w24t33.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AdminDeletePosterFragmentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView backActionbarTextview;

  @NonNull
  public final ImageButton backArrowImg;

  @NonNull
  public final Button cancelButton;

  @NonNull
  public final Button deleteButton;

  @NonNull
  public final RelativeLayout editEventActionbar;

  @NonNull
  public final LinearLayout eventInformationLayout;

  @NonNull
  public final TextInputEditText eventNameEditText;

  @NonNull
  public final TextInputLayout eventNameInputLayout;

  @NonNull
  public final ImageView eventPosterImageView;

  private AdminDeletePosterFragmentBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView backActionbarTextview, @NonNull ImageButton backArrowImg,
      @NonNull Button cancelButton, @NonNull Button deleteButton,
      @NonNull RelativeLayout editEventActionbar, @NonNull LinearLayout eventInformationLayout,
      @NonNull TextInputEditText eventNameEditText, @NonNull TextInputLayout eventNameInputLayout,
      @NonNull ImageView eventPosterImageView) {
    this.rootView = rootView;
    this.backActionbarTextview = backActionbarTextview;
    this.backArrowImg = backArrowImg;
    this.cancelButton = cancelButton;
    this.deleteButton = deleteButton;
    this.editEventActionbar = editEventActionbar;
    this.eventInformationLayout = eventInformationLayout;
    this.eventNameEditText = eventNameEditText;
    this.eventNameInputLayout = eventNameInputLayout;
    this.eventPosterImageView = eventPosterImageView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AdminDeletePosterFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AdminDeletePosterFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.admin_delete_poster_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AdminDeletePosterFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.back_actionbar_textview;
      TextView backActionbarTextview = ViewBindings.findChildViewById(rootView, id);
      if (backActionbarTextview == null) {
        break missingId;
      }

      id = R.id.back_arrow_img;
      ImageButton backArrowImg = ViewBindings.findChildViewById(rootView, id);
      if (backArrowImg == null) {
        break missingId;
      }

      id = R.id.cancel_button;
      Button cancelButton = ViewBindings.findChildViewById(rootView, id);
      if (cancelButton == null) {
        break missingId;
      }

      id = R.id.delete_button;
      Button deleteButton = ViewBindings.findChildViewById(rootView, id);
      if (deleteButton == null) {
        break missingId;
      }

      id = R.id.edit_event_actionbar;
      RelativeLayout editEventActionbar = ViewBindings.findChildViewById(rootView, id);
      if (editEventActionbar == null) {
        break missingId;
      }

      id = R.id.event_information_layout;
      LinearLayout eventInformationLayout = ViewBindings.findChildViewById(rootView, id);
      if (eventInformationLayout == null) {
        break missingId;
      }

      id = R.id.event_name_edit_text;
      TextInputEditText eventNameEditText = ViewBindings.findChildViewById(rootView, id);
      if (eventNameEditText == null) {
        break missingId;
      }

      id = R.id.event_name_input_layout;
      TextInputLayout eventNameInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (eventNameInputLayout == null) {
        break missingId;
      }

      id = R.id.event_poster_image_view;
      ImageView eventPosterImageView = ViewBindings.findChildViewById(rootView, id);
      if (eventPosterImageView == null) {
        break missingId;
      }

      return new AdminDeletePosterFragmentBinding((ConstraintLayout) rootView,
          backActionbarTextview, backArrowImg, cancelButton, deleteButton, editEventActionbar,
          eventInformationLayout, eventNameEditText, eventNameInputLayout, eventPosterImageView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
