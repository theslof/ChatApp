package se.newton.chatapp.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

public class MessageViewModel extends BaseObservable {
    private User user;
    private Message message;
    private Date timestamp;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MessageViewModel(User user, Message message) {
        this.user = user;
        this.message = message;
        this.timestamp = message.getTimestamp();
    }

    @Bindable
    public String getUserName() {
        return user.getDisplayName();
    }

    @Bindable
    public String getProfileImage() {
        return user.getProfileImage();
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
    public static void loadImage(CircleImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_profile_image_placeholder)
                .dontAnimate()
                .into(view);
    }
}
