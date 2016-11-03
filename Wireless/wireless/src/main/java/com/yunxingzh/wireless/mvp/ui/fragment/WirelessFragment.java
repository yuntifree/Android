package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

/**
 * Created by asus_ on 2016/11/1.
 *  无线
 */

public class WirelessFragment extends BaseFragment implements IHeadLineView {

    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wireless, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mMainNewsLv = findView(view,R.id.main_news_lv);
    }

    public void initData() {
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getHeadLine(0,1);
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {

    }
}
