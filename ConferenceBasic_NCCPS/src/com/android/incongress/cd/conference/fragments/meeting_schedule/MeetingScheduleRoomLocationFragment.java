package com.android.incongress.cd.conference.fragments.meeting_schedule;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.ExhibitorBean;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.polites.android.GestureImageView;

/**
 * 会议详情 定位图
 */
public class MeetingScheduleRoomLocationFragment extends BaseFragment {
    private ClassesBean mClassesBean;
    private ExhibitorBean mExhibitorBean;

    private GestureImageView mImageView;
    private int mType = 1;
    public static final int TYPE_MEETING = 1;
    public static final int TYPE_EXHIBITOR = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.metting_schedule_room_location, null);
        mImageView = (GestureImageView) view.findViewById(R.id.metting_schedule_room_location_image);

        if(mType == TYPE_MEETING) {
            String bitmapPath = "";
            if (mClassesBean.getMapName() != null && !mClassesBean.getMapName().equals("")) {
                bitmapPath = AppApplication.instance().getSDPath() + Constants.FILESDIR + mClassesBean.getMapName();
            }

            if (!bitmapPath.equals("")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap mBitmap = BitmapFactory.decodeFile(bitmapPath, options);
                Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                String[] Points = mClassesBean.getClassesLocation().split(",");
                int x = Integer.parseInt(Points[0]);
                int y = Integer.parseInt(Points[1]);
                int w = Integer.parseInt(Points[2]);
                int h = Integer.parseInt(Points[3]);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.tuding);
                int iconx = (w - icon.getWidth()) / 2 + x;
                int icony = (h - icon.getHeight()) / 2 + y;
                canvas.drawBitmap(icon, iconx, icony, null);
                mBitmap.recycle();
                mImageView.setImageBitmap(bitmap);
            }
        }else if(mType == TYPE_EXHIBITOR){
            String bitmapPath = "";
            if(!StringUtils.isEmpty(mExhibitorBean.getMapName())) {
                String bitmapName = mExhibitorBean.getMapName().substring(mExhibitorBean.getMapName().lastIndexOf("/"), mExhibitorBean.getMapName().length());
                bitmapPath = AppApplication.instance().getSDPath() + Constants.FILESDIR + bitmapName;
            }

            if(!StringUtils.isEmpty(bitmapPath)) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap mBitmap = BitmapFactory.decodeFile(bitmapPath, options);
                Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                String[] Points = mExhibitorBean.getExhibitorsLocation().split(",");
                int x = Integer.parseInt(Points[0]);
                int y = Integer.parseInt(Points[1]);
                int w = Integer.parseInt(Points[2]);
                int h = Integer.parseInt(Points[3]);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.tuding);
                int iconx = (w - icon.getWidth()) / 2 + x;
                int icony = (h - icon.getHeight()) / 2 + y;
                canvas.drawBitmap(icon, iconx, icony, null);
                mBitmap.recycle();
                mImageView.setImageBitmap(bitmap);
            }
        }

        return view;
    }

    public void setRoomBean(ClassesBean classesBean, ExhibitorBean exhibitorBean, int type) {
        this.mClassesBean = classesBean;
        this.mExhibitorBean = exhibitorBean;
        this.mType = type;
    }
}
