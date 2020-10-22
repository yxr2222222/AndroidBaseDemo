package com.yxr.base.http;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ciba
 * @description 接口管理类
 * @date 2020/09/17
 */
public class HttpManager {
    private static final long TIME_OUT = 10;
    private static HttpManager instance;
    private Retrofit retrofit;

    private HttpManager() {
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化OkHttpClient,retrofit
     *
     * @param httpConfig 网络请求配置参数
     */
    public void init(@NonNull HttpConfig httpConfig) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (httpConfig.getInterceptor() != null) {
            builder.addInterceptor(httpConfig.getInterceptor());
        }

        long timeout = httpConfig.getTimeout() > 0 ? httpConfig.getTimeout() : TIME_OUT;
        OkHttpClient client = builder.connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(httpConfig.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 获取Retrofit对象
     *
     * @return Retrofit对象
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 发起网络请求
     *
     * @param observable
     * @param subscriber
     * @param <T>
     */
    public <T extends BaseResponse> void request(@NonNull Observable<Result<T>> observable, BaseSubscriber<T> subscriber) {
        Observable<Result<T>> ob = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        if (subscriber != null) {
            ob.subscribe(subscriber);
        } else {
            ob.subscribe();
        }
    }
}
