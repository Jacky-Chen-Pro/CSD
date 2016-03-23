package com.android.incongress.cd.conference.data;

public class ConferenceTableField {
	//表示 表中的字段
	//CONFERENCES 参会议表
    public static final String CONFERENCES_CONFERENCESID="conferencesId";//会议编号 用于区分会议 这个很重要
	public static final String CONFERENCES_ABBREVIATION="abbreviation"; //会议版本名称
	public static final String CONFERENCES_ADMINUSERID="adminuserId";//会议管理员ID
	public static final String CONFERENCES_CODE="code";
	public static final String CONFERENCES_CONFERENCESADDRESS="conferencesAddress";//会议地址
    public static final String CONFERENCES_CONFERENCESSTARTTIME="conferencesStartTime";//会议开始时间
    public static final String CONFERENCES_CONFERENCESENDTIME="conferencesEndTime";//会议结束时间
    public static final String CONFERENCES_CONFERENCESNAME="conferencesName";//会议名称
    public static final String CONFERENCES_CONFERENCESSTATE="conferencesState";//会议状态
    public static final String CONFERENCES_CREATETIME="createTime";//会议创建时间
    public static final String CONFERENCES_ENLINK="enLink";//英文链接
    public static final String CONFERENCES_POSTERSHOWSTATE="posterShowState";//是否显示壁报
    public static final String CONFERENCES_POSTERSTATE="posterState";//壁报状态
    public static final String CONFERENCES_VIEWSTATE="viewState";//是否查看
    public static final String CONFERENCES_ZHLINK="zhLink";//中文链接
    
    //会议表 SESSION
    //2015.1.28 新增五个字段 chairIds coChairIds discussantIds panelistIds judgeIds
    public static final String SESSION_SESSIONGROUPID="sessionGroupId";//会议编号 通过去在MEETING查找 演讲
    public static final String SESSION_SESSIONNAME="sessionName";//	会议名称
    public static final String SESSION_CLASSESID="classesId";//会议室编号
//    public static final String SESSION_MODERATORIDS="moderatorIds";//主持人编号集合
    public static final String SESSION_SESSIONDAY="sessionDay";//会议日期
    public static final String SESSION_STARTTIME="startTime";//会议开始时间
    public static final String SESSION_ENDTIME="endTime";//会议结束时间
    public static final String SESSION_CONFIELDID="conFieldId";//领域编号
    public static final String SESSION_REMARK="remark";//摘要
    public static final String SESSION_ATTENTION="attention";//关注 用户对会议的关注设置
    public static final String SESSION_SESSIONNAME_EN="sessionName_En";//会议名称 英文
    public static final String SESSION_FACULTYID = "facultyId";//用户Id
    public static final String SESSION_ROLEID = "roleId";//用户Id对应的身份Id
//    public static final String SESSION_CHAIRIDS = "chairIds"; //主席
//    public static final String SESSION_COCHAIRIDS = "coChairIds"; //联合主席
//    public static final String SESSION_DISCUSSANTIDS = "discussantIds"; //讨论者
//    public static final String SESSION_PANELISTIDS = "panelistIds"; //评论者
//    public static final String SESSION_JUDGEIDS = "judgeIds"; //评委
    
    
    //会议演讲表 在会议中的演讲 MEETING 
    //2015.1.28 新加入的字段 interpreterIds presenterIds speakIds operatorIds IVUSInterpreterIds
    //IVUSInterpreterIds AwardPresenterIds AwardRecepientIds othersIds
    public static final String MEETING_MEETINGID="meetingId";//会议演讲Id编号
    public static final String MEETING_TOPIC="topic";//会议演讲主题
    public static final String MEETING_SUMMARY="summary";//会议演讲概要
    public static final String MEETING_CLASSESID="classesId";//会议室编号
//    public static final String MEETING_MODERATORIDS="moderatorIds";//主持人列表ID集合
//    public static final String MEETING_SPEAKERIDS="speakerIds";//演讲者列表
    public static final String MEETING_MEETINGDAY="meetingDay";//会议演讲日期
    public static final String MEETING_STARTTIME="startTime";//会议演讲开始时间
    public static final String MEETING_ENDTIME="endTime";//会议演讲结束时间
    public static final String MEETING_CONFIELDID="conFieldId";//领域编号
    public static final String MEETING_SESSIONGROUPID="sessionGroupId";//这个表明是那个会议的 演讲 这个是SESSION表的主键
    public static final String MEETING_ATTENTION = "attention";//关注 用户对演讲的关注设置
    public static final String MEETING_TOPIC_EN = "topic_En";//会议演讲主题 英文
    public static final String MEETING_SUMMARY_EN = "summary_En";//会议演讲概要 英文
//    public static final String MEETING_INTERPRETERIDS = "interpreterIds"; // 翻译
//    public static final String MEETING_PRESENTERIDS = "presenterIds"; //报告者
//    public static final String MEETING_SPEAKIDS = "speakIds"; //发言人
//    public static final String MEETING_OPERATORIDS = "operatorIds"; //术者
//    public static final String MEETING_IVUSINTERPRETERIDS = "IVUSInterpreterIds";//解说
//    public static final String MEETING_AWARDPRESENTERIDS = "AwardPresenterIds";//颁奖者
//    public static final String MEETING_AWARDRECEPIENTIDS = "AwardRecepientIds";//领奖者
//    public static final String MEETING_LEADDISCUSSANTIDS = "leadDiscussantIds"; //Lead Discussant 引导讨论者
//    public static final String MEETING_OTHERSIDS = "otherIds"; //其他
    public static final String MEETING_FACULTYID = "facultyId";//身份Id
    public static final String MEETING_ROLEID = "roleId";//身份类型
    
    
    //领域表 CONFIELD
    public static final String CONFIELD_CONFIELDID="conFieldId";//领域编号
    public static final String CONFIELD_CONFIELDNAME="conFieldName";//领域名称
    
    //会议室 CLASS
    public static final String CLASS_CLASSESID="classesId";//会议室编号
    public static final String CLASS_CONFERENCESID="conferencesId";//参会议编号
    public static final String CLASS_CLASSESCAPACITY="classesCapacity";//会议室容纳人数
    public static final String CLASS_CLASSESCODE="classesCode";//会议室用途 会议室名称
    public static final String CLASS_CLASSESLOCATION="classesLocation";//会议室位置
    public static final String CLASS_MAPNAME="mapName";//地图图片
    public static final String CLASS_CLASSLEVEL="level";//会议室排列等级
    public static final String CLASS_CLASSESCODEPINGYIN="classesCodePingyin";//会议室名称的拼音 方便排序
    
    //演讲者表 SPEAKER
    //2015.1.28 加入了enName entityId pykey
    public static final String SPEAKER_SPEAKERID="speakerId";//演讲者编号
    public static final String SPEAKER_CONFERENCESID="conferencesId";//参会议编号
    public static final String SPEAKER_REMARK="remark";//备注
    public static final String SPEAKER_SPEAKERFROM="speakerFrom";//演讲者来至于
    public static final String SPEAKER_SPEAKERNAME="speakerName";//演讲者姓名
    public static final String SPEAKER_TYPE="type";//类型
    public static final String SPEAKER_SPEAKERNAMEPINGYIN="speakerNamePingyin";//演讲者姓名拼音
    public static final String SPEAKER_ATTENTION="attention";//关注的讲者 @讲者
    public static final String SPEAKER_ENNAME = "enName"; //英文名
    public static final String SPEAKER_ENTITYID = "entityId"; //未知
    public static final String SPEAKER_PYKEY = "pykey"; //未知
    public static final String SPEAKER_USERID = "userId"; // 用户Id貌似是唯一参数
    
    //参展商表 EXHIBITOR
    public static final String EXHIBITOR_EXHIBITORSID="exhibitorsId";//参展商Id
    public static final String EXHIBITOR_ADDRESS="address";//地址
    public static final String EXHIBITOR_TITLE="title";//参展商名称
    public static final String EXHIBITOR_INFO="info";//参展商简介
    public static final String EXHIBITOR_PHONE="phone";//联系电话
    public static final String EXHIBITOR_FAX="fax";//传真号码
    public static final String EXHIBITOR_NET="net";//公司链接
    public static final String EXHIBITOR_LOCATION="location";//展位
    public static final String EXHIBITOR_LOGO="logo";//Logo图片
    public static final String EXHIBITOR_ATTENTION="attention";//关注 用户对参展商的关注设置
    public static final String EXHIBITOR_STORELOGO="storelogo";//是否存储了logo
    public static final String EXHIBITOR_ADDRESS_EN="address_En";//地址 英文
    public static final String EXHIBITOR_TITLE_EN="title_En";//参展商名称 英文
    public static final String EXHIBITOR_INFO_EN="info_En";//参展商简介 英文
    public static final String EXHIBITOR_EXHIBITOR_LOCATION = "exhibitorsLocation";//展位的定位图
    public static final String EXHIBITOR_MAP_NAME = "mapName"; //展商地图
   
    //展商活动 EXHIBITORACTYIVITY
    public static final String EXHIBITORACTYIVITY_ACTIVITYID="activityId";//活动ID
    public static final String EXHIBITORACTYIVITY_NAME="name";//活动名称
    public static final String EXHIBITORACTYIVITY_VERSION="version";//数据版本
    public static final String EXHIBITORACTYIVITY_HOT="hot";//是否为hot
    public static final String EXHIBITORACTYIVITY_URL="url";//下载地址
    public static final String EXHIBITORACTYIVITY_LOGO="logo";//预览图
    public static final String EXHIBITORACTYIVITY_STORELOGO="storelogo";//是否存储了logo
    public static final String EXHIBITORACTYIVITY_STOREURL="storeurl";//是否存储了url
    public static final String EXHIBITORACTYIVITY_ATTENTION="attention";//关注 用户对展商活动的关注设置
  
    //基本信息 TIPS
    public static final String TIPS_TIPSID="tipsId";//基本信息编号
    public static final String TIPS_CONFERENCESID="conferencesId";// 会议编号 用于区分会议 这个很重要
    public static final String TIPS_TIPSCONTENT="tipsContent";//详细内容
    public static final String TIPS_TIPSTIME="tipsTime";//创建时间 
    public static final String TIPS_TIPSTITLE="tipsTitle";//标题
    public static final String TIPS_TIPSTITLE_EN="tipsTitle_En";//标题 英文
    public static final String TIPS_TIPSCONTENT_EN="tipsContent_En";//详细内容 英文
    
    //大会笔记 NOTES
    public static final String NOTES_ID="id";//笔记ID
    public static final String NOTES_TITLE="title";//笔记标题
    public static final String NOTES_DATE="date";//大会日期
    public static final String NOTES_START="start";//大会开始时间
    public static final String NOTES_END="end";//大会结束时间
    public static final String NOTES_ROOM="room";//会议室
    public static final String NOTES_CLASSID="classid";//classid
    public static final String NOTES_SESSIONID="sessionid";//sessionid
    public static final String NOTES_MEETINGID="meetingid";//meetingid
    public static final String NOTES_CONTENTS="contents";//笔记内容
    public static final String NOTES_UPDATETIME="updatetime";//修改时间
    public static final String NOTES_CREATETIME="createtime";//创建时间
    
    //会议之声 新闻 NEWS
    public static final String NEWS_NEWSID="newsId";//新闻编号
    public static final String NEWS_NEWSTITLE="newsTitle";//新闻标题
    public static final String NEWS_NEWSSUMMARY="newsSummary";//新闻概要
    public static final String NEWS_NEWSIMAGEURL="newsImageUrl";//新闻图片url
    public static final String NEWS_NEWSSOURCE="newsSource";//新闻图片url
    public static final String NEWS_NEWSDATE="newsDate";//新闻时间
    public static final String NEWS_NEWSSTOREITEM="storeitem";//存储一条列表
    public static final String NEWS_NEWSSTOREDETAIL="storedetail";//存储详细内容
    public static final String NEWS_NEWSCONTENT="newsContent";//详细信息
    
    //会议提醒
    public static final String ALERT_ID="id";//提醒id
    public static final String ALERT_DATE="date";//提醒时间
    public static final String ALERT_REPEATDISTANCE="repeatdistance";//提醒间隔
    public static final String ALERT_REPEATTIMES="repeattimes";//提醒次数
    public static final String ALERT_ENABLE="enable";//提醒可用
    public static final String ALERT_TITLE="title";//提醒内容
    public static final String ALERT_TYPE="type";//提醒类型
    public static final String ALERT_RELATIVEID="relativeid";//提醒关联ID
    public static final String ALERT_START="start";//提醒会议开始时间
    public static final String ALERT_END="end";//提醒会议结束时间
    public static final String ALERT_ROOM="room";//提醒会议地址
    
    //广告 AD
    public static final String AD_ADID="adId";//广告Id
    public static final String AD_CONFERENCESID="conferencesId";//参会议编号
    public static final String AD_ADIMAGE="adImage";//广告图片名称
    public static final String AD_ADLEVEL="adLevel";//广告等级
    public static final String AD_ADLINK="adLink";//广告链接地址
    public static final String AD_IMGURL="imgUrl";//广告下载地址
    public static final String AD_VERSION="version";//广告版本号
    public static final String AD_VIEWLEVEL="viewLevel";//查看等级
    public static final String AD_STOPTIME="stopTime";//广告显示时间

    //身份表
    public static final String ROLE_ID = "roleId";
    public static final String ROLE_NAME = "name";
    public static final String ROLE_ENNAME = "enName";

    //地图MAP
    public static final String MAP_CONFERENCESMAPID="conferencesmapId";//地图编号
    public static final String MAP_CONFERENCESID="conferencesId";//参会议编号
    public static final String MAP_MAPREMARK="mapRemark";//地图楼层 显示名字
    public static final String MAP_MAPURL="mapUrl";//图片名称 去查找资源
    
    //精彩回顾
    public static final String REVIEW_TITLE = "reviewTitle";
    public static final String REVIEW_TIME = "reviewTime";
    public static final String REVIEW_TYPE = "reviewType";
    public static final String REVIEW_URL = "reviewUrl";
    public static final String REVIEW_FIELD = "reviewField";



    
}
