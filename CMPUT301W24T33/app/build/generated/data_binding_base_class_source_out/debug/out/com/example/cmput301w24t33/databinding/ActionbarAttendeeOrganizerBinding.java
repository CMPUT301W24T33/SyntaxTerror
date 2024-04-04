// Generated by view binder compiler. Do not edit!
package com.example.cmput301w24t33.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cmput301w24t33.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActionbarAttendeeOrganizerBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView attendeeOrganizerTextview;

  @NonNull
  public final ImageButton buttonUserMode;

  @NonNull
  public final RelativeLayout organizerAttendeeActionbar;

  @NonNull
  public final ImageView profileImage;

  private ActionbarAttendeeOrganizerBinding(@NonNull RelativeLayout rootView,
      @NonNull TextView attendeeOrganizerTextview, @NonNull ImageButton buttonUserMode,
      @NonNull RelativeLayout organizerAttendeeActionbar, @NonNull ImageView profileImage) {
    this.rootView = rootView;
    this.attendeeOrganizerTextview = attendeeOrganizerTextview;
    this.buttonUserMode = buttonUserMode;
    this.organizerAttendeeActionbar = organizerAttendeeActionbar;
    this.profileImage = profileImage;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActionbarAttendeeOrganizerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActionbarAttendeeOrganizerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.actionbar_attendee_organizer, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActionbarAttendeeOrganizerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.attendee_organizer_textview;
      TextView attendeeOrganizerTextview = ViewBindings.findChildViewById(rootView, id);
      if (attendeeOrganizerTextview == null) {
        break missingId;
      }

      id = R.id.button_user_mode;
      ImageButton buttonUserMode = ViewBindings.findChildViewById(rootView, id);
      if (buttonUserMode == null) {
        break missingId;
      }

      id = R.id.organizer_attendee_actionbar;
      RelativeLayout organizerAttendeeActionbar = ViewBindings.findChildViewById(rootView, id);
      if (organizerAttendeeActionbar == null) {
        break missingId;
      }

      id = R.id.profile_image;
      ImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      return new ActionbarAttendeeOrganizerBinding((RelativeLayout) rootView,
          attendeeOrganizerTextview, buttonUserMode, organizerAttendeeActionbar, profileImage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}