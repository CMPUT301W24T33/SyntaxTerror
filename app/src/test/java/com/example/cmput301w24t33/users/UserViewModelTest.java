package com.example.cmput301w24t33.users;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

import android.app.Application;

import androidx.collection.SparseArrayCompat;
import androidx.lifecycle.MutableLiveData;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(RobolectricTestRunner.class)
public class UserViewModelTest {

    @Mock
    private UserRepository mockUserRepo;

    @Mock
    private MutableLiveData<List<User>> mockUserList;
    @Mock
    FirebaseFirestoreException mockE;

    @Mock
    private MutableLiveData<User> mockSingleUser;

    @Mock
    private Application mockApplication;
    @Mock
    private FirebaseFirestore mockDB;
    @Mock
    private UserRepository.UserCallback mockUserCallback;
    private UserViewModel userViewModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDB = Mockito.mock(FirebaseFirestore.class);
        mockApplication = Mockito.mock(Application.class);
        mockUserCallback = Mockito.mock(UserRepository.UserCallback.class);
        mockUserRepo = Mockito.mock(UserRepository.class);
        userViewModel = new UserViewModel(mockApplication, mockUserRepo, mockUserList, mockSingleUser);
    }

    @Test
    public void testLoadUsersWhenSuccessThenFetchUsers() {
        userViewModel.loadUsers();
        verify(mockUserRepo).setUserSnapshotListener();
    }

    @Test
    public void testLoadUsersWhenErrorThenHandleException() {
        mockE = Mockito.mock(FirebaseFirestoreException.class);
        // Use the mock SparseArrayCompat
        Mockito.doNothing().when(mockUserRepo).setUserSnapshotListener();
        userViewModel.loadUsers();
        verify(mockUserRepo).setUserCallback(Mockito.any(UserRepository.UserCallback.class));
    }

    @Test
    public void testLoadUsersWhenCalledThenSetUserSnapshotListenerCalled() {
        // Act
        userViewModel.loadUsers();
        List<User> mockUsers = Mockito.mock(List.class);
        Mockito.doNothing().when(mockUserCallback).onUsersLoaded(mockUsers);
        // Assert
        verify(mockUserRepo).setUserSnapshotListener();
        verify(mockUserCallback).onUsersLoaded(mockUsers);
    }
}