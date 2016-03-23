package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

/**
 * 用户所主持的活动或者参加的发言
 * @author Administrator
 *
 */
public class ActivityBean implements Serializable{
	private static final long serialVersionUID = -706064338600464481L;

	private String time;
	private String location;
	private String locationEn;
	private int meetingId;
	private String activityName;
	private String activityNameEN;
	private int isSessionOrMeeting; // session是0，meeting是1
	private String start_time;
	private String end_time;
	private String date;

	//身份类型
	private int type;
	//身份类型名称
	private String typeName;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public int getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(int meetingId) {
		this.meetingId = meetingId;
	}
	
	public String getActivityNameEN() {
		return activityNameEN;
	}
	
	public void setActivityNameEN(String activityNameEN) {
		this.activityNameEN = activityNameEN;
	}
	
	public int getIsSessionOrMeeting() {
		return isSessionOrMeeting;
	}
	public void setIsSessionOrMeeting(int isSessionOrMeeting) {
		this.isSessionOrMeeting = isSessionOrMeeting;
	}
	
	public ActivityBean(){}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public String getLocationEn() {
		return locationEn;
	}

	public void setLocationEn(String locationEn) {
		this.locationEn = locationEn;
	}

	@Override
	public String toString() {
		return "ActivityBean{" +
				"time='" + time + '\'' +
				", location='" + location + '\'' +
				", locationEn='" + locationEn + '\'' +
				", meetingId=" + meetingId +
				", activityName='" + activityName + '\'' +
				", activityNameEN='" + activityNameEN + '\'' +
				", isSessionOrMeeting=" + isSessionOrMeeting +
				", start_time='" + start_time + '\'' +
				", end_time='" + end_time + '\'' +
				", date='" + date + '\'' +
				", type=" + type +
				", typeName='" + typeName + '\'' +
				'}';
	}
}
