package com.android.incongress.cd.conference.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class CommonUtils {

    public static View initView(Context mContext,int id){
        View view = LayoutInflater.from(mContext).inflate(id, null);
        return view;
    }
    
    public static String formatTime(int min,int max){
    	StringBuilder sb = new StringBuilder();
    	if (min<10) {
			sb.append("0");
		}
    	sb.append(min);
    	sb.append(":00-");
    	if (max<10) {
			sb.append("0");
		}
    	sb.append(max);
    	sb.append(":00");
    	return sb.toString();
    }
    public static String getToday(){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//时间格式
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    	String str=formatter.format(curDate);
    	return str;
    }
    public static String getTime(){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    	String str=formatter.format(curDate);
    	return str;
    }
    
    public static String fortmatDate(Date date){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式
    	String str=formatter.format(date);
    	return str;
    }
    
    public static String getTimeFromformat(String format){
    	SimpleDateFormat formatter = new SimpleDateFormat(format);//时间格式
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    	String str=formatter.format(curDate);
    	return str;
    }
    
    public static String formatTime(String time){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	Date datatime;
		try {
			datatime = formatter.parse(time);
			final String dateFormat = AppApplication.getContext().getString(R.string.month_day_year);
	        return DateFormat.format(dateFormat, datatime).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return "";
    }
    public static String formatTimeYueRi(String time){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	Date datatime;
		try {
			datatime = formatter.parse(time);
			final String dateFormat = AppApplication.getContext().getString(R.string.month_day_no_year);
	        return DateFormat.format(dateFormat, datatime).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
//    	String times[]=time.split("-");
//    	StringBuilder sb = new StringBuilder();
//    	if(times.length>2){
//    		int yue=Integer.parseInt(times[1]);
//    		int ri=Integer.parseInt(times[2]);
//    		sb.append(yue+"月");
//    		sb.append(ri+"日");
//    	}
		return "";
    }
	public static String CommunityTimeCompare(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 时间格式
		try {
			Date datetime = formatter.parse(time);
			Date now=new Date();
			long between=(now.getTime()-datetime.getTime())/1000;
			long day=between/(24*3600);
			long hour=between%(24*3600)/3600;
			long minute=between%3600/60;
			Context res = AppApplication.getContext();
			if(day>=1){
				if(time.length()>5){
					String value=time.substring(5);
					return  value;
				}else{
					return time;
				}
			}
/*			if(day>=1&&day<4){
				return res.getString(R.string.days_ago,day);
			}else if(day>0){
				hour=day*24+hour+minute/60;
			}else{*/
				hour=hour+minute/60;
		//	}
			if(hour>1){
				return res.getString(R.string.hours_ago,hour);
			}
			if(minute>1){
				return res.getString(R.string.mins_ago,minute);
			}else{
				return res.getString(R.string.new_ago);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
/*	public synchronized static void filecopy(String oldPath, String newPath){
	    try {   
	    	   System.out.println("----oldpath---:"+oldPath);
	    	   System.out.println("----newPath---:"+newPath);
	    	   File newfile=new File(newPath);
	    	   if(newfile.exists()){
	    		   return;
	    	   }
	           int bytesum = 0;   
	           int byteread = 0;   
	           File oldfile = new File(oldPath);   
	           if (oldfile.exists()) { //文件存在时   
	               InputStream inStream = new FileInputStream(oldPath); //读入原文件   
	               FileOutputStream fs = new FileOutputStream(newPath);   
	               byte[] buffer = new byte[1444];   
	               int length;   
	               while ( (byteread = inStream.read(buffer)) != -1) {   
	                   bytesum += byteread; //字节数 文件大小   
	                   fs.write(buffer, 0, byteread);   
	               }   
	               inStream.close();   
	           }   
	       }   
	       catch (Exception e) {   
	           System.out.println("复制单个文件操作出错");   
	           e.printStackTrace();   
	  
	       }
	}*/
}
