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

    /**
     * Subscribes for push messages when new messages are available.
     * Will display a notification if app isn't running in foreground.
     * @param  cid channel name to subscribe to
     */
    public static void subscribeToTopic(String cid) {
        FirebaseMessaging.getInstance().subscribeToTopic(cid);
    }

    /**
     * Unsubscribes from receiving push messages-
     * @param  cid channel name to unsubscribe from
     */
    public static void unsubscribeToTopic(String cid) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(cid);
    }
}
