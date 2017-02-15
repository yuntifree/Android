package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
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
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnDismissListener;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.IWirelessPresenterImpl;
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

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.vo.HotInfo;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-视频
 */

public class HeadLineVideoFragment extends BaseFragment implements IHeadLineView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private final static int HEAD_LINE_TYPE = 1;// 0-新闻 1-视频 2-应用 3-游戏 4-本地 5-娱乐
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int CLICK_COUNT = 0;//0- 视频播放 1-新闻点击 2-广告展示 3-广告点击 4-服务
    private final static int SECONDS = 60 * 1000;

    private RecyclerView mListRv;
    private IHeadLinePresenter iHeadLinePresenter;
    private IWirelessPresenter iWirelessPresenter;
    private HeadLineVideoAdapter headLineVideoAdapter;
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLay;
    private List<HotInfo> newsVo;
    private boolean isFirstRefresh = true;
    private boolean count = false;
    private AlertView alertView;

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
    }

    public void initData() {
        headLineVideoAdapter = new HeadLineVideoAdapter(new ArrayList<HotInfo>());
        headLineVideoAdapter.openLoadMore(Constants.PAGE_SIZE);
        headLineVideoAdapter.setOnLoadMoreListener(this);
        headLineVideoAdapter.setEmptyView(emptyView(mListRv));
        mListRv.setAdapter(headLineVideoAdapter);

        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iWirelessPresenter = new IWirelessPresenterImpl(this);
        if (NetUtils.isNetworkAvailable(getActivity())) {
            iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        }

        mListRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
                List<HotInfo> data = baseQuickAdapter.getData();
                // TODO: 临时用final来满足dialog的要求，找更好的方法替代
                final List<HotInfo> data2 = data;
                HotInfo item = data.get(i);

                if (!NetUtils.isWifi(getActivity())) {
                    alertView = new AlertView("温馨提示", "亲,您当前处于流量状态下,继续观看需要花费少许流量哦!", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, new com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position != AlertView.CANCELPOSITION) {
                                HotInfo item = data2.get(i);
                                videoItemClick(item, i);
                            }
                        }
                    }).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            if (alertView != null) {
                                alertView.dismiss();
                            }
                        }
                    });
                    alertView.show();
                } else {
                    videoItemClick(item, i);
                }
            }
        });
    }

    private void videoItemClick(HotInfo item, int position) {
        if (!count) {
            count = true;
            item.play++;
        }
        headLineVideoAdapter.notifyItemChanged(position);
        iWirelessPresenter.clickCount(item.id, CLICK_COUNT);//上报
        startActivity(VideoPlayActivity.class, Constants.VIDEO_URL, item.dst);
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
    public void getHeadLineFaild() {
        if (mSwipeRefreshLay != null) {
            mSwipeRefreshLay.setRefreshing(false);
        }
        ToastUtil.showMiddle(getActivity(), R.string.net_error);
    }

    @Override
    public void onRefresh() {
       // newsVo.clear();
        isFirstRefresh = true;
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
    }

    @Override
    public void onLoadMoreRequested() {
        if (iHeadLinePresenter != null && newsVo != null) {
            iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, newsVo.get(newsVo.size() - 1).seq);
        }
    }

    private View emptyView(ViewGroup viewGroup) {
        View netView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, viewGroup, false);
        TextView netErrorBtn = (TextView) netView.findViewById(R.id.video_net_error_btn);
        netErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstRefresh = true;
                iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
            }
        });
        return netView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(getActivity())){
            EventBus.getDefault().unregister(getActivity());
        }
    }

    public void startActivity(Class activity,String key,String videoUrl) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        startActivity(intent);
    }

}
