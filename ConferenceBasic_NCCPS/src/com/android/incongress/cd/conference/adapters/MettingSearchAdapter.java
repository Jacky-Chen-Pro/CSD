package com.android.incongress.cd.conference.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.utils.CommonUtils;

public class MettingSearchAdapter extends BaseAdapter {

    private List<MeetingBean> datasource = new ArrayList<MeetingBean>();

    public class MeetingViewValue {
        String title;
        String time;
        String id;
    }

    public MettingSearchAdapter() {
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_MEETING;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        datasource.addAll(ConferenceGetData.getMeetingList(ada, sql));
        ada.close();
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(),  R.layout.search_child);
            holder = new Holder();
            TextView titleView = (TextView) convertView.findViewById(R.id.search_child_title);
            TextView timeView = (TextView) convertView.findViewById(R.id.search_child_time);
            holder.titleView = titleView;
            holder.timeView = timeView;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MeetingBean bean = datasource.get(position);

        Date date = DateUtil.getDate(bean.getMeetingDay(), DateUtil.DEFAULT);
        if(AppApplication.systemLanguage == 1) {
            holder.titleView.setText(bean.getTopic());
            holder.timeView.setText(DateUtil.getDateString(date, DateUtil.DEFAULT_CHINA_TWO)  + " " + bean.getStartTime()+"-"+bean.getEndTime());
        }else {
            holder.titleView.setText(bean.getTopic_En());
            holder.timeView.setText(DateUtil.getDateShort(date)  + " " + bean.getStartTime()+"-"+bean.getEndTime());
        }
        return convertView;
    }

    private class Holder {
        TextView titleView;
        TextView timeView;
    }

    public void search(String meetingname) {
        String sql = "select * from "
                + ConferenceTables.TABLE_INCONGRESS_MEETING + " where " + ConferenceTableField.MEETING_TOPIC
                + " like '%" + meetingname + "%'";
        ;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        datasource = ConferenceGetData.getMeetingList(ada, sql);
        ada.close();
        notifyDataSetChanged();
    }

    /**
     * 判断是否显示无数据提示
     *
     * @return
     */
    public boolean isNoDataShow() {
        if (datasource == null)
            return true;
        if (datasource.size() == 0) {
            return true;
        }
        return false;
    }
}
