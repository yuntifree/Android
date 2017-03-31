package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.BackToTopView;
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnDismissListener;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.WirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.VideoPlayActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineVideoAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.ScrollViewListener;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SPUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.vo.HotInfo;
import wireless.libs.bean.vo.User;

/**
 * Created by stephon_ on 2016/11/2.
 * 头条-视频
 */

public class HeadLineVideoFragment extends BaseFragment implements IHeadLineView, SwipeRefreshLayout.OnRefreshListener,
        NetErrorLayout.OnNetErrorClickListener {

    private final static int HEAD_LINE_TYPE = 1;// 0-新闻 1-视频 2-应用 3-游戏 4-本地 5-娱乐
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int CLICK_COUNT = 0;//0- 视频播放 1-新闻点击 2-广告展示 3-广告点击 4-服务
    private final static int SECONDS = 60 * 1000;

    private boolean isFastClick;
    private ListView mListLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private IWirelessPresenter iWirelessPresenter;
    private HeadLineVideoAdapter headLineVideoAdapter;
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLay;
    private List<HotInfo> newsVo;
    private HotInfoList infoList;
    private boolean count = false;
    private AlertView alertView;
    private LinearLayout mNetErrorLay;
    private NetErrorLayout netErrorLayout;
    private int itemId;
    private int countUmeng = 0;
    private FrameLayout mVideoListLay;
    private BackToTopView mBackTopIv;
    private View headerView;
    private TextView headerViewTitle;
    private ImageView headerViewImg;
    private boolean isFirstLoad = true;
    private int lastPosition;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mListLv = findView(view, R.id.video_lv);
        mSwipeRefreshLay = findView(view, R.id.swipe_refresh_news);
        mSwipeRefreshLay.setOnRefreshListener(this);
        mNetErrorLay = findView(view, R.id.net_error_lay);
        mVideoListLay = findView(view, R.id.video_list_lay);

        mBackTopIv = findView(view, R.id.back_top_iv);

        if (isAdded() && getActivity() != null) {
            headerView = View.inflate(getActivity(), R.layout.list_item_videos, null);
            ImageView playIv = (ImageView) headerView.findViewById(R.id.video_play);
            playIv.setVisibility(View.INVISIBLE);
            TextView timeTv = (TextView) headerView.findViewById(R.id.video_time);
            timeTv.setVisibility(View.INVISIBLE);
            TextView countTv = (TextView) headerView.findViewById(R.id.video_play_count);
            countTv.setVisibility(View.INVISIBLE);

            headerViewTitle = (TextView) headerView.findViewById(R.id.video_title);
            headerViewImg = (ImageView) headerView.findViewById(R.id.video_img);
        }
    }

    public void initData() {
        mListLv.setOnScrollListener(scrollViewListener);
        mListLv.addHeaderView(headerView);
        mBackTopIv.setListView(mListLv, Constants.MY_PAGE_SIZE / 6, scrollViewListener);
        mListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (newsVo != null) {
                    final HotInfo hotInfo = (HotInfo) parent.getAdapter().getItem(position);//添加header之后需要这样获取当前点击的item
                    if (isAdded() && getActivity() != null && hotInfo != null && itemId != hotInfo.id) {
                        countUmeng++;
                        if (countUmeng == 3) {
                            MobclickAgent.onEvent(getActivity(), "video_triple_view");
                        }
                        if (countUmeng == 5) {
                            MobclickAgent.onEvent(getActivity(), "video_penta_view");
                        }
                    }
                    itemId = hotInfo.id;
                    if (isAdded() && getActivity() != null) {
                        if (!NetUtils.isWifi(getActivity())) {
                            alertView = new AlertView("温馨提示", "亲,您当前处于流量状态下,继续观看需要花费少许流量哦!", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, new com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    if (position != AlertView.CANCELPOSITION) {
                                        count = false;
                                        videoItemClick(hotInfo, position);
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
                            count = false;
                            videoItemClick(hotInfo, position);
                        }
                    }
                }
            }
        });
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iWirelessPresenter = new WirelessPresenterImpl(this);
        if (isAdded() && getActivity() != null) {
            if (NetUtils.isNetworkAvailable(getActivity())) {
                iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
            }
            if (!NetUtils.isNetworkAvailable(getActivity())) {
                mVideoListLay.setVisibility(View.GONE);
                netErrorLayout = new NetErrorLayout(getActivity());
                netErrorLayout.setOnNetErrorClickListener(this);
                mNetErrorLay.setVisibility(View.VISIBLE);
                View netErrorView = netErrorLayout.netErrorLay(0);
                mNetErrorLay.addView(netErrorView);
            }
        }
    }

    private void videoItemClick(HotInfo item, int position) {
        if (!count) {
            count = true;
            if (lastPosition != position) {
                item.play++;
                lastPosition = position;
            }
        }
        if (headLineVideoAdapter != null) {
            headLineVideoAdapter.notifyDataSetChanged();
        }
        iWirelessPresenter.clickCount(item.id, CLICK_COUNT, "");//上报
        startActivity(VideoPlayActivity.class, Constants.VIDEO_URL, item.dst);
    }

    @Override
    public void getHeadLineSuccess(HotInfoList newsVoList) {
        mSwipeRefreshLay.setRefreshing(false);
        isFastClick = true;
        if (isAdded() && getActivity() != null) {
            if (newsVoList != null) {
                infoList = newsVoList;
                if (newsVo == null) {
                    newsVo = new ArrayList<HotInfo>();
                }

                if (infoList.infos != null) {
                    newsVo.addAll(infoList.infos);
                    if (isAdded() && getActivity() != null && headLineVideoAdapter == null) {
                        headLineVideoAdapter = new HeadLineVideoAdapter(getActivity(), newsVo);
                        mListLv.setAdapter(headLineVideoAdapter);
                    }
                    headLineVideoAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showMiddle(getActivity(), R.string.no_resource);
                }

                if (isFirstLoad) {//如果第一次进来就为空则隐藏headerview
                    isFirstLoad = false;
                    if (newsVoList.top == null) {
                        mListLv.removeHeaderView(headerView);
                    }
                }

                if (infoList.top != null) {
                    SPUtils.putObject(getActivity(), "headViewData", infoList.top);
                    final HotInfoList.TopVo topVo = (HotInfoList.TopVo) SPUtils.getObject(getActivity(), "headViewData");
                    if (topVo != null) {
                        headerViewTitle.setText(topVo.title);
                        Glide.with(getActivity()).load(topVo.img).placeholder(R.drawable.img_default).into(headerViewImg);
                    }
                    headerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (topVo != null) {
                                MobclickAgent.onEvent(getActivity(), "video_360");
                                startActivity(WebViewActivity.class, Constants.URL, topVo.dst);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void getHeadLineFaild() {
        if (mSwipeRefreshLay != null) {
            mSwipeRefreshLay.setRefreshing(false);
        }
        if (isAdded() && getActivity() != null) {
            ToastUtil.showMiddle(getActivity(), R.string.net_error);

            if (netErrorLayout == null) {
                mVideoListLay.setVisibility(View.GONE);
                netErrorLayout = new NetErrorLayout(getActivity());
                netErrorLayout.setOnNetErrorClickListener(this);
                mNetErrorLay.setVisibility(View.VISIBLE);
                View netErrorView = netErrorLayout.netErrorLay(0);
                mNetErrorLay.addView(netErrorView);
            } else {
                mVideoListLay.setVisibility(View.GONE);
                mNetErrorLay.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh() {
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        if (newsVo != null && newsVo.size() > 0) {
            newsVo.clear();
        }
    }

    @Override
    public void netErrorClick() {
        if (isAdded() && getActivity() != null) {
            if (!NetUtils.isNetworkAvailable(getActivity())) {
                ToastUtil.showMiddle(getActivity(), R.string.net_set);
            } else {
                mNetErrorLay.setVisibility(View.GONE);
                mVideoListLay.setVisibility(View.VISIBLE);
                iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
            }
        }
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
                iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
            }
        });
        return netView;
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
    public void onDestroyView() {
        super.onDestroyView();
        if (iHeadLinePresenter != null && iWirelessPresenter != null) {
            iHeadLinePresenter.onDestroy();
            iWirelessPresenter.onDestroy();
        }
        if (newsVo != null) {
            newsVo.clear();
        }
        if (isAdded() && getActivity() != null) {
            if (EventBus.getDefault().isRegistered(getActivity())) {
                EventBus.getDefault().unregister(getActivity());
            }
        }
    }

    public void startActivity(Class activity, String key, String videoUrl) {
        if (isAdded() && getActivity() != null) {
            Intent intent = new Intent(getActivity(), activity);
            intent.putExtra(key, videoUrl);
            startActivity(intent);
        }
    }

    public OnScrollListener scrollViewListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 当不滚动时
                case OnScrollListener.SCROLL_STATE_IDLE:
                    // 判断滚动到底部
                    if (mListLv.getLastVisiblePosition() == (mListLv.getCount() - 1)) {
                        if (isAdded() && getActivity() != null && infoList.hasmore == 0) {
                            ToastUtil.showMiddle(getActivity(), R.string.no_resourse);
                        } else {
                            if (isFastClick) {
                                isFastClick = false;
                                if (infoList != null && infoList.infos.size() > 0) {
                                    iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, infoList.infos.get(infoList.infos.size() - 1).seq);
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
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {

        }
    };

}
