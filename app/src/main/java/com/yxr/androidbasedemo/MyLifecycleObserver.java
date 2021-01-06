package com.yxr.androidbasedemo;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.util.Log;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
public class MyLifecycleObserver implements LifecycleObserver {
    private static final String TAG = "TTTTTTAG";
    private final Lifecycle lifecycle;

    public MyLifecycleObserver(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        Log.e(TAG, "resume::::::::: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void release() {
        Log.e(TAG, "release::::::::: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void any() {
        Lifecycle.State currentState = lifecycle.getCurrentState();
        Log.e(TAG, "currentState : " + currentState.name());
    }
}
