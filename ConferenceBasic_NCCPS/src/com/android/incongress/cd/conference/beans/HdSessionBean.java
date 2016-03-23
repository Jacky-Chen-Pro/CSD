package com.android.incongress.cd.conference.beans;

/**
 *
 * Created by Jacky on 2016/1/28.
 *
 */
public class HdSessionBean {
    private int sessionId;
    private String sessionName;
    private String timeShow;
    private String classesName;
    private int isTp;
    private int isHd;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public int getIsTp() {
        return isTp;
    }

    public void setIsTp(int isTp) {
        this.isTp = isTp;
    }

    public int getIsHd() {
        return isHd;
    }

    public void setIsHd(int isHd) {
        this.isHd = isHd;
    }


    @Override
    public String toString() {
        return "HdSessionBean{" +
                "sessionId=" + sessionId +
                ", sessionName='" + sessionName + '\'' +
                ", timeShow='" + timeShow + '\'' +
                ", classesName='" + classesName + '\'' +
                ", isTp=" + isTp +
                ", isHd=" + isHd +
                '}';
    }
}
