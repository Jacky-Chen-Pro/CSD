package com.android.incongress.cd.conference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.fragments.OnlyWebViewFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/1/15.
 */
public class WebViewContainerActivity extends FragmentActivity {

    private static final String BUNDLE_URL = "url";
    private static final String BUNDLE_TITLE_NAME = "titleName";

    private String mUrl,mTitleName;
    private TextView mTvTitle;
    private ImageView mIvBack;

    public WebViewContainerActivity(){}

    public static final void startWebViewContainerActivity(Context ctx, String url, String titleName) {
        Intent intent = new Intent();
        intent.setClass(ctx, WebViewContainerActivity.class);
        intent.putExtra(BUNDLE_URL, url);
        intent.putExtra(BUNDLE_TITLE_NAME, titleName);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrl = getIntent().getStringExtra(BUNDLE_URL);
        mTitleName = getIntent().getStringExtra(BUNDLE_TITLE_NAME);

        setContentView(R.layout.fragment_webview_container);
        mTvTitle = (TextView) findViewById(R.id.title_text);
        mIvBack = (ImageView) findViewById(R.id.title_back);

        mTvTitle.setText(mTitleName);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewContainerActivity.this.finish();
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fl_container,  OnlyWebViewFragment.getInstance(mUrl)).commit();
    }
}
