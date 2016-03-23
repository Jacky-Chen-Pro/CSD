package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

/**
 * Created by Jacky on 2016/1/22.
 *
 * 讲者获取提问列表的javaBean
 */
public class SceneShowArrayBean implements Serializable {
    private static final long serialVersionUID = -7060210544479664481L;

    private String content;
    private String author;
    private int sceneShowId;
    private String timeShow;
    private int isHuiFu;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getSceneShowId() {
        return sceneShowId;
    }

    public void setSceneShowId(int sceneShowId) {
        this.sceneShowId = sceneShowId;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

    public int getIsHuiFu() {
        return isHuiFu;
    }

    public void setIsHuiFu(int isHuiFu) {
        this.isHuiFu = isHuiFu;
    }

    public SceneShowArrayBean(String content) {
        this.content = content;
    }

    public SceneShowArrayBean() {
    }

    public SceneShowArrayBean(String content, String author, int sceneShowId, String timeShow, int isHuiFu) {
        this.content = content;
        this.author = author;
        this.sceneShowId = sceneShowId;
        this.timeShow = timeShow;
        this.isHuiFu = isHuiFu;
    }

    @Override
    public String toString() {
        return "SceneShowArrayBean{" +
                "content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", sceneShowId=" + sceneShowId +
                ", timeShow='" + timeShow + '\'' +
                ", isHuiFu=" + isHuiFu +
                '}';
    }
}
