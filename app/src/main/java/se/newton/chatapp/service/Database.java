package se.newton.chatapp.service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import se.newton.chatapp.model.Channel;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.model.User;

public final class Database {
    private static final String TAG = "Database";

    private Database() {
    }

    // Creates a new user, or returns existing user if it already exists.
    public static void createUser(String uid, Callback<User> onCompleteCallback){
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Log.d(TAG, "User '" + uid + "' already exists");
                        onCompleteCallback.callback(doc.toObject(User.class));
                    }else{
                        Log.d(TAG, "Creating user '" + uid + "'");
                        User user = new User(uid);
                        FirebaseFirestore.getInstance().collection("users")
                                .document(uid).set(user).addOnCompleteListener(res -> {
                                    if(res.isSuccessful()){
                                        onCompleteCallback.callback(user);
                                    }else{
                                        onCompleteCallback.callback(null);
                                    }
                        });
                    }
                });
    }

    // Get existing user from Firestore, returns null on error
    public static void getUser(String uid, Callback<User> onCompleteCallback) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().exists()){
                        DocumentSnapshot doc = task.getResult();
                        User user = doc.toObject(User.class);
                        onCompleteCallback.callback(user);
                    }else{
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // Creates a new channel, or returns existing channel if it already exists.
    public static void createChannel(String cid, Callback<Channel> onCompleteCallback){
        FirebaseFirestore.getInstance().collection("channels").document(cid).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        onCompleteCallback.callback(doc.toObject(Channel.class));
                    }else{
                        Channel channel = new Channel(cid);
                        FirebaseFirestore.getInstance().collection("channels")
                                .document(cid).set(channel).addOnCompleteListener(res -> {
                            if(res.isSuccessful()){
                                onCompleteCallback.callback(channel);
                            }else{
                                onCompleteCallback.callback(null);
                            }
                        });
                    }
                });

    }

    // Get existing channel from Firestore, returns null on error
    public static void getChannel(String cid, Callback<Channel> onCompleteCallback) {
        FirebaseFirestore.getInstance().collection("channels").document(cid).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().exists()){
                        DocumentSnapshot doc = task.getResult();
                        Channel channel = doc.toObject(Channel.class);
                        onCompleteCallback.callback(channel);
                    }else{
                        onCompleteCallback.callback(null);
                    }
                });
    }

    // Creates a new message and returns a Message object
    public static void createMessage(int messageType, String data, String cid, Callback<Message> onCompleteCallback){
        Message message = new Message(messageType, data);
        message.setCid(cid);
        message.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseFirestore.getInstance().collection("messages").add(message)
                .addOnCompleteListener(task -> {
                    DocumentReference doc = task.getResult();
                    message.setMid(doc.getId());
                    onCompleteCallback.callback(message);
                });
    }

    // Get existing message from Firestore, returns null on error
    public static void getMessage(String mid, Callback<Message> onCompleteCallback) {
        FirebaseFirestore.getInstance().collection("messages").document(mid).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().exists()){
                        DocumentSnapshot doc = task.getResult();
                        Message message = doc.toObject(Message.class);
                        onCompleteCallback.callback(message);
                    }else{
                        onCompleteCallback.callback(null);
                    }
                });
    }

    public interface Callback<T> {
        void callback(T result);
    }

}
