package com.yxr.base.util;

import android.annotation.SuppressLint;

public class NumUtil {
    private static final float K = 1000;
    private static final float W = 10000;

    @SuppressLint("DefaultLocale")
    public static String getFormatNum(long num) {
        if (num > W) {
            return String.format("%.1f%s", num / W, "W");
        } else if (num > K) {
            return String.format("%.1f%s", num / K, "K");
        } else {
            return String.valueOf(num);
        }
    }
}
