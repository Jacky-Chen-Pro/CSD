package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.CountryCodeBean;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Jacky on 2016/2/28.
 */
public class CountryCodeAdapter extends BaseAdapter  implements StickyListHeadersAdapter, SectionIndexer {
    private List<CountryCodeBean> mCountrys;
    private LayoutInflater mInflater;

    //1）sectionIndices数组用来存放每一轮分组的第一个item的位置。
    //2）sectionHeaders数组用来存放每一个分组要展现的数据，
    private int[] mSectionIndices;
    private String[] mSectionHeaders;

    public CountryCodeAdapter(Context ctx, List<CountryCodeBean> countrys) {
        this.mCountrys = countrys;
        this.mInflater = LayoutInflater.from(ctx);

        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
    }



    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if(convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.item_country_head, parent, false);
            holder.tvCountryName = (TextView) convertView.findViewById(R.id.tv_country_code);
            convertView.setTag(holder);
        }else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String headerText = String.valueOf(mCountrys.get(position).getCountry().charAt(0));
        holder.tvCountryName.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return Long.valueOf(mCountrys.get(position).getCountry().charAt(0));
    }

    @Override
    public int getCount() {
        return mCountrys.size();
    }

    @Override
    public Object getItem(int position) {
        return mCountrys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_country_code, parent, false);
            holder.tvCountryName = (TextView) convertView.findViewById(R.id.tv_country_name);
            holder.tvCountryCode = (TextView) convertView.findViewById(R.id.tv_country_code);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvCountryName.setText(mCountrys.get(position).getCountry());
        holder.tvCountryCode.setText("+" + mCountrys.get(position).getCode());

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }



    /**
     * sectionIndices数组用来存放每一轮分组的第一个item的位置。
     * @return
     */
    private int[] getSectionIndices() {
        List<Integer> secionIndices = new ArrayList<Integer>();
        String firstLetter = String.valueOf(mCountrys.get(0).getCountry().charAt(0));
        secionIndices.add(0);

        for(int i=1; i<mCountrys.size(); i++) {
            String tempFirstLetter = String.valueOf(mCountrys.get(i).getCountry().charAt(0));
            if(!firstLetter.equals(tempFirstLetter)) {
                firstLetter = tempFirstLetter;
                secionIndices.add(i);
            }
        }

        int[] secions = new int[secionIndices.size()];

        for(int i=0; i<secionIndices.size(); i++) {
            secions[i] = secionIndices.get(i);
        }
        return secions;
    }

    /**
     * header中存放的数据
     * @return
     */
    private String[] getSectionHeaders(){
        String[] sectionHeader = new String[mSectionIndices.length];
        for(int i=0; i< mSectionIndices.length; i++) {
            sectionHeader[i] = String.valueOf(mCountrys.get(mSectionIndices[i]).getCountry().charAt(0));
        }
        return sectionHeader;
    }

    class HeaderViewHolder {
        TextView tvCountryName;
    }

    class ViewHolder {
        TextView tvCountryName;
        TextView tvCountryCode;
    }
}
