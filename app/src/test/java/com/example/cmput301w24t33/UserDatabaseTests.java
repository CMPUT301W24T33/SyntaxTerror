package com.example.cmput301w24t33;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.mockito.Mockito;

public class UserDatabaseTests {
    private User user1;
    private UserRepository userRepo;
    private FirebaseFirestore mockDB;
    private Application application;

    @Before
    public void setUp() {
        mockDB = Mockito.mock(FirebaseFirestore.class);
        UserRepository.initialize(this.application, mockDB);
        userRepo = UserRepository.getInstance();

    }
    public void initUser(User user, String id, String firstName, String lastName) {
        user.setUserId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }

    @Test
    public void testSetUser() {
        CollectionReference mockColl = Mockito.mock(CollectionReference.class);
        DocumentReference mockDoc = Mockito.mock(DocumentReference.class);
        user1 = new User();
        initUser(user1, "123abc", "Allan", "Parsons");

        Mockito.when(mockDB.collection(Mockito.anyString())).thenReturn(mockColl);
        Mockito.when(mockColl.add(Mockito.any(User.class))).thenReturn(mockDoc);

    }

}
