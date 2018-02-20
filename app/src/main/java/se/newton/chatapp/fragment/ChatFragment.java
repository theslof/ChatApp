package se.newton.chatapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.newton.chatapp.R;
import se.newton.chatapp.adapter.MessageAdapter;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.model.User;
import se.newton.chatapp.service.Database;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private String cid;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String cid) {
        Log.d(TAG, "Creating a new fragment");
        ChatFragment fragment = new ChatFragment();
        fragment.cid = cid;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflating fragment");
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Database.getMessagesByChannel(cid, this::pushMessages);
    }

    private void pushMessages(List<Message> messages){
        ((RecyclerView) getActivity().findViewById(R.id.messageList)).setAdapter(new MessageAdapter(messages));
    }

}
