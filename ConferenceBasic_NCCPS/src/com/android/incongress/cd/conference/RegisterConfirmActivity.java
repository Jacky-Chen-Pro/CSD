package com.android.incongress.cd.conference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.uis.ClearEditText;
import com.android.incongress.cd.conference.utils.ActivityUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Jacky on 2016/2/4.
 */
public class RegisterConfirmActivity extends BaseActivity {
    private ClearEditText mCetConfirm, mCetName, mCetGivenName, mCetFamilyName;
    private Button mBtRegister;
    private TextView mTvGetConfirm,mTvTitle;
    private ImageView mIvBack;
    private String mMobile;
    public static final String BUNDLE_MOBILE = "mobile";

    public static void startActivity(Context ctx, String mobile) {
        Intent intent = new Intent();
        intent.setClass(ctx, RegisterConfirmActivity.class);
        intent.putExtra(BUNDLE_MOBILE, mobile);
        ctx.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMobile = getIntent().getStringExtra(BUNDLE_MOBILE);
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_confirm);

        initViews();
        initEvents();

        ActivityUtils.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
    }

    private void initViews() {
        mIvBack = (ImageView)findViewById(R.id.title_back);
        mBtRegister = (Button) findViewById(R.id.bt_register);
        mCetConfirm = (ClearEditText) findViewById(R.id.cet_confirm_code);
        mTvGetConfirm = (TextView) findViewById(R.id.tv_get_confirm);
        mTvTitle = (TextView) findViewById(R.id.title_text);

        mTvTitle.setText(R.string.register_title);
        if(AppApplication.systemLanguage == 1) {
            mCetName = (ClearEditText) findViewById(R.id.cet_name);
        }else {
            mCetFamilyName = (ClearEditText) findViewById(R.id.cet_family_name);
            mCetGivenName = (ClearEditText) findViewById(R.id.cet_given_name);
        }
    }

    private void initEvents() {
        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmCode = mCetConfirm.getText().toString().trim();

                if(AppApplication.systemLanguage == 1) {
                    String name = mCetName.getText().toString().trim();
                    try {
                        name = URLEncoder.encode(name, Constants.ENCODING_UTF8);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (!StringUtils.isAllNotEmpty(name, mMobile, confirmCode)) {
                        ToastUtils.showShorToast(getString(R.string.complete_your_info));
                    } else {
                        doRegister(name,"","",mMobile,confirmCode,Constants.LanguageChinese);
                    }
                }else {
                    String familyName = mCetFamilyName.getText().toString();
                    String givenName = mCetGivenName.getText().toString();

                    if (!StringUtils.isAllNotEmpty(familyName, givenName, mMobile, confirmCode)) {
                        ToastUtils.showShorToast(getString(R.string.complete_your_info));
                    } else {
                        doRegister("", familyName, givenName, mMobile, confirmCode, Constants.LanguageEnglish);
                    }
                }
        }});

        mTvGetConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppApplication.systemLanguage == 1) {
                    doGetSms(mMobile,Constants.LanguageChinese);
                }else {
                    doGetSms(mMobile,Constants.LanguageEnglish);
                }
            }
        });

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterConfirmActivity.this.finish();
            }
        });
    }

    private void showDialog(String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_tips)).setMessage(msg).setPositiveButton(R.string.positive_button, okListener).setCancelable(false).show();
    }

    private void doGetSms(String mobile, String lan) {
        CHYHttpClientUsage.getInstanse().doGetSmsMobile(mobile, Constants.ConfirmTypeRegister,lan ,new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(RegisterConfirmActivity.this, null, "loading...");
            }

            /*
             返回内容{"state":1,"code":"156114","msg":""}
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                MyLogger.jLog().i(response.toString());
                try {
                    int state = response.getInt("state");
                    String msg = response.getString("msg");

                    if (state == 0) {
                        showDialog(msg, null);
                    } else {
                        ToastUtils.showShorToast(getString(R.string.success_send_regist_code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast(getString(R.string.server_error));
            }
        });
    }

    private void doRegister(String nameAfterEndoe, String familyName, String givenName, String mobile, String confirmCode, String language) {
            CHYHttpClientUsage.getInstanse().doRegUser(nameAfterEndoe, familyName,givenName, mobile, Constants.PROJECT_NAME,language, confirmCode, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                @Override
                public void onStart() {
                    super.onStart();
                    mProgressDialog = ProgressDialog.show(RegisterConfirmActivity.this, null, "loading...");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    ToastUtils.showShorToast(getString(R.string.server_error));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mProgressDialog.dismiss();
                }

                /*
                  注册成功： {"name":"闄堝己","state":1,"userType":1,"userId":15988,"mobilePhone(":"15000979730","img":""}
                  注册失败： {"state":-1,"msg":"您注册的账号已存在，请直接登录"}
                 */
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    MyLogger.jLog().i(response.toString());
                    try {
                        int state = response.getInt("state");
                        if (state == -1) {
                            String msg = response.getString("msg");
                            showDialog(msg, null);
                        } else {
                            ActivityUtils.finishAll();
                            ToastUtils.showShorToast(getString(R.string.register_success));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
