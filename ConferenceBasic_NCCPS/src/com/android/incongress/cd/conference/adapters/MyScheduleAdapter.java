package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.fragments.my_schedule.MyScheduleDetailFragment;

import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 */
public class MyScheduleAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[];
    private int NumbOfTabs;
    private List<ClassesBean> mClasses;

    public MyScheduleAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, List<ClassesBean> classes) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mClasses = classes;
    }

    @Override
    public Fragment getItem(int position) {
        return MyScheduleDetailFragment.getInstance(Titles[position].toString(), mClasses);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position].subSequence(5, Titles[position].length());
    }


    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
