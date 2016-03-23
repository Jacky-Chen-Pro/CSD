package com.android.incongress.cd.conference.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.fragments.cit_college.CitCollegeActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.exhibitor.ExhibitorsFragment;
import com.android.incongress.cd.conference.fragments.interactive.HdSessionFragment;
import com.android.incongress.cd.conference.fragments.me.MeFragment;
import com.android.incongress.cd.conference.fragments.meeting_guide.MeetingGuideFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleFragment;
import com.android.incongress.cd.conference.fragments.message_station.MessageStationFragment;
import com.android.incongress.cd.conference.fragments.my_schedule.MyScheduleFragment;
import com.android.incongress.cd.conference.fragments.professor_secretary.SecretaryActivity;
import com.android.incongress.cd.conference.fragments.scenic_xiu.ScenicXiuFragment;
import com.android.incongress.cd.conference.fragments.search_schedule.SearchScheduleFragment;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerSearchFragment;
import com.android.incongress.cd.conference.fragments.wall_poster.WallPosterFragment;
import com.android.incongress.cd.conference.utils.ArrayUtils;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jacky_Chen
 * @time 2014年11月24日  将home界面中的margin_left和margin_right的距离32去除，界面更好看些。
 * @Time 2014-12-1 Jacky_Chen 添加精彩回顾
 *
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = HomeFragment.class.getSimpleName();
    private static final int HANDLER_NUMS = 0x0001;

    //现场秀消息数，消息站消息数
    private TextView mTvScenicXiuNums, mTvMsgNums;
    private LinearLayout mLlMessageCount, mLlSceneShowCount;
    private int mMessageCount, mSceneShowCount, mXchdCount;
    private ImageView mIvHdSessionOnTips;

    //专家秘书模块需要
    private List<ActivityBean> mAllActivitys = new ArrayList<>();
    private List<ActivityBean> mValidActivitys = new ArrayList<>();
    private List<SceneShowArrayBean> mScenceShowBeans = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private int mTaskNum,mQuestionNum;

    @Override
    public void onResume() {
        super.onResume();
        getHomeNums();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int target = msg.what;

            if (target == HANDLER_NUMS) {
                if (mXchdCount > 0) {
                    mIvHdSessionOnTips.setVisibility(View.VISIBLE);
                } else {
                    mIvHdSessionOnTips.setVisibility(View.GONE);
                }

                if (mMessageCount > 0) {
                    mLlMessageCount.setVisibility(View.VISIBLE);
                    if(mMessageCount< 99) {
                        mTvMsgNums.setText(mMessageCount + "");
                    }else {
                        mTvMsgNums.setText(99+"");
                    }
                }

                if (mSceneShowCount > 0) {
                    mLlSceneShowCount.setVisibility(View.VISIBLE);
                    if(mSceneShowCount < 99) {
                        mTvScenicXiuNums.setText(mSceneShowCount + "");
                    }else{
                        mTvScenicXiuNums.setText(99 + "");
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_look_schedule:
                ImageView view = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
                if(AppApplication.systemLanguage == 1) {
                    view.setImageResource(R.drawable.schedule_switch_cn);
                }else {
                    view.setImageResource(R.drawable.schedule_switch);
                }
                MeetingScheduleFragment scheduleFragment = new MeetingScheduleFragment();
                scheduleFragment.setRightListener(view);
                action(scheduleFragment, R.string.home_schedule, view, false, false);

                CHYHttpClientUsage.getInstanse().doUpdateInfo(AppApplication.conId + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        MyLogger.jLog().i("提示信息：" + response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
                break;
            case R.id.iv_search_schedule:
                action(new SearchScheduleFragment(), R.string.home_search, false, false, false);
                break;
            case R.id.iv_my_schedule:
                TextView my_schedule = (TextView) CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                my_schedule.setText(R.string.my_schedule_edit);
                MyScheduleFragment schedule = new MyScheduleFragment();
                schedule.setRightView(my_schedule);
                action(schedule, R.string.home_my_schedule, my_schedule, false, false, true);
                break;
            case R.id.ll_live_a_and_a:
                HdSessionFragment hdFragment = new HdSessionFragment();
                View hdTtile = CommonUtils.initView(this.getActivity(), R.layout.title_hdsession);
                hdFragment.setRightListener(hdTtile);
                action(hdFragment, R.string.home_interactive, hdTtile, false, false);
                break;
            case R.id.ll_scenic_xiu:
                View scenicTitle = CommonUtils.initView(this.getActivity(), R.layout.scenic_xiu_title);
                LinearLayout mlayout = (LinearLayout) scenicTitle.findViewById(R.id.ll_senic_xiu_title);
                ScenicXiuFragment xiu = new ScenicXiuFragment();
                xiu.setScenicXiuTitle(mlayout);
                action(xiu, R.string.home_scene_xiu, scenicTitle, false, false, false);
                break;
//            case R.id.ll_scretary:
//                CHYHttpClientUsage.getInstanse().doGetSceneShowQuestions(AppApplication.conId + "", AppApplication.userId + "", "-1", new JsonHttpResponseHandler("gbk") {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        mProgressDialog = ProgressDialog.show(getActivity(),null,"loading...");
//                        mQuestionNum = 0;
//                        mTaskNum = 0;
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//
//                        MyLogger.jLog().i(response.toString());
//
//                        try {
//                            int state = response.getInt("state");
//
//                            if(state == 0){
//                                mQuestionNum = 0;
//                            }else {
//                                //有数据
//                                JSONArray jsonArray = response.getJSONArray("sceneShowArray");
//                                Gson gson = new Gson();
//                                mScenceShowBeans = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<SceneShowArrayBean>>() {}.getType());
//                                mQuestionNum = response.getInt("questionCount");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        new TaskAsyncTask().execute();
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        MyLogger.jLog().i("JSONObject");
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        super.onFailure(statusCode, headers, responseString, throwable);
//                        MyLogger.jLog().i("Throwable");
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        MyLogger.jLog().i("JSONArray");
//                    }
//                });
//                break;
            case R.id.ll_exhibitor:
                action(new ExhibitorsFragment(), R.string.home_exhibitors, false, true, true);
                break;
            case R.id.ll_speaker_search:
                SpeakerSearchFragment speaker = SpeakerSearchFragment.getInstance(SpeakerSearchFragment.TYPE_FROM_HOME);
                action(speaker, R.string.home_speakersearch, false, false, false);
                break;
            case R.id.ll_meeting_guide:
                action(new MeetingGuideFragment(), R.string.home_meetingguide, false, true, true);
                break;
            case R.id.ll_cit_college:
                getActivity().startActivity(new Intent(getActivity(), CitCollegeActivity.class));
                break;
            case R.id.ll_me:
                View scane = CommonUtils.initView(this.getActivity(), R.layout.title_right_image);
                ((ImageView) scane).setImageResource(R.drawable.scane_scane);
                MeFragment me = new MeFragment();
                me.setRightView(scane);
                action(me, R.string.home_me, scane, false, false);
                break;
            case R.id.ll_message_station:
                action(new MessageStationFragment(), R.string.home_messagestation, false, false);
                break;
            case R.id.iv_top_cit:
                String lan = "";
                if(AppApplication.systemLanguage == 1) {
                    lan = "cn";
                }else {
                    lan = "en";
                }
                if(Constants.isDebug) {
                    WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                            getString(R.string.cit_live_url_test, AppApplication.conId, lan, AppApplication.userId, AppApplication.userType), getString(R.string.home_icon_cit));
                }else {
                    WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                            getString(R.string.cit_live_url_official, AppApplication.conId, lan, AppApplication.userId, AppApplication.userType), getString(R.string.home_icon_cit));
                }
                break;
            case R.id.ll_hands_on:
                String lan2 = "";
                if(AppApplication.systemLanguage == 1) {
                    lan2 = "cn";
                }else {
                    lan2 = "en";
                }
                if(Constants.isDebug) {
                    WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                            getString(R.string.hands_on_site_test, AppApplication.conId, lan2, AppApplication.userId, AppApplication.userType), getString(R.string.home_hands_on));
                }else {
                    WebViewContainerActivity.startWebViewContainerActivity(getActivity(),
                            getString(R.string.hands_on_site_official, AppApplication.conId, lan2, AppApplication.userId, AppApplication.userType), getString(R.string.home_hands_on));
                }
                break;
            case R.id.rl_bb:
                action(new WallPosterFragment(), R.string.home_wallpaper, false, false);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_cit2016, null);

        initViews(view);
        initEvents(view);
        return view;
    }

    /**
     * 获取数据
     */
    private void getHomeNums() {
        CHYHttpClientUsage.getInstanse().doGetLookCount(AppApplication.conId + "", AppApplication.userId + "", AppApplication.userType + "", AppApplication.TOKEN_IMEI, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i("getLookCount" + response.toString());

                try {
                    mMessageCount = response.getInt("tokenMessageCount");
                    mSceneShowCount = response.getInt("sceneShowCount");
                    mXchdCount = response.getInt("xchdCount");
                } catch (Exception e) {
                    e.printStackTrace();
                    mMessageCount = 0;
                    mSceneShowCount = 0;
                    mXchdCount = 0;
                }
                mHandler.sendEmptyMessage(HANDLER_NUMS);
            }
        });
    }

    private void initEvents(View view) {
        mIvHdSessionOnTips = (ImageView) view.findViewById(R.id.iv_hd_session_on);
        mLlMessageCount = (LinearLayout) view.findViewById(R.id.ll_msg_nums);
        mLlSceneShowCount = (LinearLayout) view.findViewById(R.id.ll_scenic_xiu_num);

//        view.findViewById(R.id.ll_watch_schedule).setOnClickListener(this);
//        view.findViewById(R.id.ll_search_schedule).setOnClickListener(this);
//        view.findViewById(R.id.ll_my_schedule).setOnClickListener(this);
        view.findViewById(R.id.iv_look_schedule).setOnClickListener(this);
        view.findViewById(R.id.iv_search_schedule).setOnClickListener(this);
        view.findViewById(R.id.iv_my_schedule).setOnClickListener(this);

        view.findViewById(R.id.ll_live_a_and_a).setOnClickListener(this);
        view.findViewById(R.id.ll_scenic_xiu).setOnClickListener(this);
//        view.findViewById(R.id.ll_scretary).setOnClickListener(this);
        view.findViewById(R.id.ll_exhibitor).setOnClickListener(this);
        view.findViewById(R.id.ll_speaker_search).setOnClickListener(this);
        view.findViewById(R.id.ll_meeting_guide).setOnClickListener(this);
        view.findViewById(R.id.ll_cit_college).setOnClickListener(this);
        view.findViewById(R.id.ll_me).setOnClickListener(this);
        view.findViewById(R.id.ll_message_station).setOnClickListener(this);
        view.findViewById(R.id.iv_top_cit).setOnClickListener(this);
        view.findViewById(R.id.ll_hands_on).setOnClickListener(this);
        view.findViewById(R.id.rl_bb).setOnClickListener(this);
    }


    private void initViews(View view) {
        mTvScenicXiuNums = (TextView) view.findViewById(R.id.tv_scenic_xiu_num);
        mTvMsgNums = (TextView) view.findViewById(R.id.tv_msg_nums);
    }

    class TaskAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mValidActivitys.clear();
            mAllActivitys.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if(AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                LoginActivity.startLoginActivity(getActivity(),LoginActivity.TYPE_NORMAL,"","","","");
                return;
            }else if(AppApplication.userType != Constants.TYPE_USER_FACUTY) {
                ToastUtils.showShorToast("您不是主席团成员，无法使用该模块");
                return;
            }

            SecretaryActivity.startSecretaryActivity(getActivity(), mTaskNum, mQuestionNum, mValidActivitys, mScenceShowBeans);
        }

        @Override
        protected Void doInBackground(Void... params) {
            DbAdapter db = DbAdapter.getInstance();
            db.open();
            mAllActivitys = db.getSessionAndMeetingBySpeakerId(AppApplication.userId);
            db.close();

            // 处理过期时间下的活动
            int size = mAllActivitys.size();

            Date date = new Date();
            String current_day = DateUtil.getNowDate(DateUtil.DEFAULT);
            String currentTime = current_day +" 00:00:00";
            Date currentDate = DateUtil.getDate(currentTime, DateUtil.DEFAULT_SECOND);
            long currentSecond = currentDate.getTime();

            for (int i = 0; i < size; i++) {
                String time = mAllActivitys.get(i).getTime() + ":00";
                date = DateUtil.getDate(time, DateUtil.DEFAULT_SECOND);
                long second = date.getTime();
                if (second > currentSecond) {
                    mValidActivitys.add(mAllActivitys.get(i));
                }
            }

            //按照时间排序。
            ArrayUtils.quickSort(mValidActivitys);
            mTaskNum = mValidActivitys.size();
            return null;
        }
    }
}
