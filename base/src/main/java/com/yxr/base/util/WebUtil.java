package com.yxr.base.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yxr.base.web.BaseWebChromeClient;
import com.yxr.base.web.BaseWebViewClient;
import com.yxr.base.web.WebViewDownLoadListener;

/**
 * @author ciba
 * @description 描述
 * @date 2020/09/17
 */
public class WebUtil {

    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebView(WebView webView
            , BaseWebChromeClient webChromeClient
            , BaseWebViewClient webViewClient
            , WebViewDownLoadListener downLoadListener) {
        try {
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            //设置 缓存模式
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

            if (downLoadListener != null) {
                webView.setDownloadListener(downLoadListener);
            }
            // 是否支持viewport属性，默认值 false
            // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            // 是否自动加载图片
            settings.setLoadsImagesAutomatically(true);
            // 开启 DOM storage API 功能
            settings.setDomStorageEnabled(true);
            settings.setDefaultTextEncodingName("UTF-8");
            //启用数据库
            settings.setDatabaseEnabled(true);
            settings.setGeolocationEnabled(true);
            webView.clearHistory();
            webView.clearFormData();
            webView.clearCache(true);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            cookieManager.removeAllCookie();

            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setSupportMultipleWindows(true);

            // android 5.0以上默认不支持Mixed Content
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(
                        WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 是否在离开屏幕时光栅化(会增加内存消耗)，默认值 false
                settings.setOffscreenPreRaster(false);
            }
            if (webChromeClient != null) {
                webView.setWebChromeClient(webChromeClient);
            }
            if (webViewClient != null) {
                webView.setWebViewClient(webViewClient);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * WebView释放
     *
     * @param webView
     */
    public static void destroyWebView(WebView webView) {
        if (webView != null) {
            try {
                ViewGroup parent = (ViewGroup) webView.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                webView.setVisibility(View.GONE);
                webView.stopLoading();
                webView.setWebChromeClient(null);
                webView.setWebViewClient(null);
                webView.clearView();
                webView.removeAllViews();
                webView.destroy();
                webView = null;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
