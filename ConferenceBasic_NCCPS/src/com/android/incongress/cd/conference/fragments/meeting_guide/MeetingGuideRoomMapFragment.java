package com.android.incongress.cd.conference.fragments.meeting_guide;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.BaseFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.utils.BitMapOption;
import com.polites.android.GestureImageView;

public class MeetingGuideRoomMapFragment extends BaseFragment {
    private String filepath;
    private GestureImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gestureimageview, null);
        mImageView = (GestureImageView) view.findViewById(R.id.gesture_imageview);
        File file = new File(filepath);
        BitMapOption mBitMapOption = new BitMapOption();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        Bitmap mBitmap = mBitMapOption.getBitmapFromFile(file, options.outWidth, options.outHeight);
        mImageView.setImageBitmap(mBitmap);
        return view;
    }

    public void setFilePath(String filepath) {
        this.filepath = filepath;
    }

}
