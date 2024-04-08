package com.example.cmput301w24t33;

import static org.mockito.Mockito.*;

import android.app.Application;

import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class UserRepositoryTest {
    @Mock
    private Application application;

    @Mock
    private FirebaseFirestore db;

    @Mock
    private CollectionReference collectionReference;
    @Mock
    private DocumentReference documentReference;

    @Mock
    private QuerySnapshot querySnapshot;

    @Mock
    private QueryDocumentSnapshot documentSnapshot;

    @Mock
    private UserRepository.UserCallback userCallback;
    @Mock
    private User mockUser;

    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockUser.getUserId()).thenReturn("testUserId");
        // Mock the behavior of Firestore methods
        when(db.collection(anyString())).thenReturn(collectionReference);
        when(collectionReference.document(anyString())).thenReturn(documentReference);
        when(documentReference.get()).thenReturn(null); // return a mock Task object here
        when(documentReference.set(any())).thenReturn(null); // return a mock Task object here

        userRepository = new UserRepository(application, db);
        userRepository.setUserCallback(userCallback);
    }

    @Test
    public void testGetUser() {
        User user = new User();
        when(documentSnapshot.toObject(User.class)).thenReturn(user);
        when(querySnapshot.getDocuments()).thenReturn(Arrays.asList(documentSnapshot));

        userRepository.getUser("userId");

        verify(userCallback, times(1)).onUsersLoaded(user);
    }

    @Test
    public void testSetUser() {
        User user = new User();
        userRepository.setUser(user);

        verify(collectionReference, times(1)).document(user.getUserId());
    }

    // Add more tests for other methods in UserRepository
}