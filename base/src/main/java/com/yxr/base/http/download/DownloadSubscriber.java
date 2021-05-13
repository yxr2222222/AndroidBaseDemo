package com.yxr.base.http.download;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.yxr.base.http.HttpErrorCode;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class DownloadSubscriber<T> implements Observer<ResponseBody>, LifecycleObserver {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Disposable disposable;
    private Lifecycle lifecycle;

    public DownloadSubscriber(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        if (this.lifecycle != null) {
            this.lifecycle.addObserver(this);
        }
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void onNext(ResponseBody response) {
        if (disposable != null) {
            if (response == null) {
                innerFailed(HttpErrorCode.CODE_NULL_RESPONSE, HttpErrorCode.MESSAGE_NULL_RESPONSE);
            } else {
                byte[] bytes = null;
                try {
                    bytes = response.bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bytes == null || bytes.length <= 0) {
                    innerFailed(-1, "bytes is empty");
                } else {
                    innerSuccess(onDealBytes(bytes));
                }
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e == null || checkNetworkNotError(e)) {
            innerFailed(HttpErrorCode.CODE_UNKNOW, HttpErrorCode.MESSAGE_UNKNOW);
        }
    }

    @Override
    public void onComplete() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        try {
            if (lifecycle != null) {
                lifecycle.removeObserver(this);
                lifecycle = null;
            }
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
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
            innerFailed(HttpErrorCode.CODE_CONNECT_EXCEPTION, HttpErrorCode.MESSAGE_CONNECT_EXCEPTION);
            return false;
        } else if (error instanceof SocketTimeoutException) {
            // 连接超时或没有网络连接
            innerFailed(HttpErrorCode.CODE_CONNECT_EXCEPTION, HttpErrorCode.MESSAGE_CONNECT_TIME_OUT);
            return false;
        } else if (error instanceof UnknownHostException) {
            // 没有网络连接
            innerFailed(HttpErrorCode.CODE_CONNECT_EXCEPTION, HttpErrorCode.MESSAGE_CONNECT_NO);
            return false;
        }
        return true;
    }

    private void innerSuccess(final T data) {
        if (data == null) {
            innerFailed(-1, "bytes转数据失败");
        } else {
            if (handler != null) {
                handler.post(() -> onSuccess(data));
            }
        }
    }

    private void innerFailed(final int code, final String errorMessage) {
        if (handler != null) {
            handler.post(() -> onFailed(code, errorMessage));
        }
    }

    /**
     * 在子线程处理数据
     */
    protected abstract T onDealBytes(@NonNull byte[] bytes);

    /**
     * 网络请求成功
     *
     * @param body Response数据
     */
    protected abstract void onSuccess(@NonNull T body);

    /**
     * 网络请求失败
     *
     * @param code     错误码
     * @param errorMsg 错误信息
     */
    protected abstract void onFailed(int code, String errorMsg);
}
