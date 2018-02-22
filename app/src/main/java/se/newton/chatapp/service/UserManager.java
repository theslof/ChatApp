package se.newton.chatapp.service;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import se.newton.chatapp.R;
import se.newton.chatapp.model.User;

public class UserManager {
    private static Map<String, User> users = new HashMap<>();

    private UserManager() {
    }

    public static User getUser(RequestManager glideManager, String uid) {
        if (!users.containsKey(uid))
            return createUser(glideManager, uid);
        return users.get(uid);
    }

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
                                    .placeholder(R.drawable.ic_profile_image_placeholder)
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
