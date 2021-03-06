package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mvp.presenter.IServicePresenter;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.ServicePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.WirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.SearchActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.NetworkImageHolderView;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IServiceView;
import com.yunxingzh.wireless.utils.AppUtils;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.FindList;
import wireless.libs.bean.vo.Service;
import wireless.libs.convenientbanner.ConvenientBanner;
import wireless.libs.convenientbanner.holder.CBViewHolderCreator;
import wireless.libs.convenientbanner.listener.OnItemClickListener;

/**
 * Created by stephon_ on 2016/11/1.
 * 服务
 */

@SuppressWarnings("ResourceType")
public class ServiceFragment extends BaseFragment implements IServiceView, View.OnClickListener {

    private final static int CLICK_COUNT = 4;//0- 视频播放 1-新闻点击 2-广告展示 3-广告点击 4-服务

    private TextView mSearchTv;
    private LinearLayout mServiceParentGroup, mServiceCityLay, mServiceCityItem, mServiceRecommendLay;
    private IServicePresenter iServicePresenter;
    private IWirelessPresenter iWirelessPresenter;
    private NetErrorLayout netErrorLayout;
    private WindowManager wm;
    private View mServiceLine;
    private ImageView mServiceRecommendIv, mServiceCityIv;
    private ConvenientBanner mBannerAdvert, mBannerRecommend;
    private List<FindList.FindBannerVo> advertBannerVo;
    private List<FindList.RecommendVo> recommendsBannerVo;
    private List<FindList.CityServiceVo> cityServiceVos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mServiceParentGroup = findView(view, R.id.service_parent_group);
        mSearchTv = findView(view, R.id.search_tv);
        mSearchTv.setOnClickListener(this);
        mServiceCityLay = findView(view, R.id.service_city_lay);
        mServiceRecommendIv = findView(view, R.id.service_recommend_iv);
        mServiceCityIv = findView(view, R.id.service_city_iv);
        mBannerAdvert = findView(view, R.id.banner_advert);
        mBannerRecommend = findView(view, R.id.banner_recommend);
        mServiceCityItem = findView(view, R.id.service_city_item);
        mServiceRecommendLay = findView(view, R.id.service_recommend_lay);
        mServiceLine = findView(view, R.id.service_line);
    }

    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        mBannerAdvert.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        mBannerRecommend.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});

        iWirelessPresenter = new WirelessPresenterImpl(this);
        iServicePresenter = new ServicePresenterImpl(this);
        if (isAdded() && getActivity() != null) {
            if (!NetUtils.isNetworkAvailable(getActivity())) {
                netErrorState();
            }
        }
        iServicePresenter.getFind();
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        if (event.getMsg() == Constants.NET_ERROR) {//网络不可用（无法上网）
            netErrorState();
        }
    }

    @Override
    public void onClick(View v) {
        if (isAdded() && getActivity() != null) {
            if (mSearchTv == v) {//搜索
                MobclickAgent.onEvent(getActivity(), "life_search");
                startActivity(SearchActivity.class, "", "", "", "");
            }
        }
    }

    @Override
    public void getFindSuccess(FindList findList) {
        //获取屏幕宽高
        int width = 0;
        int height = 0;
        if (!isAdded() && getActivity() == null || findList == null) {
            return;
        }

        viewVisibile(true);

        wm = getActivity().getWindowManager();
        width = wm.getDefaultDisplay().getWidth();//720,1536
        height = wm.getDefaultDisplay().getHeight();//1280,2560
        /***
         * 顶部广告
         */
        if (advertBannerVo != null && advertBannerVo.size() > 0) {//防止数据重复
            advertBannerVo.clear();
        }
        advertBannerVo = findList.banners;
        if (advertBannerVo != null) {
            int advertSize = advertBannerVo.size();
            bannersState(advertSize, mBannerAdvert);
            List<String> imageList = new ArrayList<String>(advertSize);
            for (FindList.FindBannerVo bannersList : advertBannerVo) {
                imageList.add(bannersList.img);
            }
            setBannerPages(mBannerAdvert, imageList);
            //banner图跳转
            mBannerAdvert.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (iWirelessPresenter != null) {
                        iWirelessPresenter.clickCount(advertBannerVo.get(position).id, 10, "");//上报
                    }
                    String url = advertBannerVo.get(position).dst;
                    int type = advertBannerVo.get(position).type;
                    if (!StringUtils.isEmpty(url)) {
                        if (url.trim().equals("dgnews") && type == 1) {
                            WirelessFragment.localClick = true;
                            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
                        } else {
                            startActivity(WebViewActivity.class, Constants.URL, url, "", "");
                        }
                    }
                }
            });
        } else {
            mBannerAdvert.setVisibility(View.GONE);
        }

        /***
         * 精品推荐
         */
        if (recommendsBannerVo != null && recommendsBannerVo.size() > 0) {//防止数据重复
            recommendsBannerVo.clear();
        }
        recommendsBannerVo = findList.recommends;
        if (recommendsBannerVo != null) {
            int recomSize = recommendsBannerVo.size();
            bannersState(recomSize, mBannerRecommend);
            List<String> recomList = new ArrayList<String>(recomSize);
            for (FindList.RecommendVo bannersList : recommendsBannerVo) {
                recomList.add(bannersList.img);
            }
            setBannerPages(mBannerRecommend, recomList);
            //banner图跳转
            mBannerRecommend.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (iWirelessPresenter != null) {
                        iWirelessPresenter.clickCount(recommendsBannerVo.get(position).id, 11, "");//上报
                    }
                    String url = recommendsBannerVo.get(position).dst;
                    if (!StringUtils.isEmpty(url)) {
                        startActivity(WebViewActivity.class, Constants.URL, url, "", "");
                    }
                }
            });
        } else {
            mServiceRecommendLay.setVisibility(View.GONE);
            mServiceLine.setVisibility(View.GONE);
            mBannerRecommend.setVisibility(View.GONE);
        }
        /***
         * 城市服务
         */
        if (cityServiceVos != null && cityServiceVos.size() > 0) {
            cityServiceVos.clear();
        }
        cityServiceVos = findList.urbanservices;
        if (findList.urbanservices != null && findList.urbanservices.size() > 0) {
            for (int i = 0; i < cityServiceVos.size(); i++) {

                LinearLayout cityLay = new LinearLayout(getActivity());
                cityLay.setOrientation(LinearLayout.VERTICAL);

                ImageView cityIv = new ImageView(getActivity());
                Glide.with(getActivity()).load(cityServiceVos.get(i).img).into(cityIv);

                TextView cityView = new TextView(getActivity());
                cityView.setGravity(Gravity.CENTER);
                cityView.setText(cityServiceVos.get(i).title);
                cityView.setTextColor(Color.parseColor("#3c3c3c"));
                cityView.setTextSize(14);

                if (width <= 720 && height <= 1280) {
                    mServiceCityIv.setLayoutParams(getLayoutParams(0, Gravity.CENTER, 10, 30, 20, 7, 0, 5));
                    mServiceRecommendIv.setLayoutParams(getLayoutParams(0, Gravity.CENTER, 10, 30, 20, 7, 0, 5));
                    cityLay.addView(cityIv, getLayoutParams(0, Gravity.CENTER, 88, 88, 0, 0, 0, 20));
                } else {
                    mServiceCityIv.setLayoutParams(getLayoutParams(0, Gravity.CENTER, 20, 60, 40, 14, 0, 10));
                    mServiceRecommendIv.setLayoutParams(getLayoutParams(0, Gravity.CENTER, 20, 60, 40, 14, 0, 10));
                    cityLay.addView(cityIv, getLayoutParams(0, Gravity.CENTER, 140, 140, 0, 0, 0, 40));
                }
                cityLay.addView(cityView);

                cityLay.setTag(cityServiceVos.get(i));
                cityLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.animation(v);
                        FindList.CityServiceVo cityServiceVo = (FindList.CityServiceVo) v.getTag();
                        if (iWirelessPresenter != null) {
                            iWirelessPresenter.clickCount(cityServiceVo.id, 12, "");//上报
                        }
                        //友盟上报
                        switch (cityServiceVo.title) {
                            case "更多":
                                MobclickAgent.onEvent(getActivity(), "life_more");
                                break;
                            case "招聘":
                                MobclickAgent.onEvent(getActivity(), "life_recruit");
                                break;
                            case "租房":
                                MobclickAgent.onEvent(getActivity(), "life_rental");
                                break;
                            case "二手":
                                MobclickAgent.onEvent(getActivity(), "life_secondhand");
                                break;
                            case "家政":
                                MobclickAgent.onEvent(getActivity(), "life_service");
                                break;
                        }
                        startActivity(WebViewActivity.class, Constants.URL, cityServiceVo.dst, Constants.TITLE, cityServiceVo.title);
                    }
                });
                mServiceCityLay.addView(cityLay, getLayoutParams(1, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
            }
        } else {
            mServiceCityItem.setVisibility(View.GONE);
            mServiceCityLay.setVisibility(View.GONE);
        }

        /***
         * 服务项
         */
        List<Service> list = findList.services;
        for (int i = 0; i < list.size(); i++) {
            LinearLayout mServiceItem = new LinearLayout(getActivity());//item最外层layout
            if (isAdded()) {
                mServiceItem.setBackgroundColor(getResources().getColor(R.color.white));
            }
            mServiceItem.setOrientation(LinearLayout.VERTICAL);

            LinearLayout mItemTop = new LinearLayout(getActivity());//item顶部layout
            mItemTop.setOrientation(LinearLayout.HORIZONTAL);

            ImageView mServiceImg = new ImageView(getActivity());
            mServiceImg.setImageResource(R.drawable.img_service);

            TextView mServiceTitle = new TextView(getActivity());
            if (isAdded()) {
                mServiceTitle.setTextColor(getResources().getColor(R.color.gray_5a5a5a));
            }
            mServiceTitle.setTextSize(17);
            mServiceTitle.setGravity(Gravity.CENTER);
            mServiceTitle.setText(list.get(i).title);

//            View lineSmall = new View(getActivity());
//            lineSmall.setMinimumHeight(1);
//            lineSmall.setBackgroundColor(getResources().getColor(R.color.gray_e6e6e6));

            View line = new View(getActivity());
            line.setBackgroundColor(getResources().getColor(R.color.gray_f5f5f5));

            if (width <= 720 && height <= 1280) {
                line.setMinimumHeight(25);
                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 10, 30, 20, 20, 0, 5));
                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 10, 20, 0, 5));
            } else {
                line.setMinimumHeight(40);
                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 20, 60, 40, 40, 0, 10));
                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 20, 40, 0, 10));
            }

            int size = list.get(i).items.size();
            List<Service.ServiceItem> childDatas = list.get(i).items;

            int num = ((size % 4) > 0) ? 1 : 0;
            int lines = size / 4 + num;//得到行数

            mServiceItem.addView(mItemTop);
            //  mServiceItem.addView(lineSmall, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, 1, 0, 0, 0, 0));

            for (int j = 0; j < lines; j++) {//循环行数
                LinearLayout childLay = new LinearLayout(getActivity());//每一行的容器
                for (int k = 0; k < 4; k++) {//循环子item
                    LinearLayout itemLay = new LinearLayout(getActivity());
                    itemLay.setBackground(getResources().getDrawable(R.drawable.view_selector));
                    itemLay.setOrientation(LinearLayout.VERTICAL);
                    int positon = j * 4 + k;//得到item当前position
                    if (positon >= size) {//一行不足4个时填充空view
                        TextView nullView = new TextView(getActivity());
                        itemLay.addView(nullView, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
                        childLay.addView(itemLay, getLayoutParams(1, 0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
                    } else {
                        String title = childDatas.get(positon).title;
                        String imgUrl = childDatas.get(positon).icon;
                        ImageView img = new ImageView(getActivity());
                        Glide.with(getActivity()).load(imgUrl).into(img);
                        TextView views = new TextView(getActivity());
                        views.setText(title);
                        views.setTextSize(12);
                        views.setGravity(Gravity.CENTER);
                        if (isAdded()) {
                            views.setTextColor(getResources().getColor(R.color.gray_5a5a5a));
                        }
                        itemLay.setTag(childDatas.get(positon));
                        itemLay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Service.ServiceItem dv = (Service.ServiceItem) v.getTag();
                                if (iWirelessPresenter != null) {
                                    iWirelessPresenter.clickCount(dv.sid, CLICK_COUNT, "");//上报
                                }
                                //友盟上报
                                switch (dv.title) {
                                    case "公交查询":
                                        MobclickAgent.onEvent(getActivity(), "life_bus");
                                        break;
                                    case "汽车票":
                                        MobclickAgent.onEvent(getActivity(), "life_busticket");
                                        break;
                                    case "预约挂号":
                                        MobclickAgent.onEvent(getActivity(), "life_docappointment");
                                        break;
                                    case "医院查询":
                                        MobclickAgent.onEvent(getActivity(), "life_hospitical");
                                        break;
                                    case "积分入户":
                                        MobclickAgent.onEvent(getActivity(), "life_integralhome");
                                        break;
                                    case "积分入学":
                                        MobclickAgent.onEvent(getActivity(), "life_integralstudy");
                                        break;
                                    case "发票真伪":
                                        MobclickAgent.onEvent(getActivity(), "life_invoice");
                                        break;
                                    case "违章查询":
                                        MobclickAgent.onEvent(getActivity(), "life_peccancy");
                                        break;
                                    case "飞机票":
                                        MobclickAgent.onEvent(getActivity(), "life_planeticket");
                                        break;
                                    case "社保查询":
                                        MobclickAgent.onEvent(getActivity(), "life_socialinsurance");
                                        break;
                                    case "火车票":
                                        MobclickAgent.onEvent(getActivity(), "life_trainticket");
                                        break;
                                }

                                startActivity(WebViewActivity.class, Constants.URL, dv.dst, Constants.TITLE, dv.title);
                            }
                        });

                        if (width <= 720 && height <= 1280) {
                            itemLay.addView(img, getLayoutParams(0, Gravity.CENTER, 80, 80, 0, 0, 0, 0));
                        } else {
                            itemLay.addView(img, getLayoutParams(0, Gravity.CENTER, 120, 120, 0, 0, 0, 0));
                        }
                        itemLay.addView(views, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));

                        childLay.addView(itemLay, getLayoutParams(1, 0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
                    }
                }

                if (width <= 720 && height <= 1280) {
                    getLines(lines, mServiceItem, j, childLay, 40, 50);
                } else {
                    getLines(lines, mServiceItem, j, childLay, 80, 90);
                }
            }
            mServiceParentGroup.addView(mServiceItem);

            if (i == list.size() - 1) {
                return;
            }
            mServiceItem.addView(line, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
        }
    }

    public void viewVisibile(boolean isVisibile) {
        //顶部view（城市服务，精品推荐等）
        mServiceCityItem.setVisibility(isVisibile ? View.VISIBLE : View.GONE);
        mServiceCityLay.setVisibility(isVisibile ? View.VISIBLE : View.GONE);
        mServiceRecommendLay.setVisibility(isVisibile ? View.VISIBLE : View.GONE);
        mServiceLine.setVisibility(isVisibile ? View.VISIBLE : View.GONE);
        mBannerRecommend.setVisibility(isVisibile ? View.VISIBLE : View.GONE);
    }

    public void bannersState(int size, ConvenientBanner banner) {
        if (size > 1) {
            banner.setCanLoop(true);
            banner.setPointViewVisible(true);
            banner.startTurning(2000);
        } else {
            banner.setCanLoop(false);
            banner.setPointViewVisible(false);
        }
    }

    public void setBannerPages(ConvenientBanner banner, List<String> imgList) {
        banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgList);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBannerAdvert.stopTurning();
        mBannerRecommend.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recommendsBannerVo != null && recommendsBannerVo.size() > 0) {
            bannersState(recommendsBannerVo.size(), mBannerRecommend);
        }
        if (advertBannerVo != null && advertBannerVo.size() > 0) {
            bannersState(advertBannerVo.size(), mBannerAdvert);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iServicePresenter != null && iWirelessPresenter != null) {
            iServicePresenter.onDestroy();
            iWirelessPresenter.onDestroy();
        }
        if (mServiceParentGroup != null) {
            mServiceParentGroup.removeAllViews();
        }
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    public void startActivity(Class activity, String key, String value, String titleKey, String titleValue) {
        if (isAdded() && getActivity() != null) {
            Intent intent = new Intent(getActivity(), activity);
            intent.putExtra(key, value);
            intent.putExtra(titleKey, titleValue);
            startActivity(intent);
        }
    }

    public LinearLayout.LayoutParams getLayoutParams(int weight, int isGravity, int width, int height, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.gravity = isGravity;
        lp.weight = weight;
        lp.setMargins(left, top, right, bottom);
        return lp;
    }

    public void getLines(int lines, LinearLayout serviceItem, int j, LinearLayout childLay, int top, int bottom) {
        if (lines == 1) {//只有一行
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, top, 0, bottom));
        } else if (lines == 2 && j == 0) {
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, top, 0, bottom));
        } else if (lines == 2 && j == 1) {
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, bottom));
        } else if (lines == 3) {
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
        }
    }

    private void netErrorState() {
        if (isAdded() && getActivity() != null && netErrorLayout == null) {
            viewVisibile(false);
            netErrorLayout = new NetErrorLayout(getActivity());
            final View netErrorView = netErrorLayout.netErrorLay(0);
            netErrorLayout.setOnNetErrorClickListener(new NetErrorLayout.OnNetErrorClickListener() {
                @Override
                public void netErrorClick() {
                    if (!NetUtils.isNetworkAvailable(getActivity())) {
                        ToastUtil.showMiddle(getActivity(), R.string.net_set);
                    } else {
                        netErrorLayout = null;
                        if (mServiceParentGroup != null) {
                            mServiceParentGroup.removeAllViews();
                        }
                        netErrorView.setVisibility(View.GONE);
                        iServicePresenter.getFind();
                    }
                }
            });

            mServiceParentGroup.addView(netErrorView, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 200, 0, 0));
        }
    }

}