// Generated by view binder compiler. Do not edit!
package com.example.cmput301w24t33.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cmput301w24t33.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ProfileCreateFragmentBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final LinearLayout createLayout;

  @NonNull
  public final EditText emailEditText;

  @NonNull
  public final LinearLayout emailLayout;

  @NonNull
  public final TextView emailTextView;

  @NonNull
  public final EditText firstNameEditText;

  @NonNull
  public final LinearLayout firstNameLayout;

  @NonNull
  public final TextView firstNameTextView;

  @NonNull
  public final EditText lastNameEditText;

  @NonNull
  public final LinearLayout lastNameLayout;

  @NonNull
  public final TextView lastNameTextView;

  @NonNull
  public final RelativeLayout profileBackActionbar;

  @NonNull
  public final RelativeLayout profileFragment;

  @NonNull
  public final ImageView profileImage;

  @NonNull
  public final AppCompatButton profileSaveButton;

  @NonNull
  public final CardView profileView;

  private ProfileCreateFragmentBinding(@NonNull RelativeLayout rootView,
      @NonNull LinearLayout createLayout, @NonNull EditText emailEditText,
      @NonNull LinearLayout emailLayout, @NonNull TextView emailTextView,
      @NonNull EditText firstNameEditText, @NonNull LinearLayout firstNameLayout,
      @NonNull TextView firstNameTextView, @NonNull EditText lastNameEditText,
      @NonNull LinearLayout lastNameLayout, @NonNull TextView lastNameTextView,
      @NonNull RelativeLayout profileBackActionbar, @NonNull RelativeLayout profileFragment,
      @NonNull ImageView profileImage, @NonNull AppCompatButton profileSaveButton,
      @NonNull CardView profileView) {
    this.rootView = rootView;
    this.createLayout = createLayout;
    this.emailEditText = emailEditText;
    this.emailLayout = emailLayout;
    this.emailTextView = emailTextView;
    this.firstNameEditText = firstNameEditText;
    this.firstNameLayout = firstNameLayout;
    this.firstNameTextView = firstNameTextView;
    this.lastNameEditText = lastNameEditText;
    this.lastNameLayout = lastNameLayout;
    this.lastNameTextView = lastNameTextView;
    this.profileBackActionbar = profileBackActionbar;
    this.profileFragment = profileFragment;
    this.profileImage = profileImage;
    this.profileSaveButton = profileSaveButton;
    this.profileView = profileView;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ProfileCreateFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ProfileCreateFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.profile_create_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ProfileCreateFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.create_layout;
      LinearLayout createLayout = ViewBindings.findChildViewById(rootView, id);
      if (createLayout == null) {
        break missingId;
      }

      id = R.id.email_edit_text;
      EditText emailEditText = ViewBindings.findChildViewById(rootView, id);
      if (emailEditText == null) {
        break missingId;
      }

      id = R.id.email_layout;
      LinearLayout emailLayout = ViewBindings.findChildViewById(rootView, id);
      if (emailLayout == null) {
        break missingId;
      }

      id = R.id.email_text_view;
      TextView emailTextView = ViewBindings.findChildViewById(rootView, id);
      if (emailTextView == null) {
        break missingId;
      }

      id = R.id.first_name_edit_text;
      EditText firstNameEditText = ViewBindings.findChildViewById(rootView, id);
      if (firstNameEditText == null) {
        break missingId;
      }

      id = R.id.first_name_layout;
      LinearLayout firstNameLayout = ViewBindings.findChildViewById(rootView, id);
      if (firstNameLayout == null) {
        break missingId;
      }

      id = R.id.first_name_text_view;
      TextView firstNameTextView = ViewBindings.findChildViewById(rootView, id);
      if (firstNameTextView == null) {
        break missingId;
      }

      id = R.id.last_name_edit_text;
      EditText lastNameEditText = ViewBindings.findChildViewById(rootView, id);
      if (lastNameEditText == null) {
        break missingId;
      }

      id = R.id.last_name_layout;
      LinearLayout lastNameLayout = ViewBindings.findChildViewById(rootView, id);
      if (lastNameLayout == null) {
        break missingId;
      }

      id = R.id.last_name_text_view;
      TextView lastNameTextView = ViewBindings.findChildViewById(rootView, id);
      if (lastNameTextView == null) {
        break missingId;
      }

      id = R.id.profile_back_actionbar;
      RelativeLayout profileBackActionbar = ViewBindings.findChildViewById(rootView, id);
      if (profileBackActionbar == null) {
        break missingId;
      }

      RelativeLayout profileFragment = (RelativeLayout) rootView;

      id = R.id.profile_image;
      ImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      id = R.id.profile_save_button;
      AppCompatButton profileSaveButton = ViewBindings.findChildViewById(rootView, id);
      if (profileSaveButton == null) {
        break missingId;
      }

      id = R.id.profile_view;
      CardView profileView = ViewBindings.findChildViewById(rootView, id);
      if (profileView == null) {
        break missingId;
      }

      return new ProfileCreateFragmentBinding((RelativeLayout) rootView, createLayout,
          emailEditText, emailLayout, emailTextView, firstNameEditText, firstNameLayout,
          firstNameTextView, lastNameEditText, lastNameLayout, lastNameTextView,
          profileBackActionbar, profileFragment, profileImage, profileSaveButton, profileView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
