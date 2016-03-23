package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.media.Image;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerDetailFragment;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/7.
 */
public class MeetingWithSpeakerAdapter extends BaseAdapter {
    private List<MeetingBean> mMeetings;
    private List<List<SpeakerBean>> mSpeakers;
    private Context mContext;
    private boolean mIsAlarmMode = false;
    private OnTagListener mOnTagListner;

    private SessionAlarmListener mSessionAlarmListener;

    public MeetingWithSpeakerAdapter(Context ctx, List<MeetingBean> meetings, List<List<SpeakerBean>> speakers, OnTagListener listener, SessionAlarmListener sessionListener) {
        this.mContext = ctx;
        this.mMeetings = meetings;
        this.mSpeakers = speakers;
        this.mOnTagListner = listener;
        this.mSessionAlarmListener = sessionListener;
    }

    public List<MeetingBean> getMeetingBeanList() {
        return mMeetings;
    }

    public void setAlarmMode(boolean isAlarmMode) {
        this.mIsAlarmMode = isAlarmMode;
        notifyDataSetChanged();
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
        final ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_meeeting_with_flow,null);
            holder.tvMeetingName = (TextView) convertView.findViewById(R.id.tv_session_name);
            holder.tflNames = (TagFlowLayout) convertView.findViewById(R.id.tfl_names);
            holder.ivAlarm = (ImageView) convertView.findViewById(R.id.iv_alarm);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MeetingBean bean = mMeetings.get(position);
        SpannableString sp = null;
        if(AppApplication.systemLanguage == 1) {
            sp = new SpannableString(bean.getStartTime() + " - " + bean.getTopic());
        }else {
            sp = new SpannableString(bean.getStartTime() + " - " + bean.getTopic_En());
        }
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
        holder.tvMeetingName.setText(sp);

        final SpeakerTagAdapter adapter = new SpeakerTagAdapter(mContext, mSpeakers.get(position));
        holder.tflNames.setAdapter(adapter);
        holder.tflNames.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                SpeakerBean bean = adapter.getSpeakerList().get(position);
                mOnTagListner.tagListener(bean);
                return true;
            }
        });

        if(mIsAlarmMode) {
            holder.ivAlarm.setVisibility(View.VISIBLE);
        }
        else {
            holder.ivAlarm.setVisibility(View.GONE);
        }

        if(bean.getAttention() == Constants.ATTENTION) {
            holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmon);
        }else {
            holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);
        }

        holder.ivAlarm.setTag(position);
        holder.ivAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer)v.getTag();

                if(mMeetings.get(position).getAttention() == 0) {
                    mMeetings.get(position).setAttention(1);
                    holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmon);

                    //设置该meeting的关注，脑中不用理会
                    DbAdapter ada = DbAdapter.getInstance();
                    ada.open();
                    ConferenceSetData.updateMeetingAttention(ada, bean.getMeetingId(), Constants.ATTENTION);
                    ada.close();
                }else {
                    mMeetings.get(position).setAttention(0);
                    holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);

                    //设置该meeting的关注，脑中不用理会
                    DbAdapter ada = DbAdapter.getInstance();
                    ada.open();
                    ConferenceSetData.updateMeetingAttention(ada, bean.getMeetingId(), Constants.NOATTENTION);
                    ada.close();
                }

                //一个都没有关注
                boolean isAllAttetion = true;
                for(int i=0; i<mMeetings.size(); i++) {
                    if(mMeetings.get(i).getAttention() == 0) {
                        isAllAttetion = false;
                        break;
                    }
                }

                if(isAllAttetion) {
                    mSessionAlarmListener.doWhenMeetingAlarmClicked(true);
                }else {
                    mSessionAlarmListener.doWhenMeetingAlarmClicked(false);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView ivAlarm;
        TextView tvMeetingName;
        TagFlowLayout tflNames;
    }

    public interface OnTagListener{
        void tagListener(SpeakerBean bean);
    }

  public interface SessionAlarmListener {
      void doWhenMeetingAlarmClicked(boolean sessionAlarmToggle);
  }
}
