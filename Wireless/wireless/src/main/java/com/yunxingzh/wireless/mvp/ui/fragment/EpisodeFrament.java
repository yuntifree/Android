package com.yunxingzh.wireless.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.WirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.EpisodeAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetJokesView;
import com.yunxingzh.wireless.utils.SpacesItemDecoration;
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

public class EpisodeFrament extends BaseFragment implements IGetJokesView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private static final int ZAN_TYPE = 5;//点赞
    private static final int CAI_TYPE = 9;//点赞

    private RecyclerView mListRv;
    private SwipeRefreshLayout mSwipeRefreshLay;
    private IHeadLinePresenter iHeadLinePresenter;
    private IWirelessPresenter iWirelessPresenter;
    private boolean isFirstRefresh = true;
    private EpisodeAdapter episodeAdapter;
    private List<JokeList.JokeVo> jokeVos;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mListRv = findView(view, R.id.live_rv);
        mSwipeRefreshLay = findView(view, R.id.swipe_refresh_live);
        mSwipeRefreshLay.setOnRefreshListener(this);
        mListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListRv.setHasFixedSize(true);
        mListRv.addItemDecoration(new SpacesItemDecoration(30));
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        episodeAdapter = new EpisodeAdapter(new ArrayList<JokeList.JokeVo>());
        episodeAdapter.openLoadMore(Constants.PAGE_SIZE);
        episodeAdapter.setOnLoadMoreListener(this);
        episodeAdapter.setEmptyView(emptyView(mListRv));
        mListRv.setAdapter(episodeAdapter);
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iWirelessPresenter = new WirelessPresenterImpl(this);
        iHeadLinePresenter.getJokes(0);
    }

    @Override
    public void getJokesSuccess(JokeList jokeList) {
        mSwipeRefreshLay.setRefreshing(false);
        if (jokeList != null) {
            jokeVos = jokeList.infos;
            if (jokeVos != null) {
                if (isFirstRefresh) {
                    isFirstRefresh = false;
                    episodeAdapter.setNewData(jokeVos);
                } else {
                    episodeAdapter.addData(jokeVos);
                }
            } else {
                // 数据全部加载完毕就调用 loadComplete
                episodeAdapter.loadComplete();
                ToastUtil.showMiddle(getActivity(), R.string.no_resource);
            }
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.re_error);
        }
    }

    @Override
    public void getJokesFaild() {
        if (mSwipeRefreshLay != null) {
            mSwipeRefreshLay.setRefreshing(false);
        }
        ToastUtil.showMiddle(getActivity(), R.string.net_error);
    }

    @Override
    public void onRefresh() {
        isFirstRefresh = true;
        iHeadLinePresenter.getJokes(0);
    }

    @Override
    public void onLoadMoreRequested() {
        if (iHeadLinePresenter != null && jokeVos != null) {
            iHeadLinePresenter.getJokes(jokeVos.get(jokeVos.size() - 1).seq);
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

    private View emptyView(ViewGroup viewGroup) {
        View netView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, viewGroup, false);
        TextView netErrorBtn = (TextView) netView.findViewById(R.id.video_net_error_btn);
        netErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstRefresh = true;
                iHeadLinePresenter.getJokes(0);
            }
        });
        return netView;
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
}
