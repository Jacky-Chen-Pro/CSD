package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.MeetingScheduleListFragmentAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/31.
 */
public class MeetingScheduleListFragment extends BaseFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MeetingScheduleListFragmentAdapter mPageAdapter;

    private List<String> mSessionDaysList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_schedule_list, null, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        getSessionDays();

        mPageAdapter = new MeetingScheduleListFragmentAdapter(getChildFragmentManager(), mSessionDaysList);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.VISIBLE);
    }

    private void getSessionDays() {
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        mSessionDaysList = ada.fetchRawGroupList(ConferenceTables.TABLE_INCONGRESS_SESSION, ConferenceTableField.SESSION_SESSIONDAY, ConferenceTableField.SESSION_SESSIONDAY);
        ada.close();

    }

    public void setRightListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!= null) {
                    ((HomeActivity)getActivity()).performBackClick();
                }
            }
        });
    }
}
