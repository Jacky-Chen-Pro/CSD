package com.android.incongress.cd.conference.fragments.my_schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MyScheduleAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 *
 * 我的日程
 */
public class MyScheduleFragment extends BaseFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MyScheduleAdapter mAdapter;

    private List<String> mSessionDaysList;
    private List<ClassesBean> mClassBeanList;

    public static final String BROADCAST_EDIT = "edit";
    public static final String BROADCAST_OPEN_CLOSE ="mode";

    private boolean mIsEditMode = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_schedule, null);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        mSessionDaysList = ada.fetchRawGroupList(
                ConferenceTables.TABLE_INCONGRESS_SESSION,
                ConferenceTableField.SESSION_SESSIONDAY,
                ConferenceTableField.SESSION_SESSIONDAY);
        ada.close();

        String sqlClasses = "select * from " + ConferenceTables.TABLE_INCONGRESS_CLASS;
        DbAdapter dbClasses = DbAdapter.getInstance();
        dbClasses.open();
        mClassBeanList = ConferenceGetData.getClassesList(dbClasses, sqlClasses);
        dbClasses.close();

        String[] titles = new String[mSessionDaysList.size()];
        for(int i=0; i<mSessionDaysList.size(); i++) {
            titles[i] = mSessionDaysList.get(i);
        }

        mAdapter = new MyScheduleAdapter(getFragmentManager(), titles, titles.length, mClassBeanList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public void setRightView(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsEditMode) {
                    Intent intent = new Intent();
                    intent.setAction(BROADCAST_EDIT);
                    intent.putExtra(BROADCAST_OPEN_CLOSE, true);
                    getActivity().sendBroadcast(intent);
                    ((TextView)view).setText(R.string.mymeeting_finish);
                    mIsEditMode = true;
                }else {
                    Intent intent = new Intent();
                    intent.setAction(BROADCAST_EDIT);
                    intent.putExtra(BROADCAST_OPEN_CLOSE, false);
                    getActivity().sendBroadcast(intent);
                    ((TextView)view).setText(R.string.mymeeting_manage);
                    mIsEditMode = false;
                }
            }
        });
    }
}
