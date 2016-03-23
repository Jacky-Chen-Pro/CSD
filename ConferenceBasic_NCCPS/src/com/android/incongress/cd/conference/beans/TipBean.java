package com.android.incongress.cd.conference.beans;

public class TipBean {
	// 基本信息
	private int tipsId;// 基本信息编号
	private int conferencesId;// 参会议编号
	private String tipsContent;// 基本信息
	private String tipsTime;// 创建时间
	private String tipsTitle;// 标题
	private String tipsTitle_En;// 标题 英语
	private String tipsContent_En;// 内容 英语

	public int getTipsId() {
		return tipsId;
	}

	public void setTipsId(int tipsId) {
		this.tipsId = tipsId;
	}

	public int getConferencesId() {
		return conferencesId;
	}

	public void setConferencesId(int conferencesId) {
		this.conferencesId = conferencesId;
	}

	public String getTipsContent() {
		return tipsContent;
	}

	public void setTipsContent(String tipsContent) {
		this.tipsContent = tipsContent;
	}

	public String getTipsTime() {
		return tipsTime;
	}

	public void setTipsTime(String tipsTime) {
		this.tipsTime = tipsTime;
	}

	public String getTipsTitle() {
		return tipsTitle;
	}

	public void setTipsTitle(String tipsTitle) {
		this.tipsTitle = tipsTitle;
	}

	public String getTipsTitle_En() {
		return tipsTitle_En;
	}

	public void setTipsTitle_En(String tipsTitle_En) {
		this.tipsTitle_En = tipsTitle_En;
	}

	public String getTipsContent_En() {
		return tipsContent_En;
	}

	public void setTipsContent_En(String tipsContent_En) {
		this.tipsContent_En = tipsContent_En;
	}

}
