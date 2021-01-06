package com.yxr.base.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class BaseRefreshLayout extends SmartRefreshLayout {
    public static final int TYPE_FRESH = 0;
    public static final int TYPE_LOAD_MORE = 1;
    private BaseQuickAdapter baseQuickAdapter;

    public BaseRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static int getFinishType(boolean refresh) {
        return refresh ? TYPE_FRESH : TYPE_LOAD_MORE;
    }

    public void setBaseQuickAdapter(BaseQuickAdapter baseQuickAdapter) {
        this.baseQuickAdapter = baseQuickAdapter;
    }

    public void finish(boolean refresh, boolean success, boolean noMoreData) {
        finish(getFinishType(refresh), success, noMoreData);
    }

    /**
     * 结束刷新或加载更多
     *
     * @param type    ：类型
     * @param success ：获取数据是否成功
     */
    public void finish(int type, boolean success, boolean noMoreData) {
        if (TYPE_FRESH == type) {
            finishRefresh(success);
        } else if (TYPE_LOAD_MORE == type) {
            finishLoadMore(0, success, noMoreData);
        }
        if (baseQuickAdapter != null) {
            baseQuickAdapter.setUseEmpty(true);
            if (!success) {
                baseQuickAdapter.notifyDataSetChanged();
            }
        }
    }

    private void init() {
        //显示下拉高度/手指真实下拉高度=阻尼效果
        setDragRate(0.5f);
        //是否启用越界回弹
        setEnableOverScrollBounce(true);
        //回弹动画时长（毫秒）
        setReboundDuration(500);
        //是否启用越界拖动（仿苹果效果）
        setEnableOverScrollDrag(true);
        // 设置默认的刷新布局
        setRefreshHeader(new DefaultRefreshHeader(getContext()));
        // 设置默认的加载更多布局
        setRefreshFooter(new DefaultRefreshFooter(getContext()));
        //是否在刷新的时候禁止列表的操作
        setDisableContentWhenRefresh(false);
        //是否在加载的时候禁止列表的操作
        setDisableContentWhenLoading(false);
    }
}
