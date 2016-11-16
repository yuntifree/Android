package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineGameAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-游戏
 */

public class HeadLineGameFragment extends BaseFragment implements IHeadLineView,SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private final static int HEAD_LINE_TYPE = 3;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用

    private RecyclerView mListRv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineGameAdapter headLineGameAdapter;
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLay;
    private NewsVo newsVo;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mListRv = findView(view,R.id.list_rv);
        mListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListRv.addItemDecoration(new SpacesItemDecoration(Constants.ITEM_HEIGHT));
        mSwipeRefreshLay = findView(view, R.id.swipe_refresh_news);
        mSwipeRefreshLay.setOnRefreshListener(this);
    }

    public void initData() {
        // headLineGameAdapter = new HeadLineGameAdapter(new ArrayList<>());
        headLineGameAdapter.openLoadMore(Constants.PAGE_SIZE);
        headLineGameAdapter.setOnLoadMoreListener(this);
        mListRv.setAdapter(headLineGameAdapter);

        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE,HEAD_LINE_SEQ);

        mListRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                List<NewsVo.NewsDataVo> dataList = baseQuickAdapter.getData();
//                NewsVo.NewsDataVo newsVo = dataList.get(i);
//                Intent intent = new Intent(getActivity(), AboutWebViewActivity.class);
//                intent.putExtra(Constants.FLAG_WEBVIEW,Constants.FLAG_ADVERTISES_PAGE);
//                intent.putExtra("url",newsVo.getUrl());
//                startActivity(intent);
            }
        });
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVoList) {
        mSwipeRefreshLay.setRefreshing(false);
        if (newsVoList != null) {
            newsVo = newsVoList;
//            if (newsVoList.getP().equals("2")) {
//                headLineGameAdapter.setNewData(newsVoList.getData());
//            } else {
//                headLineGameAdapter.addData(newsVoList.getData());
//            }
//
//            if (newsVoList.getData().size() == 0) {
//                // 数据全部加载完毕就调用 loadComplete
//                headLineGameAdapter.loadComplete();
//                ToastUtil.showMiddle(getActivity(), R.string.no_resource);
//            }
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.re_error);
        }
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsVo weatherNewsVo) {}

    @Override
    public void getFontInfoSuccess(FontInfoVo fontInfoVo) {}

    @Override
    public void onRefresh() {
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE,HEAD_LINE_SEQ);
    }

    @Override
    public void onLoadMoreRequested() {
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE,HEAD_LINE_SEQ);
    }
}
