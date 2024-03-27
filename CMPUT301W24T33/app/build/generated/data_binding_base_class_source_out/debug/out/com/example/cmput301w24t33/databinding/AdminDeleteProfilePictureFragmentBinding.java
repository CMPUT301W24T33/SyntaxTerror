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

public final class AdminDeleteProfilePictureFragmentBinding implements ViewBinding {
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
  public final RelativeLayout deleteProfilePictureActionbar;

  @NonNull
  public final TextInputEditText profileFirstNameEditText;

  @NonNull
  public final TextInputLayout profileFirstNameInputLayout;

  @NonNull
  public final TextInputEditText profileLastNameEditText;

  @NonNull
  public final TextInputLayout profileLastNameInputLayout;

  @NonNull
  public final ImageView profilePictureImageView;

  @NonNull
  public final LinearLayout userInformationLayout;

  private AdminDeleteProfilePictureFragmentBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView backActionbarTextview, @NonNull ImageButton backArrowImg,
      @NonNull Button cancelButton, @NonNull Button deleteButton,
      @NonNull RelativeLayout deleteProfilePictureActionbar,
      @NonNull TextInputEditText profileFirstNameEditText,
      @NonNull TextInputLayout profileFirstNameInputLayout,
      @NonNull TextInputEditText profileLastNameEditText,
      @NonNull TextInputLayout profileLastNameInputLayout,
      @NonNull ImageView profilePictureImageView, @NonNull LinearLayout userInformationLayout) {
    this.rootView = rootView;
    this.backActionbarTextview = backActionbarTextview;
    this.backArrowImg = backArrowImg;
    this.cancelButton = cancelButton;
    this.deleteButton = deleteButton;
    this.deleteProfilePictureActionbar = deleteProfilePictureActionbar;
    this.profileFirstNameEditText = profileFirstNameEditText;
    this.profileFirstNameInputLayout = profileFirstNameInputLayout;
    this.profileLastNameEditText = profileLastNameEditText;
    this.profileLastNameInputLayout = profileLastNameInputLayout;
    this.profilePictureImageView = profilePictureImageView;
    this.userInformationLayout = userInformationLayout;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AdminDeleteProfilePictureFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AdminDeleteProfilePictureFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.admin_delete_profile_picture_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AdminDeleteProfilePictureFragmentBinding bind(@NonNull View rootView) {
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

      id = R.id.delete_profile_picture_actionbar;
      RelativeLayout deleteProfilePictureActionbar = ViewBindings.findChildViewById(rootView, id);
      if (deleteProfilePictureActionbar == null) {
        break missingId;
      }

      id = R.id.profile_first_name_edit_text;
      TextInputEditText profileFirstNameEditText = ViewBindings.findChildViewById(rootView, id);
      if (profileFirstNameEditText == null) {
        break missingId;
      }

      id = R.id.profile_first_name_input_layout;
      TextInputLayout profileFirstNameInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (profileFirstNameInputLayout == null) {
        break missingId;
      }

      id = R.id.profile_last_name_edit_text;
      TextInputEditText profileLastNameEditText = ViewBindings.findChildViewById(rootView, id);
      if (profileLastNameEditText == null) {
        break missingId;
      }

      id = R.id.profile_last_name_input_layout;
      TextInputLayout profileLastNameInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (profileLastNameInputLayout == null) {
        break missingId;
      }

      id = R.id.profile_picture_image_view;
      ImageView profilePictureImageView = ViewBindings.findChildViewById(rootView, id);
      if (profilePictureImageView == null) {
        break missingId;
      }

      id = R.id.user_information_layout;
      LinearLayout userInformationLayout = ViewBindings.findChildViewById(rootView, id);
      if (userInformationLayout == null) {
        break missingId;
      }

      return new AdminDeleteProfilePictureFragmentBinding((ConstraintLayout) rootView,
          backActionbarTextview, backArrowImg, cancelButton, deleteButton,
          deleteProfilePictureActionbar, profileFirstNameEditText, profileFirstNameInputLayout,
          profileLastNameEditText, profileLastNameInputLayout, profilePictureImageView,
          userInformationLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
