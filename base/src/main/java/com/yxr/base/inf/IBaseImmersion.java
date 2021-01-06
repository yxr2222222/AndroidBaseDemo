package com.yxr.base.inf;

import androidx.annotation.ColorRes;

public interface IBaseImmersion {
    /**
     * 是否需要沉浸式
     */
    boolean needImmersion();

    /**
     * 状态栏文字是否是深色
     */
    boolean statusBarDarkFont();

    /**
     * 状态栏颜色
     */
    @ColorRes
    int statusBarColor();

    /**
     * 内容是否不顶到状态栏
     */
    boolean fitsSystemWindows();
}
