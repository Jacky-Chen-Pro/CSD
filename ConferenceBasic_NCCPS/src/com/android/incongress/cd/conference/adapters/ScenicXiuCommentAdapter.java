package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CommentArrayBean;
import com.android.incongress.cd.conference.utils.CommentUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Jacky on 2016/1/19.
 */
public class ScenicXiuCommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommentArrayBean> mComments;

    public ScenicXiuCommentAdapter() {
    }

    public ScenicXiuCommentAdapter(Context mContext, List<CommentArrayBean> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_scenic_show_comment,null);
            holder = new ViewHolder();
            holder.tvCommentWithName = (TextView) convertView.findViewById(R.id.tv_comment_users_and_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommentArrayBean comment = mComments.get(position);
        if(comment != null) {
            String content = "";
            try {
                if(comment.getParentId() != -1) {
                    //回复信息
                    content = comment.getUserName() + "回复" + comment.getParentName() + "：" + URLDecoder.decode(comment.getContent(), Constants.ENCODING_UTF8);
                    holder.tvCommentWithName.setText(content);
                    CommentUtils.addLinks(mContext.getResources().getColor(R.color.theme_color), comment.getUserName(), holder.tvCommentWithName);
//                    CommentUtils.addAnswerLinks(mContext.getResources().getColor(R.color.theme_color), comment.getParentName(), holder.tvCommentWithName);
                    CommentUtils.addCertainPositionLinks(mContext.getResources().getColor(R.color.theme_color) ,comment.getUserName(), comment.getParentName(), holder.tvCommentWithName);
                }else{
                    content = comment.getUserName() + "：" + URLDecoder.decode(comment.getContent(), Constants.ENCODING_UTF8);
                    holder.tvCommentWithName.setText(content);
                    CommentUtils.addLinks(mContext.getResources().getColor(R.color.theme_color), comment.getUserName(), holder.tvCommentWithName);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            holder.tvCommentWithName.setPadding(0, 6, 0, 6);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvCommentWithName;
    }
}
