package com.android.incongress.cd.conference.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Jacky on 2015/12/19.
 */
public class CHYHttpClientUsage {
    private static CHYHttpClientUsage mInstance;

    private CHYHttpClientUsage(){}

    public static final CHYHttpClientUsage getInstanse() {
        if(mInstance == null) {
            synchronized (CHYHttpClientUsage.class) {
                if(mInstance == null) {
                    mInstance = new CHYHttpClientUsage();
                }
            }
        }
        return mInstance;
    }

    /**
     * 现场动态接口 下方列表数据
     */
    public void doGetSceneShowDown(String conferencesId, String lastSceneShowId, String userId, String userType,JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getSceneShowDown");
        params.put("conferencesId", conferencesId);
        params.put("lastSceneShowId", lastSceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     *现场动态上方新闻和展商活动
     */
    public void doGetSceneShowTop(String conferencesId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getSceneShowTop");
        params.put("conferencesId", conferencesId);

        CHYHttpClient.post("getSceneShowTop", params, responseHandler);
    }

    /**
     * 发表图片
     */
    public void doCreateSceneShowImg(String conferencesId, String userId, String userType, String sceneShowId, File file, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createSceneShowImg");
        params.put("conferencesId", conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("sceneShowId", sceneShowId);
        try {
            params.put("img" , file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 发表文字
     * @param conferencesId
     * @param userId
     * @param sceneShowId
     * @param content
     * @param responseHandler
     */
    public void doCreateSceneShowTxt(String conferencesId, String userId, String userType, String sceneShowId, String content, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createSceneShowTxt");
        params.put("conferencesId", conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("sceneShowId", sceneShowId);
        params.put("content" , content);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 点赞
     * @param sceneShowId
     * @param userId
     */
    public void doSceneShowLaud(String sceneShowId, String userId, String userType, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "sceneShowLaud");
        params.put("sceneShowId", sceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 点赞
     * @param conferencesId
     * @param speakerId
     */
    public void doGetSceneShowQuestions(String conferencesId, String speakerId, String lastQuestionId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getSceneShowQuestions");
        params.put("conferencesId", conferencesId);
        params.put("speakerId", speakerId);
        params.put("lastQuestionId", lastQuestionId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口getHdSession互动Session
     * @param conferencesId
     * @param lan
     * @param responseHandler
     */
    public void doGetHdSession(String conferencesId, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getHdSession");
        params.put("conferencesId", conferencesId);
        params.put("lan", lan);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 消息站
     * @param conId
     * @param count
     * @param pageIndex
     * @param responseHandler
     */
    public void doGetTokenList(String conId, String count, String pageIndex, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getTokenList");
        params.put("conId", conId);
        params.put("count", count);
        params.put("pageIndex", pageIndex);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 用户注册
     * @param name
     * @param mobile
     * @param fromWhere
     * @param lan
     * @param responseHandler
     */
    public void   doRegUser(String name, String familyName, String givenName,String mobile, String fromWhere, String lan, String sms, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("regUser","");
        params.put("name", name);
        params.put("familyName",familyName);
        params.put("giveName", givenName);
        params.put("mobile", mobile);
        params.put("fromWhere", fromWhere);
        params.put("lan", lan);
        params.put("sms", sms);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 获取验证码 type:1注册 2登录
     * @param mobile
     * @param type
     * @param lan
     */
    public void doGetSmsMobile(String mobile, String type, String lan,JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("getSmsMobile","");
        params.put("mobile", mobile);
        params.put("type", type);
        params.put("lan", lan);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 登陆接口
     *
     * @param id 参会的注册号
     * @param name utf8编码
     * @param mobile
     * @param sms
     * @param lan cn,en
     * @param fromWhere
     * @param conId
     * @param responseHandler
     */
    public void doLoginV3(String id, String name, String familyName, String giveName, String mobile, String sms, String lan, String fromWhere, String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("loginV3","");
        params.put("id", id);
        params.put("name", name);
        params.put("familyName", familyName);
        params.put("giveName", giveName);
        params.put("mobile", mobile);
        params.put("sms", sms);
        params.put("lan", lan);
        params.put("fromWhere", fromWhere);
        params.put("conId", conId);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 接口createSceneShowQuestion提问
     * 向专家提问
     *
     * @param conferencesId
     * @param userId
     * @param userType
     * @param content
     * @param speakerId
     * @param responseHandler
     */
    public void doCreateSceneShowQuestion(String conferencesId, String userId, String userType, String content, String speakerId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "createSceneShowQuestion");
        params.put("conferencesId",conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("content", content);
        params.put("speakerId", speakerId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口sceneShowComment评论
     *
     * @param sceneShowId
     * @param userId
     * @param userType
     * @param content
     * @param parentId
     * @param responseHandler
     */
    public void doSceneShowComment(String sceneShowId,String userId, String userType, String content, String parentId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "sceneShowComment");
        params.put("sceneShowId",sceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("content", content);
        params.put("parentId", parentId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 每次更新完毕数据包后的提示信息
     * @param conId
     * @param responseHandler
     */
    ///Conferences/conferences.do?getUplaodInfo&conId=44
    public void doUpdateInfo(String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("getUplaodInfo","");
        params.put("conId", conId);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * createUserLooked记录查看时间（现场秀，消息站）
     * @param userId
     * @param userType
     * @param userToken
     * @param type
     * @param responseHandler
     */
    public void doCreateUserLooked(String userId, String userType, String userToken, String type, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "createUserLooked");
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("userToken", userToken);
        params.put("type", type);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口getLookCount获取最新数据数
     *
     * @param conferencesId
     * @param userId
     * @param userType
     * @param userToken
     * @param responseHandler
     */
    public void doGetLookCount(String conferencesId, String userId, String userType, String userToken, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getLookCount");
        params.put("conferencesId", conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("userToken", userToken);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口sceneShowAnswer讲者回复提问
     *
     * @param sceneShowId
     * @param speakerId
     * @param content
     * @param responseHandler
     */
    public void doSceneShowAnswer(String sceneShowId, String speakerId, String content,JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "sceneShowAnswer");
        params.put("sceneShowId", sceneShowId);
        params.put("speakerId", speakerId);
        params.put("content", content);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     *接口getSceneShowByUser我的发帖
     *
     * @param conferencesId
     * @param lastSceneShowId
     * @param userId
     * @param userType
     * @param responseHandler
     */
    public void doGetSceneShowByUser(String conferencesId, String lastSceneShowId, String userId, String userType, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getSceneShowByUser");
        params.put("conferencesId", conferencesId);
        params.put("lastSceneShowId", lastSceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);

        CHYHttpClient.post(params, responseHandler);
    }


    /**
     * 发送推送的绑定ID
     * @param conId
     * @param clientType
     * @param token
     * @param responseHandler
     */
    public void doSendToken(String conId, String clientType, String token, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("sendToken","");
        params.put("conId", conId);
        params.put("clientType", clientType);
        params.put("token", token);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 上传用户头像
     * @param userId
     * @param userType
     * @param userImg
     * @param responseHandler
     * @throws FileNotFoundException
     */
    public void doCreateUserImg(String userId, String userType, File userImg, JsonHttpResponseHandler responseHandler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("method", "createUserImg");
        params.put("userImg", userImg);
        params.put("userId", userId);
        params.put("userType", userType);

        CHYHttpClient.post(params, responseHandler);
    }
}
