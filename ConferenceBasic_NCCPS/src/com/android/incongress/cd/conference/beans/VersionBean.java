package com.android.incongress.cd.conference.beans;

public class VersionBean {
	// 数据包的版本信息
	private int type;// 类型
	private int version;// 数据版本号
	private String zipUrl;// 数据下载包地址

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getZipUrl() {
		return zipUrl;
	}

	public void setZipUrl(String zipUrl) {
		this.zipUrl = zipUrl;
	}
}
