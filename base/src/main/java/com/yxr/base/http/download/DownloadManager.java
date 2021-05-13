package com.yxr.base.http.download;

import androidx.annotation.NonNull;

import com.yxr.base.http.HttpManager;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class DownloadManager {
    private static DownloadManager instance;
    private final DownloadService downloadService;

    private DownloadManager() {
        downloadService = HttpManager.getInstance().getRetrofit().create(DownloadService.class);
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    public <T> void download(@NonNull String url, @NonNull DownloadSubscriber<T> subscriber) {
        downloadService.download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public interface DownloadService {
        @GET
        @Streaming
        Observable<ResponseBody> download(@Url String imgUrl);
    }
}
