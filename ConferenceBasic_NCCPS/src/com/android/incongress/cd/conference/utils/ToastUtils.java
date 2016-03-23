package com.android.incongress.cd.conference.utils;

import android.widget.Toast;

import com.android.incongress.cd.conference.base.AppApplication;

/**
 * Created by Jacky on 2016/1/15.
 */
public class ToastUtils {
    public static void showShorToast(String msg) {
        Toast.makeText(AppApplication.getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String msg) {
        Toast.makeText(AppApplication.getContext(),msg,Toast.LENGTH_LONG).show();
    }
}
