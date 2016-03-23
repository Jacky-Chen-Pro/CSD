package com.android.incongress.cd.conference.base;

/**
 * 说明：参会易中大部分的常量都在这里
 */
public class Constants {

    /** 是否处于调试状态，调试状态都是测试地址；非调试状态都是正是地址 **/
    public static final boolean isDebug = false;

    public static final String CACHE_FILE_NAME = "cit_folder";

    /** 用户登录名 **/
    public static final String USER_NAME = "userName";
    public static final String USER_FAMILY_NAME = "familyName";
    public static final String USER_GIVEN_NAME = "givenName";
    /** 用户手机号 **/
    public static final String USER_MOBILE = "userMobile";
    /** 用户登录id **/
    public static final String USER_ID = "userId";
    /** 用户类型 **/
    public static final String USER_TYPE = "userType";

    /** 项目名称 主要用于log打印 **/
    public static final String PROJECT_NAME = "cit";

    /** 默认的编译格式 **/
    public static final String ENCODING_UTF8 = "utf8";

    /** 默认的分页尺寸 **/
    public static final String PAGE_SIZE = "10";

    /** 客户端类型 1 ios 2 android 3 web **/
    public static final String TYPE_ANDROID = "2";

    /** 游客身份 **/
    public static final int TYPE_USER_VISITOR = 0;
    /** 注册用户，未绑定注册码 **/
    public static final int TYPE_USER_REGISTER_NOT_BIND_CODE = 1;
    /** 注册用户，已绑定注册码 **/
    public static final int TYPE_USER_REGISTER_BIND_CODE = 2;
    /** 无敌权限的专家 **/
    public static final int TYPE_USER_FACUTY = 3;

    /** 语言类型 **/
    public static final String LanguageChinese = "cn";
    public static final String LanguageEnglish = "en";

    /** 注册码类型 1注册 2登录**/
    public static final String ConfirmTypeRegister = "1";
    public static final String ConfirmTypeLogin = "2";

    /** 1现场秀 2消息站**/
    public static final String LookTimeScenicXiu = "1";
    public static final String LookTimeMsgStation = "2";

    /** 是否需要引导页 **/
    public static final String NEED_GUIDE = "need_guide";
    public static final int NEED_GUIDE_TRUE = 1;
    public static final int NEED_GUIDE_FALSE = 2;

    /**
     * GBK编码
     */
    public static final String ENCODING_GBK = "gbk";

    /** 检测数据库版本 **/
    public static final String PREFERENCE_DB_VERSION = "db_version";

    /** sharepreference中使用 用于获取用户编号) **/
    public static final String PREFERENCE_USER_ID="db_user_id";
    /** sharepreference中使用 用于获取用户姓名 **/
    public static final String PREFERENCE_USER_NAME="db_user_name";
    /** sharepreference中使用 用于获取用户类别(0表明是用户，1表明是游客) **/
    public static final String PREFERENCE_USER_TYPE="db_user_type";


    /** 会议日程 **/
    public static final String PREFERENCE_MODULE_HYRC_VISIBLE_NEW="db_module_visible_hyrc";
    /** 讲者 **/
    public static final String PREFERENCE_MODULE_JZ_VISIBLE_NEW="db_module_visible_jz";
    /** 参会指南 **/
    public static final String PREFERENCE_MODULE_CHZN_VISIBLE_NEW="db_module_visible_chzn";
    /** 参展商 **/
    public static final String PREFERENCE_MODULE_CZS_VISIBLE_NEW="db_module_visible_czs";
    /** 展商活动 **/
    public static final String PREFERENCE_MODULE_ZSHD_VISIBLE_NEW="db_module_visible_zshd";
    /** 广告广播 **/
    public static final String ACTION_CHANGE_AD = "android.intent.action.change_ad.basic.nccps";

    /** 下载地址 **/
    public static final String DOWNLOADDIR="/cd_incongress/download/";
    /** 解压地址 **/
    public static final String FILESDIR="/cd_incongress/files/";

    /** 数据库名字 **/
    public static final String DB_NAME = "easycondb.db";

    public static final String GUIDE_INTERACTIVE = "guide_interactive";
    public static final String GUIDE_XIU = "guide_xiu";
    public static final String GUIDE_SESSION = "guide_session";

    /**  会议日程  发言还是主持 新增至15种类型 **/
    public static final int HYRC_SPEAKER_FAYAN = 1;
    public static final int HYRC_SPEAKER_ZHUCHI = 2;
    public static final int HYRC_SPEAKER_FANYI = 3;
    public static final int HYRC_SPEAKER_TAOLUN = 4;
    public static final int HYRC_SPEAKER_SHUZHE = 5;
    public static final int HYRC_SPEAKER_BAOGAO = 6;
    public static final int HYRC_SPEAKER_SPEAK = 7;
    public static final int HYRC_SPEAKER_JIESHUO = 8;
    public static final int HYRC_SPEAKER_PINGLUN = 9;
    public static final int HYRC_SPEAKER_ZHUXI = 10;
    public static final int HYRC_SPEAKER_COZHUXI = 11;
    public static final int HYRC_SPEAKER_JUDGE = 12;
    public static final int HYRC_SPEAKER_BANJIANG = 13;
    public static final int HYRC_SPEAKER_LINGJIANG = 14;
    public static final int HYRC_SPEAKER_LEADDISCUSSANT = 15;
    public static final int HYRC_SPEAKER_OTHER = 16;

    /** 会议日程中 Session(会议)和Metting(演讲)的关注 0-->不关注; 1-->关注 **/
    public static final int SMNOATTENTION = 0;
    public static final int SMATTENTION = 1;

    /** 一切的关注与不去关注 0-->不关注; 1-->关注 **/
    public static final int NOATTENTION=0;
    public static final int ATTENTION=1;

    /**　微分享信key　老版 参会易 wxb9e3f3d62eb1d652 **/
    public static final String WXKey = "wxb9e3f3d62eb1d652" ;

    /**　微博享信key和ID　**/
    public static final String WBKey="328b15858b66f44b1345ef327c2f5722";
    public static final long WBID=801479388;

    /**　中英文内容的特殊分割符　**/
    public static final String ENCHINASPLIT="#@#";

    /** 参会易下载地址(根据产品不同地址不同) **/
    public static final String APP_DOWNLOAD_SITE = "http://m.incongress.cn/csd/";

    /**
     * 说明：
     * 1.www开头测试 app和m开头正式
     * 2.app是没有缓存的,m有缓存
     * 3.只有正式的要端口，测试不需要
     * 4.TEST结尾为测试地址，OFFICIAL结尾为正是地址
     */
    public static final String IMAGEPREFIX_TEST="http://www.incongress.cn";
    public static final String HOST_TEST = "http://www.incongress.cn/Conferences/proxy.do";
    public static final String NEWSPREFIX_TEST="http://www.incongress.cn/Conferences";
    //加userId,userType 没有传-1
    public static final String CIT_WEBSITE_TEST = "http://incongress.cn/webapp/discussion/CIT2016H5/liveList.html?conId=" + AppApplication.conId;
    public static final String E_CASE_WEBSITE_TEST = "http://incongress.cn/webapp/discussion/CIT2016H5/casesList.html?conId=" + AppApplication.conId;
    public static final String GREATREVIEW_WEBSITE_TEST = "http://incongress.cn/webapp/discussion/CIT2016H5/reviewList.html?conId=" + AppApplication.conId;//测试 有缓存
    public static final String DZBB_TEST = "http://www.incongress.cn/posterAction.do";
    public static final String FORGET_PASSWORD_TEST = "http://www.incongress.cn/Conferences/files/register/findUser.html?conId=" + AppApplication.conId;
    public static final String MEETING_SCHEDULE_ACTIVITY_TEST = "http://www.incongress.cn/webapp/discussion/CIT2015H5/huDongList.jsp";
    public static final String SAHRE_DZBB_IMAGE_TEST = "http://incongress.cn/Hdh.do?method=getPoster&posterId=";
    public static final String Register_SITE_TEST = "http://www.incongress.cn/Conferences/files/register/userRegister.html?conId=" + AppApplication.conId;

    /** ============================================================================================================= **/
    public static final String IMAGEPREFIX_OFFICIAL="http://app.incongress.cn:8082"; /** 文件和图片下载地址 **/
    public static final String HOST_OFFICIAL = "http://app.incongress.cn:8082/Conferences/proxy.do";/** 服务器地址 **/
    public static final String NEWSPREFIX_OFFICIAL ="http://app.incongress.cn:8082/Conferences"; /** 会议之声的下载地址 **/
    //加userId,userType 没有传-1
    public static final String CIT_WEBSITE_OFFICIAL = "http://m.incongress.cn/webapp/discussion/CIT2016H5/liveList.html?conId=" + AppApplication.conId; /** CIT 加载地址 **/
    public static final String E_CASE_WEBSITE_OFFICIAL = "http://m.incongress.cn/webapp/discussion/CIT2016H5/casesList.html?conId=" + AppApplication.conId; /** 电子病例 加载地址  **/
    public static final String GREATREVIEW_WEBSITE_OFFICIAL = "http://app.incongress.cn/webapp/discussion/CIT2016H5/reviewList.html?conId=" + AppApplication.conId; /** 精彩回顾 加载地址 **/
    public static final String DZBB_OFFICIAL = "http://m.incongress.cn/posterAction.do";/** 电子壁报**/
    public static final String FORGET_PASSWORD_OFFICIAL = "http://app.incongress.cn:8082/Conferences/files/register/findUser.html?conId=" + AppApplication.conId;  /** 忘记密码地址 **/
    public static final String MEETING_SCHEDULE_ACTIVITY_OFFICIAL = "http://app.incongress.cn/webapp/discussion/CIT2015H5/huDongList.jsp";
    public static final String SAHRE_DZBB_IMAGE_OFFICIAL = "http://m.incongress.cn/Hdh.do?method=getPoster&posterId=";
    public static final String Register_SITE_OFFICIAL = "http://app.incongress.cn:8082/Conferences/files/register/userRegister.html?conId=" + AppApplication.conId;
    public static final String HdSession_QUESTION_LIST_OFFICIAL = "http://incongress.cn/webapp/discussion/CIT2016H5/qaList.html?conId="+AppApplication.conId;
    public static final String HD_QUESTION_SITE = "http://incongress.cn/webapp/discussion/CIT2016H5/question.html?type=&userInfoId=&sId=&conId=&lan="; //提问
    public static final String HD_SERVER_SITE = "http://incongress.cn/webapp/discussion/CIT2016H5/toupiao.jsp?type=&userInfoId=&lan="; //调研

    public static final String get_IMAGEPREFIX() {
        if(isDebug)
            return IMAGEPREFIX_TEST;
        else
            return IMAGEPREFIX_OFFICIAL;
    }

    public static final String get_HOST() {
        if(isDebug)
            return HOST_TEST;
        else
            return HOST_OFFICIAL;
    }

    public static final String get_NEWSPREFIX() {
        if(isDebug)
            return NEWSPREFIX_TEST;
        else
            return NEWSPREFIX_OFFICIAL;
    }

    public static final String get_CIT_WEBSITE() {
        if(isDebug)
            return CIT_WEBSITE_TEST;
        else
            return CIT_WEBSITE_OFFICIAL;
    }

    public static final String get_E_CASE_WEBSITE() {
        if(isDebug)
            return E_CASE_WEBSITE_TEST;
        else
            return E_CASE_WEBSITE_OFFICIAL;
    }

    public static final String get_GREATREVIEW_WEBSITE() {
        if(isDebug)
            return GREATREVIEW_WEBSITE_TEST;
        else
            return GREATREVIEW_WEBSITE_OFFICIAL;
    }

    public static final String get_DZBB() {
        if(isDebug)
            return DZBB_TEST;
        else
            return DZBB_OFFICIAL;
    }

    public static final String get_FORGET_PASSWORD() {
        if(isDebug)
            return FORGET_PASSWORD_TEST;
        else
            return FORGET_PASSWORD_OFFICIAL;
    }

    public static final String get_MEETING_SCHEDULE_ACTIVITY() {
        if(isDebug)
            return MEETING_SCHEDULE_ACTIVITY_TEST;
        else
            return MEETING_SCHEDULE_ACTIVITY_OFFICIAL;
    }

    public static final String get_SAHRE_DZBB_IMAGE() {
        if(isDebug)
            return SAHRE_DZBB_IMAGE_TEST;
        else
            return SAHRE_DZBB_IMAGE_OFFICIAL;
    }

    public static final String get_RESITER() {
        if(isDebug)
            return Register_SITE_TEST;
        else
            return Register_SITE_OFFICIAL;
    }
}
