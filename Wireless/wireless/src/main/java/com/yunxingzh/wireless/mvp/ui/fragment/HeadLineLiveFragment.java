package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.BackToTopView;
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.WirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.LiveWebViewActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineLiveAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IGetLiveListView;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.LiveList;
import wireless.libs.bean.vo.LiveVo;

/**
 * Created by stephen on 2017/2/8.
 * 头条-直播
 */

public class HeadLineLiveFragment extends BaseFragment implements IGetLiveListView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        NetErrorLayout.OnNetErrorClickListener {

    private final static int LIVE_TYPE = 7;

    private RecyclerView mListRv;
    private SwipeRefreshLayout mSwipeRefreshLay;
    private IHeadLinePresenter iHeadLinePresenter;
    private IWirelessPresenter iWirelessPresenter;
    private HeadLineLiveAdapter headLineLiveAdapter;
    private boolean isFirstRefresh = true;
    private List<LiveVo> liveVos;
    private LinearLayout mNetErrorLay;
    private NetErrorLayout netErrorLayout;
    private long liveId;
    private int countThree = 0;
    private FrameLayout mLiveListLay;
    private BackToTopView mBackTopIv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mListRv = findView(view, R.id.live_rv);
        mSwipeRefreshLay = findView(view, R.id.swipe_refresh_live);
        mSwipeRefreshLay.setColorSchemeResources(R.color.blue_009CFB);
        mSwipeRefreshLay.setOnRefreshListener(this);
        if (isAdded() && getActivity() != null) {
            mListRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        mListRv.setHasFixedSize(true);
        mListRv.addItemDecoration(new SpacesItemDecoration(4));
        mNetErrorLay = findView(view, R.id.net_error_lay);
        mLiveListLay = findView(view, R.id.live_list_lay);

        mBackTopIv = findView(view, R.id.back_top_iv);
        mBackTopIv.setRecyclerView(mListRv, Constants.MY_PAGE_SIZE / 6);

    }

    public void initData() {
        headLineLiveAdapter = new HeadLineLiveAdapter(new ArrayList<LiveVo>());
        headLineLiveAdapter.setOnLoadMoreListener(this, mListRv);
       // headLineLiveAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        // headLineLiveAdapter.setEmptyView(emptyView(mListRv));
        mListRv.setAdapter(headLineLiveAdapter);

        iWirelessPresenter = new WirelessPresenterImpl(this);
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iHeadLinePresenter.getLiveList(0);
        if (isAdded() && getActivity() != null) {
            if (!NetUtils.isNetworkAvailable(getActivity())) {
                mLiveListLay.setVisibility(View.GONE);
                netErrorLayout = new NetErrorLayout(getActivity());
                netErrorLayout.setOnNetErrorClickListener(this);
                mNetErrorLay.setVisibility(View.VISIBLE);
                View netErrorView = netErrorLayout.netErrorLay(0);
                mNetErrorLay.addView(netErrorView);
            }
        }
        headLineLiveAdapter.setOnLiveItemClickListener(new HeadLineLiveAdapter.onLiveItemClickListener() {
            @Override
            public void onItemClick(LiveVo liveVo) {
                if (iWirelessPresenter != null) {
                    iWirelessPresenter.clickCount(0, LIVE_TYPE, "livedetail");
                }
                if (isAdded() && getActivity() != null && liveVo != null) {
                    if (liveId != liveVo.live_id) {
                        countThree++;
                        if (countThree == 3) {
                            MobclickAgent.onEvent(getActivity(), "stream_triple_view");
                        }
                        if (countThree == 5) {
                            MobclickAgent.onEvent(getActivity(), "stream_penta_view");
                        }
                    }
                    liveId = liveVo.live_id;
                    Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
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
            if (liveVos != null && liveList.hasmore == 1) {
                if (isFirstRefresh) {
                    isFirstRefresh = false;
                    headLineLiveAdapter.setNewData(liveVos);
                } else {
                    headLineLiveAdapter.addData(liveVos);
                    headLineLiveAdapter.loadMoreComplete();
                }
            } else {
                // 数据全部加载完毕就调用 loadMoreEnd
                headLineLiveAdapter.loadMoreEnd();
                if (isAdded() && getActivity() != null) {
                    ToastUtil.showMiddle(getActivity(), R.string.no_resource);
                }
            }
        } else {
            if (isAdded() && getActivity() != null) {
                ToastUtil.showMiddle(getActivity(), R.string.re_error);
            }
        }
    }

    @Override
    public void getLiveListFaild() {
        if (mSwipeRefreshLay != null) {
            mSwipeRefreshLay.setRefreshing(false);
        }
        if (isAdded() && getActivity() != null) {
            ToastUtil.showMiddle(getActivity(), R.string.net_error);

            if (netErrorLayout == null) {
                mLiveListLay.setVisibility(View.GONE);
                netErrorLayout = new NetErrorLayout(getActivity());
                netErrorLayout.setOnNetErrorClickListener(this);
                mNetErrorLay.setVisibility(View.VISIBLE);
                View netErrorView = netErrorLayout.netErrorLay(0);
                mNetErrorLay.addView(netErrorView);
            } else {
                mLiveListLay.setVisibility(View.GONE);
                mNetErrorLay.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh() {
        isFirstRefresh = true;
        iHeadLinePresenter.getLiveList(0);
    }

    @Override
    public void onLoadMoreRequested() {
        if (iHeadLinePresenter != null && liveVos != null) {
            iHeadLinePresenter.getLiveList(liveVos.get(liveVos.size() - 1).seq);
        }
    }

    @Override
    public void netErrorClick() {
        if (isAdded() && getActivity() != null) {
            if (!NetUtils.isNetworkAvailable(getActivity())) {
                ToastUtil.showMiddle(getActivity(), R.string.net_set);
            } else {
                mNetErrorLay.setVisibility(View.GONE);
                mLiveListLay.setVisibility(View.VISIBLE);
                isFirstRefresh = true;
                iHeadLinePresenter.getLiveList(0);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.get(MainApplication.get()).clearMemory();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Glide.get(MainApplication.get()).clearMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iWirelessPresenter != null && iHeadLinePresenter != null) {
            iWirelessPresenter.onDestroy();
            iHeadLinePresenter.onDestroy();
        }
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    private View emptyView(ViewGroup viewGroup) {
        if (!isAdded() && getActivity() == null) {
            return null;
        }
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
