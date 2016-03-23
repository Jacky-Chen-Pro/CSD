package com.android.incongress.cd.conference.services;

import java.util.ArrayList;
import java.util.List;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class AdService extends Service {
    private static final String TAG = "AdSevice";

    boolean isStop = false;

    private List<AdBean> adList;

    //下方图在adList中的映射表
    private List<Integer> bottomList = new ArrayList<Integer>();
    //上方图在adList中的映射表
    private List<Integer> topList = new ArrayList<Integer>();
    //上方广告显示在topList的开始位置
    private int alternateTop = 0;
    //下方广告显示在bottomList的位置
    private int alternateBottom = 0;
    private Runnable mRunnable;
    public Handler mPdHandler = new Handler() {
        public void handleMessage(Message msg) {

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setAdList();
        if (adList == null || adList.size() == 0) {
            return;
        }
        if (isStop) {
            return;
        }

        if (adList != null) {
            for (int i = 0; i < adList.size(); i++) {
                if (adList.get(i).getAdLevel() == 3) {
                    bottomList.add(i);
                }
                if (adList.get(i).getAdLevel() == 2) {
                    topList.add(i);
                }
            }
        }
        int dayTime = 24 * 60 * 60;
        int _8hour = 8 * 60 * 60;

        int current = (int) ((System.currentTimeMillis() / 1000L + _8hour) % dayTime);
        alternateBottom = current / bottomList.size() % bottomList.size();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    return;
                }
                if (adList != null) {
                    if (alternateTop >= topList.size()) {
                        alternateTop = 0;
                    }
                    AppApplication.topNum = topList.get(alternateTop);
                    alternateTop++;
                    if (alternateBottom >= bottomList.size()) {
                        alternateBottom = 0;
                    }
                    AppApplication.bottomNum = bottomList.get(alternateBottom);
                    alternateBottom++;
                    Intent in = new Intent();
                    in.setAction(Constants.ACTION_CHANGE_AD);
                    sendBroadcast(in);
                    AdBean bean = adList.get(AppApplication.bottomNum);
                    if (bean != null) {
                        int stopTime = bean.getStopTime();
                        mPdHandler.postDelayed(mRunnable, stopTime * 1000);
                    } else {
                        mPdHandler.post(mRunnable);
                    }

                } else {
                    setAdList();
                }
            }

        };
        mPdHandler.post(mRunnable);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        isStop = true;
    }

    private void setAdList() {
        DbAdapter db = DbAdapter.getInstance();
        db.open();
        String adsql = "select * from " + ConferenceTables.TABLE_INCONGRESS_AD;
        adList = ConferenceGetData.getAd(db, adsql);
        db.close();
    }

}
