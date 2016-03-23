package com.android.incongress.cd.conference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.uis.ClearEditText;
import com.android.incongress.cd.conference.utils.ActivityUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/2/4.
 */
public class RegisterActivity extends BaseActivity {
    private ClearEditText mCetMobile;
    private Button mBtNext;
    private TextView mTvTitle,mTvCountryCode;
    private ImageView mIvBack;
    private String mCountryCode;

    public static final int REQUEST_COUNTRY_CODE = 1;

    public static void startActivity(Context ctx) {
        Intent intent = new Intent();
        intent.setClass(ctx, RegisterActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mCountryCode = "86";
        mCetMobile = (ClearEditText) findViewById(R.id.cet_mobile);
        mBtNext = (Button) findViewById(R.id.bt_next);
        mTvTitle = (TextView) findViewById(R.id.title_text);
        mIvBack = (ImageView)findViewById(R.id.title_back);
        mTvCountryCode = (TextView) findViewById(R.id.tv_country_code);

        mTvTitle.setText(R.string.register_title);

        if(AppApplication.systemLanguage != 1) {
            mTvCountryCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, CountryCodeActivity.class);
                    startActivityForResult(intent, REQUEST_COUNTRY_CODE);
                }
            });
        }

        ActivityUtils.addActivity(this);

        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
    }

    private void initEvents() {
        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mCetMobile.getText().toString().trim();
                if (StringUtils.isEmpty(mobile)) {
                    ToastUtils.showShorToast(getString(R.string.phone_can_not_empty));
                    return;
                }

                if(AppApplication.systemLanguage == 1) {
                    RegisterConfirmActivity.startActivity(RegisterActivity.this, mobile);
                }else {
                    RegisterConfirmActivity.startActivity(RegisterActivity.this, mCountryCode + mobile);
                }

            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
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
