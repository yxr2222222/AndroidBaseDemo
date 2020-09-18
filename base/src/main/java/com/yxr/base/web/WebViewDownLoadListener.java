package com.yxr.base.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

import java.lang.ref.WeakReference;

/**
 * @author : ciba
 * @description WebViewDownLoadListener
 * @date : 2020/09/17
 */

public class WebViewDownLoadListener implements DownloadListener {
    private WeakReference<Context> contextWeakReference;

    public WebViewDownLoadListener(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        if (contextWeakReference != null && contextWeakReference.get() != null) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                contextWeakReference.get().startActivity(intent);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
