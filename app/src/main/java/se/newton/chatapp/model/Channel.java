package se.newton.chatapp.model;

public class Channel {
    private String cid;

    public Channel(){}

    public Channel(String cid){
        this();
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
