package com.android.incongress.cd.conference.fragments.wall_poster;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.MeetingCommunityCommentsAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.HandlerFragement;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.CommunityTopicContentBean;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.DZBBDiscussResponseBean;
import com.android.incongress.cd.conference.data.JsonParser;
import com.android.incongress.cd.conference.uis.IncongressEditText;
import com.android.incongress.cd.conference.uis.IncongressTextView;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.EmotionsUtils;
import com.android.incongress.cd.conference.utils.HttpUtils;
import com.android.incongress.cd.conference.utils.ThreadPool;
import com.android.incongress.cd.conference.utils.ThreadPool.Job;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * 参与讨论 详情页面
 * @author Jakcy
 *
 */
public class WallPosterTalkFragment extends HandlerFragement implements OnClickListener {

//    private GridView mEmojoView;
    private IncongressEditText mIncongressEditText;
    private ListView mListView;
    private PullToRefreshScrollView mScrollView;
    private IncongressTextView mComments;
    private IncongressTextView mNumber;
    
//    private ImageView mEmojo;
    private Button mPostButton;
    private int mPageIndex = 1;//当前页数
	private int mPageSize = 12;//每页显示的数据条数
	private int mConnectTime = 0;// 因为hession有Bug 所有采用的是如果连接5次还没有连接上就提示无法连接请重试
    public final static int MSG_NO_MORE_DATA = 0x1003;//没有更多的数据
    public final static int MSG_REFRESH_DATA=0x1004;//刷新数据
    public final static int MSG_ERROR_SUBMIT=0x1005;
    public final static int MSG_TOP_SUCCESS = 0x1006;//正确
    public final static int MSG_TOP_REFESHDONE=0x1008;//刷新完成
    public final static int MSG_TOP_ERROR = 0x1009;//出现错误
    private List<CommunityTopicContentBean> mList;
    private MeetingCommunityCommentsAdapter mAdapter;
    private WallPosterImageFragment mDzbb;
    private DZBBBean mDZBBBean;
    private IncongressTextView mNoDataView;
    private int mPosterCommentId = 0;
    private static final String POST = "POST"; //0代表get，1代表post
    private static final String GET = "GET"; //0代表get，1代表post
    private String currentType = "GET"; //请求默认是GET
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dzbbdiscussion_comments, null);
//      MobclickAgent.onEvent(getActivity(),UMengPage.Page_Metting_Community_Detail); //统计页面 
//        mEmojoView = (GridView) view.findViewById(R.id.hysq_comments_emopanel);
        mIncongressEditText = (IncongressEditText) view.findViewById(R.id.hysq_comments_edit);
        
        mComments = (IncongressTextView) view.findViewById(R.id.hysq_home_comments_pinglun);
        mNumber = (IncongressTextView)view.findViewById(R.id.hysq_home_comments_number);
        mListView = (ListView) view.findViewById(R.id.hysq_comments_list);
        mScrollView=(PullToRefreshScrollView)view.findViewById(R.id.pull_refresh_scrollview);
        mScrollView.setMode(Mode.BOTH);
        mNoDataView=(IncongressTextView)view.findViewById(R.id.exhibitor_no_data);
//        mEmojo = (ImageView) view.findViewById(R.id.hysq_comments_emojo);
//        mEmojo.setBackgroundResource(R.drawable.hysq_icon_biaoqing);
        mPostButton = (Button) view.findViewById(R.id.hysq_comments_post);
//        mEmojo.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        mComments.setText(getString(R.string.dzbb_comment_tie,mDZBBBean.getDisCount())); //评论数通过前一页的bean获取
        
		SpannableStringBuilder message=new SpannableStringBuilder();
		
        mList=new ArrayList<CommunityTopicContentBean>();
        mAdapter= new MeetingCommunityCommentsAdapter(inflater, mList);
        mListView.setAdapter(mAdapter);

        //下拉刷新
        mScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				Mode mode=refreshView.getCurrentMode();
				if(mode==Mode.PULL_FROM_START){
					mScrollView.setMode(Mode.DISABLED);
					mPageIndex = 1;
					mList.clear();
					getTopicContentList();
				}
				if(mode==Mode.PULL_FROM_END){
					mScrollView.setMode(Mode.DISABLED);
					getTopicContentList();
				}

			}
		});
	    mIncongressEditText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
//                mEmojoView.setVisibility(View.GONE);
//                mEmojo.setBackgroundResource(R.drawable.hysq_icon_biaoqing);
			}
	    });
	    mIncongressEditText.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
//                mEmojoView.setVisibility(View.GONE);
//                mEmojo.setBackgroundResource(R.drawable.hysq_icon_biaoqing);
			}
	    	
	    });
        
        
//        SimpleAdapter adapter = creatAdapter();
//        mEmojoView.setAdapter(adapter);
//        mEmojoView.setOnItemClickListener(new EmojoClickListener());
        return view;
    }

    private SimpleAdapter creatAdapter() {
        int[] id = new int[] {R.id.hysq_home_shuo_gird_image };
        int[] emojo = EmotionsUtils.emojo;
        String[] key = new String[] {"image" };
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < emojo.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(key[0], emojo[i]);
            list.add(map);
        }
        SimpleAdapter popAdapter = new SimpleAdapter(getActivity(), list, R.layout.hysqhome_shuoliangju_gird_item, key, id);
        return popAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//        case R.id.hysq_comments_emojo:
//            if (mEmojoView.getVisibility() == View.VISIBLE) {
//                mEmojoView.setVisibility(View.GONE);
//                mEmojo.setBackgroundResource(R.drawable.hysq_icon_biaoqing);
//                toggleShurufa();
//            }else {
//                mEmojoView.setVisibility(View.VISIBLE);
//                mEmojo.setBackgroundResource(R.drawable.hysq_icon_jianpan);
//                hideShurufa();
//            }
//            break;
        case R.id.hysq_comments_post:
        	sendTopic();
        	mPostButton.setClickable(false);
        	break;
        default:
            break;
        }
    }
    private void sendTopic(){
    	String msg=mIncongressEditText.getText().toString().trim();
    	if(msg!=null&&msg.length()>0){
			sendTopicContent(msg);
    	}else{
    	   TostShowMsg(R.string.hysq_home_comment_send_msg_hint);
    	}
    }
    
//    private class EmojoClickListener implements OnItemClickListener{
//        @Override
//        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            EmotionsUtils emou = EmotionsUtils.getInstance();
//            int index = mIncongressEditText.getSelectionStart();
//            Editable edit = mIncongressEditText.getEditableText();
//            if (arg2 == EmotionsUtils.emojo.length-1) {
//                String temp = mIncongressEditText.getText().toString().substring(0, index);
//                int pos[] =emou.getLastEmoution(temp);
//                if (pos!=null&&pos[1]==index) {
//                    edit.delete(pos[0], pos[1]);
//                }else {
//                    edit.delete(index-1<0?0:index-1, index);
//                }
//                return;
//            }
//            
//            CharSequence charSequence = emou.formatMessage(emou.getEmotionCode(arg2));
//            if (index < 0 || index >= edit.length()) {
//                edit.append(charSequence);
//            } else {
//                edit.insert(index, charSequence);
//            }
//            
//        }
//    }
	@Override
	public void onStart() {
		super.onStart();
		getTopicContentList();
	}
	
	/**
	 * 设置初始数据源
	 * @param dzbb
	 * @param bean
	 */
    public void setCommunityTopicBean(WallPosterImageFragment dzbb,DZBBBean bean){
    	this.mDzbb = dzbb;
    	this.mDZBBBean = bean;
    }
    
	private void getTopicContentList() {
		ThreadPool pool = AppApplication.getThreadPool();
		pool.execute(new Job<Void>() {

			@Override
			public Void run() {
				try {
					//新的处理方式
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("method", "getPosterDiscussListByPid");
					map.put("posterId", mDZBBBean.getPosterId() + "");
					map.put("count", mPageSize + "");
					map.put("pageIndex", mPageIndex + "");
					String url = HttpUtils.getUrlWithParas(Constants.get_DZBB(), map);
					System.out.println("url====" + url);
					currentType = GET;
					String json = readParse(url);
					List<CommunityTopicContentBean> strList = JsonParser.parseDZBBContent(json);
					
					if(strList.size()==0){
						mhandler.sendEmptyMessage(MSG_NO_MORE_DATA);
					}else{
						mList.addAll(strList);
						mhandler.sendEmptyMessage(MSG_SUCCESS);
					}
					mhandler.sendEmptyMessage(MSG_REFESHDONE);
				} catch (Exception e) {
					mhandler.sendEmptyMessage(MSG_ERROR);
				}
				return null;
			}
		});
	}
	
	private void getFirstContent() {
		ThreadPool pool = AppApplication.getThreadPool();
		pool.execute(new Job<Void>() {
			@Override
			public Void run() {
				try {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("method", "getPosterDiscussById");
					map.put("posterDiscussById", mPosterCommentId + "");
					String url = HttpUtils.getUrlWithParas(Constants.get_DZBB(), map);
					currentType = GET;
					String json = readParse(url);
					CommunityTopicContentBean bean = JsonParser.parseDZBBOneContent(json);
					
					if(bean == null ){
						mhandler.sendEmptyMessage(MSG_NO_MORE_DATA);
					}else{
						mList.add(0, bean);
						mhandler.sendEmptyMessage(MSG_SUCCESS);
					}
					mhandler.sendEmptyMessage(MSG_REFESHDONE);
				} catch (Exception e) {
					mhandler.sendEmptyMessage(MSG_ERROR);
				} finally {
					mPosterCommentId = 0;
				}
				return null;
			}
		});
	}
	
	
	private void TostShowMsg(int strId){
 	   Toast.makeText(getActivity(),strId,Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 发送评论
	 */
	private void sendTopicContent(final String content){
		ThreadPool pool = AppApplication.getThreadPool();
		pool.execute(new Job<Void>() {
			@Override
			public Void run() {
				try {
					int userId= AppApplication.userId;
					
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("method", "createPosterDiscuss");
					map.put("userId", userId + "");
					map.put("posterId", mDZBBBean.getPosterId() + "");
					map.put("content", URLEncoder.encode(content,"GBK"));
					map.put("conId", 44 + "");
					String url = HttpUtils.getUrlWithParas(Constants.get_DZBB(), map);
					currentType = POST;
					String json = readParse(url);
					DZBBDiscussResponseBean bean = JsonParser.parseDiscussSuccess(json);
					mPosterCommentId = bean.getPosterDiscussId();
					if(bean != null && bean.getState() == 1) {
					  //此处如果是游客需要保存游客的身份信息 也需要持久化
				    	int userType= AppApplication.userType;
				    	if(userType==0){
							AppApplication.userType=bean.getUserType();
							AppApplication.userId=bean.getUserId();
							AppApplication.username=bean.getUserName();
							//存储到sharepreference
							SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
							Editor editor = preferences.edit();
							editor.putInt(Constants.PREFERENCE_USER_ID,bean.getUserId());
							editor.putString(Constants.PREFERENCE_USER_NAME, bean.getUserName());
							editor.putInt(Constants.PREFERENCE_USER_TYPE,bean.getUserType());//表明是用户
							editor.commit();
				    	}
			            mhandler.sendEmptyMessage(MSG_REFRESH_DATA);
					}else {
						mhandler.sendEmptyMessage(MSG_ERROR_SUBMIT);
					}
				} catch (Exception e) {
					mhandler.sendEmptyMessage(MSG_ERROR);
				}
				return null;
			}
		});
	}
	
	@Override
	protected void onReceiveMsg(Message msg) {
		// dismissDialog();
		switch (msg.what) {
		case MSG_SUCCESS:
			mPageIndex++;
			mAdapter.setContent(mList);
			mComments.setText(getString(R.string.dzbb_comment_tie,mDZBBBean.getDisCount()));
			mAdapter.notifyDataSetChanged();
			if(mList.size()>0){
				mListView.setVisibility(View.VISIBLE);
				mNoDataView.setVisibility(View.GONE);
				setListViewHeightBasedOnChildren(mListView);
			}else{
				mListView.setVisibility(View.GONE);
				mNoDataView.setVisibility(View.VISIBLE);
			}
			break;
		case MSG_ERROR:
			mConnectTime++;
			if (mConnectTime == 5) {
				Toast.makeText(getActivity(), R.string.incongress_connect_fail, 
						Toast.LENGTH_SHORT).show();
				mConnectTime=0;
				mPostButton.setClickable(true);
			}else{
				getTopicContentList();
			}
			break;
		case MSG_NO_MORE_DATA:
/*			if(visible_Toast){
		       Toast.makeText(getActivity(), 
				R.string.incongress_send_no_more_data, 
				Toast.LENGTH_SHORT).show();
		       visible_Toast=false;
			}*/
			if(mList.size()>0){
				mListView.setVisibility(View.VISIBLE);
				mNoDataView.setVisibility(View.GONE);
				setListViewHeightBasedOnChildren(mListView);
			}else{
				mListView.setVisibility(View.GONE);
				mNoDataView.setVisibility(View.VISIBLE);
			}
			break;
		case MSG_REFRESH_DATA:
			mPostButton.setClickable(true);
			hideShurufa();
	    	TostShowMsg(R.string.hysq_home_comment_send_msg_success_hint);
	    	getFirstContent();
	    	mIncongressEditText.setText("");
//            if (mEmojoView.getVisibility() == View.VISIBLE) {
//                mEmojoView.setVisibility(View.GONE);
//                mEmojo.setBackgroundResource(R.drawable.hysq_icon_biaoqing);
//            }
            mConnectTime=0;
			if (mDzbb!=null) {
				mDzbb.onChange(); 
			}
			break;
		case MSG_ERROR_SUBMIT:
			mConnectTime++;
			if (mConnectTime == 5) {
		    	TostShowMsg(R.string.hysq_home_comment_send_msg_error_hint);
		    	mIncongressEditText.setText("");
//	            if (mEmojoView.getVisibility() == View.VISIBLE) {
//	                mEmojoView.setVisibility(View.GONE);
//	                mEmojo.setBackgroundResource(R.drawable.hysq_icon_biaoqing);
//	            }
				mConnectTime=0;
			}else{
				sendTopic();
			}
		case MSG_REFESHDONE:
			mScrollView.onRefreshComplete();
			mScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(CommonUtils.getTime());
			mScrollView.setMode(Mode.BOTH);
			break;
		default:
			break;
		}
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
		conn.setRequestMethod(currentType);
		InputStream inStream = conn.getInputStream();
		while ((len = inStream.read(data)) != -1) {
			outStream.write(data, 0, len);
		}
		inStream.close();
		return new String(outStream.toByteArray(), "GBK");// 通过out.Stream.toByteArray获取到写的数据
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd(UMengPage.Page_Metting_Community_Detail); //统计页面
	}

	@Override
	public void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart(UMengPage.Page_Metting_Community_Detail); //统计页面
	}
}