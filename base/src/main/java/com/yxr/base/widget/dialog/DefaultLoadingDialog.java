package com.yxr.base.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import com.yxr.base.R;
import com.yxr.base.util.ContextCompatUtil;


/**
 * loading弹框
 *
 * @author ciba
 * @date 2020/09/17
 */

public class DefaultLoadingDialog extends BaseDialog {

    public DefaultLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected int contentView() {
        return R.layout.layout_default_loading;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setDialogWindow() {
        Window window = getWindow();
        if (window != null) {
            int dimen72 = ContextCompatUtil.getDimen(getContext(), R.dimen.dp72);
            window.setWindowAnimations(R.style.dialogAlphaAnimal);
            window.setDimAmount(0);
            window.setGravity(Gravity.CENTER);

            ViewGroup.LayoutParams lay = window.getAttributes();
            lay.width = dimen72;
            lay.height = dimen72;
        }
    }
}
