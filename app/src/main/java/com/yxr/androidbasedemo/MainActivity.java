package com.yxr.androidbasedemo;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yxr.base.activity.BaseStatusActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseStatusActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnClick)
    Button btnClick;

    @Override
    protected int contentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MyFragment());
        fragmentList.add(new MyFragment());
        fragmentList.add(new MyFragment());
        fragmentList.add(new MyFragment());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
    }

    @OnClick(R.id.btnClick)
    public void onViewClicked() {
        toast("Clicked");
    }

}
