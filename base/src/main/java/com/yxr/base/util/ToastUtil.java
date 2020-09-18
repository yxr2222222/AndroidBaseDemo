package com.yxr.base.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author ciba
 * @description 安全的Toast工具类
 * @date 2020/09/17
 */
public class ToastUtil {
    public static void show(Context context, String message) {
        if (context != null && !TextUtils.isEmpty(message)) {
            try {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                    SafeToastUtil.show(context, message);
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
