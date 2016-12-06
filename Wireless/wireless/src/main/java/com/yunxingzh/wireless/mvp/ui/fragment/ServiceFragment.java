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
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.IServicePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.ServicePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.SearchActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.view.IServiceView;

import java.util.List;

import wireless.libs.bean.Service;
import wireless.libs.bean.resp.ServiceList;
import wireless.libs.bean.vo.ServiceVo;

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
    //private List<ServiceVo.DataVo.ServiceData> dataVoList;

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
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        iServicePresenter = new ServicePresenterImpl(this);
        iServicePresenter.getService();
        if (!NetUtils.isNetworkAvailable(getActivity())){
            final View netView = LayoutInflater.from(getActivity()).inflate(R.layout.wifi_closed, null);
            TextView openTv = (TextView) netView.findViewById(R.id.net_open_tv);
            TextView contentTv = (TextView) netView.findViewById(R.id.net_content_tv);
            TextView refreshBtn = (TextView) netView.findViewById(R.id.open_wifi_btn);
            openTv.setVisibility(View.GONE);
            contentTv.setText(R.string.network_error);
            refreshBtn.setText(R.string.refresh_net);
            mServiceParentGroup.addView(netView,getLayoutParams(0,Gravity.CENTER,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0,200,0,0));
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetUtils.isNetworkAvailable(getActivity())) {
                        ToastUtil.showMiddle(getActivity(), "请检查网络设置");
                    } else {
                        netView.setVisibility(View.GONE);
                        iServicePresenter.getService();
                    }
                }
            });
        }
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

//    @Override
//    public void getServiceSuccess(ServiceVo serviceVo) {
//        dataVoList = serviceVo.getData().getServices();
//        //获取屏幕宽高
//        WindowManager wm = getActivity().getWindowManager();
//        int width = wm.getDefaultDisplay().getWidth();//720,1536
//        int height = wm.getDefaultDisplay().getHeight();//1280,2560
//
//        for (int i = 0; i < dataVoList.size(); i++) {
//            LinearLayout mServiceItem = new LinearLayout(getActivity());//item最外层layout
//            mServiceItem.setBackgroundColor(getResources().getColor(R.color.white));
//            mServiceItem.setOrientation(LinearLayout.VERTICAL);
//
//            LinearLayout mItemTop = new LinearLayout(getActivity());//item顶部layout
//            mItemTop.setOrientation(LinearLayout.HORIZONTAL);
//
//            ImageView mServiceImg = new ImageView(getActivity());
//            Glide.with(getActivity()).load(dataVoList.get(i).getIcon()).placeholder(R.drawable.ic_close).into(mServiceImg);
//
//            TextView mServiceTitle = new TextView(getActivity());
//            mServiceTitle.setTextColor(getResources().getColor(R.color.gray_3c3c3c));
//            mServiceTitle.setTextSize(14);
//            mServiceTitle.setText(dataVoList.get(i).getTitle());
//
//            View lineSmall = new View(getActivity());
//            lineSmall.setMinimumHeight(1);
//            lineSmall.setBackgroundColor(getResources().getColor(R.color.gray_e6e6e6));
//
//            View line = new View(getActivity());
//            line.setMinimumHeight(20);
//            line.setBackgroundColor(getResources().getColor(R.color.gray_f5f5f5));
//
//            if (width <= 720 && height <= 1280){
//                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 45, 45, 16, 20, 0, 20));
//                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5, 20, 0, 20));
//            } else {
//                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 100, 100, 35, 40, 0, 40));
//                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 15, 20, 0, 20));
//            }
//
//            int size = dataVoList.get(i).getItems().size();
//            List<ServiceVo.DataVo.ServiceData.ServiceChildData> childDatas = dataVoList.get(i).getItems();
//
//            int num = ((size % 3) > 0) ? 1 : 0;
//            int lines = size / 3 + num;//得到行数
//
//            mServiceItem.addView(mItemTop);
//            mServiceItem.addView(lineSmall, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, 1, 0, 0, 0, 0));
//
//            for (int j = 0; j < lines; j++) {//循环行数
//                LinearLayout childLay = new LinearLayout(getActivity());
//                for (int k = 0; k < 3; k++) {//循环子item
//                    int positon = j * 3 + k;//得到item当前position
//                    if (positon >= size) {//一行不足3个时填充空view
//                        TextView nullView = new TextView(getActivity());
//                        childLay.addView(nullView, getLayoutParams(1, 0, LinearLayout.LayoutParams.MATCH_PARENT, 60, 0, 0, 0, 0));
//                    } else {
//                        TextView views = new TextView(getActivity());
//                        String title = childDatas.get(positon).getTitle();
//                        views.setText(title);
//                        views.setTextSize(14);
//                        views.setGravity(Gravity.CENTER);
//                        views.setTextColor(getResources().getColor(R.color.gray_5a5a5a));
//                        views.setTag(childDatas.get(positon));
//                        views.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ServiceVo.DataVo.ServiceData.ServiceChildData dv = (ServiceVo.DataVo.ServiceData.ServiceChildData) v.getTag();
//                                if (iHeadLinePresenter != null) {
//                                    iHeadLinePresenter.clickCount(dv.getSid(),CLICK_COUNT);//上报
//                                }
//                                startActivity(WebViewActivity.class, Constants.URL, dv.getDst(), Constants.TITLE, dv.getTitle());
//                            }
//                        });
//                        childLay.addView(views, getLayoutParams(1, 0, LinearLayout.LayoutParams.MATCH_PARENT, 60, 0, 0, 0, 0));
//                    }
//                }
//
//                if (width <= 720 && height <= 1280){
//                    getLines(lines,mServiceItem,j,childLay,40,40);
//                } else {
//                    getLines(lines,mServiceItem,j,childLay,80,80);
//                }
//
//            }
//
//            mServiceParentGroup.addView(mServiceItem);
//            if (i == dataVoList.size() - 1){
//                return;
//            }
//            mServiceItem.addView(line, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
//        }
//    }

    @Override
    public void getServiceListSuccess(ServiceList services) {
        List<Service> list = services.services;
        //获取屏幕宽高
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//720,1536
        int height = wm.getDefaultDisplay().getHeight();//1280,2560

        for (int i = 0; i < list.size(); i++) {
            LinearLayout mServiceItem = new LinearLayout(getActivity());//item最外层layout
            mServiceItem.setBackgroundColor(getResources().getColor(R.color.white));
            mServiceItem.setOrientation(LinearLayout.VERTICAL);

            LinearLayout mItemTop = new LinearLayout(getActivity());//item顶部layout
            mItemTop.setOrientation(LinearLayout.HORIZONTAL);

            ImageView mServiceImg = new ImageView(getActivity());
            Glide.with(getActivity()).load(list.get(i).icon).placeholder(R.drawable.ic_close).into(mServiceImg);

            TextView mServiceTitle = new TextView(getActivity());
            mServiceTitle.setTextColor(getResources().getColor(R.color.gray_3c3c3c));
            mServiceTitle.setTextSize(14);
            mServiceTitle.setText(list.get(i).title);

            View lineSmall = new View(getActivity());
            lineSmall.setMinimumHeight(1);
            lineSmall.setBackgroundColor(getResources().getColor(R.color.gray_e6e6e6));

            View line = new View(getActivity());
            line.setMinimumHeight(20);
            line.setBackgroundColor(getResources().getColor(R.color.gray_f5f5f5));

            if (width <= 720 && height <= 1280){
                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 45, 45, 16, 20, 0, 20));
                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5, 20, 0, 20));
            } else {
                mItemTop.addView(mServiceImg, getLayoutParams(0, Gravity.CENTER, 100, 100, 35, 40, 0, 40));
                mItemTop.addView(mServiceTitle, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 15, 20, 0, 20));
            }

            int size = list.get(i).items.size();
            List<Service.ServiceItem> childDatas = list.get(i).items;

            int num = ((size % 3) > 0) ? 1 : 0;
            int lines = size / 3 + num;//得到行数

            mServiceItem.addView(mItemTop);
            mServiceItem.addView(lineSmall, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, 1, 0, 0, 0, 0));

            for (int j = 0; j < lines; j++) {//循环行数
                LinearLayout childLay = new LinearLayout(getActivity());
                for (int k = 0; k < 3; k++) {//循环子item
                    int positon = j * 3 + k;//得到item当前position
                    if (positon >= size) {//一行不足3个时填充空view
                        TextView nullView = new TextView(getActivity());
                        childLay.addView(nullView, getLayoutParams(1, 0, LinearLayout.LayoutParams.MATCH_PARENT, 60, 0, 0, 0, 0));
                    } else {
                        TextView views = new TextView(getActivity());
                        String title = childDatas.get(positon).title;
                        views.setText(title);
                        views.setTextSize(14);
                        views.setGravity(Gravity.CENTER);
                        views.setTextColor(getResources().getColor(R.color.gray_5a5a5a));
                        views.setTag(childDatas.get(positon));
                        views.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Service.ServiceItem dv = (Service.ServiceItem) v.getTag();
                                if (iHeadLinePresenter != null) {
                                    iHeadLinePresenter.clickCount(dv.sid,CLICK_COUNT);//上报
                                }
                                startActivity(WebViewActivity.class, Constants.URL, dv.dst, Constants.TITLE, dv.title);
                            }
                        });
                        childLay.addView(views, getLayoutParams(1, 0, LinearLayout.LayoutParams.MATCH_PARENT, 60, 0, 0, 0, 0));
                    }
                }

                if (width <= 720 && height <= 1280){
                    getLines(lines,mServiceItem,j,childLay,40,40);
                } else {
                    getLines(lines,mServiceItem,j,childLay,80,80);
                }

            }

            mServiceParentGroup.addView(mServiceItem);
            if (i == list.size() - 1){
                return;
            }
            mServiceItem.addView(line, getLayoutParams(0, 0, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
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

    public void getLines(int lines,LinearLayout serviceItem,int j,LinearLayout childLay,int top,int bottom){
        if (lines == 1){//只有一行
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, top, 0, bottom));
        } else if(lines == 2 && j == 0) {
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, top, 0, bottom));
        } else if (lines == 2 && j == 1){
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, bottom));
        } else if (lines == 3){
            serviceItem.addView(childLay, getLayoutParams(0, Gravity.CENTER, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0));
        }
    }
}