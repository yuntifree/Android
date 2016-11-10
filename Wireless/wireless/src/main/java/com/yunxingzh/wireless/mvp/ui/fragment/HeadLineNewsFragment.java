package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.refresh.SwipeRefreshLayout;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-新闻
 */

public class HeadLineNewsFragment extends BaseFragment implements IHeadLineView, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {

    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;

    private List<NewsVo.Data.NewsData> newsList;
    private NewsVo.Data data;
    private int lastPosition;
    private int allSize;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_news, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mMainNewsLv = findView(view, R.id.head_line_lv);
        mMainNewsLv.setOnItemClickListener(this);
        swipeRefreshLayout = findView(view, R.id.swipe_ly);
        swipeRefreshLayout.setOnLoadListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);
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
        startActivity(WebViewActivity.class, Constants.URL,data.getInfos().get(position).getDst(),Constants.TITLE,data.getInfos().get(position).getTitle());
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setLoading(false);
        data = newsVo.getData();
        if (newsVo != null) {
            newsList = newsVo.getData().getInfos();

            allSize = mMainNewsLv.getCount();
            lastPosition = mMainNewsLv.getLastVisiblePosition();
        }

//        if (newsList == null) {
//            newsList = new ArrayList<NewsVo.Data.NewsData>();
//        }

//        if (data.getHasmore() == 1) {
//            newsList.clear();
//        }

        if (data.getInfos() != null) {
            newsList.addAll(data.getInfos());
            if (headLineNewsAdapter == null) {
                headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsList, false);
                mMainNewsLv.setAdapter(headLineNewsAdapter);
            }
            headLineNewsAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.no_news);
        }
    }


    @Override
    public void onRefresh() {
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
    }

    @Override
    public void onLoad() {
        if (data.getHasmore() == 0) {
            swipeRefreshLayout.setLoading(false);
            ToastUtil.showMiddle(getActivity(), R.string.no_resourse);
        } else {
            iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, lastPosition);
        }
    }

    public void startActivity(Class activity,String key,String videoUrl,String titleKey,String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        intent.putExtra(titleKey, title);
        startActivity(intent);
    }
}
