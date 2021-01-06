package com.yxr.androidbasedemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {
    int position;
    private TextView textView;

    public MyFragment(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TTTTTAG", "onCreateView: " + position );
        if (textView == null) {
            textView = new TextView(getActivity());
            textView.setText("Fragment : " + position);
        }
        getLifecycle().addObserver(new MyLifecycleObserver(getLifecycle()));
        return textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
