package com.android.incongress.cd.conference.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.MapBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.uis.IncongressTextView;
import com.android.incongress.cd.conference.utils.CommonUtils;

public class TipsClassesAdapter extends BaseAdapter {

    private List<MapBean> datasource = new ArrayList<MapBean>();

    public TipsClassesAdapter() {
        String sql = "select *  from " + ConferenceTables.TABLE_INCONGRESS_MAP;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        datasource.addAll(ConferenceGetData.getMaps(ada, sql));
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
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.tips_classes_list_item);
            holder = new Holder();
            IncongressTextView titleView = (IncongressTextView) convertView.findViewById(R.id.tip_classes_name);
            holder.titleView = titleView;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MapBean bean=datasource.get(position);
        holder.titleView.setText(bean.getMapRemark());
        return convertView;
    }

    private class Holder {
    	IncongressTextView titleView;
    }
}
