package com.yxr.androidbasedemo.jiazhao;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.yxr.base.http.HttpSimpleListener;
import com.yxr.base.presenter.BasePresenter;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
public class JiaZhaoPresenter extends BasePresenter<IJiaZhaoView> {
    private final JiaZhaoModel jiaZhaoModel;

    public JiaZhaoPresenter(@NonNull Lifecycle lifecycle, @NonNull IJiaZhaoView iView) {
        super(lifecycle, iView);
        jiaZhaoModel = new JiaZhaoModel(lifecycle, this);
    }

    public void getJiaZhaoResponse() {
        getView().showLoading();
        jiaZhaoModel.getJiaZhaoResponse(JiaZhaoModel.Subject.SUBJECT1, JiaZhaoModel.Model.A1, JiaZhaoModel.TestType.RAND, new HttpSimpleListener<JiaZhaoResponse>() {
            @Override
            public void onSuccess(@NonNull JiaZhaoResponse response) {
                getView().showContent();
                getView().jiaZhaoGetSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                getView().showNetworkError();
                getView().toast(message);
            }
        });

    }
}
