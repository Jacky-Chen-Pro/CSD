package com.android.incongress.cd.conference.beans;

public class CommunityTopicContentBean {
	private String userName;// 创建者
	private String time;// 创建时间
	private String content;// 评论内容
	private int type;// 回帖类型：1普通用户回帖 2专家回帖

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
