package com.yunxingzh.wireless.mvp.ui.fragment;

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
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.WirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.EpisodeAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetJokesView;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.JokeList;

/**
 * Created by stephen on 2017/2/25.
 * 段子
 */

public class EpisodeFrament extends BaseFragment implements IGetJokesView, SwipeRefreshLayout.OnRefreshListener,NetErrorLayout.OnNetErrorClickListener {

    private static final int ZAN_TYPE = 5;//点赞
    private static final int CAI_TYPE = 9;//点赞

    private ListView mListLv;
    private SwipeRefreshLayout mSwipeRefreshLay;
    private IHeadLinePresenter iHeadLinePresenter;
    private IWirelessPresenter iWirelessPresenter;
    private EpisodeAdapter episodeAdapter;
    private List<JokeList.JokeVo> jokeVos;
    private JokeList jokeList;
    private boolean isFastClick;
    private LinearLayout mNetErrorLay;
    private NetErrorLayout netErrorLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mListLv = findView(view, R.id.epi_lv);
        mSwipeRefreshLay = findView(view, R.id.epi_sw);
        mSwipeRefreshLay.setOnRefreshListener(this);
        mNetErrorLay = findView(view, R.id.net_error_lay);
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        iWirelessPresenter = new WirelessPresenterImpl(this);
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getJokes(0);
        mListLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mListLv.getLastVisiblePosition() == (mListLv.getCount() - 1)) {
                            if (isAdded() && getActivity() != null && jokeList.hasmore == 0) {
                                ToastUtil.showMiddle(getActivity(), R.string.no_resourse);
                            } else {
                                if (isFastClick) {
                                    isFastClick = false;
                                    if (jokeList != null && jokeList.infos.size() > 0) {
                                        iHeadLinePresenter.getJokes(jokeList.infos.get(jokeList.infos.size() - 1).seq);
                                    }
                                }
                            }
                        }
                        // 判断滚动到顶部
                        if (mListLv.getFirstVisiblePosition() == 0) {
                            // ToastUtil.showMiddle(getActivity(), "顶部");
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        if (isAdded() && getActivity() != null) {
            netErrorLayout = new NetErrorLayout(getActivity());
            netErrorLayout.setOnNetErrorClickListener(this);
            if (!NetUtils.isNetworkAvailable(getActivity())) {
                mSwipeRefreshLay.setVisibility(View.GONE);
                mNetErrorLay.setVisibility(View.VISIBLE);
                View netErrorView = netErrorLayout.netErrorLay(0);
                mNetErrorLay.addView(netErrorView);
            }
        }
    }

    @Override
    public void getJokesSuccess(JokeList jokes) {
        isFastClick = true;
        mSwipeRefreshLay.setRefreshing(false);
        if (jokes != null) {
            jokeList = jokes;
        }

        if (jokeVos == null) {
            jokeVos = new ArrayList<JokeList.JokeVo>();
        }

        if (jokeList.infos != null) {
            jokeVos.addAll(jokeList.infos);
            if (isAdded() && getActivity() != null && episodeAdapter == null) {
                episodeAdapter = new EpisodeAdapter(getActivity(), jokeVos);
                mListLv.setAdapter(episodeAdapter);
            }
            episodeAdapter.notifyDataSetChanged();
        } else {
            if (isAdded() && getActivity() != null) {
                ToastUtil.showMiddle(getActivity(), R.string.no_resource);
            }
        }
    }

    @Override
    public void getJokesFaild() {
        if (mSwipeRefreshLay != null) {
            mSwipeRefreshLay.setRefreshing(false);
        }
        if (isAdded() && getActivity() != null){
            ToastUtil.showMiddle(getActivity(), R.string.net_error);

            if (netErrorLayout == null) {
                mSwipeRefreshLay.setVisibility(View.GONE);
                netErrorLayout = new NetErrorLayout(getActivity());
                netErrorLayout.setOnNetErrorClickListener(this);
                mNetErrorLay.setVisibility(View.VISIBLE);
                View netErrorView = netErrorLayout.netErrorLay(0);
                mNetErrorLay.addView(netErrorView);
            } else {
                mSwipeRefreshLay.setVisibility(View.GONE);
                mNetErrorLay.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh() {
        iHeadLinePresenter.getJokes(0);
        if (jokeVos != null && jokeVos.size() > 0) {
            jokeVos.clear();
        }
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        int position = event.getChildMsg();
        if (event.getMsg() == Constants.JOKE_ZAN_FLAG && position != -1) {//点赞上报
            if (iWirelessPresenter != null && jokeVos != null) {
                iWirelessPresenter.clickCount(jokeVos.get(position).id, ZAN_TYPE, "");
            }
        }
        if (event.getMsg() == Constants.JOKE_CAI_FLAG && position != -1) {//点踩上报
            if (iWirelessPresenter != null && jokeVos != null) {
                iWirelessPresenter.clickCount(jokeVos.get(position).id, CAI_TYPE, "");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iHeadLinePresenter != null && iWirelessPresenter != null) {
            iHeadLinePresenter.onDestroy();
            iWirelessPresenter.onDestroy();
        }
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void netErrorClick() {
        if (isAdded() && getActivity() != null) {
            if (!NetUtils.isNetworkAvailable(getActivity())) {
                ToastUtil.showMiddle(getActivity(), R.string.net_set);
            } else {
                mNetErrorLay.setVisibility(View.GONE);
                mSwipeRefreshLay.setVisibility(View.VISIBLE);
                iHeadLinePresenter.getJokes(0);
            }
        }
    }
}
