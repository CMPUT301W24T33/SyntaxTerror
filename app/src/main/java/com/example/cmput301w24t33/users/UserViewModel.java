// Purpose
// Serves as a bridge between the UI and the data layer, specifically for user-related data.
// It utilizes UserRepository for fetching user data from Firestore and updates the UI with this
// data through LiveData, ensuring UI components observe and react to data changes.
//
// Issues:
//

package com.example.cmput301w24t33.users;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * ViewModel for managing UI-related data in a lifecycle-conscious way.
 * It holds user data and communicates with the UserRepository for data operations.
 */
public class UserViewModel extends ViewModel {
    private static UserViewModel instance;
    private UserRepository userRepo;
    private MutableLiveData<List<User>> userLiveData;
    private MutableLiveData<User> liveUser;
    private Application application;


    /**
     * Creates new UserViewModel
     * @param application Application associated with this
     * @param myRepo UserRepository containing users
     * @param userList List of users
     * @param singleUser Single user
     */
    public UserViewModel (Application application, UserRepository myRepo, MutableLiveData<List<User>> userList, MutableLiveData<User> singleUser) {
        this.application = application;
        this.userRepo = myRepo;
        this.userLiveData = userList;
        this.liveUser = singleUser;
        setUserCallback(userRepo);
    }

    /**
     * Initializes static UserViewModel
     * @param application Application associated with this
     * @param myRepo UserRepository containing users
     * @param userList List of Users
     * @param singleUser Single User
     */
    public static synchronized  void initialize(Application application, UserRepository myRepo, MutableLiveData<List<User>> userList, MutableLiveData<User> singleUser) {
        if (instance == null) {
            instance = new UserViewModel(application, myRepo, userList, singleUser);
        }
    }

    /**
     * Retrieves static instance of UserViewModel
     * @return static instance of UserViewModel
     */
    public static synchronized UserViewModel getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserViewModel must be initialized in the Application class before use.");
        }
        return instance;
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
                liveUser.setValue(queriedUser);
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

    public void setUser(User user) {
        liveUser.setValue(user);
    }

    /**
     * Returns LiveData containing the list of users.
     *
     * @return A LiveData object containing a list of User objects.
     */
    public LiveData<List<User>> getUsersLiveData() {return userLiveData;}

    /**
     * Retrieves user from UserRepository
     * @param userId Id of user to be retrieved
     * @return LiveData of user that has been retrieved
     */
    public LiveData<User> getUser(String userId) {
        userRepo.getUser(userId);
        Log.d(TAG, " GET USER LiveData single user: " + liveUser.getValue());
        return liveUser;
    }
}
