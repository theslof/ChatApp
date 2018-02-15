package se.newton.chatapp.model;

public class User {
    private long uid;
    private String displayName;
    //List<Channel>
    //Profilbild
    //List<Users>


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
