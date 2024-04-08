package com.example.cmput301w24t33;

import static org.mockito.Mockito.*;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.cmput301w24t33.users.User;
import com.example.cmput301w24t33.users.UserRepository;
import com.example.cmput301w24t33.users.UserViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository.UserCallback mockUserCallback;

    @Mock
    private FirebaseFirestore mockDb;
    @Mock
    private DocumentReference docRef;
    @Mock
    private CollectionReference colRef;

    @Mock
    private Application mockApplication;
    private User user1;
    private User user2;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockDb = Mockito.mock(FirebaseFirestore.class);
        colRef = Mockito.mock(CollectionReference.class);
        docRef = Mockito.mock(DocumentReference.class);
        mockApplication = Mockito.mock(Application.class);
        mockUserCallback = Mockito.mock(UserRepository.UserCallback.class);

        Mockito.when(mockDb.collection("users")).thenReturn(colRef);
        userRepository = Mockito.mock(UserRepository.class);
        UserRepository.initialize(mockApplication, mockDb);
        //userRepository = UserRepository.getInstance();
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
    public void testSetUserSnapshotListener() {
        userRepository.setUserSnapshotListener();
        verify(colRef).addSnapshotListener(any());
        verify(mockDb.collection("users")).addSnapshotListener(any());
    }

    @Test
    public void testCheckedInUsersSnapshot() {
        String mockEventId = "mockEventId";
        CollectionReference mockEventsColRef = Mockito.mock(CollectionReference.class);
        DocumentReference mockDocRef = Mockito.mock(DocumentReference.class);
        CollectionReference mockAttendeesColRef = Mockito.mock(CollectionReference.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> argumentCaptor = ArgumentCaptor.forClass(EventListener.class);
        List<User> mockUsers = Arrays.asList(user1, user2); // Replace with actual User instances
        QuerySnapshot mockSnapshot = Mockito.mock(QuerySnapshot.class);
        QueryDocumentSnapshot mockDocument1 = Mockito.mock(QueryDocumentSnapshot.class);
        QueryDocumentSnapshot mockDocument2 = Mockito.mock(QueryDocumentSnapshot.class);

        Mockito.when(mockDb.collection("events")).thenReturn(mockEventsColRef);
        Mockito.when(mockEventsColRef.document(mockEventId)).thenReturn(mockDocRef);
        Mockito.when(mockDocRef.collection("attendees")).thenReturn(mockAttendeesColRef);
        Mockito.when(mockAttendeesColRef.addSnapshotListener(argumentCaptor.capture())).thenReturn(null);
        Mockito.when(mockSnapshot.getDocuments()).thenReturn(Arrays.asList(mockDocument1, mockDocument2));
        Mockito.when(mockDocument1.toObject(User.class)).thenReturn(mockUsers.get(0));
        Mockito.when(mockDocument2.toObject(User.class)).thenReturn(mockUsers.get(1));

        userRepository.setCheckedInUsersSnapshotListener(mockEventId);

        verify(userRepository).setCheckedInUsersSnapshotListener(mockEventId);

    }

    @Test
    public void testGetUser() {
        String mockUserId = "mockUserId";
        CollectionReference mockUserCollection = Mockito.mock(CollectionReference.class);
        DocumentReference mockDocRef = Mockito.mock(DocumentReference.class);
        Task<DocumentSnapshot> mockTask = Mockito.mock(Task.class);
        DocumentSnapshot mockSnapshot = Mockito.mock(DocumentSnapshot.class);

        Mockito.when(mockDb.collection("users")).thenReturn(mockUserCollection);
        Mockito.when(mockUserCollection.document(mockUserId)).thenReturn(mockDocRef);
        Mockito.when(mockDocRef.get()).thenReturn(mockTask);
        Mockito.when(mockTask.isSuccessful()).thenReturn(true);
        Mockito.when(mockTask.getResult()).thenReturn(mockSnapshot);
        Mockito.when(mockSnapshot.exists()).thenReturn(true);
        Mockito.when(mockSnapshot.toObject(User.class)).thenReturn(user1);

        userRepository.setUserCallback(mockUserCallback);
        userRepository.setUser(user1);
        userRepository.getUser("123abc");

        verify(userRepository).getUser(anyString());
    }
}
