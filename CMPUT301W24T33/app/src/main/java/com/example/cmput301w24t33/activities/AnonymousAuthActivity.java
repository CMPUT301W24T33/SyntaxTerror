// Reference: https://github.com/firebase/snippets-android/blob/2324a6cc01262e4df8a6b4c3623ad895119f0724/auth/app/src/main/java/com/google/firebase/quickstart/auth/AnonymousAuthActivity.java#L49-L50

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
 * Activity to handle anonymous authentication with Firebase. The class demonstrates
 * how to sign in a user anonymously and then link the anonymous account with credentials
 * if needed.
 */
public class AnonymousAuthActivity extends Activity {
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonymousAuth";

    /**
     * Called when the activity is first created. This method initializes the FirebaseAuth instance.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }
    /**
     * Invoked after the activity has been created and the activity's window has been made visible.
     * It starts the process of signing in anonymously to Firebase.
     */

    @Override
    public void onStart() {
        super.onStart();
        signInAnonymously();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    /**
     * Signs in the user anonymously using FirebaseAuth. On success or failure, updates the UI accordingly.
     */

    public void signInAnonymously() {
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END signin_anonymously]
    }
    /**
     * Attempts to link the currently signed-in anonymous user with email and password credentials.
     * On success or failure, updates the UI accordingly.
     */
    private void linkAccount() {
        AuthCredential credential = EmailAuthProvider.getCredential("", "");

        // [START link_credential]
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
                            //updateUI(null);
                        }
                    }
                });
        // [END link_credential]
    }
    /**
     * Updates the UI based on user authentication status. If the user is successfully authenticated,
     * passes the user's UID back to the calling activity.
     *
     * @param user The current FirebaseUser. This may be null if authentication failed.
     */
    private void updateUI(FirebaseUser user) {
        Log.d(TAG, "User authenticated " + user.getUid());
        // Notify AttendeeActivity that authentication is complete
        Intent resultIntent = new Intent();
        resultIntent.putExtra("USER_ID", user.getUid());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}