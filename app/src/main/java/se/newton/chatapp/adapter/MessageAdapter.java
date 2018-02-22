package se.newton.chatapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.newton.chatapp.R;
import se.newton.chatapp.databinding.MessageItemBinding;
import se.newton.chatapp.databinding.TextMessageBinding;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.viewmodel.MessageViewModel;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    private List<Message> messages = new ArrayList<>();

    public MessageAdapter() {
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = MessageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false).getRoot();
        ViewStub stub = v.findViewById(R.id.messageView);
        switch (viewType) {
            case Message.TYPE_TEXT:
                stub.setLayoutResource(R.layout.text_message);
                break;
            case Message.TYPE_IMAGE:
                stub.setLayoutResource(R.layout.image_message);
                break;
            default:
                stub.setLayoutResource(R.layout.unknown_message);
                break;
        }
        stub.inflate();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        MessageViewModel viewModel = new MessageViewModel(message);
        viewModel.setOrientation(holder.itemView);
        holder.binding.setViewModel(viewModel);
        holder.binding.executePendingBindings();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

        Glide.clear(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getMessageType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MessageItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
