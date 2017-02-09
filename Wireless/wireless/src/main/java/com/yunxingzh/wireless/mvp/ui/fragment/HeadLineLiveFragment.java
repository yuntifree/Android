package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetLiveListView;

import wireless.libs.bean.resp.LiveList;

/**
 * Created by stephen on 2017/2/8.
 * 头条-直播
 */

public class HeadLineLiveFragment extends BaseFragment implements IGetLiveListView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mListRv;
    private SwipeRefreshLayout mSwipeRefreshLay;
    private IHeadLinePresenter iHeadLinePresenter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mListRv = findView(view, R.id.list_rv);
        mSwipeRefreshLay = findView(view, R.id.swipe_refresh_news);
        mSwipeRefreshLay.setOnRefreshListener(this);
        mListRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mListRv.setHasFixedSize(true);
        mListRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
               // XdgVo dataVo = (XdgVo) baseQuickAdapter.getItem(i);
             //   Intent intent = new Intent(getActivity(), XdgDetailsActivity.class);
               // intent.putExtra(Constants.ARG_DATA, dataVo);
               // startActivity(intent);
            }
        });
    }

    public void initData() {
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getLiveList(0);
    }

    @Override
    public void getLiveListSuccess(LiveList liveList) {

    }

    @Override
    public void getLiveListFaild() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMoreRequested() {

    }

}
