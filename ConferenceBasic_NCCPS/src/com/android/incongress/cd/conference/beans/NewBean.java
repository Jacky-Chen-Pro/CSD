package com.android.incongress.cd.conference.beans;

public class NewBean {
	// 会议之声的java Bean 新闻
	private int newsId;// 新闻编号
	private String newsTitle;// 新闻标题
	private String newsSummary;// 新闻概要
	private String newsImageUrl;// 新闻图片url
	private String newsSource;// 新闻来源
	private String newsDate;// 新闻时间
	private int storeitem;// 存储一条列表
	private int storedetail;// 存储详细内容 也表明是否读过了
	private String newsContent;// 详细信息
	private String spUrl; //新闻地址
	private int dataType;//新闻类型  0是图文 1是视频，2是ppt
	
	
	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getSpUrl() {
		return spUrl;
	}

	public void setSpUrl(String spUrl) {
		this.spUrl = spUrl;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsSummary() {
		return newsSummary;
	}

	public void setNewsSummary(String newsSummary) {
		this.newsSummary = newsSummary;
	}

	public String getNewsImageUrl() {
		return newsImageUrl;
	}

	public void setNewsImageUrl(String newsImageUrl) {
		this.newsImageUrl = newsImageUrl;
	}

	public String getNewsSource() {
		return newsSource;
	}

	public void setNewsSource(String newsSource) {
		this.newsSource = newsSource;
	}

	public String getNewsDate() {
		return newsDate;
	}

	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}

	public int getStoreitem() {
		return storeitem;
	}

	public void setStoreitem(int storeitem) {
		this.storeitem = storeitem;
	}

	public int getStoredetail() {
		return storedetail;
	}

	public void setStoredetail(int storedetail) {
		this.storedetail = storedetail;
	}
}
