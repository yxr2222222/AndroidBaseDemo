package com.yxr.base.http.download;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.yxr.base.util.FileUtil;

import java.io.File;

public abstract class FileSubscriber extends DownloadSubscriber<File> {
    private final String filePath;

    public FileSubscriber(Lifecycle lifecycle, @NonNull String filePath) {
        super(lifecycle);
        this.filePath = filePath;
    }

    @Override
    protected File onDealBytes(@NonNull byte[] bytes) {
        boolean success = FileUtil.saveContentToFile(filePath, bytes);
        return success ? new File(filePath) : null;
    }
}
