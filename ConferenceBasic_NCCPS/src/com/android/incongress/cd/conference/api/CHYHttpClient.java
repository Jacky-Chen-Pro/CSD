package com.android.incongress.cd.conference.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Jacky on 2015/12/19.
 */
public class CHYHttpClient {
//    private static final String BASE_URL = "http://192.168.0.50:8080/CMEMain/";
//    private static final String BASE_URL = "http://incongress.cn/Conferences/chyApi.do"; //接口文档的地址
    private static final String BASE_URL = "http://app.incongress.cn:8082/Conferences/chyApi.do";
//    private static final String BASE_URL2= "http://incongress.cn/Conferences/conferences.do";//彪哥的几个接口的地址
    private static final String BASE_URL2 = "http://app.incongress.cn:8082/Conferences/conferences.do";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url),params,responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(BASE_URL, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + "?method=" + relativeUrl;
    }

    public static void post2(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(BASE_URL2, params, responseHandler);
    }
}
