package se.newton.chatapp.service;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import se.newton.chatapp.R;
import se.newton.chatapp.model.Channel;

public class ChannelManager {
    private static Map<String, Channel> channels = new HashMap<>();

    private ChannelManager() {
    }

    public static Channel getChannel(String cid) {
        if (!channels.containsKey(cid))
            return createChannel(cid);
        return channels.get(cid);
    }

    private static Channel createChannel(String cid) {
        Channel channel = new Channel(cid);
        channels.put(cid, channel);
        Database.createChannel(channel, c -> {
            channel.setCid(c.getCid());
            channel.setPrivateChat(c.isPrivateChat());
        });
        return channel;
    }
}
