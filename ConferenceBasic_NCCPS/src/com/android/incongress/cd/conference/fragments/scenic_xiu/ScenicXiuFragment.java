package com.android.incongress.cd.conference.fragments.scenic_xiu;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.adapters.ScenicXiuAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommentArrayBean;
import com.android.incongress.cd.conference.beans.SceneShowTopBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.fragments.OnlyWebViewFragment;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerSearchFragment;
import com.android.incongress.cd.conference.uis.AutoSwipeRefreshLayout;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/13.
 * <p/>
 * 现场秀模块
 */
public class ScenicXiuFragment extends BaseFragment implements ScenicXiuAdapter.NewsAndActivitysListener {
    private AutoSwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRclvScenic;
    private ScenicXiuAdapter mAdapter;
    private Button mBtLoadAgain;
    private SceneShowTopBean mTopBean;
    private View mScenicXiuTitle;

    private RelativeLayout mRlCommentArea;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean mIsFirst = true;

    public static final String BROAD_SCENIC_XIU_ID = "sceneXiuId";
    public static final String BROAD_POSITION = "position";
    public static final String BROAD_COMMENT_ID = "commentId";
    public static final String BROAD_PARENT_NAME = "parentName";
    public static final String  BROAD_PARENT_ID = "parentId";

    private CommentClickReceiver mCommentReceiver;
    private Button mBtSend;

    private EditText mEtComment;

    private LinkedList<ScenicXiuBean> mDownBeans = new LinkedList<ScenicXiuBean>();

    private boolean mIsOpen = true;


    public ScenicXiuFragment() {
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mIsOpen == false) {
                return;
            }

            int target = msg.what;
            if (target == 0) {
                mBtLoadAgain.setVisibility(View.GONE);
                mDownBeans.addFirst(new ScenicXiuBean(0));

                mAdapter = new ScenicXiuAdapter(mDownBeans, mTopBean, ScenicXiuFragment.this, getActivity());
                mRclvScenic.setAdapter(mAdapter);

                if (mIsFirst) {

                    mRclvScenic.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                            .paintProvider(mAdapter)
                            .marginProvider(mAdapter)
                            .build());
                    mIsFirst = false;
                }

                mSwipeRefreshLayout.setRefreshing(false);
            } else if (target == 1) {
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            } else if (target == 2) {
                Toast.makeText(getActivity(), "服务器开小差了，请稍后重试", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
                if (mDownBeans != null && mDownBeans.size() == 0) {
                    mBtLoadAgain.setVisibility(View.VISIBLE);
                }
            } else if (target == 3) {
                mAdapter.tellNoMoreDate();
            } else if (target == 4) {
                getDownData("-1");
            }
        }
    };

    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenic_xiu, null);
        mSwipeRefreshLayout = (AutoSwipeRefreshLayout) view.findViewById(R.id.srl_scenic_xiu);
        mRclvScenic = (RecyclerView) view.findViewById(R.id.rclv_scenic_xiu);
        mBtLoadAgain = (Button) view.findViewById(R.id.bt_load_again);

        mRlCommentArea = (RelativeLayout) view.findViewById(R.id.rl_comment_area);
        mEtComment = (EditText) view.findViewById(R.id.et_make_comment);
        mBtSend = (Button) view.findViewById(R.id.bt_send_comment);

        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtComment.getText().toString().trim();
                try {
                    content = URLEncoder.encode(content, Constants.ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(content)) {
                    ToastUtils.showShorToast(getString(R.string.comment_no_empty));
                } else {
                    CHYHttpClientUsage.getInstanse().doSceneShowComment(mScenicXiuId + "", AppApplication.userId + "", AppApplication.userType + "", content, mCommentId + "", new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            mProgressDialog = ProgressDialog.show(getActivity(), null, "loading...");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            MyLogger.jLog().i(response.toString());
                            try {
                                int state = response.getInt("state");
                                if (state == 1) {
                                    Gson gson = new Gson();
                                    List<CommentArrayBean> comments = gson.fromJson(response.getString("commentArray"), new TypeToken<List<CommentArrayBean>>() {
                                    }.getType());
                                    int commentCount = response.getInt("commentCount");
                                    for (int i = 0; i < comments.size(); i++) {
                                        CommentArrayBean bean = comments.get(i);
                                        String name = URLDecoder.decode(bean.getUserName(), Constants.ENCODING_UTF8);
                                        bean.setUserName(URLDecoder.decode(name, Constants.ENCODING_UTF8));
                                        if (bean.getParentId() != -1) {
                                            String parentName = URLDecoder.decode(bean.getParentName(), Constants.ENCODING_UTF8);
                                            bean.setParentName(URLDecoder.decode(parentName, Constants.ENCODING_UTF8));
                                        }
                                    }

                                    if (comments != null && comments.size() > 0) {
                                        mDownBeans.get(mPosition).setCommentArray(comments);
                                        mDownBeans.get(mPosition).setCommentCount(commentCount);
                                        mAdapter.notifyItemChanged(mPosition);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            if (mProgressDialog != null && mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            mRlCommentArea.setVisibility(View.GONE);
                            toggleShurufa();
                            mEtComment.setHint(StringUtils.EMPTY_STR);
                            mEtComment.setText(StringUtils.EMPTY_STR);
                        }
                    });
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpData();
            }
        });

        mRclvScenic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mAdapter != null) {
                    mAdapter.clearAllCommentArea();
                    return false;
                } else {
                    return true;
                }
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRclvScenic.setLayoutManager(mLinearLayoutManager);

        mRlCommentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRlCommentArea.setVisibility(View.GONE);
                mEtComment.setHint(R.string.schedule_comment_sth);
                ((HomeActivity) getActivity()).toggleShurufa();
            }
        });

        mRclvScenic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == recyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    if (lastVisibleItem == totalItemCount - 1) {
                        getDownData(mDownBeans.get(mDownBeans.size() - 1).getSceneShowId() + "");
                    }
                }


            }
        });

        mBtLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeRefreshLayout.autoRefresh();
                getDownData("-1");
            }
        });

        mSwipeRefreshLayout.autoRefresh();

        //注册广播接收器
        registerMessageReceiver();

        showGuideInfo();
        return view;
    }

    /**
     * 显示指示页
     */
    private void showGuideInfo() {
        if (AppApplication.getSPIntegerValue(Constants.GUIDE_XIU) != 1) {
            getActivity().findViewById(R.id.home_guide).setVisibility(View.VISIBLE);
            ((ImageView) getActivity().findViewById(R.id.home_guide)).setImageResource(R.drawable.show_guide);
            ((ImageView) getActivity().findViewById(R.id.home_guide)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().findViewById(R.id.home_guide).setVisibility(View.GONE);
                    AppApplication.setSPIntegerValue(Constants.GUIDE_XIU, 1);
                }
            });
        }
    }

    //设置标题栏的点击事件
    public void setScenicXiuTitle(View view) {
        mScenicXiuTitle = view;

        ImageView askQuestion = (ImageView) mScenicXiuTitle.findViewById(R.id.iv_ask_professor);
        ImageView makePost = (ImageView) mScenicXiuTitle.findViewById(R.id.iv_make_post);

        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "" , "");
                    ToastUtils.showShorToast(getString(R.string.login_first));
                    return;
                }

                SpeakerSearchFragment speaker = SpeakerSearchFragment.getInstance(SpeakerSearchFragment.TYPE_FROM_SCENIC_XIU);
                action(speaker, getString(R.string.scenic_xiu_speaker_list), false, false);
            }
        });
        makePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "" , "");
                    ToastUtils.showShorToast(getString(R.string.login_first));
                    return;
                }

                MakePostFragment fragment = new MakePostFragment();
                View postView = LayoutInflater.from(getActivity()).inflate(R.layout.include_title_make_post, null);
                fragment.setRightView(postView);
                action(fragment, R.string.create_post, postView, false, false);
            }
        });
    }


    /**
     * 获取上方的新闻和展商活动
     */
    private void getUpData() {
        CHYHttpClientUsage.getInstanse().doGetSceneShowTop(AppApplication.conId + "", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());
                Gson gson = new Gson();
                mTopBean = gson.fromJson(response.toString(), SceneShowTopBean.class);
                mHandler.sendEmptyMessage(4);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mHandler.sendEmptyMessage(2);
            }
        });
    }


    /**
     * 获取下方现场秀列表
     *
     * @param lastSceneShowId
     */
    private void getDownData(final String lastSceneShowId) {
        CHYHttpClientUsage.getInstanse().doGetSceneShowDown(AppApplication.conId + "", lastSceneShowId, AppApplication.userId + "", AppApplication.userType + "", new JsonHttpResponseHandler("gbk") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());

                try {
                    int state = response.getInt("state");
                    if (state == 0) {
                        mHandler.sendEmptyMessage(3);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (lastSceneShowId.equals("-1")) {
                    Gson gson = new Gson();
                    String jsonArray = "";
                    try {
                        jsonArray = response.getJSONArray("sceneShowArray").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mDownBeans = gson.fromJson(jsonArray, new TypeToken<LinkedList<ScenicXiuBean>>() {
                    }.getType());

                    mHandler.sendEmptyMessage(0);
                } else {
                    Gson gson = new Gson();
                    String jsonArray = "";
                    try {
                        jsonArray = response.getJSONArray("sceneShowArray").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mDownBeans.addAll((LinkedList<ScenicXiuBean>) gson.fromJson(jsonArray, new TypeToken<LinkedList<ScenicXiuBean>>() {
                    }.getType()));
                    mHandler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mHandler.sendEmptyMessage(2);
            }
        });
    }

    @Override
    public void doWhenNewsOrActivityClicked(int type, String url, String title) {
        OnlyWebViewFragment fragment;
        if (type == ScenicXiuAdapter.TYPE_MEDIA_CENTER) {
            fragment = OnlyWebViewFragment.getInstance(mTopBean.getGotoUrl1());
            action(fragment, getString(R.string.media_center), false, false);
        } else if(type == ScenicXiuAdapter.TYPE_LIVE_SHOW){
            fragment = OnlyWebViewFragment.getInstance(mTopBean.getGotoUrl2());
            action(fragment, getString(R.string.industrial_events), false, false);
        } else if(type == ScenicXiuAdapter.TYPE_NEWS || type == ScenicXiuAdapter.TYPE_NOTIFY) {
            fragment = OnlyWebViewFragment.getInstance(url);
            action(fragment, title, false, false);
        }
    }

    public static final String COMMENT_CLICK_RECEIVED_ACTION_NORMAL = "click_action_normal";
    public static final String COMMENT_CLICK_RECEIVED_ACTION_COMMENT = "click_action_comment";
    public static final String GO_TO_LOGIN_FIRST = "go_login";
    private int mScenicXiuId = 0;
    private int mPosition = 0;
    private int mCommentId = 0;
    private String mParentName = "";
    private String mUserId = "";

    //评论广播接收器
    class CommentClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mScenicXiuId = 0;
            mPosition = 0;
            mCommentId = 0;
            mParentName = "";
            mUserId = "";

            if(GO_TO_LOGIN_FIRST.equals(intent.getAction())) {
                LoginActivity.startLoginActivity(getActivity(),LoginActivity.TYPE_NORMAL,"","", "" , "");
                return;
            }
            mScenicXiuId = intent.getIntExtra(BROAD_SCENIC_XIU_ID, 0);
            mPosition = intent.getIntExtra(BROAD_POSITION, 0);
            mCommentId = intent.getIntExtra(BROAD_COMMENT_ID, -1);
            mParentName = intent.getStringExtra(BROAD_PARENT_NAME);
            mUserId = intent.getStringExtra(BROAD_PARENT_ID);

            if( (AppApplication.userId +"").equals(mUserId))  {
                return;
            }

            mRlCommentArea.setVisibility(View.VISIBLE);
            toggleShurufa();
            mEtComment.requestFocus();

            if (COMMENT_CLICK_RECEIVED_ACTION_NORMAL.equals(intent.getAction())) {

            } else if (COMMENT_CLICK_RECEIVED_ACTION_COMMENT.equals(intent.getAction())) {
                mEtComment.setHint("@" + mParentName);
            }
        }
    }

    //注册评论广播接收器
    public void registerMessageReceiver() {
        mCommentReceiver = new CommentClickReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(COMMENT_CLICK_RECEIVED_ACTION_COMMENT);
        filter.addAction(COMMENT_CLICK_RECEIVED_ACTION_NORMAL);
        filter.addAction(GO_TO_LOGIN_FIRST);
        getActivity().registerReceiver(mCommentReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCommentReceiver != null) {
            getActivity().unregisterReceiver(mCommentReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsOpen = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        CHYHttpClientUsage.getInstanse().doCreateUserLooked(AppApplication.userId + "", AppApplication.userType + "", AppApplication.TOKEN_IMEI, Constants.LookTimeScenicXiu, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        mIsOpen = false;
    }
}
