package com.android.incongress.cd.conference.beans;

/**
 * 用于首页中的每一个模块的Bean使用
 * 包括 ==》 会议日程、讲者、会议社区、参会指南、我的大会、搜索、展商活动、会议之声、参展商、扫一扫、设置
 * @author Jacky_Chen
 * @time 2014年11月24日
 *
 */
public class HomeBean {
	private int index;// home中的item项的位置
	private int drawableId;// home中的item项的图片的id
	private int stringId;// home中的item项的名称的id
	private boolean visiblenew;// home中的item项的显示New
	private boolean visiblenumber;// home中的item项的显示数字
	private int number;// home中的item项的数字

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public int getStringId() {
		return stringId;
	}

	public void setStringId(int stringId) {
		this.stringId = stringId;
	}

	public boolean isVisiblenew() {
		return visiblenew;
	}

	public void setVisiblenew(boolean visiblenew) {
		this.visiblenew = visiblenew;
	}

	public boolean isVisiblenumber() {
		return visiblenumber;
	}

	public void setVisiblenumber(boolean visiblenumber) {
		this.visiblenumber = visiblenumber;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
