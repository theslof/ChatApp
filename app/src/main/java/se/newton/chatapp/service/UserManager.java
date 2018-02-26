package se.newton.chatapp.service;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import se.newton.chatapp.R;
import se.newton.chatapp.model.User;

/*
This class is designed for one purpose, to reduce the number of Firestore calls every time we want
information about a user. This is somewhat unsafe, as the first time a unique user is requested the
data is loaded asynchronously from the server and eventually placed in the User object that is
instantly returned by the method. This means that the data inside the User object may be inaccurate
until the client gets a response from the server (for example if there is a communication error).

The upside is that as soon as the data is fetched it is saved and can be instantly accessed.
*/

public class UserManager {
    private static Map<String, User> users = new HashMap<>();

    private UserManager() {
    }

    // Returns a User object. If this user has not been requested before, the initial User object
    //  will be a plain new User() and lack the data specific to that user until the server responds
    //  and the data is updated.
    public static User getUser(RequestManager glideManager, String uid) {
        if (!users.containsKey(uid))
            return createUser(glideManager, uid);
        return users.get(uid);
    }

    // Add a new User to our cache, make a database call for the user data and then initiate a Glide
    //  call to fetch the user's profile image.
    private static User createUser(RequestManager glideManager, String uid) {
        User user = new User(uid);
        users.put(uid, user);
        Database.getUser(uid, u -> {
            user.setDisplayName(u.getDisplayName());
            user.setProfileImage(u.getProfileImage());

            new Thread(() -> {
                try {
                    user.setProfileBitmap(glideManager.asBitmap().load(u.getProfileImage())
                            .apply(RequestOptions
                                    .circleCropTransform()
                                    .placeholder(R.drawable.ic_profile_image_placeholder_circular)
                            )
                            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        return user;
    }
}
