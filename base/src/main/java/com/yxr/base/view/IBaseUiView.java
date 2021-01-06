package com.yxr.base.view;

import androidx.annotation.StringRes;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/18
 */
public interface IBaseUiView extends IBaseView {
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
    void showLoadingDialog();

    /**
     * 隐藏加载状态UI
     */
    void dismissLoadingDialog();
}
