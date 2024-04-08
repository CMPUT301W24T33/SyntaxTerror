package com.example.cmput301w24t33;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.util.Log;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;

import net.bytebuddy.asm.Advice;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class UserDatabaseTests {
    private User user1;

    @Mock
    private UserRepository.UserCallback mockUserCallback;
    @Mock
    private Task<DocumentSnapshot> mockDocTask;
    @Mock
    private DocumentSnapshot mockDocumentSnapshot;
    @InjectMocks
    private UserRepository userRepo;
    @Mock
    private FirebaseFirestore mockDB;
    @InjectMocks User user2;
    @Mock
    private CollectionReference colRef;
    @Mock
    private DocumentReference docRef;
    @Mock
    private Application application;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDB = Mockito.mock(FirebaseFirestore.class);
        colRef = Mockito.mock(CollectionReference.class);
        docRef = Mockito.mock(DocumentReference.class);
        application = Mockito.mock(Application.class);
        mockUserCallback = Mockito.mock(UserRepository.UserCallback.class);

        Mockito.when(mockDB.collection("users")).thenReturn(colRef);

        // Assuming UserRepository has a way to set the FirebaseFirestore instance
        userRepo = Mockito.mock(UserRepository.class);
        UserRepository.initialize(application, mockDB);


        user1 = new User();
        user2 = new User();
        initUser(user1, "123abc", "joe", "dirte");
        initUser(user2, "456def", "jane", "doe");
    }
    public void initUser(User user, String id, String firstName, String lastName) {
        user.setUserId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }
    @Test
    public void test() {
        user2 = Mockito.mock(User.class);
        Mockito.when(mockDB.collection("users")).thenReturn(colRef);
        Mockito.doThrow(new Exception("user set!")).when(userRepo).setUser(user2);

        Mockito.when(user2.getUserId()).thenReturn("bababooey");
        System.out.println(user2.getUserId());
        assertSame("bababooey", user2.getUserId());
    }

    @Test
    public void test_mockito_void() {
        Mockito.doThrow(IllegalArgumentException.class).when(userRepo).setUser(null);

        doAnswer((i) -> {
            System.out.println("User setUser Arg = " + i.getArgument(0));
            return null;
        }).when(userRepo).setUser(any(User.class));

        //Mockito.when(user2.getUserId()).thenReturn("123");
        Assert.assertThrows(IllegalArgumentException.class, () -> userRepo.setUser(null));
        userRepo.setUser(user2);
        System.out.println(user2.getUserId());
        //assertSame(user2, userRepo.getUser(user2.getUserId()));
        //userRepo.setUser(null);
    }
    //@Test
    public void getffUserSuccessTest() {
        when(docRef.get()).thenReturn(mockDocTask);
        // Prepare a mock DocumentSnapshot
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.toObject(User.class)).thenReturn(user1);

        // Capture the success listener
        ArgumentCaptor<OnSuccessListener> successCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);
        Mockito.doReturn(mockDocTask).when(docRef).get();
        Mockito.doNothing().when(mockDocTask).addOnSuccessListener(successCaptor.capture());

        // Execute the method under test
        userRepo.getUser("userId");

        // Trigger the success callback
        successCaptor.getValue().onSuccess(mockDocumentSnapshot);

        // Verify the callback was called with the expected user
        Mockito.verify(mockUserCallback).onUsersLoaded(any(User.class));
    }
    @Test
    public void getUserSuccessTest() {
        // Mock the Task<DocumentSnapshot> to simulate a successful Firestore fetch
        Task<DocumentSnapshot> mockTask = Tasks.forResult(mockDocumentSnapshot);

        // Assume the document exists and return a mock User object
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.toObject(User.class)).thenReturn(user1);

        // When docRef.get() is called, return the mocked Task
        when(docRef.get()).thenReturn(mockTask);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);


        // Set up the UserCallback to capture the callback result
        userRepo.setUserCallback(mockUserCallback);



        doAnswer((i) -> {
            System.out.println("User getUser Arg = " + i.getArgument(0));
            return null;
        }).when(userRepo).getUser(anyString());


        // Execute the method under test
        userRepo.getUser("userId");

        // Since we're using a direct return of the mocked Task, there's no need to manually trigger the success callback.
        // Instead, we verify that the onUsersLoaded callback was called with the expected user.
        Mockito.verify(mockUserCallback).onUsersLoaded(userCaptor.capture());
        Mockito.verify(mockDocumentSnapshot.toObject(User.class))
    }

    public void testSetUser() {
        // Create a mock Firestore collection reference
        CollectionReference mockColl = Mockito.mock(CollectionReference.class);

        // Create a mock DocumentReference
        DocumentReference mockDocRef = Mockito.mock(DocumentReference.class);

        // Create a mock User object
        User user = new User();
        initUser(user, "123", "a", "b");

        // Stub the behavior of the Firestore collection to return the mock document reference
        Mockito.when(mockDB.collection("users")).thenReturn(mockColl);
        Mockito.when(mockColl.document(Mockito.anyString())).thenReturn(mockDocRef);

        // Stub the behavior of the DocumentReference set method to return a successful Task
        Task<Void> setTask = Tasks.forResult(null);
        Mockito.when(mockDocRef.set(user)).thenReturn(setTask);

        // Invoke the setUser() method of UserRepository
        userRepo.setUser(user);

        // Verify that the set method of the document reference was called with the user object
        Mockito.verify(mockDocRef).set(user);
    }

}
