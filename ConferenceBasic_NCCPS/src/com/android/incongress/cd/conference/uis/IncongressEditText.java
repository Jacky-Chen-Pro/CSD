package com.android.incongress.cd.conference.uis;

import com.android.incongress.cd.conference.base.AppApplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class IncongressEditText extends EditText {

	public IncongressEditText(Context context) {
		super(context);
		if(AppApplication.mTypeface!=null){
			setTypeface(AppApplication.mTypeface);
		}
	}
	public IncongressEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(AppApplication.mTypeface!=null){
			setTypeface(AppApplication.mTypeface);
		}
	}

}
