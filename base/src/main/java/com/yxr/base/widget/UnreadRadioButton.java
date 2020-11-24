package com.yxr.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.yxr.base.util.DisplayUtil;

public class UnreadRadioButton extends RadioButton {
    private Paint mPaint;
    private boolean isShowNumberDot;
    private String numberText;
    private int circleDotRadius;
    private int dotPaintColor = 0xffcc0000;
    private int textPaintColor = 0xffffffff;
    private int offX = 0;
    private int offY = 0;
    private RectF rectF = new RectF();

    public UnreadRadioButton(Context context) {
        super(context);
        init();
    }

    public UnreadRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnreadRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circleDotRadius = DisplayUtil.dp2px(getContext(), 15);
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(dotPaintColor);
        mPaint.setTextSize(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowNumberDot) {
            int textWidth = (int) mPaint.measureText(numberText);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int textHeight = (int) Math.abs((fontMetrics.top + fontMetrics.bottom));

            int startX = getWidth() - getPaddingRight() - textWidth - circleDotRadius - offX;
            int startY = getPaddingTop() + offY;
            rectF.set(startX, startY, startX + circleDotRadius, startY + circleDotRadius);
            mPaint.setColor(dotPaintColor);
            canvas.drawRoundRect(rectF, circleDotRadius, circleDotRadius, mPaint);

            int textStartX = startX + (int) (Math.max(0, (rectF.right - rectF.left - textWidth) / 2));
            int textStartY = startY + (int) (Math.max(0, (rectF.bottom - rectF.top + textHeight) / 2));
            mPaint.setColor(textPaintColor);
            canvas.drawText(numberText, textStartX, textStartY, mPaint);
        }
    }

    public void setOffX(int offX) {
        this.offX = offX;
    }

    public void setOffY(int offY) {
        this.offY = offY;
    }

    public void setCircleDotRadius(int circleDotRadius) {
        this.circleDotRadius = circleDotRadius;
    }

    public void setDotPaintColor(int dotPaintColor) {
        this.dotPaintColor = dotPaintColor;
    }

    public void setTextPaintColor(int textPaintColor) {
        this.textPaintColor = textPaintColor;
    }

    /**
     * 设置是否显示数字
     */
    public void setNumberDot(boolean isShowNumberDot, int number) {
        this.isShowNumberDot = isShowNumberDot;
        this.numberText = number > 99 ? "···" : String.valueOf(number);
        invalidate();
    }
}

