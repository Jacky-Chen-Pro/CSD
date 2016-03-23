package com.android.incongress.cd.conference.fragments.interactive;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.HdSessionAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.HdSessionBean;
import com.android.incongress.cd.conference.uis.AutoSwipeRefreshLayout;
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
 * Created by Jacky on 2016/1/28.
 * <p/>
 * 现场互动模块
 */
public class HdSessionFragment extends BaseFragment {
    private RecyclerView mRcvSession;
    private List<HdSessionBean> mHdSessionNow;
    private List<HdSessionBean> mHdSessionWill;
    private HdSessionAdapter mAdapter;
    private AutoSwipeRefreshLayout mAsrlSessions;

    private boolean mIsFirst = true;
    private String mEmptyMsg;
    private boolean mIsOpen = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(mIsOpen == false)
                return;

            int target = msg.what;
            if (target == 1) {
                mAdapter = new HdSessionAdapter(getActivity(), mHdSessionNow, mHdSessionWill);
                mRcvSession.setAdapter(mAdapter);

                if (mIsFirst && getActivity() != null) {
                    mRcvSession.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                            .paintProvider(mAdapter)
                            .visibilityProvider(mAdapter)
                            .marginProvider(mAdapter)
                            .build());
                    mIsFirst = false;
                }
            } else {
                ToastUtils.showShorToast(mEmptyMsg);
            }
            mAsrlSessions.setRefreshing(false);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hd_session, null);

        mRcvSession = (RecyclerView) view.findViewById(R.id.rcv_sessions);
        mAsrlSessions = (AutoSwipeRefreshLayout) view.findViewById(R.id.asrl_sessions);
        mAsrlSessions.setColorSchemeResources(R.color.theme_color);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        mRcvSession.setLayoutManager(manager);

        mAsrlSessions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDatas();
            }
        });
        mAsrlSessions.autoRefresh();

        showGuideInfo();

        return view;
    }

    /**
     * 显示指示页
     */
    private void showGuideInfo() {
        if (AppApplication.getSPIntegerValue(Constants.GUIDE_INTERACTIVE) != 1) {

            if(getActivity() != null) {
                getActivity().findViewById(R.id.home_guide).setVisibility(View.VISIBLE);
                ((ImageView) getActivity().findViewById(R.id.home_guide)).setImageResource(R.drawable.interactive_guide);
                (getActivity().findViewById(R.id.home_guide)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().findViewById(R.id.home_guide).setVisibility(View.GONE);
                        AppApplication.setSPIntegerValue(Constants.GUIDE_INTERACTIVE, 1);
                    }
                });
            }
        }
    }

    private void initDatas() {
        CHYHttpClientUsage.getInstanse().doGetHdSession(AppApplication.conId + "", Constants.LanguageChinese + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        MyLogger.jLog().i(response.toString());
                        Gson gson = new Gson();
                        try {
                            int state = response.getInt("state");
                            if (state == 1) {
                                mHdSessionNow = gson.fromJson(response.getString("hdNowArray"), new TypeToken<List<HdSessionBean>>() {
                                }.getType());
                                mHdSessionWill = gson.fromJson(response.getString("hdWillArray"), new TypeToken<List<HdSessionBean>>() {
                                }.getType());
                                mHandler.sendEmptyMessage(1);
                            } else {
                                mEmptyMsg = response.getString("msg");
                                mHandler.sendEmptyMessage(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        ToastUtils.showShorToast("服务器开小差了，请稍后重试");
                        mAsrlSessions.setRefreshing(false);
                    }
                }
        );
    }

    public void setRightListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewContainerActivity.startWebViewContainerActivity(getActivity(), Constants.HdSession_QUESTION_LIST_OFFICIAL + "&lan=cn", getString(R.string.question_list));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsOpen = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsOpen = true;
    }
}
