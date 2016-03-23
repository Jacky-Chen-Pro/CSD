package com.android.incongress.cd.conference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by liuheng on 2015/8/19.
 */
public class ScenicXiuPicsViewpagerActivity extends Activity {

    private static final String BUNDLE_PICS = "bundle_pics";
    private static final String BUNDLE_POSITION = "bundle_position";

    private ViewPager mPager;
    private String[] mPics;
    private int mPosition;
    private TextView mTvPageInfo;

    public static final void startViewPagerActivity(Context ctx, String[] pics, int position) {
        Intent intent = new Intent();
        intent.setClass(ctx, ScenicXiuPicsViewpagerActivity.class);
        intent.putExtra(BUNDLE_PICS, pics);
        intent.putExtra(BUNDLE_POSITION, position);
        ctx.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPics = getIntent().getStringArrayExtra(BUNDLE_PICS);
        mPosition = getIntent().getIntExtra(BUNDLE_POSITION, 0);

        setContentView(R.layout.activity_schedule_viewpager);

        mPager = (ViewPager) findViewById(R.id.pager);
        mTvPageInfo = (TextView) findViewById(R.id.tv_page_info);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mPics.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(ScenicXiuPicsViewpagerActivity.this);
                view.enable();
                ImageLoader.getInstance().displayImage(mPics[position], view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        mPager.setCurrentItem(mPosition);
        mTvPageInfo.setText((mPosition+1)+"/" +mPics.length);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTvPageInfo.setText((position+1)+"/" +mPics.length);
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
