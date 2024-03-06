package com.example.cmput301w24t33.adminFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import static android.content.ContentValues.TAG;
import static android.opengl.Matrix.length;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.AdapterEventClickListener;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.organizerFragments.EventCreateEdit;

import java.util.ArrayList;
import java.util.List;

public class ViewEventsAdmin extends Fragment implements AdapterEventClickListener, View.OnTouchListener {
    private ArrayList<Event> eventList;
    private EventAdapter eventAdapter;
    private EventViewModel eventViewModel;
    private RecyclerView eventRecyclerView;

    public ViewEventsAdmin(){}

    public static ViewEventsAdmin newInstance() {
        ViewEventsAdmin fragment = new ViewEventsAdmin();
        Bundle args = new Bundle();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_view_events_fragment, container, false);
        setupClickListeners(view);
        setupActionBar(view);
        displayEvents(view);
        return view;

    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "VIEW EVENTS ADMIN FRAG RESUME");
        eventViewModel.loadEvents();
    }

    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.general_actionbar_textview);
        actionBarText.setText("All Events");

        CardView profileButton = view.findViewById(R.id.profile_button);
        profileButton.setVisibility(View.GONE);

        RelativeLayout generalActionBar = view.findViewById(R.id.general_actionbar);
        int color = ContextCompat.getColor(getContext(),R.color.admin_actionbar);
        generalActionBar.setBackgroundColor(color);
    }

    private void setupClickListeners(View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
    /**
     * Updates event adapter with current contents in our events collection
     * @param events is a live representation of Events in our events collection as a List
     */
    private void updateUI(List<Event> events) {
        Log.d(TAG, String.valueOf(events.size()));
        eventAdapter.setEvents(events);
    }

    private void setAdapter(){
        Context context = getContext();
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventRecyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(eventList,this, context);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }
    private void displayEvents(View view) {
        Log.d(TAG, "EVENTS FRAGMENT: DISPLAY EVENTS");
        eventRecyclerView = view.findViewById(R.id.event_recyclerview);

        eventList = new ArrayList<>();
        setAdapter();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), events -> {
            updateUI(events);
        });
    }

    @Override
    public void onEventClickListener(Event event, int position) {
        replaceFragment(DeleteEventAdmin.newInstance(event));
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.admin_events_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
