package com.android.incongress.cd.conference.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.MySectionIndexer;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView.PinnedHeaderAdapter;


public class MettingCommunitySpeakerAdapter extends BaseAdapter implements PinnedHeaderAdapter, OnScrollListener {
	private List<SpeakerBean> mList;
	private MySectionIndexer mIndexer;
	private int mLocationPosition = -1;
	private List<SpeakerBean> mBackup;
	private List<SpeakerBean> mSearch;
	private int isSearching = View.VISIBLE;

	public MettingCommunitySpeakerAdapter(List<SpeakerBean> mList, MySectionIndexer mIndexer) {
		this.mIndexer = mIndexer;
		this.mList = mList;
		mBackup = new ArrayList<SpeakerBean>(mList);
		mSearch = new ArrayList<SpeakerBean>();
	}

	@Override
	public int getCount() {
		int count=mList.size();
		return count;
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
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = CommonUtils.initView(AppApplication.getContext(), R.layout.hysqhome_jiangzhe_child);

			holder = new ViewHolder();
			holder.group_title = (TextView) view.findViewById(R.id.hysqhome_jiangzhe_group_title);
			holder.name = (TextView) view.findViewById(R.id.hysqhome_jiangzhe_textItem);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		SpeakerBean bean = mList.get(position);
		
		int section = mIndexer.getSectionForPosition(position);
		if (mIndexer.getPositionForSection(section) == position) {
			holder.group_title.setVisibility(isSearching);
			holder.group_title.setText(bean.getFirstLetter());
		} else {
			holder.group_title.setVisibility(View.GONE);
		}
		if(section==0){
			int dataposition=mIndexer.getPositionForSection(section);
		}
		if(AppApplication.systemLanguage == 1) {
			holder.name.setText(bean.getSpeakerName());
		}else {
			holder.name.setText(bean.getEnName());
		}


		return view;
	}

	public static class ViewHolder {
		public TextView group_title;
		public TextView name;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		if (isSearching == View.GONE) {
			return PINNED_HEADER_GONE;
		}
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = mIndexer.getSectionForPosition(realPosition);
		int nextSectionPosition = mIndexer.getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		if (position<0) {
			return;
		}
		int realPosition = position;
		int section = mIndexer.getSectionForPosition(realPosition);
		String title = (String) mIndexer.getSections()[section];
		((TextView) header.findViewById(R.id.hysqhome_jiangzhe_group_textItem)).setText(title);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			PinnedHeaderListView v = (PinnedHeaderListView) view;
			v.configureHeaderView(firstVisibleItem);
		}
	}
	
	public void search(String string){
		mSearch.clear();
		if (string.equals("")) {
			mList = mBackup;
			isSearching = View.VISIBLE;
			notifyDataSetChanged();
			return;
		}
		for (SpeakerBean bean : mBackup) {
			if (bean.getSpeakerName().contains(string)) {
				mSearch.add(bean);
			}
		}
		mList = mSearch;
		isSearching = View.GONE;
		notifyDataSetChanged();
	}
}
