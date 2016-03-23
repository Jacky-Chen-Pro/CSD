package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

public class MyScheduleDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<SessionBean> mSesionBeans;
    private List<MeetingBean> mMeetingBeans;
    private List<ClassesBean> mClassesBeans;
    private boolean isAlarmShow = false;

    public MyScheduleDetailAdapter(Context context, List<SessionBean> sesionBeans, List<MeetingBean> meetingBeans, List<ClassesBean> classesBeans) {
        this.mContext = context;
        this.mSesionBeans = sesionBeans;
        this.mMeetingBeans = meetingBeans;
        this.mClassesBeans = classesBeans;
    }

    public void setAlarmShow(boolean isShow) {
        this.isAlarmShow = isShow;
        notifyDataSetChanged();
    }

    public boolean getAlarmMode() {
        return isAlarmShow;
    }

    @Override
    public int getCount() {
        return mSesionBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mSesionBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_schedule, null);
            holder = new ViewHolder();
            holder.tvSessionName = (TextView) convertView.findViewById(R.id.tv_session_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
            holder.llMeetings = (LinearLayout) convertView.findViewById(R.id.ll_time_and_meeting);
            holder.ivAlarm = (ImageView) convertView.findViewById(R.id.iv_alarm);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SessionBean session = mSesionBeans.get(position);
        holder.tvSessionName.setText(session.getSessionName());
        holder.tvTime.setText(session.getStartTime() + " - " + session.getEndTime());

        for (int i = 0; i < mClassesBeans.size(); i++) {
            if (session.getClassesId() == mClassesBeans.get(i).getClassesId()) {
                holder.tvLocation.setText(mClassesBeans.get(i).getClassesCode());
                break;
            }
        }

        holder.llMeetings.removeAllViews();
        for (int i = 0; i < mMeetingBeans.size(); i++) {
            if (mMeetingBeans.get(i).getSessionGroupId() == session.getSessionGroupId()) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_meeting_and_time, null);
                ((TextView) view).setText(mMeetingBeans.get(i).getStartTime() + " - " + mMeetingBeans.get(i).getTopic());
                holder.llMeetings.addView(view);
            }
        }

        if (isAlarmShow) {
            holder.ivAlarm.setVisibility(View.VISIBLE);
        } else {
            holder.ivAlarm.setVisibility(View.GONE);
        }

        holder.ivAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消关注，不过要弹窗提示
                SessionBean temp = mSesionBeans.get(position);

                DbAdapter db = DbAdapter.getInstance();
                db.open();
                ConferenceSetData.updateSessionAttention(db, temp.getSessionGroupId(), Constants.NOATTENTION);
                mSesionBeans.remove(position);
                //移除闹铃
                String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_ALERT + " where " + ConferenceTableField.ALERT_RELATIVEID + " = " + temp.getSessionGroupId();
                List<AlertBean> beans = ConferenceGetData.getAlert(db, sql);
                AlertBean alertForMeeting = beans.size() > 0 ? beans.get(0) : null;
                if (alertForMeeting != null) {
                    ConferenceSetData.disableAlert(db, alertForMeeting);
                }

                List<Integer> positionToRemove = new ArrayList<>();
                for(int j=0; j<mMeetingBeans.size(); j++) {
                    if(mMeetingBeans.get(j).getSessionGroupId() == temp.getSessionGroupId()) {
                        ConferenceSetData.updateMeetingAttention(db, mMeetingBeans.get(j).getMeetingId(), Constants.NOATTENTION);
                        positionToRemove.add(j);
                    }
                }

                for(int i=0; i<positionToRemove.size(); i++) {
                    mMeetingBeans.remove(positionToRemove.get(i));
                }
                db.close();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvSessionName;
        TextView tvTime;
        TextView tvLocation;
        LinearLayout llMeetings;
        ImageView ivAlarm;
    }
}
