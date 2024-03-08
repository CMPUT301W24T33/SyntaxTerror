// Purpose
// Serves as a bridge between the UI and the data layer, specifically for user-related data.
// It utilizes UserRepository for fetching user data from Firestore and updates the UI with this
// data through LiveData, ensuring UI components observe and react to data changes.
//
// Issues:
//

package com.example.cmput301w24t33.users;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

/**
 * ViewModel for managing UI-related data in a lifecycle-conscious way.
 * It holds user data and communicates with the UserRepository for data operations.
 */
public class UserViewModel extends ViewModel {
    private final UserRepository userRepo;
    private final MutableLiveData<List<User>> userLiveData;
    private MutableLiveData<User> liveUser;
    private User user;

    /**
     * Constructor initializes the UserRepository, user LiveData, and a User object.
     * Also sets up the callback interface for user data operations.
     */
    public UserViewModel() {
        userRepo = new UserRepository();
        userLiveData = new MutableLiveData<>();
        liveUser = new MutableLiveData<>();
        user = new User();
        setUserCallback(userRepo);
    }

    /**
     * Sets the callback for user data operations. Updates LiveData with user data on successful fetch
     * and logs any errors encountered during the process.
     *
     * @param userRepo The UserRepository instance used for data operations.
     */
    private void setUserCallback(UserRepository userRepo) {
        userRepo.setUserCallback(new UserRepository.UserCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                // Update LiveData with the list of users
                userLiveData.setValue(users);
            }

            @Override
            public void onUsersLoaded(User queriedUser) {
                if (queriedUser==null) {
                    //return;
                }
                //Log.d(TAG, "UserViewModel single user: " + queriedUser.getUserId());
                liveUser.setValue(queriedUser);
                //Log.d(TAG, "LiveData single user: " + liveUser.getValue());
            }

            @Override
            public void onFailure(Exception e) {
                // Log error on failure
                Log.d(TAG, "Error loading users: ", e);
            }
        });
    }

    /**
     * Initiates the fetching of all users from the Firestore database.
     */
    public void loadUsers() {
        userRepo.setUserSnapshotListener();
    }

    /**
     * Initiates fetching of users checked into a specific event.
     *
     * @param eventId The ID of the event to fetch checked-in users for.
     */
    public void loadCheckedInUsers(String eventId) {
        userRepo.setCheckedInUsersSnapshotListener(eventId);
    }

    /**
     * Initiates fetching of users signed up for a specific event.
     *
     * @param eventId The ID of the event to fetch signed-up users for.
     */
    public void loadSignedUpUsers(String eventId) {
        userRepo.setSignedUpUsersSnapshotListener(eventId);
    }

    /**
     * Returns LiveData containing the list of users.
     *
     * @return A LiveData object containing a list of User objects.
     */
    public LiveData<List<User>> getUsersLiveData() {return userLiveData;}
    public void queryUser(String userId) {
        userRepo.getUser(userId);
    }
    public LiveData<User> getUser() {
        Log.d(TAG, " GET USER LiveData single user: " + liveUser.getValue());
        return liveUser;
    }
}
