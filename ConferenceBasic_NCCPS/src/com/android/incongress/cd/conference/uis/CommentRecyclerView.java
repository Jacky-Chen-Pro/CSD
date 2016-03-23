package com.android.incongress.cd.conference.uis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Jacky on 2016/1/21.
 */
public class CommentRecyclerView extends RecyclerView {
    private OnSizeChangedListener onSizeChangedListener;

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    public interface OnSizeChangedListener {
        public void onSizeChanged();
    }

    public CommentRecyclerView(Context context) {
        super(context);
    }

    public CommentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onSizeChangedListener.onSizeChanged();
    }
}
