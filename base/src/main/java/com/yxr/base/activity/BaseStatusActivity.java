package com.yxr.base.activity;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.yxr.base.R;
import com.yxr.base.widget.MultipleStatusView;
import com.yxr.base.widget.TitleBar;
import com.yxr.base.widget.UIStatus;

/**
 * @author ciba
 * @description 多UI状态Activity基类
 * @date 2020/09/17
 */
public abstract class BaseStatusActivity extends BaseActivity {
    protected MultipleStatusView msvBaseStatusView;
    protected View contentView;

    @Override
    public void setContentView(View view) {
        this.contentView = view;
        msvBaseStatusView = (MultipleStatusView) LayoutInflater.from(this).inflate(R.layout.layout_base_status, null);
        msvBaseStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadData();
            }
        });
        msvBaseStatusView.addView(contentView, 0, msvBaseStatusView.DEFAULT_LAYOUT_PARAMS);
        super.setContentView(msvBaseStatusView);
    }

    /**
     * 重新加载数据，默认重新走一遍initData();
     */
    protected void reloadData() {
        initData();
    }

    @Override
    public void changUiStatus(@NonNull UIStatus uiStatus) {
        if (msvBaseStatusView != null) {
            switch (uiStatus) {
                case LOADING:
                    if (getLoadingLayoutRes() == 0) {
                        msvBaseStatusView.showLoading();
                    } else {
                        msvBaseStatusView.showLoading(getLoadingLayoutRes(), null);
                    }
                    break;
                case EMPTY:
                    if (getEmptyLayoutRes() == 0) {
                        msvBaseStatusView.showEmpty();
                    } else {
                        msvBaseStatusView.showEmpty(getEmptyLayoutRes(), null);
                    }
                    break;
                case NETWORK_ERROR:
                    if (getNetWorkErrorLayoutRes() == 0) {
                        msvBaseStatusView.showNoNetwork();
                    } else {
                        msvBaseStatusView.showNoNetwork(getNetWorkErrorLayoutRes(), null);
                    }
                    break;
                case ERROR:
                    if (getErrorLayoutRes() == 0) {
                        msvBaseStatusView.showError();
                    } else {
                        msvBaseStatusView.showError(getErrorLayoutRes(), null);
                    }
                    break;
                default:
                    msvBaseStatusView.showContent();
                    break;
            }
        }
    }

    /**
     * 展示TitleBar
     */
    public void showTitleBar() {
        showTitleBar(null);
    }

    /**
     * 展示TitleBar
     *
     * @param title 标题
     */
    public void showTitleBar(String title) {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setVisibility(View.VISIBLE);
            titleBar.setTitle(title);
        }
    }

    /**
     * 获取TitleBar
     *
     * @return
     */
    public TitleBar getTitleBar() {
        return msvBaseStatusView == null ? null : msvBaseStatusView.getTitleBar();
    }

    /**
     * 获取多状态视图
     *
     * @return 多状态视图
     */
    public MultipleStatusView getStatusView() {
        return msvBaseStatusView;
    }

    /**
     * 获取内容视图
     *
     * @return 内容视图
     */
    public View getContentView() {
        return contentView;
    }

    @LayoutRes
    protected int getLoadingLayoutRes() {
        return 0;
    }

    @LayoutRes
    protected int getEmptyLayoutRes() {
        return 0;
    }

    @LayoutRes
    protected int getErrorLayoutRes() {
        return 0;
    }

    @LayoutRes
    protected int getNetWorkErrorLayoutRes() {
        return 0;
    }
}
