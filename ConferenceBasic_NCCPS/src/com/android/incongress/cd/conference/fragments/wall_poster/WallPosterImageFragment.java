package com.android.incongress.cd.conference.fragments.wall_poster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ThreadPool;
import com.android.incongress.cd.conference.utils.ThreadPool.Job;
import com.bm.library.PhotoView;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 壁报详情页面
 */
public class WallPosterImageFragment extends BaseFragment {

    private ImageView mIvDicussion;
    private TextView mTvDiscussion;
    private DZBBBean mBean;
    private boolean IsNetWorkOpen = true;
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_Net_Error = 1;
    private Bitmap mRawBitmap;

    private View mShareView;
    private PopupWindow mPopupWindow;
    private int situation = 2;

    //微信分享
    private IWXAPI api;
    private String shareUrl = "http://m.incongress.cn/Hdh.do?method=getPoster&posterId=";

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int result = msg.what;
            if (result == MSG_SUCCESS) {
                situation = 1;
                mIvDicussion.setImageBitmap(mRawBitmap);
                mImageView.setImageBitmap(mRawBitmap);
            } else {
                situation = 3;
                mIvDicussion.setImageResource(R.drawable.e_poster_loading_fail_pic);
            }
        }
    };

    private RelativeLayout mRlBigPic;
    private PhotoView mImageView;
    private ImageView mIvClose;

    private HomeActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dzbb_image, null);

        View view1 = getActivity().getWindow().peekDecorView();

        if (view1 != null) {
            InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }

        mActivity = (HomeActivity) getActivity();
        mIvDicussion = (ImageView) view.findViewById(R.id.iv_discussion);
        mTvDiscussion = (TextView) view.findViewById(R.id.bt_discussion);

        mTvDiscussion.setText(getString(R.string.dzbb_discussion, mBean.getDisCount()));
        mIvDicussion.setImageResource(R.drawable.e_poster_loading_pic);

        mIvClose = (ImageView) view.findViewById(R.id.iv_close);
        mImageView = (PhotoView) view.findViewById(R.id.pv_big);
        mImageView.enable();

        mRlBigPic = (RelativeLayout) view.findViewById(R.id.rl_big_pic);

        api = AppApplication.instance().getApi();
        mIvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRlBigPic.setVisibility(View.GONE);
                mActivity.setTitleVisiable(View.VISIBLE);
            }
        });
        mTvDiscussion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //参与讨论
                WallPosterTalkFragment comments = new WallPosterTalkFragment();
                comments.setCommunityTopicBean(WallPosterImageFragment.this, mBean);
                action(comments, R.string.dzbb_discussion_no_blacket, false, false);
            }
        });
        mIvDicussion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (situation == 1) {
                    mActivity.setTitleVisiable(View.GONE);
                    mRlBigPic.setVisibility(View.VISIBLE);
                } else {
                    //尝试重新加载图片
                    mIvDicussion.setImageResource(R.drawable.e_poster_loading_pic);
                    loadPic();
                }
            }
        });

        //加载图片
        loadPic();

        return view;
    }

    private void loadPic() {
        ThreadPool pool = AppApplication.getThreadPool();
        pool.execute(new Job<Void>() {
            @Override
            public Void run() {
                try {
                    IsNetWorkOpen = NetWorkUtils.isNetworkConnected(mActivity);
                    if (IsNetWorkOpen) {
                        byte[] data = getImage(mBean.getPosterPicUrl());
                        mRawBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        mHandler.sendEmptyMessage(MSG_SUCCESS);
                    } else {
                        //无网络的情况下
                        mHandler.sendEmptyMessage(MSG_Net_Error);
                        return null;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void setDiscussInfo(DZBBBean bean) {
        mBean = bean;
    }

    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
        httpURLconnection.setRequestMethod("GET");
        httpURLconnection.setReadTimeout(6 * 1000);
        InputStream in = null;
        byte[] b = new byte[1024];
        int len = -1;
        if (httpURLconnection.getResponseCode() == 200) {
            in = httpURLconnection.getInputStream();
            byte[] result = readStream(in);
            in.close();
            return result;

        }
        return null;
    }

    public static byte[] readStream(InputStream in) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        in.close();
        return outputStream.toByteArray();
    }

    /**
     * 更新界面数据
     */
    public void onChange() {
        //修改讨论数量
        mBean.setDisCount(mBean.getDisCount() + 1);
        mTvDiscussion.setText(getString(R.string.dzbb_discussion, mBean.getDisCount()));
    }

    private void share2weixin(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl + mBean.getPosterId();   //分享后的链接
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "[" + getResources().getString(R.string.home_wallpaper) + "]" + mBean.getTitle();
        msg.description = getResources().getString(R.string.home_wallpaper);
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
    }


    /**
     * 设置又上角的分享界面
     *
     * @param shareView
     */
    public void setShareView(View shareView) {
        this.mShareView = shareView;

        //点击后分享~
        mShareView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
    }

    /**
     * 弹出分享对话框 flag 0是好友，1是朋友圈，
     */
    public void showShareDialog() {
        initPopupWindow();
        mPopupWindow.showAtLocation(mIvDicussion, Gravity.CENTER, 0, 0);
    }

    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {

        // 定义DisplayMetrics对象
        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        screenWidth = dm. widthPixels;
//        screenHeight = dm. heightPixels;

        // 获取自定义布局文件window.xml
        View pview = CommonUtils.initView(AppApplication.getContext(), R.layout.share_dialog);
        ImageView offView = (ImageView) pview.findViewById(R.id.dialog_off);
        offView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        pview.findViewById(R.id.share_to_friend2).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), R.string.share_wechat_friend, Toast.LENGTH_SHORT).show();
                        share2weixin(0);
                    }
                });

        pview.findViewById(R.id.share_to_circle2).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), R.string.share_wechat_circle, Toast.LENGTH_SHORT).show();
                        share2weixin(1);
                    }
                });

        // 设置其背景
//      pview.setBackgroundResource(R.drawable.popup_background);
        // 创建popupW indow实例
        mPopupWindow = new PopupWindow(pview, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true); //pview:自定义布局;400、500:宽和高、true:是否获取焦点
        // 设置为false,让popupWindow出现的时候，它所依赖的父亲view可以接受点击事件
        mPopupWindow.setOutsideTouchable(true);
        // 设置为false，让popupWindow出现的时候，它所依赖的父亲view仍然能获取到焦点
        mPopupWindow.setFocusable(true);
        // 该背景如果不设置会出现点击按钮popupWindow先收缩后展开连续动作，但该方法已被弃用，网上也没找到什么好的办法可以解决。
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x55000000));
    }
}
