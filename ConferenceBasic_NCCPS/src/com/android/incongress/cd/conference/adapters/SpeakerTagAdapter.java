package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by Jacky on 2016/1/7.
 */
public class SpeakerTagAdapter extends TagAdapter<SpeakerBean> {
    private List<SpeakerBean> mBeans;
    private Context mContext;

    public SpeakerTagAdapter(Context ctx, List<SpeakerBean> datas) {
        super(datas);
        mBeans = datas;
        this.mContext = ctx;
    }

    @Override
    public void setSelectedList(int... pos) {
        super.setSelectedList(pos);
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public SpeakerBean getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public View getView(FlowLayout parent, int position, SpeakerBean speakerBean) {
        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.speaker_layout, parent, false);
        if(AppApplication.systemLanguage == 1) {
            tv.setText(speakerBean.getSpeakerName());
        }else {
            tv.setText(speakerBean.getEnName());
        }
        return tv;
    }

    /**
     * 获取讲者列表
     * @return
     */
    public List<SpeakerBean> getSpeakerList() {
        return mBeans;
    }
}
