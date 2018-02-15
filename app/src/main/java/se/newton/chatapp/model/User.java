package se.newton.chatapp.model;

public class User {
    private String uid;
    private String displayName;
    //List<Channel>
    //Profilbild
    //List<Users>


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
}
