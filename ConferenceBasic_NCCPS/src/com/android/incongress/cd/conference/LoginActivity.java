package com.android.incongress.cd.conference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.UserInfoBean;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Jacky on 2016/1/29.
 * 登录页面
 */
public class LoginActivity extends FragmentActivity {
    private EditText mEtName, mEtMobile, mEtConfirmCode, mEtRegistCode, mEtFamilyName, mEtGivenName;
    private Button mBtLogin;
    private TextView mTvGoRegister, mTvGetConfirm, mTvCountryCode;
    private LinearLayout mLlRegistCode;
    private CheckBox mCbRegist;

    private String mUserName,mUserMobile,mFamilyName,mGivenName;
    private String mCountryCode = "";

    private int mCurrentType = 1;

    private ProgressDialog mProgressDialog;

    //必须输入注册码类型，不能取消勾选框
    public static final String LOGIN_TYPE = "loginType";
    public static final String LOGIN_USERNAME = "loginUserName";
    public static final String LOGIN_USERMOBILE = "loginUserMobile";
    public static final String LOGIN_FAMILY_NAME = "familyName";
    public static final String LOGIN_GIVEN_NAME = "givenName";

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_SECRETARY = 2;
    public static final int TYPE_PROFESSOR = 3;
    public static final int REQUEST_COUNTRY_CODE = 4;

    public static final void startLoginActivity(Context ctx, int type, String userName, String mobilePhone,String familyName, String givenName) {
        Intent intent = new Intent();
        intent.setClass(ctx, LoginActivity.class);
        intent.putExtra(LOGIN_TYPE, type);
        intent.putExtra(LOGIN_USERNAME, userName);
        intent.putExtra(LOGIN_USERMOBILE, mobilePhone);
        intent.putExtra(LOGIN_FAMILY_NAME,familyName);
        intent.putExtra(LOGIN_GIVEN_NAME,givenName);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCurrentType = getIntent().getIntExtra(LOGIN_TYPE, TYPE_NORMAL);
        mUserName = getIntent().getStringExtra(LOGIN_USERNAME);
        mUserMobile = getIntent().getStringExtra(LOGIN_USERMOBILE);
        mFamilyName = getIntent().getStringExtra(LOGIN_FAMILY_NAME);
        mGivenName = getIntent().getStringExtra(LOGIN_GIVEN_NAME);

        //公共部分
        mEtMobile = (EditText) findViewById(R.id.et_mobile);
        mEtConfirmCode = (EditText) findViewById(R.id.et_confirm);
        mEtRegistCode = (EditText) findViewById(R.id.et_regist_code);
        mBtLogin = (Button) findViewById(R.id.bt_login);
        mTvGoRegister = (TextView) findViewById(R.id.tv_go_regist);
        mTvGetConfirm = (TextView) findViewById(R.id.tv_get_confirm);
        mLlRegistCode = (LinearLayout) findViewById(R.id.ll_regist_code);
        mCbRegist = (CheckBox) findViewById(R.id.cb_regist);
        mCountryCode = "86";

        if(AppApplication.systemLanguage == 1) {
            initChinese();
        }else {
            initEnglish();
        }

        //公共部分
        mEtMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtMobile.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtMobile.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });


        mEtConfirmCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtConfirmCode.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtConfirmCode.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        mEtRegistCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtRegistCode.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtRegistCode.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        if (mCurrentType == TYPE_SECRETARY) {
            mCbRegist.setVisibility(View.GONE);
            mTvGoRegister.setVisibility(View.GONE);
        }

        if (mCurrentType == TYPE_PROFESSOR) {
            mCbRegist.setChecked(true);
            mCbRegist.setClickable(false);

            AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
            alpha.setDuration(500);
            mLlRegistCode.startAnimation(alpha);
            mLlRegistCode.setVisibility(View.VISIBLE);
        }

        mCbRegist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
                    alpha.setDuration(500);
                    mLlRegistCode.startAnimation(alpha);
                    mLlRegistCode.setVisibility(View.VISIBLE);
                } else {
                    AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
                    alpha.setDuration(500);
                    mLlRegistCode.startAnimation(alpha);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mLlRegistCode.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        mTvGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startActivity(LoginActivity.this);
            }
        });

        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mEtMobile.getText().toString();
                String confirmCode = mEtConfirmCode.getText().toString();
                String registCode = mEtRegistCode.getText().toString();

                if (AppApplication.systemLanguage == 1) {
                    String name = mEtName.getText().toString();
                    String nameAfterEndoe = "";
                    try {
                        nameAfterEndoe = URLEncoder.encode(name, Constants.ENCODING_UTF8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!StringUtils.isAllNotEmpty(name, mobile, confirmCode)) {
                        ToastUtils.showShorToast(getString(R.string.login_info_empty));
                    } else {
                        doLogin(registCode, nameAfterEndoe, "", "", mobile, confirmCode, Constants.LanguageChinese);
                    }
                } else {
                    String familyName = mEtFamilyName.getText().toString();
                    String givenName = mEtGivenName.getText().toString();
                    if (!StringUtils.isAllNotEmpty(familyName, givenName, mobile, confirmCode)) {
                        ToastUtils.showShorToast(getString(R.string.login_info_empty));
                    } else {
                        doLogin(registCode, "", familyName, givenName, mobile, mCountryCode + confirmCode, Constants.LanguageEnglish);
                    }
                }
            }
        });

        mTvGetConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mEtMobile.getText().toString().trim();
                if (StringUtils.isEmpty(mobile)) {
                    ToastUtils.showShorToast(getString(R.string.phone_can_not_empty));
                } else {
                    if(AppApplication.systemLanguage == 1) {
                        doGetSms(mobile, Constants.LanguageChinese);
                    }else {
                        doGetSms(mobile, Constants.LanguageEnglish);
                    }

                }
            }
        });
    }

    private void initEnglish() {
        mEtFamilyName = (EditText) findViewById(R.id.et_family_name);
        mEtGivenName = (EditText) findViewById(R.id.et_given_name);
        mTvCountryCode = (TextView) findViewById(R.id.tv_country_code);

        mEtFamilyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtFamilyName.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtFamilyName.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        mEtGivenName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtGivenName.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtGivenName.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        mTvCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, CountryCodeActivity.class);
                startActivityForResult(intent, REQUEST_COUNTRY_CODE);
            }
        });

        if(!StringUtils.isEmpty(mFamilyName)) {
            mEtFamilyName.setText(mFamilyName);
        }
        if(!StringUtils.isEmpty(mGivenName)) {
            mEtGivenName.setText(mGivenName);
        }
        if(!StringUtils.isEmpty(mUserMobile)) {
            mEtMobile.setText(mUserMobile);
        }
    }

    private void initChinese() {
        mEtName = (EditText) findViewById(R.id.et_name);

        mEtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtName.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtName.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        if(!StringUtils.isEmpty(mUserName)) {
            mEtName.setText(mUserName);
        }

        if(!StringUtils.isEmpty(mUserMobile)) {
            mEtMobile.setText(mUserMobile);
        }
    }


    private void doLogin(String registCode, String nameAfterEndoe, String familyName, String givenName, String mobile, String confirmCode, String language) {
        CHYHttpClientUsage.getInstanse().doLoginV3(registCode, nameAfterEndoe, familyName , givenName,mobile, confirmCode, language, Constants.PROJECT_NAME, AppApplication.conId + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(LoginActivity.this, null, "loading...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ToastUtils.showShorToast("服务器开小差了，请稍后重试");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                MyLogger.jLog().i(response.toString());
                try {
                    int state = response.getInt("state");

                    if (state == 1) {

                        Gson gson = new Gson();
                        UserInfoBean user = gson.fromJson(response.toString(), UserInfoBean.class);

                        AppApplication.setSPStringValue(Constants.USER_NAME, user.getName());
                        AppApplication.setSPStringValue(Constants.USER_FAMILY_NAME, user.getFamilyName());
                        AppApplication.setSPStringValue(Constants.USER_GIVEN_NAME, user.getGiveName());
                        AppApplication.setSPStringValue(Constants.USER_MOBILE, user.getMobilePhone());
                        AppApplication.setSPIntegerValue(Constants.USER_ID, user.getUserId());
                        AppApplication.setSPIntegerValue(Constants.USER_TYPE, user.getUserType());

                        AppApplication.userType = user.getUserType();
                        AppApplication.username = user.getName();
                        AppApplication.userId = user.getUserId();

                        setResult(RESULT_OK);
                        LoginActivity.this.finish();

                    } else {
                        ToastUtils.showShorToast("登录失败");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doGetSms(String mobile, String lan) {
        CHYHttpClientUsage.getInstanse().doGetSmsMobile(mobile, Constants.ConfirmTypeLogin, lan, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(LoginActivity.this, null, "loading...");
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
                    String code = response.getString("code");

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

    private void showDialog(String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_tips)).setMessage(msg).setPositiveButton(R.string.positive_button, okListener).setCancelable(false).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_COUNTRY_CODE) {
                String code = data.getStringExtra("code");
                mTvCountryCode.setText("(+" + code+")");
                mCountryCode = code;
            }
        }

    }
}
