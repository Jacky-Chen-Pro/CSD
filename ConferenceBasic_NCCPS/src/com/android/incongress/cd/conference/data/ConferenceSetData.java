package com.android.incongress.cd.conference.data;

import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.ExhibitorActivityBean;
import com.android.incongress.cd.conference.beans.ExhibitorBean;
import com.android.incongress.cd.conference.beans.NewBean;
import com.android.incongress.cd.conference.beans.Notes;
import com.android.incongress.cd.conference.beans.SpeakerBean;

import android.util.Pair;

/**
 * 
 * @function 该类主要设置数据库中的数据，用于更新添加和删除数据库中的数据
 * @author Jacky Chen
 * @time 2014-12-8-下午5:57:44
 *
 */
public class ConferenceSetData {
	// 更新会议表 中某个会议的关注
	public static void updateSessionAttention(DbAdapter db, int sessionGroupId,
			int attention) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SESSION_ATTENTION,
						String.valueOf(attention)));
		db.update(ConferenceTables.TABLE_INCONGRESS_SESSION, bean,
				ConferenceTableField.SESSION_SESSIONGROUPID + "=?",
				new String[] { String.valueOf(sessionGroupId) });
	}

	// 更新演讲表中的关注
	public static void updateMeetingAttention(DbAdapter db, int meetingId,
			int attention) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.MEETING_ATTENTION,
						String.valueOf(attention)));
		db.update(ConferenceTables.TABLE_INCONGRESS_MEETING, bean,
				ConferenceTableField.MEETING_MEETINGID + "=?",
				new String[] { String.valueOf(meetingId) });
	}

	// 更新展商表 中某个展商的关注
	public static void updateExhibitorAttention(DbAdapter db, int exhibitorsId,
			int attention) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_ATTENTION,
						String.valueOf(attention)));
		db.update(ConferenceTables.TABLE_INCONGRESS_EXHIBITOR, bean,
				ConferenceTableField.EXHIBITOR_EXHIBITORSID + "=?",
				new String[] { String.valueOf(exhibitorsId) });
	}

	// 更新展商表中的数据
	public static void updateExhibitor(DbAdapter db, ExhibitorBean mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_ADDRESS,
						mBean.getAddress()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_TITLE,
						mBean.getTitle()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_INFO,
						mBean.getInfo()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_PHONE,
						mBean.getPhone()));
		bean.getContainer()
				.add(Pair.create(ConferenceTableField.EXHIBITOR_FAX,
						mBean.getFax()));
		bean.getContainer()
				.add(Pair.create(ConferenceTableField.EXHIBITOR_NET,
						mBean.getNet()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_LOCATION,
						mBean.getLocation()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_LOGO,
						mBean.getLogo()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_ATTENTION,
						String.valueOf(mBean.getAttention())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITOR_STORELOGO,
						String.valueOf(mBean.getStorelogo())));
		bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_EXHIBITOR_LOCATION, mBean.getExhibitorsLocation()));
		bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_MAP_NAME, mBean.getMapName()));

		db.update(ConferenceTables.TABLE_INCONGRESS_EXHIBITOR, bean,
				ConferenceTableField.EXHIBITOR_EXHIBITORSID + "=?",
				new String[] { String.valueOf(mBean.getExhibitorsId()) });
	}

	// 往展商活动表中更新数据
	public static void addExhibitorActivity(DbAdapter db,
			ExhibitorActivityBean mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_ACTIVITYID,
						String.valueOf(mBean.getActivityId())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_NAME,
						mBean.getName()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_VERSION,
						String.valueOf(mBean.getVersion())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_HOT,
						String.valueOf(mBean.getHot())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_URL,
						mBean.getUrl()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_LOGO,
						mBean.getLogo()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_STORELOGO,
						String.valueOf(mBean.getStorelogo())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_STOREURL,
						String.valueOf(mBean.getStoreurl())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_ATTENTION,
						String.valueOf(mBean.getAttention())));
		db.insert(ConferenceTables.TABLE_INCONGRESS_EXHIBITORACTYIVITY, bean);
	}

	public static void updateExhibitorActivity(DbAdapter db,
			ExhibitorActivityBean mBean) {
		System.out.println("----更新数据-----");
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_NAME,
						mBean.getName()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_VERSION,
						String.valueOf(mBean.getVersion())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_HOT,
						String.valueOf(mBean.getHot())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_URL,
						mBean.getUrl()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_LOGO,
						mBean.getLogo()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_STORELOGO,
						String.valueOf(mBean.getStorelogo())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_STOREURL,
						String.valueOf(mBean.getStoreurl())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.EXHIBITORACTYIVITY_ATTENTION,
						String.valueOf(mBean.getAttention())));
		db.update(ConferenceTables.TABLE_INCONGRESS_EXHIBITORACTYIVITY, bean,
				ConferenceTableField.EXHIBITORACTYIVITY_ACTIVITYID + "=?",
				new String[] { String.valueOf(mBean.getActivityId()) });
	}

	// 添加笔记
	public static void addNotes(DbAdapter db, Notes mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_ID,
						String.valueOf(mBean.getId())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_CONTENTS,
						mBean.getContents()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_CLASSID,
						String.valueOf(mBean.getClassid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_CREATETIME,
						String.valueOf(mBean.getCreatetime())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_DATE, mBean.getDate()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_END, mBean.getEnd()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_MEETINGID,
						String.valueOf(mBean.getMeetingid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_ROOM,
						String.valueOf(mBean.getRoom())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_SESSIONID,
						String.valueOf(mBean.getSessionid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_START,
						String.valueOf(mBean.getStart())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_TITLE,
						String.valueOf(mBean.getTitle())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_UPDATETIME,
						String.valueOf(mBean.getUpdatetime())));
		db.insertNotes(ConferenceTables.TABLE_INCONGRESS_Note, bean);
	}

	// 修改笔记
	public static void updateNotes(DbAdapter db, Notes mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_ID,
						String.valueOf(mBean.getId())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_CONTENTS,
						mBean.getContents()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_CLASSID,
						String.valueOf(mBean.getClassid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_CREATETIME,
						String.valueOf(mBean.getCreatetime())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_DATE, mBean.getDate()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_END, mBean.getEnd()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_MEETINGID,
						String.valueOf(mBean.getMeetingid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_ROOM,
						String.valueOf(mBean.getRoom())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_SESSIONID,
						String.valueOf(mBean.getSessionid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_START,
						String.valueOf(mBean.getStart())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_TITLE,
						String.valueOf(mBean.getTitle())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NOTES_UPDATETIME,
						String.valueOf(mBean.getUpdatetime())));
		db.update(ConferenceTables.TABLE_INCONGRESS_Note, bean,
				ConferenceTableField.NOTES_ID + "=?",
				new String[] { String.valueOf(mBean.getId()) });
	}

	// 添加会议之声 新闻
	public static void addNew(DbAdapter db, NewBean mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSID,
						String.valueOf(mBean.getNewsId())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSTITLE,
						mBean.getNewsTitle()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSUMMARY,
						mBean.getNewsSummary()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSIMAGEURL,
						mBean.getNewsImageUrl()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSOURCE,
						mBean.getNewsSource()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSDATE,
						mBean.getNewsDate()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSTOREITEM,
						String.valueOf(mBean.getStoreitem())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSTOREDETAIL,
						String.valueOf(mBean.getStoredetail())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSCONTENT,
						mBean.getNewsContent()));
		db.insert(ConferenceTables.TABLE_INCONGRESS_NEWS, bean);
	}

	// 修改会议之声 新闻
	public static void updateNews(DbAdapter db, NewBean mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSTITLE,
						mBean.getNewsTitle()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSUMMARY,
						mBean.getNewsSummary()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSIMAGEURL,
						mBean.getNewsImageUrl()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSOURCE,
						mBean.getNewsSource()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSDATE,
						mBean.getNewsDate()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSTOREITEM,
						String.valueOf(mBean.getStoreitem())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSSTOREDETAIL,
						String.valueOf(mBean.getStoredetail())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.NEWS_NEWSCONTENT,
						mBean.getNewsContent()));
		db.update(ConferenceTables.TABLE_INCONGRESS_NEWS, bean,
				ConferenceTableField.NEWS_NEWSID + "=?",
				new String[] { String.valueOf(mBean.getNewsId()) });
	}

	// 删除笔记
	public static void deleteNotes(DbAdapter db, Notes mBean) {
		db.deleteNotes(mBean.getId());
	}

	// 添加提醒
	public static void addAlert(DbAdapter db, AlertBean mBean) {
		//如果已经存在就不添加这个提醒到数据库
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_DATE, mBean.getDate()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_ENABLE,
						String.valueOf(mBean.getEnable())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_RELATIVEID,
						String.valueOf(mBean.getRelativeid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_REPEATDISTANCE,
						mBean.getRepeatdistance()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_REPEATTIMES,
						mBean.getRepeattimes()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_START,
						String.valueOf(mBean.getStart())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_END,
						String.valueOf(mBean.getEnd())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_ROOM,
						String.valueOf(mBean.getRoom())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_TITLE,
						String.valueOf(mBean.getTitle())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_TYPE,
						String.valueOf(mBean.getType())));
		
		db.insertNotes(ConferenceTables.TABLE_INCONGRESS_ALERT, bean);
	}

	// 修改提醒
	public static void updateAlert(DbAdapter db, AlertBean mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_ENABLE,
						String.valueOf(mBean.getEnable())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_RELATIVEID,
						String.valueOf(mBean.getRelativeid())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_REPEATDISTANCE,
						mBean.getRepeatdistance()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.ALERT_REPEATTIMES,
						mBean.getRepeattimes()));
		db.update(ConferenceTables.TABLE_INCONGRESS_ALERT, bean, "id = ?",
				new String[] { mBean.getId() });
	}
	
	public static void disableAlert(DbAdapter db,AlertBean mBean){
		db.deleteItems(ConferenceTables.TABLE_INCONGRESS_ALERT, " id = ?", new String[]{mBean.getId()});
	}
	//对讲者的关注@讲者
	public static void updateSpeaker(DbAdapter db, SpeakerBean mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SPEAKER_CONFERENCESID,
						String.valueOf(mBean.getConferencesId())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SPEAKER_REMARK,
						mBean.getRemark()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SPEAKER_SPEAKERFROM,
						mBean.getSpeakerFrom()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SPEAKER_SPEAKERNAME,
						mBean.getSpeakerName()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SPEAKER_TYPE,
						String.valueOf(mBean.getType())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SPEAKER_SPEAKERNAMEPINGYIN,
						mBean.getSpeakerNamePingyin()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.SPEAKER_ATTENTION,
						String.valueOf(mBean.getAttention())));
		db.update(ConferenceTables.TABLE_INCONGRESS_SPEAKER, bean,
				ConferenceTableField.SPEAKER_SPEAKERID + "=?",
				new String[] { String.valueOf(mBean.getSpeakerId()) });
	}
	//修改广告
	public static void updateAd(DbAdapter db, AdBean mBean) {
		DbBean bean = new DbBean();
		bean.getContainer().add(
				Pair.create(ConferenceTableField.AD_ADIMAGE,
						String.valueOf(mBean.getAdImage())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.AD_ADLEVEL,
						String.valueOf(mBean.getAdLevel())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.AD_ADLINK,
						mBean.getAdLink()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.AD_IMGURL,
						mBean.getImgUrl()));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.AD_VERSION,
						String.valueOf(mBean.getVersion())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.AD_VIEWLEVEL,
						String.valueOf(mBean.getViewLevel())));
		bean.getContainer().add(
				Pair.create(ConferenceTableField.AD_STOPTIME,
						String.valueOf(mBean.getStopTime())));
		db.update(ConferenceTables.TABLE_INCONGRESS_AD, bean,
				ConferenceTableField.AD_ADID + "=?",
				new String[] { String.valueOf(mBean.getAdId()) });
	}
}
