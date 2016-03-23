package com.android.incongress.cd.conference.beans;

/**
 * posterCode No.1124
	conField 感染学组

	title 标题
	author 作者
	posterPicUrl 跳转图片
	maxCount 总共的页数
 * @author Administrator
 *
 */
public class DZBBBean {
	private int posterId;
	private String posterCode;
	private String conField;
	private String title;
	private String author;
	private String posterPicUrl;
	private int maxCount;
	private int disCount;
	
	
	public int getDisCount() {
		return disCount;
	}
	public void setDisCount(int disCount) {
		this.disCount = disCount;
	}
	public int getPosterId() {
		return posterId;
	}
	public void setPosterId(int posterId) {
		this.posterId = posterId;
	}
	public String getPosterCode() {
		return posterCode;
	}
	public void setPosterCode(String posterCode) {
		this.posterCode = posterCode;
	}
	public String getConField() {
		return conField;
	}
	public void setConField(String conField) {
		this.conField = conField;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPosterPicUrl() {
		return posterPicUrl;
	}
	public void setPosterPicUrl(String posterPicUrl) {
		this.posterPicUrl = posterPicUrl;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	@Override
	public String toString() {
		return "DZBBBean [posterId=" + posterId + ", posterCode=" + posterCode
				+ ", conField=" + conField + ", title=" + title + ", author="
				+ author + ", posterPicUrl=" + posterPicUrl + ", maxCount="
				+ maxCount + ", disCount=" + disCount + "]";
	}
	public DZBBBean(int posterId, String posterCode, String conField,
			String title, String author, String posterPicUrl, int maxCount,
			int disCount) {
		super();
		this.posterId = posterId;
		this.posterCode = posterCode;
		this.conField = conField;
		this.title = title;
		this.author = author;
		this.posterPicUrl = posterPicUrl;
		this.maxCount = maxCount;
		this.disCount = disCount;
	}
}
