package com.android.incongress.cd.conference.fragments;

import java.io.File;

import org.apache.http.Header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.widget.CheckBox;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.ExhibitorActivityBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.utils.ProgressWebView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

public class WebViewExhibitorsActivity extends HandlerFragement{
	private ProgressWebView mWebView; 
	private ExhibitorActivityBean mBean;
	private String mUrl;
	private String mUrls[];
	private final static int DOWNLOADFILEMSG = 0x1200;
	private final static int SHOWWEBVIEW=0x1300;
	private final static int DOWNLOADFAILED=0x1400;
	private int downloadcount=0;
	private View mview;
	private boolean mFinished;
	private CheckBox mBox;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.incongresswebview, null);
//		MobclickAgent.onEvent(getActivity(),UMengPage.Page_Exhibitor_Activity_Detail); //统计页面
		mWebView=(ProgressWebView)view.findViewById(R.id.incongress_webview);
		mUrls = mBean.getUrl().split(",");
		String filevalue[] = mUrls[0].split("/");
		int length = filevalue.length;
		mUrl = "file://"+ AppApplication.instance().getSDPath()
		+ Constants.FILESDIR+ filevalue[length - 1];

		return view;
	}
	   @Override
	   public void onStart() {
	       super.onStart();
	       
			if (mBean.getStoreurl() == 0) {
				showDialog();
				downloadmsg();
			} else {
				//mHanlder.sendEmptyMessage(SHOWWEBVIEW);
				showWebView();
			}
	   }
	private void downloadmsg() {
		AsyncHttpClient client = AppApplication.getHttpClient();
		String urls[] = mBean.getUrl().split(",");
		for (int i = 0; i < urls.length; i++) {
			final String url = urls[i];
			String values[] = url.split("/");
			int size = values.length;
			final String filename = values[size - 1];
			String filepath = AppApplication.instance().getSDPath()
			+ Constants.FILESDIR
					+ filename;
			File file=new File(filepath);
			// 服务器拿数据显示并存储
			client.get(getActivity(), Constants.get_IMAGEPREFIX() +url,
					new FileAsyncHttpResponseHandler(file) {

						@Override
						public void onStart() {
							System.out.println("cccc onStart "+filename);
						}
						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, File file) {
							mHanlder.sendEmptyMessage(DOWNLOADFAILED);
							System.out.println("cccc onFailure");
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								File file) {
							mHanlder.sendEmptyMessage(DOWNLOADFILEMSG);
							System.out.println("cccc onSuccess");
						}
						@Override
						public void onFinish() {
							System.out.println("cccc finished");
						}
					});
		}
	}
	
	Handler mHanlder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOADFILEMSG:
				downloadcount++;
				if(downloadcount==mUrls.length){
			        DbAdapter ada = DbAdapter.getInstance();
			        ada.open();
			        mBean.setStoreurl(1);
			        ConferenceSetData.updateExhibitorActivity(ada, mBean);
			        ada.close();
					dismissDialog();
/*					int zshd_count=AppApplication.moduleBean.getZshdCount();
					zshd_count--;
					System.out.println("----the zshd_count is is::"+zshd_count);
					AppApplication.moduleBean.setZshdCount(zshd_count);
					if(zshd_count>0){
						AppApplication.moduleBean.setVisiblezshd(true);
					}else{
						AppApplication.moduleBean.setVisiblezshd(false);
					}*/
					showWebView();
				}
				break;
			case SHOWWEBVIEW:
				showWebView();
				break;
			case DOWNLOADFAILED:
				if (mFinished) {
					removeMessages(DOWNLOADFAILED);
					return;
				}
				mFinished = true;
				dismissDialog();
				performback();
				break;
			default:
				break;
			}
		}

	};
	public void setExhibitor(ExhibitorActivityBean bean){
		mBean=bean;
	}
	public void setView(View view){
		mview=view;
		final CheckBox checkbox=(CheckBox) mview.findViewById(R.id.title_star);
		
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			checkbox.setPadding(5, 0, 0, 0);
		}
		if(mBean.getAttention()==1){
			checkbox.setChecked(true);
			checkbox.setText(R.string.exhibitor_activity_detail_attentioned);
		}else{
			checkbox.setChecked(false);
			checkbox.setText(R.string.exhibitor_activity_detail_attention);
		}
		checkbox.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				CheckBox checkview=(CheckBox)view;
/*				//表明是游客
				int userType = AppApplication.userType;
				if(userType==1||userType==0){
					checkview.setChecked(false);
					showreglogindialog();
					return;
				}*/
				int attention=1;
				if(checkview.isChecked()){
					attention=1;
				}else{
					attention=0;
				}
				mBean.setAttention(attention);
				DbAdapter ada = DbAdapter.getInstance();
				ada.open();
				ConferenceSetData.updateExhibitorActivity(ada, mBean);
				ada.close();
				if(attention==1){
					checkbox.setText(R.string.exhibitor_activity_detail_attentioned);
				}else{
					checkbox.setText(R.string.exhibitor_activity_detail_attention);
				}
			}
			
		});
	}
	public void showWebView(){
//		 //设置Web属性，能够执行JavaScript脚本
//       WebSettings webSettings  = mWebView.getSettings();
//       //设置WebView的属性，此时可以去执行JavaScript脚本 
//       webSettings.setJavaScriptEnabled(true);  
//		//设置可以访问文件 
//       webSettings.setAllowFileAccess(true);
//		//设置默认缩放方式尺寸是far
//       webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//       //支持缩放
//       webSettings.setSupportZoom(true);
//       // 缩放按钮
//       webSettings.setBuiltInZoomControls(true);
//       webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//       mWebView.setInitialScale(-1);//为25%，最小缩放等级
//       //适应全屏  39适应竖屏    57适应横屏 
//       mWebView.setInitialScale(39); 
//       //提高渲染优先级
//       webSettings.setRenderPriority(RenderPriority.HIGH); 
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setDefaultTextEncodingName("GBK");
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);

       //把图片加载放在最后来加载渲染
       //webSettings.setBlockNetworkImage(true);
       //调用loadUrl()方法进行加载内容 
//		mWebView.loadUrl("http://www.baidu.com"); 
//        System.out.println("---the url is is::"+mUrl);
		mWebView.loadUrl(mUrl); 
	}
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_Exhibitor_Activity_Detail); //统计页面
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_Exhibitor_Activity_Detail); //统计页面
	}
}
