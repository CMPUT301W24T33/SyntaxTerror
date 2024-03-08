package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import com.example.cmput301w24t33.organizerFragments.AttendeeAdapter;
import com.example.cmput301w24t33.users.User;
import com.google.ar.core.Config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
@Config (sdk = {28}) // Adjust the SDK version according to your project setup
class AttendeeAdapterTest {

    private AttendeeAdapter adapter;
    private ArrayList<User> attendees;

    @Before
    public void setUp() {
        attendees = new ArrayList<>();
        attendees.add(new User("John", "Doe","doodoo@doo.me",false));
        attendees.add(new User("John", "Doe","doodoo@doo.me",false));
        adapter = new AttendeeAdapter(attendees);
    }

    @Test
    public void itemCount_ReturnsCorrectSize() {
        assertEquals("Adapter item count should match attendees list size.", 2, adapter.getItemCount());
    }

    @Test
    public void onBindViewHolder_BindsDataCorrectly() {
        // Create a mock parent
        RecyclerView parent = new RecyclerView(ApplicationProvider.getApplicationContext());
        AttendeeAdapter.MyViewHolder holder = adapter.onCreateViewHolder(parent, 0);

        // Bind the first item
        adapter.onBindViewHolder(holder, 0);

        // Verify data is correctly set
        assertEquals("First name should match the first attendee's first name.", "John", holder.firstNameText.getText().toString());
        assertEquals("Last name should match the first attendee's last name.", "Doe", holder.lastNameText.getText().toString());
    }

}