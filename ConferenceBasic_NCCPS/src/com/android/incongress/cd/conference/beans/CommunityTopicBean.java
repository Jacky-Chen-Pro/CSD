package com.android.incongress.cd.conference.beans;

public class CommunityTopicBean {
	private int titleId;// 帖子Id
	private String userName;// 创建者名字
	private int lauy;// 被顶数
	private int count;// 被评论数
	private String title;// 帖子内容
	private String speaker;// 讲者名称
	private String sessionName;// 会议名称
	private String time;// 创建时间
	private String isLaud;// 是否点赞，1是已赞，0是未赞

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLauy() {
		return lauy;
	}

	public void setLauy(int lauy) {
		this.lauy = lauy;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsLaud() {
		return isLaud;
	}

	public void setIsLaud(String isLaud) {
		this.isLaud = isLaud;
	}

}
