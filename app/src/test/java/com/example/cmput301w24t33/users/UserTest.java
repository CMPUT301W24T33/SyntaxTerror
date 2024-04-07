package com.example.cmput301w24t33.users;
import com.example.cmput301w24t33.events.Event; import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
class UserTest {
    private User user; private final String userId = "user123"; private final String firstName = "John"; private final String lastName = "Doe"; private final String email = "john.doe@example.com"; private final Boolean adminview = true; private final String imageUrl = "http://example.com/image.jpg"; private final String imageRef = "imageRef123";
    @BeforeEach
    void setUp() {
        user = new User(userId, firstName, lastName, email, adminview, imageUrl, imageRef);
    }

    @Test
    void testEqualsWhenObjectIsNullThenReturnFalse() {
        assertFalse(user.equals(null));
    }

    @Test
    void testEqualsWhenObjectIsNotUserThenReturnFalse() {
        Object notUser = new Object();
        assertFalse(user.equals(notUser));
    }

    @Test
    void testEqualsWhenObjectRepresentsDifferentUserIdThenReturnFalse() {
        User differentUser = new User("user456", firstName, lastName, email, adminview, imageUrl, imageRef);
        assertFalse(user.equals(differentUser));
    }

    @Test
    void testEqualsWhenObjectRepresentsSameUserIdThenReturnTrue() {
        User sameUser = new User(userId, "Jane", "Smith", "jane.smith@example.com", false, "http://example.com/jane.jpg", "imageRef456");
        assertTrue(user.equals(sameUser));
    }

    @Test
    void testGetUserId() {
        assertEquals(userId, user.getUserId());
    }

    @Test
    void testSetUserId() {
        String newUserId = "newUser123";
        user.setUserId(newUserId);
        assertEquals(newUserId, user.getUserId());
    }

    @Test
    void testGetFirstName() {
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void testSetFirstName() {
        String newFirstName = "Alice";
        user.setFirstName(newFirstName);
        assertEquals(newFirstName, user.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void testSetLastName() {
        String newLastName = "Smith";
        user.setLastName(newLastName);
        assertEquals(newLastName, user.getLastName());
    }

    @Test
    void testGetFullName() {
        assertEquals(firstName + " " + lastName, user.getFullName());
    }


    @Test
    void testGetAdminview() {
        assertEquals(adminview, user.getAdminview());
    }

    @Test
    void testSetAdminview() {
        Boolean newAdminview = false;
        user.setAdminview(newAdminview);
        assertEquals(newAdminview, user.getAdminview());
    }

    @Test
    void testGetEmail() {
        assertEquals(email, user.getEmail());
    }

    @Test
    void testSetEmail() {
        String newEmail = "alice.smith@example.com";
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void testGetImageRef() {
        assertEquals(imageRef, user.getImageRef(imageRef));
    }

    @Test
    void testSetImageRef() {
        String newImageRef = "newImageRef123";
        user.setImageRef(newImageRef);
        assertEquals(newImageRef, user.getImageRef(newImageRef));
    }

    @Test
    void testGetImageUrl() {
        assertEquals(imageUrl, user.getImageUrl());
    }

    @Test
    void testSetImageUrl() {
        String newImageUrl = "http://example.com/newimage.jpg";
        user.setImageUrl(newImageUrl);
        assertEquals(newImageUrl, user.getImageUrl());
    }

    @Test
    public void userConstructorTest() {
        assertEquals("user123", user.getUserId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertTrue(user.getAdminview());
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
