package com.yxr.base.http;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

/**
 * @author ciba
 * @description 接口回答基类
 * @date 2020/09/17
 */
public class BaseSubscriber<T extends BaseResponse> implements Observer<Result<T>>, LifecycleObserver {
    private Lifecycle lifecycle;
    private IHttpListener<T> httpListener;
    private Disposable disposable;

    public BaseSubscriber(@NonNull Lifecycle lifecycle) {
        this(lifecycle, null);
    }

    public BaseSubscriber(@NonNull Lifecycle lifecycle, IHttpListener<T> httpListener) {
        this.lifecycle = lifecycle;
        this.httpListener = httpListener;
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        this.disposable = disposable;
        if (httpListener != null && !disposable.isDisposed()) {
            httpListener.onStart(disposable);
        }
    }

    @Override
    public void onNext(Result<T> result) {
        Response<T> response = result.response();
        if (response == null) {
            Throwable error = result.error();
            if (checkNetworkNotError(error)) {
                onFailed(HttpErrorCode.CODE_NULL_RESPONSE, HttpErrorCode.MESSAGE_NULL_RESPONSE);
            }
        } else if (!response.isSuccessful()) {
            String message = response.message();
            onFailed(response.code(), TextUtils.isEmpty(message) ? "Failed! Code is " + response.code() : message);
        } else if (response.body() == null) {
            onFailed(HttpErrorCode.CODE_NULL_BODY, HttpErrorCode.MESSAGE_NULL_BODY);
        } else if (httpListener != null) {
            httpListener.onSuccess(response.body());
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e == null || checkNetworkNotError(e)) {
            onFailed(HttpErrorCode.CODE_UNKNOW, HttpErrorCode.MESSAGE_UNKNOW);
        }
    }

    @Override
    public void onComplete() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (httpListener != null){
            httpListener.onDestroy();
            httpListener = null;
        }

        try {
            if (lifecycle != null) {
                lifecycle.removeObserver(this);
                lifecycle = null;
            }
            if (disposable != null) {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
                disposable = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否是网络异常
     *
     * @param error 访问网络错误
     */
    protected boolean checkNetworkNotError(Throwable error) {
        if (error instanceof HttpException || error instanceof ConnectException) {
            onFailed(HttpErrorCode.CODE_CONNECT_EXCEPTION, HttpErrorCode.MESSAGE_CONNECT_EXCEPTION);
            return false;
        } else if (error instanceof SocketTimeoutException) {
            // 连接超时或没有网络连接
            onFailed(HttpErrorCode.CODE_CONNECT_EXCEPTION, HttpErrorCode.MESSAGE_CONNECT_TIME_OUT);
            return false;
        } else if (error instanceof UnknownHostException) {
            // 没有网络连接
            onFailed(HttpErrorCode.CODE_CONNECT_EXCEPTION, HttpErrorCode.MESSAGE_CONNECT_NO);
            return false;
        }
        return true;
    }

    /**
     * 网络请求失败
     *
     * @param code     错误码
     * @param errorMsg 错误信息
     */
    protected void onFailed(int code, String errorMsg) {
        if (httpListener != null) {
            httpListener.onFailed(code, errorMsg);
        }
    }

    public IHttpListener<T> getHttpListener() {
        return httpListener;
    }
}
