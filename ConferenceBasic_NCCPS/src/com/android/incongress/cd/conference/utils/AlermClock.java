package com.android.incongress.cd.conference.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;

public class AlermClock {

	public static final String INTENT_ALERT="com.android.incongress.cd.conference.alarm_start";
    private final static String DM12 = "E h:mm aa";
    private final static String DM24 = "E k:mm";
    public static final String KEY_BEFORE = "before";
    public static final String KEY_TIMES = "times";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_ENABLE = "enable";
    
    final static String M24 = "kk:mm";
	
	
	public static void addClock(AlertBean alertBean){
		if (alertBean == null) {
			return;
		}
		calculateNextAlert(alertBean);
	}
	
	public static void disableClock(AlertBean alertBean){
		if (alertBean == null) {
			return;
		}
		disableAlert(AppApplication.getContext(),alertBean);
		DbAdapter db = DbAdapter.getInstance();
		db.open();
		ConferenceSetData.disableAlert(db, alertBean);
		db.close();
	}
	
	public static void deleteClock(AlertBean alertBean){
		DbAdapter db = DbAdapter.getInstance();
		db.open();
		ConferenceSetData.disableAlert(db, alertBean);
		db.close();
	}

	public static AlertBean calculateNextAlert(final Context context) {
		AlertBean alarm = null;
		long now = System.currentTimeMillis();
		String sql = "select * from "+ ConferenceTables.TABLE_INCONGRESS_ALERT;
		DbAdapter db = DbAdapter.getInstance();
		db.open();
		List<AlertBean> lists = ConferenceGetData.getAlert(db, sql);
		db.close();
		for (int i = 0; i < lists.size(); i++) {
			alarm = lists.get(i);
			alarm.setTime(calculateAlarm(alarm).getTimeInMillis());
	        Calendar c = Calendar.getInstance();
	        c.setTimeInMillis(alarm.getTime());
	        //String timeString = CommonUtils.fortmatDate(c.getTime());
	        //Toast.makeText(context, timeString, Toast.LENGTH_LONG).show();
			if (alarm.getTime() < now) {
				deleteClock(alarm);
			}
			enableAlert(context,alarm,alarm.getTime());
		}
		return alarm;
	}
	
	public static void diasbleExpiredClock(){
		long now = System.currentTimeMillis();
		String sql = "select * from "+ ConferenceTables.TABLE_INCONGRESS_ALERT;
		DbAdapter db = DbAdapter.getInstance();
		db.open();
		List<AlertBean> lists = ConferenceGetData.getAlert(db, sql);
		db.close();
		for(int i = 0;i<lists.size();i++){
			AlertBean bean = lists.get(i);
			if (now > calculateAlarm(bean).getTimeInMillis()) {
				deleteClock(bean);
			}
		}
	}
	
	public static void diasbleClock(){
		String sql = "select * from "+ ConferenceTables.TABLE_INCONGRESS_ALERT;
		DbAdapter db = DbAdapter.getInstance();
		db.open();
		List<AlertBean> lists = ConferenceGetData.getAlert(db, sql);
		db.close();
		for(int i = 0;i<lists.size();i++){
			AlertBean bean = lists.get(i);
			disableAlert(AppApplication.getContext(), bean);
		}
	}
	
	public static void calculateNextAlert(AlertBean alertBean) {
		if (alertBean == null) {
			return;
		}
		
		long now = System.currentTimeMillis();
		AlertBean a = alertBean;
		a.setTime(calculateAlarm(a).getTimeInMillis());
	     Calendar c = Calendar.getInstance();
	        c.setTimeInMillis(a.getTime());
	        String timeString = CommonUtils.fortmatDate(c.getTime());
	        //Toast.makeText(AppApplication.getContext(), timeString, Toast.LENGTH_LONG).show();
	        Log.d("cccc", "calculateNextAlert "+timeString);
		if (a.getTime() < now) {
			System.out.println("-----delete delete delete -----");
			deleteClock(a);
			return;
		}
		enableAlert(AppApplication.getContext(),a,a.getTime());
	}
	
	public static void calculateSnoothAlert(AlertBean alertBean){
		if (alertBean == null) {
			return;
		}
		AlertBean a = alertBean;
		a.setTime(calculateSnoothAlarm(a).getTimeInMillis());
		   Calendar c = Calendar.getInstance();
	        c.setTimeInMillis(a.getTime());
	        String timeString = CommonUtils.fortmatDate(c.getTime());
	        Log.d("cccc", "snooth alert is "+timeString+ "repeatetimes "+alertBean.getRepeattimes());
		enableAlert(AppApplication.getContext(),a,a.getTime());
	}
	 
	static Calendar calculateSnoothAlarm(AlertBean bean) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        SharedPreferences spPreferences = PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
        int before = spPreferences.getInt(AlermClock.KEY_DISTANCE, 5);
        c.add(Calendar.MINUTE, before);
        return c;
    }
	
	 static Calendar calculateAlarm(AlertBean bean) {

	        // start with now
	        Calendar c = Calendar.getInstance();
	        c.setTimeInMillis(System.currentTimeMillis());
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");// 时间格式
			try {
				Date monthAndDate = formatter.parse(bean.getDate());
		        String[] hourAndMinute =bean.getStart().split(":");
	//	        int nowHour = c.get(Calendar.HOUR_OF_DAY);
	//	        int nowMinute = c.get(Calendar.MINUTE);
		        int hour = Integer.parseInt(hourAndMinute[0]);
		        int minute = Integer.parseInt(hourAndMinute[1]);
		        // if alarm is behind current time, advance one day
	//	        if (hour < nowHour  ||
	//	            hour == nowHour && minute <= nowMinute) {
	//	            c.add(Calendar.DAY_OF_YEAR, 1);
	//	        }
		        c.set(Calendar.HOUR_OF_DAY, hour);
		        c.set(Calendar.MINUTE, minute);
		        c.set(Calendar.SECOND, 0); 
		        c.set(Calendar.MILLISECOND, 0);
		        c.set(Calendar.MONTH, monthAndDate.getMonth());
		        c.set(Calendar.DAY_OF_MONTH, monthAndDate.getDate());
		        SharedPreferences spPreferences = PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
		        int before = spPreferences.getInt(AlermClock.KEY_BEFORE, 5);
		        c.add(Calendar.MINUTE, -before);
		        return c;
			}catch(ParseException e){
				
			}
			return null;
	    }
	 
	
	  private static void enableAlert(Context context, final AlertBean alarm,
	            final long atTimeInMillis) {
		  	boolean enable = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_ENABLE, true);
		  	if (!enable) {
		  		return;
			}
	        AlarmManager am = (AlarmManager)
	                context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(INTENT_ALERT);
	        intent.putExtra("object", alarm);
	        PendingIntent sender = PendingIntent.getBroadcast(
	                AppApplication.getContext(), Integer.parseInt(alarm.getId()), intent, PendingIntent.FLAG_CANCEL_CURRENT);
	        am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
	        Calendar c = Calendar.getInstance();
	        c.setTimeInMillis(atTimeInMillis);
	        String timeString = CommonUtils.fortmatDate(c.getTime());
	        Log.d("cccc", "enableAlert is "+ timeString);
	    }

	    /**
	     * Disables alert in AlarmManger and StatusBar.
	     *
	     * @param id Alarm ID.
	     */
	    static void disableAlert(Context context,AlertBean mbean) {
	        AlarmManager am = (AlarmManager)
	                context.getSystemService(Context.ALARM_SERVICE);
	        PendingIntent sender = PendingIntent.getBroadcast(
	        		AppApplication.getContext(), Integer.parseInt(mbean.getId()), new Intent(INTENT_ALERT),
	                PendingIntent.FLAG_CANCEL_CURRENT);
	        am.cancel(sender);
	    }
	    
	    private static String formatDayAndTime(final Context context, Calendar c) {
	        String format = get24HourMode(context) ? DM24 : DM12;
	        return (c == null) ? "" : (String)DateFormat.format(format, c);
	    }
	    static boolean get24HourMode(final Context context) {
	        return android.text.format.DateFormat.is24HourFormat(context);
	    }
	
}
