package com.yxr.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SchmeUtil {
    /**
     * 跳转到市场详情页，引导用户评论打分
     */
    public static void jumpMarketComments(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + PackageUtil.getPackageName(context));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
