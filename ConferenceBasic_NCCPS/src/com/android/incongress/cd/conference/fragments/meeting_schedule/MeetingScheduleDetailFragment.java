package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.uis.HScroll;
import com.android.incongress.cd.conference.uis.HVScrollView;
import com.android.incongress.cd.conference.uis.VScroll;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jacky on 2015/12/15.
 * <p/>
 * 主题色：00a63b  0,166,59 --> rgba
 * session色块：   80%的主题色 0,166,59,.8   --> rgba
 * session详情页： 20%的主题色 0,166,59,.2   --> rgba
 * <p/>
 * 当前时间分隔线： EC6941 没有透明度
 */
public class MeetingScheduleDetailFragment extends BaseFragment implements View.OnTouchListener {
    private TableLayout mTableLayout;
    private LinearLayout mLlLeftContainer, mLlTopContainer;
    private VScroll mLeftScrollView;
    private HVScrollView mHvScrollView;
    private HScroll mTopScrollView;
    private AbsoluteLayout mAbsContainer;
    private Context mContext;

    private ProgressDialog mDialog;

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    private float mx, my;
    private String mTime;//当前页面的时间

    public static final String BUNDLE_TIME = "time";
    public static final String BUNDLE_ROOM = "room";
    public static final String BUNDLE_SESSION_DAY = "sessionDay";

    //所有房间的list集合
    private List<ClassesBean> mRoomsList;
    //所有session的list集合
    private List<SessionBean> mAllSessionsList;

    private List<String> mSessionDaysList;
    private Map<String, List<SessionBean>> mSessionsByDay;

    public MeetingScheduleDetailFragment() {
    }

    public static MeetingScheduleDetailFragment getSingleScheduleFragmetn(String time, List<ClassesBean> classes, ArrayList<String> sessionDayList) {
        MeetingScheduleDetailFragment meetingScheduleDetailFragment = new MeetingScheduleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, time);
        bundle.putSerializable(BUNDLE_ROOM, (Serializable) classes);
        bundle.putStringArrayList(BUNDLE_SESSION_DAY, sessionDayList);
        meetingScheduleDetailFragment.setArguments(bundle);
        return meetingScheduleDetailFragment;
    }

    private String[] mTimes = {"08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45",
            "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
            "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45",
            "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00"};

    private long firstSessionTouchTime = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTime = getArguments().getString(BUNDLE_TIME);
            mRoomsList = (List<ClassesBean>) getArguments().getSerializable(BUNDLE_ROOM);
            mSessionDaysList = getArguments().getStringArrayList(BUNDLE_SESSION_DAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View returnView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_single_schedule, null, false);

        mTableLayout = (TableLayout) returnView.findViewById(R.id.table_layout);
        mLlLeftContainer = (LinearLayout) returnView.findViewById(R.id.ll_left_container);
        mLeftScrollView = (VScroll) returnView.findViewById(R.id.left_scrollview);
        mHvScrollView = (HVScrollView) returnView.findViewById(R.id.hv_scrollview);
        mTopScrollView = (HScroll) returnView.findViewById(R.id.top_scrollview);
        mLlTopContainer = (LinearLayout) returnView.findViewById(R.id.ll_top_container);
        mAbsContainer = (AbsoluteLayout) returnView.findViewById(R.id.abs_container);
        mContext = getActivity();

        mLeftScrollView.setOnTouchListener(this);
        mTopScrollView.setOnTouchListener(this);
        mHvScrollView.setOnTouchListener(this);
        mHvScrollView.setSmoothScrollingEnabled(true);
        mHvScrollView.setFlingEnabled(true);

        new MyAsyncTask().execute();
        return returnView;
    }

    private void setTablesLayout() {
        final TableRow.LayoutParams tableParams = new TableRow.LayoutParams(DensityUtil.dip2px(mContext, (float) (100 * mRoomsList.size())), DensityUtil.dip2px(mContext, 30f));

        for (int row = 0; row < mTimes.length; row++) {
            final int row_temp = row;
            if (row % 4 == 0) {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider, null);

                final TableRow tableRow = new TableRow(mContext);
                final View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_table, null);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tableRow.addView(view1, tableParams);
                        if (row_temp != 0) {
                            mTableLayout.addView(view);
                        }
                        mTableLayout.addView(tableRow);
                    }
                });
            } else {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider_gap, null);

                final TableRow tableRow = new TableRow(mContext);
                final View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_table, null);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tableRow.addView(view1, tableParams);
                        if (row_temp != 0) {
                            mTableLayout.addView(view);
                        }
                        mTableLayout.addView(tableRow);
                    }
                });
            }
        }
    }


    private void setSessionLayout() {
        mSessionsByDay = new HashMap<>();
        mAllSessionsList = new ArrayList<>();

        DbAdapter dbAdapter = DbAdapter.getInstance();
        dbAdapter.open();
        for (int i = 0; i < mSessionDaysList.size(); i++) {
            String sqlSession = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION + " where sessionDay = '" + mSessionDaysList.get(i) + "'" + " order by " + ConferenceTableField.SESSION_CLASSESID + " ASC, " + ConferenceTableField.SESSION_STARTTIME + " ASC";
            List<SessionBean> tempSessionList = ConferenceGetData.getSessionList(dbAdapter, sqlSession);
            mAllSessionsList.addAll(tempSessionList);//获取会议室
            mSessionsByDay.put(mSessionDaysList.get(i), tempSessionList);
        }
        dbAdapter.close();

        //开始时间--> y坐标值 ，Room位置-->x坐标值， 宽固定值， 高-->结束时间-开始时间
        //"startTime":"14:00","endTime":"16:00" sessionName":"国家心血管病中心心脏培训：第一场: 冠心病 classesId":2423
        SessionBean sessionBean;
        for (int i = 0; i < mSessionsByDay.get(mTime).size(); i++) {
            sessionBean = mSessionsByDay.get(mTime).get(i);

            final android.widget.AbsoluteLayout.LayoutParams absParams = new AbsoluteLayout.LayoutParams(DensityUtil.dip2px(mContext, 100f),
                    DensityUtil.dip2px(mContext, getSessionHeight(sessionBean.getStartTime(), sessionBean.getEndTime())),
                    DensityUtil.dip2px(mContext, getStartXLocation(sessionBean.getClassesId() + "")),
                    DensityUtil.dip2px(mContext, getYLocation(sessionBean.getStartTime())));

            final View temp = LayoutInflater.from(mContext).inflate(R.layout.temp_session, null);
            temp.setTag("" + sessionBean.getSessionGroupId());
            if (AppApplication.systemLanguage == 1) {
                ((TextView) temp.findViewById(R.id.tv_session)).setText(sessionBean.getSessionName());
            } else {
                ((TextView) temp.findViewById(R.id.tv_session)).setText(sessionBean.getSessionName_En());
            }

            temp.findViewById(R.id.tv_session).setBackgroundColor(Color.argb(204, 48, 143, 207));
            temp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mx = event.getX();
                    my = event.getY();

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        firstSessionTouchTime = System.currentTimeMillis();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        return false;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        long endSessionTouchTime = System.currentTimeMillis();
                        if (endSessionTouchTime - firstSessionTouchTime < 1000) {
                            int sessionGroupId = Integer.parseInt((String) temp.getTag());

                            int tartgetPosition = 0;
                            for (int i = 0; i < mAllSessionsList.size(); i++) {
                                if (mAllSessionsList.get(i).getSessionGroupId() == sessionGroupId) {
                                    tartgetPosition = i;
                                }
                            }

                            SessionDetailFragment detail = new SessionDetailFragment();
                            detail.setArguments(tartgetPosition, mAllSessionsList, mRoomsList);
                            View moreView = CommonUtils.initView(getActivity(), R.layout.titlebar_session_detail_more);
                            detail.setRightListener(moreView);
                            action(detail, R.string.meeting_schedule_detail_title, moreView, false, false);

                            return true;
                        } else {
                            return false;
                        }
                    }
                    return false;
                }
            });

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAbsContainer.addView(temp, absParams);
                }
            });
        }
    }

    //获取Y坐标值
    private float getYLocation(String time) {
        if (StringUtils.isBlank(time))
            return 0;
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        //加上分隔线高度
        return ((hour - 8) * 4 * 30 + minute * 2 + (float) ((hour - 8) * 4 * 30 + minute * 2) / 30f * 1) + 1;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //获取X坐标值
    private float getStartXLocation(String classRoomId) {
        int position = 0;
        for (int i = 0; i < mRoomsList.size(); i++) {
            if (classRoomId.equals(mRoomsList.get(i).getClassesId() + "")) {
                position = i;
            }
        }
        return (float) (position * 100);
    }

    //获取控件的高度
    private float getSessionHeight(String startTime, String endTime) {
        return getYLocation(endTime) - getYLocation(startTime);
    }

    private void setTimeLayout() {
        final LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 80f), DensityUtil.dip2px(mContext, 30f));
        for (int i = 0; i < mTimes.length; i++) {
            final int temp = i;
            if (i % 4 == 0) {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider, null);

                final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_table, null);
                tv.setText(mTimes[i]);
                tv.setGravity(Gravity.CENTER);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp != 0) {
                            mLlLeftContainer.addView(view);
                        }
                        mLlLeftContainer.addView(tv, timeParams);
                    }
                });
            } else {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider_gap, null);

                final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_table, null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp != 0) {
                            mLlLeftContainer.addView(view);
                        }
                        mLlLeftContainer.addView(tv, timeParams);
                    }
                });
            }
        }
    }

    private void setRoomsLayout() {
        final LinearLayout.LayoutParams roomParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 100f), FP);

        for (int i = 0; i < mRoomsList.size(); i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_table, null);
            String location = "";
            String newLocation = "";
            if (AppApplication.systemLanguage == 1) {
                location = mRoomsList.get(i).getClassesCode();
                newLocation = location.substring(0, 2) + "\n" + location.substring(2, location.length());
            } else {
                location = mRoomsList.get(i).getClassesCodeEn();
//                newLocation = location.substring(0, location.indexOf(",") + 1) + "\n" + location.substring(location.indexOf(",") + 1, location.length());
            }

            tv.setText(location);
            tv.setPadding(8, 8, 8, 8);
            tv.setGravity(Gravity.CENTER);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLlTopContainer.addView(tv, roomParams);
                }
            });
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float curX, curY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                mLeftScrollView.scrollBy((int) (Math.rint(mx - curX)), (int) (Math.rint(my - curY)));
                mTopScrollView.scrollBy((int) (Math.rint(mx - curX)), (int) (Math.rint(my - curY)));
                mHvScrollView.scrollBy((int) (Math.rint(mx - curX)), (int) (Math.rint(my - curY)));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                mLeftScrollView.scrollBy((int) (Math.rint(mx - curX)), (int) (Math.rint(my - curY)));
                mTopScrollView.scrollBy((int) (Math.rint(mx - curX)), (int) (Math.rint(my - curY)));
                mHvScrollView.scrollBy((int) (Math.rint(mx - curX)), (int) (Math.rint(my - curY)));
                break;
        }
        return true;
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, null, "loading...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            setTimeLayout();
            setRoomsLayout();
            setTablesLayout();
            setSessionLayout();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mDialog.dismiss();
        }
    }
}
