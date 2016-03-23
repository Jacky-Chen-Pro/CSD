package com.android.incongress.cd.conference.fragments.me;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.ScanQRActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.uis.CircleImageView;
import com.android.incongress.cd.conference.uis.IconChoosePopupWindow;
import com.android.incongress.cd.conference.uis.MyButton;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jacky on 2016/1/28.
 * 模块：我
 * Jacky Chen
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    public static final int REQUEST_LOGIN = 0x0001;
    private RelativeLayout mMeetingAlertPanel, mDataBasePanel, mContackPanel, mSharePanel, mHelpPanel;
    private Button mBtLogin, mBtLoginout;
    private TextView username, welcomeInfo;
    private LinearLayout mLlPersonInfo;
    private int mNoteCount, mTieZiCount;
    private MyButton mBtNote, mBtTieZi;
    private CircleImageView mCivHeadIcon;

    private static final int HANDLE_TIEZI_COUNT = 0x0001;
    private static final int HANDLE_NOTE_COUNT = 0x0002;
    /** 页面是否处于打开状态 **/
    private boolean mIsOpen = true;


    //头像上传相关
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int ALBUM_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int UPLOAD_IMGURL_SUCCESS = 3;

    private Uri fileUri;
    /** 头像的本地路径  **/
    private String mLocalFilePath = "";
    /** 头像上传后的地址 **/
    private String mUploadFilePath = "";
    private IconChoosePopupWindow mIconChoosePopupWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(mIsOpen == false) {
                return;
            }

            int target = msg.what;
            if (target == HANDLE_TIEZI_COUNT) {
                if (mTieZiCount > 0) {
                    mBtTieZi.setText(getString(R.string.mymeeting_tiezi) + " " + mTieZiCount);
                }else {
                    mBtTieZi.setText(getString(R.string.mymeeting_tiezi));
                }
            }else if(target == HANDLE_NOTE_COUNT) {
                if (mNoteCount > 0) {
                    mBtNote.setText(getString(R.string.mymeeting_note) + " " + mNoteCount);
                }else {
                    mBtNote.setText(getString(R.string.mymeeting_note));
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mycenter_warmning_panel:
                action(new MyMeetingWarmning(), R.string.mymeeting_warning, false, false);
                break;
            case R.id.settings_database_panel:
                SettingsDatabaseFragment database = new SettingsDatabaseFragment();
                action(database, R.string.settings_data_title, false, false, true);
                break;
            case R.id.settings_version_panel:
                SettingsVersion version = new SettingsVersion();
                action(version, R.string.settings_version_title, false, false);
                break;
            case R.id.settings_about_panel:
                SettingsAboutFragment about = new SettingsAboutFragment();
                action(about, R.string.settings_about, false, false);
                break;
            case R.id.settings_contact_panel:
                SettingContactFragment contact = new SettingContactFragment();
                action(contact, R.string.settings_contact, false, false);
                break;
            case R.id.settings_share_panel:
                SettingsShare share = new SettingsShare();
                action(share, R.string.settings_share_title, false, false);
                break;
            case R.id.settings_help_panel:
                SettingsHelper help = new SettingsHelper();
                View view = CommonUtils.initView(this.getActivity(), R.layout.hysqhome_shuoliangju_nav_rightbtn);
                TextView mText = (TextView) view.findViewById(R.id.hysq_jiangliangju_titlebar_send);
                help.setView(mText);
                action(help, R.string.settings_help_title, view, false, false);
                break;
            case R.id.bt_login:
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                intent.putExtra(LoginActivity.LOGIN_TYPE, LoginActivity.TYPE_NORMAL);
                startActivityForResult(intent, REQUEST_LOGIN);
                break;
            case R.id.bt_login_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示信息").setMessage(R.string.login_out_tips).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginOut();
                    }
                }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).show();
                break;
            case R.id.bt_note:
                NoteManageFragment noteManager = new NoteManageFragment();
                TextView textView = (TextView) CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                textView.setText(R.string.mymeeting_manage);
                noteManager.setTitleView(textView);
                action(noteManager, R.string.mymeeting_note, textView, false, false, true);
                break;
            case R.id.bt_tiezi:
                if(isLogin()) {
                    action(HistoryPostFragment.getInstance(),R.string.mymeeting_tiezi,false,false);
                }else {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "" , "", "" , "");
                }
                break;
            case R.id.civ_me:
                if(isLogin()) {
//                    initPopupWindow();
//                    mIconChoosePopupWindow.showAtLocation(mLlPersonInfo, Gravity.BOTTOM, 0, 0);
//                    lightOff();
                }else {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "" , "", "" , "");
                }
            default:
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        initViews(view);
        initEvents();
        return view;
    }

    private void initEvents() {
        mMeetingAlertPanel.setOnClickListener(this);
        mBtLogin.setOnClickListener(this);
        mDataBasePanel.setOnClickListener(this);
        mContackPanel.setOnClickListener(this);
        mSharePanel.setOnClickListener(this);
        mHelpPanel.setOnClickListener(this);
        mBtLoginout.setOnClickListener(this);
        mBtNote.setOnClickListener(this);
        mBtTieZi.setOnClickListener(this);
        mCivHeadIcon.setOnClickListener(this);

        getNoteCount();
    }

    /**
     * 查询需要显示的笔记
     */
    private void getNoteCount() {
        String notesql = "select * from " + ConferenceTables.TABLE_INCONGRESS_Note;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();

        List<List<String>> rawList = ada.fetchListBySql(notesql);
        rawList = ada.fetchListBySql(notesql);
        if (rawList != null && !rawList.isEmpty() && !rawList.get(0).isEmpty()) {
            mNoteCount = rawList.get(0).size();
        }
        ada.close();

        mHandler.sendEmptyMessage(HANDLE_NOTE_COUNT);
    }

    private void initViews(View view) {
        mCivHeadIcon  = (CircleImageView) view.findViewById(R.id.civ_me);
        mMeetingAlertPanel = (RelativeLayout) view.findViewById(R.id.mycenter_warmning_panel);
        mDataBasePanel = (RelativeLayout) view.findViewById(R.id.settings_database_panel);
        mBtLogin = (Button) view.findViewById(R.id.bt_login);
        mContackPanel = (RelativeLayout) view.findViewById(R.id.settings_contact_panel);
        mSharePanel = (RelativeLayout) view.findViewById(R.id.settings_share_panel);
        mHelpPanel = (RelativeLayout) view.findViewById(R.id.settings_help_panel);
        username = (TextView) view.findViewById(R.id.tv_name);
        welcomeInfo = (TextView) view.findViewById(R.id.tv_welcome);
        mLlPersonInfo = (LinearLayout) view.findViewById(R.id.ll_person_info);
        mBtLoginout = (Button) view.findViewById(R.id.bt_login_out);
        mBtNote = (MyButton) view.findViewById(R.id.bt_note);
        mBtTieZi = (MyButton) view.findViewById(R.id.bt_tiezi);

        //判断是否登录
        if (isLogin()) {
            showLoginInfo();
        }
    }

    private boolean isLogin() {
        if (StringUtils.isAllNotEmpty(AppApplication.getSPStringValue(Constants.USER_NAME) + "",
                AppApplication.getSPIntegerValue(Constants.USER_ID) + "",
                AppApplication.getSPIntegerValue(Constants.USER_TYPE) + "")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == getActivity().RESULT_OK) {
                //判断是否登录
                if (isLogin()) {
                    showLoginInfo();
                }
            }
        }else {
            if(mIconChoosePopupWindow != null && mIconChoosePopupWindow.isShowing()) {
                mIconChoosePopupWindow.dismiss();
            }
            if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if(resultCode == getActivity().RESULT_OK) {
                    mLocalFilePath = getRealPathFromURI(fileUri);
                    doUploadFile(AppApplication.userId + "", AppApplication.userType + "", new File(mLocalFilePath));
                }else if(resultCode == getActivity().RESULT_CANCELED) {
                    ToastUtils.showShorToast("操作取消");
                }else {
                    ToastUtils.showShorToast("获取图片失败");
                }
            }

            if(requestCode == ALBUM_IMAGE_ACTIVITY_REQUEST_CODE) {
                if(resultCode == getActivity().RESULT_OK) {
                    Uri fileUri = data.getData();
                    mLocalFilePath = getRealPathFromURI(fileUri);
                    doUploadFile(AppApplication.userId+"",AppApplication.userType+"", new File(mLocalFilePath));
                }else if(resultCode == getActivity().RESULT_CANCELED) {
                    ToastUtils.showShorToast("操作取消");
                }else {
                    ToastUtils.showShorToast("获取图片失败");
                }
            }
        }
    }

    public void setRightView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanQRActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * 显示登录信息
     */
    private void showLoginInfo() {
        mBtLogin.setVisibility(View.GONE);
        mLlPersonInfo.setVisibility(View.VISIBLE);

        username.setText(AppApplication.getSPStringValue(Constants.USER_NAME));
        welcomeInfo.setText(getString(R.string.mymeeting_welcome_sb, AppApplication.getSPStringValue(Constants.USER_NAME)));
        mBtLoginout.setVisibility(View.VISIBLE);
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        AppApplication.clearSPValues();
        mLlPersonInfo.setVisibility(View.GONE);
        mBtLogin.setVisibility(View.VISIBLE);
        mBtLoginout.setVisibility(View.GONE);

        AppApplication.setSPStringValue(Constants.USER_NAME, StringUtils.EMPTY_STR);
        AppApplication.setSPIntegerValue(Constants.USER_ID, -1);
        AppApplication.setSPIntegerValue(Constants.USER_TYPE, Constants.TYPE_USER_VISITOR);

        AppApplication.userType = 0;
        AppApplication.userId = -1;
        AppApplication.username = "";

        queryCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsOpen = true;
        queryCount();
    }

    private void queryCount() {
        //查询我的发帖
        CHYHttpClientUsage.getInstanse().doGetSceneShowByUser(AppApplication.conId + "", "-1", AppApplication.userId + "", AppApplication.userType + "", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    mTieZiCount = response.getInt("sceneShowCount");
                    mHandler.sendEmptyMessage(HANDLE_TIEZI_COUNT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        getNoteCount();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsOpen = false;
    }


    private void initPopupWindow() {
        mIconChoosePopupWindow = new IconChoosePopupWindow(getActivity());
        mIconChoosePopupWindow.setAnimationStyle(R.style.icon_popup_window);
        mIconChoosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.me_choose)), ALBUM_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //将拍照的图片保存到指定的地址，获取的时候就从fileUri中获取，否则因为设定了输出图片的位置，在onActivityResult()方法中，获取到的data是空的
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIconChoosePopupWindow.dismiss();
            }
        });
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.PROJECT_NAME);
        }else {
            mediaStorageDir = new File(getActivity().getCacheDir(), Constants.PROJECT_NAME);
        }

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(Constants.PROJECT_NAME, "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private ProgressDialog mProgressDialog;



    /**
     * 并不建议这么做，因为这个操作需要查询数据库，另外一种就是直接获取Uri中的内容，也就是图片流
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void doUploadFile(String userId, String userType, File uploadFile) {

        try {
            CHYHttpClientUsage.getInstanse().doCreateUserImg(userId, userType, uploadFile,  new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    mProgressDialog = ProgressDialog.show(getActivity(),null,"loading...");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    MyLogger.jLog().i("onSuccess" + response.toString());
                    try {
                        int state = response.getInt("state");
                        if (state == 1) {
                            mUploadFilePath = response.getString("fileUrl");
                            mHandler.sendEmptyMessage(UPLOAD_IMGURL_SUCCESS);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
