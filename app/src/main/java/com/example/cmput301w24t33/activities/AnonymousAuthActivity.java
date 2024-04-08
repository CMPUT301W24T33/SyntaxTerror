// Purpose:
// Manages anonymous user authentication using Firebase, offering a pathway for users to later
// link the anonymous account with email credentials.


package com.example.cmput301w24t33.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Demonstrates anonymous authentication in a Firebase application, allowing users to sign in anonymously and link their account with an email and password later.
 */
public class AnonymousAuthActivity extends Activity {
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonymousAuth";

    /**
     * Initializes the Firebase authentication instance when the activity is created.
     * @param savedInstanceState Contains data supplied in onSaveInstanceState(Bundle) if the activity is re-initialized after previously being shut down.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Automatically attempts to sign in the user anonymously when the activity starts.
     */
    @Override
    public void onStart() {
        super.onStart();
        signInAnonymously();
    }

    /**
     * Signs in the user anonymously using Firebase Authentication.
     */
    public void signInAnonymously() {
        // Attempts to sign in the user anonymously and handles success or failure.
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Links the anonymous account with an email and password.
     */
    private void linkAccount() {
        AuthCredential credential = EmailAuthProvider.getCredential("", "");
        // Attempts to link the anonymous account with an email and password credential.
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Updates the user interface based on the user's authentication status.
     * @param user The current FirebaseUser. Null if authentication failed.
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "User authenticated " + user.getUid());
            Intent resultIntent = new Intent();
            resultIntent.putExtra("USER_ID", user.getUid());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
