package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

public class ExhibitorActivityBean implements Serializable {

	private int activityId;// 活动ID
	private String name;// 活动名称
	private int version;// 数据版本
	private int hot;// 是否为hot
	private String url;// 下载地址
	private String logo;// 预览图
	private int storelogo;// 是否存储了logo 0表示未存储 1表示存储
	private int storeurl;// 是否存储了url 0表示未存储 1表示存储
	private int attention;// 关注
	private boolean checked;// 用于判断是否用户关注了这个参展商的checkbox的显示 这个是否显示与attention相关

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getAttention() {
		return attention;
	}

	public void setAttention(int attention) {
		this.attention = attention;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getStorelogo() {
		return storelogo;
	}

	public void setStorelogo(int storelogo) {
		this.storelogo = storelogo;
	}

	public int getStoreurl() {
		return storeurl;
	}

	public void setStoreurl(int storeurl) {
		this.storeurl = storeurl;
	}

}
