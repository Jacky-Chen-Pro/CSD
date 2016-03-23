package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MeetingWithSpeakerAdapter;
import com.android.incongress.cd.conference.adapters.SpeakerTagAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.FacultyBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.beans.RoleBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerDetailFragment;
import com.android.incongress.cd.conference.uis.ListViewForScrollView;
import com.android.incongress.cd.conference.uis.MeetingAndSessionAttentionPopupWindow;
import com.android.incongress.cd.conference.uis.MyButton;
import com.android.incongress.cd.conference.uis.ScrollControlViewpager;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jacky on 2015/12/16.
 * Session详情页
 */
public class SessionDetailPageFragment extends BaseFragment {

    private TextView mTvScheduleTime, mTvRoom, mTvScheduleName, mTvScheduleDetailTime;
    private LinearLayout mLlSessionName, mLlSpeakerContainer;
    private ListViewForScrollView mLvMeetings;
    private ScrollView mScrollview;
    private MeetingAndSessionAttentionPopupWindow mPopupWindow;
    private MyButton mBtLocation, mBtAlarm;

    private SessionBean mSessionBean;
    private ClassesBean mClassBean;
    private List<FacultyBean> mFacultyBeanList = new ArrayList<>();
    private String mFacultyIds, mRoles;

    private List<SpeakerBean> mSpeakerList = new ArrayList<>();
    private List<RoleBean> mRoleList = new ArrayList<>();
    private List<MeetingBean> mMeetingBeanList = new ArrayList<>();
    private List<List<SpeakerBean>> mAllSpeakers = new ArrayList<>();

    private ImageView mIvSessionAlarm;

    public static final String BUNDLE_SESSION = "session";
    public static final String BUNDLE_CLASS = "class";

    /**
     * 闹铃模式
     **/
    private static boolean mIsAlarmMode = false;
    private MeetingWithSpeakerAdapter mMeetingWithSpeakerAdapter;
    private ScrollControlViewpager mViewPager;

    public SessionDetailPageFragment() {
    }

    public static SessionDetailPageFragment getInstance(SessionBean sessionBean, ClassesBean classesBean) {
        SessionDetailPageFragment fragment = new SessionDetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_SESSION, sessionBean);
        bundle.putSerializable(BUNDLE_CLASS, classesBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setViewPager(ScrollControlViewpager pager) {
        this.mViewPager = pager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSessionBean = (SessionBean) getArguments().getSerializable(BUNDLE_SESSION);
            mClassBean = (ClassesBean) getArguments().getSerializable(BUNDLE_CLASS);
        } catch (Exception e) {
            mSessionBean = null;
            mClassBean = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_detail_page, container, false);
        initViews(view);
        initEvents();

        new MyAsynkTask().execute();
        showGuideInfo();
        return view;
    }

    private void initViews(View view) {
        mTvScheduleTime = (TextView) view.findViewById(R.id.tv_schedule_time);
        mTvRoom = (TextView) view.findViewById(R.id.tv_schedule_room);
        mTvScheduleName = (TextView) view.findViewById(R.id.tv_schedule_name);
        mTvScheduleDetailTime = (TextView) view.findViewById(R.id.tv_schedule_detail_time);
        mLlSpeakerContainer = (LinearLayout) view.findViewById(R.id.ll_speaker_container);
        mLvMeetings = (ListViewForScrollView) view.findViewById(R.id.lv_meetings);
        mScrollview = (ScrollView) view.findViewById(R.id.scrollview);
        mBtLocation = (MyButton) view.findViewById(R.id.bt_location);
        mBtAlarm = (MyButton) view.findViewById(R.id.bt_alarm);
        mLlSessionName = (LinearLayout) view.findViewById(R.id.ll_session_name);
        mIvSessionAlarm = (ImageView) view.findViewById(R.id.iv_session_alarm);

        if(mSessionBean.getAttention() == 1) {
            mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmon);
        }else {
            mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);
        }

        mLlSessionName.setBackgroundColor(Color.argb(51, 48, 143, 207));

        if (AppApplication.systemLanguage == 1) {
            mTvScheduleTime.setText(DateUtil.getDateWithWeek(DateUtil.getDate(mSessionBean.getSessionDay(), DateUtil.DEFAULT)));
            mTvRoom.setText(mClassBean.getClassesCode());
            mTvScheduleName.setText(mSessionBean.getSessionName());
        } else {
            mTvScheduleTime.setText(DateUtil.getDateWithWeekInEnglish(DateUtil.getDate(mSessionBean.getSessionDay(), DateUtil.DEFAULT)));
            mTvRoom.setText(mClassBean.getClassesCodeEn());
            mTvScheduleName.setText(mSessionBean.getSessionName_En());
        }

        mTvScheduleDetailTime.setText(mSessionBean.getStartTime() + "-" + mSessionBean.getEndTime());

        mFacultyIds = mSessionBean.getFacultyId();
        mRoles = mSessionBean.getRoleId();
    }

    private void initEvents() {
        mBtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsAlarmMode) {
                    MeetingScheduleRoomLocationFragment fragment = new MeetingScheduleRoomLocationFragment();
                    fragment.setRoomBean(mClassBean, null, MeetingScheduleRoomLocationFragment.TYPE_MEETING);
                    action(fragment, R.string.meeting_schedule_room_location, false, false);
                }
            }
        });

        mBtAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsAlarmMode) {
                    //进入闹铃模式
                    mIsAlarmMode = true;
                    mViewPager.setCanScroll(false);
                    mBtAlarm.setText(R.string.mymeeting_finish);
                    mIvSessionAlarm.setVisibility(View.VISIBLE);
                    mMeetingWithSpeakerAdapter.setAlarmMode(true);
                } else {
                    //完成闹铃设置
                    mIvSessionAlarm.setVisibility(View.GONE);
                    mMeetingWithSpeakerAdapter.setAlarmMode(false);
                    mIsAlarmMode = false;
                    mViewPager.setCanScroll(true);
                    mBtAlarm.setText(R.string.meeting_schedule_alarm);
                }
            }
        });

        mIvSessionAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIvSessionAlarm.getVisibility() == View.VISIBLE) {
                    if (mSessionBean.getAttention() == Constants.ATTENTION) {
                        mSessionBean.setAttention(Constants.NOATTENTION);
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);

                        List<MeetingBean> meetings = mMeetingWithSpeakerAdapter.getMeetingBeanList();
                        for (int i = 0; i < meetings.size(); i++) {
                            meetings.get(i).setAttention(Constants.NOATTENTION);
                        }

                        mMeetingWithSpeakerAdapter.notifyDataSetChanged();

                        //取消Session的关注,如果有闹钟则移除闹钟
                        DbAdapter ada = DbAdapter.getInstance();
                        ada.open();
                        ConferenceSetData.updateSessionAttention(ada, mSessionBean.getSessionGroupId(), Constants.NOATTENTION);
                        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_ALERT + " where " + ConferenceTableField.ALERT_RELATIVEID + " = " + mSessionBean.getSessionGroupId();
                        List<AlertBean> beans = ConferenceGetData.getAlert(ada, sql);
                        AlertBean alertForSession = beans.size() > 0 ? beans.get(0) : null;
                        if (alertForSession != null) {
                            ConferenceSetData.disableAlert(ada, alertForSession);
                        }

                        //取消meeting的关注
                        for (int i = 0; i < mMeetingWithSpeakerAdapter.getMeetingBeanList().size(); i++) {
                            MeetingBean bean = mMeetingWithSpeakerAdapter.getMeetingBeanList().get(i);
                            ConferenceSetData.updateMeetingAttention(ada, bean.getMeetingId(), Constants.NOATTENTION);
                        }
                        ada.close();
                    } else {
                        mSessionBean.setAttention(Constants.ATTENTION);
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmon);

                        //全部都关注起来
                        List<MeetingBean> meetings = mMeetingWithSpeakerAdapter.getMeetingBeanList();
                        for (int i = 0; i < meetings.size(); i++) {
                            meetings.get(i).setAttention(Constants.ATTENTION);
                        }

                        mMeetingWithSpeakerAdapter.notifyDataSetChanged();

                        //关注该session，包括下属所有Meeting,添加闹钟
                        DbAdapter ada = DbAdapter.getInstance();
                        ada.open();
                        ConferenceSetData.updateSessionAttention(ada, mSessionBean.getSessionGroupId(), Constants.ATTENTION);
                        ada.close();;

                        AlertBean alertbean = new AlertBean();
                        alertbean.setDate(mSessionBean.getSessionDay());
                        alertbean.setEnable(1);
                        alertbean.setEnd(mSessionBean.getEndTime());
                        alertbean.setRelativeid(String.valueOf(mSessionBean.getSessionGroupId()));
                        alertbean.setRepeatdistance("5");
                        alertbean.setRepeattimes("0");
                        alertbean.setRoom(mClassBean.getClassesCode());
                        alertbean.setStart(mSessionBean.getStartTime());
                        alertbean.setTitle(mSessionBean.getSessionName() + "#@#" + mSessionBean.getSessionName_En());
                        alertbean.setType(AlertBean.TYPE_SESSTION);
                        ada.open();
                        ConferenceSetData.addAlert(ada, alertbean);
                        String sql ="select * from "+ ConferenceTables.TABLE_INCONGRESS_ALERT +" where "+ConferenceTableField.ALERT_RELATIVEID +" = "+mSessionBean.getSessionGroupId();
                        List<AlertBean> beans = ConferenceGetData.getAlert(ada, sql);
                        AlertBean bean = beans.size()>0?beans.get(0):null;
                        ada.close();

                        if(bean!=null)
                            AlermClock.addClock(bean);

                        ada.open();
                        //设置meeting的关注
                        for (int i = 0; i < mMeetingWithSpeakerAdapter.getMeetingBeanList().size(); i++) {
                            MeetingBean temp = mMeetingWithSpeakerAdapter.getMeetingBeanList().get(i);
                            ConferenceSetData.updateMeetingAttention(ada, temp.getMeetingId(), Constants.ATTENTION);
                        }
                        ada.close();
                    }
                }
            }
        });
    }

    /**
     * 根据speakerId获取SpeakerBean对象
     *
     * @param speakerId
     * @return
     */
    private SpeakerBean getSpeakerById(DbAdapter db, String speakerId) {
        if ("".equals(speakerId)) {
            return null;
        }
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_SPEAKER + " where speakerId = " + speakerId;
        List<SpeakerBean> lists = ConferenceGetData.getSpeakersList(db, sql);
        if (lists == null || lists.size() == 0)
            return null;
        return lists.get(0);
    }

    /**
     * 根据roleId获取身份信息
     *
     * @param roleId
     * @return
     */
    private RoleBean getRoleBeanById(DbAdapter db, String roleId) {
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_ROLE + " where roleId = " + roleId;
        List<RoleBean> lists = ConferenceGetData.getroleList(db, sql);
        return lists.get(0);
    }

    /**
     * 根据sessionGroupId去查找所有的meetingId
     *
     * @param sessionGroupId
     */
    private void getMeetingBeanBySessionId(DbAdapter db, String sessionGroupId) {
        mMeetingBeanList.clear();

        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_MEETING + " where sessionGroupId = " + sessionGroupId;
        mMeetingBeanList.addAll(ConferenceGetData.getMeetingList(db, sql));
    }

    //老方法
    //查找一个人的所有身份类型和相对应类型下的meeting或者session
    //先查session表：session -- > roleId
    //查出所有的session  --> HashMap<Integer,List<MeetingBean>>
    //先把roleId放入integer中，如果有了就不加，然后把相应的session放到对应key的List<MeetingBean>中

    //首先得到这个人的speakerId，这个是唯一的，然后根据这个speakerId分别去session表和meeting表找对应的session和meeting然后存到队列中，最后返还给需要的数据
    private List<Integer> mRoleIds = new ArrayList<>();
    private List<MeetingBean_new> mAllMeetings = new ArrayList<>();
    private List<MeetingBean_new> mAllMeetings_sorted = new ArrayList<>();

    private void getAllSessionAndMeetingBySpeakerId(DbAdapter ada, int speakerId) {
        //获得了所有的身份类型
        mRoleIds.clear();
        mAllMeetings.clear();
        mAllMeetings_sorted.clear();

        //获取该speakerId下的所有session级别会议
        String sessionSql = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION +
                " where facultyId like '%," + speakerId + ",%' or facultyId like '%," + speakerId + "' or facultyId like '" + speakerId + ",%'";

        List<SessionBean> sessions = new ArrayList<SessionBean>();
        sessions.addAll(ConferenceGetData.getSessionList(ada, sessionSql));

        //获取该speakerId下的所有presentation级别会议
        String meetingSql = "select * from " + ConferenceTables.TABLE_INCONGRESS_MEETING +
                " where facultyId = " + speakerId;
        List<MeetingBean> meetings = new ArrayList<MeetingBean>();
        meetings.addAll(ConferenceGetData.getMeetingList(ada, meetingSql));

        //设置每个session下对应的身份类型
        for (int i = 0; i < sessions.size(); i++) {
            MyLogger.jLog().i(sessions.get(i).toString());
            SessionBean bean = sessions.get(i);
            String roleId = bean.getRoleId();

            int rolePosition = 0;
            String[] speakerIds = bean.getFacultyId().split(",");
            for (int j = 0; j < speakerIds.length; j++) {
                if (speakerIds[j].equals(speakerId + "")) {
                    rolePosition = j;
                    break;
                }
            }

            String[] roleIds = roleId.split(",");
            String tempRoleId = roleIds[rolePosition];
            if (!mRoleIds.contains(Integer.parseInt(tempRoleId))) {
                mRoleIds.add(Integer.parseInt(tempRoleId));
            }

            MeetingBean_new newBean = new MeetingBean_new(bean.getSessionGroupId(), bean.getSessionName(), bean.getSessionDay(),
                    bean.getStartTime(), bean.getEndTime(), bean.getClassesId(), bean.getSessionGroupId(), bean.getSessionName_En(),
                    Integer.parseInt(tempRoleId), getRoleNameById(ada, tempRoleId).getName(), getRoleNameById(ada, tempRoleId).getEnName(),
                    getClassNameByClassId(ada, bean.getClassesId()).getClassesCode(), getClassNameByClassId(ada, bean.getClassesId()).getClassesCodeEn(),1);
            mAllMeetings.add(newBean);
        }

        for (int i = 0; i < meetings.size(); i++) {
            MyLogger.jLog().i(meetings.get(i).toString());

            MeetingBean bean = meetings.get(i);
            String roleId = bean.getRoleIds();

            int rolePosition = 0;
            String[] speakerIds = bean.getFacultyIds().split(",");
            for (int j = 0; j < speakerIds.length; j++) {
                if (speakerIds[j].equals(speakerId + "")) {
                    rolePosition = j;
                    break;
                }
            }

            String[] roleIds = roleId.split(",");
            String tempRoleId = roleIds[rolePosition];

            if (!mRoleIds.contains(Integer.parseInt(tempRoleId))) {
                mRoleIds.add(Integer.parseInt(tempRoleId));
            }

//            MeetingBean_new newBean = new MeetingBean_new(bean.getSessionGroupId(), bean.getTopic(), bean.getMeetingDay(), bean.getStartTime(), bean.getEndTime(), bean.getClassesId(), getClassNameByClassId(ada, bean.getClassesId()), bean.getSessionGroupId(), bean.getTopic_En(), Integer.parseInt(tempRoleId), getRoleNameById(ada, tempRoleId));
            MeetingBean_new newBean = new MeetingBean_new(bean.getMeetingId(), bean.getTopic(),
                    bean.getMeetingDay(), bean.getStartTime(), bean.getEndTime(), bean.getClassesId(),
                    bean.getSessionGroupId(), bean.getTopic_En(), Integer.parseInt(tempRoleId),
                    getRoleNameById(ada, tempRoleId).getName(), getRoleNameById(ada, tempRoleId).getEnName(),
                    getClassNameByClassId(ada, bean.getClassesId()).getClassesCode(),
                    getClassNameByClassId(ada, bean.getClassesId()).getClassesCodeEn(),2);
            mAllMeetings.add(newBean);
        }

        //根据id将meeting重新排序
        Collections.sort(mRoleIds);

        //调整会议的顺序
        for (int i = 0; i < mRoleIds.size(); i++) {
            int roleId = mRoleIds.get(i);
            for (int j = 0; j < mAllMeetings.size(); j++) {
                if (mAllMeetings.get(j).getRoleId() == roleId) {
                    mAllMeetings_sorted.add(mAllMeetings.get(j));
                }
            }
        }
    }

    class MyAsynkTask extends AsyncTask<Void, Void, Void> {
        DbAdapter db = DbAdapter.getInstance();

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mMeetingWithSpeakerAdapter = new MeetingWithSpeakerAdapter(getActivity(), mMeetingBeanList, mAllSpeakers, new MeetingWithSpeakerAdapter.OnTagListener() {
                @Override
                public void tagListener(SpeakerBean bean) {
                    if (!mIsAlarmMode) {
                        DbAdapter tempDb = DbAdapter.getInstance();
                        tempDb.open();
                        //查出这个人所有的meeting內容
                        getAllSessionAndMeetingBySpeakerId(tempDb, bean.getSpeakerId());
                        tempDb.close();
                        SpeakerDetailFragment speaker = new SpeakerDetailFragment();
                        speaker.setArguments(bean.getSpeakerId(), bean.getSpeakerName(), bean.getEnName(), mRoleIds.toString(), mAllMeetings_sorted, true);
                        action(speaker, R.string.speaker_detial, null, false, false);
                    }
                }
            }, new MeetingWithSpeakerAdapter.SessionAlarmListener() {
                @Override
                public void doWhenMeetingAlarmClicked(boolean sessionAlarmToggle) {
                    if (sessionAlarmToggle) {
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmon);
                        mSessionBean.setAttention(Constants.ATTENTION);
                    } else {
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);
                        mSessionBean.setAttention(Constants.NOATTENTION);
                    }
                }
            });

            mLvMeetings.setAdapter(mMeetingWithSpeakerAdapter);
            mLvMeetings.setFocusable(false);
            mScrollview.smoothScrollTo(0, 20);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db.open();
        }


        @Override
        protected Void doInBackground(Void... params) {
            //开始操作之前，初始化数据，存在刷新重复加载的问题
            mAllSpeakers.clear();
            mSpeakerList.clear();
            mFacultyBeanList.clear();

            //设置所有该session下的meeting
            getMeetingBeanBySessionId(db, String.valueOf(mSessionBean.getSessionGroupId()));

            String[] facults = mFacultyIds.split(",");
            String[] roles = mRoles.split(",");

            //获取所有的speaker，包括演讲者id和身份id
            for (int i = 0; i < facults.length; i++) {
                FacultyBean bean = new FacultyBean();
                bean.setFacultyId(facults[i]);
                bean.setRoleId(roles[i]);
                mFacultyBeanList.add(bean);
            }


            for (int i = 0; i < mFacultyBeanList.size(); i++) {
                FacultyBean bean = mFacultyBeanList.get(i);

                if ("".equals(bean.getFacultyId()))
                    continue;

                SpeakerBean speaker = getSpeakerById(db, bean.getFacultyId());
                RoleBean role = getRoleBeanById(db, bean.getRoleId());
                speaker.setType(Integer.parseInt(bean.getRoleId()));
                mSpeakerList.add(speaker);

                boolean isRoleExist = false;
                for (int j = 0; j < mRoleList.size(); j++) {
                    if (role.getRoleId() == mRoleList.get(j).getRoleId()) {
                        isRoleExist = true;
                    }
                }
                if (!isRoleExist) {
                    mRoleList.add(role);
                }
            }

            //添加对应身份类型和对应的人员
            for (int i = 0; i < mRoleList.size(); i++) {
                final int temp = i;
                List<SpeakerBean> speakers = new ArrayList<>();
                for (int j = 0; j < mSpeakerList.size(); j++) {
                    if (mSpeakerList.get(j).getType() == mRoleList.get(i).getRoleId()) {
                        speakers.add(mSpeakerList.get(j));
                    }
                }

                final SpeakerTagAdapter speakerAdapter = new SpeakerTagAdapter(getActivity(), speakers);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //加上身份类型和，相应身份类型下面的人民
                        View roleView = LayoutInflater.from(getContext()).inflate(R.layout.speaker_flowlayout, null);
                        if (AppApplication.systemLanguage == 1) {
                            ((TextView) roleView.findViewById(R.id.tv_role_name)).setText(mRoleList.get(temp).getName());
                        } else {
                            ((TextView) roleView.findViewById(R.id.tv_role_name)).setText(mRoleList.get(temp).getEnName());
                        }
                        final TagFlowLayout flowLayout = (TagFlowLayout) roleView.findViewById(R.id.speaker_by_role);
                        final View divider = roleView.findViewById(R.id.divider_line);
                        mLlSpeakerContainer.addView(roleView);

                        //不显示分隔线
//                        if(temp != mRoleList.size()-1) {
//                            divider.setVisibility(View.VISIBLE);
//                        }

                        flowLayout.setAdapter(speakerAdapter);
                        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                            @Override
                            public boolean onTagClick(View view, int position, FlowLayout parent) {
                                if (!mIsAlarmMode) {
                                    SpeakerBean bean = speakerAdapter.getSpeakerList().get(position);

                                    DbAdapter tempDb = DbAdapter.getInstance();
                                    tempDb.open();
                                    //查出这个人所有的meeting內容
                                    getAllSessionAndMeetingBySpeakerId(tempDb, bean.getSpeakerId());
                                    tempDb.close();
                                    SpeakerDetailFragment speaker = new SpeakerDetailFragment();
                                    speaker.setArguments(bean.getSpeakerId(), bean.getSpeakerName(), bean.getEnName(), mRoleIds.toString(), mAllMeetings_sorted, true);

                                    ImageView askView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
                                    askView.setImageResource(R.drawable.ask_professor);
                                    speaker.setRightView(askView);
                                    action(speaker, R.string.speaker_detial, askView, false, false);
                                }
                                return true;
                            }
                        });
                    }
                });

            }

            for (int i = 0; i < mMeetingBeanList.size(); i++) {
                String sps = mMeetingBeanList.get(i).getFacultyIds();
                String[] speaker_ = sps.split(",");

                List<SpeakerBean> speakersList = new ArrayList<SpeakerBean>();

                for (int j = 0; j < speaker_.length; j++) {
                    SpeakerBean bean = getSpeakerById(db, speaker_[j]);
                    if (bean != null)
                        speakersList.add(bean);
                }
                mAllSpeakers.add(speakersList);
            }
            return null;
        }
    }

    private RoleBean getRoleNameById(DbAdapter db, String roleId) {
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_ROLE + " where roleId = " + roleId;
        List<RoleBean> lists = ConferenceGetData.getroleList(db, sql);
        return lists.get(0);
    }

    private ClassesBean getClassNameByClassId(DbAdapter db, int classId) {
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_CLASS + " where classesId = " + classId;
        List<ClassesBean> lists = ConferenceGetData.getClassesList(db, sql);
        return lists.get(0);
    }


    /**
     * 显示指示页
     */
    private void showGuideInfo() {
        if (AppApplication.getSPIntegerValue(Constants.GUIDE_SESSION) != 1) {
            getActivity().findViewById(R.id.home_guide).setVisibility(View.VISIBLE);
            ((ImageView) getActivity().findViewById(R.id.home_guide)).setImageResource(R.drawable.session_guide);
            ((ImageView) getActivity().findViewById(R.id.home_guide)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().findViewById(R.id.home_guide).setVisibility(View.GONE);
                    AppApplication.setSPIntegerValue(Constants.GUIDE_SESSION, 1);
                }
            });
        }
    }
}
