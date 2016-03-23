package com.android.incongress.cd.conference.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.utils.CommonUtils;

public class MettingSpeechAdapter extends BaseAdapter {

    private List<MeetingBean> datasource = new ArrayList<MeetingBean>();

    public MettingSpeechAdapter(int sessionGroupId) {
        String sql = "select *  from " + ConferenceTables.TABLE_INCONGRESS_MEETING
        +" where "+ ConferenceTableField.MEETING_SESSIONGROUPID+" ="+sessionGroupId;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        datasource.addAll(ConferenceGetData.getMeetingList(ada, sql));
        ada.close();
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.metting_schedule_detail_speech);
            holder = new Holder();
            TextView timeView = (TextView) convertView.findViewById(R.id.metting_schedule_details_speech_time);
            TextView titleView = (TextView) convertView.findViewById(R.id.metting_schedule_details_speech_title);
            TextView speakerView=(TextView)convertView.findViewById(R.id.metting_schedule_details_speech_speaker);
            CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.metting_schedule_details_child_star);
            LinearLayout checkLayout=(LinearLayout)convertView.findViewById(R.id.metting_schedule_details_child_star_layout);
            holder.timeView = timeView;
            holder.titleView = titleView;
            holder.speakerView=speakerView;
            holder.checkBox=checkBox;
            holder.checkLayout=checkLayout;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final MeetingBean bean=datasource.get(position);
        holder.timeView.setText(bean.getStartTime()+"-"+bean.getEndTime());
        //因为speaker会有多个 而需要在一个发言里面显示 所以这里不能用in
		List<SpeakerBean> speakerlist = new ArrayList<SpeakerBean>();
		String speakerids = bean.getSpeakerIds();
		if (speakerids != null && !speakerids.equals("")) {
			String sql = "select * from "
					+ ConferenceTables.TABLE_INCONGRESS_SPEAKER + " where "
					+ ConferenceTableField.SPEAKER_SPEAKERID + " in("
					+ speakerids + ")";
			DbAdapter ada = DbAdapter.getInstance();
			ada.open();
			speakerlist.addAll(ConferenceGetData.getSpeakersList(ada, sql));
			ada.close();
		}
		String speakers="";
		
        if(AppApplication.systemLanguage==1){
        	 holder.titleView.setText(bean.getTopic());
        	 
        	for(int i=0;i<speakerlist.size();i++){
     			SpeakerBean speaker=speakerlist.get(i);
     			speakers=speakers+" "+speaker.getSpeakerName();
     		}
        }else if(AppApplication.systemLanguage==2){
        	if(bean.getTopic_En()!=null&&!"".equals(bean.getTopic_En())){
        		holder.titleView.setText(bean.getTopic_En());
        	}else{
        		holder.titleView.setText(bean.getTopic());
        	}
        	for(int i=0;i<speakerlist.size();i++){
      			SpeakerBean speaker=speakerlist.get(i);
      			speakers=speakers+" "+speaker.getEnName();
      		}
        }
        
        holder.speakerView.setText(speakers);
        
        if(bean.getAttention()== Constants.SMNOATTENTION){
        	bean.setChecked(false);
        }
        if(bean.getAttention()== Constants.SMATTENTION){
        	bean.setChecked(true);
        }
        holder.checkBox.setChecked(bean.isChecked());
		holder.checkLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				CheckBox checkbox=(CheckBox)v.findViewById(R.id.metting_schedule_details_child_star);
				if (mListener != null) {
					checkbox.setChecked(!bean.isChecked());
					mListener.onCheck(bean,!bean.isChecked(),checkbox);
				}
			}
		});
		holder.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (mListener != null) {
					mListener.onCheck(bean, !bean.isChecked(),(CheckBox)v);
				}
			}
		});
        return convertView;
    }

    public void setDataSource(List<MeetingBean> list) {
        datasource.clear();
        datasource.addAll(list);
        this.notifyDataSetChanged();
    }
    
    public List<MeetingBean> getDataSource(){
        return datasource;
    }

    private class Holder {
    	TextView timeView;
        TextView titleView;
        TextView speakerView;
        CheckBox checkBox;
        LinearLayout checkLayout;
    }
	public interface StartCheckListener {
		public void onCheck(MeetingBean been, boolean check,CheckBox view);
	}
    private StartCheckListener mListener;
	public void setStartCheckListener(StartCheckListener listener) {
		mListener = listener;
	}
}
