package com.yxr.base.widget.layoutmanager;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPagerLayoutManager extends LinearLayoutManager {
    private PagerSnapHelper mPagerSnapHelper;
    private OnPageChangeListener onPageChangeListener;
    private int oldPosition = -1;

    public ViewPagerLayoutManager(Context context, int orientation) {
        this(context, orientation, false);
    }

    public ViewPagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        mPagerSnapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onScrollStateChanged(int state) {
        View snapView = mPagerSnapHelper.findSnapView(this);

        int position = -1;
        if (snapView != null) {
            //获取itemView的position
            position = getPosition(snapView);
        }
        boolean pageChanged = false;
        if (oldPosition != position && position >= 0) {
            oldPosition = position;
            pageChanged = true;
        }
        if (onPageChangeListener != null && pageChanged) {
            onPageChangeListener.onPageChanged(position);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener {
        void onPageChanged(int page);
    }
}
