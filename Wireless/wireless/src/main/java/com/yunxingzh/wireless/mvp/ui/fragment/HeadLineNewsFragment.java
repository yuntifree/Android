package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.IWirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.NewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.vo.HotInfo;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-新闻
 */

public class HeadLineNewsFragment extends BaseFragment implements IHeadLineView, SwipeRefreshLayout.OnRefreshListener,
        NetErrorLayout.OnNetErrorClickListener {

    private static final String ARG_CTYPE = "ctype";
    private final static int HEAD_LINE_TYPE = 0;// 0-新闻 1-视频 2-应用 3-游戏 4-本地 5-娱乐
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int CLICK_COUNT = 1;//上报；0- 视频播放 1-新闻点击 2-广告展示 3-广告点击 4-服务

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mMainNewsRv;
    private IHeadLinePresenter iHeadLinePresenter;
    private IWirelessPresenter iWirelessPresenter;
    private NewsAdapter headLineNewsAdapter;
    private List<HotInfo> newsListNext;
    private HotInfoList data;
    private LinearLayout mNetErrorLay;
    private NetErrorLayout netErrorLayout;

    private boolean isFastClick = true;

    private int newsTypes;
    private boolean firstLoad = true;

    public static HeadLineNewsFragment getInstance(int type) {
        HeadLineNewsFragment headLineNewsFragment = new HeadLineNewsFragment();
        headLineNewsFragment.newsTypes = type;
        return headLineNewsFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_news, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mMainNewsRv = findView(view, R.id.head_line_rv);
        swipeRefreshLayout = findView(view, R.id.swipe_ly);
        mMainNewsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainNewsRv.setHasFixedSize(true);
        mMainNewsRv.addItemDecoration(new SpacesItemDecoration(3));
        swipeRefreshLayout.setOnRefreshListener(this);
        mNetErrorLay = findView(view, R.id.net_error_lay);

        // RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        // RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        mMainNewsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    // 当不滚动时
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //滑动完成
                        if (!mMainNewsRv.canScrollVertically(1)) {//false:到底部
                            if (data != null) {
                                if (data.hasmore == 0) {
                                    ToastUtil.showMiddle(getActivity(), R.string.no_resourse);
                                } else {
                                    if (isFastClick) {
                                        isFastClick = false;
                                        if (data != null && data.infos.size() > 0) {
                                            iHeadLinePresenter.getHeadLine(newsTypes, data.infos.get(data.infos.size() - 1).seq);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                //swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                if (topRowVerticalPosition >= 0){
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iWirelessPresenter = new IWirelessPresenterImpl(this);
        if (firstLoad) {
            iHeadLinePresenter.getHeadLine(this.newsTypes, HEAD_LINE_SEQ);
            swipeRefreshLayout.setRefreshing(true);
        }

        if (!NetUtils.isNetworkAvailable(getActivity())) {
            swipeRefreshLayout.setVisibility(View.GONE);
            netErrorLayout = new NetErrorLayout(getActivity());
            netErrorLayout.setOnNetErrorClickListener(this);
            mNetErrorLay.setVisibility(View.VISIBLE);
            View netErrorView = netErrorLayout.netErrorLay(0);
            mNetErrorLay.addView(netErrorView);
        }
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        int index = event.getChildMsg();
        if (event.getMsg() == Constants.HEAD_LINE_NEWS_FLAG && index != -1) {//上报
            if (iWirelessPresenter != null && data != null) {
                iWirelessPresenter.clickCount(data.infos.get(index).id, CLICK_COUNT);
            }
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

        firstLoad = false;
        isFastClick = true;

        if (newsVo != null) {
            data = newsVo;
        }
        if (newsListNext == null) {
            newsListNext = new ArrayList<>();
        }

        if (swipeRefreshLayout.isRefreshing()) {//下拉刷新后清空之前的数据
            if (data.infos != null) {
                newsListNext.clear();
            }
        }
        swipeRefreshLayout.setRefreshing(false);

        if (data.infos != null) {
            newsListNext.addAll(data.infos);
            if (headLineNewsAdapter == null) {
                headLineNewsAdapter = new NewsAdapter(getActivity(), newsListNext);
                mMainNewsRv.setAdapter(headLineNewsAdapter);
            }
            // mMainNewsRv.setAdapter(headLineNewsAdapter);
            headLineNewsAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.re_error);
        }
    }

    @Override
    public void getHeadLineFaild() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        isFastClick = true;
        ToastUtil.showMiddle(getActivity(), R.string.net_error);
    }

    @Override
    public void onRefresh() {
        iHeadLinePresenter.getHeadLine(newsTypes, HEAD_LINE_SEQ);
    }

    public void startActivity(Class activity, String key, String videoUrl, String titleKey, String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        intent.putExtra(titleKey, title);
        startActivity(intent);
    }

    @Override
    public void netErrorClick() {
        if (!NetUtils.isNetworkAvailable(getActivity())) {
            ToastUtil.showMiddle(getActivity(), R.string.net_set);
        } else {
            mNetErrorLay.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            iHeadLinePresenter.getHeadLine(newsTypes, HEAD_LINE_SEQ);
        }
    }
}
