package com.android.incongress.cd.conference.fragments.professor_secretary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MyQuestionFragmentAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.uis.DividerItemDecoration;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 * 我收到的提问
 */
public class ProfessorQuestionFragment extends BaseFragment {
    private ProgressDialog mProgressDialog;
    private List<SceneShowArrayBean> mSceneShowQuestionBeans;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MyQuestionFragmentAdapter mAdapter;

    private static final String QUESTION_LIST = "questions";

    public ProfessorQuestionFragment() {
    }

    public static ProfessorQuestionFragment getInstance(List<SceneShowArrayBean> sceneShowArrayBeans) {
        ProfessorQuestionFragment fragment = new ProfessorQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(QUESTION_LIST, (Serializable) sceneShowArrayBeans);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSceneShowQuestionBeans = (List<SceneShowArrayBean>) getArguments().getSerializable(QUESTION_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myquestion, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MyQuestionFragmentAdapter(getActivity(), mSceneShowQuestionBeans);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .paintProvider(mAdapter)
                .visibilityProvider(mAdapter)
                .marginProvider(mAdapter)
                .build());

        mAdapter.setOnItemClickListener(new MyQuestionFragmentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SceneShowArrayBean question) {
                if(question.getIsHuiFu() == 1){
                    ToastUtils.showShorToast("已经回复过了");
                    return;
                }

                AnswerQuestionActivity.startAnswerQuestionActivity(getActivity(),question);
            }
        });

        return view;
    }

    private void initData() {
        CHYHttpClientUsage.getInstanse().doGetSceneShowQuestions(AppApplication.conId + "", AppApplication.userId + "", "-1", new JsonHttpResponseHandler("gbk") {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(getActivity(), null, "loading...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());

                try {
                    int state = response.getInt("state");

                    if (state == 0) {
                        //没有数据
//                       mHandler.sendEmptyMessage(HANDLER_NO_DATA);
                    } else {
                        //有数据
                        JSONArray jsonArray = response.getJSONArray("sceneShowArray");
                        Gson gson = new Gson();
                        mSceneShowQuestionBeans = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SceneShowArrayBean>>() {
                        }.getType());
//                        mHandler.sendEmptyMessage(HANDLER_DATA_FIRST);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                MyLogger.jLog().i("error=" + headers);
            }
        });
    }
}
