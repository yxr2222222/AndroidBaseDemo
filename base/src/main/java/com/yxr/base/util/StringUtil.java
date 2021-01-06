package com.yxr.base.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 手机号号段校验，
     第1位：1；
     第2位：{3、4、5、6、7、8}任意数字；
     第3—11位：0—9任意数字
     * @param value
     * @return
     */
    public static boolean isPhoneNumber(String value) {
        if (value != null && value.length() == 11) {
            Pattern pattern = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
        return false;
    }

    public static void setCustomKeyWordClickSpan(TextView tvContent, String text, KeyWordClick... keyWordClicks) {
        if (text == null || keyWordClicks == null || tvContent == null) {
            return;
        }
        SpannableString spannableString = new SpannableString(text);
        for (KeyWordClick keyWordClick : keyWordClicks) {
            if (keyWordClick.getKeyWord() == null) {
                continue;
            }
            Pattern p = Pattern.compile(keyWordClick.getKeyWord());
            Matcher m = p.matcher(spannableString);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                spannableString.setSpan(new CustomKeyWordClickSpan(keyWordClick.getHighLightColor(), keyWordClick.getClickListener()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tvContent.setClickable(true);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(spannableString);
    }

    public static class KeyWordClick {
        private final String mKeyWord;
        private final int mHighLightColor;
        private final View.OnClickListener mClickListener;

        public KeyWordClick(String mKeyWord, int mHighLightColor, View.OnClickListener mClickListener) {
            this.mKeyWord = mKeyWord;
            this.mHighLightColor = mHighLightColor;
            this.mClickListener = mClickListener;
        }

        String getKeyWord() {
            return mKeyWord;
        }

        int getHighLightColor() {
            return mHighLightColor;
        }

        View.OnClickListener getClickListener() {
            return mClickListener;
        }
    }

    private static class CustomKeyWordClickSpan extends ClickableSpan {
        private final int mHighLightColor;
        private boolean mUnderLine = false;
        private View.OnClickListener mClickListener;

        CustomKeyWordClickSpan(@ColorInt int highLightColor
                , @NonNull View.OnClickListener listener) {
            this.mHighLightColor = highLightColor;
            this.mClickListener = listener;
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (mClickListener != null) {
                mClickListener.onClick(widget);
            }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(mHighLightColor);
            ds.setUnderlineText(mUnderLine);
        }
    }
}
