package se.newton.chatapp.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import se.newton.chatapp.BR;
import se.newton.chatapp.R;
import se.newton.chatapp.model.User;
import se.newton.chatapp.service.Database;

/**
 * Created by fr9b on 2018-02-21.
 */

public class ProfileViewModel extends BaseObservable {
    private User user = new User();
    private String uid = "";


    public ProfileViewModel(String uid) {
        Database.getUser(uid, this::setUser);
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.userName);
        notifyPropertyChanged(BR.profileImage);
        notifyPropertyChanged(BR.userBio);
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
    public static void loadProfileImage(CircleImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_profile_image_placeholder)
                .dontAnimate()
                .into(view);
    }

    public void startPrivateChannelBtn() {
        // Start new chat channel with user
    }

}

