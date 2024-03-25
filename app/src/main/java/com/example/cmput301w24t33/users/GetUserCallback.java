// Purpose:
// interface handles user retrieval outcomes, offering methods for successful user retrieval
// (onUserReceived) and failure (onFailure).
//
// Issues:
//


package com.example.cmput301w24t33.users;

/**
 * Callback interface for receiving the result of a user retrieval operation.
 * This interface defines methods for handling the outcomes of attempting to
 * retrieve a user from a data source.
 */
public interface GetUserCallback {
    /**
     * Called when a user is successfully retrieved.
     *
     * @param user The user object that was retrieved.
     */
    void onUserReceived(User user);

    /**
     * Called when there is a failure in retrieving a user.
     *
     * @param e The exception that occurred during the retrieval process.
     */
    void onFailure(Exception e);
}
