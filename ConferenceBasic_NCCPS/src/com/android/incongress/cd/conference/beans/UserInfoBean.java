package com.android.incongress.cd.conference.beans;

//	{
//		"name": "陈强",
//		"img": "",
//		"userType": 1,
//		"userId": 15997,
//		"mobilePhone": "15000979730",
//		"giveName": null,
//		"familyName": null,
//		"state": 1,
//		"msg": ""
//	}

public class UserInfoBean {
	private int state;// 状态
	private String name; //名字
	private String img;  //图片
	private String mobilePhone;//手机号
	private int userType;// 用户类型 1表明是游客 2表明是普通用户
	private int userId;// 用户id编号
	private String giveName;// 名
	private String familyName; // 姓\
	private String msg; // 提示信息

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

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

	public String getGiveName() {
		return giveName;
	}

	public void setGiveName(String giveName) {
		this.giveName = giveName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
