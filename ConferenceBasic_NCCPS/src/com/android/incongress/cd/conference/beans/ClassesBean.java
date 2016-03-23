package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

public class ClassesBean implements Serializable{
	private static final long serialVersionUID = -70612354460014451L;

	// 会议室Bean
	private int classesId;// 会议室编号
	private int conferencesId;// 参会议编号
	private int classesCapacity;// 会议室容纳人数
	private String classesCode;// 会议室名称
	private String classesCodeEn;//会议室名称
	private String classesLocation;// 会议室位置
	private String createTime;// 创建时间
	private String mapName;// 地图图片
	private int level;//会议室排列等级
	private boolean checked = true;
	private String classesCodePingyin;// 会议室名称的拼音

	public String getClassesCodePingyin() {
		return classesCodePingyin;
	}

	public void setClassesCodePingyin(String classesCodePingyin) {
		this.classesCodePingyin = classesCodePingyin;
	}

	public int getClassesId() {
		return classesId;
	}

	public void setClassesId(int classesId) {
		this.classesId = classesId;
	}

	public int getConferencesId() {
		return conferencesId;
	}

	public void setConferencesId(int conferencesId) {
		this.conferencesId = conferencesId;
	}

	public int getClassesCapacity() {
		return classesCapacity;
	}

	public void setClassesCapacity(int classesCapacity) {
		this.classesCapacity = classesCapacity;
	}

	public String getClassesCode() {
		return classesCode;
	}

	public void setClassesCode(String classesCode) {
		this.classesCode = classesCode;
	}

	public String getClassesLocation() {
		return classesLocation;
	}

	public void setClassesLocation(String classesLocation) {
		this.classesLocation = classesLocation;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	@Override
	public boolean equals(Object o) {
	    ClassesBean bean = (ClassesBean) o;
	    return bean.classesId == this.classesId;
	}


	public String getClassesCodeEn() {
		return classesCodeEn;
	}

	public void setClassesCodeEn(String classesCodeEn) {
		this.classesCodeEn = classesCodeEn;
	}
}
