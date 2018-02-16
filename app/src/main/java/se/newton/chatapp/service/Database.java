package se.newton.chatapp.service;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import se.newton.chatapp.model.Channel;
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

    public interface Callback<T> {
        void callback(T result);
    }

}
