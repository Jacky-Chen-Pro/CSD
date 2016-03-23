package com.android.incongress.cd.conference;

import android.os.Bundle;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.uis.VerticalLinearLayout;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/2/19.
 *
 * 引导页面
 */
public class GuideAcitivity extends BaseActivity {
    private VerticalLinearLayout mVerticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        if(AppApplication.getSPIntegerValue(Constants.NEED_GUIDE) == Constants.NEED_GUIDE_FALSE) {
            SplashActivity.startSplashActivity(GuideAcitivity.this);
            GuideAcitivity.this.finish();
            return;
        }

        mVerticalLayout = (VerticalLinearLayout) findViewById(R.id.id_main_ly);
        mVerticalLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                if (currentPage == 4) {
                    SplashActivity.startSplashActivity(GuideAcitivity.this);
                    GuideAcitivity.this.finish();

                    AppApplication.setSPIntegerValue(Constants.NEED_GUIDE, Constants.NEED_GUIDE_FALSE);
                }
            }

        });
    }
}
