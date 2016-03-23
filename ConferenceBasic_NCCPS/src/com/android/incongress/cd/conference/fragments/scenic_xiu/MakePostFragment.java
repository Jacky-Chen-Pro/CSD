package com.android.incongress.cd.conference.fragments.scenic_xiu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.incongress.cd.conference.ChoosePhotoActivity;
import com.android.incongress.cd.conference.adapters.ImageAdapter;
import com.android.incongress.cd.conference.adapters.MyGridAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.uis.IconChoosePopupWindow;
import com.android.incongress.cd.conference.uis.MyGridView;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Jacky on 2016/1/15.
 * 发帖页面
 */
public class MakePostFragment extends BaseFragment {
    private EditText mEtPostContent;
    private IconChoosePopupWindow mPopupChoosePhotos;
    private MyGridView mGvPics;
    private ImageView mIvChoosePhoto;
    private MyGridAdapter myGridAdapter;
    private View mMakePost;
    private ProgressDialog mProgressDialog;

    private ArrayList<String> mPhotosPaths = new ArrayList<String>();

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int ALBUM_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    //当前传送的图片顺序
    private int mCurrentPostImg = 0;

    private Uri fileUri;
    //发布的现场秀id
    private String mPostScenicShowId = "-1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenicxiu_makepost, null);

        mEtPostContent = (EditText) view.findViewById(R.id.et_post_content);
        mGvPics = (MyGridView) view.findViewById(R.id.gv_pic);
        mIvChoosePhoto = (ImageView) view.findViewById(R.id.iv_add_pic);

        mIvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow();
                mPopupChoosePhotos.showAtLocation(mEtPostContent, Gravity.BOTTOM, 0, 0);
                lightOff();

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtPostContent.getWindowToken(),0);
            }
        });

        myGridAdapter = new MyGridAdapter(mPhotosPaths, getActivity());
        mGvPics.setAdapter(myGridAdapter);

        return view;
    }

    public void setRightView(View view) {
        mMakePost = view;
        mMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotosPaths.size() != 0 || !StringUtils.isEmpty(mEtPostContent.getText().toString().trim())) {
                    //先传图片，后穿文字
                    doCreateImge();
                } else {
                    ToastUtils.showShorToast("发布内容不能为空");
                }
            }
        });
    }

    private void initPopupWindow() {
        mPopupChoosePhotos = new IconChoosePopupWindow(getActivity());
        mPopupChoosePhotos.setAnimationStyle(R.style.icon_popup_window);
        mPopupChoosePhotos.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String[] paths = new String[mPhotosPaths.size()];
                for (int i = 0; i < mPhotosPaths.size(); i++) {
                    paths[i] = mPhotosPaths.get(i);
                }
                intent.putExtra(ChoosePhotoActivity.CHOOSE_PICS, paths);
                intent.setClass(getActivity(), ChoosePhotoActivity.class);
                startActivityForResult(intent, ALBUM_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPhotosPaths.size() == 9) {
                    ToastUtils.showShorToast("选择的图片不能超过9张哦...");
                    return;
                }

                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //将拍照的图片保存到指定的地址，获取的时候就从fileUri中获取，否则因为设定了输出图片的位置，在onActivityResult()方法中，获取到的data是空的
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        mPopupChoosePhotos.getContentView().findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupChoosePhotos.dismiss();
            }
        });
    }


    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.CACHE_FILE_NAME);
        } else {
            mediaStorageDir = new File(getActivity().getCacheDir(), Constants.CACHE_FILE_NAME);
        }

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Constants.CACHE_FILE_NAME, "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                mPhotosPaths.add(getRealPathFromURI(fileUri));
                myGridAdapter.notifyDataSetChanged();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Action Capture Photo Canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Action Capture Photo Failed", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == ALBUM_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                mPhotosPaths.clear();

                Set<String> imgs = ImageAdapter.getmSelectedImg();
                Iterator<String> imgsIterator = imgs.iterator();
                while (imgsIterator.hasNext()) {
                    String photoPath = imgsIterator.next();
                    mPhotosPaths.add(photoPath);
                    MyLogger.jLog().i(photoPath);
                }
                myGridAdapter.notifyDataSetChanged();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Action Album Photo Canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Action Album Photo Failed", Toast.LENGTH_SHORT).show();
            }
        }

        if (mPhotosPaths.size() > 0) {
            mGvPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    initPopupWindow();
                    mPopupChoosePhotos.showAtLocation(mEtPostContent, Gravity.BOTTOM, 0, 0);
                    lightOff();
                }
            });
            mIvChoosePhoto.setVisibility(View.GONE);
        } else {
            mIvChoosePhoto.setVisibility(View.VISIBLE);
        }

        mPopupChoosePhotos.dismiss();
    }

    /**
     * 并不建议这么做，因为这个操作需要查询数据库，另外一种就是直接获取Uri中的内容，也就是图片流
     *
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImageAdapter.getmSelectedImg().clear();
    }


    //递归上传图片
    private void doCreateImge() {
        if (mPhotosPaths.size() == 0) {
            doCreateContent();
        } else {
            CHYHttpClientUsage.getInstanse().doCreateSceneShowImg(AppApplication.conId + "", AppApplication.userId + "", AppApplication.userType + "",mPostScenicShowId, new File(mPhotosPaths.get(mCurrentPostImg)), new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    if (mCurrentPostImg == 0)
                        mProgressDialog = ProgressDialog.show(getActivity(), null, "正在发布...");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    MyLogger.jLog().i(response.toString());

                    try {
                        mPostScenicShowId = response.getInt("sceneShowId") + "";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (mCurrentPostImg < mPhotosPaths.size() - 1) {
                        mCurrentPostImg++;
                        doCreateImge();
                    } else {
                        doCreateContent();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    /**
     * 发送文字内容
     */
    private void doCreateContent() {
        String content = mEtPostContent.getText().toString().trim();

        try {
            CHYHttpClientUsage.getInstanse().doCreateSceneShowTxt(AppApplication.conId + "", AppApplication.userId + "", AppApplication.userType + "", mPostScenicShowId, URLEncoder.encode(content, Constants.ENCODING_UTF8), new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    if (mPhotosPaths.size() == 0)
                        mProgressDialog = ProgressDialog.show(getActivity(), null, "正在发布...");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                        mPostScenicShowId = "-1";
                        performback();

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEtPostContent.getWindowToken(), 0);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    MyLogger.jLog().i(responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    MyLogger.jLog().i(response.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

