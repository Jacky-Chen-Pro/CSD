package com.android.incongress.cd.conference;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.uis.IncongressTextView;
import com.android.incongress.cd.conference.uis.VideoEnabledWebChromeClient;
import com.android.incongress.cd.conference.uis.VideoEnabledWebView;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WebViewVideoActivity extends FragmentActivity {
	private VideoEnabledWebView webView;
	private VideoEnabledWebChromeClient webChromeClient;

	private ImageView mIvBack;
	private IncongressTextView mIvTitle;
	private Button mHome;

	private LinearLayout mPb_loading;
	private IncongressTextView mItvNetError;
	private boolean IsNetWorkOpen;
	private RelativeLayout include;
	private String mUrl;
	private int type;
	
	//微信分享
	private IWXAPI api;
	private LinearLayout mLinearLayout; //分享
	private PopupWindow mPopupWindow; //分享弹出的对话框
	private String shareUrl = "" ;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getIntent().getBooleanExtra("fromMeeting", false)){
			setContentView(R.layout.webview_video_meeting_voice);
			
			//加入分享右上角的按钮
			mLinearLayout = (LinearLayout) findViewById(R.id.title_costom);
			View view = CommonUtils.initView(AppApplication.getContext(), R.layout.right_top_share);
			mLinearLayout.addView(view);
			mLinearLayout.setVisibility(View.VISIBLE);
			shareUrl = getIntent().getStringExtra("url");
		}else {
			setContentView(R.layout.webview_video);
		}

		mIvBack = (ImageView) findViewById(R.id.title_back);
		mIvBack.setImageResource(R.drawable.nav_btn_close);
		mIvTitle = (IncongressTextView) findViewById(R.id.title_text);
		mHome = (Button) findViewById(R.id.title_home);
		mHome.setVisibility(View.GONE);
		mPb_loading = (LinearLayout) findViewById(R.id.pb_loading);
		mItvNetError = (IncongressTextView) findViewById(R.id.itv_net_error);
		include = (RelativeLayout) findViewById(R.id.include_title);
		
		api = AppApplication.instance().getApi();

		if(mLinearLayout != null && mLinearLayout.getVisibility() == View.VISIBLE) {
			mLinearLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showShareDialog();
				}
			});
		}
		mIvBack.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				finish();
				try {
					if(webView != null) {
						webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
						webView.loadUrl("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mUrl = getIntent().getStringExtra("url");
		type = getIntent().getIntExtra("type", -1);

		if (type == 1) {
			if (AppApplication.systemLanguage == 2) {
				mIvTitle.setText("View the video");
			} else {
				mIvTitle.setText("查看视频");
			}
		} else if (type == 2) {
			if (AppApplication.systemLanguage == 2) {
				mIvTitle.setText("View the PPT");
			} else {
				mIvTitle.setText("查看PPT");
			}
		} else {
			if (AppApplication.systemLanguage == 2) {
				mIvTitle.setText("See Detail");
			} else {
				mIvTitle.setText("查看详情");
			}
		}

		// Save the web view
		webView = (VideoEnabledWebView) findViewById(R.id.webView);

		// Initialize the VideoEnabledWebChromeClient and set event handlers
		View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own
																	// view,
																	// read
																	// class
																	// comments
		ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your
																			// own
																			// view,
																			// read
																			// class
																			// comments
		// noinspection all
		View loadingView = getLayoutInflater().inflate(
				R.layout.view_loading_video, null); // Your own view, read class
													// comments
		webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout,
				videoLayout, loadingView, webView) // See all available
													// constructors...
		{
			// Subscribe to standard events, such as onProgressChanged()...
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Your code...
			}

		};
		webChromeClient
				.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
					@SuppressLint("NewApi")
					@Override
					public void toggledFullscreen(boolean fullscreen) {
						// Your code to handle the full-screen change, for
						// example showing and hiding the title bar. Example:
						if (fullscreen) {
							WindowManager.LayoutParams attrs = getWindow()
									.getAttributes();
							attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
							attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
							getWindow().setAttributes(attrs);
							if (android.os.Build.VERSION.SDK_INT >= 14) {
								// noinspection all
								getWindow()
										.getDecorView()
										.setSystemUiVisibility(
												View.SYSTEM_UI_FLAG_LOW_PROFILE);
							}
							include.setVisibility(View.INVISIBLE);
						} else {
							WindowManager.LayoutParams attrs = getWindow()
									.getAttributes();
							attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
							attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
							getWindow().setAttributes(attrs);
							if (android.os.Build.VERSION.SDK_INT >= 14) {
								// noinspection all
								getWindow().getDecorView()
										.setSystemUiVisibility(
												View.SYSTEM_UI_FLAG_VISIBLE);
							}
							include.setVisibility(View.VISIBLE);
						}

					}
				});
		webView.setWebChromeClient(webChromeClient);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mPb_loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mPb_loading.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setDefaultTextEncodingName("GBK");
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);

		// /*** 打开本地缓存提供JS调用 **/
		webView.getSettings().setDomStorageEnabled(true);
		// // Set cache size to 8 mb by default. should be more than enough
		webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		// // This next one is crazy. It's the DEFAULT location for your app's

		// // But it didn't work for me without this line.
		// // TYPE_UPDATE: no hardcoded path. Thanks to Kevin Hawkins
		String appCachePath = getCacheDir().getAbsolutePath();
		webView.getSettings().setAppCachePath(appCachePath);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		
		//自动播放
		if(android.os.Build.VERSION.SDK_INT > 17) {
			webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
		}

		// if (AppApplication.systemLanguage == 2) {
		// url = url + "&lan=en";
		// }
		//
		IsNetWorkOpen = AppApplication.instance().NetWorkIsOpen();

		if (IsNetWorkOpen) {
			webView.loadUrl(mUrl);
		} else {
			mPb_loading.setVisibility(View.GONE);
			videoLayout.setVisibility(View.GONE);
			nonVideoLayout.setVisibility(View.GONE);
			mItvNetError.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		// Notify the VideoEnabledWebChromeClient, and handle it ourselves if it
		// doesn't handle it
		if (!webChromeClient.onBackPressed()) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				// Standard back button implementation (for example this could
				// close the app)
				super.onBackPressed();
			}
		}
	}

	/**
	 * 当用户按下home键时靠它停止播放视频
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (webView != null)
			webView.reload();
	}
	
	/**
	 * 弹出分享对话框
	 */
	public void showShareDialog() {
		initPopupWindow();
		mPopupWindow.showAtLocation(webView, Gravity. CENTER, 0, 0);
	}
	
	/**
	 * 隐藏分享对话框
	 */
	private void exitShareDialog() {
		mPopupWindow.dismiss();
	}
	
	/**
	 * 初始化popupWindow
	 */
	private void initPopupWindow() {

        // 定义DisplayMetrics对象
       DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
       getWindowManager().getDefaultDisplay().getMetrics(dm);
//        screenWidth = dm. widthPixels;
//        screenHeight = dm. heightPixels;

        // 获取自定义布局文件window.xml
       View pview = CommonUtils.initView(AppApplication.getContext(), R.layout.share_dialog);
       
       ImageView offView = (ImageView) pview.findViewById(R.id.dialog_off);
       offView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
       });
       
		LinearLayout friend = (LinearLayout)pview.findViewById(R.id.share_to_friend2);
		friend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), R.string.share_wechat_friend, Toast.LENGTH_SHORT).show();
				share2weixin(0);
			}
		});
		LinearLayout circle = (LinearLayout)pview.findViewById(R.id.share_to_circle2);
		circle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), R.string.share_wechat_circle, Toast.LENGTH_SHORT).show();
				share2weixin(1);
			}
		});
   
        // 设置其背景
		//pview.setBackgroundResource(R.drawable.popup_background);
        // 创建popupW indow实例
        mPopupWindow = new PopupWindow(pview, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true); //pview:自定义布局;400、500:宽和高、true:是否获取焦点
        // 设置为false,让popupWindow出现的时候，它所依赖的父亲view可以接受点击事件
        mPopupWindow.setOutsideTouchable( true);
        // 设置为false，让popupWindow出现的时候，它所依赖的父亲view仍然能获取到焦点
        mPopupWindow.setFocusable( true);
        // 该背景如果不设置会出现点击按钮popupWindow先收缩后展开连续动作，但该方法已被弃用，网上也没找到什么好的办法可以解决。
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x55000000));
	}
	
	private void share2weixin(int flag) {  
	    // Bitmap bmp = BitmapFactory.decodeResource(getResources(),  
	    // R.drawable.weixin_share);  
	  
// 	    if (!api.isWXAppInstalled()) {  
//	        Toast.makeText(AppApplication.getContext(), "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
//	        return;  
//	    }  
	    WXWebpageObject webpage = new WXWebpageObject();  
	    webpage.webpageUrl = shareUrl + "&isShare=1" ;   //分享后的链接
	    WXMediaMessage msg = new WXMediaMessage(webpage);  
	    msg.title = "["+getResources().getString(R.string.home_icon_hyzs)+"]" + webView.getTitle();
	    msg.description = getResources().getString(R.string.home_icon_hyzs);
	    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
	    msg.setThumbImage(thumb);  
	    
	    SendMessageToWX.Req req = new SendMessageToWX.Req();  
	    req.transaction = String.valueOf(System.currentTimeMillis());  
	    req.message = msg;  
	    req.scene = flag;  
	    api.sendReq(req);  
	}  
	
}
