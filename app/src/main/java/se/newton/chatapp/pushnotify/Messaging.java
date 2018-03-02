package se.newton.chatapp.pushnotify;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Martin on 2018-03-01.
 */

public final class Messaging {

    private Messaging() {
    }

    public static void registerNotificationChannel() {
        //ToDo: Register a notification channel for >Oreo. Set default channel in manifest.
    }

    public static void subscribeToTopic(String cid) {
        FirebaseMessaging.getInstance().subscribeToTopic(cid);
    }

    public static void unsubscribeToTopic(String cid) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(cid);
    }
}
