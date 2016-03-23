package com.android.incongress.cd.conference.fragments.wall_poster;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.adapters.DZBBAdapter;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.data.JsonParser;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.HttpUtils;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ThreadPool;
import com.android.incongress.cd.conference.utils.ThreadPool.Job;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 电子壁报  
 * 测试地址：
 * http://114.80.201.49/posterAction.do?method=getPosterListByConId&count=10&conId=44&pageIndex=1&searchStirng=
 * @author Administrator
 *
 */
public class WallPosterFragment extends BaseFragment {
	
	private PullToRefreshListView mDzbbListview;
	private EditText mSearchEditText;
	private ImageView mCancelImage;
	private LinearLayout mRlSort;
	private LinearLayout mLlCode;
	private LinearLayout mLlAuthor;
	private LinearLayout mLl_sort;
	private ImageView mIvCode;
	private ImageView mIvAuthor;
	private TextView mNoData;
	private LinearLayout mPb_loading;
	private TextView mNetWorkError;
	
	private boolean IsNetWorkOpen = true;
	private List<DZBBBean> dzbbBeans = new ArrayList<DZBBBean>();
	private List<DZBBBean> allBeans = new ArrayList<DZBBBean>();
	private DZBBAdapter mAdapter;
	private int orderBy = 1; //1按照code排序，0按照Author排序
	
	private int currentPage = 1;// 当前分页位置
	private int mPageSize = 5;// 每页显示的数据条数
	private String mSearchString = ""; //搜索的信息,非搜索状态下为空
	
	private static final int MSG_REFRESH = 0;
	private static final int MSG_DONE = 1;
	private static final int MSG_ERROR = 1;
	private static final int MSG_NO_DATA = 2;
	private static final int MSG_TOAST_NO_MORE_DATA = 3;
	
	/** 判断是否是搜索模式，防止重复加载 **/
	private boolean isSearchState = false;
	
	private boolean isSortViewOn = false;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int result = msg.what;
			
			if(result == MSG_REFRESH) {
				mNoData.setVisibility(View.GONE);
				mPb_loading.setVisibility(View.VISIBLE);
				getDZBBList();
				mDzbbListview.getLoadingLayoutProxy().setLastUpdatedLabel(CommonUtils.getTime());
				mAdapter.notifyDataSetChanged();
				mDzbbListview.onRefreshComplete();
				if(isSearchState) {
					isSearchState = !isSearchState;
				}
			}else if(result == MSG_DONE) {
				mNoData.setVisibility(View.GONE);
				mPb_loading.setVisibility(View.GONE);
				mDzbbListview.setVisibility(View.VISIBLE);
				mDzbbListview.getLoadingLayoutProxy().setLastUpdatedLabel(CommonUtils.getTime());
				mAdapter.notifyDataSetChanged();
			}else if(result == MSG_ERROR) {
				mNoData.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "加载数据出错，请稍后重试", Toast.LENGTH_SHORT).show();
				currentPage = 1;
				mSearchString = "";
				dzbbBeans.clear();
				orderBy = 1;
				getDZBBList();
				mAdapter.notifyDataSetChanged();
			}else if(result == MSG_NO_DATA) {
				mNoData.setVisibility(View.VISIBLE);
				mDzbbListview.setVisibility(View.GONE);
			}else if(result == MSG_TOAST_NO_MORE_DATA) {
				Toast.makeText(AppApplication.getContext(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
			}else {
				//do nothing
			}
			
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.electronic_bb, null);
		mDzbbListview = (PullToRefreshListView) view.findViewById(R.id.dzbb_listview);
		mSearchEditText = (EditText) view.findViewById(R.id.itv_search_text);
		mCancelImage = (ImageView) view.findViewById(R.id.iv_cancel);
		mRlSort = (LinearLayout) view.findViewById(R.id.rl_sort);
		mLlCode = (LinearLayout) view.findViewById(R.id.ll_bb_code);
		mLlAuthor = (LinearLayout) view.findViewById(R.id.ll_bb_author);
		mIvCode = (ImageView) view.findViewById(R.id.iv_code);
		mIvAuthor = (ImageView) view.findViewById(R.id.iv_author);
		mLl_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
		mPb_loading = (LinearLayout) view.findViewById(R.id.pb_loading);
		mNetWorkError = (TextView) view.findViewById(R.id.itv_net_error);
		mNoData = (TextView) view.findViewById(R.id.no_bb_data);
		
		IsNetWorkOpen = NetWorkUtils.isNetworkConnected(getActivity());
		
		if(!IsNetWorkOpen) {
			mNetWorkError.setVisibility(View.VISIBLE);
		}else {
			mPb_loading.setVisibility(View.VISIBLE);
			
			mLlCode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					clearChoose();
					mIvCode.setImageResource(R.drawable.speaker_role_choose);
					
					//按照壁报机编号排序 搜索
					//肯定是先清空原来的数据，然后重新请求数据
					currentPage = 1;
					orderBy = 1;
					dzbbBeans.clear();
					allBeans.clear();
					mHandler.sendEmptyMessage(MSG_REFRESH);
					mRlSort.setVisibility(View.GONE);
				}
			});
			
			mLlAuthor.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					clearChoose();
					mIvAuthor.setImageResource(R.drawable.speaker_role_choose);
					
					//按照作者编号排序 搜索
					//肯定是先清空原来的数据，然后重新请求数据
					currentPage = 1;
					dzbbBeans.clear();
					orderBy = 0;
					allBeans.clear();
					mHandler.sendEmptyMessage(MSG_REFRESH);
					mRlSort.setVisibility(View.GONE);
				}
			});
			
			mLl_sort.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isSortViewOn) {
						mRlSort.setVisibility(View.GONE);
						isSortViewOn = false;
					}else {
						mRlSort.setVisibility(View.VISIBLE);
						isSortViewOn = true;
					}
					
				}
			});
			
			mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View arg0, boolean focus) {
					if (focus == true) {
						toggleShurufa();
						setAdvisiable(false);

					} else {
						hideShurufa();
						setAdvisiable(true);
					}
				}

			});
			
			mSearchEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					isSearchState = true;
					int length = mSearchEditText.getText().length();
					mNoData.setVisibility(View.GONE);
					if (length == 0) {
						dzbbBeans.clear();
						currentPage = 1;
						allBeans.clear();
						mSearchString = "";
						getDZBBList();
						return;
					}
					if (length > 0) {
						if (mNoData.VISIBLE == View.VISIBLE)
							mCancelImage.setVisibility(View.VISIBLE);
						setAdvisiable(false);
						//直接进行搜索
						try {
							mSearchString = URLEncoder.encode(s.toString().trim(), "GBK");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}

					dzbbBeans.clear();
					currentPage = 1;
					allBeans.clear();
					getDZBBList();
				}
			});
			
			mDzbbListview.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					currentPage = 1;
					allBeans.clear();
					dzbbBeans.clear();
					mHandler.sendEmptyMessage(MSG_REFRESH);
				}
			});
			
			mDzbbListview.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

				@Override
				public void onLastItemVisible() {
					if (!isSearchState)
						//获取新的数据
						getDZBBList();
				}
			});
			
			mDzbbListview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
//					if (AppApplication.userType == 0 || AppApplication.userType == 1) {
//						LoginActivity.startLoginActivity(getActivity(),LoginActivity.TYPE_PROFESSOR);
//						return;
//					}
					if (AppApplication.userType != Constants.TYPE_USER_REGISTER_BIND_CODE && AppApplication.userType != Constants.TYPE_USER_FACUTY) {
						if (AppApplication.userType == Constants.TYPE_USER_VISITOR) {
							LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_PROFESSOR, "", "", "", "");
						}

						if (AppApplication.userType == Constants.TYPE_USER_REGISTER_NOT_BIND_CODE) {
							if (AppApplication.systemLanguage == 1) {
								String userName = AppApplication.getSPStringValue(Constants.USER_NAME);
								String mobile = AppApplication.getSPStringValue(Constants.USER_MOBILE);
								LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_PROFESSOR, userName, mobile, "", "");
							} else {
								String familyName = AppApplication.getSPStringValue(Constants.USER_FAMILY_NAME);
								String givenName = AppApplication.getSPStringValue(Constants.USER_GIVEN_NAME);
								String mobile = AppApplication.getSPStringValue(Constants.USER_MOBILE);
								LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_PROFESSOR, "", mobile, familyName, givenName);
							}
						}
						return;
					}

					DZBBBean bean = (DZBBBean) parent.getAdapter().getItem(position);
					WallPosterImageFragment fragment = new WallPosterImageFragment();
					fragment.setDiscussInfo(bean);
//					View view = CommonUtils.initView(AppApplication.getContext(), R.layout.right_top_share);
//					fragment.setShareView(view);
					action(fragment, R.string.dzbb_detail, false, false);
				}
			});
			
			mCancelImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mSearchEditText.setText("");
					mSearchEditText.clearFocus();
					hideShurufa();
					setAdvisiable(true);
					mCancelImage.setVisibility(View.GONE);

					//初始化各个搜索条件
					dzbbBeans.clear();
					currentPage = 1;
					mSearchString = "";
					allBeans.clear();
					mHandler.sendEmptyMessage(MSG_REFRESH);
				}
			});
			
			mAdapter= new DZBBAdapter(getActivity(), allBeans);
			mDzbbListview.setAdapter(mAdapter);
			
			getDZBBList();
		}
	
		return view;
	}
	
	/**
	 * 获取电子壁报数据
	 */
	private void getDZBBList() {
		ThreadPool pool = AppApplication.getThreadPool();
		pool.execute(new Job<Void>() {
			@Override
			public Void run() {
				try {
					if (IsNetWorkOpen) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("method", "getPosterListByConId");
						map.put("pageIndex", (currentPage++) + "");
						map.put("conId", AppApplication.conId + "");
						map.put("searchString", new String(mSearchString.getBytes(), "GBK"));
						map.put("count", mPageSize+"");
						map.put("orderBy", orderBy + "");
						String url = HttpUtils.getUrlWithParas(Constants.get_DZBB(), map);
						String str = readParse(url);
						dzbbBeans = JsonParser.parseDzbb(str);
					}else {
						//无网络的情况下
						return null;
					}
					
					if(dzbbBeans!=null && dzbbBeans.size()>0) {
						allBeans.addAll(dzbbBeans);
						mHandler.sendEmptyMessage(MSG_DONE);
					}else {
						mHandler.sendEmptyMessage(MSG_ERROR);
					}
					
					if(dzbbBeans != null) {
						if(dzbbBeans.size() == 0 && allBeans.size() == 0) 
							mHandler.sendEmptyMessage(MSG_NO_DATA);
						if(allBeans.size() > 0 && dzbbBeans.size() == 0)
							mHandler.sendEmptyMessage(MSG_TOAST_NO_MORE_DATA);
					}
				} catch (Exception e) {
					mHandler.sendEmptyMessage(MSG_ERROR);
				}
				return null;
			}
		});
	}

	private void setAdvisiable(boolean visable){
		HomeActivity activity = (HomeActivity) getActivity();
		activity.setAdVisible(false, visable);
	}

	/**
     * 从指定的URL中获取数组
     * @param urlPath
     * @return
     * @throws Exception
     */
	private String readParse(String urlPath) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.connect();
		InputStream inStream = conn.getInputStream();
		while ((len = inStream.read(data)) != -1) {
			outStream.write(data, 0, len);
		}
		inStream.close();
		return new String(outStream.toByteArray(),"GBK");// 通过out.Stream.toByteArray获取到写的数据
	}
	
	private void clearChoose() {
		mIvCode.setImageResource(R.drawable.speaker_role_not_choose);
		mIvAuthor.setImageResource(R.drawable.speaker_role_not_choose);
	}
}
