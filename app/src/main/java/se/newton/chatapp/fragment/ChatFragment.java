package se.newton.chatapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import se.newton.chatapp.R;
import se.newton.chatapp.adapter.MessageAdapter;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.service.Database;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private String cid;
    private MessageAdapter adapter;

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

        adapter = new MessageAdapter(Glide.with(this));
        Database.getMessagesByChannel(cid, adapter::setMessages);

        Activity activity = getActivity();
        ((RecyclerView) activity.findViewById(R.id.messageList)).setAdapter(adapter);
        activity.findViewById(R.id.buttonSend).setOnClickListener(view -> {
            TextView messageText = activity.findViewById(R.id.messageText);
            if(messageText.getText().length() > 0)
                Database.createMessage(Message.TYPE_TEXT, messageText.getText().toString(), cid,
                        message -> {
                            adapter.getMessages().add(message);
                            adapter.notifyDataSetChanged();
                        });
                messageText.setText("");
        });

        activity.findViewById(R.id.buttonAttach).setOnClickListener(view -> {
            Database.createMessage(Message.TYPE_IMAGE, FirebaseAuth.getInstance().getCurrentUser()
                    .getPhotoUrl().toString(), cid, message -> {
                adapter.getMessages().add(message);
                adapter.notifyDataSetChanged();
            });
        });


    }
}
