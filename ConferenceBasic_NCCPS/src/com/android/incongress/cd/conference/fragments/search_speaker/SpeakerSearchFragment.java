package com.android.incongress.cd.conference.fragments.search_speaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.beans.RoleBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.fragments.scenic_xiu.AskProfessorFragment;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.adapters.MettingCommunitySpeakerAdapter;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.utils.BladeView;
import com.android.incongress.cd.conference.utils.BladeView.OnItemClickListener;
import com.android.incongress.cd.conference.utils.MySectionIndexer;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView;

public class SpeakerSearchFragment extends BaseFragment {

    PinnedHeaderListView mSpeakerListView;
    MettingCommunitySpeakerAdapter mAdAdapter;
    BladeView mBladeView;
    private TextView mTvTips;

    private List<SpeakerBean> mSpeakers;
    private int mCurrentType = TYPE_FROM_HOME;

    public static final String TYPE_FROM_WHERE = "from_where";
    public static final int TYPE_FROM_HOME = 1;
    public static final int TYPE_FROM_SCENIC_XIU = 2;

    private MySectionIndexer mIndexer;
    private static final String ALL_CHARACTER = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
    protected static final String TAG = null;

    private String[] sections = {"", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    private int[] counts;

    public SpeakerSearchFragment() {
    }

    public static final SpeakerSearchFragment getInstance(int typeFrom) {
        SpeakerSearchFragment fragment = new SpeakerSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_FROM_WHERE, typeFrom);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentType = getArguments().getInt(TYPE_FROM_WHERE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.hysqhome_jiangzhe, null);
        mSpeakerListView = (PinnedHeaderListView) view.findViewById(R.id.hysq_home_jiangzhe_pinnedListView);
        mBladeView = (BladeView) view.findViewById(R.id.hysq_home_jiangzhe_mLetterListView);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);

        counts = new int[sections.length];
        getSpeakerDatas();

        if(mSpeakers.size() == 0) {
            mTvTips.setVisibility(View.VISIBLE);
            mSpeakerListView.setVisibility(View.GONE);
            mBladeView.setVisibility(View.GONE);
        }else {
            mTvTips.setVisibility(View.GONE);
            mBladeView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(String s) {
                    if (s != null) {
                        int section = ALL_CHARACTER.indexOf(s);
                        int position = mIndexer.getPositionForSection(section);

                        if (position != -1) {
                            mSpeakerListView.setSelection(position);
                        }
                    }
                }
            });

            if (mCurrentType == TYPE_FROM_HOME) {
                mSpeakerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        SpeakerBean bean = (SpeakerBean) mAdAdapter.getItem(arg2);

                        DbAdapter tempDb = DbAdapter.getInstance();
                        tempDb.open();
                        getAllSessionAndMeetingBySpeakerId(tempDb, bean.getSpeakerId());
                        tempDb.close();

                        View view = CommonUtils.initView(getActivity(), R.layout.title_right_image);
                        ((ImageView) view).setImageResource(R.drawable.ask_professor);

                        SpeakerDetailFragment speakerDetailFragment = new SpeakerDetailFragment();
                        speakerDetailFragment.setArguments(bean.getSpeakerId(), bean.getSpeakerName(), bean.getEnName(), mRoleIds.toString(), mAllMeetings, false);
                        speakerDetailFragment.setRightView(view);
                        action(speakerDetailFragment, R.string.speaker_detial, view, false, false);
                    }
                });
            } else {
                mSpeakerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SpeakerBean bean = ((SpeakerBean) mAdAdapter.getItem(position));

                        AskProfessorFragment fragment = AskProfessorFragment.getInstance(bean.getSpeakerName(), bean.getSpeakerId());
                        View question = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                        fragment.setRightListener(question);

                        action(fragment, "向" + bean.getSpeakerName() + "提问", question, false, false);
                    }
                });
            }
            mSpeakerListView.setOnScrollListener(mAdAdapter);
        }
        return view;
    }

    private void getSpeakerDatas() {
        DbAdapter db = DbAdapter.getInstance();
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_SPEAKER + " order by " + ConferenceTableField.SPEAKER_SPEAKERNAMEPINGYIN + " asc";
        db.open();
        mSpeakers = ConferenceGetData.getSpeakersList(db, sql);
        db.close();

        List<SpeakerBean> mAttentionList = new ArrayList<SpeakerBean>();
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (int i = 0; i < sections.length; i++) {
            map.put(sections[i], 0);
        }

        for (int i = 0; i < mSpeakers.size(); i++) {
            SpeakerBean bean = mSpeakers.get(i);
            String letter = bean.getFirstLetter();
            if (!map.containsKey(letter)) {
                letter = sections[sections.length - 1];
                bean.setFirstLetter(letter);
            }
            int count = map.get(bean.getFirstLetter());
            map.put(bean.getFirstLetter(), ++count);
            if (bean.getAttention() == 1) {
                int count_attention = map.get(sections[0]);
                map.put(sections[0], ++count_attention);
                mAttentionList.add(bean);
            }
        }

        for (int i = 0; i < sections.length; i++) {
            counts[i] = map.get(sections[i]);
        }
        if (counts[0] == 0) {
            String[] msectionslist = new String[sections.length - 1];
            int[] newcounts = new int[sections.length - 1];
            for (int i = 1; i < sections.length; i++) {
                String section = sections[i];
                msectionslist[i - 1] = section;
                int count = counts[i];
                newcounts[i - 1] = count;
            }
            sections = msectionslist;
            counts = newcounts;
        }
        mIndexer = new MySectionIndexer(sections, counts);
        mSpeakers.addAll(0, mAttentionList);
        mAdAdapter = new MettingCommunitySpeakerAdapter(mSpeakers, mIndexer);

        mSpeakerListView.setAdapter(mAdAdapter);
        mSpeakerListView.setPinnedHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.hysqhome_jiangzhe_group, mSpeakerListView, false));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private List<Integer> mRoleIds = new ArrayList<Integer>();
    private List<MeetingBean_new> mAllMeetings = new ArrayList<MeetingBean_new>();
    private List<MeetingBean_new> mAllMeetings_sorted = new ArrayList<MeetingBean_new>();
    

    private void getAllSessionAndMeetingBySpeakerId(DbAdapter ada, int speakerId) {
        //获得了所有的身份类型
        mRoleIds.clear();
        mAllMeetings.clear();
        mAllMeetings_sorted.clear();

        //获取该speakerId下的所有session级别会议
        String sessionSql = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION + " where facultyId like '%" + speakerId + "%'";

        List<SessionBean> sessions = new ArrayList<SessionBean>();
        sessions.addAll(ConferenceGetData.getSessionList(ada, sessionSql));

        //获取该speakerId下的所有presentation级别会议
        String meetingSql = "select * from " + ConferenceTables.TABLE_INCONGRESS_MEETING + " where facultyId like '%" + speakerId + "%'";
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

            MeetingBean_new newBean =
                    new MeetingBean_new(bean.getSessionGroupId(), bean.getSessionName(), bean.getSessionDay(), bean.getStartTime(), bean.getEndTime(),
                            bean.getClassesId(), bean.getSessionGroupId(),bean.getSessionName_En(), Integer.parseInt(tempRoleId),
                            getRoleNameById(ada, tempRoleId).getName(),  getRoleNameById(ada, tempRoleId).getEnName(),
                            getClassNameByClassId(ada, bean.getClassesId()).getClassesCode(),
                            getClassNameByClassId(ada, bean.getClassesId()).getClassesCodeEn(),1);

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

            MeetingBean_new newBean =
                    new MeetingBean_new(bean.getSessionGroupId(), bean.getTopic(), bean.getMeetingDay(), bean.getStartTime(),
                             bean.getEndTime(),
                            bean.getClassesId(), bean.getSessionGroupId(),bean.getTopic_En(), Integer.parseInt(tempRoleId),
                            getRoleNameById(ada, tempRoleId).getName(),  getRoleNameById(ada, tempRoleId).getEnName(),
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

}
