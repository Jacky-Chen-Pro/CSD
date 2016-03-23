package com.android.incongress.cd.conference.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.CommunityTopicBean;
import com.android.incongress.cd.conference.beans.CommunityTopicContentBean;
import com.android.incongress.cd.conference.beans.CommunityTopicResponse;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.DZBBDiscussResponseBean;
import com.android.incongress.cd.conference.beans.Detail;
import com.android.incongress.cd.conference.beans.ExhibitorActivityBean;
import com.android.incongress.cd.conference.beans.IncongressBean;
import com.android.incongress.cd.conference.beans.MessageStationBean;
import com.android.incongress.cd.conference.beans.NewBean;
import com.android.incongress.cd.conference.beans.TopicCotentSendBean;
import com.android.incongress.cd.conference.beans.UserInfoBean;
import com.android.incongress.cd.conference.beans.VersionBean;
import com.android.incongress.cd.conference.utils.JSONUtils;
import com.android.incongress.cd.conference.utils.PinyinConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.gene.model.entity.ZipResponse;

public class JsonParser {

    //参会易conferences.txt
    public static DbBean parseConference(InputStream is) {
        JSONObject obj = null;
        DbBean bean = new DbBean();
        try {
            obj = new JSONObject(readJsonString(is));

            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CONFERENCESID,
                            obj.getString(ConferenceTableField.CONFERENCES_CONFERENCESID)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_ABBREVIATION,
                            obj.getString(ConferenceTableField.CONFERENCES_ABBREVIATION)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_ADMINUSERID,
                            obj.getString(ConferenceTableField.CONFERENCES_ADMINUSERID)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CODE,
                            obj.getString(ConferenceTableField.CONFERENCES_CODE)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CONFERENCESADDRESS,
                            obj.getString(ConferenceTableField.CONFERENCES_CONFERENCESADDRESS)));
            JSONObject timeStartObj = obj.getJSONObject(ConferenceTableField.CONFERENCES_CONFERENCESSTARTTIME);
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CONFERENCESSTARTTIME,
                            getOffsetedTime(timeStartObj)));
            JSONObject timeEndObj = obj.getJSONObject(ConferenceTableField.CONFERENCES_CONFERENCESENDTIME);
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CONFERENCESENDTIME,
                            getOffsetedTime(timeEndObj)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CONFERENCESNAME,
                            obj.getString(ConferenceTableField.CONFERENCES_CONFERENCESNAME)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CONFERENCESSTATE,
                            obj.getString(ConferenceTableField.CONFERENCES_CONFERENCESSTATE)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_CREATETIME,
                            obj.getString(ConferenceTableField.CONFERENCES_CREATETIME)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_ENLINK,
                            obj.getString(ConferenceTableField.CONFERENCES_ENLINK)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_POSTERSHOWSTATE,
                            obj.getString(ConferenceTableField.CONFERENCES_POSTERSHOWSTATE)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_POSTERSTATE,
                            obj.getString(ConferenceTableField.CONFERENCES_POSTERSTATE)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_VIEWSTATE,
                            obj.getString(ConferenceTableField.CONFERENCES_VIEWSTATE)));
            bean.getContainer().add(
                    Pair.create(ConferenceTableField.CONFERENCES_ZHLINK,
                            obj.getString(ConferenceTableField.CONFERENCES_ZHLINK)));

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return bean;
    }

    //会议表 sessionNew.txt,目前是session2.0
    public static List<DbBean> parseSession(InputStream is) {
        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer().add(Pair.create(ConferenceTableField.SESSION_SESSIONGROUPID, obj.getString(ConferenceTableField.SESSION_SESSIONGROUPID)));
                String sessionname = obj.getString(ConferenceTableField.SESSION_SESSIONNAME);
                String values[] = sessionname.split(Constants.ENCHINASPLIT);

                //#@#的不同处理
                String name = "";
                String enName = "";
                if (values.length == 1) {
                    name = values[0];
                    enName = values[0];
                }

                if (values.length == 2) {
                    if (values[0].length() == 0) {
                        name = enName = values[1];
                    }
                    if (values[1].length() == 0) {
                        name = enName = values[0];
                    } else {
                        name = values[0];
                        enName = values[1];
                    }
                }
                bean.getContainer().add(Pair.create(ConferenceTableField.SESSION_SESSIONNAME, name));
                bean.getContainer().add(Pair.create(ConferenceTableField.SESSION_SESSIONNAME_EN, enName));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.SESSION_CLASSESID, obj.getString(ConferenceTableField.SESSION_CLASSESID)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.SESSION_SESSIONDAY, obj.getString(ConferenceTableField.SESSION_SESSIONDAY)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.SESSION_STARTTIME, obj.getString(ConferenceTableField.SESSION_STARTTIME)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.SESSION_ENDTIME, obj.getString(ConferenceTableField.SESSION_ENDTIME)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SESSION_CONFIELDID, obj.getString(ConferenceTableField.SESSION_CONFIELDID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SESSION_REMARK, obj.getString(ConferenceTableField.SESSION_REMARK)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SESSION_ATTENTION, String.valueOf(Constants.SMNOATTENTION)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SESSION_FACULTYID, obj.getString(ConferenceTableField.SESSION_FACULTYID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SESSION_ROLEID, obj.getString(ConferenceTableField.SESSION_ROLEID)));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    //会议中的演讲表 meetingNew.txt
    public static List<DbBean> parseMeeting(InputStream is) {

        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer().add(Pair.create(ConferenceTableField.MEETING_MEETINGID, obj.getString(ConferenceTableField.MEETING_MEETINGID)));

                String topic = obj.getString(ConferenceTableField.MEETING_TOPIC);
                String topicvalues[] = topic.split(Constants.ENCHINASPLIT);

                //#@#的不同处理
                String name = "";
                String enName = "";
                if (topicvalues.length == 1) {
                    name = topicvalues[0];
                    enName = topicvalues[0];
                }

                if (topicvalues.length == 2) {
                    if (topicvalues[0].length() == 0) {
                        name = enName = topicvalues[1];
                    }
                    if (topicvalues[1].length() == 0) {
                        name = enName = topicvalues[0];
                    } else {
                        name = topicvalues[0];
                        enName = topicvalues[1];
                    }
                }
                bean.getContainer().add(Pair.create(ConferenceTableField.MEETING_TOPIC, name));
                bean.getContainer().add(Pair.create(ConferenceTableField.MEETING_TOPIC_EN, enName));

                String summary = obj.getString(ConferenceTableField.MEETING_SUMMARY);
                String summaryvalues[] = summary.split(Constants.ENCHINASPLIT);

                //#@#的不同处理
                String summaryName = "";
                String summaryEnName = "";
                if (summaryvalues.length == 1) {
                    summaryName = summaryvalues[0];
                    summaryEnName = summaryvalues[0];
                }

                if (summaryvalues.length == 2) {
                    if (summaryvalues[0].length() == 0) {
                        summaryName = summaryEnName = summaryvalues[1];
                    }
                    if (summaryvalues[1].length() == 0) {
                        summaryName = summaryEnName = summaryvalues[0];
                    } else {
                        summaryName = summaryvalues[0];
                        summaryEnName = summaryvalues[1];
                    }
                }

                bean.getContainer().add(Pair.create(ConferenceTableField.MEETING_SUMMARY, summaryName));
                bean.getContainer().add(Pair.create(ConferenceTableField.MEETING_SUMMARY_EN, summaryEnName));

                bean.getContainer().add(
                        Pair.create(ConferenceTableField.MEETING_CLASSESID,
                                obj.getString(ConferenceTableField.MEETING_CLASSESID)));
//				bean.getContainer().add(
//						Pair.create(ConferenceTableField.MEETING_MODERATORIDS,
//								obj.getString(ConferenceTableField.MEETING_MODERATORIDS)));
//				bean.getContainer().add(
//						Pair.create(ConferenceTableField.MEETING_SPEAKERIDS,
//								obj.getString(ConferenceTableField.MEETING_SPEAKERIDS)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.MEETING_MEETINGDAY,
                                obj.getString(ConferenceTableField.MEETING_MEETINGDAY)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.MEETING_STARTTIME,
                                obj.getString(ConferenceTableField.MEETING_STARTTIME)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.MEETING_ENDTIME,
                                obj.getString(ConferenceTableField.MEETING_ENDTIME)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.MEETING_CONFIELDID,
                                obj.getString(ConferenceTableField.MEETING_CONFIELDID)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.MEETING_SESSIONGROUPID,
                                obj.getString(ConferenceTableField.MEETING_SESSIONGROUPID)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.MEETING_ATTENTION,
                                String.valueOf(Constants.SMNOATTENTION)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.MEETING_FACULTYID, obj.getString(ConferenceTableField.MEETING_FACULTYID)));
                bean.getContainer().add(
                        Pair.create(ConferenceTableField.MEETING_ROLEID, obj.getString(ConferenceTableField.MEETING_ROLEID)));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    //领域 conField.txt
    public static List<DbBean> parseConField(InputStream is) {

        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.CONFIELD_CONFIELDID,
                                obj.getString(ConferenceTableField.CONFIELD_CONFIELDID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.CONFIELD_CONFIELDNAME,
                                obj.getString(ConferenceTableField.CONFIELD_CONFIELDNAME)));

                list.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    //会议室 classes.txt
    public static List<DbBean> parseClasses(InputStream is) {
        List<ClassesBean> listdata = new ArrayList<ClassesBean>();
        Gson gson = new Gson();
        listdata = (List<ClassesBean>) gson.fromJson(readJsonString(is), new TypeToken<List<ClassesBean>>() {
        }.getType());
        List<DbBean> list = new ArrayList<DbBean>();
        try {
            for (int i = 0; i < listdata.size(); i++) {
                DbBean bean = new DbBean();
                ClassesBean classbean = listdata.get(i);
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.CLASS_CLASSESID,
                                String.valueOf(classbean.getClassesId())));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.CLASS_CONFERENCESID,
                                String.valueOf(classbean.getConferencesId())));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.CLASS_CLASSESCAPACITY,
                                String.valueOf(classbean.getClassesCapacity())));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.CLASS_CLASSESCODE,
                                String.valueOf(classbean.getClassesCode())));
                bean.getContainer().add(Pair.create(ConferenceTableField.CLASS_CLASSESLOCATION,
                        String.valueOf(classbean.getClassesLocation())));
                bean.getContainer().add(Pair.create(ConferenceTableField.CLASS_CLASSLEVEL, String.valueOf(classbean.getLevel())));
                bean.getContainer().add(Pair.create(ConferenceTableField.CLASS_MAPNAME, String.valueOf(classbean.getMapName())));
                String codepingyin = PinyinConverter.getPinyin(classbean.getClassesCode());
                bean.getContainer().add(Pair.create(ConferenceTableField.CLASS_CLASSESCODEPINGYIN,
                        codepingyin));
                list.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("---发生异常发生异常-----");
            return null;
        }
        return list;
    }

    //演讲者 speaker.txt
    public static List<DbBean> parseSpeakers(InputStream is) {

        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_SPEAKERID,
                                obj.getString(ConferenceTableField.SPEAKER_SPEAKERID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_CONFERENCESID,
                                obj.getString(ConferenceTableField.SPEAKER_CONFERENCESID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_REMARK,
                                obj.getString(ConferenceTableField.SPEAKER_REMARK)));
                bean.getContainer().add(Pair.create(ConferenceTableField.SPEAKER_SPEAKERFROM,
                        obj.getString
                                (ConferenceTableField.SPEAKER_SPEAKERFROM) == null ? "" : obj.getString(ConferenceTableField.SPEAKER_SPEAKERFROM)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_SPEAKERNAME,
                                obj.getString(ConferenceTableField.SPEAKER_SPEAKERNAME).replace("\n", "")));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_TYPE,
                                obj.getString(ConferenceTableField.SPEAKER_TYPE)));

                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_ENNAME, obj.getString(ConferenceTableField.SPEAKER_ENNAME)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_ENTITYID, obj.getString(ConferenceTableField.SPEAKER_ENTITYID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_PYKEY, obj.getString(ConferenceTableField.SPEAKER_PYKEY)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_USERID, obj.getString(ConferenceTableField.SPEAKER_USERID)));
                String speakernamepingyin = PinyinConverter.getPinyin(obj.getString(ConferenceTableField.SPEAKER_SPEAKERNAME).replace("\n", ""));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.SPEAKER_SPEAKERNAMEPINGYIN,
                                speakernamepingyin.equals("") ? ConferenceTableField.SPEAKER_ENNAME :
                                        speakernamepingyin.toUpperCase()));
                list.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    //基本信息 tips.txt
    public static List<DbBean> parseTipss(InputStream is) {

        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.TIPS_TIPSID,
                                obj.getString(ConferenceTableField.TIPS_TIPSID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.TIPS_CONFERENCESID,
                                obj.getString(ConferenceTableField.TIPS_CONFERENCESID)));
                String content = obj.getString(ConferenceTableField.TIPS_TIPSCONTENT);
                String contentvalues[] = content.split(Constants.ENCHINASPLIT);

                //#@#的不同处理
                String name = "";
                String enName = "";
                if (contentvalues.length == 1) {
                    name = contentvalues[0];
                    enName = contentvalues[0];
                }

                if (contentvalues.length == 2) {
                    if (contentvalues[0].length() == 0) {
                        name = enName = contentvalues[1];
                    }
                    if (contentvalues[1].length() == 0) {
                        name = enName = contentvalues[0];
                    } else {
                        name = contentvalues[0];
                        enName = contentvalues[1];
                    }
                }
                bean.getContainer().add(Pair.create(ConferenceTableField.TIPS_TIPSCONTENT, name));
                bean.getContainer().add(Pair.create(ConferenceTableField.TIPS_TIPSCONTENT_EN, enName));
//                if(contentvalues.length>0){
//                	//参会指南内容 中文
//    				bean.getContainer().add(
//    						Pair.create(ConferenceTableField.TIPS_TIPSCONTENT,
//    								contentvalues[0]));	
//    				if(contentvalues.length==2){
//                    	//参会指南内容 英文
//        				bean.getContainer().add(
//        						Pair.create(ConferenceTableField.TIPS_TIPSCONTENT_EN,
//        								contentvalues[1]));		
//    				}else {
//    					//参会指南内容 英文
//        				bean.getContainer().add(
//        						Pair.create(ConferenceTableField.TIPS_TIPSCONTENT_EN,""));
//    				}
//                }
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.TIPS_TIPSTIME,
                                obj.getString(ConferenceTableField.TIPS_TIPSTIME)));
                String title = obj.getString(ConferenceTableField.TIPS_TIPSTITLE);
                String titlevalues[] = title.split(Constants.ENCHINASPLIT);

                //#@#的不同处理
                String titleName = "";
                String titleEnName = "";
                if (titlevalues.length == 1) {
                    titleName = titlevalues[0];
                    titleEnName = titlevalues[0];
                }

                if (titlevalues.length == 2) {
                    if (titlevalues[0].length() == 0) {
                        titleName = titleEnName = titlevalues[1];
                    }
                    if (titlevalues[1].length() == 0) {
                        titleName = titleEnName = titlevalues[0];
                    } else {
                        titleName = titlevalues[0];
                        titleEnName = titlevalues[1];
                    }
                }
                bean.getContainer().add(Pair.create(ConferenceTableField.TIPS_TIPSTITLE, titleName));
                bean.getContainer().add(Pair.create(ConferenceTableField.TIPS_TIPSTITLE_EN, titleEnName));

//                if(titlevalues.length>0){
//                	//参会指南标题 中文
//    				bean.getContainer().add(
//    						Pair.create(ConferenceTableField.TIPS_TIPSTITLE,
//    								titlevalues[0]));	
//    				if(titlevalues.length==2){
//                    	//参会指南标题 英文
//        				bean.getContainer().add(
//        						Pair.create(ConferenceTableField.TIPS_TIPSTITLE_EN,
//        								titlevalues[1]));		
//    				}else {
//        				bean.getContainer().add(
//        						Pair.create(ConferenceTableField.TIPS_TIPSTITLE_EN,""));
//    				}
//                }
                list.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    //参展商 exhibitorsNew.txt
    public static List<DbBean> parseExhibitors(InputStream is) {
        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();
        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_EXHIBITORSID, obj.getString(ConferenceTableField.EXHIBITOR_EXHIBITORSID)));//exhibitorsId

                String address = obj.getString(ConferenceTableField.EXHIBITOR_ADDRESS);
                String[] addresses = getChinesAndEnBySplit(address);
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_ADDRESS, addresses[0]));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_ADDRESS_EN, addresses[1]));

                String title = obj.getString(ConferenceTableField.EXHIBITOR_TITLE);
                String[] titles = getChinesAndEnBySplit(title);
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_TITLE, titles[0]));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_TITLE_EN, titles[1]));

                String info = obj.getString(ConferenceTableField.EXHIBITOR_INFO);
                String infos[] = getChinesAndEnBySplit(info);

                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_INFO, infos[0]));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_INFO_EN, infos[1]));

                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_PHONE, obj.getString(ConferenceTableField.EXHIBITOR_PHONE)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_FAX, obj.getString(ConferenceTableField.EXHIBITOR_FAX)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_NET, obj.getString(ConferenceTableField.EXHIBITOR_NET)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_LOCATION, obj.getString(ConferenceTableField.EXHIBITOR_LOCATION)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_LOGO, obj.getString(ConferenceTableField.EXHIBITOR_LOGO)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_ATTENTION, String.valueOf(Constants.NOATTENTION)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_STORELOGO, String.valueOf(0)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_EXHIBITOR_LOCATION, obj.getString(ConferenceTableField.EXHIBITOR_EXHIBITOR_LOCATION)));
                bean.getContainer().add(Pair.create(ConferenceTableField.EXHIBITOR_MAP_NAME , obj.getString(ConferenceTableField.EXHIBITOR_MAP_NAME)));
                list.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    //广告 ad.txt
    public static List<DbBean> parseAds(InputStream is) {

        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_ADID,
                                obj.getString(ConferenceTableField.AD_ADID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_CONFERENCESID,
                                obj.getString(ConferenceTableField.AD_CONFERENCESID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_ADIMAGE,
                                obj.getString(ConferenceTableField.AD_ADIMAGE)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_ADLEVEL,
                                obj.getString(ConferenceTableField.AD_ADLEVEL)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_ADLINK,
                                obj.getString(ConferenceTableField.AD_ADLINK)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_IMGURL,
                                obj.getString(ConferenceTableField.AD_IMGURL)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_VERSION,
                                obj.getString(ConferenceTableField.AD_VERSION)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_VIEWLEVEL,
                                obj.getString(ConferenceTableField.AD_VIEWLEVEL)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.AD_STOPTIME,
                                obj.getString(ConferenceTableField.AD_STOPTIME)));
                list.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }


    //地图 conferencesMap.txt
    public static List<DbBean> parseMaps(InputStream is) {

        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));
            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.MAP_CONFERENCESMAPID,
                                obj.getString(ConferenceTableField.MAP_CONFERENCESMAPID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.MAP_CONFERENCESID,
                                obj.getString(ConferenceTableField.MAP_CONFERENCESID)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.MAP_MAPREMARK,
                                obj.getString(ConferenceTableField.MAP_MAPREMARK)));
                bean.getContainer()
                        .add(Pair.create(ConferenceTableField.MAP_MAPURL,
                                obj.getString(ConferenceTableField.MAP_MAPURL)));
                list.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    //会议表 sessionNew.txt,目前是session2.0
    public static List<DbBean> parseRole(InputStream is) {
        JSONArray array = null;
        List<DbBean> list = new ArrayList<DbBean>();

        try {
            array = new JSONArray(readJsonString(is));

            for (int i = 0; i < array.length(); i++) {
                DbBean bean = new DbBean();

                JSONObject obj = array.getJSONObject(i);

                bean.getContainer().add(Pair.create(ConferenceTableField.ROLE_ID, obj.getString(ConferenceTableField.ROLE_ID)));
                bean.getContainer().add(Pair.create(ConferenceTableField.ROLE_NAME, obj.getString(ConferenceTableField.ROLE_NAME)));
                bean.getContainer().add(Pair.create(ConferenceTableField.ROLE_ENNAME, obj.getString(ConferenceTableField.ROLE_ENNAME)));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
        return list;
    }

    public static String readJsonString(InputStream is) {
        String jsonStr = "";
        try {
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            jsonStr = EncodingUtils.getString(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }

    private static String getOffsetedTime(JSONObject obj) {
        try {
            return obj.getString("time");
            // long time = Long.valueOf(obj.getString("time"));
            // long offset = Long.valueOf(obj.getString("timezoneOffset"));
            // GMT+8:00
            // return String.valueOf(time + 28800000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<ZipResponse> parseZipList(String str) {
        JSONArray array = null;
        List<ZipResponse> list = new ArrayList<ZipResponse>();

        try {
            array = new JSONArray(str);

            int count = array.length();
            for (int i = 0; i < count; i++) {
                ZipResponse n = new ZipResponse();
                JSONObject newObj = array.getJSONObject(i);

                n.setType(newObj.getInt("type"));
                n.setVersion(newObj.getInt("version"));
                n.setZipUrl(newObj.getString("zipUrl"));

                list.add(n);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    //用于与服务端解析的json

    //解析会议社区 主页的信息
    public static List<CommunityTopicBean> parseCommunityTopReponse(String str) {
        List<CommunityTopicBean> list = new ArrayList<CommunityTopicBean>();
        Gson gson = new Gson();
        list = (List<CommunityTopicBean>) gson.fromJson(str, new TypeToken<List<CommunityTopicBean>>() {
        }.getType());
        return list;
    }

    //解析会议社区 中的评论 列表
    public static List<CommunityTopicContentBean> parseTopicContentReponse(String str) {
        List<CommunityTopicContentBean> list = new ArrayList<CommunityTopicContentBean>();
        Gson gson = new Gson();
        list = (List<CommunityTopicContentBean>) gson.fromJson(str, new TypeToken<List<CommunityTopicContentBean>>() {
        }.getType());
        return list;
    }

    //解析 会议社区中的评论 发表一条评论后 反馈的数据
    public static TopicCotentSendBean parseTopicContentSendResponse(String str) {
        TopicCotentSendBean mBean = new TopicCotentSendBean();
        Gson gson = new Gson();
        mBean = gson.fromJson(str, TopicCotentSendBean.class);
        return mBean;
    }

    //解析说两句的反馈数据
    public static CommunityTopicResponse parseTopicSendResponse(String str) {
        CommunityTopicResponse mBean = new CommunityTopicResponse();
        Gson gson = new Gson();
        mBean = gson.fromJson(str, CommunityTopicResponse.class);
        return mBean;
    }

    //解析站上活动的信息
    public static List<ExhibitorActivityBean> parseExhibitorActivity(String str) {
        List<ExhibitorActivityBean> list = new ArrayList<ExhibitorActivityBean>();
        Gson gson = new Gson();
        list = (List<ExhibitorActivityBean>) gson.fromJson(str, new TypeToken<List<ExhibitorActivityBean>>() {
        }.getType());
        return list;
    }

    //解析会议之声 新闻的信息
    public static List<NewBean> parseNewsReponse(String str) {
        String value = "";
        try {
            JSONObject obj = new JSONObject(str);
            value = obj.getString("newsList");
        } catch (Exception e) {

        }
        List<NewBean> list = new ArrayList<NewBean>();
        Gson gson = new Gson();
        list = (List<NewBean>) gson.fromJson(value, new TypeToken<List<NewBean>>() {
        }.getType());
        return list;
    }

    //解析会议之志 新闻的单个信息
    public static NewBean parseNewBean(String str) {
        NewBean mBean = new NewBean();
        Gson gson = new Gson();
        mBean = gson.fromJson(str, NewBean.class);
        return mBean;
    }

    //解析状态 
    public static int parseState(String str) {
        int state = 0;
        try {
            JSONObject stateobj = new JSONObject(str);
            state = stateobj.getInt("state");
        } catch (Exception e) {

        }

        return state;
    }

    //解析初始化数据
    public static IncongressBean parseIncongress(String str) {
        IncongressBean bean = new IncongressBean();
        try {
            JSONObject obj = new JSONObject(str);
            String client = obj.getString("client");
            String appVersion = obj.getString("appVersion");
            String clientVersion = "";
            if (client.equals("1")) {
                clientVersion = obj.getString("clientVersion");
            }
            String url = "";
            if (client.equals("1")) {
                url = obj.getString("url");
            }
            String version = obj.getString("version");
            int newsCount = obj.getInt("newsCount");
            int reCount = obj.getInt("reCount");
            List<VersionBean> versions = new ArrayList<VersionBean>();
            Gson gson = new Gson();
            versions = (List<VersionBean>) gson.fromJson(version, new TypeToken<List<VersionBean>>() {
            }.getType());
            bean.setClient(client);
            bean.setAppVersion(appVersion);
            bean.setClientVersion(clientVersion);
            bean.setUrl(url);
            bean.setReCount(reCount);
            bean.setNewsCount(newsCount);
            bean.setVersionList(versions);
        } catch (Exception e) {

        }
        return bean;
    }

    //解析用户登陆成功后返回的信息
    public static UserInfoBean parseUserInfo(String str) {
        UserInfoBean mBean = new UserInfoBean();
        Gson gson = new Gson();
        mBean = gson.fromJson(str, UserInfoBean.class);
        return mBean;
    }

    //解析广告更新的信息
    public static List<AdBean> parseAdList(String str) {
        List<AdBean> list = new ArrayList<AdBean>();
        Gson gson = new Gson();
        list = (List<AdBean>) gson.fromJson(str, new TypeToken<List<AdBean>>() {
        }.getType());
        return list;
    }

    /**
     * 解析精彩回顾的信息
     *
     * @param json
     * @return
     */
    public static List<Detail> parseReviews(String json) {
        List<Detail> reviews = new ArrayList<Detail>();
        int state = JSONUtils.getInt(json, "state", -1);
        int maxCount = JSONUtils.getInt(json, "maxCount", -1);
        JSONArray array = JSONUtils.getJSONArray(json, "caseList", null);

        if (array == null) {
            return null;
        }

        if (state == 0) {
            return null;
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                int caseId = obj.getInt("caseId");
                String title = obj.getString("title");
                String dataUrl = obj.getString("dataUrl");
                int type = obj.getInt("type");
                int fieldId = obj.getInt("fieldId");
                String fieldName = obj.getString("fieldName");
                String createTime = obj.getString("createTime");

                Detail detail = new Detail(caseId, title, dataUrl, createTime, type, fieldId, fieldName);
                reviews.add(detail);
                System.out.println("detail======" + detail.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }

    public static List<DZBBBean> parseDzbb(String json) {
        List<DZBBBean> dzbbs = new ArrayList<DZBBBean>();
        int maxCount = JSONUtils.getInt(json, "maxCount", -1);
        JSONArray array = JSONUtils.getJSONArray(json, "array", null);

        if (array == null) {
            return null;
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                int posterId = obj.getInt("posterId");
                String posterCode = obj.getString("posterCode");
                String conField = obj.getString("conField");
                String title = obj.getString("title");
                String author = obj.getString("author");
                String posterPicUrl = obj.getString("posterPicUrl");
                int disCount = obj.getInt("disCount");

                DZBBBean bean = new DZBBBean(posterId, posterCode, conField, title, author, posterPicUrl, maxCount, disCount);
                dzbbs.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dzbbs;
    }

    //解析电子壁报中的评论 列表
    public static List<CommunityTopicContentBean> parseDZBBContent(String json) {
        List<CommunityTopicContentBean> list = new ArrayList<CommunityTopicContentBean>();
        int maxCount = JSONUtils.getInt(json, "maxCount", -1);
        JSONArray array = JSONUtils.getJSONArray(json, "array", null);
        System.out.println("数据===" + json);
        if (maxCount == 0) {
            return list;
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String userName = obj.getString("userName");
                String content = obj.getString("content");
                String createTime = obj.getString("createTime");

                CommunityTopicContentBean bean = new CommunityTopicContentBean();
                bean.setUserName(userName);
                bean.setContent(content);
                bean.setTime(createTime);
                list.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //解析发表电子壁报评论后返回的数据，也就是是否成功
    public static DZBBDiscussResponseBean parseDiscussSuccess(String json) {
        DZBBDiscussResponseBean bean = new DZBBDiscussResponseBean();
        int state = JSONUtils.getInt(json, "state", 0);
        int userId = JSONUtils.getInt(json, "userId", -1);
        String userName = JSONUtils.getString(json, "userName", "游客未知");
        int posterDiscussId = JSONUtils.getInt(json, "posterDiscussId", -1);
        bean.setUserName(userName);
        bean.setUserId(userId);
        bean.setState(state);
        bean.setPosterDiscussId(posterDiscussId);
        return bean;
    }

    // 根据id获得一个评论
    public static CommunityTopicContentBean parseDZBBOneContent(String json) {
        CommunityTopicContentBean bean = new CommunityTopicContentBean();
        String userName = JSONUtils.getString(json, "userName", "");
        String content = JSONUtils.getString(json, "content", "");
        String createTime = JSONUtils.getString(json, "createTime", "");
        bean.setUserName(userName);
        bean.setTime(createTime);
        bean.setType(1);
        bean.setContent(content);

        if (userName.equals("-1")) {
            return null;
        }
        return bean;
    }

    public static List<MessageStationBean> parseMessageStation(String json) {
        List<MessageStationBean> msgBeans = new ArrayList<MessageStationBean>();

        int state = JSONUtils.getInt(json, "state", 0);
        if (state == 0) {
            return msgBeans;
        }

        int maxCount = JSONUtils.getInt(json, "maxCount", -1);
        JSONArray array = JSONUtils.getJSONArray(json, "tokenList", null);

        if (array == null) {
            return msgBeans;
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                int tokenMessageId = obj.getInt("tokenMessageId");
                String content = obj.getString("content");
                String createTime = obj.getString("createTime");

                MessageStationBean bean = new MessageStationBean(content, createTime, 1, tokenMessageId);
                msgBeans.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return msgBeans;
    }

    //分别获取中文和英文的数据
    public static String[] getChinesAndEnBySplit(String content) {
        String name = "";
        String enName = "";

        String contents[] = content.split(Constants.ENCHINASPLIT);
        if (contents.length == 1) {
            name = contents[0];
            enName = contents[0];
        }

        if (contents.length == 2) {
            if (contents[0].length() == 0) {
                name = enName = contents[1];
            }
            if (contents[1].length() == 0) {
                name = enName = contents[0];
            } else {
                name = contents[0];
                enName = contents[1];
            }
        }

        return new String[]{name, enName};
    }
}
