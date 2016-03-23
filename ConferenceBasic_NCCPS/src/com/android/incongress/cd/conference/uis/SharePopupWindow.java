package com.android.incongress.cd.conference.uis;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.tencent.mm.sdk.openapi.SendMessageToWX;


/**
 *
 * 在创建分享窗口的时候，就同时将分享的标题和内容传入构造函数中
 *
 * Created by Jacky on 2015/12/20.
 */
public class SharePopupWindow extends PopupWindow{

    private int mWidth;

    private View mConvertView;

    private TextView mTvWechat,mTvFriendCircle,mTvEmail,mTvSms;
    private String mTitle,mContent;
    private Context mContext;
    private Button mBtCancel;

    public void setContent(String content){
        this.mContent = content;
    }

    public SharePopupWindow(Context context, String title, String content) {
        super(context);
        calWidthAndHeight(context);
        initViews(context);
        initEvents();

        mTitle = title;
        mContent = content;
        mContext = context;

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void initEvents() {
        mTvWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWechat();
            }
        });
        mTvFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToCircleFriend();
            }
        });

        mTvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToEmail();
            }
        });
        mTvSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToSms();
            }
        });
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePopupWindow.this.dismiss();
            }
        });
    }

    private void initViews(Context context) {
        mConvertView = LayoutInflater.from(context).inflate(R.layout.share_bottom_layout,null);

        mTvWechat = (TextView) mConvertView.findViewById(R.id.tv_share_wechat);
        mTvFriendCircle = (TextView) mConvertView.findViewById(R.id.tv_share_friend_circle);
        mTvEmail = (TextView) mConvertView.findViewById(R.id.tv_share_email);
        mTvSms = (TextView) mConvertView.findViewById(R.id.tv_share_sms);
        mBtCancel = (Button) mConvertView.findViewById(R.id.bt_cancel);
    }

    /**
     * 计算popupWindow的高度和宽度
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
    }


    /**
     * 分享到微信
     */
    private void shareToWechat() {
        AppApplication.ShareToWX(Constants.APP_DOWNLOAD_SITE, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher),
                mContent, mContext.getResources().getString(R.string.home_schedule), String.valueOf(System.currentTimeMillis()),
                SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 分享到朋友圈
     */
    private void shareToCircleFriend() {
        AppApplication.ShareToWX(Constants.APP_DOWNLOAD_SITE, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher),
                mContent, mContext.getResources().getString(R.string.home_schedule), String.valueOf(System.currentTimeMillis()),
                SendMessageToWX.Req.WXSceneTimeline);
    }

    /**
     * 分享到Email
     */
    private void shareToEmail() {
        Intent itmail = new Intent(Intent.ACTION_SEND);
        itmail.putExtra(Intent.EXTRA_TEXT, mContent);
        itmail.setType("text/plain");
        mContext.startActivity(itmail);
    }


    /**
     * 分享到短信
     */
    private void shareToSms() {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", mContext.getString(R.string.meeting_share_info,mContent));
        mContext.startActivity(mIntent);
    }
}
