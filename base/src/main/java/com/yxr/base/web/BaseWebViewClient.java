package com.yxr.base.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.support.annotation.NonNull;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : ciba
 * @description BaseWebViewClient
 * @date : 2020/09/17
 */

public class BaseWebViewClient extends WebViewClient {
    private BaseWebActivity activity;

    public BaseWebViewClient(@NonNull BaseWebActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {
        if (schemeMatch(url) && activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.showLoading();
                }
            });
        }
    }


    @Override
    public void onPageFinished(final WebView view, String url) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.showContent();
                }
            });
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        super.onFormResubmission(view, dontResend, resend);
        resend.sendToTarget();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (view.canGoBack()) {
            view.goBack();
        } else if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.showContent();
                }
            });
        }
    }

    private boolean schemeMatch(String url) {
        try {
            Pattern p = Pattern.compile("^(?!http)[0-9a-zA-Z]{2,}:\\/\\/[\\d\\D]*");
            //通过模式对象创建一个匹配对象
            Matcher m1 = p.matcher(url);
            //尝试将整个区域与模式匹配。当且仅当整个区域序列匹配此匹配器的模式时才返回 true。
            return m1.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
