package com.example.cmput301w24t33.users;

import static org.mockito.Mockito.*;
import static org.robolectric.Shadows.shadowOf;

import android.app.Application;
import android.os.Looper;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;

import java.util.concurrent.ExecutionException;
@RunWith(RobolectricTestRunner.class)
public class UserRepositoryTest {
    @Mock
    private Application application;
    @Mock
    private Looper looper;
    @Mock
    private FirebaseFirestore db;
    @Mock
    private UserRepository.UserCallback userCallback;
    @Mock
    private CollectionReference userCollection;
    @Mock
    private DocumentReference documentReference;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);


        when(db.collection("users")).thenReturn(userCollection);
        when(userCollection.document(anyString())).thenReturn(documentReference);
        UserRepository.initialize(application, db);
        userRepository = UserRepository.getInstance();
        userRepository.setUserCallback(userCallback);
    }


    @Test
    public void testSetUserWhenUserAddedThenSuccess() throws ExecutionException, InterruptedException {
        User user = new User("1", "John", "Doe", "john.doe@example.com", false, "imageUrl", "imageRef");
        Task<Void> successfulTask = Tasks.forResult(null);
        when(documentReference.set(user)).thenReturn(successfulTask);

        userRepository.setUser(user);

        shadowOf(Looper.getMainLooper()).idle();

        verify(documentReference).set(user);
        verify(userCallback).onUsersLoaded(user);
    }

    @Test
    public void testSetUserWhenUserNotAddedThenFailure() throws ExecutionException, InterruptedException {


        User user = new User("1", "John", "Doe", "john.doe@example.com", false, "imageUrl", "imageRef");
        Exception exception = new RuntimeException("Test exception");
        Task<Void> failedTask = Tasks.forException(exception);
        when(documentReference.set(user)).thenReturn(failedTask);

        userRepository.setUser(user);

        shadowOf(Looper.getMainLooper()).idle();

        verify(documentReference).set(user);
        verify(userCallback).onFailure(exception);
    }
}