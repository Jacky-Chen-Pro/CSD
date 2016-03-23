package com.android.incongress.cd.conference.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.utils.ActivityUtils;

import java.util.List;

public class BaseActivity extends FragmentActivity {

	protected List<AdBean> mAdList;
    private AdReceiver mAdReceiver;

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        mAdReceiver = new AdReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CHANGE_AD);

        registerReceiver(mAdReceiver, filter);
        onAdChanging();
    }

    protected Handler mPdHandler = new Handler() {
        public void handleMessage(Message msg) {
            onReceiveMsg(msg);
        }
    };

    @Override
	protected void onDestroy() {
		super.onDestroy();
	    unregisterReceiver(mAdReceiver);
	}

    private class AdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.onAdChanging();
        }
    }

    protected void onAdChanging() {}
    
    protected void onReceiveMsg(Message msg) {}
}
