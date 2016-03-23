package com.android.incongress.cd.conference.fragments.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class SettingsShare extends BaseFragment implements OnClickListener {
	String content;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_share, null);
//		MobclickAgent.onEvent(getActivity(),UMengPage.Page_Setting_Share); //统计页面
		content = getResources().getString(R.string.settings_share_content);
		TextView mms = (TextView) view.findViewById(R.id.share_mms);
		TextView mail = (TextView) view.findViewById(R.id.share_mail);
		mms.setOnClickListener(this);
		mail.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.share_mms:
			Uri smsToUri = Uri.parse("smsto:");
			Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
			mIntent.putExtra("sms_body", content);
			startActivity(mIntent);
			break;
		case R.id.share_mail:
			Intent itmail = new Intent(Intent.ACTION_SEND);
			itmail.putExtra(Intent.EXTRA_TEXT, content);
			itmail.setType("text/plain");
			startActivity(itmail);
			break;
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_Setting_Share); //统计页面
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_Setting_Share); //统计页面
	}
}
