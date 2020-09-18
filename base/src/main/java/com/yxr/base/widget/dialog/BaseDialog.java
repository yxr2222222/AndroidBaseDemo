package com.yxr.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import com.yxr.base.R;


/**
 * @author ciba
 * @description 描述
 * @date 2020/09/17
 */
public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context, R.style.common_dialog);
        setContentView(contentView());
        initView();
        initListener();
        initData();
        setDialogWindow();
    }

    protected abstract int contentView();

    protected void initView() {

    }

    protected void initListener() {

    }

    protected void initData() {

    }

    /**
     * set dialog data with anim and other more
     */
    protected void setDialogWindow() {
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.fromBottomToTopAnimStyle);
            window.setGravity(Gravity.BOTTOM);
            ViewGroup.LayoutParams lay = window.getAttributes();
            lay.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }
}
