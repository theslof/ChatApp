package se.newton.chatapp.service;

import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.newton.chatapp.model.Channel;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.model.User;

public final class Database {
    private static final String TAG = "Database";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Database() {
    }


    // ---- CRUD methods ----


    // -- CREATE --

    // Creates a new user, or returns existing user if it already exists.
    public static void createUser(String uid, Callback<User> onCompleteCallback) {
        Log.d(TAG, "Creating user " + uid);
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    Log.d(TAG, task.getResult().getId());
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Log.d(TAG, "User '" + uid + "' already exists");
                            onCompleteCallback.callback(doc.toObject(User.class));
                        } else {
                            Log.d(TAG, "Creating user '" + uid + "'");
                            User user = new User(uid);
                            doc.getReference().set(user).addOnCompleteListener(res -> {
                                if (res.isSuccessful()) {
                                    onCompleteCallback.callback(user);
                                } else {
                                    onCompleteCallback.callback(null);
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // Creates a new channel, or returns existing channel if it already exists.
    public static void createChannel(Channel channel, Callback<Channel> onCompleteCallback) {
        if (channel == null || channel.getCid() == null)
            onCompleteCallback.callback(null);
        db.collection("channels").document(channel.getCid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            onCompleteCallback.callback(doc.toObject(Channel.class));
                        } else {
                            doc.getReference().set(channel).addOnCompleteListener(res -> {
                                if (res.isSuccessful()) {
                                    onCompleteCallback.callback(channel);
                                } else {
                                    Log.d(TAG, task.getException().toString());
                                    onCompleteCallback.callback(null);
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });

    }

    // Creates a new message and returns a Message object
    public static void createMessage(int messageType, String data, String cid, Callback<Message> onCompleteCallback) {
        createMessage(messageType, data, cid, null, onCompleteCallback);
    }

    public static void createMessage(int messageType, String data, String cid,
                                     String receiver, Callback<Message> onCompleteCallback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || (cid == null && receiver == null))
            onCompleteCallback.callback(null);
        DocumentReference doc = db.collection("messages").document();
        Message message = new Message(messageType, data);
        message.setCid(cid);
        message.setReceiver(receiver);
        message.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        message.setMid(doc.getId());
        doc.set(message)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        message.setTimestamp(new Date());
                        onCompleteCallback.callback(message);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }


    // -- READ --

    // Get existing user from Firestore, returns null on error
    public static void getUser(String uid, Callback<User> onCompleteCallback) {
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot doc = task.getResult();
                        User user = doc.toObject(User.class);
                        onCompleteCallback.callback(user);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }


    // Get existing channel from Firestore, returns null on error
    public static void getChannel(String cid, Callback<Channel> onCompleteCallback) {
        if (cid == null)
            onCompleteCallback.callback(null);
        db.collection("channels").document(cid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot doc = task.getResult();
                        Channel channel = doc.toObject(Channel.class);
                        onCompleteCallback.callback(channel);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // Get existing message from Firestore, returns null on error
    public static void getMessage(String mid, Callback<Message> onCompleteCallback) {
        db.collection("messages").document(mid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot doc = task.getResult();
                        Message message = doc.toObject(Message.class);
                        onCompleteCallback.callback(message);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // Get all messages from user as a list
    public static void getMessagesByUser(String uid, Callback<List<Message>> onCompleteCallback) {
        db.collection("messages").whereEqualTo("uid", uid)
                .orderBy("timestamp")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG + ": getMessagesByUser", "" + task.getResult().size());
                List<Message> messages = task.getResult().toObjects(Message.class);
                onCompleteCallback.callback(messages);
            } else {
                Log.d(TAG, task.getException().toString());
                onCompleteCallback.callback(null);
            }
        });
    }

    // Get all channels the user is active in
    public static void getActiveChannels(String uid, Callback<List<Channel>> onCompleteCallback) {
        db.collection("channels").whereEqualTo(uid, true)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Channel> channels = task.getResult().toObjects(Channel.class);
                onCompleteCallback.callback(channels);
            } else {
                Log.d(TAG, task.getException().toString());
                onCompleteCallback.callback(null);
            }
        });
    }

    public static void getMyPrivateChannels(Callback<List<Channel>> onCompleteCallback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("channels").whereEqualTo(user.getUid(), true)
                .whereEqualTo("privateChat", true).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Channel> channels = task.getResult().toObjects(Channel.class);
                onCompleteCallback.callback(channels);
            } else {
                Log.d(TAG, task.getException().toString());
                onCompleteCallback.callback(null);
            }
        });

    }

    // This is used by FirestoreRecyclerAdapter to attach a listener to the query
    public static FirestoreRecyclerOptions<Message> getMessagesByChannelOption(String cid) {
        Query query = db.collection("messages").whereEqualTo("cid", cid)
                .orderBy("timestamp");
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

    }

    // This is used by FirestoreRecyclerAdapter to attach a listener to the query
    public static void getAllPrivateConversations(Callback<List<Message>> onCompleteCallback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            onCompleteCallback.callback(null);
        db.collection("channels").whereEqualTo("privateChat", true).whereEqualTo(user.getUid(), true)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Message> messages = task.getResult().toObjects(Message.class);
                onCompleteCallback.callback(messages);
            } else {
                Log.d(TAG, task.getException().toString());
                onCompleteCallback.callback(null);
            }
        });
    }


    // -- UPDATE --

    // Update a user, return null on error.
    public static void updateUser(User user, Callback<User> onCompleteCallback) {
        db.collection("users").document(user.getUid()).set(user, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onCompleteCallback.callback(user);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // Subscribe to a channel
    public static void channelSubscribe(String cid, Map<String, Boolean> users, Callback<Boolean> onCompleteCallback) {
        if (users == null)
            onCompleteCallback.callback(null);
        db.collection("channels").document(cid).set(users, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onCompleteCallback.callback(true);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(false);
                    }
                });
    }

    // Unsubscribe from a channel
    public static void channelUnsubscribe(String cid, Callback<Boolean> onCompleteCallback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            onCompleteCallback.callback(null);
        Map<String, Boolean> data = new HashMap<String, Boolean>() {{
            put(user.getUid(), false);
        }};
        db.collection("channels").document(cid).set(data, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onCompleteCallback.callback(true);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(false);
                    }
                });
    }

    public static void setPrivate(String cid, boolean p, Callback<Boolean> onCompleteCallback) {
        if (cid == null)
            onCompleteCallback.callback(null);
        Channel c = new Channel(cid);
        c.setPrivateChat(p);
        db.collection("channels").document(cid).set(c, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onCompleteCallback.callback(true);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(false);
                    }
                });
    }

    // -- DELETE --
    // Not used for this project

    public interface Callback<T> {
        void callback(T result);
    }

}
