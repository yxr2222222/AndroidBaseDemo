package com.yxr.base.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxr.base.util.ListUtil;

import java.util.List;

public class BaseRefreshLayout<T extends BaseQuickAdapter> extends SmartRefreshLayout {
    private T adapter;

    public BaseRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setAdapter(T adapter) {
        this.adapter = adapter;
    }

    /**
     * 结束刷新或加载更多
     *
     * @param refresh 是否是刷新
     * @param success 获取数据是否成功
     */
    public void finish(boolean refresh, boolean success, boolean noMoreData) {
        if (refresh && !noMoreData) {
            finishRefresh(success);
        } else {
            finishLoadMore(0, success, noMoreData);
        }
        if (adapter != null) {
            List data = adapter.getData();
            boolean needNotifyDataSetChanged = false;
            if (adapter.hasEmptyView() && ListUtil.isEmpty(data)) {
                adapter.setUseEmpty(true);
                needNotifyDataSetChanged = true;
            }
            if (refresh && noMoreData) {
                if (!ListUtil.isEmpty(data)) {
                    data.clear();
                    needNotifyDataSetChanged = true;
                }
            }
            if (needNotifyDataSetChanged) {
                adapter.notifyDataSetChanged();
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
