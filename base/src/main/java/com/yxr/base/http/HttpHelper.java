package com.yxr.base.http;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;

public class HttpHelper {
    /**
     * 获取网络请求服务
     *
     * @return 网络请求服务
     */
    public <E> E getService(@NonNull Class<E> cls) {
        return HttpManager.getInstance().getRetrofit().create(cls);
    }

    public <T extends BaseResponse> void request(@NonNull Observable<Result<T>> observable, BaseSubscriber<T> subscriber) {
        HttpManager.getInstance().request(observable, subscriber);
    }
}
