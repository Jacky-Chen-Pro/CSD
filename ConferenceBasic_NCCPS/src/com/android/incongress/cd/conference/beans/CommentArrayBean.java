package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2016/1/13.
 */
public class CommentArrayBean {
    private String content;
    private int parentId;
    private String userName;
    private String timeShow;
    private int commentId;
    private String parentName;
    private int userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CommentArrayBean{" +
                "content='" + content + '\'' +
                ", parentId=" + parentId +
                ", userName='" + userName + '\'' +
                ", timeShow='" + timeShow + '\'' +
                ", commentId=" + commentId +
                ", parentName='" + parentName + '\'' +
                ", userId=" + userId +
                '}';
    }

    public CommentArrayBean() {
    }

    public CommentArrayBean(String content, int parentId, String userName, String timeShow, int commentId, String parentName, int userId) {
        this.content = content;
        this.parentId = parentId;
        this.userName = userName;
        this.timeShow = timeShow;
        this.commentId = commentId;
        this.parentName = parentName;
        this.userId = userId;
    }
}
