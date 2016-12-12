package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.vo.HotInfo;
import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.WeatherNewsList;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-新闻
 */

public class HeadLineNewsFragment extends BaseFragment implements IHeadLineView, SwipeRefreshLayout.OnRefreshListener {

    private final static int HEAD_LINE_TYPE = 0;// 0-新闻 1-视频 2-应用 3-游戏 5-东莞新闻
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int CLICK_COUNT = 1;//上报；0- 视频播放 1-新闻点击 2-广告展示 3-广告点击 4-服务

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;
    private List<HotInfo> newsListNext;
    private HotInfoList data;
    private LinearLayout mNetErrorLay;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_news, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mMainNewsLv = findView(view, R.id.head_line_lv);
        swipeRefreshLayout = findView(view, R.id.swipe_ly);
        swipeRefreshLayout.setOnRefreshListener(this);
        mNetErrorLay = findView(view, R.id.net_error_news_lay);
        mMainNewsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mMainNewsLv.getLastVisiblePosition() == (mMainNewsLv.getCount() - 1)) {
                            if (data.hasmore == 0) {
                                ToastUtil.showMiddle(getActivity(), R.string.no_resourse);
                            } else {
                                iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, data.infos.get(19).seq);
                            }
                        }
                        // 判断滚动到顶部
                        if (mMainNewsLv.getFirstVisiblePosition() == 0) {
                            // ToastUtil.showMiddle(getActivity(), "顶部");
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        swipeRefreshLayout.setRefreshing(true);
        netErrorLay();
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        int index = event.getChildMsg();
        if (event.getMsg() == Constants.HEAD_LINE_NEWS_FLAG && index != -1) {//上报
            iHeadLinePresenter.clickCount(data.infos.get(index).id, CLICK_COUNT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        iHeadLinePresenter.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }


    @Override
    public void getHeadLineSuccess(HotInfoList newsVo) {
        swipeRefreshLayout.setRefreshing(false);
        // swipeRefreshLayout.setLoading(false);
        if (newsVo != null) {
            data = newsVo;
        }

        if (newsListNext == null) {
            newsListNext = new ArrayList<HotInfo>();
        }

        if (data.infos != null) {
            newsListNext.addAll(data.infos);
            if (headLineNewsAdapter == null) {
                headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsListNext);
                mMainNewsLv.setAdapter(headLineNewsAdapter);
            }
            headLineNewsAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.no_news);
        }
    }

    public void netErrorLay(){
        if (!NetUtils.isNetworkAvailable(getActivity())){
            View netView = LayoutInflater.from(getActivity()).inflate(R.layout.wifi_closed, null);
            swipeRefreshLayout.setVisibility(View.GONE);
            mNetErrorLay.setVisibility(View.VISIBLE);
            TextView openTv = (TextView) netView.findViewById(R.id.net_open_tv);
            TextView contentTv = (TextView) netView.findViewById(R.id.net_content_tv);
            TextView refreshBtn = (TextView) netView.findViewById(R.id.open_wifi_btn);
            openTv.setVisibility(View.GONE);
            contentTv.setText(R.string.network_error);
            refreshBtn.setText(R.string.refresh_net);
            mNetErrorLay.addView(netView);
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetUtils.isNetworkAvailable(getActivity())) {
                        ToastUtil.showMiddle(getActivity(), R.string.net_set);
                    } else {
                        mNetErrorLay.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
                    }
                }
            });
        }
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsList weatherNewsVo) {
    }

    @Override
    public void getFontInfoSuccess(FontInfoList fontInfoVo) {
    }

    @Override
    public void onRefresh() {
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        newsListNext.clear();
    }

    public void startActivity(Class activity, String key, String videoUrl, String titleKey, String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        intent.putExtra(titleKey, title);
        startActivity(intent);
    }
}
