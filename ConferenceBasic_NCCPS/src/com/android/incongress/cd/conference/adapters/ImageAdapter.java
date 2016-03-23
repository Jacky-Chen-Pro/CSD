package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.incongress.cd.conference.utils.ImageLoaderForPhotoChoose;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends BaseAdapter {
    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;

    //静态变量用于保存已经勾选的图片
    private static Set<String> mSelectedImg = new HashSet<String>();

    public static Set<String> getmSelectedImg() {
        return mSelectedImg;
    }

    public static void setmSelectedImg(Set<String> mSelectedImg) {
        ImageAdapter.mSelectedImg = mSelectedImg;
    }

    public void addSlelectedImg(String path) {
        mSelectedImg.add(path);
    }

    public ImageAdapter(Context context, List<String> datas, String dirPath) {
        this.mDirPath = dirPath;
        this.mImgPaths = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_gridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_item_image);
            viewHolder.mSelect = (ImageButton) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String filePath = mDirPath +"/" + mImgPaths.get(position);

        //为了防止从一屏到另一屏的时候，看到的是原来的一屏的图片，再变回现在的图片，用默认的图片效果更好
        viewHolder.mImg.setImageResource(R.drawable.default_load_bg);
        viewHolder.mSelect.setImageResource(R.drawable.picture_unselected);
        viewHolder.mImg.setColorFilter(null);

        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedImg.contains(filePath)) {
                    //已被選擇
                    mSelectedImg.remove(filePath);
                    viewHolder.mImg.setColorFilter(null);
                    viewHolder.mSelect.setImageResource(R.drawable.picture_unselected);
                } else {
                    if(mSelectedImg.size() == 9) {
                        ToastUtils.showShorToast("选择的图片不能超过9张哦...");
                    }else {
                        //未被選擇
                        mSelectedImg.add(filePath);
                        viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                        viewHolder.mSelect.setImageResource(R.drawable.picture_selected);
                    }
                }

                Iterator<String> imgs = mSelectedImg.iterator();
                while(imgs.hasNext()) {
                    MyLogger.jLog().i(imgs.next());
                }
            }
        });

        if(mSelectedImg.contains(filePath)) {
            viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.mSelect.setImageResource(R.drawable.picture_selected);
        }

        ImageLoaderForPhotoChoose.getInstance(3, ImageLoaderForPhotoChoose.Type.LIFO).loadImage(mDirPath + "/" + mImgPaths.get(position), viewHolder.mImg);
        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
        ImageButton mSelect;
    }

    /**
     * 清楚已经选择的图片
     */
    public void clearChoosePic() {
        mSelectedImg.clear();
    }
}


