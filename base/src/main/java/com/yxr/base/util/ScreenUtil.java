package com.yxr.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Method;

/**
 * @author : ciba
 * @description 屏幕相关工具类
 * @date : 2020/09/17
 */

public class ScreenUtil {

    private static final String STATUS_BAR_BG = "STATUS_BAR_BG";
    private static int statusBarHeight;

    /**
     * 获取底部虚拟键盘的高度
     */
    public static int getBottomKeyboardHeight(Activity activity) {
        int screenHeight = getAccurateScreenDpi(activity)[1];
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return screenHeight - dm.heightPixels;
    }

    /**
     * 获取精确的屏幕大小
     */
    public static int[] getAccurateScreenDpi(Activity activity) {
        int[] screenWH = new int[2];
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWH;
    }

    public static int getStatusBarHeightWithTranslucentStatus(Context context) {
        if (statusBarHeight <= 0) {
            if (Build.VERSION.SDK_INT >= 19) {
                return getStatusBarHeight(context);
            } else {
                return 0;
            }
        } else {
            return statusBarHeight;
        }
    }

    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        if (context == null) {
            return 0;
        }
        statusBarHeight = (int) (context.getResources().getDisplayMetrics().density * 28);
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void translucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void immersiveSticky(boolean hasFocus, Activity activity) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public static void onConfigurationChanged(Activity activity, Configuration newConfig, View rlVideoLayout, boolean translucentStatusBar) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            // 设置全屏即隐藏状态栏
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ViewGroup.LayoutParams layoutParams = rlVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            rlVideoLayout.setLayoutParams(layoutParams);

            // 隐藏虚拟键
            if (Build.VERSION.SDK_INT < 19) {
                View view = activity.getWindow().getDecorView();
                view.setSystemUiVisibility(View.GONE);
            } else {
                View decorView = activity.getWindow().getDecorView();
                if (decorView.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams layoutParams = rlVideoLayout.getLayoutParams();
            layoutParams.width = activity.getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = layoutParams.width * 9 / 16;
            rlVideoLayout.setLayoutParams(layoutParams);
            if (translucentStatusBar) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                translucentStatusBar(activity);
            } else {
                //恢复状态栏
                WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                activity.getWindow().setAttributes(attrs);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                View view = activity.getWindow().getDecorView();
                // 显示虚拟键
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    public static void setTranslucentStatusBarBackground(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            int statusBarHeightWithTranslucentStatus = getStatusBarHeightWithTranslucentStatus(activity);
            View decorView = activity.getWindow().getDecorView();
            if (decorView instanceof FrameLayout) {
                FrameLayout parent = (FrameLayout) decorView;
                View statusBarBackgroundView = parent.findViewWithTag(STATUS_BAR_BG);
                if (statusBarBackgroundView == null) {
                    statusBarBackgroundView = new View(activity);
                    statusBarBackgroundView.setTag(STATUS_BAR_BG);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeightWithTranslucentStatus);
                    statusBarBackgroundView.setLayoutParams(params);
                    parent.addView(statusBarBackgroundView);
                }
                statusBarBackgroundView.setBackgroundColor(color);
            }
        }

    }

    public static void removeStatusBarView(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView instanceof FrameLayout) {
                FrameLayout parent = (FrameLayout) decorView;
                View statusBarBackgroundView = parent.findViewWithTag(STATUS_BAR_BG);
                if (statusBarBackgroundView != null) {
                    ((FrameLayout) decorView).removeView(statusBarBackgroundView);
                }
            }
        }

    }
}
