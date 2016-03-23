package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2016/1/18.
 * 关注的会议列表[meeting]
 */
public class AttentionPopupAdapter extends BaseAdapter {

    private List<MeetingBean> mMeetings;
    private Context mContext;

    public AttentionPopupAdapter(List<MeetingBean> mMeetings, Context mContext) {
        this.mMeetings = mMeetings;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_attention_popup,null);
            holder = new ViewHolder();
            holder.ivAttention = (ImageView) convertView.findViewById(R.id.iv_attention);
            holder.tvMeeting = (TextView) convertView.findViewById(R.id.tv_meeting);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        MeetingBean bean = mMeetings.get(position);
        holder.tvMeeting.setText(bean.getStartTime() + " - " + bean.getTopic());

        if(bean.getAttention() == Constants.ATTENTION) {
            holder.ivAttention.setImageResource(R.drawable.bt_collected);
        }else if(bean.getAttention() == Constants.NOATTENTION) {
            holder.ivAttention.setImageResource(R.drawable.bt_uncollected);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ivAttention;
        TextView tvMeeting;
    }
}
