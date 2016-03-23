package com.android.incongress.cd.conference.fragments.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.HistoryPostAdapter;
import com.android.incongress.cd.conference.adapters.ScenicXiuAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.uis.AutoSwipeRefreshLayout;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jacky on 2016/2/22.
 */
public class HistoryPostFragment extends BaseFragment {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private RecyclerView mRcvPost;
    private boolean mIsOpen = true;
    private boolean mIsFirst = true;
    private HistoryPostAdapter mAdapter;
    private Button mBtLoadAgain;
    private LinearLayoutManager mLinearLayoutManager;
    private LinkedList<ScenicXiuBean> mDownBeans = new LinkedList<ScenicXiuBean>();
    private TextView mTvTips;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mIsOpen == false) {
                return;
            }

            int target = msg.what;
            if (target == 0) {
                mBtLoadAgain.setVisibility(View.GONE);
                mTvTips.setVisibility(View.GONE);

                mAdapter = new HistoryPostAdapter(mDownBeans, getActivity());
                mRcvPost.setAdapter(mAdapter);

                if (mIsFirst) {
                    mRcvPost.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                            .paintProvider(mAdapter)
                            .marginProvider(mAdapter)
                            .visibilityProvider(mAdapter)
                            .build());
                    mIsFirst = false;
                }
                mAutoSwipeRefreshLayout.setRefreshing(false);

            } else if (target == 1) {
                mAdapter.notifyDataSetChanged();
                mAutoSwipeRefreshLayout.setRefreshing(false);
            } else if (target == 2) {
                Toast.makeText(getActivity(), "服务器开小差了，请稍后重试", Toast.LENGTH_SHORT).show();
                mAutoSwipeRefreshLayout.setRefreshing(false);
                if (mDownBeans != null && mDownBeans.size() == 0) {
                    mBtLoadAgain.setVisibility(View.VISIBLE);
                }
            } else if (target == 3) {
                if(mDownBeans.size() != 0) {
                    mAdapter.tellNoMoreDate();
                }
            } else if (target == 4) {
                getDownData("-1");
            } else if(target == 6) {
                mTvTips.setVisibility(View.VISIBLE);
                mAutoSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    public HistoryPostFragment() {
    }

    public static HistoryPostFragment getInstance() {
        HistoryPostFragment fragment = new HistoryPostFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_post, null);
        mAutoSwipeRefreshLayout = (AutoSwipeRefreshLayout) view.findViewById(R.id.asrl_layout);
        mRcvPost = (RecyclerView) view.findViewById(R.id.rclv_post);
        mBtLoadAgain = (Button) view.findViewById(R.id.bt_load_again);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);

        mAutoSwipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        mAutoSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDownData("-1");
            }
        });

        mAutoSwipeRefreshLayout.autoRefresh();

        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mRcvPost.setLayoutManager(mLinearLayoutManager);

        mRcvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(mDownBeans.size()>0) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (newState == recyclerView.SCROLL_STATE_IDLE) {
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();

                        if (lastVisibleItem == totalItemCount - 1) {
                            getDownData(mDownBeans.get(mDownBeans.size() - 1).getSceneShowId() + "");
                        }
                    }
                }
            }
        });

        mBtLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoSwipeRefreshLayout.autoRefresh();
                getDownData("-1");
            }
        });

        return view;
    }

    private void getDownData(final String lastSceneShowId) {
        CHYHttpClientUsage.getInstanse().doGetSceneShowByUser(AppApplication.conId + "", lastSceneShowId + "", AppApplication.userId + "", AppApplication.userType + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (lastSceneShowId.equals("-1")) {
                    Gson gson = new Gson();
                    String jsonArray = "";
                    try {
                        jsonArray = response.getJSONArray("sceneShowArray").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mDownBeans = gson.fromJson(jsonArray, new TypeToken<LinkedList<ScenicXiuBean>>() {}.getType());

                    if(mDownBeans.size() == 0) {
                        mHandler.sendEmptyMessage(6);
                    }else {
                        mHandler.sendEmptyMessage(0);
                    }

                } else {
                    Gson gson = new Gson();
                    String jsonArray = "";
                    try {
                        jsonArray = response.getJSONArray("sceneShowArray").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    List<ScenicXiuBean> temp = new LinkedList<ScenicXiuBean>();
                    temp = (LinkedList<ScenicXiuBean>) gson.fromJson(jsonArray, new TypeToken<LinkedList<ScenicXiuBean>>() {}.getType());
                    if(temp.size() == 0) {
                    }else {
                        mDownBeans.addAll((LinkedList<ScenicXiuBean>) gson.fromJson(jsonArray, new TypeToken<LinkedList<ScenicXiuBean>>() {}.getType()));
                        mHandler.sendEmptyMessage(1);
                    }

                }
            }
        });
    }
}
