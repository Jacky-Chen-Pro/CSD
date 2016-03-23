package com.android.incongress.cd.conference.base;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.beans.IncongressBean;
import com.android.incongress.cd.conference.beans.ModuleBean;
import com.android.incongress.cd.conference.services.AdService;
import com.android.incongress.cd.conference.utils.CrashHandler;
import com.android.incongress.cd.conference.utils.EmotionsUtils;
import com.android.incongress.cd.conference.utils.ThreadPool;
import com.loopj.android.http.AsyncHttpClient;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class AppApplication extends Application {

    private static AppApplication instance;
    private static Context mContext;

    //conference id 参会议id编号 现为测试数据 最后会从txt中的真实的
    public static int conId = 103;

    //表明类型 在初始化数据时会用
    public static int conType = 2;

    //0表明是游客  1表明是用户，2绑定注册码的用户，, 3是专家
    public static int userType = 0;

    //用户id
    public static int userId = -1;

    //用户真实姓名
    public static String username = "";

    //初始化数据的Bean
    public static IncongressBean conBean = new IncongressBean();

    //各个模块是否显示New或者count
    public static ModuleBean moduleBean = new ModuleBean();

    //1代表 中文  2代表英文
    public static int systemLanguage = 1;

    public static Typeface mTypeface = null;

    private static IWXAPI api;

    /**
     * 设备的唯一token
     **/
    public static String TOKEN_IMEI = "";
    /**
     * 上方广告序号
     */
    public static int topNum = -1;

    /**
     * 下方广告序号
     */
    public static int bottomNum = -1;

    /**
     * 广告列表
     */
    public static List<AdBean> adList = null;


    /**
     * 会议名称
     */
    public static String conferenceName = null;

    private static DisplayMetrics mDisplayMetrics;

    public static AsyncHttpClient mHttpClient;

    public static AppApplication instance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 增加对线程池的管理
     **/
    private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    /**
     * 执行线程操作
     *
     * @param runnable
     */
    public static final void doSingleThread(Runnable runnable) {
        singleThreadExecutor.execute(runnable);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this;
        EmotionsUtils.init(this);
        mHttpClient = new AsyncHttpClient();
        mHttpClient.setMaxConnections(10);
        mHttpClient.setMaxRetriesAndTimeout(3, 20000);

        getSystemLauanguage();
        CrashHandler.getInstance().init(getApplicationContext());

        if (systemLanguage == 2) {
            mTypeface = Typeface.createFromAsset(getAssets(), "Arial.ttf");
        }

        //Jpush初始化操作
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        CustomPushNotificationBuilder builder = new
                CustomPushNotificationBuilder(getApplicationContext(),
                R.layout.item_notitfication,
                R.id.icon,
                R.id.title,
                R.id.text);
        builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.layoutIconDrawable = R.drawable.ic_launcher;
        JPushInterface.setPushNotificationBuilder(2, builder);


        //初始化 UniversalImageLoader
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_load_bg)
                .showImageForEmptyUri(R.drawable.default_load_bg)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .resetViewBeforeLoading(false)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(100))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    public String getImei() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    public static ThreadPool getThreadPool() {
        return ThreadPool.getThreadPool();
    }

    public static AsyncHttpClient getHttpClient() {
        return mHttpClient;
    }


    public boolean NetWorkIsOpen() {
        boolean flag = false;
        //判断网络是否连接
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info != null) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public void setDisPlayMetrics(DisplayMetrics mDisplayMetrics) {
        this.mDisplayMetrics = mDisplayMetrics;
    }

    public DisplayMetrics getDisPlayMetrics() {
        return this.mDisplayMetrics;
    }

    public String getVersionName() {
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    this.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {

        }
        return null;
    }

    public String getSDPath() {
        File file = mContext.getExternalCacheDir();
        return file.getAbsolutePath();
    }

    private void getSystemLauanguage() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            systemLanguage = 1;
        }

        if (language.endsWith("en")) {
            systemLanguage = 2;
        }
    }

    public static String getSystemLanuageCode() {
        if (systemLanguage == 1) {
            return "cn";
        } else {
            return "en";
        }
    }

    public static IWXAPI getApi() {
        return api;
    }

    /**
     * 将微信的分享方法集合进这里做一个简单的封装
     *
     * @param urlStr      点击分享后跳转的链接
     * @param thumb       缩略图
     * @param title       分享标题
     * @param description 分享的描述    这里设置为参会易相应的模块
     * @param transaction 分享的时间    req.transaction = String.valueOf(System.currentTimeMillis());
     * @param secene      分享的形式（朋友/朋友圈）
     * @return 是否成功分享
     */
    public static void ShareToWX(String urlStr, Bitmap thumb, String title, String description, String transaction, int secene) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = urlStr;   //分享后的链接
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = title;
        msg.description = description;

        //一直都分享失败主要是因为这个分享的图标不能超过32KB，想来想去可能就是这里出的问题。
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = secene;//1代表朋友圈，2代表朋友
        boolean isSuccess = api.sendReq(req);//这个返回值仅仅是调用是否成功的返回值

        if (isSuccess) {
        } else {
        }
    }

    public void setApi(IWXAPI api) {
        this.api = api;
    }

    public void stopService() {
        stopService(new Intent(getApplicationContext(), AdService.class));
    }

    /**
     * shezhi sp value
     *
     * @param key
     * @param value
     */
    public static boolean setSPStringValue(String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.edit().putString(key, value).commit();
    }

    public static String getSPStringValue(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(key, "");
    }

    /**
     * shezhi sp value
     *
     * @param key
     * @param value
     */
    public static boolean setSPIntegerValue(String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.edit().putInt(key, value).commit();
    }

    public static int getSPIntegerValue(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getInt(key, 0);
    }

    /**
     * 清空所有sp信息
     */
    public static void clearSPValues() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.edit().clear();
    }
}
