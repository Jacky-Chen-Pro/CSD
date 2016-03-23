package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.incongress.cd.conference.adapters.MeetingScheduleAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 会议日程模块
 *
 * @author Jacky_Chen
 * @time 2014年11月25日
 */
public class MeetingScheduleFragment extends BaseFragment {

    private MeetingScheduleAdapter adapter;
    private TabLayout mTabLayout;
    private ViewPager mViewpager;
    private CharSequence Titles[];
    private int Numboftabs;

    private List<String> mSessionDaysList;
    private List<ClassesBean> mRoomList = new ArrayList<>();
    private ProgressDialog mDialog;

    public MeetingScheduleFragment getInstance() {
        MeetingScheduleFragment meetingSchedule = new MeetingScheduleFragment();
        return meetingSchedule;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_meeting_schedule, null, false);

        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mViewpager = (ViewPager) view.findViewById(R.id.pager);
        mViewpager.setOffscreenPageLimit(3);

        new MyAsyncTask().execute();
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        /**
         * 这里的Integer参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
         * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
         * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
         */
        @Override
        protected String doInBackground(Integer... params) {
            DbAdapter ada = DbAdapter.getInstance();
            ada.open();
            mSessionDaysList = ada.fetchRawGroupList(
                    ConferenceTables.TABLE_INCONGRESS_SESSION,
                    ConferenceTableField.SESSION_SESSIONDAY,
                    ConferenceTableField.SESSION_SESSIONDAY);
            ada.close();
            Titles = new CharSequence[mSessionDaysList.size()];
            for (int i = 0; i < mSessionDaysList.size(); i++) {
                Titles[i] = mSessionDaysList.get(i);
            }

            Numboftabs = mSessionDaysList.size();

            String sqlclass = "select * from " + ConferenceTables.TABLE_INCONGRESS_CLASS;
            DbAdapter dbAdapter = DbAdapter.getInstance();
            dbAdapter.open();
            mRoomList.addAll(ConferenceGetData.getClassesList(dbAdapter, sqlclass));//获取会议室
            dbAdapter.close();

            return "";
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();

            adapter = new MeetingScheduleAdapter(getChildFragmentManager(), Titles, Numboftabs, mRoomList, mSessionDaysList);
            mViewpager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(mViewpager);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), null, "loading");
        }
    }

    public void setRightListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingScheduleListFragment listFragment = new MeetingScheduleListFragment();
                ImageView view = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
                if(AppApplication.systemLanguage == 1) {
                    view.setImageResource(R.drawable.schedule_switch_cn);
                }else {
                    view.setImageResource(R.drawable.schedule_switch);
                }
                listFragment.setRightListener(view);
                action(listFragment, R.string.home_schedule, view, false, false);
            }
        });
    }
}
