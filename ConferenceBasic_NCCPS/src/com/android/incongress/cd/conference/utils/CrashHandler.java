package com.android.incongress.cd.conference.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.android.incongress.cd.conference.base.AppApplication;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {
    private static CrashHandler handler;
    private static Context mContext;

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        System.out.println(error);
        AppApplication.instance().stopService();
		System.exit(100);
    }

    public static CrashHandler getInstance() {
        if (handler == null) {
            handler = new CrashHandler();
        }
        return handler;
    }

    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出错,请重新进入", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }
        }.start();
        return true;
    }

}
