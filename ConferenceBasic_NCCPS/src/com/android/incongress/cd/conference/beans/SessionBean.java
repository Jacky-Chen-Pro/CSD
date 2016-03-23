package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

public class SessionBean implements Serializable {
	private static final long serialVersionUID = -706125544600464481L;

	// 会议表 SESSION
	private int sessionGroupId;// 会议编号 通过去在MEETING查找 演讲
	private String sessionName;// 会议名称
	private int classesId;// 会议室编号
	private String sessionDay;// 会议日期
	private String startTime;// 会议开始时间
	private String endTime;// 会议结束时间
	private int conFieldId;// 领域编号
	private String remark;// 摘要
	private int attention;// 关注
	private String sessionName_En;// 会议名称 英文
	private boolean checked;
	private String facultyId;//演讲者Id
	private String roleId;//身份id

	public int getSessionGroupId() {
		return sessionGroupId;
	}
	public void setSessionGroupId(int sessionGroupId) {
		this.sessionGroupId = sessionGroupId;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public int getClassesId() {
		return classesId;
	}
	public void setClassesId(int classesId) {
		this.classesId = classesId;
	}
	public String getSessionDay() {
		return sessionDay;
	}
	public void setSessionDay(String sessionDay) {
		this.sessionDay = sessionDay;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getAttention() {
		return attention;
	}
	public void setAttention(int attention) {
		this.attention = attention;
	}
	public String getSessionName_En() {
		return sessionName_En;
	}
	public void setSessionName_En(String sessionName_En) {
		this.sessionName_En = sessionName_En;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}


	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "SessionBean{" +
				"sessionGroupId=" + sessionGroupId +
				", sessionName='" + sessionName + '\'' +
				", classesId=" + classesId +
				", sessionDay='" + sessionDay + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", conFieldId=" + conFieldId +
				", remark='" + remark + '\'' +
				", attention=" + attention +
				", sessionName_En='" + sessionName_En + '\'' +
				", checked=" + checked +
				", facultyId='" + facultyId + '\'' +
				", roleId='" + roleId + '\'' +
				'}';
	}
}
