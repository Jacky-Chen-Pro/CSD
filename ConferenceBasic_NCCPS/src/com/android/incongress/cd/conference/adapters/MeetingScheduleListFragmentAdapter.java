package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListDetailFragment;

import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 */
public class MeetingScheduleListFragmentAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 3;
    private List<String> mTitles;

    public MeetingScheduleListFragmentAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return MeetingScheduleListDetailFragment.getInstance(mTitles.get(position));
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}