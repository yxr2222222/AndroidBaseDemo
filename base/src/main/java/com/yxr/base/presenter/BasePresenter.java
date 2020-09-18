package com.yxr.base.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

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
        getLifecycle().removeObserver(this);
    }

    @NonNull
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    /**
     * 获取MVP中的View
     *
     * @return View
     */
    @NonNull
    public T getView() {
        return view;
    }
}
