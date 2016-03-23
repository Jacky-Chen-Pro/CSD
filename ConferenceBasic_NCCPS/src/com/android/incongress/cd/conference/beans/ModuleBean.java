package com.android.incongress.cd.conference.beans;

public class ModuleBean {
	// 各个模块数据量
	private boolean visiblehyrc;// 显示会议日程New
	private boolean visiblejz;// 显示讲者New
	private boolean visiblechzn;// 显示参会指南New
	private int zshdCount;// 展商活动
	private boolean visiblezshd;// 显示展商活动New
	private int hyzsCount;// 会议之声
	private boolean visiblehyzs;// 显示会议之声的数据
	private boolean visibleczs;// 显示参展商New

	public boolean isVisiblehyrc() {
		return visiblehyrc;
	}

	public void setVisiblehyrc(boolean visiblehyrc) {
		this.visiblehyrc = visiblehyrc;
	}

	public boolean isVisiblejz() {
		return visiblejz;
	}

	public void setVisiblejz(boolean visiblejz) {
		this.visiblejz = visiblejz;
	}

	public boolean isVisiblechzn() {
		return visiblechzn;
	}

	public void setVisiblechzn(boolean visiblechzn) {
		this.visiblechzn = visiblechzn;
	}

	public int getZshdCount() {
		return zshdCount;
	}

	public void setZshdCount(int zshdCount) {
		this.zshdCount = zshdCount;
	}

	public boolean isVisiblezshd() {
		return visiblezshd;
	}

	public void setVisiblezshd(boolean visiblezshd) {
		this.visiblezshd = visiblezshd;
	}

	public int getHyzsCount() {
		return hyzsCount;
	}

	public void setHyzsCount(int hyzsCount) {
		this.hyzsCount = hyzsCount;
	}

	public boolean isVisiblehyzs() {
		return visiblehyzs;
	}

	public void setVisiblehyzs(boolean visiblehyzs) {
		this.visiblehyzs = visiblehyzs;
	}

	public boolean isVisibleczs() {
		return visibleczs;
	}

	public void setVisibleczs(boolean visibleczs) {
		this.visibleczs = visibleczs;
	}

}
