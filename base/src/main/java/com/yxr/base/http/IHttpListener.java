package com.yxr.base.http;

import androidx.annotation.NonNull;

import io.reactivex.disposables.Disposable;

/**
 * @author ciba
 * @description 接口调用监听器
 * @date 2018/10/15
 */
public interface IHttpListener<T> {
    /**
     * 网络请求开始
     *
     * @param disposable rxJava任务
     */
    void onStart(Disposable disposable);

    /**
     * 网络请求成功
     *
     * @param response 获取的数据
     */
    void onSuccess(@NonNull T response);

    /**
     * 网络请求失败
     *
     * @param code    失败错误码
     * @param message 失败错误信息
     */
    void onFailed(int code, String message);

    void onDestroy();
}
