package com.yxr.base.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.yxr.base.http.HttpManager;
import com.yxr.base.presenter.BasePresenter;

/**
 * @author ciba
 * @description MVP中Model层基类
 * @date 2020/9/17
 */
public class BaseModel<T extends BasePresenter> implements LifecycleObserver {
    private Lifecycle lifecycle;
    private T presenter;

    public BaseModel(@NonNull Lifecycle lifecycle, T presenter) {
        this.lifecycle = lifecycle;
        this.presenter = presenter;
        this.lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        getLifecycle().removeObserver(this);
    }

    @NonNull
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    /**
     * 获取MVP中的Presenter
     *
     * @return Presenter
     */
    public T getPresenter() {
        return presenter;
    }

    /**
     * 获取网络请求服务
     *
     * @return 网络请求服务
     */
    protected <E> E getService(@NonNull Class<E> cls) {
        return HttpManager.getInstance().getRetrofit().create(cls);
    }
}
