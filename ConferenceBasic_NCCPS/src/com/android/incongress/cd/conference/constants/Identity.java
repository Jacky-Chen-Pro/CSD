package com.android.incongress.cd.conference.constants;

import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * 获取身份认证信息
 * @author Administrator
 *
 */
public class Identity {
	public static final int speaker = 1;// 讲者
	public static final int moderator = 2;// 主持人
	public static final int translate = 3;// 翻译
	public static final int discussant = 4;// 讨论者
	public static final int operator = 5;// 术者
	public static final int presenter = 6;// 报告者
	public static final int speak = 7;// 发言
	public static final int interpreter = 8;// 解说
	public static final int panelist = 9;// 评论者
	public static final int chair = 10;// 主席
	public static final int coChair = 11;// 联合主席
	public static final int judge = 12;// 评委
	public static final int awardPresenter = 13;// 颁奖者
	public static final int recepient = 14;// 领奖者
	public static final int leadDiscussant = 15;// 引导评论者
	public static final int other = 16;// 其他
	
	/**
	 * 根据id获取对应的身份名称,如果对应type不存在，返回未知身份
	 * @param id
	 * @return
	 */
	public static int getIdentity(int id) {
		return 1;
//		switch (id) {
//			return 1;
//		}
//		case 1:
//			return R.string.identity_speaker;
//		case 2:
//			return R.string.identity_moderator;
//		case 3:
//			return R.string.identity_translate;
//		case 4:
//			return R.string.identity_discussant;
//		case 5:
//			return R.string.identity_operator;
//		case 6:
//			return R.string.identity_presenter;
//		case 7:
//			return R.string.identity_speak;
//		case 8:
//			return R.string.identity_interpreter;
//		case 9:
//			return R.string.identity_panelist;
//		case 10:
//			return R.string.identity_chair;
//		case 11:
//			return R.string.identity_coChair;
//		case 12:
//			return R.string.identity_judge;
//		case 13:
//			return R.string.identity_awardPresenter;
//		case 14:
//			return R.string.identity_recepient;
//		case 15:
//			return R.string.identity_leadDiscussantIds;
//		case 16:
//			return R.string.identity_otherIds;
//		}
//
//		return R.string.identity_unknow;
	}
	
	/**
	 * 根据id获取对应的身份名称,如果对应type不存在，返回未知身份 没有括号
	 * @param id
	 * @return
	 */
	public static int getIdentityWithoutBracket(int id) {
		 return 1;
	}
}
