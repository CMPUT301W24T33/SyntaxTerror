// Generated by view binder compiler. Do not edit!
package com.example.cmput301w24t33.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cmput301w24t33.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class OrganizerActivityBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ActionbarAttendeeOrganizerBinding attendeeOrganizer;

  @NonNull
  public final FloatingActionButton buttonCreateEvent;

  @NonNull
  public final RecyclerView organizedEvents;

  @NonNull
  public final ConstraintLayout organizerActivity;

  @NonNull
  public final FrameLayout organizerLayout;

  private OrganizerActivityBinding(@NonNull ConstraintLayout rootView,
      @NonNull ActionbarAttendeeOrganizerBinding attendeeOrganizer,
      @NonNull FloatingActionButton buttonCreateEvent, @NonNull RecyclerView organizedEvents,
      @NonNull ConstraintLayout organizerActivity, @NonNull FrameLayout organizerLayout) {
    this.rootView = rootView;
    this.attendeeOrganizer = attendeeOrganizer;
    this.buttonCreateEvent = buttonCreateEvent;
    this.organizedEvents = organizedEvents;
    this.organizerActivity = organizerActivity;
    this.organizerLayout = organizerLayout;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static OrganizerActivityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static OrganizerActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.organizer_activity, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static OrganizerActivityBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.attendee_organizer;
      View attendeeOrganizer = ViewBindings.findChildViewById(rootView, id);
      if (attendeeOrganizer == null) {
        break missingId;
      }
      ActionbarAttendeeOrganizerBinding binding_attendeeOrganizer = ActionbarAttendeeOrganizerBinding.bind(attendeeOrganizer);

      id = R.id.button_create_event;
      FloatingActionButton buttonCreateEvent = ViewBindings.findChildViewById(rootView, id);
      if (buttonCreateEvent == null) {
        break missingId;
      }

      id = R.id.organized_events;
      RecyclerView organizedEvents = ViewBindings.findChildViewById(rootView, id);
      if (organizedEvents == null) {
        break missingId;
      }

      ConstraintLayout organizerActivity = (ConstraintLayout) rootView;

      id = R.id.organizer_layout;
      FrameLayout organizerLayout = ViewBindings.findChildViewById(rootView, id);
      if (organizerLayout == null) {
        break missingId;
      }

      return new OrganizerActivityBinding((ConstraintLayout) rootView, binding_attendeeOrganizer,
          buttonCreateEvent, organizedEvents, organizerActivity, organizerLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
