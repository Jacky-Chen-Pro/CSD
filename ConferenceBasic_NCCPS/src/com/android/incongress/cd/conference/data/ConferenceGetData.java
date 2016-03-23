package com.android.incongress.cd.conference.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.ConFieldBean;
import com.android.incongress.cd.conference.beans.ConferenceBean;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CountryCodeBean;
import com.android.incongress.cd.conference.beans.ExhibitorActivityBean;
import com.android.incongress.cd.conference.beans.ExhibitorBean;
import com.android.incongress.cd.conference.beans.MapBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.NewBean;
import com.android.incongress.cd.conference.beans.Notes;
import com.android.incongress.cd.conference.beans.RoleBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.beans.TipBean;

/**
 * 该类主要用于从数据库中获取到表的数据。
 * @Time 2014年11月24日 星期一
 * @author Jacky_Chen
 *
 */
public class ConferenceGetData {

	//获取参会议信息
	public static ConferenceBean getConference(DbAdapter db){
		ConferenceBean bean=new ConferenceBean();
		String sql="select * from "+ ConferenceTables.TABLE_INCONGRESS_CONFERENCES;
		List<List<String>> rawList = db.fetchListBySql(sql);
		if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
			List<String> conferencesIdList = rawList.get(0);
			List<String> abbreviationList = rawList.get(1);
			List<String> adminuserIdList = rawList.get(2);
			List<String> codeList = rawList.get(3);
			List<String> conferencesAddressList = rawList.get(4);
			List<String> conferencesStartTimeList = rawList.get(5);
			List<String> conferencesEndTimeList = rawList.get(6);
			List<String> conferencesNameList = rawList.get(7);
			List<String> conferencesStateList = rawList.get(8);
			List<String> createTimeList = rawList.get(9);
			List<String> enLinkList = rawList.get(10);
			List<String> posterShowStateList = rawList.get(11);
			List<String> posterStateList = rawList.get(12);
			List<String> viewStateList = rawList.get(13);
			List<String> zhLinkList = rawList.get(14);
			if(conferencesIdList!=null&&conferencesIdList.size()>0){
				String conferencesId=conferencesIdList.get(0);
				String abbreviation=abbreviationList.get(0);
				String adminuserId=adminuserIdList.get(0);
				String code=codeList.get(0);
				String conferencesAddress=conferencesAddressList.get(0);
				String conferencesStartTime=conferencesStartTimeList.get(0);
				String conferencesEndTime=conferencesEndTimeList.get(0);
				String conferencesName=conferencesNameList.get(0);
				String conferencesState=conferencesStateList.get(0);
				String createTime=createTimeList.get(0);
				String enLink=enLinkList.get(0);
				String posterShowState=posterShowStateList.get(0);
				String posterState=posterStateList.get(0);
				String viewState=viewStateList.get(0);
				String zhLink=zhLinkList.get(0);
				bean.setConferencesId(Integer.parseInt(conferencesId));
				bean.setAbbreviation(abbreviation);
				bean.setAdminuserId(Integer.parseInt(adminuserId));
				bean.setCode(code);
				bean.setConferencesAddress(conferencesAddress);
				bean.setConferencesStartTime(conferencesStartTime);
				bean.setConferencesEndTime(conferencesEndTime);
				bean.setConferencesName(conferencesName);
				bean.setConferencesState(Integer.parseInt(conferencesState));
				bean.setCreateTime(createTime);
				bean.setEnLink(enLink);
				bean.setPosterShowState(Integer.parseInt(posterShowState));
				bean.setPosterState(Integer.parseInt(posterState));
				bean.setViewState(Integer.parseInt(viewState));
				bean.setZhLink(zhLink);
			}
		}
		return  bean;
	}
	
	//获取会议
    public static List<SessionBean> getSessionList(DbAdapter db, String sql) {
        List<SessionBean> list = new ArrayList<SessionBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> sessionGroupIdList = rawList.get(0);
            List<String> sessionNameList = rawList.get(1);
            List<String> classesIdList = rawList.get(2);
            List<String> sessionDayList = rawList.get(3);
            List<String> startTimeList = rawList.get(4);
            List<String> endTimeList = rawList.get(5);
            List<String> conFieldIdList = rawList.get(6);
            List<String> remarkList = rawList.get(7);
            List<String> attentionList=rawList.get(8);
            List<String> sessionName_EnList=rawList.get(9);
			List<String> facultyIdList = rawList.get(10);
			List<String> roleIdList = rawList.get(11);

            for (int i = 0; i < sessionGroupIdList.size(); i++) {
            	SessionBean bean = new SessionBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String sessionGroupId=sessionGroupIdList.get(i);
            	String sessionName=sessionNameList.get(i);
            	String classesId=classesIdList.get(i);
            	String sessionDay=sessionDayList.get(i);
            	String startTime=startTimeList.get(i);
            	String endTime=endTimeList.get(i);
            	String conFieldId=conFieldIdList.get(i);
            	String remark=remarkList.get(i);
            	String attention=attentionList.get(i);
            	String sessionName_En=sessionName_EnList.get(i);
				String facultyId = facultyIdList.get(i);
				String roleId = roleIdList.get(i);

            	SessionBean bean=list.get(i);
                bean.setSessionGroupId(Integer.parseInt(sessionGroupId));
                bean.setSessionName(sessionName);
                bean.setClassesId(Integer.parseInt(classesId));
                bean.setSessionDay(sessionDay);
                bean.setStartTime(startTime);
                bean.setEndTime(endTime);
				bean.setFacultyId(facultyId);
				bean.setRoleId(roleId)
				;
                if(conFieldId!=null&&!"".equals(conFieldId)){
                	bean.setConFieldId(Integer.parseInt(conFieldId));
                }
                bean.setRemark(remark);
                bean.setAttention(Integer.parseInt(attention));
                if(sessionName_En == null) {
                	sessionName_En = "";
                }
                bean.setSessionName_En(sessionName_En);
            }
        }

        return list;
    }
	
	//获取会议演讲
    public static List<MeetingBean> getMeetingList(DbAdapter db, String sql) {
        List<MeetingBean> list = new ArrayList<MeetingBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> meetingIdList = rawList.get(0);
            List<String> topicList = rawList.get(1);
            List<String> summaryList = rawList.get(2);
            List<String> classesIdList = rawList.get(3);
            List<String> meetingDayList = rawList.get(4);
            List<String> startTimeList = rawList.get(5);
            List<String> endTimeList = rawList.get(6);
            List<String> conFieldIdList = rawList.get(7);
            List<String> sessionGroupIdList = rawList.get(8);
            List<String> attentionList=rawList.get(9);
            List<String> topic_EnList=rawList.get(10);
            List<String> summary_EnList = rawList.get(11) == null?null : rawList.get(11);
			List<String> facultyIdList = rawList.get(12);
			List<String> roleIdList = rawList.get(13);

            for (int i = 0; i < meetingIdList.size(); i++) {
                MeetingBean bean = new MeetingBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String meetingId=meetingIdList.get(i);
            	String topic=topicList.get(i);
            	String summary=summaryList.get(i);
            	String classesId=classesIdList.get(i);
            	String meetingDay=meetingDayList.get(i);
            	String startTime=startTimeList.get(i);
            	String endTime=endTimeList.get(i);
            	String conFieldId=conFieldIdList.get(i);
            	String sessionGroupId=sessionGroupIdList.get(i);
            	String attention=attentionList.get(i);
            	String topic_En=topic_EnList.get(i);
            	String summary_En=summary_EnList.get(i) == null ? "":summary_EnList.get(i);
				String facultyId = facultyIdList.get(i);
				String roleId = roleIdList.get(i);

            	MeetingBean bean=list.get(i);
            	bean.setMeetingId(Integer.parseInt(meetingId));
            	bean.setTopic(topic);
            	bean.setSummary(summary);
            	bean.setClassesId(Integer.parseInt(classesId));
            	bean.setMeetingDay(meetingDay);
            	bean.setStartTime(startTime);
            	bean.setEndTime(endTime);
				bean.setFacultyIds(facultyId);
				bean.setRoleIds(roleId);

            	if(conFieldId!=null&&!"".equals(conFieldId)){
            		bean.setConFieldId(Integer.parseInt(conFieldId));
            	}
            	bean.setSessionGroupId(Integer.parseInt(sessionGroupId));
            	bean.setAttention(Integer.parseInt(attention));
            	bean.setTopic_En(topic_En);
            	bean.setSummary_En(summary_En);
            }
        }

        return list;
    }
    //获取领域
    public static List<ConFieldBean> getConFieldList(DbAdapter db, String sql) {
        List<ConFieldBean> list = new ArrayList<ConFieldBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> conFieldIdList = rawList.get(0);
            List<String> conFieldNameList = rawList.get(1);

            for (int i = 0; i < conFieldIdList.size(); i++) {
            	ConFieldBean bean = new ConFieldBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String conFieldId=conFieldIdList.get(i);
            	String conFieldName=conFieldNameList.get(i);
            	ConFieldBean bean=list.get(i);
                bean.setConFieldId(Integer.parseInt(conFieldId));
                bean.setConFieldName(conFieldName);
            }
        }

        return list;
    }
    
    //获取会议室
    public static List<ClassesBean> getClassesList(DbAdapter db, String sql) {
        List<ClassesBean> list = new ArrayList<ClassesBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> classesIdList = rawList.get(0);
            List<String> conferencesIdList = rawList.get(1);
            List<String> classesCapacityList = rawList.get(2);
            List<String> classesCodeList = rawList.get(3);
            List<String> classesLocationList = rawList.get(4);
            List<String> mapNameList = rawList.get(5);
            List<String> levelList = rawList.get(6);
            List<String> classesCodePingyinList=rawList.get(7);

            for (int i = 0; i < classesIdList.size(); i++) {
            	ClassesBean bean = new ClassesBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String classesId=classesIdList.get(i);
            	String conferencesId=conferencesIdList.get(i);
            	String classesCapacity=classesCapacityList.get(i);
            	String classesCode=classesCodeList.get(i);
            	String classesLocation=classesLocationList.get(i);
            	String classesCodePingyin=classesCodePingyinList.get(i);
            	String mapName=mapNameList.get(i);
            	String level=levelList.get(i);
            	ClassesBean bean=list.get(i);
                bean.setClassesId(Integer.parseInt(classesId));
                bean.setConferencesId(Integer.parseInt(conferencesId));
                bean.setClassesCapacity(Integer.parseInt(classesCapacity));

				String classesCodes[]=classesCode.split(Constants.ENCHINASPLIT);
				String classCode = "";
				String classCodeEn = "";

				if(classesCodes.length == 1){
					classCode = classesCodes[0];
					classCodeEn = classesCodes[0];
				}

				if(classesCodes.length == 2){
					classCode = classesCodes[0];
					classCodeEn = classesCodes[1];
				}

                bean.setClassesCode(classCode);
				bean.setClassesCodeEn(classCodeEn);
                bean.setClassesLocation(classesLocation);
                bean.setMapName(mapName);
                bean.setLevel(Integer.parseInt(level));
                bean.setClassesCodePingyin(classesCodePingyin);
            }
        }

        return list;
    }
    
    //获取演讲者
    public static List<SpeakerBean> getSpeakersList(DbAdapter db, String sql) {
        List<SpeakerBean> list = new ArrayList<SpeakerBean>();
        System.out.println("最终的添加顺序====" + sql);
        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> speakerIdList = rawList.get(0);
            List<String> conferencesIdList = rawList.get(1);
            List<String> remarkList = rawList.get(2);
            List<String> speakerFromList = rawList.get(3);
            List<String> speakerNameList = rawList.get(4);
            List<String> typeList = rawList.get(5);
            List<String> speakerNamePingyinList=rawList.get(6);
            List<String> attentionList=rawList.get(7);
            List<String> enNameList = rawList.get(8);
            List<String> entityIdList = rawList.get(9);
            List<String> pykeyList = rawList.get(10);
            for (int i = 0; i < speakerIdList.size(); i++) {
            	SpeakerBean bean = new SpeakerBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String speakerId=speakerIdList.get(i);
            	String conferencesId=conferencesIdList.get(i);
            	String remark=remarkList.get(i);
            	String speakerFrom=speakerFromList.get(i);
            	String speakerName=speakerNameList.get(i);
            	String type=typeList.get(i);
            	String speakerNamePingyin=speakerNamePingyinList.get(i);
            	String attention=attentionList.get(i);
            	String enName = enNameList.get(i);
            	String entityId = entityIdList.get(i);
            	String pykey = pykeyList.get(i);
            	SpeakerBean bean=list.get(i);
                bean.setSpeakerId(Integer.parseInt(speakerId));
                bean.setConferencesId(Integer.parseInt(conferencesId));
                bean.setRemark(remark);
                bean.setSpeakerFrom(speakerFrom);
                bean.setSpeakerName(speakerName);
                bean.setType(Integer.parseInt(type));
                bean.setSpeakerNamePingyin(speakerNamePingyin.replaceAll("\\s*", ""));
                bean.setAttention(Integer.parseInt(attention));

				String letter = "";
				if(speakerNamePingyin.trim().length() > 0) {
					try {
						letter = speakerNamePingyin.replaceAll("\\s*","").substring(0,1);
						bean.setFirstLetter(letter);
					}catch (Exception e) {
						e.printStackTrace();
						bean.setFirstLetter("#");
					}
				}else {
					bean.setFirstLetter("");
				}

                bean.setEnName(enName);
                bean.setEntityId(Integer.parseInt(entityId));
                bean.setPykey(pykey);
            }
        }

        return list;
    }
    
    //获取Meeting表interpreterIds、presenterIds、speakIds、operatorIds、
    //IVUSInterpreterIds、AwardPresenterIds、AwardRecepienterIds、leadDiscussantIds
    public static String getMeetingMembersIds(DbAdapter db, String sql) {
    	List<MeetingBean> list = new ArrayList<MeetingBean>();
    	List<List<String>> rawList = db.fetchListBySql(sql);
    	  
    	if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
          List<String> meetingIdList = rawList.get(0);
          List<String> topicList = rawList.get(1);
          List<String> summaryList = rawList.get(2);
          List<String> classesIdList = rawList.get(3);
          List<String> moderatorIdsList = rawList.get(4);
          List<String> speakerIdsList = rawList.get(5);
          List<String> meetingDayList = rawList.get(6);
          List<String> startTimeList = rawList.get(7);
          List<String> endTimeList = rawList.get(8);
          List<String> conFieldIdList = rawList.get(9);
          List<String> sessionGroupIdList = rawList.get(10);
          List<String> attentionList=rawList.get(11);
          List<String> topic_EnList=rawList.get(12);
          List<String> summary_EnList=rawList.get(13);

          for (int i = 0; i < meetingIdList.size(); i++) {
              MeetingBean bean = new MeetingBean();
              list.add(bean);
          }

          for (int i = 0; i < list.size(); i++) {
          	String meetingId=meetingIdList.get(i);
          	String topic=topicList.get(i);
          	String summary=summaryList.get(i);
          	String classesId=classesIdList.get(i);
          	String speakerIds=speakerIdsList.get(i);
          	String meetingDay=meetingDayList.get(i);
          	String startTime=startTimeList.get(i);
          	String endTime=endTimeList.get(i);
          	String conFieldId=conFieldIdList.get(i);
          	String sessionGroupId=sessionGroupIdList.get(i);
          	String attention=attentionList.get(i);
          	String topic_En=topic_EnList.get(i);
          	String summary_En=summary_EnList.get(i);

          	MeetingBean bean=list.get(i);
          	bean.setMeetingId(Integer.parseInt(meetingId));
          	bean.setTopic(topic);
          	bean.setSummary(summary);
          	bean.setClassesId(Integer.parseInt(classesId));
          	bean.setSpeakerIds(speakerIds);
          	bean.setMeetingDay(meetingDay);
          	bean.setStartTime(startTime);
          	bean.setEndTime(endTime);

          	if(conFieldId!=null&&!"".equals(conFieldId)){
          		bean.setConFieldId(Integer.parseInt(conFieldId));
          	}
          	bean.setSessionGroupId(Integer.parseInt(sessionGroupId));
          	bean.setAttention(Integer.parseInt(attention));
          	bean.setTopic_En(topic_En);
          	bean.setSummary_En(summary_En);
          }
    	}
    	StringBuffer ids = new StringBuffer();
    	for(int i=0; i<list.size(); i++) {
    		MeetingBean bean = list.get(i);
    	}
    	
    	//如果长度为0，那么就返回-1
    	//TODO  11111
    	if(ids.length() == 0) {
    		return "-1";
    	}
    	
    	//如果最后一个是逗号，就截取
    	if(ids.substring(ids.length()-1, ids.length()).equals(",")) {
    		String members = ids.substring(0, ids.length() - 1);
    		return members;
    	}else{
    		return new String(ids);
    	}
    }
    
    //获取参展商
    public static List<ExhibitorBean> getExhibitorsList(DbAdapter db, String sql) {
        List<ExhibitorBean> list = new ArrayList<ExhibitorBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> exhibitorsIdList = rawList.get(0);
            List<String> addressList = rawList.get(1);
            List<String> titleList = rawList.get(2);
            List<String> infoList = rawList.get(3);
            List<String> phoneList = rawList.get(4);
            List<String> faxList = rawList.get(5);
            List<String> netList=rawList.get(6);
            List<String> locationList=rawList.get(7);
            List<String> logoList=rawList.get(8);
            List<String> attentionList=rawList.get(9);
            List<String> storelogoList=rawList.get(10);
            List<String> address_EnList=rawList.get(11);
            List<String> title_EnList=rawList.get(12);
            List<String> info_EnList=rawList.get(13);
			List<String> exhibitorsLocationList = rawList.get(14);
			List<String> mapNameList = rawList.get(15);
            
            for (int i = 0; i < exhibitorsIdList.size(); i++) {
            	ExhibitorBean bean = new ExhibitorBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String exhibitorsId=exhibitorsIdList.get(i);
            	String address=addressList.get(i);
            	String title=titleList.get(i);
            	String info=infoList.get(i);
            	String phone=phoneList.get(i);
            	String fax=faxList.get(i);
            	String net=netList.get(i);
            	String location=locationList.get(i);
            	String logo=logoList.get(i);
            	String attention=attentionList.get(i);
            	String storelogo=storelogoList.get(i);
            	String address_En=address_EnList.get(i);
            	String title_En=title_EnList.get(i);
            	String info_En=info_EnList.get(i);
				String exhibitorsLocation = exhibitorsLocationList.get(i);
				String mapName = mapNameList.get(i);


            	ExhibitorBean bean=list.get(i);
                bean.setExhibitorsId(Integer.parseInt(exhibitorsId));
                bean.setAddress(address);
                bean.setTitle(title);
                bean.setInfo(info);
                bean.setPhone(phone);
                bean.setFax(fax);
                bean.setNet(net);
                bean.setLocation(location);
                bean.setLogo(logo);
                bean.setAttention(Integer.parseInt(attention));
                bean.setStorelogo(Integer.parseInt(storelogo));
                bean.setAddress_En(address_En);
                bean.setTitle_En(title_En);
                bean.setInfo_En(info_En);
				bean.setMapName(mapName);
				bean.setExhibitorsLocation(exhibitorsLocation);
            }
        }

        return list;
    }
    
    //获取展商活动
    public static List<ExhibitorActivityBean> getExhibitorActivityList(DbAdapter db, String sql) {
        List<ExhibitorActivityBean> list = new ArrayList<ExhibitorActivityBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> activityIdList = rawList.get(0);
            List<String> nameList = rawList.get(1);
            List<String> versionList = rawList.get(2);
            List<String> hotList = rawList.get(3);
            List<String> urlList = rawList.get(4);
            List<String> logoList = rawList.get(5);
            List<String> storelogoList = rawList.get(6);
            List<String> storeurlList = rawList.get(7);
            List<String> attentionList=rawList.get(8);

            for (int i = 0; i < activityIdList.size(); i++) {
            	ExhibitorActivityBean bean = new ExhibitorActivityBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String activityId=activityIdList.get(i);
            	String name=nameList.get(i);
            	String version=versionList.get(i);
            	String hot=hotList.get(i);
            	String url=urlList.get(i);
            	String logo=logoList.get(i);
            	String storelogo=storelogoList.get(i);
            	String storeurl=storeurlList.get(i);
            	String attention=attentionList.get(i);
            	ExhibitorActivityBean bean=list.get(i);
                bean.setActivityId(Integer.parseInt(activityId));
                bean.setName(name);
                bean.setVersion(Integer.parseInt(version));
                bean.setHot(Integer.parseInt(hot));
                bean.setUrl(url);
                bean.setLogo(logo);
                bean.setStorelogo(Integer.parseInt(storelogo));
                bean.setStoreurl(Integer.parseInt(storeurl));
                bean.setAttention(Integer.parseInt(attention));
            }
        }

        return list;
    }
    
    //获取基本信息
    public static List<TipBean> getTipsList(DbAdapter db, String sql) {
        List<TipBean> list = new ArrayList<TipBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> tipsIdList = rawList.get(0);
            List<String> conferencesIdList = rawList.get(1);
            List<String> tipsContentList = rawList.get(2);
            List<String> tipsTimeList = rawList.get(3);
            List<String> tipsTitleList = rawList.get(4);
            List<String> tipsTitle_EnList=rawList.get(5);
            List<String> tipsContent_EnList=rawList.get(6);

            for (int i = 0; i < tipsIdList.size(); i++) {
            	TipBean bean = new TipBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String tipsId=tipsIdList.get(i);
            	String conferencesId=conferencesIdList.get(i);
            	String tipsContent=tipsContentList.get(i);
            	String tipsTime=tipsTimeList.get(i);
            	String tipsTitle=tipsTitleList.get(i);
            	String tipsTitle_En=tipsTitle_EnList.get(i);
            	String tipsContent_En=tipsContent_EnList.get(i);
            	TipBean bean=list.get(i);
                bean.setTipsId(Integer.parseInt(tipsId));
                bean.setConferencesId(Integer.parseInt(conferencesId));
                bean.setTipsContent(tipsContent);
                bean.setTipsTime(tipsTime);
                bean.setTipsTitle(tipsTitle);
                bean.setTipsTitle_En(tipsTitle_En);
                bean.setTipsContent_En(tipsContent_En);
            }
        }

        return list;
    }

    //获取会议之声 新闻列表
    public static List<NewBean> getNewsList(DbAdapter db, String sql) {
        List<NewBean> list = new ArrayList<NewBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> newsIdList = rawList.get(0);
            List<String> newsTitleList = rawList.get(1);
            List<String> newsSummaryList = rawList.get(2);
            List<String> newsImageUrlList = rawList.get(3);
            List<String> newsSourceList = rawList.get(4);
            List<String> newsDateList = rawList.get(5);
            List<String> storeitemList = rawList.get(6);
            List<String> storedetailList = rawList.get(7);
            List<String> contentList=rawList.get(8);
            for (int i = 0; i < newsIdList.size(); i++) {
            	NewBean bean = new NewBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String newsId=newsIdList.get(i);
            	String newsTitle=newsTitleList.get(i);
            	String newsSummary=newsSummaryList.get(i);
            	String newsImageUrl=newsImageUrlList.get(i);
            	String newsSource=newsSourceList.get(i);
            	String newsDate=newsDateList.get(i);
            	String storeitem=storeitemList.get(i);
            	String storedetail=storedetailList.get(i);
            	String newsContent=contentList.get(i);
            	NewBean bean=list.get(i);
                bean.setNewsId(Integer.parseInt(newsId));
                bean.setNewsTitle(newsTitle);
                bean.setNewsSummary(newsSummary);
                bean.setNewsImageUrl(newsImageUrl);
                bean.setNewsSource(newsSource);
                bean.setNewsDate(newsDate);
                bean.setStoreitem(Integer.parseInt(storeitem));
                bean.setStoredetail(Integer.parseInt(storedetail));
                bean.setNewsContent(newsContent);
            }
        }

        return list;
    }
    
    //获取笔记
    public static List<Notes> getNoteList(DbAdapter db, String sql) {
        List<Notes> list = new ArrayList<Notes>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
        	
            List<String> ids = rawList.get(0);
            List<String> contents = rawList.get(1);
            List<String> starts = rawList.get(2);
            List<String> ends = rawList.get(3);
            List<String> dates = rawList.get(4);
            List<String> rooms = rawList.get(5);
            List<String> createtimes = rawList.get(6);
            List<String> updatetimes = rawList.get(7);
            List<String> sessionids=rawList.get(8);
            List<String> classids=rawList.get(9);
            List<String> meetingids=rawList.get(10);
            List<String> titles=rawList.get(11);

            for (int i = 0; i < ids.size(); i++) {
            	Notes bean = new Notes();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String id =ids.get(i);
            	String content=contents.get(i);
            	String start=starts.get(i);
            	String end=ends.get(i);
            	String date	=dates.get(i);
            	String room=rooms.get(i);
            	String createtime=createtimes.get(i);
            	String updatetime=updatetimes.get(i);
            	String sessionid=sessionids.get(i);
            	String classid=classids.get(i);
            	String meetingid=meetingids.get(i);
            	String title=titles.get(i);
            	Notes bean=list.get(i);
            	bean.setId(id);
            	bean.setContents(content);
            	bean.setStart(start);
            	bean.setEnd(end);
            	bean.setDate(date);
            	bean.setRoom(room);
            	bean.setCreatetime(createtime);
            	bean.setUpdatetime(updatetime);
            	bean.setSessionid(sessionid);
            	bean.setClassid(classid);
            	bean.setMeetingid(meetingid);
            	bean.setTitle(title);
            }
        }

        return list;
    }
    
    //获取提醒
    public static List<AlertBean> getAlert(DbAdapter db, String sql) {
        List<AlertBean> list = new ArrayList<AlertBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> ids = rawList.get(0);
            List<String> dates = rawList.get(1);
            List<String> enables = rawList.get(2);
            List<String> relativeids = rawList.get(3);
            List<String> repeateDistances = rawList.get(4);
            List<String> repeateTimes = rawList.get(5);
            List<String> titles = rawList.get(6);
            List<String> starts= rawList.get(7);
            List<String> ends=rawList.get(8);
            List<String> rooms=rawList.get(9);
            List<String> types=rawList.get(10);

            for (int i = 0; i < ids.size(); i++) {
            	AlertBean bean = new AlertBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String id =ids.get(i);
            	String start=starts.get(i);
            	String end=ends.get(i);
            	String date	=dates.get(i);
            	String room=rooms.get(i);
            	String enable=enables.get(i);
            	String relativeid=relativeids.get(i);
            	String repeateDistance=repeateDistances.get(i);
            	String repeateTime=repeateTimes.get(i);
            	String type=types.get(i);
            	String title=titles.get(i);
            	AlertBean bean=list.get(i);
            	bean.setId(id);
            	bean.setStart(start);
            	bean.setEnd(end);
            	bean.setDate(date);
            	bean.setRoom(room);
            	bean.setTitle(title);
            	bean.setEnable(Integer.parseInt(enable));
            	bean.setRelativeid(relativeid);
            	bean.setRepeatdistance(repeateDistance);
            	bean.setRepeattimes(repeateTime);
            	bean.setType(Integer.parseInt(type));
            }
        }

        return list;
    }
    
    //获取广告
    public static List<AdBean> getAd(DbAdapter db, String sql) {
        List<AdBean> list = new ArrayList<AdBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> adIdList = rawList.get(0);
            List<String> conferencesIdList = rawList.get(1);
            List<String> adImageList = rawList.get(2);
            List<String> adLevelList = rawList.get(3);
            List<String> adLinkList = rawList.get(4);
            List<String> imgUrlList = rawList.get(5);
            List<String> versionList = rawList.get(6);
            List<String> viewLevelList= rawList.get(7);
            List<String> stopTimeList=rawList.get(8);
            for (int i = 0; i < adIdList.size(); i++) {
            	AdBean bean = new AdBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String adId =adIdList.get(i);
            	String conferencesId=conferencesIdList.get(i);
            	String adImage=adImageList.get(i);
            	String adLevel=adLevelList.get(i);
            	String adLink=adLinkList.get(i);
            	String imgUrl=imgUrlList.get(i);
            	String version=versionList.get(i);
            	String viewLevel=viewLevelList.get(i);
            	String stopTime=stopTimeList.get(i);
            	AdBean bean=list.get(i);
                bean.setAdId(Integer.parseInt(adId));
                bean.setConferencesId(Integer.parseInt(conferencesId));
                bean.setAdImage(adImage);
                bean.setAdLevel(Integer.parseInt(adLevel));
                bean.setAdLink(adLink);
                bean.setImgUrl(imgUrl);
                bean.setVersion(Integer.parseInt(version));
                bean.setViewLevel(Integer.parseInt(viewLevel));
                bean.setStopTime(Integer.parseInt(stopTime));
            }
        }

        return list;
    }
    
    //获取地图
    public static List<MapBean> getMaps(DbAdapter db, String sql) {
        List<MapBean> list = new ArrayList<MapBean>();

        List<List<String>> rawList = db.fetchListBySql(sql);

        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            List<String> conferencesmapIdList = rawList.get(0);
            List<String> conferencesIdList = rawList.get(1);
            List<String> mapRemarkList = rawList.get(2);
            List<String> mapUrlList = rawList.get(3);

            for (int i = 0; i < conferencesmapIdList.size(); i++) {
            	MapBean bean = new MapBean();
                list.add(bean);
            }

            for (int i = 0; i < list.size(); i++) {
            	String conferencesmapId =conferencesmapIdList.get(i);
            	String conferencesId=conferencesIdList.get(i);
            	String mapRemark=mapRemarkList.get(i);
            	String mapUrl=mapUrlList.get(i);
            	MapBean bean=list.get(i);
                bean.setConferencesmapId(Integer.parseInt(conferencesmapId));
                bean.setConferencesId(Integer.parseInt(conferencesId));
                bean.setMapRemark(mapRemark);
                bean.setMapUrl(mapUrl);
            }
        }

        return list;
    }

	//获取基本信息
	public static List<RoleBean> getroleList(DbAdapter db, String sql) {
		List<RoleBean> list = new ArrayList<RoleBean>();
		List<List<String>> rawList = db.fetchListBySql(sql);

		if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
			List<String> roleIdList = rawList.get(0);
			List<String> roleNameList = rawList.get(1);
			List<String> roleEnNameList = rawList.get(2);

			for (int i = 0; i < roleIdList.size(); i++) {
				RoleBean bean = new RoleBean();
				list.add(bean);
			}

			for (int i = 0; i < list.size(); i++) {
				String roleId=roleIdList.get(i);
				String roleName=roleNameList.get(i);
				String roleEnName=roleEnNameList.get(i);

				RoleBean bean=list.get(i);
				bean.setRoleId(Integer.parseInt(roleId));
				bean.setName(roleName);
				bean.setEnName(roleEnName);
			}
		}
		return list;
	}


	/**
	 * 获取国家码所有数据
	 * @param context
	 * @return
	 */
	public static List<CountryCodeBean> getAllCountryCode(Context context) {
		List<CountryCodeBean> countrys = new ArrayList<>();
		SQLiteDatabase db = CountryDb.openDatabase(context);

		Cursor cursor = db.query("countrycode", null, null, null, null, null, null);
		CountryCodeBean bean = null;
		while(cursor.moveToNext()) {
			bean = new CountryCodeBean();
			bean.setCountry(cursor.getString(cursor.getColumnIndex("country")));
			bean.setCode(cursor.getString(cursor.getColumnIndex("code")));
			countrys.add(bean);
		}
		return countrys;
	}
}
