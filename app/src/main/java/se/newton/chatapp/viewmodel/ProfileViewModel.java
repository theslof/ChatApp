package se.newton.chatapp.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import se.newton.chatapp.BR;
import se.newton.chatapp.R;
import se.newton.chatapp.model.User;
import se.newton.chatapp.service.Database;
import se.newton.chatapp.service.UserManager;

/**
 * Created by fr9b on 2018-02-21.
 */

public class ProfileViewModel extends BaseObservable {
    private User user = new User();
    private String uid;
    private RequestManager glideManager;


    public ProfileViewModel(RequestManager glideManager, String uid) {
        user = UserManager.getUser(glideManager, uid);
        this.glideManager = glideManager;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
        //notifyPropertyChanged(BR.userName);
        //notifyPropertyChanged(BR.profileImage);
        //notifyPropertyChanged(BR.userBio);
    }

    @Bindable
    public User getUser() {
        return user;
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
    public String getUserBio() {
        return user.getBio();
    }

    @BindingAdapter("android:profile")
    public static void loadProfileImage(ImageView view, Bitmap image) {
        if (image == null)
            view.setImageResource(R.drawable.ic_profile_image_placeholder);
        else
            view.setImageBitmap(image);
    }

    public void startPrivateChannelBtn() {
        // Start new chat channel with user
    }

}

