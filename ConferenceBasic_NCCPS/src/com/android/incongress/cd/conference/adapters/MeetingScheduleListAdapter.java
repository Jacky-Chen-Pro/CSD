package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Jacky on 2016/1/8.
 */
public class MeetingScheduleListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private List<SessionBean> mSessions;
    private List<ClassesBean> mClassBean;
    private LayoutInflater mInflater;

    //1）sectionIndices数组用来存放每一轮分组的第一个item的位置。
    //2）sectionHeaders数组用来存放每一个分组要展现的数据，
    private int[] mSectionIndices;
    private String[] mSectionHeaders;

    public MeetingScheduleListAdapter(Context ctx, List<SessionBean> session, List<ClassesBean> classes) {
        this.mSessions = session;
        this.mInflater = LayoutInflater.from(ctx);
        this.mClassBean = classes;

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
        int classId = mSessions.get(0).getClassesId();
        secionIndices.add(0);

        for(int i=1; i< mSessions.size(); i++) {
            int tempClassId = mSessions.get(i).getClassesId();

            if(classId != tempClassId) {
                classId = tempClassId;
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
            sectionHeader[i] = mSessions.get(mSectionIndices[i]).getClassesId() + "";
        }

        return sectionHeader;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if(convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.item_headerview_schedule_list, parent, false);
            holder.tvClassRoom = (TextView) convertView.findViewById(R.id.tv_schedule_list_location);
            convertView.setTag(holder);
        }else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        for(int i=0; i<mClassBean.size(); i++) {
            if(mSessions.get(position).getClassesId() == mClassBean.get(i).getClassesId()) {
//                holder.tvClassRoom.setText(StringUtils.getContentByLanguage(mClassBean.get(i).getClassesCode()));
                if(AppApplication.systemLanguage == 1) {
                    holder.tvClassRoom.setText(mClassBean.get(i).getClassesCode());
                }else {
                    holder.tvClassRoom.setText(mClassBean.get(i).getClassesCodeEn());
                }
                break;
            }
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mSessions.get(position).getClassesId();
    }

    @Override
    public int getCount() {
        return mSessions.size();
    }

    @Override
    public Object getItem(int position) {
        return mSessions.get(position);
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
            convertView = mInflater.inflate(R.layout.item_single_schedule_list, parent, false);
            holder.tvMeetingName = (TextView) convertView.findViewById(R.id.schedule_list_title);
            holder.tvMeetingTime = (TextView) convertView.findViewById(R.id.schedule_list_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(AppApplication.systemLanguage == 1) {
            holder.tvMeetingName.setText(mSessions.get(position).getSessionName());
        }else {
            holder.tvMeetingName.setText(mSessions.get(position).getSessionName_En());
        }

        Date date = DateUtil.getDate(mSessions.get(position).getSessionDay(), DateUtil.DEFAULT);
        holder.tvMeetingTime.setText(DateUtil.getDateShort(date)  + " " + mSessions.get(position).getStartTime() + "-" + mSessions.get(position).getEndTime());

        return convertView;
    }

    class HeaderViewHolder {
        TextView tvClassRoom;
    }

    class ViewHolder {
        TextView tvMeetingName;
        TextView tvMeetingTime;
    }
}
