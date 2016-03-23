package com.android.incongress.cd.conference;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.beans.ConferenceBean;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.TitleEntry;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.BaseFragment.MainCallBack;
import com.android.incongress.cd.conference.fragments.HomeFragment;
import com.android.incongress.cd.conference.services.AdService;
import com.android.incongress.cd.conference.utils.AsyncImageLoader;
import com.android.incongress.cd.conference.utils.AsyncImageLoader.ImageLoadCallback;
import com.android.incongress.cd.conference.utils.ExampleUtil;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 主页容器，主要用来布置上方广告，下方广告，以及中间区域，从splash页面启动
 * <p/>
 * 以及相应的广告页面
 */
public class HomeActivity extends BaseActivity implements OnClickListener, MainCallBack {

    private ImageView mBackButton, mAdTop, mAdBottom, mAdHome;
    private Button mHomeButton;
    private TextView mTitleView, mTvSkip; //标题,跳过
    private LinearLayout mCoustomView, mBackButtonPanel, mHomeButtonPanel;//自定义view，返回按钮，小房子按钮
    private RelativeLayout mTitleContainer;//整个titleBar，就是上方操作栏

    /**
     * Jpush 推送数据，判断是否在主页
     **/
    public static boolean isForeground = false;

    private static final int MSG_DISMISS_AD = 0X0001;
    private long timeOut = 0;
    private static boolean stopTimer = false;

    private Stack<TitleEntry> mTitleEntries = new Stack<TitleEntry>();
    private SharedPreferences preferences;
    private IWXAPI api;

    private BaseFragment mPreFragment;
    private AsyncImageLoader mAdAsyncImageLoader;

    public void performBackClick() {
        mBackButton.performClick();
    }

    public void controlButtonPanelVisibility(boolean isVisible) {
        if(isVisible)
            mBackButtonPanel.setVisibility(View.VISIBLE);
        else
            mBackButtonPanel.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        initData();
        initViews();
        initEvents();
        initJpush();

        if (mAdList != null && mAdList.size() > 0) {
            for (AdBean bean : mAdList) {
                if (bean.getAdLevel() == 1) {
                    timeOut = Integer.valueOf(bean.getStopTime()) * 200;
                    String filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR + bean.getAdImage();
                    File f = new File(filespath);
                    if (f.exists()) {
                        Uri uri = Uri.fromFile(f);
                        mAdHome.setImageURI(uri);
                    } else {
                        hideAdAndSkip();
                    }
                    break;
                }
            }
        }

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if (!stopTimer) {
                    mPdHandler.sendEmptyMessage(MSG_DISMISS_AD);
                }
            }
        };

        mPdHandler.postDelayed(mRunnable, timeOut);
        mAdAsyncImageLoader = new AsyncImageLoader();

        startService(new Intent(getApplicationContext(), AdService.class));

        addFragment(new HomeFragment(), true);
        setTitleEntry(false, false, false, null, R.string.app_name, true, true, false);

        //判断当前是否已经登录
        if (StringUtils.isAllNotEmpty(AppApplication.getSPStringValue(Constants.USER_NAME) + "",
                AppApplication.getSPIntegerValue(Constants.USER_ID) + "",
                AppApplication.getSPIntegerValue(Constants.USER_TYPE) + "")) {
            AppApplication.userId =  AppApplication.getSPIntegerValue(Constants.USER_ID);
            AppApplication.username = AppApplication.getSPStringValue(Constants.USER_NAME);
            AppApplication.userType = AppApplication.getSPIntegerValue(Constants.USER_TYPE);
        }

        try {
            Bundle bundle = getIntent().getExtras();

            if(bundle != null) {
                String url = bundle.getString("url");
                String title = bundle.getString("title");
                long notificationId = bundle.getLong("notificationId");
                if(!StringUtils.isEmpty(url))
                    WebViewContainerActivity.startWebViewContainerActivity(HomeActivity.this, url, title);

                JPushInterface.removeLocalNotification(this,notificationId);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将JPush的registerId发送给服务端，方便服务端进行推送
     */
    private void initJpush() {
        final String registrationID = JPushInterface.getRegistrationID(this);
        CHYHttpClientUsage.getInstanse().doSendToken(AppApplication.conId+"", Constants.TYPE_ANDROID, registrationID, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void initEvents() {
        mBackButton.setOnClickListener(this);
        mHomeButton.setOnClickListener(this);
        mBackButtonPanel.setOnClickListener(this);
        mHomeButtonPanel.setOnClickListener(this);
        mAdTop.setOnClickListener(this);
        mAdBottom.setOnClickListener(this);
        mAdHome.setOnClickListener(this);
        mTvSkip.setOnClickListener(this);
    }

    private void initViews() {
        mBackButton = (ImageView) findViewById(R.id.title_back);
        mBackButtonPanel = (LinearLayout) findViewById(R.id.title_back_panel);

        mHomeButton = (Button) findViewById(R.id.title_home);
        mHomeButtonPanel = (LinearLayout) findViewById(R.id.title_home_panel);

        mTitleView = (TextView) findViewById(R.id.title_text);
        mCoustomView = (LinearLayout) findViewById(R.id.title_costom);
        mTitleContainer = (RelativeLayout) findViewById(R.id.title_container);

        mAdTop = (ImageView) findViewById(R.id.ad_top);
        mAdBottom = (ImageView) findViewById(R.id.ad_bottom);
        mAdHome = (ImageView) findViewById(R.id.ad_home);
        mTvSkip = (TextView) findViewById(R.id.tv_skip);
    }

    public void addFragment(BaseFragment fragment, boolean save) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment.setCallBack(this);
        transaction.replace(R.id.maincontainer, fragment);
        if (save) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    public void addFragment(BaseFragment oldfragment, BaseFragment newfragment, boolean save) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        newfragment.setCallBack(this);
        transaction.remove(oldfragment);
        transaction.add(R.id.maincontainer, newfragment);
        transaction.show(newfragment);
        transaction.addToBackStack(newfragment.getClass().getSimpleName());
        transaction.commit();
        mPreFragment = oldfragment;
    }

    public void addFragment(BaseFragment oldfragment, BaseFragment newfragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        newfragment.setCallBack(this);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.hide(oldfragment);
        transaction.add(R.id.maincontainer, newfragment);
        transaction.show(newfragment);
        transaction.addToBackStack(newfragment.getClass().getSimpleName());
        transaction.commit();
        mPreFragment = oldfragment;
    }

    /***
     * 设置Title Bar
     *
     * @param title
     */
    public void setTitleBar(TitleEntry title) {
        //返回按钮是否显示
        if (title.isShowBack()) {
            mBackButton.setVisibility(View.VISIBLE);
            mBackButton.setClickable(true);
            mBackButtonPanel.setVisibility(View.VISIBLE);
            mBackButtonPanel.setClickable(true);
        } else {
            mBackButton.setVisibility(View.INVISIBLE);
            mBackButton.setClickable(false);
            mBackButtonPanel.setVisibility(View.INVISIBLE);
            mBackButtonPanel.setClickable(false);
        }

        //标题是否显示
        if (title.isShowHome()) {
            mHomeButton.setVisibility(View.VISIBLE);
            mHomeButtonPanel.setVisibility(View.VISIBLE);
            mHomeButtonPanel.setClickable(true);
            mHomeButton.setClickable(true);
        } else {
            mHomeButton.setVisibility(View.INVISIBLE);
            mHomeButtonPanel.setVisibility(View.INVISIBLE);
            mHomeButtonPanel.setClickable(false);
            mHomeButton.setClickable(false);
        }

        //自定义view是否显示
        if (title.isShowCoustomView()) {
            mHomeButton.setVisibility(View.INVISIBLE);
            mCoustomView.removeAllViews();
            mCoustomView.setVisibility(View.VISIBLE);
            if (title.getCoustomView() != null)
                mCoustomView.addView(title.getCoustomView());
        } else {
            mCoustomView.removeAllViews();
            mCoustomView.setVisibility(View.INVISIBLE);
            if (mTitleEntries.size() >= 3) {
                mHomeButtonPanel.setVisibility(View.VISIBLE);
                mHomeButton.setVisibility(View.VISIBLE);
                mHomeButtonPanel.setClickable(true);
                mHomeButton.setClickable(true);
            }
        }

        if (title.getTitle() == 0) {
            mTitleView.setText(title.getTitleString());
        } else {
            mTitleView.setText(title.getTitle());
        }

        if (title.isShowtitlebar()) {
            mTitleContainer.setVisibility(View.VISIBLE);
        } else {
            mTitleContainer.setVisibility(View.GONE);
        }
        setAdVisible(title.isAdTop(), title.isAdBottom());
    }

    /**
     * @param showBack        返回键是否显示
     * @param showHome        小房子是否显示
     * @param showCoustomView 自定义右上方是否显示
     * @param coustomView     自定义右上方UI
     * @param title           标题
     * @param adTop           上方广告
     * @param adBottom        下方广告
     * @param showtitlebar    是否显示上方操作栏
     */
    public void setTitleEntry(boolean showBack, boolean showHome,
                              boolean showCoustomView, View coustomView, int title,
                              boolean adTop, boolean adBottom, boolean showtitlebar) {
        TitleEntry titleEntry = new TitleEntry();
        if (coustomView != null)
            titleEntry.setCoustomView(coustomView);
        titleEntry.setShowBack(showBack);
        titleEntry.setShowHome(showHome);
        titleEntry.setShowCoustomView(showCoustomView);
        titleEntry.setTitle(title);
        titleEntry.setAdTop(adTop);
        titleEntry.setAdBottom(adBottom);
        titleEntry.setShowtitlebar(showtitlebar);
        mTitleEntries.push(titleEntry);
    }

    public void setTitleEntryWithoutSave(boolean showBack, boolean showHome, boolean showCoustomView, View coustomView, int title) {
        TitleEntry titleEntry = new TitleEntry();
        titleEntry.setCoustomView(coustomView);
        titleEntry.setShowBack(showBack);
        titleEntry.setShowHome(showHome);
        titleEntry.setShowCoustomView(showCoustomView);
        titleEntry.setTitle(title);
        setTitleBar(titleEntry);
    }

    public void setTitleEntry(boolean showBack, boolean showHome,
                              boolean showCoustomView, View coustomView, String title,
                              boolean adTop, boolean adBottom, boolean showtitlebar) {
        TitleEntry titleEntry = new TitleEntry();
        titleEntry.setCoustomView(coustomView);
        titleEntry.setShowBack(showBack);
        titleEntry.setShowHome(showHome);
        titleEntry.setShowCoustomView(showCoustomView);
        titleEntry.setTitleString(title);
        titleEntry.setAdTop(adTop);
        titleEntry.setAdBottom(adBottom);
        titleEntry.setShowtitlebar(showtitlebar);
        mTitleEntries.push(titleEntry);
        setTitleBar(titleEntry);
    }

    public void hideShurufa() {
        View v = getCurrentFocus();
        if (v != null) {
            if (((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS)) {
                return;
            }
        }
    }

    public boolean toggleShurufa() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        return manager.isActive();
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getSupportFragmentManager();
        switch (v.getId()) {
            //返回控制
            case R.id.title_back:
            case R.id.title_back_panel:
                hideShurufa();
                manager.popBackStackImmediate();
                mTitleEntries.pop();
                setTitleBar(mTitleEntries.peek());
                mPreFragment.setUserVisibleHint(false);
                break;
            //上方广告控制
            case R.id.ad_top:
                int top = AppApplication.topNum;
                if (top >= 0) {
                    AdBean bean = AppApplication.adList.get(top);
                    String link = bean.getAdLink().trim();
                    if (link != null && !link.equals("")) {
                        WebViewContainerActivity.startWebViewContainerActivity(this, link, "");
                    }
                }
                break;
            //下方广告
            case R.id.ad_bottom:
                int bottom = AppApplication.bottomNum;
                if (bottom >= 0) {
                    AdBean bean = AppApplication.adList.get(bottom);
                    String link = bean.getAdLink().trim();
                    if (link != null && !link.equals("")) {
                        WebViewContainerActivity.startWebViewContainerActivity(this, link, link);
                    }
                }
                break;
            //首页图标
            case R.id.ad_home:
                hideAdAndSkip();
                stopTimer = true;
                WebViewContainerActivity.startWebViewContainerActivity(HomeActivity.this, getString(R.string.home_ad_url, AppApplication.conId), "");
                break;
            case R.id.tv_skip:
                hideAdAndSkip();
                stopTimer = true;
                break;
        }
    }

    /**
     * 隐藏广告和跳过
     */
    private void hideAdAndSkip() {
        mAdHome.setVisibility(View.GONE);
        mTvSkip.setVisibility(View.GONE);
    }

    /**
     * 弹出退出框
     */
    private void showexitdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_tips)).setMessage(R.string.conference_exit_app).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService(new Intent(getApplicationContext(), AdService.class));
                System.exit(100);
            }
        }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            manager.executePendingTransactions();

            if (manager.getBackStackEntryCount() == 1) {
                showexitdialog();
            } else {
                hideShurufa();
                manager.popBackStackImmediate();
                mTitleEntries.pop();
                setTitleBar(mTitleEntries.peek());
                if (mPreFragment != null) {
                    mPreFragment.setUserVisibleHint(true);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void perfromBackPressTitleEntry() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        mTitleEntries.pop();
        setTitleBar(mTitleEntries.peek());
        if (mPreFragment != null) {
            mPreFragment.setUserVisibleHint(true);
        }
    }

    @Override
    protected void onAdChanging() {
        super.onAdChanging();

        if (AppApplication.topNum < 0 || AppApplication.bottomNum < 0) {
            return;
        }

        int top = AppApplication.topNum;
        int bottom = AppApplication.bottomNum;

        String filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR;
        String pathTop = "";
        if (mAdList.size() > top) {
            pathTop = filespath + mAdList.get(top).getAdImage();
        }
        String pathBottom = "";
        if (mAdList.size() > bottom) {
            pathBottom = filespath + mAdList.get(bottom).getAdImage();
        }

        File fileTop = new File(pathTop);
        File fileBottom = new File(pathBottom);
        if (fileTop.exists()) {
            setAdImageView(fileTop.getAbsolutePath(), mAdTop);
        }
        if (fileBottom.exists()) {
            setAdImageView(fileBottom.getAbsolutePath(), mAdBottom);
        }
    }

    private void setAdImageView(String filepath, ImageView imageview) {
        Bitmap cachedImage = mAdAsyncImageLoader.getCachBitmap(filepath);
        if (cachedImage == null) {
            mAdAsyncImageLoader.loadBitmap(imageview, filepath, new ImageLoadCallback() {
                @Override
                public void imageLoaded(ImageView imageView, Bitmap imageBitmap, String imageUrl) {
                    if (imageBitmap != null) {
                        imageView.setImageBitmap(imageBitmap);
                    } else {
                        Toast.makeText(getApplicationContext(), "广告显示失效，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } else {
            imageview.setImageBitmap(cachedImage);
        }
    }

    // 设置广告是否可见 top为上方广告 bottom为下方广告
    public void setAdVisible(boolean top, boolean bottom) {
        if (top == true) {
            mAdTop.setVisibility(View.VISIBLE);
        } else {
            mAdTop.setVisibility(View.GONE);
        }
        if (bottom == true) {
            mAdBottom.setVisibility(View.VISIBLE);
        } else {
            mAdBottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), AdService.class));
        android.os.Debug.stopMethodTracing();
    }

    @Override
    public void onCreateViewFinish() {
        setTitleBar(mTitleEntries.peek());
    }

    public void setTitleVisiable(int visiable) {
        mTitleContainer.setVisibility(visiable);
    }

    // 初始化数据
    private void initData() {
        DbAdapter db = DbAdapter.getInstance();
        db.open();
        String adsql = "select * from " + ConferenceTables.TABLE_INCONGRESS_AD;
        AppApplication.adList = ConferenceGetData.getAd(db, adsql);
        ConferenceBean conference = ConferenceGetData.getConference(db);
        AppApplication.conId = conference.getConferencesId();
        AppApplication.userId = preferences.getInt(Constants.PREFERENCE_USER_ID, 0);
        AppApplication.username = preferences.getString(Constants.PREFERENCE_USER_NAME, "");
        AppApplication.userType = preferences.getInt(Constants.PREFERENCE_USER_TYPE, 0);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        AppApplication.instance().setDisPlayMetrics(dm);
        mAdList = AppApplication.adList;

        AppApplication.TOKEN_IMEI = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();

        if (AppApplication.instance().NetWorkIsOpen()) {
            init();
            registerMessageReceiver();
        }

        api = WXAPIFactory.createWXAPI(this, Constants.WXKey);
        api.registerApp(Constants.WXKey);

        ((AppApplication) getApplication()).setApi(api);
    }

    @Override
    protected void onReceiveMsg(Message msg) {
        super.onReceiveMsg(msg);
        switch (msg.what) {
            case MSG_DISMISS_AD:
                hideAdAndSkip();
                break;
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();


    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }


    //广播接收器的相关东西
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.android.incongress.cd.conference.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();

        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    /**
     * 注册JPush广播的接收器
     */
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                String messge = intent.getStringExtra(KEY_MESSAGE);
//                String extras = intent.getStringExtra(KEY_EXTRAS);
//                StringBuilder showMsg = new StringBuilder();
//                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                if (!ExampleUtil.isEmpty(extras)) {
//                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                }

                String content = intent.getStringExtra(KEY_MESSAGE);
                String urlJson = intent.getStringExtra(KEY_EXTRAS);
                JSONObject url = null;
                try {
                    url = new JSONObject(urlJson);
                    String trueUrlJson = url.getString("key").replace("\\\\", "");
                    if(StringUtils.isEmpty(trueUrlJson)) {
                        showPushInfo(HomeActivity.this,content,"");
                    }else {
                        JSONObject obj = new JSONObject(trueUrlJson);
                        String finalUrl = obj.getString("url");
                        showPushInfo(HomeActivity.this,content, finalUrl);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 弹出退出框
     */
    private void showPushInfo(final Context context, final String message, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.push_tips)).setMessage(message).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!StringUtils.isEmpty(url))
                    WebViewContainerActivity.startWebViewContainerActivity(context, url, message);
            }
        }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false).show();

//        AlertDialog dialog = builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
    }
}
