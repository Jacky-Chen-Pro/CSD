package com.android.incongress.cd.conference;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.CountryCodeAdapter;
import com.android.incongress.cd.conference.beans.CountryCodeBean;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.utils.BladeView;
import com.android.incongress.cd.conference.utils.MySectionIndexer;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CountryCodeActivity extends FragmentActivity {

    private StickyListHeadersListView mStickCountrys;
    private CountryCodeAdapter mAdapter;
    private BladeView mBladeView;
    private List<CountryCodeBean> mCountrys;
    private static final String ALL_CHARACTER = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
    private String[] sections = {"", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    private MySectionIndexer mIndexer;
    private int[] counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_code);

        mStickCountrys = (StickyListHeadersListView) findViewById(R.id.slv_countrys);
        mBladeView = (BladeView) findViewById(R.id.bv_firstcode);

        counts = new int[sections.length];
        ((TextView)findViewById(R.id.title_text)).setText(getString(R.string.country_code));
        ((ImageView)findViewById(R.id.title_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        getCountryDatas();

        mStickCountrys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("code", mCountrys.get(position).getCode());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    //获取国家码信息
    private void getCountryDatas() {
        mCountrys = ConferenceGetData.getAllCountryCode(this);

        mAdapter = new CountryCodeAdapter(this, mCountrys);
        mStickCountrys.setAdapter(mAdapter);

        mBladeView.setOnItemClickListener(new BladeView.OnItemClickListener() {
            @Override
            public void onItemClick(String s) {
                if (s != null) {
                    int section = ALL_CHARACTER.indexOf(s);
                    int position = mIndexer.getPositionForSection(section);

                    if (position != -1) {
                        mStickCountrys.setSelection(position);
                    }
                }
            }
        });
        //设置Section
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (int i = 0; i < sections.length; i++) {
            map.put(sections[i], 0);
        }

        for (int i = 0; i < mCountrys.size(); i++) {
            CountryCodeBean bean = mCountrys.get(i);
            int count = map.get(String.valueOf(bean.getCountry().charAt(0)));
            map.put(String.valueOf(bean.getCountry().charAt(0)), ++count);
        }

        for (int i = 0; i < sections.length; i++) {
            counts[i] = map.get(sections[i]);
        }
        if (counts[0] == 0) {
            String[] msectionslist = new String[sections.length - 1];
            int[] newcounts = new int[sections.length - 1];
            for (int i = 1; i < sections.length; i++) {
                String section = sections[i];
                msectionslist[i - 1] = section;
                int count = counts[i];
                newcounts[i - 1] = count;
            }
            sections = msectionslist;
            counts = newcounts;
        }
        mIndexer = new MySectionIndexer(sections, counts);
    }
}
