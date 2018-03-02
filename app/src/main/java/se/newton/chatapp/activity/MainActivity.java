package se.newton.chatapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import se.newton.chatapp.R;
import se.newton.chatapp.fragment.ChatFragment;
import se.newton.chatapp.fragment.ProfileFragment;
import se.newton.chatapp.fragment.SettingsFragment;
import se.newton.chatapp.model.User;
import se.newton.chatapp.service.UserManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final static String TAG = "MainActivity";
    private FirebaseUser fUser;
    private FirebaseFirestore db;
    private User user;
    private FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- Firebase --

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (fUser == null) {
            setContentView(R.layout.activity_main_nologin);
        } else {
            setContentView(R.layout.activity_main);
            user = UserManager.getUser(Glide.with(this), fUser.getUid());
        }

        if (savedInstanceState == null) {

            //TODO: Implement another landing page, perhaps latest channel viewed

            //When started after clicking on Cloud Message Notification
            String cid = getIntent().getStringExtra("cid");
            if (cid == null)
                cid = "MyTestChannel";
            //When started with deep link
            Uri data = this.getIntent().getData();
            if (data != null) {
                cid = data.getLastPathSegment();
            }


            // Create a new chat fragment that will show all messages sent to channel cid
            openChannel(cid, true);
        }

        if (fUser == null)
            return;

        // Create a User object for the current user, which will fetch the data from Firebase and
        //  download the profile image.
        user = UserManager.getUser(Glide.with(this), fUser.getUid());

        // Connect Toolbar to layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup side drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (fUser == null)
            return;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // -- Side drawer user info --
        // TODO: Use data from our own Firebase User instead, as this may crash if user does not log in via Google.
        // Use databinding instead, so it updates as the user profile is edited? Or move this to onStart?
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        Uri personPhoto = null;
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.nameView);
        nav_user.setText(acct.getDisplayName());
        TextView nav_mail = (TextView) hView.findViewById(R.id.mailView);
        nav_mail.setText(acct.getEmail());
        ImageView nav_img = (ImageView) hView.findViewById(R.id.imageView);
        nav_img.setImageURI(acct.getPhotoUrl());

        Glide.with(this).load(personPhoto)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(
                        R.drawable.ic_profile_image_placeholder_circular))
                .into(nav_img);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "test");

        //When clicking Cloud Message Notification
        //and already running we can get the channel id from extra data in intent.
        String cid = intent.getStringExtra("cid");
        if (cid != null) {
            Log.d("NewIntent", cid);
            openChannel(cid);
        }

        //Already running and deep link start
        Uri data = intent.getData();

        if (data != null) {
            cid = data.getLastPathSegment();
            Log.d("NewIntent", cid);
            openChannel(cid);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // TODO: Move this to side drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    // TODO: Implement navigation via side drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_channel_one) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_log_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        finish();
                    });

        } else if (id == R.id.nav_my_profile) {
            openProfile(fUser.getUid());
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openChannel(String cid) {
        openChannel(cid, false);
    }

    private void openChannel(String cid, boolean noBackstack) {

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getTag().equals(cid))
            noBackstack = true;

        Fragment fragment = ChatFragment.newInstance(cid);

        if (noBackstack)
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, cid)
                    .commit();
        else
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, cid)
                    .addToBackStack(cid)
                    .commit();
    }

    private void openProfile(String uid) {
        openProfile(uid, false);
    }

    private void openProfile(String uid, boolean noBackstack) {

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getTag().equals(uid))
            return;

        Fragment fragment = fragmentManager.findFragmentByTag(uid);
        if (fragment == null)
            fragment = ProfileFragment.newInstance(uid);

        if (noBackstack)
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, uid)
                    .commit();
        else
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, uid)
                    .addToBackStack(uid)
                    .commit();
    }

}

