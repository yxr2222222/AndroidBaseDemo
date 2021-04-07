package com.yxr.base.web;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

/**
 * @author : ciba
 * @description
 * @date : 2020/09/17
 */

public class NormalWebActivity extends BaseWebActivity {
    private BaseWebChromeClient chromeClient;
    private BaseWebViewClient webViewClient;

    public static void start(Context context, String url) {
        if (context != null && !TextUtils.isEmpty(url)) {
            Intent intent = new Intent(context, NormalWebActivity.class);
            intent.putExtra(BaseWebActivity.WEB_URL, url);
            context.startActivity(intent);
        }
    }

    @Override
    public BaseWebChromeClient getWebChromeClient() {
        if (chromeClient == null) {
            chromeClient = new BaseWebChromeClient((View) webView.getParent(), flFullVideoView, this);
            chromeClient.setOnToggledFullscreen(new BaseWebChromeClient.ToggledFullscreenCallback() {
                @Override
                public void toggledFullscreen(boolean fullscreen) {
                    if (fullscreen) {
                        getTitleBar().setVisibility(View.GONE);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        getTitleBar().setVisibility(View.VISIBLE);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }

                @Override
                public void getTitle(String title) {
                    if (title == null) {
                        title = "";
                    }
                    if (title.length() > 8) {
                        title = title.substring(0, 8);
                    }
                    showTitleBar(title);
                }
            });
        }
        return chromeClient;
    }

    @Override
    public BaseWebViewClient getWebViewClient() {
        if (webViewClient == null) {
            webViewClient = new BaseWebViewClient(this);
        }
        return webViewClient;
    }

    @Override
    public String getWebUrl() {
        return getIntent().getStringExtra(BaseWebActivity.WEB_URL);
    }
}
