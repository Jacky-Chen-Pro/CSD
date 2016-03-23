package com.android.incongress.cd.conference.beans;

import java.util.ArrayList;


/**
 * 
 * @function 精彩回顾Bean
 * @author Jacky Chen
 * @time 2014-12-8-下午5:38:11
 *
 */
public class GreatReviewBean {
	
	public int state;
	private int maxCount;
	
	public ArrayList<Detail> caseList;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public ArrayList<Detail> getCaseList() {
		return caseList;
	}

	public void setCaseList(ArrayList<Detail> caseList) {
		this.caseList = caseList;
	}

	public GreatReviewBean(int state, int maxCount, ArrayList<Detail> caseList) {
		super();
		this.state = state;
		this.maxCount = maxCount;
		this.caseList = caseList;
	}
}
