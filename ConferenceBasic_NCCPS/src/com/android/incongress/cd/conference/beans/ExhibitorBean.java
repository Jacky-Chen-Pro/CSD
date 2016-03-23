package com.android.incongress.cd.conference.beans;

public class ExhibitorBean {
	// 参展商
	private int exhibitorsId;// 参展商Id
	private String address;// 地址
	private String title;// 参展商名称
	private String info;// 参展商简介
	private String phone;// 联系电话
	private String fax;// 传真号码
	private String net;// 公司链接
	private String location;// 展位
	private String logo;// Logo图片
	private int attention;// 关注 用户对参展商的关注设置
	private boolean checked;// 用于判断是否用户关注了这个参展商的checkbox的显示 这个是否显示与attention相关
	private int storelogo;// 是否存储了logo 0表示未存储 1表示存储
	private String address_En;// 地址 英文
	private String title_En;// 参展商名称 英文
	private String info_En;// 参展商简介 英文
//exhibitorsLocation
	private String exhibitorsLocation; //展位的具体位置
	private String mapName;//展位图片

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getExhibitorsLocation() {
		return exhibitorsLocation;
	}

	public void setExhibitorsLocation(String exhibitorsLocation) {
		this.exhibitorsLocation = exhibitorsLocation;
	}

	public int getExhibitorsId() {
		return exhibitorsId;
	}

	public void setExhibitorsId(int exhibitorsId) {
		this.exhibitorsId = exhibitorsId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getAttention() {
		return attention;
	}

	public void setAttention(int attention) {
		this.attention = attention;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getStorelogo() {
		return storelogo;
	}

	public void setStorelogo(int storelogo) {
		this.storelogo = storelogo;
	}

	public String getAddress_En() {
		return address_En;
	}

	public void setAddress_En(String address_En) {
		this.address_En = address_En;
	}

	public String getTitle_En() {
		return title_En;
	}

	public void setTitle_En(String title_En) {
		this.title_En = title_En;
	}

	public String getInfo_En() {
		return info_En;
	}

	public void setInfo_En(String info_En) {
		this.info_En = info_En;
	}

}
