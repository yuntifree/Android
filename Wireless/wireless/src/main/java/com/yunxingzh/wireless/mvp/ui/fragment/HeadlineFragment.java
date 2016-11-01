package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;

/**
 * Created by asus_ on 2016/11/1.
 * 头条
 */

public class HeadlineFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_headline, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

    }

    public void initData() {

    }
}
