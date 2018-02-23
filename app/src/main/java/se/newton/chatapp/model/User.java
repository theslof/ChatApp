package se.newton.chatapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;

import com.google.firebase.firestore.Exclude;

import se.newton.chatapp.BR;

public class User extends BaseObservable{
    private String uid = "";
    private String displayName = "Anonymous";
    private String profileImage;

    //List<Channel>
    //List<Users>
    @Exclude private Bitmap profileBitmap;

    public User(){}

    public User(String uid){
        this();
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Bindable
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        notifyPropertyChanged(BR.displayName);
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Bindable
    public Bitmap getProfileBitmap() {
        return profileBitmap;
    }

    public void setProfileBitmap(Bitmap profileBitmap) {
        this.profileBitmap = profileBitmap;
        notifyPropertyChanged(BR.profileBitmap);
    }
}
