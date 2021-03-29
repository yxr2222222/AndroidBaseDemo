package com.yxr.base.http;

import androidx.annotation.NonNull;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager x509TrustManager;
    private HostnameVerifier hostnameVerifier;

    private HttpConfig(SSLSocketFactory sslSocketFactory
            , X509TrustManager x509TrustManager
            , HostnameVerifier hostnameVerifier) {
        this.sslSocketFactory = sslSocketFactory;
        this.x509TrustManager = x509TrustManager;
        this.hostnameVerifier = hostnameVerifier;
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

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager getX509TrustManager() {
        return x509TrustManager;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public static class Builder {
        private HttpConfig httpConfig = new HttpConfig(createSSLSocketFactory(), new TrustAllCerts(), new TrustAllHostnameVerifier());

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

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            httpConfig.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder x509TrustManager(X509TrustManager x509TrustManager) {
            httpConfig.x509TrustManager = x509TrustManager;
            return this;
        }

        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            httpConfig.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public HttpConfig build() {
            return httpConfig;
        }

        private static class TrustAllCerts implements X509TrustManager {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }

        private static class TrustAllHostnameVerifier implements HostnameVerifier {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }

        private static SSLSocketFactory createSSLSocketFactory() {
            SSLSocketFactory ssfFactory = null;
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
                ssfFactory = sc.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ssfFactory;
        }
    }
}
