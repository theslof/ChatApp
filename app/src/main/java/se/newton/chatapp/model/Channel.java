package se.newton.chatapp.model;

public class Channel {
    private String cid;
    private boolean privateChat;

    public Channel(){}

    public Channel(String cid){
        this();
        this.cid = cid;
        this.privateChat = false;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public boolean isPrivateChat() {
        return privateChat;
    }

    public void setPrivateChat(boolean privateChat) {
        this.privateChat = privateChat;
    }
}
