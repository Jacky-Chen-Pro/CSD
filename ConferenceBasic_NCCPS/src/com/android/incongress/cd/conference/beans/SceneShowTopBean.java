package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2016/1/15.
 */
public class SceneShowTopBean {
    //新闻数据，以及新闻数据图,以及跳转连接
    private int state1;
    private String imgUrl1;
    private String gotoUrl1;

    //展商活动，以及展商活动图，以及跳转连接
    private int state2;
    private String imgUrl2;
    private String gotoUrl2;

    public int getState1() {
        return state1;
    }

    public void setState1(int state1) {
        this.state1 = state1;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getGotoUrl1() {
        return gotoUrl1;
    }

    public void setGotoUrl1(String gotoUrl1) {
        this.gotoUrl1 = gotoUrl1;
    }

    public int getState2() {
        return state2;
    }

    public void setState2(int state2) {
        this.state2 = state2;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getGotoUrl2() {
        return gotoUrl2;
    }

    public void setGotoUrl2(String gotoUrl2) {
        this.gotoUrl2 = gotoUrl2;
    }

    @Override
    public String toString() {
        return "SceneShowTopBean{" +
                "state1=" + state1 +
                ", imgUrl1='" + imgUrl1 + '\'' +
                ", gotoUrl1='" + gotoUrl1 + '\'' +
                ", state2=" + state2 +
                ", imgUrl2='" + imgUrl2 + '\'' +
                ", gotoUrl2='" + gotoUrl2 + '\'' +
                '}';
    }

    public SceneShowTopBean() {
    }

    public SceneShowTopBean(int state1, String imgUrl1, String gotoUrl1, int state2, String imgUrl2, String gotoUrl2) {
        this.state1 = state1;
        this.imgUrl1 = imgUrl1;
        this.gotoUrl1 = gotoUrl1;
        this.state2 = state2;
        this.imgUrl2 = imgUrl2;
        this.gotoUrl2 = gotoUrl2;
    }
}
