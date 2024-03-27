// Generated by view binder compiler. Do not edit!
package com.example.cmput301w24t33.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cmput301w24t33.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AttendeeEventFragmentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView eventDescriptionTextView;

  @NonNull
  public final TextView eventLocationTextView;

  @NonNull
  public final TextView eventNameTextView;

  @NonNull
  public final ImageView eventPosterImageView;

  @NonNull
  public final TextView eventStartEndDateTimeTextView;

  @NonNull
  public final MaterialButton goingButton;

  @NonNull
  public final MaterialButton notGoingButton;

  @NonNull
  public final MaterialButton notificationsButton;

  @NonNull
  public final ImageButton shareQrCodeButton;

  @NonNull
  public final MaterialButtonToggleGroup toggleButtonGroup;

  @NonNull
  public final MaterialToolbar toolbar;

  private AttendeeEventFragmentBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView eventDescriptionTextView, @NonNull TextView eventLocationTextView,
      @NonNull TextView eventNameTextView, @NonNull ImageView eventPosterImageView,
      @NonNull TextView eventStartEndDateTimeTextView, @NonNull MaterialButton goingButton,
      @NonNull MaterialButton notGoingButton, @NonNull MaterialButton notificationsButton,
      @NonNull ImageButton shareQrCodeButton, @NonNull MaterialButtonToggleGroup toggleButtonGroup,
      @NonNull MaterialToolbar toolbar) {
    this.rootView = rootView;
    this.eventDescriptionTextView = eventDescriptionTextView;
    this.eventLocationTextView = eventLocationTextView;
    this.eventNameTextView = eventNameTextView;
    this.eventPosterImageView = eventPosterImageView;
    this.eventStartEndDateTimeTextView = eventStartEndDateTimeTextView;
    this.goingButton = goingButton;
    this.notGoingButton = notGoingButton;
    this.notificationsButton = notificationsButton;
    this.shareQrCodeButton = shareQrCodeButton;
    this.toggleButtonGroup = toggleButtonGroup;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AttendeeEventFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AttendeeEventFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.attendee_event_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AttendeeEventFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.event_description_text_view;
      TextView eventDescriptionTextView = ViewBindings.findChildViewById(rootView, id);
      if (eventDescriptionTextView == null) {
        break missingId;
      }

      id = R.id.event_location_text_view;
      TextView eventLocationTextView = ViewBindings.findChildViewById(rootView, id);
      if (eventLocationTextView == null) {
        break missingId;
      }

      id = R.id.event_name_text_view;
      TextView eventNameTextView = ViewBindings.findChildViewById(rootView, id);
      if (eventNameTextView == null) {
        break missingId;
      }

      id = R.id.event_poster_image_view;
      ImageView eventPosterImageView = ViewBindings.findChildViewById(rootView, id);
      if (eventPosterImageView == null) {
        break missingId;
      }

      id = R.id.event_start_end_date_time_text_view;
      TextView eventStartEndDateTimeTextView = ViewBindings.findChildViewById(rootView, id);
      if (eventStartEndDateTimeTextView == null) {
        break missingId;
      }

      id = R.id.goingButton;
      MaterialButton goingButton = ViewBindings.findChildViewById(rootView, id);
      if (goingButton == null) {
        break missingId;
      }

      id = R.id.notGoingButton;
      MaterialButton notGoingButton = ViewBindings.findChildViewById(rootView, id);
      if (notGoingButton == null) {
        break missingId;
      }

      id = R.id.notifications_button;
      MaterialButton notificationsButton = ViewBindings.findChildViewById(rootView, id);
      if (notificationsButton == null) {
        break missingId;
      }

      id = R.id.share_qr_code_button;
      ImageButton shareQrCodeButton = ViewBindings.findChildViewById(rootView, id);
      if (shareQrCodeButton == null) {
        break missingId;
      }

      id = R.id.toggleButtonGroup;
      MaterialButtonToggleGroup toggleButtonGroup = ViewBindings.findChildViewById(rootView, id);
      if (toggleButtonGroup == null) {
        break missingId;
      }

      id = R.id.toolbar;
      MaterialToolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new AttendeeEventFragmentBinding((ConstraintLayout) rootView, eventDescriptionTextView,
          eventLocationTextView, eventNameTextView, eventPosterImageView,
          eventStartEndDateTimeTextView, goingButton, notGoingButton, notificationsButton,
          shareQrCodeButton, toggleButtonGroup, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
