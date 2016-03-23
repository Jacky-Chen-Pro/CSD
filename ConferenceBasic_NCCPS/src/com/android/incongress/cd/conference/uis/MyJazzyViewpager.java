package com.android.incongress.cd.conference.uis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.incongress.cd.conference.uis.jazzyviewpager.JazzyViewPager;

/**
 * Created by Jacky on 2016/2/18.
 */
public class MyJazzyViewpager extends JazzyViewPager {
    /** 是否可以滚动 **/
    private boolean isCanScroll = true;

    public MyJazzyViewpager(Context context) {
        super(context);
    }

    public MyJazzyViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }
}
