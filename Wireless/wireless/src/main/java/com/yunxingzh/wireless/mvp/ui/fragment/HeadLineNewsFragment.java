package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.refresh.SwipeRefreshLayout;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

import java.util.List;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-新闻
 */

public class HeadLineNewsFragment extends BaseFragment implements IHeadLineView, AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener,SwipeRefreshLayout.OnLoadListener {

    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;

    private List<NewsVo.Data.NewsData> newsList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_news, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mMainNewsLv = findView(view, R.id.head_line_lv);
        swipeRefreshLayout = findView(view,R.id.swipe_ly);
        swipeRefreshLayout.setOnLoadListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        swipeRefreshLayout.setLoadNoFull(false);
    }

    public void initData() {
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.showMiddle(getActivity(), "" + position);
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        if (newsVo != null) {
            newsList = newsVo.getData().getInfos();
        }
        headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsList, false);
        mMainNewsLv.setAdapter(headLineNewsAdapter);
        mMainNewsLv.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoad() {

    }
}
