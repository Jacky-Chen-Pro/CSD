package com.android.incongress.cd.conference.beans;

public class ConFieldBean {
	private int conFieldId;// 领域编号
	private String conFieldName;// 领域名称
	private boolean checked = true;

	public int getConFieldId() {
		return conFieldId;
	}

	public void setConFieldId(int conFieldId) {
		this.conFieldId = conFieldId;
	}

	public String getConFieldName() {
		return conFieldName;
	}

	public void setConFieldName(String conFieldName) {
		this.conFieldName = conFieldName;
	}

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    @Override
    public boolean equals(Object o) {
        ConFieldBean pare = (ConFieldBean) o;
        return pare.conFieldId == conFieldId;
    }
}
