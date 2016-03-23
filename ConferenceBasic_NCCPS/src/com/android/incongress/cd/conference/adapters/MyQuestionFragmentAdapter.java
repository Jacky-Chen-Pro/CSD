package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.uis.CircleImageView;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Jacky on 2016/1/22.
 */
public class MyQuestionFragmentAdapter extends RecyclerView.Adapter<MyQuestionFragmentAdapter.ViewHolder>  implements View.OnClickListener,
        FlexibleDividerDecoration.PaintProvider,
        FlexibleDividerDecoration.VisibilityProvider,
        HorizontalDividerItemDecoration.MarginProvider{

    private Context mContext;
    private List<SceneShowArrayBean> mBeans;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

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
        paint.setColor(mContext.getResources().getColor(R.color.alpha_gray));
        paint.setStrokeWidth(18);
        return paint;
    }

    @Override
    public boolean shouldHideDivider(int position, RecyclerView parent) {
        return false;
    }

    public MyQuestionFragmentAdapter(Context mContext, List<SceneShowArrayBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_i_got_question,null);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SceneShowArrayBean bean = mBeans.get(position);
        holder.itemView.setTag(bean);

        holder.tvTime.setText(bean.getTimeShow());
        holder.tvAuthorName.setText(bean.getAuthor());
        try {
            holder.tvQuestionContent.setText(URLDecoder.decode(bean.getContent(), Constants.ENCODING_UTF8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(bean.getIsHuiFu() == 1) {
            holder.tvAnswer.setBackgroundResource(R.drawable.bg_button_gray);
            holder.tvAnswer.setText("已回答");
        }else {
            holder.tvAnswer.setBackgroundResource(R.drawable.bg_button);
            holder.tvAnswer.setText("去回答");
        }
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvAuthorName;
        TextView tvQuestionContent;
        CircleImageView civAuthorIcon;
        TextView tvAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvAuthorName = (TextView) itemView.findViewById(R.id.tv_author_name);
            tvQuestionContent = (TextView) itemView.findViewById(R.id.tv_question_content);
            civAuthorIcon = (CircleImageView) itemView.findViewById(R.id.civ_author);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
        }
    }

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , SceneShowArrayBean question);
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(v,(SceneShowArrayBean)v.getTag());
    }
}
