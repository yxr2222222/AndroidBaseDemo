package com.yxr.base;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.yxr.base.widget.guide.Component;
import com.yxr.base.widget.guide.GuideBuilder;

public class BaseGuide implements GuideBuilder.OnVisibilityChangedListener {
    @NonNull
    private final Activity activity;

    public BaseGuide(@NonNull Activity activity) {
        this.activity = activity;
    }

    public void showGuide(View targetView) {
        if (targetView != null) {
            GuideBuilder builder = new GuideBuilder();
            builder = builder.setTargetView(targetView)
                    .setAlpha(getAlpha())
                    .setOutsideTouchable(getOutsideTouchable())
                    .setHighTargetCorner(getHeightTargetCorner())
                    .setHighTargetPadding(getHeightTargetPadding())
                    .setOnVisibilityChangedListener(this);
            Component component = getComponent();
            if (component != null) {
                builder = builder.addComponent(component);
            }
            builder.createGuide()
                    .show(activity);
        }
    }

    @Override
    public void onShown() {

    }

    @Override
    public void onDismiss() {

    }

    protected boolean getOutsideTouchable() {
        return false;
    }

    protected Component getComponent() {
        return null;
    }

    protected int getHeightTargetPadding() {
        return 0;
    }

    protected int getHeightTargetCorner() {
        return 20;
    }

    protected int getAlpha() {
        return 150;
    }
}
