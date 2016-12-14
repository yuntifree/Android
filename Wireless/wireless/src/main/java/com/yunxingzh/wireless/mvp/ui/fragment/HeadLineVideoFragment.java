package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
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
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.VideoPlayActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineVideoAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.bean.vo.HotInfo;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-视频
 */

public class HeadLineVideoFragment extends BaseFragment implements IHeadLineView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private final static int HEAD_LINE_TYPE = 1;// 0-新闻 1-视频 2-应用 3-游戏 5-东莞新闻
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int CLICK_COUNT = 0;//0- 视频播放 1-新闻点击 2-广告展示 3-广告点击 4-服务
    private final static int SECONDS = 60 * 1000;

    private LinearLayout mNetErrorVideoLay;
    private RecyclerView mListRv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineVideoAdapter headLineVideoAdapter;
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLay;
    private List<HotInfo> newsVo;
    private boolean isFirstRefresh = true;
    private long exitTime = 0;

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
        headLineVideoAdapter = new HeadLineVideoAdapter(new ArrayList<HotInfo>());
        headLineVideoAdapter.openLoadMore(Constants.PAGE_SIZE);
        headLineVideoAdapter.setOnLoadMoreListener(this);
        mListRv.setAdapter(headLineVideoAdapter);

        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        if (NetUtils.isNetworkAvailable(getActivity())) {
            iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        }
        netErrorLay();
        mListRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
                final List<HotInfo> data = baseQuickAdapter.getData();
                if (NetUtils.isWifi(getActivity())) {
                    if ((System.currentTimeMillis() - exitTime) > SECONDS) {
                        iHeadLinePresenter.clickCount(data.get(i).id, CLICK_COUNT);//上报
                        int s = data.get(i).play + 1;
                        exitTime = System.currentTimeMillis();
                    }
                    startActivity(VideoPlayActivity.class, Constants.VIDEO_URL, data.get(i).dst);
                } else {
                    final AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
                    mDialog.setTitle(R.string.dialog_notices);
                    mDialog.setMessage(R.string.dialog_msg);
                    mDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    mDialog.setPositiveButton(R.string.query, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (data != null) {
                                if ((System.currentTimeMillis() - exitTime) > SECONDS) {
                                    iHeadLinePresenter.clickCount(data.get(i).id, CLICK_COUNT);//上报
                                    exitTime = System.currentTimeMillis();
                                }
                                startActivity(VideoPlayActivity.class, Constants.VIDEO_URL, data.get(i).dst);
                            }
                        }
                    });
                    mDialog.create().show();
                }
            }
        });
    }

    @Override
    public void getHeadLineSuccess(HotInfoList newsVoList) {
        mSwipeRefreshLay.setRefreshing(false);
        if (newsVoList != null) {
            newsVo = newsVoList.infos;
            if (newsVoList.hasmore == 1) {
                if (isFirstRefresh){
                    isFirstRefresh = false;
                    headLineVideoAdapter.setNewData(newsVo);
                } else {
                    headLineVideoAdapter.addData(newsVo);
                }
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
    public void onRefresh() {
       // newsVo.clear();
        isFirstRefresh = true;
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
    }

    @Override
    public void onLoadMoreRequested() {
        if (newsVo != null) {
            iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, newsVo.get(newsVo.size() - 1).seq);
            LogUtils.e("lsd", newsVo.get(newsVo.size() - 1).seq + "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(getActivity())){
            EventBus.getDefault().unregister(getActivity());
        }
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsList weatherNewsVo) {}

    @Override
    public void getFontInfoSuccess(FontInfoList fontInfoVo) {}

    public void startActivity(Class activity,String key,String videoUrl) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        startActivity(intent);
    }

    public void netErrorLay(){
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
                        ToastUtil.showMiddle(getActivity(), R.string.net_set);
                    } else {
                        mNetErrorVideoLay.setVisibility(View.GONE);
                        mSwipeRefreshLay.setVisibility(View.VISIBLE);
                        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
                    }
                }
            });
        }
    }
}
