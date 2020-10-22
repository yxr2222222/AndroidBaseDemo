package com.yxr.androidbasedemo.jiazhao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yxr.androidbasedemo.R;
import com.yxr.base.fragment.BaseStatusFragment;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
public class JiaZhaoFragment extends BaseStatusFragment implements IJiaZhaoView {
    private TextView tvContent;

    @Override
    protected int contentView() {
        return R.layout.fragment_jiazhao;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvContent = findViewById(R.id.tvContent);
    }

    @Override
    protected void initData() {
        super.initData();
        JiaZhaoPresenter jiaZhaoPresenter = new JiaZhaoPresenter(getLifecycle(), this);
        jiaZhaoPresenter.getJiaZhaoResponse();
    }

    @Override
    public void jiaZhaoGetSuccess(@NonNull JiaZhaoResponse response) {
        tvContent.setText(new Gson().toJson(response));
    }
}
