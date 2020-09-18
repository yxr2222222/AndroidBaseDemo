package com.yxr.base.http;

import android.support.annotation.NonNull;

import io.reactivex.disposables.Disposable;

/**
 * @author ciba
 * @description 网络监听基础实现类
 * @date 2020/9/17
 */
public class HttpSimpleListener<T> implements IHttpListener<T> {
    @Override
    public void onStart(Disposable disposable) {

    }

    @Override
    public void onSuccess(@NonNull T response) {

    }

    @Override
    public void onFailed(int code, String message) {

    }
}
