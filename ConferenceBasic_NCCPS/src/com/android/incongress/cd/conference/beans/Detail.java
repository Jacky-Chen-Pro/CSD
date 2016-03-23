package com.android.incongress.cd.conference.beans;

public class Detail {
	/** 标题ID */
	public int caseId;
	/** 标题 */
	private String title;
	/** 链接地址 */
	public String dataUrl;
	/** 发布时间 */
	public String createTime;
	/** 类型，分为video 和 ppt */
	public int type; 
	/** 领域 */
	public int fieldId;
	public String fieldName;
	public int getCaseId() {
		return caseId;
	}
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDataUrl() {
		return dataUrl;
	}
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getFieldId() {
		return fieldId;
	}
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public Detail(int caseId, String title, String dataUrl, String createTime,
			int type, int fieldId, String fieldName) {
		super();
		this.caseId = caseId;
		this.title = title;
		this.dataUrl = dataUrl;
		this.createTime = createTime;
		this.type = type;
		this.fieldId = fieldId;
		this.fieldName = fieldName;
	}
	
	@Override
	public String toString() {
		return "Detail [caseId=" + caseId + ", title=" + title + ", dataUrl="
				+ dataUrl + ", createTime=" + createTime + ", type=" + type
				+ ", fieldId=" + fieldId + ", fieldName=" + fieldName + "]";
	}
	
}