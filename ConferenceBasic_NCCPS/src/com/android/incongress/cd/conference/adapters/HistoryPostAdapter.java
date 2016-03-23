package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.ScenicXiuPicsViewpagerActivity;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommentArrayBean;
import com.android.incongress.cd.conference.beans.SceneShowTopBean;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.fragments.scenic_xiu.ScenicXiuFragment;
import com.android.incongress.cd.conference.uis.CircleImageView;
import com.android.incongress.cd.conference.uis.ListViewForScrollView;
import com.android.incongress.cd.conference.uis.NoScrollGridView;
import com.android.incongress.cd.conference.utils.CommentUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据type类型的不同，设置不同的view
 * 1新闻 2通知 3展商活动 4发帖 5企业活动 6提问
 */
public class HistoryPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        FlexibleDividerDecoration.PaintProvider,
        FlexibleDividerDecoration.VisibilityProvider,
        HorizontalDividerItemDecoration.MarginProvider {
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
        paint.setColor(mContext.getResources().getColor(R.color.recyler_gray));
        paint.setStrokeWidth(32);
        return paint;
    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
            return false;
    }

    private List<ScenicXiuBean> mDownBeans;
    private Context mContext;

    private static final int TYPE_NEW = 1;
    private static final int TYPE_NOTIFACATION = 2;
    private static final int TYPE_MAKE_POST = 4;
    private static final int TYPE_COMPANY_ACTIVITY = 5;
    private static final int TYPE_QUESTION = 6;
    private static final int TYPE_LOAD_MORE = 7;

    private List<View> mOperationViewList = new ArrayList<View>();

    private boolean mIsNoMoreData = false;

    public HistoryPostAdapter(List<ScenicXiuBean> beans,Context context) {
        this.mDownBeans = beans;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if (viewType == TYPE_NEW) {
            //新闻 --> 没有评论和点赞
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_news, parent, false);
            ViewHolder1News holder = new ViewHolder1News(view);
            return holder;
        } else if (viewType == TYPE_NOTIFACATION) {
            //通知 --> 没有评论和点赞
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_notifacation, parent, false);
            ViewHolder2Notifacation holder = new ViewHolder2Notifacation(view);
            return holder;
        } else if (viewType == TYPE_MAKE_POST) {
            //发帖
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_make_post, parent, false);
            ViewHolder4MakePost holder = new ViewHolder4MakePost(view);
            return holder;
        } else if (viewType == TYPE_COMPANY_ACTIVITY) {
            //企业活动
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_company_activity, parent, false);
            ViewHolder5CompanyActivitys holder = new ViewHolder5CompanyActivitys(view);
            return holder;
        } else if (viewType == TYPE_QUESTION) {
            //提问
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_xiu_question, parent, false);
            ViewHolder6Question holder = new ViewHolder6Question(view);
            return holder;
        } else if (viewType == TYPE_LOAD_MORE) {
            //更多
            View view = LayoutInflater.from(mContext).inflate(R.layout.load_more_view, parent, false);
            ViewHolder7LoadMore holder = new ViewHolder7LoadMore(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);

        //新闻类型
        if (viewType == TYPE_NEW) {
            final ScenicXiuBean bean = mDownBeans.get(position);
            ((ViewHolder1News) holder).tvPublisherName.setText(bean.getAuthor());
            ((ViewHolder1News) holder).tvPublishTime.setText(bean.getTimeShow());

            if (!bean.getImgUrls().equals(((ViewHolder1News) holder).ivShow.getTag())) {
                ((ViewHolder1News) holder).ivShow.setTag(bean.getImgUrls());
                ImageLoader.getInstance().displayImage(bean.getImgUrls(), (((ViewHolder1News) holder).ivShow));
            }

            ((ViewHolder1News) holder).tvContent.setText(bean.getTitle());

        }
        //通知类型
        else if (viewType == TYPE_NOTIFACATION) {
            final ScenicXiuBean bean = mDownBeans.get(position);
            ((ViewHolder2Notifacation) holder).tvPublisherName.setText(bean.getAuthor());
            ((ViewHolder2Notifacation) holder).tvPublishTime.setText(bean.getTimeShow());
            if (!bean.getImgUrls().equals(((ViewHolder2Notifacation) holder).ivShow.getTag())) {
                ((ViewHolder2Notifacation) holder).ivShow.setTag(bean.getImgUrls());
                ImageLoader.getInstance().displayImage(bean.getImgUrls(), (((ViewHolder2Notifacation) holder).ivShow));
            }
            ((ViewHolder2Notifacation) holder).tvContent.setText(bean.getTitle());
        }
        //发帖类型
        else if (viewType == TYPE_MAKE_POST) {
            final ScenicXiuBean bean = mDownBeans.get(position);

            ((ViewHolder4MakePost) holder).tvPublisherName.setText(bean.getAuthor());
            ((ViewHolder4MakePost) holder).tvPublishTime.setText(bean.getTimeShow());

            ((ViewHolder4MakePost) holder).tvCommentNum.setText(mContext.getString(R.string.xxx_comments, bean.getCommentCount()));
            ((ViewHolder4MakePost) holder).tvPraiseNum.setText(mContext.getString(R.string.xxx_praise, bean.getLaudCount()));

            if(bean.getLaudCount() == 0) {
                ((ViewHolder4MakePost) holder).tvPraiseNum.setVisibility(View.GONE);
            }
            if(bean.getCommentCount() == 0) {
                ((ViewHolder4MakePost) holder).tvCommentNum.setVisibility(View.GONE);
            }

            if (StringUtils.isEmpty(bean.getAuthorImg())) {
                ((ViewHolder4MakePost) holder).civPublisherIcon.setImageResource(R.drawable.professor_default);
            } else {
                ImageLoader.getInstance().displayImage(bean.getAuthorImg(), ((ViewHolder4MakePost) holder).civPublisherIcon);
            }

            String content = "";
            try {
                content = URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8);
                content = URLDecoder.decode(content, Constants.ENCODING_UTF8);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((ViewHolder4MakePost) holder).tvContent.setText(content);

            ((ViewHolder4MakePost) holder).gridViewPics.setVisibility(View.GONE);
            //不为空，说明有图片，至少一张
            if (!StringUtils.isEmpty(bean.getImgUrls())) {
                ((ViewHolder4MakePost) holder).gridViewPics.setVisibility(View.VISIBLE);
                final String[] strPics = bean.getImgUrls().split(",");
                ((ViewHolder4MakePost) holder).gridViewPics.setAdapter(new ScenicXiuGridAdapter(strPics, mContext));
                ((ViewHolder4MakePost) holder).gridViewPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ScenicXiuPicsViewpagerActivity.startViewPagerActivity(mContext, strPics, position);
                    }
                });
            }

            ((ViewHolder4MakePost) holder).ivMoreOperationClick.setVisibility(View.GONE);

            //评论列表 适配数据
            ((ViewHolder4MakePost) holder).lvComments.setDividerHeight(0);
            ((ViewHolder4MakePost) holder).lvComments.setAdapter(new ScenicXiuCommentAdapter(mContext, bean.getCommentArray()));

        } else if (viewType == TYPE_COMPANY_ACTIVITY) {
            final ScenicXiuBean bean = mDownBeans.get(position);

            ((ViewHolder5CompanyActivitys) holder).tvPublisherName.setText(bean.getAuthor());
            ((ViewHolder5CompanyActivitys) holder).tvPublishTime.setText(bean.getTimeShow());

            ((ViewHolder5CompanyActivitys) holder).tvCommentNum.setText( mContext.getString(R.string.xxx_comments, bean.getCommentCount()));
            ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setText(mContext.getString(R.string.xxx_praise, bean.getLaudCount()));

            ImageLoader.getInstance().displayImage(bean.getAuthorImg(), ((ViewHolder5CompanyActivitys) holder).civPublisherIcon);

            if(bean.getLaudCount() == 0) {
                ((ViewHolder5CompanyActivitys) holder).tvPraiseNum.setVisibility(View.GONE);
            }
            if(bean.getCommentCount() == 0) {
                ((ViewHolder5CompanyActivitys) holder).tvCommentNum.setVisibility(View.GONE);
            }

            String content = "";
            try {
                content = URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8);
                content = URLDecoder.decode(content, Constants.ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ((ViewHolder5CompanyActivitys) holder).tvContent.setText(content);

            //不为空，说明有图片，至少一张
            ((ViewHolder5CompanyActivitys) holder).gridViewPics.setVisibility(View.GONE);
            if (!StringUtils.isEmpty(bean.getImgUrls())) {
                ((ViewHolder5CompanyActivitys) holder).gridViewPics.setVisibility(View.VISIBLE);
                final String[] strPics = bean.getImgUrls().split(",");
                ((ViewHolder5CompanyActivitys) holder).gridViewPics.setAdapter(new ScenicXiuGridAdapter(strPics, mContext));

                ((ViewHolder5CompanyActivitys) holder).gridViewPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ScenicXiuPicsViewpagerActivity.startViewPagerActivity(mContext, strPics, position);
                    }
                });
            }
            ((ViewHolder5CompanyActivitys) holder).ivMoreOperationClick.setVisibility(View.GONE);

            //评论列表 适配数据
            ((ViewHolder5CompanyActivitys) holder).lvComments.setDividerHeight(0);
            ((ViewHolder5CompanyActivitys) holder).lvComments.setAdapter(new ScenicXiuCommentAdapter(mContext, mDownBeans.get(position).getCommentArray()));
        } else if (viewType == TYPE_QUESTION) {
            final ScenicXiuBean bean = mDownBeans.get(position);
            try {
                String question = URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8);
                question = URLDecoder.decode(question, Constants.ENCODING_UTF8);
                ((ViewHolder6Question) holder).tvQuestion.setText(question + "@" + bean.getAnswerUserName());
                CommentUtils.addAnswerLinks(mContext.getResources().getColor(R.color.theme_color), "@" + bean.getAnswerUserName(), ((ViewHolder6Question) holder).tvQuestion);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((ViewHolder6Question) holder).tvTime.setText(bean.getTimeShow());

            try {
                String answer = URLDecoder.decode(bean.getAnswerContent(), Constants.ENCODING_UTF8);
                answer = URLDecoder.decode(answer, Constants.ENCODING_UTF8);
                ((ViewHolder6Question) holder).tvAnswer.setText(bean.getAnswerUserName() + ":" + answer);
                CommentUtils.addLinks(mContext.getResources().getColor(R.color.theme_color), bean.getAnswerUserName(), ((ViewHolder6Question) holder).tvAnswer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bean.getLaudCount() == 0) {
                ((ViewHolder6Question) holder).tvPraiseNum.setVisibility(View.INVISIBLE);
            } else {
                ((ViewHolder6Question) holder).tvPraiseNum.setText(mContext.getString(R.string.xxx_praise,bean.getLaudCount()));
            }

            ImageLoader.getInstance().displayImage(bean.getAuthorImg(), ((ViewHolder6Question) holder).civAuthor);
            ((ViewHolder6Question) holder).tvAuthor.setText(bean.getAuthor());

            ((ViewHolder6Question) holder).ivMoreOperationClick.setVisibility(View.GONE);
        } else if (viewType == TYPE_LOAD_MORE) {
            ((ViewHolder7LoadMore) holder).pbLoad.setVisibility(View.GONE);
            ((ViewHolder7LoadMore) holder).tvLoad.setText("");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDownBeans.size()) {
            return TYPE_LOAD_MORE;
        } else {
            return mDownBeans.get(position).getType();
        }
    }

    @Override
    public int getItemCount() {
        return mDownBeans.size() + 1;
    }

    class ViewHolder1News extends RecyclerView.ViewHolder {
        RelativeLayout rlNews;
        TextView tvPublisherName;
        TextView tvPublishTime;
        TextView tvContent;
        ImageView ivShow;

        public ViewHolder1News(View view) {
            super(view);
            rlNews = (RelativeLayout) view.findViewById(R.id.rl_news);
            ivShow = (ImageView) view.findViewById(R.id.iv_show);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
        }
    }

    class ViewHolder2Notifacation extends RecyclerView.ViewHolder {
        RelativeLayout rlNotify;
        TextView tvPublisherName;
        TextView tvPublishTime;
        TextView tvContent;
        ImageView ivShow;

        public ViewHolder2Notifacation(View view) {
            super(view);
            rlNotify = (RelativeLayout) view.findViewById(R.id.rl_notify);
            ivShow = (ImageView) view.findViewById(R.id.iv_show);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
        }
    }

    class ViewHolder4MakePost extends RecyclerView.ViewHolder {
        NoScrollGridView gridViewPics;
        TextView tvPublisherName;
        CircleImageView civPublisherIcon;
        TextView tvPublishTime;
        TextView tvContent;
        TextView tvCommentNum;
        TextView tvPraiseNum;
        ImageView ivMoreOperationClick;
        LinearLayout llMoreOperationContainer;
        TextView tvComment;
        TextView tvPraise;
        ListViewForScrollView lvComments;

        public ViewHolder4MakePost(View view) {
            super(view);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            gridViewPics = (NoScrollGridView) view.findViewById(R.id.gv_pics);
            civPublisherIcon = (CircleImageView) view.findViewById(R.id.civ_publisher);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
            tvCommentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            tvPraiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
            ivMoreOperationClick = (ImageView) view.findViewById(R.id.iv_operate_more);
            llMoreOperationContainer = (LinearLayout) view.findViewById(R.id.ll_more_operate_container);
            lvComments = (ListViewForScrollView) view.findViewById(R.id.lv_commets);
            tvComment = (TextView) view.findViewById(R.id.tv_comment);
            tvPraise = (TextView) view.findViewById(R.id.tv_praise);
        }
    }

    class ViewHolder5CompanyActivitys extends RecyclerView.ViewHolder {
        NoScrollGridView gridViewPics;
        TextView tvPublisherName;
        CircleImageView civPublisherIcon;
        TextView tvPublishTime;
        TextView tvContent;
        TextView tvCommentNum;
        TextView tvPraiseNum;
        ImageView ivMoreOperationClick;
        LinearLayout llMoreOperationContainer;
        TextView tvComment;
        TextView tvPraise;
        ListViewForScrollView lvComments;

        public ViewHolder5CompanyActivitys(View view) {
            super(view);
            tvPublisherName = (TextView) view.findViewById(R.id.tv_publisher_name);
            gridViewPics = (NoScrollGridView) view.findViewById(R.id.gv_pics);
            civPublisherIcon = (CircleImageView) view.findViewById(R.id.civ_publisher);
            tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            tvContent = (TextView) view.findViewById(R.id.tv_publish_content);
            tvCommentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            tvPraiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
            ivMoreOperationClick = (ImageView) view.findViewById(R.id.iv_operate_more);
            llMoreOperationContainer = (LinearLayout) view.findViewById(R.id.ll_more_operate_container);
            lvComments = (ListViewForScrollView) view.findViewById(R.id.lv_commets);
            tvComment = (TextView) view.findViewById(R.id.tv_comment);
            tvPraise = (TextView) view.findViewById(R.id.tv_praise);
        }
    }

    class ViewHolder6Question extends RecyclerView.ViewHolder {
        CircleImageView civAuthor;
        TextView tvAuthor;
        TextView tvQuestion;
        TextView tvTime;
        TextView tvAnswer;
        TextView tvPraiseNum;
        TextView tvPraise;
        LinearLayout llMoreOperationContainer;
        ImageView ivMoreOperationClick;

        public ViewHolder6Question(View itemView) {
            super(itemView);
            civAuthor = (CircleImageView) itemView.findViewById(R.id.civ_author);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author_name);
            tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
            tvPraiseNum = (TextView) itemView.findViewById(R.id.tv_praise_num);
            llMoreOperationContainer = (LinearLayout) itemView.findViewById(R.id.ll_more_operate_container);
            ivMoreOperationClick = (ImageView) itemView.findViewById(R.id.iv_operate_more);
            tvPraise = (TextView) itemView.findViewById(R.id.tv_praise);
        }
    }

    class ViewHolder7LoadMore extends RecyclerView.ViewHolder {
        ProgressBar pbLoad;
        TextView tvLoad;

        public ViewHolder7LoadMore(View view) {
            super(view);
            pbLoad = (ProgressBar) view.findViewById(R.id.pb_load);
            tvLoad = (TextView) view.findViewById(R.id.tv_load);
        }
    }

    /**
     * 清楚所有的评论框
     */
    public void clearAllCommentArea() {
        if (mOperationViewList.size() > 0) {
            for (int i = 0; i < mOperationViewList.size(); i++) {
                mOperationViewList.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void tellNoMoreDate() {
        mIsNoMoreData = true;
        notifyItemChanged(mDownBeans.size());
    }

    /**
     * 新闻中心和展商活动的回调
     */
    public interface NewsAndActivitysListener {
        void doWhenNewsOrActivityClicked(int type, String url, String title);
    }


    /**
     * 评论 1,正常评论，2 对评论进行回复
     */
    private void doComment(int type, int position, int sceneShowId,String userId,String parentName, int commentId, View view) {
        Intent intent;
        //未登陆
        if (AppApplication.userType == 0) {
            intent = new Intent(ScenicXiuFragment.GO_TO_LOGIN_FIRST);
        } else {
            //已登录
            if (type == 1) {
                intent = new Intent(ScenicXiuFragment.COMMENT_CLICK_RECEIVED_ACTION_NORMAL);
            } else {
                intent = new Intent(ScenicXiuFragment.COMMENT_CLICK_RECEIVED_ACTION_COMMENT);
            }
            intent.putExtra(ScenicXiuFragment.BROAD_POSITION, position);
            intent.putExtra(ScenicXiuFragment.BROAD_SCENIC_XIU_ID, sceneShowId);
            intent.putExtra(ScenicXiuFragment.BROAD_COMMENT_ID, commentId);
            intent.putExtra(ScenicXiuFragment.BROAD_PARENT_NAME, parentName);
            intent.putExtra(ScenicXiuFragment.BROAD_PARENT_ID, userId);
        }

        mContext.sendBroadcast(intent);
        view.setVisibility(View.GONE);
    }

    /**
     * 点赞
     */
    private void doPraise(final View view, final int scenicShowId, final int userId, final int userType) {
        //点赞
        //未登录
        if (userType == 0) {
            Intent intent = new Intent(ScenicXiuFragment.GO_TO_LOGIN_FIRST);
            mContext.sendBroadcast(intent);
            return;
        }

        ScaleAnimation scale = (ScaleAnimation) AnimationUtils.loadAnimation(mContext, R.anim.scale);
        view.startAnimation(scale);

        CHYHttpClientUsage.getInstanse().doSceneShowLaud(scenicShowId + "", userId + "", userType + "", new JsonHttpResponseHandler("gbk") {

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
                try {
                    int state = response.getInt("state");

                    if (state == 1) {
                        ((TextView) view).setTextColor(mContext.getResources().getColor(R.color.theme_color));
                        Drawable praiseDrawable = mContext.getResources().getDrawable(R.drawable.praised);
                        praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
                        ((TextView) view).setCompoundDrawables(praiseDrawable, null, null, null);
                        ((TextView) view).setText(response.getInt("laudCount") + "人点赞");
                    } else {
                        String tips = response.getString("msg");
                        ToastUtils.showShorToast(tips);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast("服务器开小差了，请稍后重试");
            }
        });
    }

    private void doWhenMoreViewClick(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_from_left_right);
            view.startAnimation(animation);
            view.setVisibility(View.INVISIBLE);
            mOperationViewList.remove(view);
        } else {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_from_right_left);
            view.startAnimation(animation);
            view.setVisibility(View.VISIBLE);
            mOperationViewList.add(view);
        }
    }
}