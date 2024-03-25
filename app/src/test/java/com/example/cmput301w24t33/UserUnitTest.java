package com.example.cmput301w24t33;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.cmput301w24t33.users.User;

import org.junit.Before;
import org.junit.Test;

public class UserUnitTest {

    private User user;

    @Before
    public void setUp() {
        // Instantiate the user object with all required parameters
        user = new User("1", "John", "Doe", "john.doe@example.com", false, "imageURL", "imageRef");
    }


    @Test
    public void userConstructorTest() {
        assertEquals("1", user.getUserId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertFalse(user.getAdminview());
    }

    @Test
    public void getFullNameTest() {
        String expectedFullName = "John Doe";
        assertEquals(expectedFullName, user.getFullName());
    }

    @Test
    public void setAndGetFirstNameTest() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    public void setAndGetLastNameTest() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    public void setAndGetEmailTest() {
        user.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", user.getEmail());
    }

    @Test
    public void setAndGetUserIdTest() {
        user.setUserId("2");
        assertEquals("2", user.getUserId());
    }

    @Test
    public void setAndGetAdminViewTest() {
        user.setAdminview(true);
        assertTrue(user.getAdminview());
    }
}