package com.yxr.base.http.download;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.yxr.base.util.BitmapUtil;

public abstract class BitmapSubscriber extends DownloadSubscriber<Bitmap> {
    public BitmapSubscriber(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected Bitmap onDealBytes(@NonNull byte[] bytes) {
        return BitmapUtil.bytes2Bitmap(bytes);
    }
}
