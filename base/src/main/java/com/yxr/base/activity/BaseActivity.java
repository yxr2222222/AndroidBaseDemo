package com.yxr.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.yxr.base.util.ContextCompatUtil;
import com.yxr.base.util.ToastUtil;
import com.yxr.base.view.IBaseUiView;
import com.yxr.base.widget.UIStatus;
import com.yxr.base.widget.dialog.DefaultLoadingDialog;

import java.util.List;

/**
 * Activity基类
 *
 * @author ciba
 * @date 2020/09/17
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseUiView {
    private DefaultLoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutInflater.from(this).inflate(contentView(), null, false));
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
        if (manager != null) {
            List<Fragment> fragments = manager.getFragments();
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        loadingDialog = null;
        super.onDestroy();
    }

    @Override
    public void toast(@StringRes int stringRes) {
        toast(ContextCompatUtil.getText(getApplicationContext(), stringRes));
    }

    @Override
    public void toast(String message) {
        ToastUtil.show(getApplicationContext(), message);
    }

    @Override
    public void showLoading() {
        changUiStatus(UIStatus.LOADING);
    }

    @Override
    public void showContent() {
        changUiStatus(UIStatus.CONTENT);
    }

    @Override
    public void showEmpty() {
        changUiStatus(UIStatus.EMPTY);
    }

    @Override
    public void showNetworkError() {
        changUiStatus(UIStatus.NETWORK_ERROR);
    }

    @Override
    public void showError() {
        changUiStatus(UIStatus.ERROR);
    }

    @Override
    public void changUiStatus(@NonNull UIStatus uiStatus) {
        switch (uiStatus) {
            case LOADING:
                showLoadingDialog();
                break;
            default:
                dismissLoadingDialog();
                break;
        }
    }

    /**
     * 展示弹框类型的loading
     */
    protected void showLoadingDialog() {
        if (!isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog = new DefaultLoadingDialog(this);
                loadingDialog.setCanceledOnTouchOutside(false);
            }
            loadingDialog.show();
        }
    }

    /**
     * 隐藏弹框loading
     */
    protected void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
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
     * 获取内容视图的LayoutRes
     *
     * @return 内容视图LayoutRes
     */
    @LayoutRes
    protected abstract int contentView();
}
