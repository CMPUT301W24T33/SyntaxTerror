// Purpose:
// Models a user of the application, encapsulating their identification, contact information, and
// their interaction with events through sign-ups.
//
// Issues: wants to be able to enable or disable geolocation tracking
//

package com.example.cmput301w24t33.users;

import com.example.cmput301w24t33.events.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a user within the application, including their personal information
 * and their interactions with events.
 */
public class User implements Serializable {
    private String userId;
    private Boolean adminview;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<Event> signedUpEvents = new ArrayList<>();
    private String imageRef;
    private String imageUrl;

    /**
     * Constructs a User object with specified details.
     *
     * @param userId    Unique identifier for the user.
     * @param firstName User's first name.
     * @param lastName  User's last name.
     * @param email     User's email address.
     * @param adminview Flag indicating whether the user has administrative views.
     */
    public User(String userId, String firstName, String lastName, String email, Boolean adminview,String imageurl, String imageRef) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.adminview = adminview;
        this.imageUrl = imageurl;
        this.imageRef = imageRef;
    }

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Compares this user to the specified object. The result is {@code true} if and only if
     * the argument is not {@code null} and is a {@code User} object that represents the same user ID as this object.
     *
     * @param o the object to compare this {@code User} against
     * @return {@code true} if the given object represents a {@code User} equivalent to this user, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    /**
     * Returns a hash code value for this user. This method is supported for the benefit of hash tables such as those provided by {@link java.util.HashMap}.
     *
     * @return a hash code value for this user.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    /**
     * Gets the user's ID.
     *
     * @return A string representing the user's unique ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user's ID.
     *
     * @param userId A string representing the user's unique ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's first name.
     *
     * @return A string representing the user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName A string representing the user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return A string representing the user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName A string representing the user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's full name.
     *
     * @return A string representing the user's full name.
     */
    public String getFullName() {
        return this.firstName + ' ' + this.lastName;
    }

    /**
     * Adds an event to the user's list of signed up events.
     *
     * @param event The event to add to the list.
     */
    public void addEvent(Event event) {
        this.signedUpEvents.add(event);
    }

    /**
     * Gets the user's admin view status.
     *
     * @return A Boolean representing whether the user has admin view privileges.
     */
    public Boolean getAdminview() {
        return adminview;
    }

    /**
     * Sets the user's admin view status.
     *
     * @param adminview A Boolean representing whether the user should have admin view privileges.
     */
    public void setAdminview(Boolean adminview) {
        this.adminview = adminview;
    }

    /**
     * Gets the user's email address.
     *
     * @return A string representing the user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email A string representing the user's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
