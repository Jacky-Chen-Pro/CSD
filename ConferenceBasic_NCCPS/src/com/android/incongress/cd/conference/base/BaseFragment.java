package com.android.incongress.cd.conference.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.utils.ToastUtils;

public class BaseFragment extends Fragment{

	/** 没有数据 **/
	public static final int HANDLER_NO_DATA = 0x0001;
	/** 第一次获取数据 **/
	public static final int HANDLER_DATA_FIRST = 0x0002;
	/** 获取更多数据 **/
	public static final int HANDLER_DATA_MORE = 0x0003;
	
	public MainCallBack mCallBack;

	public interface MainCallBack{
		void onCreateViewFinish();
	}
	
	public void setCallBack(MainCallBack mCallBack){
		this.mCallBack = mCallBack;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (mCallBack!=null) {
			mCallBack.onCreateViewFinish();
		}
	}
	public void action(BaseFragment fragment, int id,boolean adTop,boolean adBottom,boolean refresh) {
		HomeActivity activity = (HomeActivity) getActivity();
		activity.addFragment(this,fragment,refresh);
		activity.setTitleEntry(true, false, false, null, id, adTop, adBottom, true);
	}
	
	public void action(BaseFragment fragment, int id,boolean adTop,boolean adBottom) {
		HomeActivity activity = (HomeActivity) getActivity();
		activity.addFragment(this,fragment);
		activity.setTitleEntry(true, false, false, null, id,adTop,adBottom,true);

	}
	
	public void action(BaseFragment fragment, String title,boolean adTop,boolean adBottom) {
		HomeActivity activity = (HomeActivity) getActivity();
		activity.addFragment(this, fragment);
		activity.setTitleEntry(true, false, false, null, title, adTop, adBottom, true);
	}
	
	public void action(BaseFragment fragment, String title,View view, boolean adTop,boolean adBottom) {
		HomeActivity activity = (HomeActivity) getActivity();
		activity.addFragment(this, fragment);
		activity.setTitleEntry(true, false, true, view, title, adTop, adBottom, true);
	}
	
	public void action(BaseFragment fragment, int id, View view,boolean adTop,boolean adBottom,boolean refresh) {
		HomeActivity activity = (HomeActivity) getActivity();
		activity.addFragment(this,fragment,refresh);
		activity.setTitleEntry(true, false, true, view, id, adTop, adBottom, true);
	}
	
	public void action(BaseFragment fragment, int id, View view,boolean adTop,boolean adBottom) {
		HomeActivity activity = (HomeActivity) getActivity();
		activity.addFragment(this,fragment);
		activity.setTitleEntry(true, false, true, view, id, adTop, adBottom, true);
	}
	
	public void action(BaseFragment fragment, int id,boolean adTop,boolean adBottom,int jacky) {
		HomeActivity activity = (HomeActivity) getActivity();
		activity.addFragment(fragment, false);
		activity.setTitleEntry(true, false, true, null, id, adTop, adBottom, true);
	}
	
	public void performback(){
		HomeActivity activity = (HomeActivity) getActivity();
		activity.perfromBackPressTitleEntry();
	}
	public void hideShurufa(){
		HomeActivity activity = (HomeActivity) getActivity();
		activity.hideShurufa();
	}
	public boolean toggleShurufa(){
		HomeActivity activity = (HomeActivity) getActivity();
		return activity.toggleShurufa();
	}
	
	public void showreglogindialog() {
		ToastUtils.showShorToast("showreglogindialog===from BaseFragment");
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
		     if (i == 0) {
		    	 listItem.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
		      }
		     listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	/**
	 * 内容区域变量
	 */
	protected void lightOn() {
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = 1.0f;
		getActivity().getWindow().setAttributes(lp);
	}

	/**
	 * 内容区域变暗
	 */
	protected void lightOff() {
		WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
		lp.alpha = 0.3f;
		getActivity().getWindow().setAttributes(lp);
	}
}
