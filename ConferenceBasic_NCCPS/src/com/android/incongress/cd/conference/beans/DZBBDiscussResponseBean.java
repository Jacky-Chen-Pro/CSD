package com.android.incongress.cd.conference.beans;

public class DZBBDiscussResponseBean {
	private int userId;
	private String userName;
	private int state;
	private int userType;
	private int posterDiscussId;
	
	
	
	public int getPosterDiscussId() {
		return posterDiscussId;
	}

	public void setPosterDiscussId(int posterDiscussId) {
		this.posterDiscussId = posterDiscussId;
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

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}
	 

	public DZBBDiscussResponseBean(int userId, String userName, int state,
			int userType, int posterDiscussId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.state = state;
		this.userType = userType;
		this.posterDiscussId = posterDiscussId;
	}

	public DZBBDiscussResponseBean(){}
}
