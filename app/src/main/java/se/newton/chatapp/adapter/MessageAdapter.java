package se.newton.chatapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import se.newton.chatapp.R;
import se.newton.chatapp.databinding.MessageItemBinding;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.viewmodel.MessageViewModel;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    private final RequestManager glideManager;

    //  options - This is the object that handles Firestore communications. Thanks to this we only
    //      need to specify a query, the adapter does the rest of the work.
    //  glideManager - This is out way to communicate with the Glide library, letting us manage
    //      background loading of images.
    public MessageAdapter(FirestoreRecyclerOptions<Message> options, RequestManager glideManager) {
        super(options);
        this.glideManager = glideManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the message_item layout with data bindings
        View v = MessageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false).getRoot();

        // Find the ViewStub object, this is where we place out message (be it text, image, etc)
        ViewStub stub = v.findViewById(R.id.messageView);
        switch (viewType) {
            case Message.TYPE_TEXT:
                // It's a text message, so we select the text_message layout
                stub.setLayoutResource(R.layout.text_message);
                break;
            case Message.TYPE_IMAGE:
                // It's an image, so we select the image_message layout
                stub.setLayoutResource(R.layout.image_message);
                break;
            default:
                // It's an unknown (or not yet implemented) layout, select a text box informing the user
                stub.setLayoutResource(R.layout.unknown_message);
                break;
        }

        // Inflate the selected layout for the view stub
        stub.inflate();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, @NonNull Message message) {

        // A message is supposed to be drawn to the screen. Create a new ViewModel and attach our
        //  glideManager as well as the message object.
        MessageViewModel viewModel = new MessageViewModel(glideManager, message);

        // Have the ViewModel check if the layout should be aligned to the left or right
        viewModel.setOrientation(holder.itemView);

        // Connect the data bindings
        holder.binding.setViewModel(viewModel);
        holder.binding.executePendingBindings();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

        // The message view is no longer visible and should be used for another message,
        //  abort all Glide processes for that message (so we don't keep loading an image in the
        //  background for a message that no longer is visible).
        glideManager.clear(holder.itemView);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMessageType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MessageItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
