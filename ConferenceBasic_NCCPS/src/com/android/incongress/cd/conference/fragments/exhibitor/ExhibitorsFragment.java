package com.android.incongress.cd.conference.fragments.exhibitor;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.adapters.ExhibitorListAdapter;
import com.android.incongress.cd.conference.adapters.ExhibitorListAdapter.StartCheckListener;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ExhibitorBean;
import com.android.incongress.cd.conference.data.DbAdapter;

/**
 * 参展商列表
 *
 * @author Jacky_Chen
 * @time 2014年11月25日
 */
public class ExhibitorsFragment extends BaseFragment {
    private ListView mListView;
    private ExhibitorListAdapter mAdapter;
    private TextView mNoDataView;
    private SharedPreferences preferences;

    public ExhibitorsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Editor editor = preferences.edit();
        editor.putBoolean(Constants.PREFERENCE_MODULE_CZS_VISIBLE_NEW, false);
        editor.commit();
        AppApplication.moduleBean.setVisibleczs(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View view = inflater.inflate(R.layout.exhibitor_list_view, null);

        mListView = (ListView) view.findViewById(R.id.exhibitor_list);
        mNoDataView = (TextView) view.findViewById(R.id.exhibitor_no_data);
        mAdapter = new ExhibitorListAdapter(this.getActivity());
        mListView.setAdapter(mAdapter);
        int count = mAdapter.getCount();
        if (count == 0) {
            mNoDataView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mNoDataView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                ExhibitorBean mBean = (ExhibitorBean) mAdapter.getItem(position);
                ExhibitorDetailFragment fragment = new ExhibitorDetailFragment();
                fragment.setExhibitor(mBean);
                action(fragment, R.string.exhibitor_detail_title, false, false);
            }

        });

        mAdapter.setStartCheckListener(new StartCheckListener() {
            @Override
            public void onCheck(final ExhibitorBean been, final boolean check, final CheckBox view) {
                if (check) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppApplication.getContext());
                    builder.setTitle("提示信息").setMessage(R.string.exhibitor_list_item_guanzhuchengong).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            been.setChecked(check);
                            been.setAttention(Constants.ATTENTION);
                            DbAdapter ada = DbAdapter.getInstance();
                            ada.open();
                            ConferenceSetData.updateExhibitorAttention(ada, been.getExhibitorsId(), Constants.ATTENTION);
                            ada.close();
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            been.setChecked(!check);
                            view.setChecked(!check);
                        }
                    }).setCancelable(false).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppApplication.getContext());
                    builder.setTitle("提示信息").setMessage(R.string.exhibitor_list_item_guanzhuquxiao).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            been.setChecked(check);
                            been.setAttention(Constants.NOATTENTION);
                            DbAdapter ada = DbAdapter.getInstance();
                            ada.open();
                            ConferenceSetData.updateExhibitorAttention(ada, been.getExhibitorsId(), Constants.NOATTENTION);
                            ada.close();
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            been.setChecked(!check);
                            view.setChecked(!check);
                        }
                    }).setCancelable(false).show();
                }
            }
        });
        return view;
    }
}
