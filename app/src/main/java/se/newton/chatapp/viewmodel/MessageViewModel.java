package se.newton.chatapp.viewmodel;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import de.hdodenhof.circleimageview.CircleImageView;
import se.newton.chatapp.BR;
import se.newton.chatapp.R;
import se.newton.chatapp.model.Message;
import se.newton.chatapp.model.User;
import se.newton.chatapp.service.Database;
import se.newton.chatapp.service.UserManager;

public class MessageViewModel extends BaseObservable {
    private User user;
    private Message message;
    private Date timestamp;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final RequestManager glideManager;

    public MessageViewModel(RequestManager glideManager, Message message) {
        this.message = message;
        this.timestamp = message.getTimestamp();
        user = UserManager.getUser(glideManager, message.getUid());
        this.glideManager = glideManager;
//        Database.getUser(message.getUid(), this::setUser);
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public User getUser() {
        return user;
    }

    @Bindable
    public String getMessageText() {
        return message.getData();
    }

    @Bindable
    public String getTimestamp() {
        return dateFormat.format(timestamp);
    }

    @BindingAdapter("android:profile")
    public static void loadProfileImage(CircleImageView view, Bitmap image) {
        if(image == null)
            view.setImageResource(R.drawable.ic_profile_image_placeholder);
        else
            view.setImageBitmap(image);
/*
        Glide.with(view.getContext())
                .load(image)
                .placeholder(R.drawable.ic_profile_image_placeholder)
                .dontAnimate()
                .into(view);
*/
    }

    @BindingAdapter("android:image")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_profile_image_placeholder)
//                .dontAnimate()
                .into(view);
    }

    public void setOrientation(View view){
        String fUser = FirebaseAuth.getInstance().getUid();
        ViewGroup viewGroup = (ViewGroup) view;
        TransitionManager.beginDelayedTransition(viewGroup);
        if(message.getUid().equals(fUser)) {
            Log.d("ViewModel", "Message from you, attempting to shift constraints; uid:" + message.getUid() + ", fuid:" + fUser);
            ConstraintSet set = new ConstraintSet();
            set.clone(viewGroup.getContext(), R.layout.message_item_right);
            set.applyTo(viewGroup.findViewById(R.id.messageConstraintLayout));
        }else{
            ConstraintSet set = new ConstraintSet();
            set.clone(viewGroup.getContext(), R.layout.message_item);
            set.applyTo(viewGroup.findViewById(R.id.messageConstraintLayout));
        }
    }
}
