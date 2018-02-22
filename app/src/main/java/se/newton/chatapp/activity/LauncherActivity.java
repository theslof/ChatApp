package se.newton.chatapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import se.newton.chatapp.service.Database;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = "LauncherActivity";
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get instances of Firebase APIs for later use
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the user object for the current Firebase session (this is separate from out internal User class)
        // If the user is not logged in, this returns null
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //If not logged in, launch login intent
        if (user == null) {
            // Select login methods, save as a List used in the next method call
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    // Email and password
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    // Google
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

            // Create and launch sign-in intent. This is done through startActivityForResult, which
            // launches an activity from an Intent and calls onActivityResult when this Intent exits.
            startActivityForResult(
                    // Create the Intent via FirebaseUI
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    // Assign an identifier for the result callback
                    RC_SIGN_IN);
        } else
            // If we are already logged in, launch MainActivity
            launchMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the callback is from the Intent we launched earlier (ie. same callback ID)
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // If FirebaseUI login activity completed successfully
            if (resultCode == RESULT_OK) {
                launchMainActivity();
            }
        }
    }

    private void launchMainActivity() {
        Log.d(TAG, "Create user");
        FirebaseUser fUser = firebaseAuth.getCurrentUser();

        // Tell the database to create a user with the same UserID as our current session,
        // or just return it if it already exists. We supply a callback (here a lambda
        // expression) that executes when the Database call is finished.
        Database.createUser(fUser.getUid(), user -> {

            // If the Database call failed it returns null, so we check for that
            if (user != null) {
                // If the User object is missing a profile image and display name, copy
                // them from the Firebase account
                if (user.getProfileImage() == null)
                    user.setProfileImage(fUser.getPhotoUrl().toString());
                if (user.getDisplayName() == null)
                    user.setDisplayName(fUser.getDisplayName());
                Database.updateUser(user, u -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                Log.d(TAG, "Error creating user " + firebaseAuth.getCurrentUser().getUid());
            }
        });
    }
}
