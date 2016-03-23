package com.android.incongress.cd.conference.adapters;

import java.util.ArrayList;
import java.util.List;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.utils.CommonUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DZBBAdapter extends BaseAdapter {
    private List<DZBBBean> mBeans = new ArrayList<DZBBBean>();
    private Context mContext;

    public DZBBAdapter(Context ctx, List<DZBBBean> beans) {

        this.mContext = ctx;
        this.mBeans = beans;
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.item_dzbb);
            holder = new ViewHolder();
            holder.posterCode = (TextView) convertView.findViewById(R.id.posterCode);
            holder.conField = (TextView) convertView.findViewById(R.id.conField);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.author = (TextView) convertView.findViewById(R.id.author);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DZBBBean bean = mBeans.get(position);

        holder.posterCode.setText(bean.getPosterCode());
        holder.conField.setText(bean.getConField());
        holder.title.setText(bean.getTitle());
        holder.author.setText(mContext.getString(R.string.first_author) + bean.getAuthor());

        return convertView;
    }

    class ViewHolder {
        TextView posterCode;
        TextView conField;
        TextView title;
        TextView author;
    }

}
