package com.android.incongress.cd.conference.fragments.exhibitor;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleRoomLocationFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ExhibitorBean;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 参展商详情
 */
public class ExhibitorDetailFragment extends BaseFragment {
    private ExhibitorBean mBean;
    private ImageView logo;
    private TextView title,address,phone,fax,internet,location,info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exhibitor_detail_view, null);
        initViews(view);

        String values[] = mBean.getLogo().split("/");
        int size = values.length;
        String logoname = values[size - 1];
        String filepath = AppApplication.instance().getSDPath() + Constants.FILESDIR + logoname;

        File file = new File(filepath);
        if (!file.exists()) {
            logo.setVisibility(View.GONE);
        } else {
            ImageLoader.getInstance().displayImage(Uri.fromFile(new File(filepath)).toString(), logo);

        }
        if (AppApplication.systemLanguage == 1) {
            title.setText(mBean.getTitle());
        } else if (AppApplication.systemLanguage == 2) {
            if (mBean.getTitle_En() != null && !"".equals(mBean.getTitle_En())) {
                title.setText(mBean.getTitle_En());
            } else {
                title.setText(mBean.getTitle());
            }
        }
        if (AppApplication.systemLanguage == 1) {
            address.setText(mBean.getAddress());
            info.setText(mBean.getInfo());
        } else if (AppApplication.systemLanguage == 2) {
            if (mBean.getAddress_En() != null && !"".equals(mBean.getAddress_En())) {
                address.setText(mBean.getAddress_En());
            } else {
                address.setText(mBean.getAddress());
            }

            if (mBean.getInfo_En() != null && !"".equals(mBean.getInfo_En())) {
                info.setText(mBean.getInfo_En());
            } else {
                info.setText(mBean.getInfo());
            }
        }
        phone.setText(mBean.getPhone());
        fax.setText(mBean.getFax());
        internet.setText(mBean.getNet());
        location.setText(this.getResources().getString(R.string.exhibitor_detail_zanwei, mBean.getLocation()));

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingScheduleRoomLocationFragment locationFragment = new MeetingScheduleRoomLocationFragment();
                locationFragment.setRoomBean(null, mBean, MeetingScheduleRoomLocationFragment.TYPE_EXHIBITOR);
                action(locationFragment, R.string.plane,false, false);
            }
        });

        return view;
    }

    private void initViews(View view) {
        logo = (ImageView) view.findViewById(R.id.exhibitor_detail_logo);
        title = (TextView) view.findViewById(R.id.exhibitor_detail_title);
        address = (TextView) view.findViewById(R.id.exhibitor_detail_address);
        phone = (TextView) view.findViewById(R.id.exhibitor_detail_phone);
        fax = (TextView) view.findViewById(R.id.exhibitor_detail_fax);
        internet = (TextView) view.findViewById(R.id.exhibitor_detail_internet);
        location = (TextView) view.findViewById(R.id.exhibitor_detail_location);
        info = (TextView) view.findViewById(R.id.exhibitor_detail_info);
    }

    public void setExhibitor(ExhibitorBean mBean) {
        this.mBean = mBean;
    }
}
