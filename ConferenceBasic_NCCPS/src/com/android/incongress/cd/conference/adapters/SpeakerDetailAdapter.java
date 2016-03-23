package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Jacky on 2016/1/8.
 */
public class SpeakerDetailAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private List<MeetingBean_new> mMeetings;
    private LayoutInflater mInflater;

    //1）sectionIndices数组用来存放每一轮分组的第一个item的位置。
    //2）sectionHeaders数组用来存放每一个分组要展现的数据，
    private int[] mSectionIndices;
    private String[] mSectionHeaders;
    private boolean mIsFromSessionDetail;

    public SpeakerDetailAdapter(Context ctx, List<MeetingBean_new> meetings, boolean isFromSessionDetail) {
        this.mMeetings = meetings;
        this.mInflater = LayoutInflater.from(ctx);
        this.mIsFromSessionDetail = isFromSessionDetail;

        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
    }


    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    /**
     * sectionIndices数组用来存放每一轮分组的第一个item的位置。
     * @return
     */
    private int[] getSectionIndices() {
        List<Integer> secionIndices = new ArrayList<Integer>();
        int lastRoleId = mMeetings.get(0).getRoleId();
        secionIndices.add(0);

        for(int i=1; i<mMeetings.size(); i++) {
            int tempRoleId = mMeetings.get(i).getRoleId();

            if(lastRoleId != tempRoleId) {
                lastRoleId = tempRoleId;
                secionIndices.add(i);
            }
        }

        int[] secions = new int[secionIndices.size()];

        for(int i=0; i<secionIndices.size(); i++) {
            secions[i] = secionIndices.get(i);
        }
        return secions;
    }

    /**
     * header中存放的数据
     * @return
     */
    private String[] getSectionHeaders(){
        String[] sectionHeader = new String[mSectionIndices.length];
        for(int i=0; i< mSectionIndices.length; i++) {
            sectionHeader[i] = mMeetings.get(mSectionIndices[i]).getRoleId()+"";
        }

        return sectionHeader;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if(convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.speaker_headerview, parent, false);
            holder.tvRoleName = (TextView) convertView.findViewById(R.id.tv_role_name);
            convertView.setTag(holder);
        }else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String headerText;
        if(AppApplication.systemLanguage == 1) {
            headerText = mMeetings.get(position).getRoleName();
        }else {
            headerText = mMeetings.get(position).getRoleNameEn();
        }

        holder.tvRoleName.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mMeetings.get(position).getRoleId();
    }

    @Override
    public int getCount() {
        return mMeetings.size();
    }

    @Override
    public Object getItem(int position) {
        return mMeetings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_speaker_role_meeting, parent, false);
            holder.tvMeetingName = (TextView) convertView.findViewById(R.id.tv_meeting_name);
            holder.tvMeetingTime = (TextView) convertView.findViewById(R.id.tv_meeting_time);
            holder.tvMeetingLocation = (TextView) convertView.findViewById(R.id.tv_meeting_room);
            holder.llNext = (LinearLayout) convertView.findViewById(R.id.ll_next);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(AppApplication.systemLanguage == 1) {
            holder.tvMeetingName.setText(mMeetings.get(position).getTopic());
            holder.tvMeetingLocation.setText(mMeetings.get(position).getClassName());

            Date date = DateUtil.getDate(mMeetings.get(position).getMeetingDay(), DateUtil.DEFAULT);
            holder.tvMeetingTime.setText(DateUtil.getDateString(date, DateUtil.DEFAULT_CHINA_TWO)  + " " + mMeetings.get(position).getStartTime() + "-" + mMeetings.get(position).getEndTime());
        }else  {
            holder.tvMeetingName.setText(mMeetings.get(position).getTopic_En());
            holder.tvMeetingLocation.setText(mMeetings.get(position).getClassEnName());

            Date date = DateUtil.getDate(mMeetings.get(position).getMeetingDay(), DateUtil.DEFAULT);
            holder.tvMeetingTime.setText(DateUtil.getDateShort(date)  + " " + mMeetings.get(position).getStartTime() + "-" + mMeetings.get(position).getEndTime());
        }

        if(mIsFromSessionDetail) {
            holder.llNext.setVisibility(View.GONE);
        }else {
            holder.llNext.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class HeaderViewHolder {
        TextView tvRoleName;
    }

    class ViewHolder {
        TextView tvMeetingName;
        TextView tvMeetingTime;
        TextView tvMeetingLocation;
        LinearLayout llNext;
    }
}
