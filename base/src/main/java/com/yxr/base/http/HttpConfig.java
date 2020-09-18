package com.yxr.base.http;

import android.support.annotation.NonNull;

import okhttp3.Interceptor;

/**
 * @author ciba
 * @description 网络请求配置信息
 * @date 2020/9/17
 */
public class HttpConfig {
    private long timeout;
    private String baseUrl;
    private Interceptor interceptor;

    private HttpConfig() {
    }

    public long getTimeout() {
        return timeout;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public static class Builder {
        private HttpConfig httpConfig = new HttpConfig();

        /**
         * 设置超时时长，单位秒
         *
         * @param timeout 超时时长
         */
        public Builder timeout(long timeout) {
            httpConfig.timeout = timeout;
            return this;
        }

        /**
         * 设置网络请求BaseUrl
         */
        public Builder baseUrl(@NonNull String baseUrl) {
            httpConfig.baseUrl = baseUrl;
            return this;
        }

        /**
         * 设置网络请求拦截器
         *
         * @param interceptor 拦截器
         */
        public Builder interceptor(Interceptor interceptor) {
            httpConfig.interceptor = interceptor;
            return this;
        }

        public HttpConfig build() {
            return httpConfig;
        }
    }
}
