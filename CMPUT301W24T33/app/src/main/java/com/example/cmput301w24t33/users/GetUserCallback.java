package com.example.cmput301w24t33.users;

public interface GetUserCallback {
    void onUserReceived(User user);
    void onFailure(Exception e);
}