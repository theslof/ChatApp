package se.newton.chatapp.model;

import android.net.Uri;

public class User {
    private String uid = "";
    private String displayName = "";
    private String profileImage;
    private String bio = "";

    //ToDo: List<Channel>
    //List<Users>

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
