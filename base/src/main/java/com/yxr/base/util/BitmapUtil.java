package com.yxr.base.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

public class BitmapUtil {
    public static Bitmap view2Bitmap(@NonNull View v) {
        try {
            Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            c.drawColor(Color.WHITE);
            v.draw(c);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
