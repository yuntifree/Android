package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.VideoPlayActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineLiveAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetLiveListView;
import com.yunxingzh.wireless.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.LiveList;
import wireless.libs.bean.vo.LiveVo;

/**
 * Created by stephen on 2017/2/8.
 * 头条-直播
 */

public class HeadLineLiveFragment extends BaseFragment implements IGetLiveListView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mListRv;
    private SwipeRefreshLayout mSwipeRefreshLay;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineLiveAdapter headLineLiveAdapter;
    private boolean isFirstRefresh = true;
    private List<LiveVo> liveVos;
    private int offset;

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
        mListRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mListRv.setHasFixedSize(true);
        mListRv.addItemDecoration(new SpacesItemDecoration(4));
    }

    public void initData() {
        headLineLiveAdapter = new HeadLineLiveAdapter(new ArrayList<LiveVo>());
        headLineLiveAdapter.openLoadMore(Constants.PAGE_SIZE);
        headLineLiveAdapter.setOnLoadMoreListener(this);
        headLineLiveAdapter.setEmptyView(emptyView(mListRv));
        mListRv.setAdapter(headLineLiveAdapter);

        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getLiveList(0);
        headLineLiveAdapter.setOnLiveItemClickListener(new HeadLineLiveAdapter.onLiveItemClickListener() {
            @Override
            public void onItemClick(LiveVo liveVo) {
                if (liveVo != null) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(Constants.URL, Constants.LIVE_NUM + liveVo.live_id + "");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void getLiveListSuccess(LiveList liveList) {
        mSwipeRefreshLay.setRefreshing(false);
        if (liveList != null) {
            liveVos = liveList.list;
            if (liveList.more == 1) {
                offset = liveList.offset;
                if (isFirstRefresh){
                    isFirstRefresh = false;
                    headLineLiveAdapter.setNewData(liveVos);
                } else {
                    headLineLiveAdapter.addData(liveVos);
                }
            } else {
                // 数据全部加载完毕就调用 loadComplete
                headLineLiveAdapter.loadComplete();
                ToastUtil.showMiddle(getActivity(), R.string.no_resource);
            }
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.re_error);
        }
    }

    @Override
    public void getLiveListFaild() {
        if (mSwipeRefreshLay != null) {
            mSwipeRefreshLay.setRefreshing(false);
        }
        ToastUtil.showMiddle(getActivity(), R.string.net_error);
    }

    @Override
    public void onRefresh() {
        isFirstRefresh = true;
        iHeadLinePresenter.getLiveList(0);
    }

    @Override
    public void onLoadMoreRequested() {
        if (iHeadLinePresenter != null) {
            iHeadLinePresenter.getLiveList(offset);
        }
    }

    private View emptyView(ViewGroup viewGroup) {
        View netView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, viewGroup, false);
        TextView netErrorBtn = (TextView) netView.findViewById(R.id.video_net_error_btn);
        netErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstRefresh = true;
                iHeadLinePresenter.getLiveList(0);
            }
        });
        return netView;
    }

}
