package com.example.cmput301w24t33.adminFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cmput301w24t33.R;
import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.events.EventAdapter;
import com.example.cmput301w24t33.events.EventViewModel;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.example.cmput301w24t33.users.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ViewImagesAdmin extends Fragment {

    private RecyclerView eventPosterRecyclerView;
    private RecyclerView userImageRecyclerView;
    private EventPosterAdapter eventPosterAdapter;
    private UserImageAdapter userImageAdapter;
    private ArrayList<Event> eventList;
    private ArrayList<User> userList = new ArrayList<>();
    private UserViewModel userViewModel;
    private EventViewModel eventViewModel;
    private View inflatedView;
    private boolean showEventPosters = true;


    public ViewImagesAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_view_images_fragment, container, false);
        inflatedView = view;
        userViewModel = UserViewModel.getInstance();
        eventViewModel = EventViewModel.getInstance();
        setupActionBar(view);
        setupClickListeners(view);
        displayEvents(view);
        return view;
    }


    /**
     * Callback method to be invoked when an event in the RecyclerView is clicked. It navigates to the DeleteEventAdmin fragment for the selected event.
     *
     * @param event The Event object associated with the clicked item.
     * @param position The position of the clicked item in the adapter.
     */
    public void onEventClickListener(Event event, int position) {
        RecyclerView.ViewHolder viewHolder = eventPosterRecyclerView.findViewHolderForAdapterPosition(position);
        View view = viewHolder.itemView;
        if(event.getImageUrl() != null) {   // there is an image in poster imageview, get it and pass forward
            replaceFragment(DeletePosterAdmin.newInstance(event));
        }
        else{
            Snackbar.make(view, "Event has no poster to delete", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets up the action bar for the fragment, including setting the title and hiding unnecessary buttons.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupActionBar(View view) {
        TextView actionBarText = view.findViewById(R.id.back_actionbar_textview);
        actionBarText.setText("Browse Images");

        RelativeLayout generalActionBar = view.findViewById(R.id.edit_profile_actionbar);
        int color = ContextCompat.getColor(getContext(), R.color.admin_actionbar);
        generalActionBar.setBackgroundColor(color);
    }

    /**
     * Sets up click listeners for the fragment, such as the back button.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void setupClickListeners(View view) {
        ImageButton backButton = view.findViewById(R.id.back_arrow_img);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        MaterialButton switchRecyclerviewButton = view.findViewById(R.id.switch_imagetype_button);
        switchRecyclerviewButton.setOnClickListener(v -> {
            ChangeRecyclerview();
        });
    }

    /**
     * Toggles between event and profile image recyclerviews
     *
     */
    private void ChangeRecyclerview() {
        showEventPosters = !showEventPosters;
        TextView button = inflatedView.findViewById(R.id.switch_imagetype_button);
        button.setText((showEventPosters) ? "Browse User Images" : "Browse Event Posters");
        // set event recyclerview visibility
        eventPosterRecyclerView.setVisibility((showEventPosters) ? View.VISIBLE:View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            eventPosterRecyclerView.setFocusable((showEventPosters) ? View.FOCUSABLE:View.NOT_FOCUSABLE);
        }
        eventPosterRecyclerView.setClickable((showEventPosters) ? true:false);
        // set user recyclerview visibility
        userImageRecyclerView.setVisibility((!showEventPosters) ? View.VISIBLE:View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            userImageRecyclerView.setFocusable((!showEventPosters) ? View.FOCUSABLE:View.NOT_FOCUSABLE);
        }
        userImageRecyclerView.setClickable((!showEventPosters) ? true:false);

    }


    /**
     * Updates the event adapter with the current list of events.
     *
     * @param events List of events to display.
     */
    private void updateEventUI(List<Event> events) {
        eventPosterAdapter.setEvents(events);
    }

    /**
     * Updates the UI by refreshing the list of user profiles shown.
     *
     * @param users The updated list of users.
     */
    private void updateUserUI(List<User> users) {
        userImageAdapter.setUsers(users);
    }

    /**
     * Refreshes the events list from the ViewModel when the fragment resumes, ensuring the data displayed is up-to-date.
     */
    @Override
    public void onResume() {
        super.onResume();
        eventViewModel.loadEvents();
        userViewModel.loadUsers();
    }

    /**
     * Initializes the event adapter and sets it on the RecyclerView.
     */

    private void setAdapter() {
        Context context = getContext();
        eventPosterRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventPosterRecyclerView.setHasFixedSize(true);
        eventPosterAdapter = new EventPosterAdapter(eventList, this::onEventClickListener,this);
        eventPosterRecyclerView.setAdapter(eventPosterAdapter);

        userImageRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        userImageRecyclerView.setHasFixedSize(true);
        userImageAdapter = new UserImageAdapter(userList, this::onUserClick);
        userImageRecyclerView.setAdapter(userImageAdapter);
    }

    private void onUserClick(int position) {
        User user = userList.get(position);
        if (user.getImageUrl() != null && user.getImageUrl() != "") {
            replaceFragment(DeleteProfilePictureAdmin.newInstance(user));
        }
        else{
            Snackbar.make(inflatedView, "User has no image to delete", Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * Configures the RecyclerView and initializes the event list and adapter.
     *
     * @param view The current view instance containing the UI elements.
     */
    private void displayEvents(View view) {
        eventPosterRecyclerView = view.findViewById(R.id.event_poster_recyclerview);
        eventList = new ArrayList<>();
        userList = new ArrayList<>();
        userImageRecyclerView = view.findViewById(R.id.user_image_recyclerview);
        setAdapter();

        //eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), this::updateEventUI);

        //userRepo = new UserRepository(FirebaseFirestore.getInstance());
        //userViewModel = new UserViewModel(userRepo, new MutableLiveData<>(), new MutableLiveData<>(), new User());
        //userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        //userViewModel = UserViewModel.getInstance();
        userViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), this::updateUserUI);
    }
    /**
     * Replaces the current fragment with a new fragment for event deletion.
     *
     * @param fragment The new fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.admin_view_images_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}