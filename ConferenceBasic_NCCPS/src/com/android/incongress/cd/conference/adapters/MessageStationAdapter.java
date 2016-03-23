package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.MessageBean;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by Jacky on 2016/1/29.
 */
public class MessageStationAdapter extends RecyclerView.Adapter<MessageStationAdapter.ViewHolder> implements View.OnClickListener,
        FlexibleDividerDecoration.PaintProvider {

    @Override
    public Paint dividerPaint(int position, RecyclerView parent) {
        Paint paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.divider_color));
        paint.setStrokeWidth(2);
        return paint;
    }

    private Context mContext;
    private List<MessageBean> mBeans;
    private LayoutInflater mInflater;

    private static final int TYPE_NO_URL = 1;
    private static final int TYPE_HAVE_URL = 2;

    public MessageStationAdapter(Context mContext, List<MessageBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == TYPE_HAVE_URL) {
            View view = mInflater.inflate(R.layout.item_messagestation_with_url, parent, false);
            view.setOnClickListener(this);
            holder = new ViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.item_messagestation_without_url, parent, false);
            view.setOnClickListener(this);
            holder = new ViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageBean bean = mBeans.get(position);
        holder.tvMessage.setText(bean.getContent());
        holder.tvTime.setText(bean.getCreateTime());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemViewType(int position) {
        MessageBean bean = mBeans.get(position);
        if (bean.getType() == TYPE_HAVE_URL) {
            return TYPE_HAVE_URL;
        } else {
            return TYPE_NO_URL;
        }
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (MessageBean)v.getTag());
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvTime;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.ll_item);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , MessageBean data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
