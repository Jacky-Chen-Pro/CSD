package com.android.incongress.cd.conference.beans;

public class MeetingBean_new {
    // 会议演讲
    private int meetingId;// 会议演讲Id编号
    private String topic;// 会议演讲主题
    private String meetingDay;// 会议演讲日期
    private String startTime;// 会议演讲开始时间
    private String endTime;// 会议演讲结束时间
    private int classesId;// 会议室编号
    private int sessionGroupId;// 这个表明是那个会议的 演讲 这个是SESSION表的主键
    private String topic_En;// 会议演讲主题 英文
    private int roleId;//身份
    private String roleName;//身份的中文名
    private String roleNameEn;//身份的英文名
    private String className; //会议室名称
    private String classEnName;//会议室名称
    private int isSessionOrMeeting;//session 是1，meeting是2

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMeetingDay() {
        return meetingDay;
    }

    public void setMeetingDay(String meetingDay) {
        this.meetingDay = meetingDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getClassesId() {
        return classesId;
    }

    public void setClassesId(int classesId) {
        this.classesId = classesId;
    }

    public int getSessionGroupId() {
        return sessionGroupId;
    }

    public void setSessionGroupId(int sessionGroupId) {
        this.sessionGroupId = sessionGroupId;
    }

    public String getTopic_En() {
        return topic_En;
    }

    public void setTopic_En(String topic_En) {
        this.topic_En = topic_En;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNameEn() {
        return roleNameEn;
    }

    public void setRoleNameEn(String roleNameEn) {
        this.roleNameEn = roleNameEn;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassEnName() {
        return classEnName;
    }

    public void setClassEnName(String classEnName) {
        this.classEnName = classEnName;
    }

    @Override
    public String toString() {
        return "MeetingBean_new{" +
                "meetingId=" + meetingId +
                ", topic='" + topic + '\'' +
                ", meetingDay='" + meetingDay + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", classesId=" + classesId +
                ", sessionGroupId=" + sessionGroupId +
                ", topic_En='" + topic_En + '\'' +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleNameEn='" + roleNameEn + '\'' +
                ", className='" + className + '\'' +
                ", classEnName='" + classEnName + '\'' +
                '}';
    }

    public MeetingBean_new(int meetingId, String topic, String meetingDay, String startTime, String endTime, int classesId, int sessionGroupId, String topic_En, int roleId, String roleName, String roleNameEn, String className, String classEnName,int isSessionOrMeeting) {
        this.meetingId = meetingId;
        this.topic = topic;
        this.meetingDay = meetingDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classesId = classesId;
        this.sessionGroupId = sessionGroupId;
        this.topic_En = topic_En;
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleNameEn = roleNameEn;
        this.className = className;
        this.classEnName = classEnName;
        this.isSessionOrMeeting = isSessionOrMeeting;
    }

    public int getIsSessionOrMeeting() {
        return isSessionOrMeeting;
    }

    public void setIsSessionOrMeeting(int isSessionOrMeeting) {
        this.isSessionOrMeeting = isSessionOrMeeting;
    }
}
