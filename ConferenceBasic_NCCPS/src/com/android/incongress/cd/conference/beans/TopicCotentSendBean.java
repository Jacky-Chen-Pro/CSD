package com.android.incongress.cd.conference.beans;

public class TopicCotentSendBean {
	// 针对帖子发表信息 服务端反回的数据
	private int userType;// 如果用户为游客，才有此项；用户类型 1,为游客
	private int userId;// 同上，用户id 同上指的是(如果用户为游客，才有此项；用户类型 1,为游客)
	private String userName;// 同上，用户名字
	private int state;// 状态，如果为1说明评论发送成功，0，不成功

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
