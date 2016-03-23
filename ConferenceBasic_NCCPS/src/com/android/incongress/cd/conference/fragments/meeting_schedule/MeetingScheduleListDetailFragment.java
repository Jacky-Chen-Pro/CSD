package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.MeetingScheduleListAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * Created by Jacky on 2016/2/1.
 */
public class MeetingScheduleListDetailFragment extends BaseFragment {
    private StickyListHeadersListView mStickLVSpeaker;
    private MeetingScheduleListAdapter mScheduleListAdapter;
    private String mCurrentMeetingDay = "2016-03-17";

    private static final String BUNDLE_MEETING_DAY = "meetingDay";


    public static MeetingScheduleListDetailFragment getInstance(String meetingDay) {
        MeetingScheduleListDetailFragment fragment = new MeetingScheduleListDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MEETING_DAY, meetingDay);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCurrentMeetingDay = getArguments().getString(BUNDLE_MEETING_DAY);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, null);
        mStickLVSpeaker = (StickyListHeadersListView) view.findViewById(R.id.slhlv_sessions);

        mStickLVSpeaker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int tartgetPosition = 0;
                for (int i = 0; i < mAllSessionsList.size(); i++) {
                    if (mAllSessionsList.get(i).getSessionGroupId() == mCurrentTimeSessions.get(position).getSessionGroupId()) {
                        tartgetPosition = i;
                    }
                }

                SessionDetailFragment detail = new SessionDetailFragment();
                detail.setArguments(tartgetPosition, mAllSessionsList, mClasses);
                View moreView = CommonUtils.initView(getActivity(), R.layout.titlebar_session_detail_more);
                detail.setRightListener(moreView);
                action(detail, R.string.meeting_schedule_detail_title, moreView, false, false);

            }
        });
        new MyMeetingAsyncTask().execute();

        ((HomeActivity)getActivity()).controlButtonPanelVisibility(false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((HomeActivity)getActivity()).controlButtonPanelVisibility(true);
    }

    class MyMeetingAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mScheduleListAdapter = new MeetingScheduleListAdapter(getActivity(), mCurrentTimeSessions, mClasses);
            mStickLVSpeaker.setAdapter(mScheduleListAdapter);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getCurrentDaySessions(mCurrentMeetingDay);
            getAllSessionList();
            return null;
        }
    }

    //一共有几天
    private List<SessionBean> mCurrentTimeSessions;
    private List<ClassesBean> mClasses;
    private List<String> mSessionDaysList;
    private List<SessionBean> mAllSessionsList;

    /**
     * 获取当前一天的session数据
     */
    private void getCurrentDaySessions(String time) {
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        mSessionDaysList = ada.fetchRawGroupList(ConferenceTables.TABLE_INCONGRESS_SESSION, ConferenceTableField.SESSION_SESSIONDAY, ConferenceTableField.SESSION_SESSIONDAY);
        ada.close();

        mCurrentTimeSessions = new ArrayList<>();
        String sqlSession = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION + " where sessionDay = '" + time + "'" +" order by "+ ConferenceTableField.SESSION_CLASSESID +" ASC, " + ConferenceTableField.SESSION_STARTTIME +" ASC" ;

        DbAdapter dbAdapter = DbAdapter.getInstance();
        dbAdapter.open();
        mCurrentTimeSessions = ConferenceGetData.getSessionList(dbAdapter, sqlSession);
        dbAdapter.close();

        mClasses = new ArrayList<>();
        String sqlClass = "select * from " + ConferenceTables.TABLE_INCONGRESS_CLASS;
        DbAdapter dbAdapter2 = DbAdapter.getInstance();
        dbAdapter2.open();
        mClasses = ConferenceGetData.getClassesList(dbAdapter2, sqlClass);
        dbAdapter2.close();
    }


    private void getAllSessionList() {
        mAllSessionsList = new ArrayList<>();
        for(int i=0; i<mSessionDaysList.size(); i++) {
            String sqlSession = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION + " where sessionDay = '" + mSessionDaysList.get(i) + "'" +" order by "+ ConferenceTableField.SESSION_CLASSESID +" ASC, " + ConferenceTableField.SESSION_STARTTIME +" ASC" ;

            DbAdapter dbAdapter = DbAdapter.getInstance();
            dbAdapter.open();
            List<SessionBean> temp = ConferenceGetData.getSessionList(dbAdapter, sqlSession);
            mAllSessionsList.addAll(temp);//获取会议室
            dbAdapter.close();
        }
    }

}
