package com.android.incongress.cd.conference.beans;

public class CommunityTopicResponse {
	// 说两句返回信息
	private int userType;// 如果用户为游客，才有此项；用户类型 1为游客
	private int userId;// 同上，用户id
	private String userName;// 同上，用户名字
	private int state;// 状态，如果为1说明消息发送成功，0，不成功
	private int luadCount;// 表示赞的个数
	
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

	public int getLuadCount() {
		return luadCount;
	}

	public void setLuadCount(int luadCount) {
		this.luadCount = luadCount;
	}
}
