package com.android.incongress.cd.conference.beans;

import android.view.View;

/**
 * 标题包含控件的显示与隐藏
 * @author Administrator
 *
 */
public class TitleEntry {

	private View coustomView;
	private int title;
	private String titleString;
	private boolean showHome;
	private boolean showBack;
	private boolean showCoustomView;
	private boolean adTop;
	private boolean adBottom;
	private boolean showtitlebar;

	public boolean isShowHome() {
		return showHome;
	}

	public void setShowHome(boolean showHome) {
		this.showHome = showHome;
	}

	public boolean isShowBack() {
		return showBack;
	}

	public void setShowBack(boolean showBack) {
		this.showBack = showBack;
	}

	public boolean isShowCoustomView() {
		return showCoustomView;
	}

	public void setShowCoustomView(boolean showCoustomView) {
		this.showCoustomView = showCoustomView;
	}

	public View getCoustomView() {
		return coustomView;
	}

	public void setCoustomView(View coustomView) {
		this.coustomView = coustomView;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public String getTitleString() {
		return titleString;
	}

	public void setTitleString(String titleString) {
		this.titleString = titleString;
	}

	public boolean isAdTop() {
		return adTop;
	}

	public void setAdTop(boolean adTop) {
		this.adTop = adTop;
	}

	public boolean isAdBottom() {
		return adBottom;
	}

	public void setAdBottom(boolean adBottom) {
		this.adBottom = adBottom;
	}

	public boolean isShowtitlebar() {
		return showtitlebar;
	}

	public void setShowtitlebar(boolean showtitlebar) {
		this.showtitlebar = showtitlebar;
	}

}
