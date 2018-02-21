package se.newton.chatapp.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import se.newton.chatapp.BR;
import se.newton.chatapp.model.User;

/**
 * Created by fr9b on 2018-02-21.
 */

public class ProfileViewModel extends BaseObservable {
    private User user = new User();



    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.userName);
        notifyPropertyChanged(BR.profileImage);
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
}
