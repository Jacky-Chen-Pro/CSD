package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.adapters.TipsClassesAdapter;
import com.android.incongress.cd.conference.adapters.TipsInformationAdapter;
import com.android.incongress.cd.conference.beans.MapBean;
import com.android.incongress.cd.conference.uis.IconPagerAdapter;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * 参会指南
 * @author Jacky_Chen
 * @time 2014年11月25日
 *
 */
public class MeetingGuideFragment extends BaseFragment {
	
	private static final int[] CONTENT = new int[] { R.string.tips_title_information, R.string.tips_title_classes };
	private static final int[] ICONS = new int[] { R.drawable.icon_tips_information, R.drawable.icon_tips_classes };
	    
    private TipsInformationAdapter tips_inf_adapter;
    private TipsClassesAdapter tips_classes_adapter;
	private SharedPreferences preferences;
	private TabLayout mTabLayout;
	
	@Override
	public void onStart() {
		super.onStart();
		Editor editor = preferences.edit();
		editor.putBoolean(Constants.PREFERENCE_MODULE_CHZN_VISIBLE_NEW, false);
		editor.commit();
		AppApplication.moduleBean.setVisiblechzn(false);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		preferences=PreferenceManager.getDefaultSharedPreferences(getActivity());

		View view=inflater.inflate(R.layout.tips_listview, null);
		tips_inf_adapter=new TipsInformationAdapter(getActivity());
		tips_classes_adapter=new TipsClassesAdapter();
		FragmentStatePagerAdapter adapter = new TipsPagerAdapter(getFragmentManager());
        final ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

		mTabLayout = (TabLayout)view.findViewById(R.id.tablayout);
		mTabLayout.setTabMode(TabLayout.MODE_FIXED);
		mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		mTabLayout.setupWithViewPager(pager);

		return view;
	}
 
    class TipsPagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
		
        public TipsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	if (position == 0) {
				return new TipsFragment();
			}else
				return new ClassFragment();
        	
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(CONTENT[position % CONTENT.length]);
        }

        @Override 
        public int getIconResId(int index) {
          return ICONS[index];
        }
      
        @Override
        public int getCount() {
          return CONTENT.length;
        }
    }
	
	@SuppressLint("ValidFragment")
	class TipsFragment extends Fragment{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.tipsfragment, null);
			ExpandableListView tips_inf_list=(ExpandableListView)view.findViewById(R.id.tips_information_list);
			if (Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB) {
				tips_inf_list.setChildDivider(new ColorDrawable(getResources().getColor(R.color.dividerColor)));
			}
			tips_inf_list.setGroupIndicator(null);
			tips_inf_list.setAdapter(tips_inf_adapter);
			return view;
		}
	}
	
	@SuppressLint("ValidFragment")
	class ClassFragment extends Fragment{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View view = inflater.inflate(R.layout.searchfragment, null);
			ListView listView = (ListView) view.findViewById(R.id.search_session_list);
			listView.setAdapter(tips_classes_adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					MapBean bean=(MapBean)tips_classes_adapter.getItem(position);
					MeetingGuideRoomMapFragment fragment=new MeetingGuideRoomMapFragment();
				    String filepath= AppApplication.instance().getSDPath() + Constants.FILESDIR+bean.getMapUrl();
					fragment.setFilePath(filepath);
				    action(fragment,bean.getMapRemark(),false,false);
				    
				}
				
			});
			return view;
		}
	}
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_Guide); //统计页面
	}

	@Override
	public void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_Guide); //统计页面
	}
}
