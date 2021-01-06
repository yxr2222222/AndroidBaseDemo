package com.yxr.base.web;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.yxr.base.R;
import com.yxr.base.activity.BaseStatusActivity;
import com.yxr.base.util.WebUtil;

/**
 * @author : ciba
 * @description BaseWebActivity
 * @date : 2020/09/17
 */

public abstract class BaseWebActivity extends BaseStatusActivity {
    public WebView webView;
    public FrameLayout flFullVideoView;
    public static final String WEB_URL = "WEB_URL";

    @Override
    public int contentView() {
        return R.layout.activity_base_web;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        webView = findViewById(R.id.webView);
        flFullVideoView = findViewById(R.id.flFullVideoView);
    }

    @Override
    public void initData() {
        showTitleBar("");
        WebUtil.initWebView(webView, getWebChromeClient(), getWebViewClient(), new WebViewDownLoadListener(this));
        loadUrl();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getWebChromeClient() != null) {
            getWebChromeClient().onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (flFullVideoView != null) {
            flFullVideoView.removeAllViews();
        }
        WebUtil.destroyWebView(webView);
        super.onDestroy();
    }

    /**
     * 设置WebChromeClient
     *
     * @return ：BaseWebChromeClient
     */
    protected abstract BaseWebChromeClient getWebChromeClient();

    /**
     * 设置WebChromeClient
     *
     * @return ：BaseWebChromeClient
     */
    protected abstract BaseWebViewClient getWebViewClient();

    /**
     * 设置需要加载的地址
     *
     * @return ：地址
     */
    protected abstract String getWebUrl();

    protected void loadUrl() {
        webView.loadUrl(getWebUrl());
    }
}
