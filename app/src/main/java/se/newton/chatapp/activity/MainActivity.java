package se.newton.chatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.newton.chatapp.R;
import se.newton.chatapp.model.Channel;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.model.User;
import se.newton.chatapp.service.Database;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final static String TAG = "MainActivity";
    FirebaseUser fUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonSignOut).setOnClickListener(View -> {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        finish();
                    });

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ---- Firebase ----

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //makeDummyData();

        /*
        // Get the User object for the currently logged in user.
        Database.getUser(fUser.getUid(), user -> {
            if (user != null)
                printMessagesFrom(user.getUid());
            else
                Log.d(TAG, "User '" + fUser.getUid() + "' not found!");
        });

       */

        Database.userExists(fUser.getUid(), result -> {
            if(result);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_channel_one) {
            // Handle the camera action
        } else if (id == R.id.nav_channel_two) {

        } else if (id == R.id.nav_channel_three) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeDummyData() {
        User user = new User(fUser.getUid());
        user.setDisplayName(fUser.getDisplayName());

        User user1 = new User("TestUser1");
        user1.setDisplayName("Test One");

        User user2 = new User("TestUser2");
        user2.setDisplayName("Test Two");

        User user3 = new User("TestUser3");
        user3.setDisplayName("Test Three");

        Channel chan = new Channel();
        chan.setCid("MyTestChannel2");

        Channel chan1 = new Channel();
        chan1.setCid("MyTestChannel");

        Message message1 = new Message();
        message1.setMessageType(Message.TYPE_TEXT);
        message1.setUid(user.getUid());
        message1.setCid(chan1.getCid());
        message1.setData("Test message 1 Chan 1");

        Message message2 = new Message();
        message2.setMessageType(Message.TYPE_TEXT);
        message2.setUid(user.getUid());
        message2.setCid(chan1.getCid());
        message2.setData("Test message 2 Chan 1");

        Message message3 = new Message();
        message3.setMessageType(Message.TYPE_TEXT);
        message3.setUid(user.getUid());
        message3.setCid(chan1.getCid());
        message3.setData("Test message 3 Chan 1");

        Message message1_0 = new Message();
        message1_0.setMessageType(Message.TYPE_TEXT);
        message1_0.setUid(user.getUid());
        message1_0.setCid(chan.getCid());
        message1_0.setData("Test message 1 Chan 2");

        Message message2_0 = new Message();
        message2_0.setMessageType(Message.TYPE_TEXT);
        message2_0.setUid(user.getUid());
        message2_0.setCid(chan.getCid());
        message2_0.setData("Test message 2 Chan 2");

        Message message3_0 = new Message();
        message3_0.setMessageType(Message.TYPE_TEXT);
        message3_0.setUid(user.getUid());
        message3_0.setCid(chan.getCid());
        message3_0.setData("Test message 3 Chan 2");

        Message message1_1 = new Message();
        message1_1.setMessageType(Message.TYPE_TEXT);
        message1_1.setUid(user1.getUid());
        message1_1.setCid(chan.getCid());
        message1_1.setData("Test message 1 from " + user1.getUid());

        Message message2_1 = new Message();
        message2_1.setMessageType(Message.TYPE_TEXT);
        message2_1.setUid(user1.getUid());
        message2_1.setCid(chan.getCid());
        message2_1.setData("Test message 2 from " + user1.getUid());

        Message message3_1 = new Message();
        message3_1.setMessageType(Message.TYPE_TEXT);
        message3_1.setUid(user1.getUid());
        message3_1.setCid(chan.getCid());
        message3_1.setData("Test message 3 from " + user1.getUid());

        Message message1_2 = new Message();
        message1_2.setMessageType(Message.TYPE_TEXT);
        message1_2.setUid(user2.getUid());
        message1_2.setCid(chan.getCid());
        message1_2.setData("Test message 1 from " + user2.getUid());

        Message message2_2 = new Message();
        message2_2.setMessageType(Message.TYPE_TEXT);
        message2_2.setUid(user2.getUid());
        message2_2.setCid(chan.getCid());
        message2_2.setData("Test message 2 from " + user2.getUid());

        Message message3_2 = new Message();
        message3_2.setMessageType(Message.TYPE_TEXT);
        message3_2.setUid(user1.getUid());
        message3_2.setCid(chan.getCid());
        message3_2.setData("Test message 3 from " + user2.getUid());

        Message message1_3 = new Message();
        message1_3.setMessageType(Message.TYPE_TEXT);
        message1_3.setUid(user2.getUid());
        message1_3.setCid(chan.getCid());
        message1_3.setData("Test message 1 from " + user3.getUid());

        Message message2_3 = new Message();
        message2_3.setMessageType(Message.TYPE_TEXT);
        message2_3.setUid(user2.getUid());
        message2_3.setCid(chan.getCid());
        message2_3.setData("Test message 2 from " + user3.getUid());

        Message message3_3 = new Message();
        message3_3.setMessageType(Message.TYPE_TEXT);
        message3_3.setUid(user1.getUid());
        message3_3.setCid(chan.getCid());
        message3_3.setData("Test message 3 from " + user3.getUid());

        db.collection("channels").document(chan1.getCid()).set(chan1);
        db.collection("channels").document(chan.getCid()).set(chan);
        db.collection("users").document(user1.getUid()).set(user1);
        db.collection("users").document(user2.getUid()).set(user2);
        db.collection("users").document(user3.getUid()).set(user3);
        db.collection("messages").add(message1);
        db.collection("messages").add(message2);
        db.collection("messages").add(message3);
        db.collection("messages").add(message1_0);
        db.collection("messages").add(message2_0);
        db.collection("messages").add(message3_0);
        db.collection("messages").add(message1_1);
        db.collection("messages").add(message2_1);
        db.collection("messages").add(message3_1);
        db.collection("messages").add(message1_2);
        db.collection("messages").add(message2_2);
        db.collection("messages").add(message3_2);
        db.collection("messages").add(message1_3);
        db.collection("messages").add(message2_3);
        db.collection("messages").add(message3_3);
    }

    private void printMessagesFrom(String uid) {
        db.collection("messages").whereEqualTo("uid", uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    Log.d(TAG, doc.getId() + " => " + doc.getData());
                }
            } else {
                Log.d(TAG, "Error getting documents: " + task.getException());
            }
        });
    }
}

