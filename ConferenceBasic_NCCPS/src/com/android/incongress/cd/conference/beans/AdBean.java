package com.android.incongress.cd.conference.beans;

public class AdBean {
	// 广告的Bean
	private int adId;// 广告id编号
	private int conferencesId;// 参会议编号
	private String adImage;// 广告图片名称
	private int adLevel;// 广告等级
	private String adLink;// 广告链接地址
	private String imgUrl;// 广告下载地址
	private int version;// 广告版本号 当数据包有版本更新的时候版本号大于的时候会去从服务器众新下载
	private int viewLevel;// 查看等级
	private int stopTime;// 广告显示时间
	// 下载信息
	private String image;// 下载图片地址
	private int level;
	private String link;

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	public int getConferencesId() {
		return conferencesId;
	}

	public void setConferencesId(int conferencesId) {
		this.conferencesId = conferencesId;
	}

	public String getAdImage() {
		return adImage;
	}

	public void setAdImage(String adImage) {
		this.adImage = adImage;
	}

	public int getAdLevel() {
		return adLevel;
	}

	public void setAdLevel(int adLevel) {
		this.adLevel = adLevel;
	}

	public String getAdLink() {
		return adLink;
	}

	public void setAdLink(String adLink) {
		this.adLink = adLink;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getViewLevel() {
		return viewLevel;
	}

	public void setViewLevel(int viewLevel) {
		this.viewLevel = viewLevel;
	}

	public int getStopTime() {
		return stopTime;
	}

	public void setStopTime(int stopTime) {
		this.stopTime = stopTime;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
