package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

public class MeetingBean implements Serializable{
	private static final long serialVersionUID = -7123884514460014451L;

	// 会议演讲
	private int meetingId;// 会议演讲Id编号
	private String topic;// 会议演讲主题
	private String summary;// 会议演讲概要
	private int classesId;// 会议室编号
	private String speakerIds;// 演讲者列表
	private String meetingDay;// 会议演讲日期
	private String startTime;// 会议演讲开始时间
	private String endTime;// 会议演讲结束时间
	private int conFieldId;// 领域编号
	private int sessionGroupId;// 这个表明是那个会议的 演讲 这个是SESSION表的主键
	private int attention;// 关注
	private String topic_En;// 会议演讲主题 英文
	private String summary_En;// 会议演讲概要 英文
	private boolean checked;
	private String facultyIds;//演讲者
	private String roleIds;//身份
	
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getClassesId() {
		return classesId;
	}
	public void setClassesId(int classesId) {
		this.classesId = classesId;
	}
	public String getSpeakerIds() {
		return speakerIds;
	}
	public void setSpeakerIds(String speakerIds) {
		this.speakerIds = speakerIds;
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
	public int getConFieldId() {
		return conFieldId;
	}
	public void setConFieldId(int conFieldId) {
		this.conFieldId = conFieldId;
	}
	public int getSessionGroupId() {
		return sessionGroupId;
	}
	public void setSessionGroupId(int sessionGroupId) {
		this.sessionGroupId = sessionGroupId;
	}
	public int getAttention() {
		return attention;
	}
	public void setAttention(int attention) {
		this.attention = attention;
	}
	public String getTopic_En() {
		return topic_En;
	}
	public void setTopic_En(String topic_En) {
		this.topic_En = topic_En;
	}
	public String getSummary_En() {
		return summary_En;
	}
	public void setSummary_En(String summary_En) {
		this.summary_En = summary_En;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getFacultyIds() {
		return facultyIds;
	}

	public void setFacultyIds(String facultyIds) {
		this.facultyIds = facultyIds;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	@Override
	public String toString() {
		return "MeetingBean{" +
				"meetingId=" + meetingId +
				", topic='" + topic + '\'' +
				", summary='" + summary + '\'' +
				", classesId=" + classesId +
				", speakerIds='" + speakerIds + '\'' +
				", meetingDay='" + meetingDay + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", conFieldId=" + conFieldId +
				", sessionGroupId=" + sessionGroupId +
				", attention=" + attention +
				", topic_En='" + topic_En + '\'' +
				", summary_En='" + summary_En + '\'' +
				", checked=" + checked +
				", facultyIds='" + facultyIds + '\'' +
				", roleIds='" + roleIds + '\'' +
				'}';
	}
}
