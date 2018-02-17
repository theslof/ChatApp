package se.newton.chatapp.service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;

import se.newton.chatapp.model.Channel;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.model.User;

public final class Database {
    private static final String TAG = "Database";

    private Database() {
    }


    // ---- CRUD methods ----


    // -- CREATE --

    // Creates a new user, or returns existing user if it already exists.
    public static void createUser(String uid, Callback<User> onCompleteCallback) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
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
                    }else{
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // Creates a new channel, or returns existing channel if it already exists.
    public static void createChannel(String cid, Callback<Channel> onCompleteCallback) {
        FirebaseFirestore.getInstance().collection("channels").document(cid).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            onCompleteCallback.callback(doc.toObject(Channel.class));
                        } else {
                            Channel channel = new Channel(cid);
                            doc.getReference().set(channel).addOnCompleteListener(res -> {
                                if (res.isSuccessful()) {
                                    onCompleteCallback.callback(channel);
                                } else {
                                    Log.d(TAG, task.getException().toString());
                                    onCompleteCallback.callback(null);
                                }
                            });
                        }
                    }else{
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });

    }

    // Creates a new message and returns a Message object
    public static void createMessage(int messageType, String data, String cid, Callback<Message> onCompleteCallback) {
        DocumentReference doc = FirebaseFirestore.getInstance().collection("messages").document();
        Message message = new Message(messageType, data);
        message.setCid(cid);
        message.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        message.setMid(doc.getId());
        doc.set(message)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        message.setMid(doc.getId());
                        doc.set(new HashMap<>().put("mid", doc.getId()), SetOptions.merge());
                        onCompleteCallback.callback(message);
                    }else{
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }


    // -- READ --

    // Get existing user from Firestore, returns null on error
    public static void getUser(String uid, Callback<User> onCompleteCallback) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
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
        FirebaseFirestore.getInstance().collection("channels").document(cid).get()
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
        FirebaseFirestore.getInstance().collection("messages").document(mid).get()
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
        FirebaseFirestore.getInstance().collection("messages").whereEqualTo("uid", uid)
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

    // Get all messages from channel as a list
    public static void getMessagesByChannel(String cid, Callback<List<Message>> onCompleteCallback){
        FirebaseFirestore.getInstance().collection("messages").whereEqualTo("cid", cid)
                .orderBy("timestamp").get().addOnCompleteListener(task -> {
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
        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).set(user, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onCompleteCallback.callback(user);
                    } else {
                        Log.d(TAG, task.getException().toString());
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // -- DELETE --
    // Not used for this project

    public interface Callback<T> {
        void callback(T result);
    }

}
