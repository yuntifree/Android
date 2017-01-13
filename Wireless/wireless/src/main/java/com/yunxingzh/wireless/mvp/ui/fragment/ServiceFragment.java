package com.yunxingzh.wireless.mvp.ui.fragment;

import android.content.Intent;
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
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mview.NetErrorLayout;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IServicePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.ServicePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.SearchActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IServiceView;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.ToastUtil;

import java.util.List;

import wireless.libs.bean.resp.ServiceList;
import wireless.libs.bean.vo.Service;

/**
 * Created by stephon_ on 2016/11/1.
 * 服务
 */

@SuppressWarnings("ResourceType")
public class ServiceFragment extends BaseFragment implements IServiceView, View.OnClickListener {

    private final static int CLICK_COUNT = 4;//0- 视频播放 1-新闻点击 2-广告展示 3-广告点击 4-服务

    private TextView mSearchTv;
    private TextView mServiceMoreTv, mHouseKeepingTv, mHousingTv, mSecondHandsTv, mCooperationTv;
    private LinearLayout mServiceParentGroup;
    private IServicePresenter iServicePresenter;
    private IHeadLinePresenter iHeadLinePresenter;
    private NetErrorLayout netErrorLayout;
    private WindowManager wm;

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
        mServiceMoreTv = findView(view, R.id.service_more_tv);
        mServiceMoreTv.setOnClickListener(this);
        mHouseKeepingTv = findView(view, R.id.housekeeping_tv);
        mHouseKeepingTv.setOnClickListener(this);
        mHousingTv = findView(view, R.id.housing_tv);
        mHousingTv.setOnClickListener(this);
        mSecondHandsTv = findView(view, R.id.second_hands_tv);
        mSecondHandsTv.setOnClickListener(this);
        mCooperationTv = findView(view, R.id.cooperation_tv);
        mCooperationTv.setOnClickListener(this);
        mSearchTv = findView(view, R.id.search_tv);
        mSearchTv.setOnClickListener(this);
    }

    public void initData() {
        iHeadLinePresenter = new HeadLinePresenterImpl();
        iServicePresenter = new ServicePresenterImpl(this);
        if (!NetUtils.isNetworkAvailable(getActivity())) {
            netErrorLayout = new NetErrorLayout(getActivity());
            final View netErrorView = netErrorLayout.netErrorLay(0);
            netErrorLayout.setOnNetErrorClickListener(new NetErrorLayout.OnNetErrorClickListener() {
                @Override
                public void netErrorClick() {
                    if (!NetUtils.isNetworkAvailable(getActivity())) {
                        ToastUtil.showMiddle(getActivity(), R.string.net_set);
                    } else {
                        netErrorView.setVisibility(View.GONE);
                        iServicePresenter.getService();
                    }
                }
            });
            mServiceParentGroup.addView(netErrorView, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 200, 0, 0));
        }
        iServicePresenter.getService();
    }

    @Override
    public void onClick(View v) {
        if (mCooperationTv == v) {//招聘
            if (iHeadLinePresenter != null) {
                iHeadLinePresenter.clickCount(1, CLICK_COUNT);//上报
            }
            startActivity(WebViewActivity.class, Constants.URL, "http://jump.luna.58.com/i/29Zk", Constants.TITLE, mCooperationTv.getText() + "");
        } else if (mSecondHandsTv == v) {//二手
            if (iHeadLinePresenter != null) {
                iHeadLinePresenter.clickCount(2, CLICK_COUNT);
            }
            startActivity(WebViewActivity.class, Constants.URL, "http://jump.luna.58.com/i/29Zl", Constants.TITLE, mSecondHandsTv.getText() + "");
        } else if (mHousingTv == v) {//租房
            if (iHeadLinePresenter != null) {
                iHeadLinePresenter.clickCount(3, CLICK_COUNT);
            }
            startActivity(WebViewActivity.class, Constants.URL, "http://jump.luna.58.com/i/29Zj", Constants.TITLE, mHousingTv.getText() + "");
        } else if (mHouseKeepingTv == v) {//家政
            if (iHeadLinePresenter != null) {
                iHeadLinePresenter.clickCount(4, CLICK_COUNT);
            }
            startActivity(WebViewActivity.class, Constants.URL, "http://jump.luna.58.com/i/29Zm", Constants.TITLE, mHouseKeepingTv.getText() + "");
        } else if (mServiceMoreTv == v) {//更多
            if (iHeadLinePresenter != null) {
                iHeadLinePresenter.clickCount(5, CLICK_COUNT);
            }
            startActivity(WebViewActivity.class, Constants.URL, "http://jump.luna.58.com/i/29Zn", Constants.TITLE, mServiceMoreTv.getText() + "");
        } else if (mSearchTv == v) {//搜索
            startActivity(SearchActivity.class, "", "", "", "");
        }
    }

    @Override
    public void getServiceListSuccess(ServiceList services) {
        List<Service> list = services.services;
        //获取屏幕宽高
        int width = 0;
        int height = 0;
        if (getActivity() == null) {
           return;
        }
        wm = getActivity().getWindowManager();
        width = wm.getDefaultDisplay().getWidth();//720,1536
        height = wm.getDefaultDisplay().getHeight();//1280,2560

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
                mServiceTitle.setTextColor(getResources().getColor(R.color.gray_3c3c3c));
            }
            mServiceTitle.setTextSize(17);
            mServiceTitle.setGravity(Gravity.CENTER);
            mServiceTitle.setText(list.get(i).title);

//            View lineSmall = new View(getActivity());
//            lineSmall.setMinimumHeight(1);
//            lineSmall.setBackgroundColor(getResources().getColor(R.color.gray_e6e6e6));

//            View line = new View(getActivity());
//            line.setBackgroundColor(getResources().getColor(R.color.gray_f5f5f5));

            if (width <= 720 && height <= 1280) {
               // line.setMinimumHeight(20);
                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 10, 30, 0, 20, 0, 5));
                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 20, 20, 0, 5));
            } else {
               // line.setMinimumHeight(40);
                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 20, 70, 0, 40, 0, 10));
                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 40, 40, 0, 10));
            }

            int size = list.get(i).items.size();
            List<Service.ServiceItem> childDatas = list.get(i).items;

            int num = ((size % 3) > 0) ? 1 : 0;
            int lines = size / 3 + num;//得到行数

            mServiceItem.addView(mItemTop);
          //  mServiceItem.addView(lineSmall, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, 1, 0, 0, 0, 0));

            for (int j = 0; j < lines; j++) {//循环行数
                LinearLayout childLay = new LinearLayout(getActivity());
                for (int k = 0; k < 3; k++) {//循环子item
                    LinearLayout itemLay = new LinearLayout(getActivity());
                    itemLay.setOrientation(LinearLayout.VERTICAL);
                    int positon = j * 3 + k;//得到item当前position
                    if (positon >= size) {//一行不足3个时填充空view
                        TextView nullView = new TextView(getActivity());
                        itemLay.addView(nullView, getLayoutParams(0,Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
                        childLay.addView(itemLay, getLayoutParams(1, 0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
                    } else {
                        String title = childDatas.get(positon).title;
                        String imgUrl = childDatas.get(positon).icon;
                        ImageView img = new ImageView(getActivity());
                        Glide.with(getActivity()).load(imgUrl).into(img);
                        TextView views = new TextView(getActivity());
                        views.setText(title);
                        views.setTextSize(14);
                        views.setGravity(Gravity.CENTER);
                        if (isAdded()) {
                            views.setTextColor(getResources().getColor(R.color.gray_5a5a5a));
                        }
                        itemLay.setTag(childDatas.get(positon));
                        itemLay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Service.ServiceItem dv = (Service.ServiceItem) v.getTag();
                                if (iHeadLinePresenter != null) {
                                    iHeadLinePresenter.clickCount(dv.sid, CLICK_COUNT);//上报
                                }
                                startActivity(WebViewActivity.class, Constants.URL, dv.dst, Constants.TITLE, dv.title);
                            }
                        });

                        if (width <= 720 && height <= 1280) {
                            itemLay.addView(img, getLayoutParams(0, Gravity.CENTER, 80, 80, 0, 0, 0, 0));
                        } else {
                            itemLay.addView(img, getLayoutParams(0, Gravity.CENTER, 120, 120, 0, 0, 0, 0));
                        }
                        itemLay.addView(views, getLayoutParams(0,Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));

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
//            if (i == list.size() - 1) {
//                return;
//            }
          //  mServiceItem.addView(line, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
        }
    }

    public void startActivity(Class activity, String key, String value, String titleKey, String titleValue) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, value);
        intent.putExtra(titleKey, titleValue);
        startActivity(intent);
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

}