package com.yxr.base.view;

import androidx.annotation.NonNull;

import com.yxr.base.widget.status.UIStatus;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/18
 */
public interface IBaseStatusUiView extends IBaseUiView {
    /**
     * 展示内容状态UI
     */
    void showContent();

    /**
     * 展示加载中状态UI
     */
    void showLoading();

    /**
     * 展示加载中状态
     *
     * @param loadingText 加载中文案
     */
    void showLoading(String loadingText);

    /**
     * 展示空数据状态UI
     */
    void showEmpty();

    /**
     * 展示错误数据状态
     */
    void showError();

    /**
     * 展示网络错误数据状态
     */
    void showNetworkError();

    /**
     * 展示网络错误数据状态
     */
    void showNetworkError(String hintMessage);

    /**
     * 展示网络错误数据状态
     */
    void showNetworkError(String hintMessage, String retryText);

    /**
     * 改变UI状态
     *
     * @param uiStatus UI状态
     */
    void changUiStatus(@NonNull UIStatus uiStatus);
}
