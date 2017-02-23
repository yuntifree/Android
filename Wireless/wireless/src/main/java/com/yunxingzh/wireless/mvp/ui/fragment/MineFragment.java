package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;

/**
 * Created by stephen on 2017/2/22.
 * 我
 */

public class MineFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

    }

    public void initData() {

    }
}
