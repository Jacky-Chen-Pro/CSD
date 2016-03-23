package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Jacky on 2016/1/13.
 */
public class ScenicXiuBean {
    private int sceneShowId;
    private int type;
    private String author;
    private String timeShow;
    private String authorImg;
    private String title;
    private String htmlUrl;
    private String imgUrls;
    private String content;
    private String logoUrl;
    private String answerContent;
    private String answerUserName;
    private int laudCount;
    private int isLaud;
    private int commentCount;
    private List<CommentArrayBean> commentArray;

    public ScenicXiuBean() {
    }

    public ScenicXiuBean(int type) {
        this.type = type;
    }

    public int getSceneShowId() {
        return sceneShowId;
    }

    public void setSceneShowId(int sceneShowId) {
        this.sceneShowId = sceneShowId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

    public String getAuthorImg() {
        return authorImg;
    }

    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getLaudCount() {
        return laudCount;
    }

    public void setLaudCount(int laudCount) {
        this.laudCount = laudCount;
    }

    public int getIsLaud() {
        return isLaud;
    }

    public void setIsLaud(int isLaud) {
        this.isLaud = isLaud;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<CommentArrayBean> getCommentArray() {
        return commentArray;
    }

    public void setCommentArray(List<CommentArrayBean> commentArray) {
        this.commentArray = commentArray;
    }


    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    @Override
    public String toString() {
        return "ScenicXiuBean{" +
                "sceneShowId=" + sceneShowId +
                ", type=" + type +
                ", author='" + author + '\'' +
                ", timeShow='" + timeShow + '\'' +
                ", authorImg='" + authorImg + '\'' +
                ", title='" + title + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", imgUrls='" + imgUrls + '\'' +
                ", content='" + content + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", answerUserName='" + answerUserName + '\'' +
                ", laudCount=" + laudCount +
                ", isLaud=" + isLaud +
                ", commentCount=" + commentCount +
                ", commentArray=" + commentArray +
                '}';
    }

    public String getAnswerUserName() {
        return answerUserName;
    }

    public void setAnswerUserName(String answerUserName) {
        this.answerUserName = answerUserName;
    }
}
