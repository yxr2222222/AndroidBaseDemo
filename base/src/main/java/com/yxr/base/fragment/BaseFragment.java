package com.yxr.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxr.base.util.ContextCompatUtil;
import com.yxr.base.util.ToastUtil;
import com.yxr.base.view.IBaseUiView;
import com.yxr.base.widget.dialog.DefaultLoadingDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseFragment
 *
 * @author ciba
 * @date 2020/09/17
 */

public abstract class BaseFragment extends Fragment implements IBaseUiView {
    public Activity activity;
    protected View rootView;
    /**
     * UI是否准备完成，用于数据懒加载
     */
    protected boolean uiPrepare;
    /**
     * 数据是否加载过了，用于数据懒加载
     */
    protected boolean dataLoaded;
    private boolean attached;
    private DefaultLoadingDialog loadingDialog;
    private Unbinder unbinder;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 用户可见不可见，用于Fragment数据懒加载
        checkFirstInitData();

        checkChildFragmentFirstInitData(isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        // Fragment与Activity发生关联时调用
        super.onAttach(context);
        attached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attached = false;
    }

    @Override
    public void onDestroy() {
        dismissLoadingDialog();
        loadingDialog = null;
        super.onDestroy();
        unbindView();
    }

    @Override
    public void toast(@StringRes int stringRes) {
        if (activity != null) {
            toast(ContextCompatUtil.getText(activity.getApplicationContext(), stringRes));
        }
    }

    @Override
    public void toast(String message) {
        if (activity != null) {
            ToastUtil.show(activity.getApplicationContext(), message);
        }
    }

    /**
     * 展示弹框类型的loading
     */
    @Override
    public void showLoadingDialog() {
        if (activity != null && !activity.isFinishing()) {
            if (loadingDialog == null) {
                loadingDialog = createLoadingDialog();
                if (loadingDialog == null) {
                    loadingDialog = new DefaultLoadingDialog(activity);
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

    /**
     * 检查子Fragment是否需要重新进行第一次数据初始化
     *
     * @param isVisibleToUser 用户是否可见
     */
    private void checkChildFragmentFirstInitData(boolean isVisibleToUser) {
        if (!isVisibleToUser || !attached) {
            return;
        }
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments.size() > 0) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof BaseFragment && fragment.getUserVisibleHint()) {
                    ((BaseFragment) fragment).checkFirstInitData();
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        if (rootView == null) {
            rootView = inflaterRootView(inflater);
            uiPrepare = true;
            bindView();
            // 初始化View
            initView(savedInstanceState);
            // 初始化事件监听
            initListener();
            checkFirstInitData();
        }
        return rootView;
    }

    protected View inflaterRootView(@NonNull LayoutInflater inflater) {
        return inflater.inflate(contentView(), null);
    }

    /**
     * 通过rootView查找子View
     *
     * @param id  控件IdRes
     * @param <T> 子View的具体类型
     * @return 子View
     */
    protected <T extends View> T findViewById(@IdRes int id) {
        if (rootView == null) {
            throw new RuntimeException("rootView is not be null");
        }
        return rootView.findViewById(id);
    }

    public View getContentView() {
        return rootView;
    }

    /**
     * 当UI准备完成并且Fragment是可见的并且还没有初始化过数据时进行数据初始化
     */
    protected void checkFirstInitData() {
        Fragment parentFragment = getParentFragment();
        boolean parentIsVisible = parentFragment == null || parentFragment.getUserVisibleHint();

        if (uiPrepare && parentIsVisible && getUserVisibleHint() && !dataLoaded) {
            dataLoaded = true;
            initData();
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
     * 自定义Loading的Dialog
     *
     * @return Loading的Dialog
     */
    protected DefaultLoadingDialog createLoadingDialog() {
        return null;
    }

    private void bindView() {
        if (getContentView() != null) {
            unbinder = ButterKnife.bind(this, getContentView());
        }
    }

    private void unbindView() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    /**
     * 内容视图的layout布局
     *
     * @return 内容视图的layout布局id
     */
    @LayoutRes
    protected abstract int contentView();

}
