package com.android.incongress.cd.conference.uis;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.FolderBean;
import com.android.incongress.cd.conference.utils.ImageLoaderForPhotoChoose;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;


/**
 * Created by Jacky on 2015/12/20.
 */
public class ListImgDirPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;

    private List<FolderBean> mDatas;

    public interface OnDirSelectedListener {
        void onSelected(FolderBean folderBean);
    }

    public OnDirSelectedListener mListener;

    public void setOnDirSelectedListener(OnDirSelectedListener mListener) {
        this.mListener = mListener;
    }

    public ListImgDirPopupWindow(Context context, List<FolderBean> datas) {
        super(context);
        calWidthAndHeight(context);

        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_main,null);
        mDatas = datas;

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true); //外部可以点击
        setBackgroundDrawable(new BitmapDrawable());;//点击外部消失
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initViews(context);
        initEvent();
    }

    private void initViews(Context context) {
       mListView = (ListView) mConvertView.findViewById(R.id.id_list_dir);
        mListView.setAdapter(new ListDirAdapter(context,mDatas));
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mListener != null)
                    mListener.onSelected(mDatas.get(position));
            }
        });
    }


    private class ListDirAdapter extends ArrayAdapter<FolderBean> {
        private LayoutInflater mInflater;
        private List<FolderBean> mDatas;
        public ListDirAdapter(Context context, List<FolderBean> objects) {
            super(context,0,objects);
            mInflater = LayoutInflater.from(context);
            mDatas = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView ==null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_popup_main, parent, false);
                holder.imageView = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
                holder.dirName = (TextView) convertView.findViewById(R.id.id_dir_item_name);
                holder.dirCount = (TextView) convertView.findViewById(R.id.id_dir_item_count);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            FolderBean bean = mDatas.get(position);
            //重置图片
            holder.imageView.setImageResource(R.drawable.default_load_bg);
            bean.getFirstImgPath();
            ImageLoaderForPhotoChoose.getInstance().loadImage(bean.getFirstImgPath(), holder.imageView);

            holder.dirName.setText(bean.getName());
            holder.dirCount.setText(bean.getCount() + "");

            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView dirName;
            TextView dirCount;
        }
    }

    /**
     * 计算popupWindow的高度和宽度
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.7);
    }
}
