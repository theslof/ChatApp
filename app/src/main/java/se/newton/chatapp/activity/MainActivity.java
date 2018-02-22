package se.newton.chatapp.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import se.newton.chatapp.R;
import se.newton.chatapp.adapter.MessageAdapter;
import se.newton.chatapp.fragment.ChatFragment;
import se.newton.chatapp.fragment.First;
import se.newton.chatapp.fragment.Second;
import se.newton.chatapp.model.Channel;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.model.User;
import se.newton.chatapp.other.CircleTransform;
import se.newton.chatapp.service.Database;
import se.newton.chatapp.viewmodel.MessageViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final static String TAG = "MainActivity";
    FirebaseUser fUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        findViewById(R.id.signOutView).setOnClickListener(View -> {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        finish();
                    });

        });
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        Glide.with(this)
                .load(personPhoto)
                .bitmapTransform(new CircleTransform(this))
                .into(nav_img);


        // ---- Firebase ----

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        // -- Showing all messages in a chat room

        ChatFragment chatFragment = ChatFragment.newInstance("MyTestChannel");

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, chatFragment, "CHAT")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // makeDummyData();
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
        if (id == R.id.action_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        finish();
                    });

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

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

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

        Message message1 = new Message(Message.TYPE_TEXT, "Test message 1 Chan 1");
        message1.setUid(user.getUid());
        message1.setCid(chan1.getCid());

        Message message2 = new Message(Message.TYPE_TEXT, "");
        message2.setUid(user.getUid());
        message2.setCid(chan1.getCid());
        message2.setData("Test message 2 Chan 1");

        Message message3 = new Message(Message.TYPE_TEXT, "");
        message3.setUid(user.getUid());
        message3.setCid(chan1.getCid());
        message3.setData("Test message 3 Chan 1");

        Message message1_0 = new Message(Message.TYPE_TEXT, "");
        message1_0.setUid(user.getUid());
        message1_0.setCid(chan.getCid());
        message1_0.setData("Test message 1 Chan 2");

        Message message2_0 = new Message(Message.TYPE_TEXT, "");
        message2_0.setUid(user.getUid());
        message2_0.setCid(chan.getCid());
        message2_0.setData("Test message 2 Chan 2");

        Message message3_0 = new Message(Message.TYPE_TEXT, "");
        message3_0.setUid(user.getUid());
        message3_0.setCid(chan.getCid());
        message3_0.setData("Test message 3 Chan 2");

        Message message1_1 = new Message(Message.TYPE_TEXT, "");
        message1_1.setUid(user1.getUid());
        message1_1.setCid(chan.getCid());
        message1_1.setData("Test message 1 from " + user1.getUid());

        Message message2_1 = new Message(Message.TYPE_TEXT, "");
        message2_1.setUid(user1.getUid());
        message2_1.setCid(chan.getCid());
        message2_1.setData("Test message 2 from " + user1.getUid());

        Message message3_1 = new Message(Message.TYPE_TEXT, "");
        message3_1.setUid(user1.getUid());
        message3_1.setCid(chan.getCid());
        message3_1.setData("Test message 3 from " + user1.getUid());

        Message message1_2 = new Message(Message.TYPE_TEXT, "");
        message1_2.setUid(user2.getUid());
        message1_2.setCid(chan.getCid());
        message1_2.setData("Test message 1 from " + user2.getUid());

        Message message2_2 = new Message(Message.TYPE_TEXT, "");
        message2_2.setUid(user2.getUid());
        message2_2.setCid(chan.getCid());
        message2_2.setData("Test message 2 from " + user2.getUid());

        Message message3_2 = new Message(Message.TYPE_TEXT, "");
        message3_2.setUid(user1.getUid());
        message3_2.setCid(chan.getCid());
        message3_2.setData("Test message 3 from " + user2.getUid());

        Message message1_3 = new Message(Message.TYPE_TEXT, "");
        message1_3.setUid(user2.getUid());
        message1_3.setCid(chan.getCid());
        message1_3.setData("Test message 1 from " + user3.getUid());

        Message message2_3 = new Message(Message.TYPE_TEXT, "");
        message2_3.setUid(user2.getUid());
        message2_3.setCid(chan.getCid());
        message2_3.setData("Test message 2 from " + user3.getUid());

        Message message3_3 = new Message(Message.TYPE_TEXT, "");
        message3_3.setUid(user1.getUid());
        message3_3.setCid(chan.getCid());
        message3_3.setData("Test message 3 from " + user3.getUid());

        OnSuccessListener<DocumentReference> onSuccessListener = new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference doc) {
                doc.set(new HashMap<String, String>() {{
                    put("mid", doc.getId());
                }}, SetOptions.merge());
            }
        };

        db.collection("channels").document(chan1.getCid()).set(chan1);
        db.collection("channels").document(chan.getCid()).set(chan);
        db.collection("users").document(user1.getUid()).set(user1);
        db.collection("users").document(user2.getUid()).set(user2);
        db.collection("users").document(user3.getUid()).set(user3);
        db.collection("messages").add(message1).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message2).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message3).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message1_0).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message2_0).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message3_0).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message1_1).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message2_1).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message3_1).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message1_2).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message2_2).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message3_2).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message1_3).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message2_3).addOnSuccessListener(onSuccessListener);
        db.collection("messages").add(message3_3).addOnSuccessListener(onSuccessListener);
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

