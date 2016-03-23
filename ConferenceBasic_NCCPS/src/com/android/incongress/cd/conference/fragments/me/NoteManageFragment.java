package com.android.incongress.cd.conference.fragments.me;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleFragment;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.adapters.MyMeetingNotesAdapter;
import com.android.incongress.cd.conference.beans.Notes;
import com.android.incongress.cd.conference.utils.BaseSwipeListViewListener;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.SwipeListView;

/**
 * 笔记管理界面
 */
public class NoteManageFragment extends BaseFragment implements OnClickListener {

    private SwipeListView mListView;
    private MyMeetingNotesAdapter mAdapter;
    private TextView mTitleView;
    private int mode = MODE_NORMAL;
    public static final int MODE_MANAGE = 1;
    public static final int MODE_NORMAL = 0;
    private TextView mTvTips;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mycenter_note, null);

        mListView = (SwipeListView) view.findViewById(R.id.mycenter_note_list);
        mListView.setSwipeMode(SwipeListView.SWIPE_ACTION_NONE);
        mAdapter = new MyMeetingNotesAdapter(getActivity(), mListView, getActivity());
        mListView.setSwipeCloseAllItemsWhenMoveList(true);
        mListView.setAdapter(mAdapter);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);

        if (mAdapter.getCount() == 0) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle("提示信息").setMessage(R.string.mymeeting_no_note).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    action(new MeetingScheduleFragment(), R.string.home_icon_hyrc, false, true, true);
//                }
//            }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            }).setCancelable(false).show();
            mTvTips.setVisibility(View.VISIBLE);
        }

        mListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onClickBackView(int position) {
                mListView.closeOpenedItems();
                Notes notes = (Notes) mAdapter.getItem(position);
                MyMeetingNoteEditor editor = new MyMeetingNoteEditor();
                editor.setDatasource(notes);
                View view = CommonUtils.initView(NoteManageFragment.this.getActivity(), R.layout.hysqhome_shuoliangju_nav_rightbtn);
                TextView mText = (TextView) view.findViewById(R.id.hysq_jiangliangju_titlebar_send);
                mText.setText(R.string.mymeeting_finish);
                mText.setOnClickListener(editor);
                action(editor, R.string.mymeeting_note_title, view, false, false);
                super.onClickBackView(position);
            }

            @Override
            public void onClickFrontView(int position) {
                mListView.closeOpenedItems();
                Notes notes = (Notes) mAdapter.getItem(position);
                MyMeetingNoteEditor editor = new MyMeetingNoteEditor();
                editor.setDatasource(notes);
                View view = CommonUtils.initView(NoteManageFragment.this.getActivity(), R.layout.hysqhome_shuoliangju_nav_rightbtn);
                TextView mText = (TextView) view.findViewById(R.id.hysq_jiangliangju_titlebar_send);
                mText.setText(R.string.mymeeting_finish);
                mText.setOnClickListener(editor);
                action(editor, R.string.mymeeting_note_title, view, false, false);
                super.onClickBackView(position);

            }
        });
        return view;
    }

    public void setTitleView(TextView mTextView) {
        this.mTitleView = mTextView;
        mTitleView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mode == MODE_NORMAL) {
            if(mAdapter.getCount() == 0) {
                return;
            }

            mTitleView.setText(R.string.mymeeting_finish);
            mode = MODE_MANAGE;
            mAdapter.setMode(mode);
            mListView.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
        } else {
            mTitleView.setText(R.string.mymeeting_manage);
            mode = MODE_NORMAL;
            mAdapter.setMode(mode);
            mListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
        }
        mListView.closeOpenedItems();
    }
}
