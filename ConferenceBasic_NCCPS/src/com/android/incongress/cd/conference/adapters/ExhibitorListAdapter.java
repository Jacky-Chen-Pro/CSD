package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ExhibitorBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExhibitorListAdapter extends BaseAdapter {

    private List<ExhibitorBean> mExhibitors = new ArrayList<ExhibitorBean>();
    private Context mContext;
    private boolean IsNetWorkOpen = true;// 判断网络是否打开

    public ExhibitorListAdapter(Context mContext) {
        String sql = "select *  from " + ConferenceTables.TABLE_INCONGRESS_EXHIBITOR;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        mExhibitors.addAll(ConferenceGetData.getExhibitorsList(ada, sql));
        ada.close();
        this.mContext = mContext;
        IsNetWorkOpen = AppApplication.instance().NetWorkIsOpen();
    }

    @Override
    public int getCount() {
        return mExhibitors.size();
    }

    @Override
    public Object getItem(int position) {
        return mExhibitors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.exhibitor_item_view);
            holder = new Holder();
            ImageView logoView = (ImageView) convertView.findViewById(R.id.exhibitor_logo);
            TextView nameView = (TextView) convertView.findViewById(R.id.exhibitor_name);
            TextView locationView = (TextView) convertView.findViewById(R.id.exhibitor_location);
            TextView infoView = (TextView) convertView.findViewById(R.id.exhibitor_info);

            holder.logoView = logoView;
            holder.nameView = nameView;
            holder.locationView = locationView;
            holder.infoView = infoView;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ExhibitorBean bean = mExhibitors.get(position);
        final Holder holder2 = holder;
        if (bean.getLogo() == null || bean.getLogo().equals("")) {
            holder.logoView.setVisibility(View.GONE);
        } else {
            if (IsNetWorkOpen && bean.getStorelogo() == 0) {
                holder.logoView.setImageDrawable(null);
                holder.logoView.setBackgroundColor(0xFFEFEEF3);
                AsyncHttpClient client = AppApplication.getHttpClient();
                String values[] = bean.getLogo().split("/");
                int size = values.length;
                String logoname = values[size - 1];
                final String filepath = AppApplication.instance().getSDPath() + Constants.FILESDIR + logoname;
                final File file = new File(filepath);
                client.get(mContext, Constants.get_IMAGEPREFIX() + bean.getLogo(),
                        new FileAsyncHttpResponseHandler(file) {
                            private void debugFile(File file) {
                                try {
                                    if (file.exists()) {
                                        ImageLoader.getInstance().displayImage(Uri.fromFile(new File(filepath)).toString(), holder2.logoView);
                                        if (bean.getStorelogo() != 1) {
                                            bean.setStorelogo(1);
                                            DbAdapter ada = DbAdapter
                                                    .getInstance();
                                            ada.open();
                                            ConferenceSetData.updateExhibitor(ada, bean);
                                            ada.close();
                                        }
                                    }
                                } catch (Throwable t) {
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                                  File file) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  Header[] headers, File file) {
                                debugFile(file);

                            }
                        });
            } else {
                String values[] = bean.getLogo().split("/");
                int size = values.length;
                String logoname = values[size - 1];
                String filepath = AppApplication.instance().getSDPath()
                        + Constants.FILESDIR + logoname;
                ImageLoader.getInstance().displayImage(Uri.fromFile(new File(filepath)).toString(), holder.logoView);
            }
        }
        if (AppApplication.systemLanguage == 1) {
            holder.nameView.setText(bean.getTitle());
        } else if (AppApplication.systemLanguage == 2) {
            if (bean.getTitle_En() != null && !"".equals(bean.getTitle_En())) {
                holder.nameView.setText(bean.getTitle_En());
            } else {
                holder.nameView.setText(bean.getTitle());
            }
        }

        holder.locationView.setText(AppApplication
                .getContext()
                .getResources()
                .getString(R.string.exhibitor_detail_zanwei,
                        bean.getLocation()));

        if (bean.getInfo() == null || bean.getInfo().equals("")) {
            holder.infoView.setVisibility(View.GONE);
        } else {
            if (AppApplication.systemLanguage == 1) {
                holder.infoView.setText(bean.getInfo());
            } else if (AppApplication.systemLanguage == 2) {
                if (bean.getInfo_En() != null && !"".equals(bean.getInfo_En())) {
                    holder.infoView.setText(bean.getInfo_En());
                } else {
                    holder.infoView.setText(bean.getInfo());
                }
            }
            holder.infoView.setVisibility(View.VISIBLE);
        }
        if (bean.getAttention() == Constants.NOATTENTION) {
            bean.setChecked(false);
        }
        if (bean.getAttention() == Constants.ATTENTION) {
            bean.setChecked(true);
        }
        return convertView;
    }

    private class Holder {
        ImageView logoView;
        TextView nameView;
        TextView locationView;
        TextView infoView;
    }

    public interface StartCheckListener {
        public void onCheck(ExhibitorBean been, boolean check, CheckBox view);
    }

    private StartCheckListener mListener;

    public void setStartCheckListener(StartCheckListener listener) {
        mListener = listener;
    }
}
