package com.example.cmput301w24t33;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.app.Application;

import com.example.cmput301w24t33.events.Event;
import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class UserDatabaseTests {
    private User user1;

    @InjectMocks
    private UserRepository userRepo;
    @InjectMocks
    private FirebaseFirestore mockDB;
    @InjectMocks User user2;
    @Mock
    private CollectionReference colRef;
    @Mock
    private DocumentReference docRef;
    @Mock
    private Application application;

    @Before
    public void setUp() {
        mockDB = Mockito.mock(FirebaseFirestore.class);
        colRef = Mockito.mock(CollectionReference.class);
        application = Mockito.mock(Application.class);
        Mockito.when(mockDB.collection("users")).thenReturn(colRef);
        userRepo = Mockito.mock(UserRepository.class);
        //UserRepository.initialize(application, mockDB);
        //userRepo = UserRepository.getInstance();
        user1 = new User();
        user2 = Mockito.mock(User.class);
        initUser(user1, "123abc", "joe", "dirte");
    }
    public void initUser(User user, String id, String firstName, String lastName) {
        user.setUserId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }
    @Test
    public void test() {
        Mockito.when(mockDB.collection("users")).thenReturn(colRef);
        //Mockito.when(userRepo.setUser(user2)).thenReturn(userRepo);
        user2 = Mockito.mock(User.class);
        Mockito.when(user2.getUserId()).thenReturn("bababooey");
        System.out.println(user2.getUserId());
        assertSame("bababooey", user2.getUserId());
    }

    @Test
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
