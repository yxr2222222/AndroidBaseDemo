package com.yxr.base.util;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * @author ciba
 * @description 描述
 * @date 2018/10/23
 */
public class ContextCompatUtil {
    public static int getColor(@NonNull Context context, @ColorRes int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }

    public static String getText(@NonNull Context context, @StringRes int stringRes) {
        return context.getText(stringRes).toString();
    }

    public static int getDimen(@NonNull Context context, @DimenRes int dimenRes) {
        return (int) context.getResources().getDimension(dimenRes);
    }
}
