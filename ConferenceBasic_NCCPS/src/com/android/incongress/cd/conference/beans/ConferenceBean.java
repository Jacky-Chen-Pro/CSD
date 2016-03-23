package com.android.incongress.cd.conference.beans;

public class ConferenceBean {
	private int conferencesId;// 会议编号 用于区分会议 这个很重要
	private String abbreviation;// 会议版本名称
	private int adminuserId;// 会议管理员ID
	private String code;
	private String conferencesAddress;// 会议地址
	private String conferencesStartTime;// 会议开始时间
	private String conferencesEndTime;// 会议结束时间
	private String conferencesName;// 会议名称
	private int conferencesState;// 会议状态
	private String createTime;// 会议创建时间
	private String enLink;// 英文链接
	private int posterShowState;// 是否显示壁报
	private int posterState;// 壁报状态
	private int viewState;// 是否查看
	private String zhLink;// 中文链接

	public int getConferencesId() {
		return conferencesId;
	}

	public void setConferencesId(int conferencesId) {
		this.conferencesId = conferencesId;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public int getAdminuserId() {
		return adminuserId;
	}

	public void setAdminuserId(int adminuserId) {
		this.adminuserId = adminuserId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getConferencesAddress() {
		return conferencesAddress;
	}

	public void setConferencesAddress(String conferencesAddress) {
		this.conferencesAddress = conferencesAddress;
	}

	public String getConferencesStartTime() {
		return conferencesStartTime;
	}

	public void setConferencesStartTime(String conferencesStartTime) {
		this.conferencesStartTime = conferencesStartTime;
	}

	public String getConferencesEndTime() {
		return conferencesEndTime;
	}

	public void setConferencesEndTime(String conferencesEndTime) {
		this.conferencesEndTime = conferencesEndTime;
	}

	public String getConferencesName() {
		return conferencesName;
	}

	public void setConferencesName(String conferencesName) {
		this.conferencesName = conferencesName;
	}

	public int getConferencesState() {
		return conferencesState;
	}

	public void setConferencesState(int conferencesState) {
		this.conferencesState = conferencesState;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEnLink() {
		return enLink;
	}

	public void setEnLink(String enLink) {
		this.enLink = enLink;
	}

	public int getPosterShowState() {
		return posterShowState;
	}

	public void setPosterShowState(int posterShowState) {
		this.posterShowState = posterShowState;
	}

	public int getPosterState() {
		return posterState;
	}

	public void setPosterState(int posterState) {
		this.posterState = posterState;
	}

	public int getViewState() {
		return viewState;
	}

	public void setViewState(int viewState) {
		this.viewState = viewState;
	}

	public String getZhLink() {
		return zhLink;
	}

	public void setZhLink(String zhLink) {
		this.zhLink = zhLink;
	}
}
