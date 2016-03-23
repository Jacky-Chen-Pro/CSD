package com.android.incongress.cd.conference.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.SplashActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailPageFragment;
import com.android.incongress.cd.conference.services.AdService;
import com.android.incongress.cd.conference.utils.ExampleUtil;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

			if(HomeActivity.isForeground) {
				processCustomMessage(context, bundle);
			}else {
				//得到具体的推送内容id，将其打开
				String urlJson = bundle.getString(JPushInterface.EXTRA_EXTRA);
				//String title = bundle.getString(JPushInterface.EXTRA_TITLE);
				String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);

				JSONObject url = null;
				try {
					url = new JSONObject(urlJson);
					if(!StringUtils.isEmpty(urlJson)) {

						JPushLocalNotification ln = new JPushLocalNotification();
						ln.setContent(content);

						//需要标题，简介，dataId，
						long notificationid = System.currentTimeMillis();
						ln.setNotificationId(notificationid);
						ln.setBuilderId(2);
						String trueUrlJson = url.getString("key").replace("\\\\","");

						String finalUrl = "";
						try{
							JSONObject obj = new JSONObject(trueUrlJson);
							finalUrl = obj.getString("url");

						}catch (Exception e) {
							e.printStackTrace();
						}

						Map<String , Object> map = new HashMap<String, Object>() ;
						map.put("finalUrl", finalUrl) ;
						map.put("title", content);
						map.put("notificationId", notificationid);
						JSONObject json = new JSONObject(map) ;
						ln.setExtras(json.toString()) ;
						JPushInterface.addLocalNotification(AppApplication.getContext(), ln);
					}else {
						//
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			//打开自定义的Activity
			//这里可以根据具体的消息打开需要的Activity
			String info = bundle.getString(JPushInterface.EXTRA_EXTRA).replace("\\","");
			MyLogger.jLog().i("info===" + info);
			String url = "";
			String title = "";
			long notificationId;
			try{
				JSONObject obj = new JSONObject(info);
				url = obj.getString("finalUrl");
				title = obj.getString("title");
				notificationId = obj.getLong("notificationId");

				bundle.putString("url", url);
				bundle.putString("title", title);
				bundle.putLong("notificationId", notificationId);
				Intent i = new Intent(context, HomeActivity.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}catch (Exception e){
				e.printStackTrace();
			}
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to HomeActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (HomeActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(HomeActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(HomeActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(HomeActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {
				}
			}
			context.sendBroadcast(msgIntent);
		}
	}


}
