package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.SessionDetailViewPagerAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.Notes;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.uis.ScrollControlViewpager;
import com.android.incongress.cd.conference.uis.SharePopupWindow;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2015/12/14.
 * 会议详情 目前有一个viewpager和页码标识
 */
public class SessionDetailFragment extends BaseFragment {
    private ScrollControlViewpager mViewPager;

    private View mRightView;
    private SessionDetailViewPagerAdapter mAdapter;
    private CharSequence[] Titles;

    private SharePopupWindow mSharePopupWindow;

    public SessionDetailFragment() {
    }

    private List<SessionBean> mSessionBeanList;
    private List<ClassesBean> mClassBeanList;

    //分享记笔记弹窗
    private PopupWindow mShareNotePopup;

    //总的页数
    private int mNumTabs = 0;
    public static int mPosition = 0;

    //页码信息
    private TextView mTvPageInfo;

    private List<SessionDetailPageFragment> mSessionDetailFragments = new ArrayList<>();

    public void setArguments(int position, List<SessionBean> sessionBeanList, List<ClassesBean> classesBeanList) {
        this.mSessionBeanList = sessionBeanList;
        this.mClassBeanList = classesBeanList;
        this.mPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_detail, null, false);

        mViewPager = (ScrollControlViewpager) view.findViewById(R.id.session_pager);
        mTvPageInfo = (TextView) view.findViewById(R.id.tv_page_info);

        mNumTabs = mSessionBeanList.size();
        Titles = new CharSequence[mSessionBeanList.size()];

        for (int i = 0; i < mSessionBeanList.size(); i++) {
            Titles[i] = mSessionBeanList.get(i).getSessionName();
        }

        for(int i=0; i<mSessionBeanList.size(); i++) {
            SessionBean session = mSessionBeanList.get(i);
            ClassesBean classesBean = null;

            for (int j = 0; j < mClassBeanList.size(); j++) {
                if (session.getClassesId() == mClassBeanList.get(j).getClassesId()) {
                    classesBean = mClassBeanList.get(j);
                }
            }
            SessionDetailPageFragment fragment = SessionDetailPageFragment.getInstance(session, classesBean);
            fragment.setViewPager(mViewPager);
            mSessionDetailFragments.add(fragment);
        }

        mAdapter = new SessionDetailViewPagerAdapter(getChildFragmentManager(),mSessionDetailFragments);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mTvPageInfo.setText((mPosition + 1) + "/" + mNumTabs);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTvPageInfo.setText((1 + position) + "/" + mNumTabs);
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    private void initPopupWindow() {
        View pview = getActivity().getLayoutInflater().inflate(R.layout.share_note_background, null, false);

        mShareNotePopup = new PopupWindow(pview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mShareNotePopup.setOutsideTouchable(true);
        mShareNotePopup.setBackgroundDrawable(new BitmapDrawable());

        final TextView shareView = (TextView) pview.findViewById(R.id.tv_share);
        TextView noteView = (TextView) pview.findViewById(R.id.tv_make_note);

        initSharePopupWindow();

        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareNotePopup.dismiss();
                mSharePopupWindow.setAnimationStyle(R.style.popupwindow_anim_alpha);
                mSharePopupWindow.showAtLocation(getActivity().findViewById(R.id.fl_container), Gravity.BOTTOM, 0, 0);
                lightOff();
            }
        });

        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNoted;

                DbAdapter db = DbAdapter.getInstance();
                db.open();
                String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_Note + " where " + ConferenceTableField.NOTES_SESSIONID + " = " + mSessionBeanList.get(mPosition).getSessionGroupId();
                List<Notes> list = ConferenceGetData.getNoteList(db, sql);
                if (list.size() > 0) {
                    isNoted = true;
                } else {
                    isNoted = false;
                }
                db.close();

                if (isNoted) {
                    //已经记录过一次笔记，那么就是type_update
                    View view = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                    ((TextView) view).setText(R.string.save);
                    MeetingNoteEditorFragment fragment = MeetingNoteEditorFragment.getInstance(MeetingNoteEditorFragment.TYPE_UPDATE, mSessionBeanList.get(mPosition), mClassBeanList.get(mPosition), list.get(0));
                    fragment.setRightView(view);
                    action(fragment, R.string.meeting_schedule_note, view, false, false);
                    mShareNotePopup.dismiss();
                } else {
                    //没有笔记，那么是第一次进入笔记本是type_new
                    View view = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                    ((TextView) view).setText(R.string.save);
                    ClassesBean classBean = null;
                    for (int i = 0; i < mClassBeanList.size(); i++) {
                        if (mSessionBeanList.get(mPosition).getClassesId() == mClassBeanList.get(i).getClassesId()) {
                            classBean = mClassBeanList.get(i);
                        }
                    }
                    MeetingNoteEditorFragment fragment = MeetingNoteEditorFragment.getInstance(MeetingNoteEditorFragment.TYPE_NEW, mSessionBeanList.get(mPosition), classBean, null);
                    fragment.setRightView(view);
                    action(fragment, R.string.meeting_schedule_note, view, false, false);
                    mShareNotePopup.dismiss();
                }
            }
        });
    }

    public void setRightListener(View view) {
        mRightView = view;
        mRightView.findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareNotePopup == null)
                    initPopupWindow();
                else {
                    mSharePopupWindow.setContent(mSessionBeanList.get(mPosition).getSessionName());
                }

                if (mShareNotePopup != null && mShareNotePopup.isShowing()) {
                    mShareNotePopup.dismiss();
                } else {
                    mShareNotePopup.setAnimationStyle(R.style.popupwindow_anim_alpha);
                    mShareNotePopup.showAtLocation(mViewPager, Gravity.RIGHT | Gravity.TOP, 30, DensityUtil.dip2px(getActivity(), 76));
                }
            }
        });
    }

    private void initSharePopupWindow() {
        mSharePopupWindow = new SharePopupWindow(getActivity(), "", mSessionBeanList.get(mPosition).getSessionName());
        mSharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
    }
}
