package com.android.incongress.cd.conference.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.TipBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.uis.IncongressTextView;
import com.android.incongress.cd.conference.utils.CommonUtils;

public class TipsInformationAdapter extends BaseExpandableListAdapter {
	private List<TipBean> mTipsInfList=new ArrayList<TipBean>();
    private Context mContext;
	public TipsInformationAdapter(Context mContext){
		this.mContext=mContext;
        String sql = "select *  from " + ConferenceTables.TABLE_INCONGRESS_TIPS;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        mTipsInfList.addAll(ConferenceGetData.getTipsList(ada, sql));
        ada.close();
	}
	
	private static class groupHolder {
		IncongressTextView tv;
		CheckBox checkbox;
	}

	private static class childHolder {
		IncongressTextView tvtitle;
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		TipBean bean = mTipsInfList.get(groupPosition);
		return bean;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		childHolder holder = null;
		
		if (convertView == null) {
			convertView = CommonUtils.initView(mContext, R.layout.tips_information_child);
			holder = new childHolder();
			holder.tvtitle = (IncongressTextView) convertView.findViewById(R.id.tips_information_content);
			convertView.setTag(holder);
		} else {
			holder = (childHolder) convertView.getTag();
		}
		
		TextView textview= holder.tvtitle;
		TipBean bean=mTipsInfList.get(groupPosition);
		
		if(AppApplication.systemLanguage==1){
			textview.setText(bean.getTipsContent());
		}else if(AppApplication.systemLanguage==2){
			if(bean.getTipsContent_En()!=null&&!"".equals(bean.getTipsContent_En())){
				textview.setText(bean.getTipsContent_En());
			}else{
				textview.setText(bean.getTipsContent());
			}
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mTipsInfList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mTipsInfList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		groupHolder holder = null;
		
		if (convertView == null) {
			convertView = CommonUtils.initView(mContext, R.layout.tips_information_group);
			holder = new groupHolder();
			holder.tv = (IncongressTextView) convertView.findViewById(R.id.tip_inf_group_title);
			holder.checkbox=(CheckBox)convertView.findViewById(R.id.tip_inf_group_checked);
			convertView.setTag(holder);
		} else {
			holder = (groupHolder) convertView.getTag();
		}
		
		TipBean bean=mTipsInfList.get(groupPosition);
		if(AppApplication.systemLanguage==1){
			holder.tv.setText(bean.getTipsTitle());
		}else if(AppApplication.systemLanguage==2){
			if(bean.getTipsTitle_En()!=null&&!"".equals(bean.getTipsTitle_En())){
				holder.tv.setText(bean.getTipsTitle_En());
			}else{
				holder.tv.setText(bean.getTipsTitle());
			}
		}
		if(isExpanded){
			holder.checkbox.setChecked(true);
		}else{
			holder.checkbox.setChecked(false);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
