package com.yxr.base.web;

import android.content.ClipData;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * @author : ciba
 * @description replace your description
 * @date : 2020/09/17
 */

public class BaseWebChromeClient extends WebChromeClient implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private ProgressBar loadingView;
    private View mWebViewContainer;
    private ViewGroup mFullScreenContainer;
    private boolean isVideoFullscreen;
    private FrameLayout videoViewContainer;
    private CustomViewCallback videoViewCallback;
    private ToggledFullscreenCallback toggledFullscreenCallback;

    private static final int REQUEST_UPLOAD_FILE_CODE = 12343;
    private static final int LOAD_FINISH = 100;
    private static final int RESULT_OK = -1;
    private ValueCallback<Uri> mUploadFile;
    private ValueCallback<Uri[]> mFilePathCallback;
    private BaseWebActivity activity;

    public BaseWebChromeClient(BaseWebActivity activity) {
        this(null, null, activity);
    }

    /**
     * 创建一个video客户端
     *
     * @param webViewContainer    视频播放页面的容器
     * @param fullScreenContainer 全屏播放容器对象
     * @param activity            持有对象，用于创建进度条
     */
    public BaseWebChromeClient(View webViewContainer, ViewGroup fullScreenContainer, BaseWebActivity activity) {
        this.mWebViewContainer = webViewContainer;
        this.mFullScreenContainer = fullScreenContainer;
        this.isVideoFullscreen = false;
        this.loadingView = new ProgressBar(activity);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    /**
     * Android 4.1+
     */
    public final void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        openFileChooser(uploadFile);
    }

    /**
     * Android 3.0 +
     */
    public final void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
        openFileChooser(uploadFile);
    }

    /**
     * Android 3.0
     */
    public void openFileChooser(ValueCallback<Uri> uploadFile) {
        // 上传文件/图片"
        if (activity != null) {
            try {
                mUploadFile = uploadFile;
                activity.startActivityForResult(Intent.createChooser(createCameraIntent(), "Image Browser"), REQUEST_UPLOAD_FILE_CODE);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Android 5.0+
     */
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (activity != null) {
            mFilePathCallback = filePathCallback;
            try {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                activity.startActivityForResult(Intent.createChooser(i, "File Browser"), REQUEST_UPLOAD_FILE_CODE);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
        // 载入进度改变而触发
        if (progress == LOAD_FINISH) {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.showContent();
                    }
                });
            }
        }
        super.onProgressChanged(view, progress);
    }

    private Intent createCameraIntent() {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        return imageIntent;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPLOAD_FILE_CODE && resultCode == RESULT_OK) {
            try {
                Uri result = (null == data) ? null : data.getData();
                if (mFilePathCallback != null) {
                    Uri[] results = null;
                    if (data != null) {
                        String dataString = data.getDataString();
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            results = new Uri[clipData.getItemCount()];
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                results[i] = item.getUri();
                            }
                        }
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                    mFilePathCallback.onReceiveValue(results);
                    mFilePathCallback = null;
                } else if (mUploadFile != null) {
                    mUploadFile.onReceiveValue(result);
                    mUploadFile = null;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     *
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    public boolean isVideoFullscreen() {
        return isVideoFullscreen;
    }

    /**
     * Set a callback that will be fired when the video starts or finishes displaying using a custom view (typically full-screen)
     *
     * @param callback A VideoEnabledWebChromeClient.ToggledFullscreenCallback callback
     */
    public void setOnToggledFullscreen(ToggledFullscreenCallback callback) {
        this.toggledFullscreenCallback = callback;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (mWebViewContainer == null || mFullScreenContainer == null) {
            return;
        }
        if (view instanceof FrameLayout) {
            // A video wants to be shown
            FrameLayout frameLayout = (FrameLayout) view;
            View focusedChild = frameLayout.getFocusedChild();

            // Save video related variables
            this.isVideoFullscreen = true;
            this.videoViewContainer = frameLayout;
            this.videoViewCallback = callback;
            // Hide the non-video view, add the video view, and show it
            mWebViewContainer.setVisibility(View.INVISIBLE);

            mFullScreenContainer.addView(videoViewContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenContainer.setVisibility(View.VISIBLE);

            if (focusedChild instanceof android.widget.VideoView) {
                // android.widget.VideoView (typically API level <11)
                android.widget.VideoView videoView = (android.widget.VideoView) focusedChild;

                // Handle all the required events
                videoView.setOnPreparedListener(this);
                videoView.setOnCompletionListener(this);
                videoView.setOnErrorListener(this);
            }

            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback.toggledFullscreen(true);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        if (mWebViewContainer == null || mFullScreenContainer == null) {
            return;
        }
        // This method should be manually called on video end in all cases because it's not always called automatically.
        // This method must be manually called on back key press (from this class' onBackPressed() method).
        if (isVideoFullscreen) {
            // Hide the video view, remove it, and show the non-video view
            mFullScreenContainer.setVisibility(View.INVISIBLE);
            mFullScreenContainer.removeView(videoViewContainer);
            mWebViewContainer.setVisibility(View.VISIBLE);

            // 全屏返回之后，视频状态不能衔接上，因为onCustomViewHidden很多情况下会奔溃
            //原因： Call back (only in API level <19, because in API level 19+ with chromium webview it crashes)
            if (videoViewCallback != null && !videoViewCallback.getClass().getName().contains(".chromium.")) {
                videoViewCallback.onCustomViewHidden();
            }
            isVideoFullscreen = false;
            videoViewContainer = null;
            videoViewCallback = null;

            // Notify full-screen change
            if (toggledFullscreenCallback != null) {
                toggledFullscreenCallback.toggledFullscreen(false);
            }
        }
    }

    @Override
    public View getVideoLoadingProgressView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            return loadingView;
        } else {
            return super.getVideoLoadingProgressView();
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (toggledFullscreenCallback != null) {
            toggledFullscreenCallback.getTitle(title);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        onHideCustomView();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    /**
     * Notifies the class that the back key has been pressed by the user.
     * This must be called from the Activity's onBackPressed(), and if it returns false, the activity itself should handle it. Otherwise don't do anything.
     *
     * @return Returns true if the event was handled, and false if was not (video view is not visible)
     */
    @SuppressWarnings("unused")
    public boolean onBackPressed() {
        if (isVideoFullscreen) {
            onHideCustomView();
            return true;
        } else {
            return false;
        }
    }

    public interface ToggledFullscreenCallback {
        void toggledFullscreen(boolean fullscreen);

        void getTitle(String title);
    }
}
