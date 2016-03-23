package com.android.incongress.cd.conference.adapters;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.CommunityTopicContentBean;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.EmotionsUtils;

public class MeetingCommunityCommentsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<CommunityTopicContentBean> mList;
	public MeetingCommunityCommentsAdapter(LayoutInflater mInflater,List<CommunityTopicContentBean> mList){
		this.mInflater=mInflater;
		this.mList=mList;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder=null;
	//	if(convertView==null){
			convertView=mInflater.inflate(R.layout.hysqhome_comments_item, null);
			holder=new Holder();
			holder.author=(TextView)convertView.findViewById(R.id.hysq_home_tiezi_author);
			holder.time=(TextView)convertView.findViewById(R.id.hysq_home_tiezi_time);
			holder.message=(TextView)convertView.findViewById(R.id.hysq_home_tiezi_message);
/*		    convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}*/
		CommunityTopicContentBean bean=mList.get(position);
		holder.author.setText(bean.getUserName());
		holder.time.setText(CommonUtils.CommunityTimeCompare(bean.getTime()));
		EmotionsUtils emou = EmotionsUtils.getInstance();
		holder.message.setText(emou.formatMessage(bean.getContent()));
		return convertView;
	}
	private static class Holder{
		TextView author;
		TextView time;
		TextView message;
	}
	public void setContent(List<CommunityTopicContentBean> list){
		mList=list;
	}
}
