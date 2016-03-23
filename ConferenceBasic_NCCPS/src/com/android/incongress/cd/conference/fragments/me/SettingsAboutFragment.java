package com.android.incongress.cd.conference.fragments.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class SettingsAboutFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_about, null);
		return view;
	}

}
