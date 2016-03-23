package com.android.incongress.cd.conference.fragments.search_speaker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.adapters.SpeakerDetailAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.beans.RoleBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailFragment;
import com.android.incongress.cd.conference.fragments.scenic_xiu.AskProfessorFragment;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Jacky on 2016/1/7.
 * 讲者详情页面
 */
public class SpeakerDetailFragment extends BaseFragment {
    private StickyListHeadersListView mStickLVSpeaker;
    private TextView mTvSpeakerName, mTvSpeakerRoles;
    private SpeakerDetailAdapter mSpeakerAdapter;
    private List<MeetingBean_new> mMeetings;

    private String mSpeakerName, mSpeakerRoles, mTrueRoleNames = "", mSpeakerNameEn;
    private boolean mIsFromSessionDetai = false;

    private int mSpeakerId;
    private View mHeaderView;

    public SpeakerDetailFragment() {}

    public void setArguments(int speakerId, String speakerName, String speakerEnName, String speakerRoles, List<MeetingBean_new> meetings,  boolean isFromSessionDetai) {
        this.mSpeakerId = speakerId;
        this.mSpeakerName = speakerName;
        this.mSpeakerRoles = speakerRoles;
        this.mMeetings = meetings;
        this.mSpeakerNameEn = speakerEnName;
        this.mIsFromSessionDetai = isFromSessionDetai;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_detail_new, null);
        mStickLVSpeaker = (StickyListHeadersListView) view.findViewById(R.id.slhlv_speaker);

        mStickLVSpeaker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!mIsFromSessionDetai) {
                    int sessionGroupId = mMeetings.get(position-1).getSessionGroupId();

                    SessionDetailFragment detail = new SessionDetailFragment();
                    detail.setArguments(getSessionPosition(sessionGroupId), mSessionList, mRoomsList);
                    action(detail, R.string.meeting_schedule_detail_title, false, false, true);
                }
            }
        });

        mHeaderView = inflater.inflate(R.layout.header_view_speaker_detail, null);
        mStickLVSpeaker.addHeaderView(mHeaderView);
        mTvSpeakerName = (TextView) mHeaderView.findViewById(R.id.tv_speaker_name);
        mTvSpeakerRoles = (TextView) mHeaderView.findViewById(R.id.tv_speaker_role_names);

        if (mMeetings.size() != 0) {
            new MyAsyncTask().execute();
        } else {
            ToastUtils.showShorToast(getString(R.string.faculty_no_assignments));
        }

        return view;
    }

    private ProgressDialog mDialog;

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), null, "loading");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mDialog.dismiss();

            if(AppApplication.systemLanguage == 1) {
                mTvSpeakerName.setText(mSpeakerName);
            }else {
                mTvSpeakerName.setText(mSpeakerNameEn);
            }

            mTvSpeakerRoles.setText(mTrueRoleNames);
            mStickLVSpeaker.setAdapter(mSpeakerAdapter);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mSpeakerAdapter = new SpeakerDetailAdapter(getActivity(), mMeetings, mIsFromSessionDetai);

            //对roleId进行处理显示中文名
            String roleNames = mSpeakerRoles.substring(1, mSpeakerRoles.length() - 1);
            String[] roleNameStrs = roleNames.split(",");

            for (int i = 0; i < roleNameStrs.length; i++) {
                mTrueRoleNames = mTrueRoleNames + getRoleNameById(roleNameStrs[i]);
                if (i != roleNameStrs.length - 1) {
                    mTrueRoleNames = mTrueRoleNames + "、";
                }
            }


            DbAdapter dbAdapter = DbAdapter.getInstance();
            dbAdapter.open();
            String sqlSession = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION;
            String sqlclass = "select * from " + ConferenceTables.TABLE_INCONGRESS_CLASS;
            mSessionList.addAll(ConferenceGetData.getSessionList(dbAdapter, sqlSession));
            mRoomsList.addAll(ConferenceGetData.getClassesList(dbAdapter, sqlclass));
            dbAdapter.close();
            return null;
        }
    }

    private String getRoleNameById(String roleId) {
        DbAdapter db = DbAdapter.getInstance();
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_ROLE + " where roleId = " + roleId;
        db.open();
        List<RoleBean> lists = ConferenceGetData.getroleList(db, sql);
        db.close();
        return lists.get(0).getName();
    }

    public void setRightView(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "" , "");
                    ToastUtils.showShorToast(getString(R.string.login_first));
                    return;
                }
                AskProfessorFragment fragment = null;
                if (AppApplication.systemLanguage == 1) {
                    fragment = AskProfessorFragment.getInstance(mSpeakerName, mSpeakerId);
                } else {
                    fragment = AskProfessorFragment.getInstance(mSpeakerNameEn, mSpeakerId);
                }
                View question = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                fragment.setRightListener(question);
                action(fragment, getString(R.string.ask_sb, mSpeakerName), question, false, false);
            }
        });
    }

    private List<SessionBean> mSessionList = new ArrayList<>();
    private List<ClassesBean> mRoomsList = new ArrayList<>();

    /**
     * 获取Meeting在session中的位置
     * @param sessionGroupId
     * @return
     */
    private int getSessionPosition(int sessionGroupId) {
        for (int i = 0; i < mSessionList.size(); i++) {
            SessionBean bean = mSessionList.get(i);
            if (bean.getSessionGroupId() == sessionGroupId) {
                return i;
            }
        }
        return 0;
    }
}
