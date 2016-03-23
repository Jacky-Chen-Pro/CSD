package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailPageFragment;
import com.android.incongress.cd.conference.uis.ScrollControlViewpager;

import java.util.List;

/**
 * Created by Edwin on 15/02/2015.
 * 会议详情 中viewpager的适配器
 */
public class SessionDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<SessionDetailPageFragment> mFragments;
    public SessionDetailViewPagerAdapter(FragmentManager fm, List<SessionDetailPageFragment> fragments){
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
