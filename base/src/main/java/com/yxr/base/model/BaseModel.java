package com.yxr.base.model;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.annotation.NonNull;

import com.yxr.base.http.HttpHelper;
import com.yxr.base.presenter.BasePresenter;

/**
 * @author ciba
 * @description MVP中Model层基类
 * @date 2020/9/17
 */
public class BaseModel<T extends BasePresenter> extends HttpHelper implements LifecycleObserver {
    private Lifecycle lifecycle;
    private T presenter;

    public BaseModel(@NonNull Lifecycle lifecycle, T presenter) {
        this.lifecycle = lifecycle;
        this.presenter = presenter;
        this.lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (lifecycle != null) {
            lifecycle.removeObserver(this);
            lifecycle = null;
        }
        presenter = null;
    }

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

}
