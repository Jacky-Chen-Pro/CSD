package com.android.incongress.cd.conference.fragments.professor_secretary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Jacky on 2016/2/22.
 * <p/>
 * 回答问题列表
 */
public class AnswerQuestionActivity extends BaseActivity {
    private static final String INTENT_QUESTION = "question";
    private TextView mTvQuestionName, mTvQuestionTime, mTvContent, mTvTitleText;
    private EditText mEtAnswer;
    private LinearLayout mLlRightView;
    private ImageView mIvBack;
    private SceneShowArrayBean mQuestion;

    public static final void startAnswerQuestionActivity(Context ctx, SceneShowArrayBean question) {
        Intent intent = new Intent();
        intent.setClass(ctx, AnswerQuestionActivity.class);
        intent.putExtra(INTENT_QUESTION, question);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        try {
            mQuestion = (SceneShowArrayBean) getIntent().getSerializableExtra(INTENT_QUESTION);
        }catch(Exception e){
            e.printStackTrace();
        }
        mIvBack = (ImageView) findViewById(R.id.title_back);
        mTvQuestionName = (TextView) findViewById(R.id.tv_author_name);
        mTvQuestionTime = (TextView) findViewById(R.id.tv_time);
        mTvContent = (TextView) findViewById(R.id.tv_question_content);
        mTvTitleText = (TextView) findViewById(R.id.title_text);
        mLlRightView = (LinearLayout) findViewById(R.id.title_costom);
        mEtAnswer = (EditText) findViewById(R.id.et_answer);

        View view = CommonUtils.initView(this, R.layout.title_right_textview);
        ((TextView)view).setText(R.string.send_question);
        mLlRightView.addView(view);
        mLlRightView.setVisibility(View.VISIBLE);
        mTvTitleText.setText(R.string.answer_question);

        mTvQuestionName.setText(mQuestion.getAuthor());
        mTvQuestionTime.setText(mQuestion.getTimeShow());
        try {
            String content = URLDecoder.decode(mQuestion.getContent(), Constants.ENCODING_UTF8);
            mTvContent.setText(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        initEvents();
    }

    private void initEvents() {
        mLlRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmpty(mEtAnswer.getText().toString())) {
                    ToastUtils.showShorToast(getString(R.string.enter_content));
                    return;
                }

                String answer = "";
                try {
                    answer = URLEncoder.encode(mEtAnswer.getText().toString(), Constants.ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                CHYHttpClientUsage.getInstanse().doSceneShowAnswer(mQuestion.getSceneShowId()+"", AppApplication.userId + "", answer, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                         try {
                             int state = response.getInt("state");
                             if(state == 1) {
                                 ToastUtils.showShorToast(getString(R.string.succcess_reply));
                                 AnswerQuestionActivity.this.finish();
                             }else {
                                 ToastUtils.showShorToast(getString(R.string.fail_reply));
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
        });

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnswerQuestionActivity.this.finish();
            }
        });
    }
}
