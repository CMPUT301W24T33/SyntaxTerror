package com.example.cmput301w24t33.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

public class UserAdapterTest {
    private User user; private final String userId = "user123"; private final String firstName = "John"; private final String lastName = "Doe"; private final String email = "john.doe@example.com"; private final Boolean adminview = true; private final String imageUrl = "http://example.com/image.jpg"; private final String imageRef = "imageRef123";

    private UserAdapter userAdapter;
    private UserAdapter.OnUserListener mockListener;
    private List<User> userList;

    @BeforeEach
    public void setUp() {
        mockListener = mock(UserAdapter.OnUserListener.class);
        userList = new ArrayList<>();
        // Properly initialize the User objects before adding them to the list
        User user1 = new User("user123", "John", "Doe", "john.doe@example.com", true, "http://example.com/image.jpg", "imageRef123");
        User user2 = new User("user456", "Jane", "Doe", "jane.doe@example.com", false, "http://example.com/image2.jpg", "imageRef456");
        userList.add(user1);
        userList.add(user2);
        userAdapter = new UserAdapter(userList, mockListener);
    }


    @Test
    public void testOnBindViewHolderWhenCalledThenTextViewsSetWithCorrectValues() {
        UserAdapter.ViewHolder mockHolder = mock(UserAdapter.ViewHolder.class);
        mockHolder.firstName = mock(TextView.class);
        mockHolder.lastName = mock(TextView.class);
        int position = 0;

        userAdapter.onBindViewHolder(mockHolder, position);

        verify(mockHolder.firstName).setText(userList.get(position).getFirstName());
        verify(mockHolder.lastName).setText(userList.get(position).getLastName());
    }

    @Test
    public void testGetItemCountWhenCalledThenCorrectNumberReturned() {
        int itemCount = userAdapter.getItemCount();

        assertEquals(userList.size(), itemCount);
    }
}