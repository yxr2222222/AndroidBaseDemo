package com.yxr.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.yxr.base.http.HttpConfig;
import com.yxr.base.http.HttpManager;

/**
 * @author ciba
 * @description Application基类
 * @date 2020/9/17
 */
public abstract class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HttpManager.getInstance().init(getHttpConfig());
    }

    /**
     * 获取网络配置信息
     *
     * @return 网络配置信息
     */
    @NonNull
    protected abstract HttpConfig getHttpConfig();
}
