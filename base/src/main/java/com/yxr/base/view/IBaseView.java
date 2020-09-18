package com.yxr.base.view;

import com.yxr.base.presenter.BasePresenter;

/**
 * @author ciba
 * @description MVP中的View层
 * @date 2020/9/17
 */
public interface IBaseView<T extends BasePresenter> {
    /**
     * 获取MVP中的Presenter
     *
     * @return Presenter
     */
    T getPresenter();
}
