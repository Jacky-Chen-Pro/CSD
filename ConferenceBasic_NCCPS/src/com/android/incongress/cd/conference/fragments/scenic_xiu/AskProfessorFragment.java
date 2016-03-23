package com.android.incongress.cd.conference.fragments.scenic_xiu;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Jacky on 2016/2/2.
 * 向某专家提问
 */
public class AskProfessorFragment extends BaseFragment {

    public static final String ASK_SPEAKER_NAME = "speakerName";
    public static final String ASK_SPEAKER_ID = "speakerId";

    private String mSpeakerName;
    private int mSpeakerId;

    private EditText mEtQuestion;

    @Override
    public void onStart() {
        super.onStart();
    }

    public static AskProfessorFragment getInstance(String speakerName,int speakerId) {
        AskProfessorFragment fragment = new AskProfessorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ASK_SPEAKER_NAME, speakerName);
        bundle.putInt(ASK_SPEAKER_ID, speakerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSpeakerName = getArguments().getString(ASK_SPEAKER_NAME);
            mSpeakerId = getArguments().getInt(ASK_SPEAKER_ID);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_ask_professor, null);
        mEtQuestion = (EditText) view.findViewById(R.id.et_question);
        mEtQuestion.setHint("@" + mSpeakerName);
        return view;
    }
    private ProgressDialog mProgressDialog;

    public void setRightListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtQuestion.getText().toString();
                try {
                    content = URLEncoder.encode(content, Constants.ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(!StringUtils.isEmpty(content)) {
                    CHYHttpClientUsage.getInstanse().doCreateSceneShowQuestion(AppApplication.conId + "", AppApplication.userId + "",AppApplication.userType + "", content, mSpeakerId+"",new JsonHttpResponseHandler(){
                        @Override
                        public void onStart() {
                            super.onStart();
                            mProgressDialog = ProgressDialog.show(getActivity(),null, "loading...");
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            if(mProgressDialog!=null && mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            ToastUtils.showShorToast("服务器开小差了，请稍后重试");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            MyLogger.jLog().i(response.toString());
                            try {
                                int state = response.getInt("state");
                                if(state == 1) {
                                    ToastUtils.showShorToast("提问成功");
                                    InputMethodManager imm = (InputMethodManager)
                                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(mEtQuestion.getWindowToken(), 0);
                                    performback();
                                }else {
                                    ToastUtils.showShorToast("提问失败，请稍后重试");
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    ToastUtils.showShorToast("提问不许为空");
                }
            }
        });
    }
}
