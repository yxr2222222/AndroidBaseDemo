package com.yxr.base.fragment;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.yxr.base.R;
import com.yxr.base.view.IBaseStatusUiView;
import com.yxr.base.widget.status.MultipleStatusView;
import com.yxr.base.widget.TitleBar;
import com.yxr.base.widget.status.UIStatus;

/**
 * @author ciba
 * @description 多UI状态Fragment基类
 * @date 2020/10/16
 */
public abstract class BaseStatusFragment extends BaseFragment implements IBaseStatusUiView {
    private MultipleStatusView msvBaseStatusView;
    private View contentView;

    @Override
    public View inflaterRootView(@NonNull LayoutInflater inflater) {
        msvBaseStatusView = (MultipleStatusView) LayoutInflater.from(activity).inflate(R.layout.layout_base_status, null);
        msvBaseStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadData();
            }
        });
        contentView = LayoutInflater.from(activity).inflate(contentView(), null);
        msvBaseStatusView.addView(contentView, 0, msvBaseStatusView.DEFAULT_LAYOUT_PARAMS);
        return msvBaseStatusView;
    }

    /**
     * 重新加载数据，默认重新走一遍initData();
     */
    protected void reloadData() {
        initData();
    }

    @Override
    public void showLoading() {
        changUiStatus(UIStatus.LOADING);
    }

    @Override
    public void showContent() {
        changUiStatus(UIStatus.CONTENT);
    }

    @Override
    public void showEmpty() {
        changUiStatus(UIStatus.EMPTY);
    }

    @Override
    public void showNetworkError() {
        changUiStatus(UIStatus.NETWORK_ERROR);
    }

    @Override
    public void showError() {
        changUiStatus(UIStatus.ERROR);
    }

    @Override
    public void changUiStatus(@NonNull UIStatus uiStatus) {
        if (msvBaseStatusView != null) {
            msvBaseStatusView.changUiStatus(uiStatus);
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
    public void showTitleBar(CharSequence title) {
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
}
