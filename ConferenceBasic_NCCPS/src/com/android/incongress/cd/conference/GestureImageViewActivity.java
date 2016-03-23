package com.android.incongress.cd.conference;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;

import java.io.File;


public class GestureImageViewActivity extends FragmentActivity{
    private String filepath;
    private GestureImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gestureimageviewactivity);
		mImageView=(GestureImageView)this.findViewById(R.id.gesture_imageview);
		filepath=getIntent().getStringExtra("filepath");
		ImageLoader.getInstance().displayImage(Uri.fromFile(new File(filepath)).toString(),mImageView);
		mImageView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				GestureImageViewActivity.this.finish();
			}
			
		});
	}
}
