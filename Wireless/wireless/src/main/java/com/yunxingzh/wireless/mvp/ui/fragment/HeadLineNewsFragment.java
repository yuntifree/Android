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
    private final static int CLICK_COUNT = 1;//记录点击次数；0-视频，1-新闻，2-广告展示；3-广告点击

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;

    private List<NewsVo.Data.NewsData> newsListNext;
    private NewsVo.Data data;

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
        iHeadLinePresenter.clickCount(data.getInfos().get(position).getId(),CLICK_COUNT);
        startActivity(WebViewActivity.class, Constants.URL,data.getInfos().get(position).getDst(),Constants.TITLE,data.getInfos().get(position).getTitle());
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setLoading(false);
        if (newsVo != null) {
            data = newsVo.getData();
        }

        if (newsListNext == null) {
            newsListNext = new ArrayList<NewsVo.Data.NewsData>();
        }

        if (data.getInfos() != null) {
            newsListNext.addAll(data.getInfos());
            if (headLineNewsAdapter == null) {
                headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsListNext, false);
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
        newsListNext.clear();
    }

    @Override
    public void onLoad() {
        if (data.getHasmore() == 0) {
            swipeRefreshLayout.setLoading(false);
            ToastUtil.showMiddle(getActivity(), R.string.no_resourse);
        } else {
            iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, data.getInfos().get(19).getSeq());
        }
    }

    public void startActivity(Class activity,String key,String videoUrl,String titleKey,String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        intent.putExtra(titleKey, title);
        startActivity(intent);
    }
}
