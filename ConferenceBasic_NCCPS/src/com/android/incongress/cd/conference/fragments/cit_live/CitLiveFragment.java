package com.android.incongress.cd.conference.fragments.cit_live;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.AdBean;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.uis.IncongressTextView;
import com.android.incongress.cd.conference.uis.VideoEnabledWebChromeClient;
import com.android.incongress.cd.conference.uis.VideoEnabledWebView;
import com.android.incongress.cd.conference.utils.AsyncImageLoader;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.AsyncImageLoader.ImageLoadCallback;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

@SuppressLint("NewApi") 
public class CitLiveFragment extends FragmentActivity {

	private VideoEnabledWebView webView; 
	private VideoEnabledWebChromeClient webChromeClient;
	
	private ImageView mIvBack;
	private IncongressTextView mIvTitle;
	private Button mHome;
	
	private LinearLayout mPb_loading;
	private IncongressTextView mItvNetError;
	private boolean IsNetWorkOpen;
	private RelativeLayout include;
	private LinearLayout mLinearLayout; //分享\
	private PopupWindow mPopupWindow;
	private ImageView mAdBottom; //底部广告
	private List<AdBean> mAdList;
	
	// 用于广告的缓存 按照MainActivity的写法即可
	private AsyncImageLoader mAdAsyncImageLoader;
	
	//微信分享
	private IWXAPI api;
	private String shareUrl = "" ;
	
	@Override @SuppressLint("NewApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.webview_video_cit);
		
		mIvBack = (ImageView) findViewById(R.id.title_back);
		mIvBack.setImageResource(R.drawable.nav_btn_close);
		mIvTitle = (IncongressTextView) findViewById(R.id.title_text);
		mIvTitle.setText(R.string.home_icon_cit);
		mHome = (Button) findViewById(R.id.title_home);
		mHome.setVisibility(View.GONE);
		mPb_loading = (LinearLayout) findViewById(R.id.pb_loading);
		mItvNetError = (IncongressTextView)findViewById(R.id.itv_net_error);
		include = (RelativeLayout) findViewById(R.id.include_title);
		mAdBottom = (ImageView) findViewById(R.id.cit_ad_bottom);
		
		mLinearLayout = (LinearLayout) findViewById(R.id.title_costom);
		View view = CommonUtils.initView(AppApplication.getContext(), R.layout.right_top_share);
		mLinearLayout.addView(view);
		mLinearLayout.setVisibility(View.GONE);
		
		api = AppApplication.instance().getApi();
		mAdAsyncImageLoader = new AsyncImageLoader();
		mAdList = AppApplication.adList;
		
		mLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showShareDialog();
			}
		});
		
		mIvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				try {
					if(webView != null) {
						webView.loadUrl("javascript:clearCachc(1)");
						webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
						webView.loadUrl("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// Save the web view
		webView = (VideoEnabledWebView) findViewById(R.id.webView);

		// Initialize the VideoEnabledWebChromeClient and set event handlers
		final View nonVideoLayout = findViewById(R.id.nonVideoLayout);
		ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout);
		// noinspection all
		View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class
		webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available
		{
			@Override
			public void onProgressChanged(WebView view, int progress) {
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
							//控制为必须横屏显示
						     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
							//=======
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
							include.setVisibility(View.GONE);
						} else {
							//控制为必须竖屏显示
							include.setVisibility(View.VISIBLE);
						     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
							//=======
							WindowManager.LayoutParams attrs = getWindow().getAttributes();
							attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
							attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
							getWindow().setAttributes(attrs);
							if (android.os.Build.VERSION.SDK_INT >= 14) {
								// noinspection all
								getWindow().getDecorView()
										.setSystemUiVisibility(
												View.SYSTEM_UI_FLAG_VISIBLE);
							}
							
						}

					}
				});
		webView.setWebChromeClient(webChromeClient);
		webView.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onPageStarted(WebView view, String url,	Bitmap favicon) {
				mPb_loading.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				mPb_loading.setVisibility(View.GONE);
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				if(url != null) {
					mLinearLayout.setVisibility(View.VISIBLE);
					shareUrl = url;
					//隐藏广告
					setADVisible(false);
					//距离底部距离改成0
					include.setVisibility(View.VISIBLE);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
							(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
							android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
//					params.setMargins(0, 50, 0, 0);
					params.addRule(RelativeLayout.BELOW, R.id.include_title); //动态修改布局:参照文章：http://www.cnblogs.com/angeldevil/p/3836256.html
					nonVideoLayout.setLayoutParams(params);
				} 
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
		webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getCacheDir().getAbsolutePath();
		webView.getSettings().setAppCachePath(appCachePath);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		
		if(android.os.Build.VERSION.SDK_INT > 17) {
			webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
		}

		String url = Constants.get_CIT_WEBSITE();
		if (AppApplication.systemLanguage == 2) {
			url = url + "&lan=en";
		}
		
		IsNetWorkOpen = AppApplication.instance().NetWorkIsOpen();
		
		if(IsNetWorkOpen) {
			webView.loadUrl(url);
		}else {
			mPb_loading.setVisibility(View.GONE);
			videoLayout.setVisibility(View.GONE);
			nonVideoLayout.setVisibility(View.GONE);
			mItvNetError.setVisibility(View.VISIBLE);
		}
		//显示广告
		onAdChanging();
	}
	
	@Override
	public void onBackPressed() {
		// Notify the VideoEnabledWebChromeClient, and handle it ourselves if it
		// doesn't handle it
		if (!webChromeClient.onBackPressed()) {
			if (webView.canGoBack()) {
				webView.goBack();
				webView.loadUrl("javascript:clearCachc()");
				mLinearLayout.setVisibility(View.GONE);
				//将广告显示出来
				mAdBottom.setVisibility(View.VISIBLE);
			} else {
				// Standard back button implementation (for example this could
				// close the app)
				webView.loadUrl("javascript:clearCachc()");
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
		if (webView != null) {
			webView.reload();
			webView.loadUrl("javascript:clearCachc()");
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webView != null) {
			webView.reload();
			webView.loadUrl("javascript:clearCachc()");
		}
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
	  
	    msg.title = "[" + getResources().getString(R.string.home_icon_cit)+"]" + webView.getTitle();  
	    msg.description = getResources().getString(R.string.home_icon_cit);
	    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
	    msg.setThumbImage(thumb);  
	    
	    SendMessageToWX.Req req = new SendMessageToWX.Req();  
	    req.transaction = String.valueOf(System.currentTimeMillis());  
	    req.message = msg;  
	    req.scene = flag;  
	    api.sendReq(req);  
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
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	// 覆盖主Activity的广告消息
	// 处理上方和下方的广告  这里只需要处理下方广告, 下方广告有11个（测试时的情况）
	protected void onAdChanging() {
		if (AppApplication.bottomNum < 0) {
			return;
		}
//		int top = AppApplication.topNum;
		int bottom = AppApplication.bottomNum;

		// 处理接收到的内容
		String filespath = AppApplication.instance().getSDPath() + Constants.FILESDIR;
		
		String pathBottom = "";
		if (mAdList.size() > bottom) {
			pathBottom = filespath + mAdList.get(bottom).getAdImage();
		}
		
		File fileBottom = new File(pathBottom);
		
		if (fileBottom.exists()) {
			setAdImageView(fileBottom.getAbsolutePath(), mAdBottom);
		}
	}
	
	private void setAdImageView(String filepath, ImageView imageview) {
		Bitmap cachedImage = mAdAsyncImageLoader.getCachBitmap(filepath);
		if (cachedImage == null) {
			mAdAsyncImageLoader.loadBitmap(imageview, filepath, new ImageLoadCallback() {
				@Override
				public void imageLoaded(ImageView imageView, Bitmap imageBitmap, String imageUrl) {
					if (imageBitmap != null) {
						imageView.setImageBitmap(imageBitmap);
					} else {
						Toast.makeText(getApplicationContext(), "广告显示失效，请稍后重试", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} else {
			mAdBottom.setImageBitmap(cachedImage);
		}
	}
	
	/**
	 * 设置广告的显示与否
	 * @param isShow
	 */
	private void setADVisible(boolean isShow) {
		if(isShow)
			mAdBottom.setVisibility(View.VISIBLE);
		else 
			mAdBottom.setVisibility(View.GONE);
	}
}
