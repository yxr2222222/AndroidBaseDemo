package com.yxr.androidbasedemo;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.yxr.base.fragment.BaseStatusFragment;

import butterknife.BindView;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
@SuppressLint("ValidFragment")
public class MyFragment extends BaseStatusFragment {

    @BindView(R.id.tvContent)
    TextView tvContent;

    @Override
    protected int contentView() {
        return R.layout.fragment_jiazhao;
    }

    @Override
    protected void initData() {
        tvContent.setText("aaaaaaaaaaaaaaaaaaaaa");
    }
}
