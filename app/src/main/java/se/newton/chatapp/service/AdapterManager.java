package se.newton.chatapp.service;

import com.bumptech.glide.RequestManager;

import java.util.HashMap;
import java.util.Map;

import se.newton.chatapp.adapter.MessageAdapter;

public class AdapterManager {
    private static Map<String, MessageAdapter> adapters = new HashMap<>();

    private AdapterManager() {
    }

    public static MessageAdapter getAdapter(RequestManager glideManager, String cid) {
        if (!adapters.containsKey(cid)) {
            MessageAdapter adapter = new MessageAdapter(Database.getMessagesByChannelOption(cid), glideManager);
            adapters.put(cid, adapter);
            return adapter;
        }
        return adapters.get(cid);
    }
}
