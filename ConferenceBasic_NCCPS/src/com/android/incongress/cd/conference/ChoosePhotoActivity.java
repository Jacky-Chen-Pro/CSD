package com.android.incongress.cd.conference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.service.chooser.ChooserTargetService;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.ImageAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.FolderBean;
import com.android.incongress.cd.conference.uis.ListImgDirPopupWindow;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jacky on 2015/12/20.
 *
 * 目标
 * 1.尽可能的去避免内存溢出
 *      a.根据图片的显示大小去压缩图片
 *      b.使用缓存对我们的图片进行管理 LruCache
 * 2.用户操作UI控件必须充分的流畅
 *      a.getView里面尽可能不去做耗时操作 异步操作+回调显示
 * 3.用户预期显示的图片尽可能的快（图片加载策略的选择）-->LIFO/FIFO
 *      a.统一用ImageLoader统一加载图片
 */
public class ChoosePhotoActivity extends Activity {
    public static final String CHOOSE_PICS = "choose_pics";
    //已经选择上了的图片
    public String[] mSelectedPhotos;
    private GridView mGridView;
    private List<String> mImgs;

    private RelativeLayout mBottomLy;
    private TextView mDirCount,mDirName,mTitleText;
    private LinearLayout mLlTitleBack,mLlRightContainer;

    private ImageAdapter mImgAdapter;

    private File mCurrentDir;
    private int mMaxCount;

    private ProgressDialog mProgressDialog;

    private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();

    private ListImgDirPopupWindow mDirPopupWindow;

    private static final int DATA_LOADED = 0x110;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DATA_LOADED) {
                mProgressDialog.dismiss();

                //绑定数据到View中
                data2View();
                initDirPopupWindow();
            }
        }


    };

    private void initDirPopupWindow() {
        mDirPopupWindow = new ListImgDirPopupWindow(this, mFolderBeans);
        mDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mDirPopupWindow.setOnDirSelectedListener(new ListImgDirPopupWindow.OnDirSelectedListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());
                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png"))
                            return true;
                        return false;
                    }
                }));

                mImgAdapter = new ImageAdapter(ChoosePhotoActivity.this, mImgs, mCurrentDir.getAbsolutePath());

                for(int i=0 ;i<mSelectedPhotos.length; i++) {
                    mImgAdapter.addSlelectedImg(mSelectedPhotos[i]);
                }
                mGridView.setAdapter(mImgAdapter);

                mDirCount.setText(mImgs.size() + "");
                mDirName.setText(folderBean.getName());
                mDirPopupWindow.dismiss();
            }
        });
    }

    /**
     * 内容区域变量
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    private void data2View() {
        if(mCurrentDir == null) {
            Toast.makeText(ChoosePhotoActivity.this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }

        mImgs = Arrays.asList(mCurrentDir.list());
        mImgAdapter = new ImageAdapter(ChoosePhotoActivity.this, mImgs, mCurrentDir.getAbsolutePath());

        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName());

        mGridView.setAdapter(mImgAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_choose);
        initViews();
        initDatas();
        initEvent();

    }

    private void initEvent() {
        mBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDirPopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
                mDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                lightOff();
            }
        });
    }

    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 利用ContentProvider扫描手机中的所有图片
     */
    private void initDatas() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(ChoosePhotoActivity.this, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        //获取已经有的图片
        mSelectedPhotos = getIntent().getStringArrayExtra(CHOOSE_PICS);

        mProgressDialog = ProgressDialog.show(ChoosePhotoActivity.this,null,"正在加载");

        new Thread(){
            @Override
            public void run() {
                Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = ChoosePhotoActivity.this.getContentResolver();

                Cursor cursor = cr.query(imgUri, null, MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> dirPaths = new HashSet<String>();

                while (cursor.moveToNext()) {
                    String path =  cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //有些图片不存在
                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null)
                        continue;

                    String dirPath = parentFile.getAbsolutePath();

                    FolderBean folderBean = null;

                    if(dirPaths.contains(dirPath)) {
                        continue;
                    }else {
                        dirPaths.add(dirPath);

                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }

                    if(parentFile.list() == null) {
                        continue;
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if(filename.endsWith(".jpg")||filename.endsWith(".jpeg")||filename.endsWith(".png"))
                                return true;
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);

                    mFolderBeans.add(folderBean);

                    if(picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                cursor.close();
                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();
    }

    private void initViews() {
        mTitleText = (TextView) findViewById(R.id.title_text);
        mLlTitleBack = (LinearLayout)  findViewById(R.id.title_back_panel);
        mGridView = (GridView) findViewById(R.id.id_gridView);
        mBottomLy = (RelativeLayout)  findViewById(R.id.id_bottom_layout);
        mDirName = (TextView) findViewById(R.id.id_dir_name);
        mDirCount = (TextView) findViewById(R.id.id_dir_count);
        mLlRightContainer = (LinearLayout) findViewById(R.id.title_costom);

        TextView tvComplete = new TextView(this);
        tvComplete.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvComplete.setGravity(Gravity.CENTER);
        tvComplete.setText("完成");
        tvComplete.setTextColor(getResources().getColor(R.color.white));
        mLlRightContainer.addView(tvComplete);
        mLlRightContainer.setVisibility(View.VISIBLE);
        mLlRightContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                ChoosePhotoActivity.this.finish();
            }
        });

        mTitleText.setText(R.string.album);
        mLlTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePhotoActivity.this.finish();
                mImgAdapter.clearChoosePic();
            }
        });
    }



}
