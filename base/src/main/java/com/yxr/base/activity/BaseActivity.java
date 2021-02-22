package com.yxr.base.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.yxr.base.R;
import com.yxr.base.inf.IBaseImmersion;
import com.yxr.base.util.ContextCompatUtil;
import com.yxr.base.util.ToastUtil;
import com.yxr.base.view.IBaseUiView;
import com.yxr.base.widget.dialog.DefaultLoadingDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity基类
 *
 * @author ciba
 * @date 2020/09/17
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseUiView, IBaseImmersion {
    private DefaultLoadingDialog loadingDialog;
    private View contentView;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersion();
        contentView = LayoutInflater.from(this).inflate(contentView(), null, false);
        setContentView(contentView);

        // bindView()
        bindView();

        // 初始化View
        initView(savedInstanceState);
        // 初始化事件监听
        initListener();
        // 初始化数据
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager manager = getSupportFragmentManager();
        List<Fragment> fragments = manager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        loadingDialog = null;
        super.onDestroy();
        unbindView();
    }

    @Override
    public void toast(@StringRes int stringRes) {
        toast(ContextCompatUtil.getText(getApplicationContext(), stringRes));
    }

    @Override
    public void toast(String message) {
        ToastUtil.show(getApplicationContext(), message);
    }

    /**
     * 展示弹框类型的loading
     */
    @Override
    public void showLoadingDialog() {
        if (!isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog = createLoadingDialog();
                if (loadingDialog == null) {
                    loadingDialog = new DefaultLoadingDialog(this);
                    loadingDialog.setCanceledOnTouchOutside(false);
                }
            }
            loadingDialog.show();
        }
    }

    /**
     * 隐藏弹框loading
     */
    @Override
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public boolean needImmersion() {
        return true;
    }

    @Override
    public boolean statusBarDarkFont() {
        return true;
    }

    @Override
    public int statusBarColor() {
        return R.color.colorPrimaryDark;
    }

    @Override
    public boolean fitsSystemWindows() {
        return true;
    }

    /**
     * 初始化沉浸式相关
     */
    protected void initImmersion() {
        if (needImmersion()) {
            ImmersionBar.with(this)
                    .statusBarColor(statusBarColor())
                    .statusBarDarkFont(statusBarDarkFont())
                    .fitsSystemWindows(fitsSystemWindows())
                    .init();
        }
    }

    /**
     * 获取内容视图
     *
     * @return 内容视图
     */
    public View getContentView() {
        return contentView;
    }

    /**
     * 初始化视图
     */
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 初始化监听
     */
    protected void initListener() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 绑定视图
     */
    private void bindView() {
        if (getContentView() != null) {
            unbinder = ButterKnife.bind(this, getContentView());
        }
    }

    /**
     * 绑定视图
     */
    private void unbindView() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    /**
     * 自定义Loading的Dialog
     *
     * @return Loading的Dialog
     */
    protected DefaultLoadingDialog createLoadingDialog() {
        return null;
    }

    /**
     * 获取内容视图的LayoutRes
     *
     * @return 内容视图LayoutRes
     */
    @LayoutRes
    protected abstract int contentView();
}
