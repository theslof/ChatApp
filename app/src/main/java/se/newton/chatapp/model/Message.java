package se.newton.chatapp.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;

    private String uid;
    private String mid;
    private String cid;
    private String data;
    private int messageType;

    // ServerTimestamp is filled in by the Firestore server as the message is created, meaning that
    //  we will not get strange timestamp issues if people chat from different locales, or have the
    //  wrong time set.
    @ServerTimestamp
    private Date timestamp;

    public Message(){}

    public Message(int messageType, String data){
        this();
        this.messageType = messageType;
        this.data = data;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
