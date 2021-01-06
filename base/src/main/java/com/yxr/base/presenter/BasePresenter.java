package com.yxr.base.presenter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.annotation.NonNull;

import com.yxr.base.http.HttpHelper;
import com.yxr.base.view.IBaseView;

/**
 * @author ciba
 * @description MVP中的Presenter层
 * @date 2020/9/17
 */
public class BasePresenter<T extends IBaseView> extends HttpHelper implements LifecycleObserver {
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

}
