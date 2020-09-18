package com.yxr.androidbasedemo;

import android.support.annotation.NonNull;

import com.yxr.base.BaseApplication;
import com.yxr.base.http.HttpConfig;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
public class MyApp extends BaseApplication {
    @NonNull
    @Override
    protected HttpConfig getHttpConfig() {
        return new HttpConfig.Builder()
                .baseUrl("http://v.juhe.cn/")
                .timeout(10)
                .build();
    }
}
