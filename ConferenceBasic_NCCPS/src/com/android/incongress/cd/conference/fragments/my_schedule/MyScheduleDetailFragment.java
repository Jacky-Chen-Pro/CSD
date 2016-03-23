package com.android.incongress.cd.conference.fragments.my_schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MyScheduleDetailAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 * 我的日程中的子fragment
 */
public class MyScheduleDetailFragment extends BaseFragment {
    private static final String BUNDLE_TIME = "time";
    private static final String BUNDLE_CLASS = "class";

    private String mTime;
    private ListView mLvMySchedule;
    private TextView mTvTips;
    private MyScheduleDetailAdapter mAdapter;

    private List<SessionBean> mSessionBeans = new ArrayList<>();
    private List<MeetingBean> mMeetingBeans = new ArrayList<>();
    private List<ClassesBean> mClassBeans;

    private List<SessionBean> mShowSessionList = new ArrayList<>();

    private List<SessionBean> mAllSessionList = new ArrayList<>();
    private MyReceiver mReceiver;

    public MyScheduleDetailFragment() {
    }

    public static final MyScheduleDetailFragment getInstance(String time, List<ClassesBean> classes) {
        MyScheduleDetailFragment fragment = new MyScheduleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, time);
        bundle.putSerializable(BUNDLE_CLASS, (Serializable) classes);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTime = getArguments().getString(BUNDLE_TIME);
            mClassBeans = (List<ClassesBean>) getArguments().getSerializable(BUNDLE_CLASS);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, new IntentFilter(MyScheduleFragment.BROADCAST_EDIT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_schedule_item, null);

        mLvMySchedule = (ListView) view.findViewById(R.id.lv_schedule);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);
        mReceiver = new MyReceiver();

        mShowSessionList.clear();
        mMeetingBeans.clear();
        new MyScheduleTask().execute();
        return view;
    }

    class MyScheduleTask extends AsyncTask<Void,Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter = new MyScheduleDetailAdapter(getActivity(),mShowSessionList, mMeetingBeans, mClassBeans);
            mLvMySchedule.setAdapter(mAdapter);

            mLvMySchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    SessionBean session = mShowSessionList.get(position);

                    SessionDetailFragment detail = new SessionDetailFragment();
                    detail.setArguments(getSessionPosition(session.getSessionGroupId()), mAllSessionList, mClassBeans);
                    action(detail, R.string.meeting_schedule_detail_title, false, false, false);
                }
            });

            if(mShowSessionList.size() == 0) {
                mTvTips.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String sqlSession = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION + " where sessionDay = '" + mTime + "' and attention = 1" ;
            DbAdapter dbAdapter = DbAdapter.getInstance();
            dbAdapter.open();
            mSessionBeans = ConferenceGetData.getSessionList(dbAdapter, sqlSession);

            String sqlMeeting = "select * from " + ConferenceTables.TABLE_INCONGRESS_MEETING + " where meetingDay = '" + mTime + "' and attention = 1" ;
            mMeetingBeans = ConferenceGetData.getMeetingList(dbAdapter, sqlMeeting);

            mShowSessionList.addAll(mSessionBeans);

            List<Integer> sessionGroupIdsFromMeetings = new ArrayList<>();

            for(int i=0; i<mMeetingBeans.size(); i++) {
                MeetingBean meeting = mMeetingBeans.get(i);

                boolean isMeetingInSessionList = false;
                for(int j=0; j<mSessionBeans.size(); j++) {
                    if(meeting.getSessionGroupId() == mSessionBeans.get(j).getSessionGroupId()) {
                        isMeetingInSessionList = true;
                        break;
                    }
                }

                if(!isMeetingInSessionList) {
                     if(!sessionGroupIdsFromMeetings.contains(meeting.getSessionGroupId())) {
                         sessionGroupIdsFromMeetings.add(meeting.getSessionGroupId());
                     }
                }
            }

            for(int i=0; i<sessionGroupIdsFromMeetings.size(); i++) {
                    String tempSql = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION + " where sessionGroupId = " +  sessionGroupIdsFromMeetings.get(i);
                    mShowSessionList.add(ConferenceGetData.getSessionList(dbAdapter, tempSql).get(0));
            }

            String sqlAllSession = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION;
            mAllSessionList.addAll(ConferenceGetData.getSessionList(dbAdapter, sqlAllSession));
            dbAdapter.close();

            return null;
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isOpen = intent.getBooleanExtra(MyScheduleFragment.BROADCAST_OPEN_CLOSE,false);
            if(isOpen) {
                mAdapter.setAlarmShow(true);
            }else {
                mAdapter.setAlarmShow(false);
            }
        }
    }

    /**
     * 获取Meeting在session中的位置
     * @param sessionGroupId
     * @return
     */
    private int getSessionPosition(int sessionGroupId) {
        for (int i = 0; i < mAllSessionList.size(); i++) {
            SessionBean bean = mAllSessionList.get(i);
            if (bean.getSessionGroupId() == sessionGroupId) {
                return i;
            }
        }
        return 0;
    }
}
