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
//        setContentView(R.layout.activity_launcher);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //If not logged in, launch login intent
        if (user == null) {
            // Select login methods
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    // Email and password
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    // Google
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        } else
            launchMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Database.createUser(firebaseAuth.getCurrentUser().getUid(), user -> {
                    if (user != null) {
                        FirebaseUser fUser = firebaseAuth.getCurrentUser();
                        if (fUser.getPhotoUrl() != null)
                            user.setProfileImage(fUser.getPhotoUrl().toString());
                        if (fUser.getDisplayName() != null)
                            user.setDisplayName(fUser.getDisplayName());
                        Database.updateUser(user, u -> {
                            launchMainActivity();
                        });
                    } else {
                        Log.d(TAG, "Error creating user " + firebaseAuth.getCurrentUser().getUid());
                    }
                });
            }
        }
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
