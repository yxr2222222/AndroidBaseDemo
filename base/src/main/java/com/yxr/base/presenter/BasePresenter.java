package com.yxr.base.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.yxr.base.http.HttpManager;
import com.yxr.base.view.IBaseView;

/**
 * @author ciba
 * @description MVP中的Presenter层
 * @date 2020/9/17
 */
public class BasePresenter<T extends IBaseView> implements LifecycleObserver {
    private Lifecycle lifecycle;
    private T view;

    public BasePresenter(@NonNull Lifecycle lifecycle, @NonNull T view) {
        this.lifecycle = lifecycle;
        this.view = view;
        this.lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
            lifecycle = null;
        }
        view = null;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    /**
     * 获取MVP中的View
     *
     * @return View
     */
    public T getView() {
        return view;
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
