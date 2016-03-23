package com.android.incongress.cd.conference.fragments.me;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.VersionBean;
import com.android.incongress.cd.conference.data.EasyConDb;
import com.android.incongress.cd.conference.utils.FileUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

public class SettingsDatabaseFragment extends BaseFragment {
	private static final int MSG_DOWNLOADING = 0x0002;
	private static final int MSG_FINISH = 0x0003;
	private static final int MSG_NEW_FILE = 0x0004;
	private static final int MSG_DOWNLOADED = 0x0005;
	private static final int MSG_UPDATEVERSION = 0x0006;
	private static final int MSG_DOWNLOADING_ZIP = 0x0007;
	protected static final int MSG_ERROR = 0x1002;

	// 下载百分比
	private int downloadPercent = 0;
	String path = null;// zip包下载到cache的地址
	String filespath = null;// zip包解析的地址
	// 数据包下载地址列表
	private List<VersionBean> zipList =new ArrayList<VersionBean>();
	TextView current_version;
	TextView new_version;
	LinearLayout update_button;
	TextView update_text;
	ProgressBar mPb;
    ProgressBar mPbh;
	TextView mTv;
	SharedPreferences preferences;
	int new_dbVersion = 0 ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_database, null);
//		MobclickAgent.onEvent(getActivity(),UMengPage.Page_Setting_Database); //统计页面
	    current_version=(TextView)view.findViewById(R.id.settings_data_vsersion);
	    new_version=(TextView)view.findViewById(R.id.settings_data_newversion);
		update_button=(LinearLayout)view.findViewById(R.id.settings_data_button);
	    update_text=(TextView)view.findViewById(R.id.setting_data_text);
	    mPb=(ProgressBar)view.findViewById(R.id.splash_pb);
	    mPbh = (ProgressBar)view.findViewById(R.id.splash_pbh);
		mTv=(TextView)view.findViewById(R.id.splash_text);
	    preferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
		path = AppApplication.instance().getSDPath()+ Constants.DOWNLOADDIR;
        filespath = AppApplication.instance().getSDPath()+ Constants.FILESDIR;
	    final int current_dbVersion = preferences.getInt(Constants.PREFERENCE_DB_VERSION, 1);
		new_dbVersion=0;
		final List<VersionBean> versionlist= AppApplication.conBean.getVersionList();
		if(versionlist!=null&&versionlist.size()>0){
			VersionBean bean=versionlist.get(versionlist.size()-1);
			new_dbVersion=bean.getVersion();
		}else{
			new_dbVersion=current_dbVersion;
		}
		String current_text= String.format(getActivity().
				getResources().getString(R.string.settings_data_version),current_dbVersion*1.0f);
		String new_text= String.format(getActivity().
				getResources().getString(R.string.settings_data_newversion),new_dbVersion*1.0f);
		current_version.setText(current_text);
		new_version.setText(new_text);
		if(current_dbVersion==new_dbVersion){
			update_button.setBackgroundResource(R.drawable.setting_bg_update_no);
			update_text.setText(R.string.settings_data_noupdate);
			update_button.setClickable(false);
		}else{
			update_button.setBackgroundResource(R.drawable.setting_bg_update_yes);
			update_text.setText(R.string.settings_data_manual);
			update_button.setClickable(true);
			update_button.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					update_button.setClickable(false);
					update_button.setBackgroundResource(R.drawable.setting_bg_update_no);
					for(int i=0;i<versionlist.size();i++){
						VersionBean bean=versionlist.get(i);
						if(bean.getVersion()<current_dbVersion){
							continue;
						}
						zipList.add(bean);
					}
					mPb.setVisibility(View.VISIBLE);
					mTv.setVisibility(View.VISIBLE);
					UpdateZip(0);
				}
				
			});
		}
		return view;
	}

	private void UpdateZip(final int index) {
		try {
			String strUrl = null;
			final VersionBean response = zipList.get(index);
			handler.sendMessage(Message.obtain(handler, MSG_NEW_FILE,
					(index + 1), zipList.size()));
			strUrl = Constants.get_NEWSPREFIX() + response.getZipUrl();
			// 获取文件名
			String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
			// 创建文件夹
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 删除旧文件
			File f = new File(path + fileName);
			if (f.exists()) {
				f.delete();
			}
			AsyncHttpClient httpClient = AppApplication.getHttpClient();
			httpClient.get(getActivity(), strUrl,
					new FileAsyncHttpResponseHandler(f) {
						@Override
						public void onProgress(int bytesWritten, int totalSize) {
							if(zipList.size()==1){
								int percent = (int) ((float) bytesWritten / totalSize * 100);
								handler.sendMessage(Message.obtain(handler, MSG_DOWNLOADING,
										percent, 0));
							}
							super.onProgress(bytesWritten, totalSize);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, File file) {
							handler.sendEmptyMessage(MSG_ERROR);
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								File file) {
							FileInputStream zis;
							try {
								zis = new FileInputStream(file);
								FileUtils.unZip(zis, filespath);
								Editor editor = preferences.edit();
								editor.putInt(
										Constants.PREFERENCE_DB_VERSION,
										response.getVersion());
								editor.commit();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							if(zipList.size()>1){
								handler.sendMessage(Message.obtain(handler, MSG_DOWNLOADING_ZIP, index,
										zipList.size()));
							}else{
								handler.sendEmptyMessage(MSG_DOWNLOADED);
								EasyConDb.createDb(filespath, true);
								Log.d("cc11","finish cancle");
								handler.sendEmptyMessage(MSG_FINISH);
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(MSG_ERROR);
		}
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
				mPb.setVisibility(View.INVISIBLE);
				mPbh.setVisibility(View.VISIBLE);
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
			case MSG_DOWNLOADING_ZIP:
				int curent = msg.arg1;
				int totalsize = msg.arg2;
				downloadPercent = Math.round(curent*100.0f/(total-1)*1.0f+0.5f);
				if(downloadPercent>100){
					downloadPercent=100;
				}
				mTv.setText(getString(R.string.splash_downloading,downloadPercent+"")+"%");
				mPbh.setProgress(downloadPercent);
				mPbh.setMax(100);
				if (curent<totalsize-1) {
					UpdateZip(++curent);
				}else {
					handler.sendEmptyMessage(MSG_DOWNLOADED);
					EasyConDb.createDb(filespath, true);
					handler.sendEmptyMessage(MSG_FINISH);
				}
				break;
			case MSG_DOWNLOADED: {
				mTv.setText(R.string.splash_downloaded);
				update_button.setBackgroundResource(R.drawable.setting_bg_update_no);
				update_text.setText(R.string.settings_data_noupdate);
				update_button.setClickable(false);
				int version=new_dbVersion;
				String current_text= String.format(getActivity().
						getResources().getString(R.string.settings_data_version),version*1.0f);
				current_version.setText(current_text);
			}
				break;
			case MSG_FINISH:
				mPb.setVisibility(View.GONE);
				mTv.setVisibility(View.GONE);
				mPbh.setVisibility(View.GONE);
				break;

			case MSG_ERROR: {
				AlertDialog.Builder builder = new AlertDialog.Builder(AppApplication.getContext());
				builder.setTitle("提示信息").setMessage(R.string.incongress_data_update_fail).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UpdateZip(0);
					}
				}).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.sendEmptyMessage(MSG_FINISH);
						update_button.setClickable(true);
						update_button.setBackgroundResource(R.drawable.setting_bg_update_yes);
					}
				}).setCancelable(false).show();
			}
				break;
			default:
				break;
			}

		}
	};
	public void onDestroy() {
		super.onDestroy();
		AsyncHttpClient httpClient = AppApplication.getHttpClient();
		httpClient.cancelRequests(getActivity(), true);
		if (handler!=null) {
			handler.removeMessages(MSG_ERROR);
			handler.removeMessages(MSG_DOWNLOADING);
			handler.removeMessages(MSG_DOWNLOADING_ZIP);
			handler.removeMessages(MSG_NEW_FILE);
		}
		
	};
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_Setting_Database); //统计页面
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_Setting_Database); //统计页面
	}
}