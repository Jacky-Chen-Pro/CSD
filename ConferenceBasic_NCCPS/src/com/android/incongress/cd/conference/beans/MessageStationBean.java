package com.android.incongress.cd.conference.beans;

/**
 * Created by Administrator on 2015/3/31.
 *
 * 消息站使用的bean
 */
public class MessageStationBean {
    private String messageTitle;
    private String messageTime;
    /** 是否显示 **/
    private int isShow;
    private int messageId;

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "MessageStationBean{" +
                "messageTitle='" + messageTitle + '\'' +
                ", messageTime='" + messageTime + '\'' +
                ", isShow=" + isShow +
                ", messageId=" + messageId +
                '}';
    }

    public MessageStationBean(){

    }

    public MessageStationBean(String messageTitle, String messageTime, int isShow, int messageId) {
        this.messageTitle = messageTitle;
        this.messageTime = messageTime;
        this.isShow = isShow;
        this.messageId = messageId;
    }
}
