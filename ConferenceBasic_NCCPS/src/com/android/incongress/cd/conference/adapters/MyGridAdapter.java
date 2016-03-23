package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.incongress.cd.conference.utils.ImageLoaderForPhotoChoose;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/6/26.
 */
public class MyGridAdapter extends BaseAdapter {
    private List<String> imgsPaths = new ArrayList<String>();
    private Context mContext;

    public MyGridAdapter(List<String> paths, Context ctx){
        this.mContext = ctx;
        this.imgsPaths = paths;
    }

    @Override
    public int getCount() {
        return imgsPaths.size();
    }

    @Override
    public Object getItem(int i) {
        return imgsPaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.my_grid_item, null);
            holder.pic = (ImageView)view.findViewById(R.id.id_item_image);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        ImageLoaderForPhotoChoose.getInstance().loadImage(imgsPaths.get(i),holder.pic);
        return view;
    }

    class ViewHolder {
        ImageView pic;
    }
}
