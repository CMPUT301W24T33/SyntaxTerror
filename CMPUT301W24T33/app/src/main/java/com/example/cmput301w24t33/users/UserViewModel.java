package com.example.cmput301w24t33.users;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
public class UserViewModel extends ViewModel {
    private final UserRepository userRepo;
    private final MutableLiveData<List<User>> userLiveData;

    // Constructor
    public UserViewModel() {
        userRepo = new UserRepository();
        userLiveData = new MutableLiveData<>();
        setUserCallback(userRepo);
    }

    /**
     * Defines UserCallback interface by:
      * <ul>
     *     <li>Set userLiveData to a list of users provided by the setUserSnapshotListener()</li>
     *     <li>Logging an exception if the Firebase query encountered an error<</li>
     * </ul>
     * @param userRepo
     */
    private void setUserCallback(UserRepository userRepo) {
        // When users are successfully loaded, set our Live Data list to our query results
        userRepo.setUserCallback(new UserRepository.UserCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                userLiveData.setValue(users);
            }
            // When Firebase encounters an error, log it
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, String.valueOf(e));
            }
        });
    }

    /**
     * This method loads all users in the "users" collection in our Firestore.
     * This is done by defining the UserCallback interface to:

     */
    public void loadUsers() {
        userRepo.setUserSnapshotListener(new UserRepository.UserCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                userLiveData.setValue(users);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, String.valueOf(e));
            }
        });
    }
    public void loadCheckedInUsers(String eventId) {

    }
    /**
     * Getter method to return users in our users collection
     * @return userLiveData as a MutableLiveData List of User, which contains a live reflection of
     * our users collection in our Firestore Database
     */
    public LiveData<List<User>> getUsersLiveData() {return userLiveData;}
}
