package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-新闻
 */

public class HeadLineNewsFragment extends BaseFragment implements IHeadLineView, SwipeRefreshLayout.OnRefreshListener{

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
        swipeRefreshLayout = findView(view, R.id.swipe_ly);
        swipeRefreshLayout.setOnRefreshListener(this);
        mMainNewsLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mMainNewsLv.getLastVisiblePosition() == (mMainNewsLv.getCount() - 1)) {
                            if (data.getHasmore() == 0) {
                                ToastUtil.showMiddle(getActivity(), R.string.no_resourse);
                            } else {
                                iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, data.getInfos().get(19).getSeq());
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
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        int index = event.getChildMsg();
        if (event.getMsg() == Constants.HEAD_LINE_NEWS_FLAG && index != -1) {
            iHeadLinePresenter.clickCount(data.getInfos().get(index).getId(), CLICK_COUNT);
        } else if (event.getMsg() == Constants.NET_CHAGED){
            ToastUtil.showMiddle(getActivity(),"sss");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }


    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        swipeRefreshLayout.setRefreshing(false);
        // swipeRefreshLayout.setLoading(false);
        if (newsVo != null) {
            data = newsVo.getData();
        }

        if (newsListNext == null) {
            newsListNext = new ArrayList<NewsVo.Data.NewsData>();
        }

        if (data.getInfos() != null) {
            newsListNext.addAll(data.getInfos());
            if (headLineNewsAdapter == null) {
                headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsListNext);
                mMainNewsLv.setAdapter(headLineNewsAdapter);
            }
            headLineNewsAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.no_news);
        }
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsVo weatherNewsVo) {
    }

    @Override
    public void getFontInfoSuccess(FontInfoVo fontInfoVo) {
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
