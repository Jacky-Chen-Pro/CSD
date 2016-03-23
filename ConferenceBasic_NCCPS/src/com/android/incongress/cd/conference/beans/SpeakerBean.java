package com.android.incongress.cd.conference.beans;

public class SpeakerBean {
	// 演讲者
	private int speakerId;// 演讲者编号
	private int conferencesId;// 参会议编号
	private String remark;// 备注
	private String speakerFrom;// 演讲者来至于
	private String speakerName;// 演讲者姓名
	private int type;// 类型 1讲者 2主持 新加入了15种身份
	private String speakerNamePingyin;// 名字的拼音
	private String firstLetter;
	private int attention;// 用于@讲者 最近的讲者
	private String enName;//英文名
	private int entityId ;//未知
	private String pykey; //未知

	public int getSpeakerId() {
		return speakerId;
	}
	public void setSpeakerId(int speakerId) {
		this.speakerId = speakerId;
	}
	public int getConferencesId() {
		return conferencesId;
	}
	public void setConferencesId(int conferencesId) {
		this.conferencesId = conferencesId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSpeakerFrom() {
		return speakerFrom;
	}
	public void setSpeakerFrom(String speakerFrom) {
		this.speakerFrom = speakerFrom;
	}
	public String getSpeakerName() {
		return speakerName;
	}
	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSpeakerNamePingyin() {
		return speakerNamePingyin;
	}
	public void setSpeakerNamePingyin(String speakerNamePingyin) {
		this.speakerNamePingyin = speakerNamePingyin;
	}
	public String getFirstLetter() {
		return firstLetter;
	}
	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	public int getAttention() {
		return attention;
	}
	public void setAttention(int attention) {
		this.attention = attention;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public int getEntityId() {
		return entityId;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	public String getPykey() {
		return pykey;
	}
	public void setPykey(String pykey) {
		this.pykey = pykey;
	}

	@Override
	public String toString() {
		return "SpeakerBean{" +
				"speakerId=" + speakerId +
				", conferencesId=" + conferencesId +
				", remark='" + remark + '\'' +
				", speakerFrom='" + speakerFrom + '\'' +
				", speakerName='" + speakerName + '\'' +
				", type=" + type +
				", speakerNamePingyin='" + speakerNamePingyin + '\'' +
				", firstLetter='" + firstLetter + '\'' +
				", attention=" + attention +
				", enName='" + enName + '\'' +
				", entityId=" + entityId +
				", pykey='" + pykey + '\'' +
				'}';
	}
}
