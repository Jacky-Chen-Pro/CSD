package com.android.incongress.cd.conference.uis;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class SimpleProgressDialog extends Dialog {
	
	private static SimpleProgressDialog simpleProgressDialog = null;
	private static MySimpleDialogCloseListener mSimpleDialogCloseListener;
	
	public SimpleProgressDialog(Context context){
		super(context);
	}
	
	public SimpleProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static SimpleProgressDialog createDialog(Context context, MySimpleDialogCloseListener simpleDialogCloseListener){
		simpleProgressDialog = new SimpleProgressDialog(context, R.style.SimpleProgressDialog);
		simpleProgressDialog.setContentView(R.layout.simple_progress_dialog);
		simpleProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		TextView text = (TextView) simpleProgressDialog.findViewById(android.R.id.text1);
		text.setVisibility(View.VISIBLE);

		ImageView ivClose = (ImageView)simpleProgressDialog.findViewById(R.id.iv_close);

		ivClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mSimpleDialogCloseListener.doWhenDialogClose();
			}
		});

		mSimpleDialogCloseListener = simpleDialogCloseListener;

		return simpleProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	if (simpleProgressDialog == null){
    		return;
    	}
    	
//        ImageView imageView = (ImageView) simpleProgressDialog.findViewById(android.R.id.progress);
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//        animationDrawable.start();
    }
 
    public SimpleProgressDialog setTitile(String strTitle){
    	return simpleProgressDialog;
    }
    
    public SimpleProgressDialog setMessage(String strMessage){
    	TextView text = (TextView) simpleProgressDialog.findViewById(android.R.id.text1);
    	if (!StringUtils.isBlank(strMessage)) {
    		text.setVisibility(View.VISIBLE);
    		text.setText(strMessage);
    	} else {
    		text.setVisibility(View.GONE);
    	}
		text.setVisibility(View.VISIBLE);
    	return simpleProgressDialog;
    }

	public interface MySimpleDialogCloseListener{
		public void doWhenDialogClose();
	}
}