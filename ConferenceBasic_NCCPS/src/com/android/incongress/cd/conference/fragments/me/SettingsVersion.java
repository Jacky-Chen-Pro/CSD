package com.android.incongress.cd.conference.fragments.me;

import java.io.File;

import org.apache.http.Header;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.base.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

public class SettingsVersion extends BaseFragment {
	private TextView current_version;
	private TextView new_version;
	private LinearLayout update_button;
	private TextView update_text;
	private ProgressBar mPb;
	private ProgressBar mPbh;
	private TextView mTv;
	String path = null;// zip包下载到cache的地址
	// 下载百分比
	private int downloadPercent = 0;
	private static final int MSG_DOWNLOADING = 0x0002;
	private static final int MSG_FINISH = 0x0003;
	private static final int MSG_NEW_FILE = 0x0004;
	protected static final int MSG_ERROR = 0x1002;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_version, null);
//		MobclickAgent.onEvent(getActivity(),UMengPage.Page_Setting_Version); //统计页面
		current_version=(TextView)view.findViewById(R.id.settings_version_vsersion);
		new_version=(TextView)view.findViewById(R.id.settings_version_newversion);
		update_button=(LinearLayout)view.findViewById(R.id.settings_version_button);
		update_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mPb.setVisibility(View.VISIBLE);
				mTv.setVisibility(View.VISIBLE);
				UpdateApk();
			}
			
		});
		update_text=(TextView)view.findViewById(R.id.settings_version_text);
	    mPb=(ProgressBar)view.findViewById(R.id.splash_pb);
	    mPbh = (ProgressBar)view.findViewById(R.id.splash_pbh);
		mTv=(TextView)view.findViewById(R.id.splash_text);
		path = AppApplication.instance().getSDPath()+ Constants.DOWNLOADDIR;
		String current_dataversion= AppApplication.instance().getVersionName();
	    String new_dataversion= AppApplication.conBean.getAppVersion();
	    if(new_dataversion==null||"".equals(new_dataversion)){
	    	new_dataversion=current_dataversion;
	    }
		String current_text=String.format(getActivity().
		        getResources().getString(R.string.settings_version_version),current_dataversion);
		String new_text=String.format(getActivity().
		        getResources().getString(R.string.settings_version_newversion),new_dataversion);
		current_version.setText(current_text);
		new_version.setText(new_text);
		if(current_dataversion.equals(new_dataversion)){
			update_button.setBackgroundResource(R.drawable.setting_bg_update_no);
			update_text.setText(R.string.settings_data_noupdate);
			update_button.setClickable(false);
		}else{
			update_button.setBackgroundResource(R.drawable.setting_bg_update_yes);
			update_text.setText(R.string.settings_data_manual);
			update_button.setClickable(true);
		}
		return view;
	}
	
	
	Handler handler = new Handler() {
		private int total = 0;

		private int now = 0;

		public void handleMessage(Message msg) {
			switch (msg.what) {

			case MSG_NEW_FILE:
				now = msg.arg1;
				total = msg.arg2;
				mTv.setText(getString(R.string.splash_downloading,downloadPercent+"")+"%");
				break;

			case MSG_DOWNLOADING:
				if (msg.arg1 != downloadPercent) {
					downloadPercent = msg.arg1;
					mTv.setText(getString(R.string.splash_downloading,downloadPercent+"")+"%");
					mPb.setVisibility(View.INVISIBLE);
					mPbh.setVisibility(View.VISIBLE);
					mPbh.setProgress(downloadPercent);
				}
				break;


			case MSG_FINISH:
				System.out.println("-----finish finish finish----");
				mPb.setVisibility(View.GONE);
				mTv.setVisibility(View.GONE);
				mPbh.setVisibility(View.GONE);
				break;

			case MSG_ERROR: {
				AlertDialog.Builder builder = new AlertDialog.Builder(AppApplication.getContext());
				builder.setTitle("提示信息").setMessage(R.string.incongress_data_update_fail).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UpdateApk();
					}
				}).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).setCancelable(false).show();
			}
				break;
			default:
				break;
			}

		}
	};
	
	private void UpdateApk() {
		try {
			handler.sendMessage(Message.obtain(handler, MSG_NEW_FILE, 1, 1));
			String strUrl = AppApplication.conBean.getUrl();
			// 获取文件名
			String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);

			// 创建文件夹
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// 删除旧文件
			final File f = new File(path + fileName);
			if (f.exists()) {
				f.delete();
			}
			
			AsyncHttpClient httpClient = AppApplication.getHttpClient();
			httpClient.get(getActivity(),strUrl,new FileAsyncHttpResponseHandler(f) {

						@Override
						public void onStart() {
							System.out.println("cccc onStart "+f.getName());
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, File file) {
							handler.sendEmptyMessage(MSG_ERROR);
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								File file) {
							handler.sendEmptyMessage(MSG_FINISH);
							Intent intent = new Intent();
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(file),
									"application/vnd.android.package-archive");
							startActivity(intent);
						}
						@Override
						public void onProgress(int bytesWritten, int totalSize) {
							int percent = (int) ((float) bytesWritten / totalSize * 100);
							handler.sendMessage(Message.obtain(handler, MSG_DOWNLOADING,
									percent, 0));
							super.onProgress(bytesWritten, totalSize);
						}
					});
			
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(MSG_ERROR);
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_Setting_Version); //统计页面
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_Setting_Version); //统计页面
	}
}
