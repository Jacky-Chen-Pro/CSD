package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.HdSessionBean;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/28.
 *
 * 提问：
 * http://incongress.cn/webapp/discussion/CIT2016H5/question.html?type=&userInfoId=&sId=&conId=&lan=
 *投票：
 *http://incongress.cn/webapp/discussion/CIT2016H5/toupiao.jsp?type=&userInfoId=&lan=
 *
 */
public class HdSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        FlexibleDividerDecoration.PaintProvider,
        FlexibleDividerDecoration.VisibilityProvider,
        HorizontalDividerItemDecoration.MarginProvider {

    private Context mContext;
    private List<HdSessionBean> mSessionNowBeans;
    private List<HdSessionBean> mSessionWillBeans;
    private List<HdSessionBean> mAllSessionBeans = new ArrayList<>();

    private static final int TYPE_NOW = 1;
    private static final int TYPE_WILL = 2;
    private static final int TYPE_DIVIDER = 3;
    private static final int TYPE_EMPTY = 4;

    public HdSessionAdapter(Context context, List<HdSessionBean> sessionNowBeans, List<HdSessionBean> sessionWillBeans) {
        this.mContext = context;

        this.mSessionNowBeans = sessionNowBeans;
        this.mSessionWillBeans = sessionWillBeans;

        mAllSessionBeans.addAll(mSessionNowBeans);
        mAllSessionBeans.add(new HdSessionBean());
        mAllSessionBeans.addAll(mSessionWillBeans);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_NOW) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_hdsession_now, parent, false);
            holder = new ViewHolder_Will_and_Now(view);
        } else if (viewType == TYPE_WILL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_hdsession_will, parent, false);
            holder = new ViewHolder_Will_and_Now(view);
        } else if (viewType == TYPE_DIVIDER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_hdsession_divider, parent, false);
            holder = new ViewHolderDivider(view);
        } else if(viewType == TYPE_EMPTY) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.empty_view, parent, false);
            holder = new ViewHolder_Empty(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_NOW || getItemViewType(position) == TYPE_WILL) {
            final HdSessionBean bean =  mAllSessionBeans.get(position);
            //设置相应的值
            try {
                ((ViewHolder_Will_and_Now) holder).tvHdSessionLocation.setText(bean.getClassesName());
            }catch (Exception e) {
                MyLogger.jLog().i("error:" + e.toString() + "position:" + position + "");
            }

            String[] topics = bean.getSessionName().split("#@#");

            ((ViewHolder_Will_and_Now) holder).tvHdSessionTimeAndTopic.setText(bean.getTimeShow() + "-" + topics[0]);

            if(bean.getIsHd() == 1) {
                ((ViewHolder_Will_and_Now) holder).ivHdSessionQuestion.setVisibility(View.VISIBLE);
            }else {
                ((ViewHolder_Will_and_Now) holder).ivHdSessionQuestion.setVisibility(View.GONE);
            }

            if(bean.getIsTp() == 1) {
                ((ViewHolder_Will_and_Now) holder).ivHdSessionServer.setVisibility(View.VISIBLE);
            }else {
                ((ViewHolder_Will_and_Now) holder).ivHdSessionServer.setVisibility(View.GONE);
            }

            ((ViewHolder_Will_and_Now) holder).ivHdSessionServer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position < mSessionNowBeans.size()) {
                        if (AppApplication.userType != Constants.TYPE_USER_REGISTER_BIND_CODE && AppApplication.userType != Constants.TYPE_USER_FACUTY) {
                            if(AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                                LoginActivity.startLoginActivity(mContext,LoginActivity.TYPE_PROFESSOR,"","","","");
                            }

                            if(AppApplication.userType == Constants.TYPE_USER_REGISTER_NOT_BIND_CODE) {
                                LoginActivity.startLoginActivity(mContext,LoginActivity.TYPE_PROFESSOR,
                                        AppApplication.getSPStringValue(Constants.USER_NAME),
                                        AppApplication.getSPStringValue(Constants.USER_MOBILE),"","");
                            }
                            return;
                        }
                        if(Constants.isDebug) {
                            WebViewContainerActivity.startWebViewContainerActivity(mContext, mContext.getString(R.string.hd_server_site_test,AppApplication.userType,AppApplication.userId, AppApplication.getSystemLanuageCode()), mContext.getResources().getString(R.string.survey));
                        }else {
                            WebViewContainerActivity.startWebViewContainerActivity(mContext, mContext.getString(R.string.hd_server_site_official,AppApplication.userType,AppApplication.userId, AppApplication.getSystemLanuageCode()), mContext.getResources().getString(R.string.survey));
                        }                    }
                }
            });

            ((ViewHolder_Will_and_Now) holder).ivHdSessionQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position < mSessionNowBeans.size()) {
                        if (AppApplication.userType != Constants.TYPE_USER_REGISTER_BIND_CODE && AppApplication.userType != Constants.TYPE_USER_FACUTY) {
                            if(AppApplication.userType == Constants.TYPE_USER_VISITOR) {
                                LoginActivity.startLoginActivity(mContext,LoginActivity.TYPE_PROFESSOR,"","", "" , "");
                            }

                            if(AppApplication.userType == Constants.TYPE_USER_REGISTER_NOT_BIND_CODE) {
                                String userName =  AppApplication.getSPStringValue(Constants.USER_NAME);
                                String mobile =  AppApplication.getSPStringValue(Constants.USER_MOBILE);
                                LoginActivity.startLoginActivity(mContext,LoginActivity.TYPE_PROFESSOR, userName,mobile, "" , "");
                            }
                            return;
                        }
                        if(Constants.isDebug) {
                            WebViewContainerActivity.startWebViewContainerActivity(mContext, mContext.getString(R.string.hd_question_site_test,AppApplication.userType,AppApplication.userId, bean.getSessionId(),AppApplication.conId,AppApplication.getSystemLanuageCode()), mContext.getResources().getString(R.string.question));
                        }else {
                            WebViewContainerActivity.startWebViewContainerActivity(mContext, mContext.getString(R.string.hd_question_site_official,AppApplication.userType,AppApplication.userId, bean.getSessionId(),AppApplication.conId,AppApplication.getSystemLanuageCode()), mContext.getResources().getString(R.string.question));
                        }
                    }
                }
            });

            if(position % 2 == 0) {
                ((ViewHolder_Will_and_Now) holder).ivVertical.setBackgroundColor( mContext.getResources().getColor(R.color.hd_session_color_first));
            }else {
                ((ViewHolder_Will_and_Now) holder).ivVertical.setBackgroundColor( mContext.getResources().getColor(R.color.hd_session_color_second));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mSessionNowBeans.size()) {
            return TYPE_NOW;
        } else if (position == mSessionNowBeans.size()) {
            return TYPE_DIVIDER;
        } else {
            return TYPE_WILL;
        }
    }

    @Override
    public int getItemCount() {
        return mAllSessionBeans.size();
    }

    class ViewHolder_Will_and_Now extends RecyclerView.ViewHolder {
        TextView tvHdSessionLocation, tvHdSessionTimeAndTopic;
        ImageView ivHdSessionQuestion, ivHdSessionServer;
        View ivVertical;

        public ViewHolder_Will_and_Now(View itemView) {
            super(itemView);
            tvHdSessionLocation = (TextView) itemView.findViewById(R.id.tv_hdsession_location);
            tvHdSessionTimeAndTopic = (TextView) itemView.findViewById(R.id.tv_hdsession_time_and_topic);
            ivHdSessionQuestion = (ImageView) itemView.findViewById(R.id.iv_hdsession_question);
            ivHdSessionServer = (ImageView) itemView.findViewById(R.id.iv_hdsession_server);
            ivVertical = itemView.findViewById(R.id.iv_vertical);
        }
    }

    class ViewHolder_Empty extends RecyclerView.ViewHolder {
        TextView tvEmptyMsg;

        public ViewHolder_Empty(View itemView) {
            super(itemView);
            tvEmptyMsg = (TextView) itemView.findViewById(R.id.tv_empty_msg);
        }
    }

    class ViewHolderDivider extends RecyclerView.ViewHolder {
        public ViewHolderDivider(View itemView) {
            super(itemView);
        }
    }


    @Override
    public int dividerLeftMargin(int position, RecyclerView parent) {
        return 0;
    }

    @Override
    public int dividerRightMargin(int position, RecyclerView parent) {
        return 0;
    }

    @Override
    public Paint dividerPaint(int position, RecyclerView parent) {
        Paint paint = new Paint();
        if(position <mSessionNowBeans.size()) {
            paint.setColor(mContext.getResources().getColor(R.color.hd_session_divider_first));
            paint.setStrokeWidth(2);
        }else if(position == mSessionNowBeans.size()-1 || position == mSessionNowBeans.size()) {
            paint.setStrokeWidth(0);
        }else {
            paint.setColor(mContext.getResources().getColor(R.color.hd_session_divider_second));
            paint.setStrokeWidth(28);
        }
        return paint;
    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
        if(position == mSessionNowBeans.size()-1 || position == mSessionNowBeans.size()) {
            return true;
        }
        return false;
    }
}
