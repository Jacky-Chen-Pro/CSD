package com.android.incongress.cd.conference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.ConferencesAPI;
import com.android.incongress.cd.conference.api.EasyConApiFactory;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.ConferenceBean;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.VersionBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.EasyConDb;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.JsonParser;
import com.android.incongress.cd.conference.utils.FileUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ThreadPool;
import com.android.incongress.cd.conference.utils.ThreadPool.Job;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动屏，进行数据更新和数据库建立
 */
public class SplashActivity extends Activity {
    private boolean APK_INSTALL_RESTART = false;

    private static final int MSG_CHECK = 0x0001;
    private static final int MSG_DOWNLOADING = 0x0002;
    private static final int MSG_FINISH = 0x0003;
    private static final int MSG_NEW_FILE = 0x0004;
    private static final int MSG_DOWNLOADED = 0x0005;
    private static final int MSG_LOGIN = 0x0006;
    private static final int MSG_UPDATE_APK_FOUND = 0x0007;
    private static final int MSG_UPDATE_FOUND = 0x0008;
    private static final int MSG_DOWNLOADING_ZIP = 0x0009;
    protected static final int MSG_ERROR = 0x1002;

    //初始数据库版本
    private static final int DATABASE_VERSION = 28;

    private ProgressBar mLoadingProgress;
    private ProgressBar mPbh;

    private TextView mTv;

    private int downloadPercent = 0;// 下载百分比
    private String path = null;// zip包下载到cache的地址
    private String filespath = null;// zip包解析的地址
    private List<VersionBean> zipList = null;// 数据包下载地址列表

    private SharedPreferences preferences;
    private boolean updateing;

    public static void startSplashActivity(Context ctx) {
        Intent intent = new Intent();
        intent.setClass(ctx, SplashActivity.class);
        ctx.startActivity(intent);
    }


    private Handler handler = new Handler() {
        private int total = 0;

        public void handleMessage(Message msg) {
            if (isFinishing()) {
                return;
            }
            switch (msg.what) {

                case MSG_LOGIN:
                    mTv.setText(R.string.splash_login);
                    break;

                case MSG_CHECK:
                    //检查更新
                    mTv.setText(R.string.splash_checking);
                    mLoadingProgress.setVisibility(View.VISIBLE);

//                Animation operatingAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
//                LinearInterpolator lin = new LinearInterpolator();
//                operatingAnim.setInterpolator(lin);
//                mLoadingProgress.startAnimation(operatingAnim);

                    break;
                case MSG_NEW_FILE:
                    total = msg.arg2;
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                    mTv.setText(getString(R.string.splash_downloading, downloadPercent + "") + "%");
                    mLoadingProgress.setVisibility(View.INVISIBLE);
                    mPbh.setVisibility(View.VISIBLE);
                    break;
                case MSG_DOWNLOADING_ZIP:
                    int curent = msg.arg1;
                    int totalsize = msg.arg2;
                    downloadPercent = Math.round(curent * 100.0f / (total - 1) * 1.0f + 0.5f);
                    if (downloadPercent > 100) {
                        downloadPercent = 100;
                    }
                    mTv.setText(getString(R.string.splash_downloading, downloadPercent + "") + "%");
                    mPbh.setProgress(downloadPercent);
                    mPbh.setMax(100);
                    if (curent < totalsize - 1) {
                        UpdateZip(++curent);
                    } else {
                        handler.sendEmptyMessage(MSG_DOWNLOADED);
                        EasyConDb.createDb(filespath, true);
                        updateing = false;
                        handler.sendEmptyMessage(MSG_FINISH);
                    }
                    break;
                case MSG_DOWNLOADING:
                    if (msg.arg1 != downloadPercent) {
                        downloadPercent = msg.arg1;
                        mTv.setText(getString(R.string.splash_downloading, downloadPercent + "") + "%");
                        mLoadingProgress.setVisibility(View.INVISIBLE);
                        mPbh.setVisibility(View.VISIBLE);
                        mPbh.setProgress(downloadPercent);
                    }
                    break;

                case MSG_DOWNLOADED: {
                    mTv.setText(R.string.splash_downloaded);
                }
                break;

                case MSG_FINISH: {
                    if (updateing) {
                        break;
                    }
                    finishsplash();
                }
                break;

                case MSG_ERROR: {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ThreadPool pool = AppApplication
                                    .getThreadPool();
                            pool.execute(new Job<Void>() {
                                @Override
                                public Void run() {
                                    handler.sendEmptyMessage(MSG_CHECK);
                                    downloadData();
                                    return null;
                                }
                            });
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateing = false;
                            EasyConDb.createDb(filespath, false);
                            handler.sendEmptyMessage(MSG_FINISH);
                        }
                    }).setCancelable(false).setMessage(R.string.incongress_data_update_fail).show();
                }
                break;

                case MSG_UPDATE_APK_FOUND: {
                    String titles[] = AppApplication.conBean.getClientVersion().split(Constants.ENCHINASPLIT);
                    String title = "";
                    if (AppApplication.systemLanguage == 1) {
                        title = titles[0];
                    } else {
                        if (titles.length > 1 && titles[1] != null && !titles[1].equals("")) {
                            title = titles[1];
                        } else {
                            title = titles[0];
                        }
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UpdateApk();
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (zipList != null && zipList.size() > 0) {
                                handler.sendEmptyMessage(MSG_UPDATE_FOUND);
                            } else {
                                updateing = false;
                                handler.sendEmptyMessage(MSG_FINISH);
                            }
                        }
                    }).setCancelable(false).setMessage(title).show();
                }
                break;
                case MSG_UPDATE_FOUND: {
                    if (!updateing) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder.setTitle(R.string.dialog_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UpdateZip(0);
                                updateing = true;
                            }
                        }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.sendEmptyMessage(MSG_FINISH);
                            }
                        }).setCancelable(false).setMessage(R.string.incongress_data_update).show();
                        mPbh.setProgress(0);
                    }
                }
                break;
                default:
                    break;
            }

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (zipList != null && zipList.size() > 0) {
            mTv.setText(R.string.splash_downloading);
            handler.sendEmptyMessage(MSG_UPDATE_FOUND);
        } else if (APK_INSTALL_RESTART) {
            handler.sendEmptyMessage(MSG_FINISH);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        initViews();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        path = AppApplication.instance().getSDPath() + Constants.DOWNLOADDIR;//bug
        filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR;

        AppApplication.getThreadPool().execute(new Job<Void>() {
            @Override
            public Void run() {
                handler.sendEmptyMessage(MSG_CHECK);
                downloadData();
                return null;
            }
        });


    }

    private void initViews() {
        mLoadingProgress = (ProgressBar) findViewById(R.id.splash_pb);
        mPbh = (ProgressBar) findViewById(R.id.splash_pbh);
        mTv = (TextView) findViewById(R.id.splash_text);

        mLoadingProgress.setVisibility(View.INVISIBLE);
    }

    private void finishsplash() {
        AppApplication.getHttpClient().cancelRequests(this, true);

        if (handler != null) {
            handler.removeMessages(MSG_ERROR);
            handler.removeMessages(MSG_UPDATE_APK_FOUND);
            handler.removeMessages(MSG_UPDATE_FOUND);
            handler.removeMessages(MSG_DOWNLOADING);
            handler.removeMessages(MSG_DOWNLOADING_ZIP);
            handler.removeMessages(MSG_NEW_FILE);
            handler.removeMessages(MSG_CHECK);
            handler.removeMessages(MSG_LOGIN);
            handler.removeMessages(MSG_FINISH);
        }

        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private void UpdateApk() {
        try {
            handler.sendMessage(Message.obtain(handler, MSG_NEW_FILE, 1, 1));
            String strUrl = AppApplication.conBean.getUrl().replace("\n", "");
            String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            final File f = new File(path + fileName);
            if (f.exists()) {
                f.delete();
            }

            AsyncHttpClient httpClient = AppApplication.getHttpClient();
            httpClient.get(this, strUrl, new FileAsyncHttpResponseHandler(f) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    handler.sendEmptyMessage(MSG_ERROR);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    APK_INSTALL_RESTART = true;
                    startActivity(intent);
                    handler.sendEmptyMessage(MSG_DOWNLOADED);
                    APK_INSTALL_RESTART = true;
                    downloadPercent = 0;
                    mPbh.setProgress(0);
                }

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    int percent = (int) ((float) bytesWritten / totalSize * 100);
                    handler.sendMessage(Message.obtain(handler, MSG_DOWNLOADING, percent, 0));
                    super.onProgress(bytesWritten, totalSize);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(MSG_ERROR);
        }
    }

    private void UpdateZip(final int index) {
        String strUrl = null;
        final VersionBean response = zipList.get(index);
        handler.sendMessage(Message.obtain(handler, MSG_NEW_FILE, (index + 1), zipList.size()));
        strUrl = Constants.get_NEWSPREFIX() + response.getZipUrl().replace("\n", "");
        String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(path + fileName);
        if (f.exists()) {
            f.delete();
        }
        final String temp = strUrl;

        AsyncHttpClient httpClient = AppApplication.getHttpClient();
        httpClient.get(this, strUrl, new FileAsyncHttpResponseHandler(f) {
            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                if (zipList.size() == 1) {
                    int percent = (int) ((float) bytesWritten / totalSize * 100);
                    handler.sendMessage(Message.obtain(handler, MSG_DOWNLOADING, percent, 0));
                }
                super.onProgress(bytesWritten, totalSize);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                handler.sendEmptyMessage(MSG_ERROR);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                FileInputStream zis;
                try {
                    zis = new FileInputStream(file);
                    FileUtils.unZip(zis, filespath);
                    Editor editor = preferences.edit();
                    editor.putInt(Constants.PREFERENCE_DB_VERSION, response.getVersion());
                    editor.commit();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (zipList.size() > 1) {
                    handler.sendMessage(Message.obtain(handler, MSG_DOWNLOADING_ZIP, index, zipList.size()));
                } else {
                    handler.sendEmptyMessage(MSG_DOWNLOADED);
                    EasyConDb.createDb(filespath, true);
                    Log.d("cc11", "finish cancle");
                    updateing = false;
                    handler.sendEmptyMessage(MSG_FINISH);
                }
            }
        });
    }

    public void downloadData() {
        int dbVersion = preferences.getInt(Constants.PREFERENCE_DB_VERSION, 0);

        if (dbVersion == 0) {
            InputStream zipIn = getResources().openRawResource(R.raw.data1);
            FileUtils.unZip(zipIn, filespath);
            EasyConDb.createDb(filespath, false);
            Editor editor = preferences.edit();
            editor.putInt(Constants.PREFERENCE_DB_VERSION, DATABASE_VERSION);
            editor.commit();
            dbVersion = DATABASE_VERSION;
        }

        File dirfile = new File(filespath);
        if (!dirfile.exists()) {
            InputStream zipIn = getResources().openRawResource(R.raw.data1);
            FileUtils.unZip(zipIn, filespath);
            EasyConDb.createDb(filespath, false);
            Editor editor = preferences.edit();
            editor.putInt(Constants.PREFERENCE_DB_VERSION, DATABASE_VERSION);
            editor.commit();
            dbVersion = DATABASE_VERSION;
        }
        DbAdapter db = DbAdapter.getInstance();
        db.open();
        ConferenceBean conference = ConferenceGetData.getConference(db);

        // 会议编号 用于区分会议 这个很重要 这个算是用来区分各个不同参会易的主要手断了吧。不同的会议编号，一些地方的设置会不同，也是在最初获取这个数据的主要原因。
        AppApplication.conId = conference.getConferencesId();
        db.close();

        if(StringUtils.isAllNotEmpty(AppApplication.getSPStringValue(Constants.USER_NAME), AppApplication.getSPIntegerValue(Constants.USER_ID)+"", AppApplication.getSPIntegerValue(Constants.USER_TYPE)+"")) {
            AppApplication.userId = AppApplication.getSPIntegerValue(Constants.USER_ID);
            AppApplication.userType = AppApplication.getSPIntegerValue(Constants.USER_TYPE);
            AppApplication.username = AppApplication.getSPStringValue(Constants.USER_NAME);
        }

        ConferencesAPI api = EasyConApiFactory.instance().getApi();
        int conId = AppApplication.conId;
        int type = AppApplication.conType;
        String appversion = AppApplication.instance().getVersionName();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sp.getString("incongress_token", null);
        String value = "";
        try {
            value = api.getInitDataV2(conId, dbVersion, type, appversion, token);
        } catch (Exception e) {
            handler.sendEmptyMessage(MSG_ERROR);
        }
        AppApplication.conBean = JsonParser.parseIncongress(value);
        zipList = AppApplication.conBean.getVersionList();
        if (AppApplication.conBean.getClient().equals("1")) {
            handler.sendEmptyMessage(MSG_UPDATE_APK_FOUND);
        } else {
            if (zipList != null && zipList.size() > 0) {
                handler.sendEmptyMessage(MSG_UPDATE_FOUND);
            } else {
                handler.sendEmptyMessage(MSG_FINISH);
            }
        }
    }

    protected void onDestroy() {
        AppApplication.getHttpClient().cancelRequests(this, true);
        if (handler != null) {
            handler.removeMessages(MSG_ERROR);
            handler.removeMessages(MSG_UPDATE_APK_FOUND);
            handler.removeMessages(MSG_UPDATE_FOUND);
            handler.removeMessages(MSG_DOWNLOADING);
            handler.removeMessages(MSG_DOWNLOADING_ZIP);
            handler.removeMessages(MSG_NEW_FILE);
            handler.removeMessages(MSG_CHECK);
            handler.removeMessages(MSG_LOGIN);
            handler.removeMessages(MSG_FINISH);
        }
        super.onDestroy();
    }

    ;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(100);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}