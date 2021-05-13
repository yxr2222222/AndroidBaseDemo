package com.yxr.base.activity;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxr.base.R;
import com.yxr.base.view.IBaseStatusUiView;
import com.yxr.base.widget.DefaultAnimLoadingView;
import com.yxr.base.widget.status.MultipleStatusView;
import com.yxr.base.widget.TitleBar;
import com.yxr.base.widget.status.UIStatus;

/**
 * @author ciba
 * @description 多UI状态Activity基类
 * @date 2020/09/17
 */
public abstract class BaseStatusActivity extends BaseActivity implements IBaseStatusUiView {
    protected MultipleStatusView msvBaseStatusView;
    protected View contentView;
    private DefaultAnimLoadingView animLoadingView;
    private TextView tvRetryHint, tvRetry;

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
        msvBaseStatusView.setLoadingLayoutResId(R.layout.layout_default_anim_loading);
        super.setContentView(msvBaseStatusView);
    }

    /**
     * 重新加载数据，默认重新走一遍initData();
     */
    protected void reloadData() {

    }

    @Override
    public void showContent() {
        changUiStatus(UIStatus.CONTENT);
    }

    @Override
    public void showLoading() {
        showLoading(null);
    }

    @Override
    public void showLoading(String loadingText) {
        changUiStatus(UIStatus.LOADING);
        if (animLoadingView == null && msvBaseStatusView != null) {
            View loadingView = msvBaseStatusView.findViewById(R.id.loadingView);
            if (loadingView != null && loadingView instanceof DefaultAnimLoadingView) {
                animLoadingView = (DefaultAnimLoadingView) loadingView;
            }
        }
        if (animLoadingView != null) {
            animLoadingView.startLoading();
            animLoadingView.setLoadingText(loadingText);
        }
    }

    @Override
    public void showEmpty() {
        changUiStatus(UIStatus.EMPTY);
    }

    @Override
    public void showNetworkError() {
        showNetworkError(null);
    }

    @Override
    public void showNetworkError(String hintMessage) {
        showNetworkError(hintMessage, null);
    }

    @Override
    public void showNetworkError(String hintMessage, String retryText) {
        changUiStatus(UIStatus.NETWORK_ERROR);

        if (tvRetryHint == null) {
            tvRetryHint = msvBaseStatusView.findViewById(R.id.tvRetryHint);
        }
        if (tvRetryHint != null) {
            tvRetryHint.setText(TextUtils.isEmpty(hintMessage) ? getString(R.string.load_error_refresh_please) : hintMessage);
        }

        if (retryText != null) {
            if (tvRetry == null) {
                tvRetry = msvBaseStatusView.findViewById(R.id.no_network_retry_view);
            }
            if (tvRetry != null) {
                tvRetry.setText(retryText);
            }
        }
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
        if (animLoadingView != null && UIStatus.LOADING != uiStatus) {
            animLoadingView.pauseLoading();
        }
    }

    /**
     * 获取内容视图
     *
     * @return 内容视图
     */
    @Override
    public View getContentView() {
        return contentView;
    }

    @Override
    protected void onDestroy() {
        if (animLoadingView != null) {
            animLoadingView.cancelLoading();
        }
        super.onDestroy();
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

    protected void showTitleBar(boolean immersive
            , boolean isContentNotBelowTitleBar
            , int dividerHeight
            , @ColorInt int backgroundColor
            , @DrawableRes int leftImageResource
            , String title) {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setImmersive(immersive);
            titleBar.setDividerHeight(dividerHeight);
            titleBar.setBackgroundColor(backgroundColor);
            titleBar.setLeftImageResource(leftImageResource);
            if (isContentNotBelowTitleBar) {
                getStatusView().DEFAULT_LAYOUT_PARAMS.removeRule(RelativeLayout.BELOW);
            }
            showTitleBar(title);
        }
    }

    /**
     * 获取TitleBar
     *
     * @return TitleBar
     */
    public TitleBar getTitleBar() {
        return msvBaseStatusView == null ? null : msvBaseStatusView.getTitleBar();
    }
}
