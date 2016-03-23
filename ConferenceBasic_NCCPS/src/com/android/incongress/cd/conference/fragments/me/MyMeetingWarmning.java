package com.android.incongress.cd.conference.fragments.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.OnWheelChangedListener;
import com.android.incongress.cd.conference.utils.WheelAdapter;
import com.android.incongress.cd.conference.utils.WheelView;

public class MyMeetingWarmning extends BaseFragment implements OnClickListener {

	private WheelView mWheelView;
	private String[] repeateDis;
	private String[] repeateTimes;
	private String[] repeateTimesDis;
	
	private String[] repeateDisValue;
	private String[] repeateTimesValue;
	private String[] repeateTimesDisValue;

	private RelativeLayout mRepeatedisPanel;
	private RelativeLayout mRepeateTimedisPanel;
	private RelativeLayout mrepeateTimesPanel;
	private CheckBox mCheckBox;
	
	private TextView mDis;
	private TextView mTimes;
	private TextView mTimesDis;

	private String[] currentStrings;
	private String[] currentValue;
	private WarnmingWheelAdapter mAdapter;

	private int type = type_dis;
	private static final int type_dis = 1;
	private static final int type_times = 2;
	private static final int type_timeDis = 3;

	private String key_dis = "pre_key_dis";
	private String key_times = "pre_key_times";
	private String key_timesDis = "pre_key_timesDis";
	
	private int index_dis;
	private int index_times;
	private int index_timesDis;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mycenter_warnming, null);
//		MobclickAgent.onEvent(getActivity(),UMengPage.Page_MyMetting_Warming_Setting); //统计页面
		mWheelView = (WheelView) view.findViewById(R.id.mycenter_w_wheel);
		
		repeateDis = getResources().getStringArray(R.array.repeate_dis);
		repeateTimes = getResources().getStringArray(R.array.repeate_times);
		repeateTimesDis = getResources().getStringArray(R.array.repeate_time_dis);
		
		repeateDisValue = getResources().getStringArray(R.array.repeate_dis_value);
		repeateTimesValue = getResources().getStringArray(R.array.repeate_times_value);
		repeateTimesDisValue = getResources().getStringArray(R.array.repeate_time_dis_value);
		
		mCheckBox = (CheckBox) view.findViewById(R.id.mycenter_w_switch);
		mRepeatedisPanel = (RelativeLayout) view.findViewById(R.id.mycenter_w_timem_panel);
		mrepeateTimesPanel = (RelativeLayout) view.findViewById(R.id.mycenter_w_repatetimes_panel);
		mRepeateTimedisPanel = (RelativeLayout) view.findViewById(R.id.mycenter_w_repeate_panel);
		mDis = (TextView) view.findViewById(R.id.mycenter_w_timem);
		mTimesDis = (TextView) view.findViewById(R.id.mycenter_w_repatetimes);
		mTimes = (TextView) view.findViewById(R.id.mycenter_w_repeate);
		
		mRepeatedisPanel.setOnClickListener(this);
		mRepeateTimedisPanel.setOnClickListener(this);
		mrepeateTimesPanel.setOnClickListener(this);
		currentStrings = repeateDis;
		currentValue = repeateDisValue;
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getActivity());
		index_dis = shared.getInt(key_dis, 3);
		index_times = shared.getInt(key_times, 3);
		index_timesDis = shared.getInt(key_timesDis, 2);
		
		mDis.setText(repeateDis[index_dis]);
		mTimesDis.setText(repeateTimes[index_times]);
		mTimes.setText(repeateTimesDis[index_timesDis]);
		mCheckBox.setChecked(shared.getBoolean(AlermClock.KEY_ENABLE, true));
		mWheelView.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String key = null;
				String index_key = null;
				TextView mTextView = null;
				if (type == type_dis) {
					key = AlermClock.KEY_BEFORE;
					index_key = key_dis;
					mTextView = mDis;
				}else if (type == type_times) {
					key = AlermClock.KEY_DISTANCE;
					index_key = key_timesDis;
					mTextView = mTimes;
				}else {
					key = AlermClock.KEY_TIMES;
					index_key = key_times;
					mTextView = mTimesDis;
				}
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				sp.edit().putInt(key, Integer.parseInt(currentValue[newValue])).commit();
				mTextView.setText(currentStrings[newValue]);
				sp.edit().putInt(index_key, newValue).commit();
				System.out.println("currentStrings[newValue]===" + currentStrings[newValue]);
			}
		});
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				sp.edit().putBoolean(AlermClock.KEY_ENABLE, isChecked).commit();
				if(isChecked){
					AlermClock.calculateNextAlert(getActivity());
				}else {
					AlermClock.diasbleClock();
				}
			}
		});
		mAdapter = new WarnmingWheelAdapter();
		mWheelView.setCyclic(true);
		mWheelView.setAdapter(mAdapter);
		mWheelView.setCurrentItem(index_dis);
		mWheelView.setVisibility(View.GONE);
		return view;
	}

	@Override
	public void onClick(View v) {
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getActivity());
		index_dis = shared.getInt(key_dis, 5);
		index_times = shared.getInt(key_times, 3);
		index_timesDis = shared.getInt(key_timesDis, 2);
		int item = 0;
		switch (v.getId()) {
		case R.id.mycenter_w_timem_panel:
			currentStrings = repeateDis;
			currentValue = repeateDisValue;
			type = type_dis;
			item = index_dis;
			break;
		case R.id.mycenter_w_repatetimes_panel:
			currentStrings = repeateTimes;
			currentValue = repeateTimesValue;
			type = type_timeDis;
			item = index_times;
			break;
		case R.id.mycenter_w_repeate_panel:
			currentStrings = repeateTimesDis;
			currentValue = repeateTimesDisValue;
			type = type_times;
			item = index_timesDis;
			break;
		}
		mWheelView.setVisibility(View.VISIBLE);
		mWheelView.setAdapter(mAdapter);
		mWheelView.setCurrentItem(item);
	}

	private class WarnmingWheelAdapter implements WheelAdapter {

		@Override
		public int getItemsCount() {
			return currentStrings.length;
		}

		@Override
		public String getItem(int index) {
			return currentStrings[index];
		}

		@Override
		public int getMaximumLength() {
			return -1;
		}

	}
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_MyMetting_Warming_Setting); //统计页面
	}

	@Override
	public void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_MyMetting_Warming_Setting); //统计页面
	}
}
