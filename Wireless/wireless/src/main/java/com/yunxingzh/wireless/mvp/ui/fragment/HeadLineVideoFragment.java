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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.VideoPlayActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineVideoAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-视频
 */

public class HeadLineVideoFragment extends BaseFragment implements IHeadLineView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private final static int HEAD_LINE_TYPE = 1;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用

    private LinearLayout mNetErrorVideoLay;
    private RecyclerView mListRv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineVideoAdapter headLineVideoAdapter;
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLay;
    private List<NewsVo.Data.NewsData> newsVo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mListRv = findView(view, R.id.list_rv);
        mListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListRv.setHasFixedSize(true);
        mListRv.addItemDecoration(new SpacesItemDecoration(Constants.ITEM_HEIGHT));
        mSwipeRefreshLay = findView(view, R.id.swipe_refresh_news);
        mSwipeRefreshLay.setOnRefreshListener(this);
        mNetErrorVideoLay = findView(view, R.id.net_error_video_lay);
    }

    public void initData() {
        headLineVideoAdapter = new HeadLineVideoAdapter(new ArrayList<NewsVo.Data.NewsData>());
        headLineVideoAdapter.openLoadMore(Constants.PAGE_SIZE);
        headLineVideoAdapter.setOnLoadMoreListener(this);
        mListRv.setAdapter(headLineVideoAdapter);

        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);

        if (!NetUtils.isNetworkAvailable(getActivity())){
            View netView = LayoutInflater.from(getActivity()).inflate(R.layout.wifi_closed, null);
            netView.setBackgroundColor(getResources().getColor(R.color.gray_f5f5f5));
            mSwipeRefreshLay.setVisibility(View.GONE);
            mNetErrorVideoLay.setVisibility(View.VISIBLE);
            TextView openTv = (TextView) netView.findViewById(R.id.net_open_tv);
            TextView contentTv = (TextView) netView.findViewById(R.id.net_content_tv);
            TextView refreshBtn = (TextView) netView.findViewById(R.id.open_wifi_btn);
            openTv.setVisibility(View.GONE);
            contentTv.setText(R.string.network_error);
            refreshBtn.setText(R.string.refresh_net);
            mNetErrorVideoLay.addView(netView);
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetUtils.isNetworkAvailable(getActivity())) {
                        ToastUtil.showMiddle(getActivity(), "请检查网络设置");
                    } else {
                        mNetErrorVideoLay.setVisibility(View.GONE);
                        mSwipeRefreshLay.setVisibility(View.VISIBLE);
                        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
                    }
                }
            });
        }

        mListRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                List<NewsVo.Data.NewsData> data = baseQuickAdapter.getData();
                iHeadLinePresenter.clickCount(data.get(i).getId(),HEAD_LINE_SEQ);//记录播放次数
                startActivity(VideoPlayActivity.class,Constants.VIDEO_URL,data.get(i).getDst());
            }
        });
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVoList) {
        mSwipeRefreshLay.setRefreshing(false);
        if (newsVoList != null) {
            newsVo = newsVoList.getData().getInfos();
            if (newsVoList.getData().getHasmore() == 1) {
                headLineVideoAdapter.addData(newsVoList.getData().getInfos());
               // headLineVideoAdapter.setNewData(newsVoList.getData().getInfos());
            } else {
                // 数据全部加载完毕就调用 loadComplete
                headLineVideoAdapter.loadComplete();
                ToastUtil.showMiddle(getActivity(), R.string.no_resource);
            }
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
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        newsVo.clear();
    }

    @Override
    public void onLoadMoreRequested() {
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, newsVo.get(19).getSeq());
    }

    public void startActivity(Class activity,String key,String videoUrl) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        startActivity(intent);
    }
}
