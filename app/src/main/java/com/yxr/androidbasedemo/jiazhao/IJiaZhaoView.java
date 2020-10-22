package com.yxr.androidbasedemo.jiazhao;

import android.support.annotation.NonNull;

import com.yxr.base.view.IBaseStatusUiView;
import com.yxr.base.view.IBaseUiView;
import com.yxr.base.view.IBaseView;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
public interface IJiaZhaoView extends IBaseStatusUiView {
    /**
     * 驾照考题获取成功
     *
     * @param response 驾照考题内容
     */
    void jiaZhaoGetSuccess(@NonNull JiaZhaoResponse response);
}
