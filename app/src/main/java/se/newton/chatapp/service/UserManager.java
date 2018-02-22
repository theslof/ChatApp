package se.newton.chatapp.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import se.newton.chatapp.model.User;

public class UserManager {
    private static Map<String, User> users = new HashMap<>();

    private UserManager(){}

    public static User getUser(String uid){
        if(!users.containsKey(uid))
            return createUser(uid);
        return users.get(uid);
    }

    private static User createUser(String uid){
        User user = new User(uid);
        users.put(uid, user);
        Database.getUser(uid, u -> {
            user.setDisplayName(u.getDisplayName());
            user.setProfileImage(u.getProfileImage());
            GetBitmapFromURLAsync getBitmapFromURLAsync = new GetBitmapFromURLAsync();
            getBitmapFromURLAsync.execute(user);
        });
        return user;
    }

    private static class GetBitmapFromURLAsync extends AsyncTask<User, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(User... params) {
            try {
                User user = params[0];
                URL url = new URL(user.getProfileImage());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                user.setProfileBitmap(myBitmap);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
