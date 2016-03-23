package com.android.incongress.cd.conference.fragments.search_schedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MettingSearchAdapter;
import com.android.incongress.cd.conference.adapters.SessionSearchAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailFragment;
import com.android.incongress.cd.conference.uis.ClearEditText;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索模块
 *
 * @author Jacky_Chen
 * @time 2014年11月25日
 * <p/>
 * 标题选中白色，未选中#234a6c
 */
public class SearchScheduleFragment extends BaseFragment implements OnClickListener {

    private TextView mImageView;
    private ClearEditText mIncongressEditText;
    private SessionSearchAdapter mSessionAdapter;
    private MettingSearchAdapter mMettingAdapter;
    private TextView search_cancel_text;
    private boolean mDeleteClick;//删除按键和 Edittext的监听有冲突 为避免 进行判断
    private TextView mNoData;
    private ViewPager mPager;
    private TabLayout mTabLayout;

    private String mTabTitles[] = new String[2];

    //用于跳转详情页
    //所有房间的list集合
    private List<ClassesBean> mRoomsList;

    //所有session的list集合
    private List<SessionBean> mSessionList;

    public SearchScheduleFragment() {
    }

    private MeetingSearchAdapter mMeetingSearchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_schedule, null);

        mTabTitles[0] = getString(R.string.search_metting);
        mTabTitles[1] = getString(R.string.search_fayan);

        search_cancel_text = (TextView) view.findViewById(R.id.search_cancel_text);
        mIncongressEditText = (ClearEditText) view.findViewById(R.id.search_search);

        search_cancel_text.setOnClickListener(this);
        mIncongressEditText.setOnClickListener(this);
        mSessionAdapter = new SessionSearchAdapter();
        mMettingAdapter = new MettingSearchAdapter();

        mImageView = (TextView) view.findViewById(R.id.search_icon);
        mNoData = (TextView) view.findViewById(R.id.itv_no_data);
        mMeetingSearchAdapter = new MeetingSearchAdapter(getChildFragmentManager(), mTabTitles);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(mMeetingSearchAdapter);
        mPager.setOffscreenPageLimit(3);

        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mImageView.setText(R.string.search_session_hint);
                    hideShurufa();
                    mIncongressEditText.setText("");
                    search_cancel_text.setVisibility(View.GONE);

                } else {
                    mImageView.setText(R.string.search_meeting_hint);
                    hideShurufa();
                    mIncongressEditText.setText("");
                    search_cancel_text.setVisibility(View.GONE);
                }

                //pager滑动时，取消对editText的聚焦
                mIncongressEditText.setFocusable(false);
                mIncongressEditText.setFocusableInTouchMode(false);
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mIncongressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (mIncongressEditText.getText().length() > 0) {
                    mImageView.setVisibility(View.INVISIBLE);
                } else {
                    int checkedId = mPager.getCurrentItem();
                    if (mDeleteClick == true) {
                        String text = s.toString().trim();
                        if (checkedId == 0) {
                            mSessionAdapter.search(text);
                        } else {
                            mMettingAdapter.search(text);
                        }
                        return;
                    }
                    if (checkedId == 0) {
                        mImageView.setHint(R.string.search_session_hint);
                    } else {
                        mImageView.setHint(R.string.search_meeting_hint);
                    }

                    mImageView.setVisibility(View.VISIBLE);
                    hideShurufa();
                    mIncongressEditText.setFocusable(false);
                    mIncongressEditText.setFocusableInTouchMode(false);
                    mDeleteClick = false;
                }

                String text = s.toString().trim();
                int checkedId = mPager.getCurrentItem();
                if (checkedId == 0) {
                    mSessionAdapter.search(text);
                    if (mSessionAdapter.isNoDataShow()) {
                        //显示无数据提示
                        mNoData.setVisibility(View.VISIBLE);
                        mPager.setVisibility(View.GONE);
                    } else {
                        //显示有数据提示
                        mNoData.setVisibility(View.GONE);
                        mPager.setVisibility(View.VISIBLE);
                    }
                } else {
                    mMettingAdapter.search(text);
                    if (mMettingAdapter.isNoDataShow()) {
                        //显示无数据提示
                        mNoData.setVisibility(View.VISIBLE);
                        mPager.setVisibility(View.GONE);
                    } else {
                        //显示有数据提示
                        mNoData.setVisibility(View.GONE);
                        mPager.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        mIncongressEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean focus) {
                if (focus == true) {
                    toggleShurufa();
                    mImageView.setVisibility(View.GONE);
                    search_cancel_text.setVisibility(View.VISIBLE);
                } else {
                    mIncongressEditText.setFocusable(false);
                    mIncongressEditText.setFocusableInTouchMode(false);
                    hideShurufa();
                    mImageView.setVisibility(View.VISIBLE);
                    mDeleteClick = false;
                    search_cancel_text.setVisibility(View.GONE);
                }
            }

        });

        setRoomListAndSesssionList();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_cancel_text:
                mIncongressEditText.setText("");
                hideShurufa();
                mIncongressEditText.setFocusable(false);
                mIncongressEditText.setFocusableInTouchMode(false);
                mDeleteClick = false;
                mImageView.setVisibility(View.VISIBLE);
                search_cancel_text.setVisibility(View.GONE);
                //加入的无数据提示也要去除
                mNoData.setVisibility(View.GONE);
                mPager.setVisibility(View.VISIBLE);
                break;
            case R.id.search_cancel_button:
                mDeleteClick = true;
                mIncongressEditText.setText("");
                //加入的无数据提示也要去除
                mNoData.setVisibility(View.GONE);
                mPager.setVisibility(View.VISIBLE);
                break;
            case R.id.search_search:
                mIncongressEditText.setFocusable(true);
                mIncongressEditText.setFocusableInTouchMode(true);
                mIncongressEditText.requestFocus();
                mIncongressEditText.requestFocusFromTouch();
                break;
        }
    }

    //会议，session内容
    @SuppressLint("ValidFragment")
    public class SessionFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.searchfragment, null);
            ListView listView = (ListView) view.findViewById(R.id.search_session_list);
            listView.setAdapter(mSessionAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    SessionBean session = (SessionBean) mSessionAdapter.getItem(arg2);
                    SessionDetailFragment detail = new SessionDetailFragment();
                    detail.setArguments(getSessionPosition(session.getSessionGroupId()), mSessionList, mRoomsList);
                    action(detail, R.string.meeting_schedule_detail_title, false, false, true);
                }
            });
            return view;
        }
    }

    @SuppressLint("ValidFragment")
    public class MeetingFragment extends Fragment {
        private ListView listView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.searchfragment, null);
            listView = (ListView) view.findViewById(R.id.search_session_list);
            listView.setAdapter(mMettingAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    MeetingBean metting = (MeetingBean) mMettingAdapter.getItem(position);

                    SessionDetailFragment detail = new SessionDetailFragment();
                    detail.setArguments(getSessionPosition(metting.getSessionGroupId()), mSessionList, mRoomsList);
                    action(detail, R.string.meeting_schedule_detail_title, false, false, true);
                }
            });
            return view;
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIncongressEditText.getText().toString().length() > 0) {
                mImageView.setVisibility(View.GONE);
                search_cancel_text.setVisibility(View.VISIBLE);
                mIncongressEditText.setFocusable(false);
                mIncongressEditText.setFocusableInTouchMode(false);
            }
        }
    }

    class MeetingSearchAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 2;
        private String[] mTitles;

        public MeetingSearchAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            this.mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SessionFragment();
            } else
                return new MeetingFragment();
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    /**
     * 获取所有的房间list和会议list
     */
    private void setRoomListAndSesssionList() {
        String sqlclass = "select * from " + ConferenceTables.TABLE_INCONGRESS_CLASS;
        mRoomsList = new ArrayList<>();
        DbAdapter dbAdapter = DbAdapter.getInstance();
        dbAdapter.open();
        mRoomsList.addAll(ConferenceGetData.getClassesList(dbAdapter, sqlclass));//获取会议室
        String sqlSession = "select * from " + ConferenceTables.TABLE_INCONGRESS_SESSION;
        mSessionList = new ArrayList<>();
        mSessionList.addAll(ConferenceGetData.getSessionList(dbAdapter, sqlSession));//获取会议室
        dbAdapter.close();
    }

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
