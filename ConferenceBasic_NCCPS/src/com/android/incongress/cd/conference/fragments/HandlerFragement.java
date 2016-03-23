package com.android.incongress.cd.conference.fragments;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.android.incongress.cd.conference.base.BaseFragment;

public class HandlerFragement extends BaseFragment {
	
    //用于Handler的处理消息
	public final static int MSG_SUCCESS = 0x1000;//正确
	public final static int MSG_ERROR = 0x1002;//出现错误
	private static final int MSG_TIME_OUT = 0x1003;//连接超时
	public final static int MSG_REFESHDONE = 0x2000;//刷新完成
	public final static int MSG_REFESHING = 0x3000;//刷新完成
	private ProgressDialog mPd = null;
	
	public Handler mhandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
            if (msg.what == MSG_TIME_OUT) {/*
                if (mPd != null) {
                    mPd.dismiss();
                    mPd = null;
                }
                Toast mToast=new Toast(getActivity());
                mToast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT);
                mToast.show();
                return;
            */} else {
            }
            if (getActivity()!=null) {
                onReceiveMsg(msg);
            }
            
        }
	};
    protected void onReceiveMsg(Message msg) {
        // override it.用于在继承该Fragement
    }
    protected void showDialog() {
        mPd = ProgressDialog.show(getActivity(), "", "正在获取...", true, false, null);
    }
    protected void dismissDialog() {
        if (mPd != null) {
            mPd.dismiss();
            mPd = null;
        }
    }
}
