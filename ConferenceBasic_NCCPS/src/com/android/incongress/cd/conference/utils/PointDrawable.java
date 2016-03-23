package com.android.incongress.cd.conference.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * 会议日程三个选择下面的线条，带向上的箭头
 * @author Administrator
 *
 */
public class PointDrawable {

    private Drawable dleft;
    private Drawable dmid;
    private Drawable dright;
    private int parentLeft = 0;
    private int parentWidth = 0;
    private int popupWindowHeight = 0;
    private int screenwidth;
    private Drawable bg;
    private Context context;
    private static PointDrawable mInstance;

    public PointDrawable(Context context) {
        this.dleft = context.getResources().getDrawable(R.drawable.left);
        this.dmid = context.getResources().getDrawable(R.drawable.mid);
        this.dright = context.getResources().getDrawable(R.drawable.right); 
        this.context = context;
        setCursorWidth();
    }
    
    public PointDrawable(Context context,boolean jacky) {
        this.dleft = context.getResources().getDrawable(R.drawable.left2);
        this.dmid = context.getResources().getDrawable(R.drawable.mid2);
        this.dright = context.getResources().getDrawable(R.drawable.right2); 
        this.context = context;
        setCursorWidth();
    }

    private void drawbitMap(Bitmap canvasBitmap, Bitmap drawBitmap, int top, int left) {
        Canvas localCanvas = new Canvas(canvasBitmap);
        localCanvas.drawBitmap(drawBitmap, left, top, null);
        localCanvas.save(Canvas.ALL_SAVE_FLAG);
        localCanvas.restore();
        drawBitmap.recycle();
    }

    private void getBitMap(Rect paramRect, Drawable drawable, Bitmap canvasBitmap) {
        drawable.setBounds(0, 0, paramRect.right, paramRect.bottom);
        Canvas localCanvas = new Canvas(canvasBitmap);
        drawable.draw(localCanvas);
        localCanvas.save(Canvas.ALL_SAVE_FLAG);
        localCanvas.restore();
    }

    private void createDrawable(Context context,int index) {
        parentWidth = screenwidth/3;
        parentLeft = index*parentWidth;
        
        Rect[] arrayOfRect = new Rect[3];
        arrayOfRect[0] = new Rect();
        arrayOfRect[0].top = 0;
        arrayOfRect[0].left = 0;
        arrayOfRect[0].right = this.parentLeft + this.parentWidth / 4;
        arrayOfRect[0].bottom = this.popupWindowHeight;

        arrayOfRect[1] = new Rect();
        arrayOfRect[1].top = 0;
        arrayOfRect[1].left = arrayOfRect[0].right;
        arrayOfRect[1].right = this.parentWidth / 2;
        arrayOfRect[1].bottom = this.popupWindowHeight;

        arrayOfRect[2] = new Rect();
        arrayOfRect[2].top = 0;
        arrayOfRect[2].left = this.parentLeft + this.parentWidth * 3 / 4;
        arrayOfRect[2].right = screenwidth - arrayOfRect[2].left;
        arrayOfRect[2].bottom = this.popupWindowHeight;

        Drawable[] arrayOfDrawable = new Drawable[3];
        arrayOfDrawable[0] = dleft;
        arrayOfDrawable[1] = dmid;
        arrayOfDrawable[2] = dright;
        bg = getDrawable(context, arrayOfRect, arrayOfDrawable);
    }
    
    /**
     * 添加将屏幕横向分成四份
     * @param context
     * @param index
     */
    private void createDrawable2(Context context,int index) { 
        parentWidth = screenwidth/4;
        parentLeft = index*parentWidth;
        
        Rect[] arrayOfRect = new Rect[3];
        arrayOfRect[0] = new Rect();
        arrayOfRect[0].top = 0;
        arrayOfRect[0].left = 0;
        arrayOfRect[0].right = this.parentLeft + this.parentWidth / 4;
        arrayOfRect[0].bottom = this.popupWindowHeight;

        arrayOfRect[1] = new Rect();
        arrayOfRect[1].top = 0;
        arrayOfRect[1].left = arrayOfRect[0].right;
        arrayOfRect[1].right = this.parentWidth / 2;
        arrayOfRect[1].bottom = this.popupWindowHeight;

        arrayOfRect[2] = new Rect();
        arrayOfRect[2].top = 0;
        arrayOfRect[2].left = this.parentLeft + this.parentWidth * 3 / 4;
        arrayOfRect[2].right = screenwidth - arrayOfRect[2].left;
        arrayOfRect[2].bottom = this.popupWindowHeight;

        Drawable[] arrayOfDrawable = new Drawable[3];
        arrayOfDrawable[0] = dleft;
        arrayOfDrawable[1] = dmid;
        arrayOfDrawable[2] = dright;
        bg = getDrawable(context, arrayOfRect, arrayOfDrawable);
    }

    private Drawable getDrawable(Context context, Rect[] ArrayOfRect, Drawable[] ArrayOfDrawable) {
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap paramBitmap = Bitmap.createBitmap(screenwidth, popupWindowHeight, localConfig);

        for (int i = 0; i < ArrayOfDrawable.length; i++) {
            Rect localRect = ArrayOfRect[i];
            Bitmap localBitmap = Bitmap.createBitmap(localRect.right, localRect.bottom, localConfig);
            Drawable localDrawable = ArrayOfDrawable[i];
            getBitMap(localRect, localDrawable, localBitmap);
            drawbitMap(paramBitmap, localBitmap, localRect.top, localRect.left);
            localBitmap.recycle();
        }
        return new BitmapDrawable(context.getResources(), paramBitmap);
    }

    public static Drawable getBackgroud(int index, Context context) {
        if (mInstance == null) {
            mInstance = new PointDrawable(context);
        }
        mInstance.bg = null;
        mInstance.createDrawable(context,index);
        return mInstance.bg;
    }
    
    public static Drawable getBackgroud2(int index, Context context) {
        if (mInstance == null) {
            mInstance = new PointDrawable(context,true);
        }
        mInstance.bg = null;
        mInstance.createDrawable2(context,index);
        return mInstance.bg;
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        popupWindowHeight = (int)(dm.density*300);
        return screenW;

    }

    public void setCursorWidth() {
        screenwidth = getScreenWidth();
    }

}
