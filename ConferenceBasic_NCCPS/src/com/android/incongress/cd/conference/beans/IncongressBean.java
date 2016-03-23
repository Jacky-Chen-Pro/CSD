package com.android.incongress.cd.conference.beans;

import java.util.List;

public class IncongressBean {
	// 参会议数据初始化Bean
	private String client;// 1表示有新的apk 0表示现在的apk为最新的
	private String appVersion;// 表示当前的app版本号
	private String clientVersion;// 表示有新的apk的提示语
	private String url;// 表示apk的下载地址
	private int newsCount;// 表示新闻的总数据量
	private int reCount;// 展商活动的总数据量
	private List<VersionBean> versionList;// 数据包下载列表

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getNewsCount() {
		return newsCount;
	}

	public void setNewsCount(int newsCount) {
		this.newsCount = newsCount;
	}

	public int getReCount() {
		return reCount;
	}

	public void setReCount(int reCount) {
		this.reCount = reCount;
	}

	public List<VersionBean> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<VersionBean> versionList) {
		this.versionList = versionList;
	}

}
