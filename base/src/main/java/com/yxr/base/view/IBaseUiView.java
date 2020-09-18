package com.yxr.base.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.yxr.base.widget.UIStatus;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/18
 */
public interface IBaseUiView {
    /**
     * String资源文件吐司
     *
     * @param stringRes String资源
     */
    void toast(@StringRes int stringRes);

    /**
     * 文字吐司
     *
     * @param message 文字
     */
    void toast(String message);

    /**
     * 展示加载状态UI
     */
    void showLoading();

    /**
     * 展示内容状态UI
     */
    void showContent();

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
     * 改变UI状态
     *
     * @param uiStatus UI状态
     */
    void changUiStatus(@NonNull UIStatus uiStatus);
}
