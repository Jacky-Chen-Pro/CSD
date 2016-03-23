package com.android.incongress.cd.conference.fragments.message_station;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.MessageStationAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MessageBean;
import com.android.incongress.cd.conference.uis.AutoSwipeRefreshLayout;
import com.android.incongress.cd.conference.uis.DividerItemDecoration;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jacky on 2016/1/29.
 */
public class MessageStationFragment extends BaseFragment {
    private AutoSwipeRefreshLayout mAsrlMessages;
    private List<MessageBean> mMessageBeans = null;
    private MessageStationAdapter mAdapter;
    private RecyclerView mRcvMsgs;
    private int mCurrentPage = 1; //从页数
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mIsFirst = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(getActivity() == null)
                return;

            super.handleMessage(msg);
            int target = msg.what;
            if (target == HANDLER_DATA_FIRST) {
                mAdapter = new MessageStationAdapter(getActivity(), mMessageBeans);
                mRcvMsgs.setAdapter(mAdapter);

                if (mIsFirst) {
                    mRcvMsgs.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                            .paintProvider(mAdapter)
                            .build());
                    mIsFirst = false;
                }

                mAsrlMessages.setRefreshing(false);
                mAdapter.setOnItemClickListener(new MessageStationAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, MessageBean bean) {
                        if (bean != null && bean.getType() == 2) {
                            WebViewContainerActivity.startWebViewContainerActivity(getActivity(), bean.getUrl(), bean.getContent());
                        }
                    }
                });
                mCurrentPage++;
            } else if (target == HANDLER_DATA_MORE) {
                mCurrentPage++;
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 是否正在加载更多
     */
    private boolean isLoadingMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_station, null, false);
        mAsrlMessages = (AutoSwipeRefreshLayout) view.findViewById(R.id.asrl_messages);
        mRcvMsgs = (RecyclerView) view.findViewById(R.id.rclv_msgs);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcvMsgs.setLayoutManager(mLinearLayoutManager);
        mRcvMsgs.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mRcvMsgs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                // lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if (isLoadingMore) {
                    } else {
                        getDatas(mCurrentPage);//这里多线程也要手动控制isLoadingMore
                    }
                }
            }
        });

        mAsrlMessages.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                getDatas(mCurrentPage);
            }
        });

        mAsrlMessages.setColorSchemeResources(R.color.theme_color);
        mAsrlMessages.autoRefresh();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLookTime();
    }

    private void updateLookTime() {
        CHYHttpClientUsage.getInstanse().doCreateUserLooked(AppApplication.userId+"", AppApplication.userType + "", AppApplication.TOKEN_IMEI, Constants.LookTimeMsgStation, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int state = response.getInt("state");
                    if(state != 1) {
                        updateLookTime();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void getDatas(final int currentPage) {
        CHYHttpClientUsage.getInstanse().doGetTokenList(AppApplication.conId + "", Constants.PAGE_SIZE, currentPage + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                if (currentPage != 1)
                    isLoadingMore = true;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isLoadingMore = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast(getString(R.string.server_error));
                mAsrlMessages.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());

                try {
                    int state = response.getInt("state");
                    if (state == 1) {
                        Gson gson = new Gson();
                        if (mCurrentPage == 1) {
                            mMessageBeans = gson.fromJson(response.getString("tokenList"), new TypeToken<List<MessageBean>>() {}.getType());
                            mHandler.sendEmptyMessage(HANDLER_DATA_FIRST);
                        } else {
                            List<MessageBean> tempBeans = gson.fromJson(response.getString("tokenList"), new TypeToken<List<MessageBean>>() {}.getType());
                            if (tempBeans != null && tempBeans.size() > 0) {
                                mMessageBeans.addAll(tempBeans);
                                mHandler.sendEmptyMessage(HANDLER_DATA_MORE);
                            }
                        }
                    } else {
                        ToastUtils.showShorToast(getString(R.string.no_more_date));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
