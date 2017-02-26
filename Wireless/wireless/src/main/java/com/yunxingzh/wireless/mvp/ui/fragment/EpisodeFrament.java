package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetJokesView;

import wireless.libs.bean.resp.JokeList;

/**
 * Created by stephen on 2017/2/25.
 * 段子
 */

public class EpisodeFrament extends BaseFragment implements IGetJokesView {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {

    }

    public void initData() {

    }

    @Override
    public void getJokesSuccess(JokeList jokeList) {

    }

    @Override
    public void getJokesFaild() {

    }
}
