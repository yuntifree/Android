package com.yunxingzh.wireless.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dgwx.app.lib.bl.WifiInterface;
import com.dgwx.app.lib.common.util.SettingUtility;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.config.MyApplication;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.ScanCodeActivity;
import com.yunxingzh.wireless.mvp.ui.activity.SpeedTestActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiManagerActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiMapActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.HeadLineNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.adapter.NetworkImageHolderView;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.ui.utils.MyScrollView;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.Utility;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.ScrollViewListener;
import com.yunxingzh.wirelesslibs.convenientbanner.ConvenientBanner;
import com.yunxingzh.wirelesslibs.convenientbanner.holder.CBViewHolderCreator;
import com.yunxingzh.wirelesslibs.convenientbanner.listener.OnItemClickListener;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephon_ on 2016/11/1.
 * 无线
 */

public class WirelessFragment extends BaseFragment implements IHeadLineView, AdapterView.OnItemClickListener, View.OnClickListener, ScrollViewListener {

    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int PULL_HEIGHT = 10;
    private final static int HEIGHT = 0;
    private final static int ITEM = 0;
    private final static int NEWS = 1;//新闻点击上报
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int DG_SDK_TIME_OUT = 5 * 1000;

    private FragmentManager fragmentManager;

    private LinearLayout mNoticeLay, mMainWifiManager, mMainMapLay, mMainSpeedtest,mMainHeadImg;
    private MyScrollView scrollView;
    private TextView mNoticeTv, mConnectTv, mCircleSecondTv, mCircleThreeTv, mConnectCountTv,
            mEconomizeTv, mFontNewsTv, mFontVideoTv, mFontServiceTv, mFontZhiTv, mFontPlayingTv, mFontBuyingTv;
    private ImageView mShowMoreIv, mTitleRightIv, mWeatherImgBottom, mWeatherImgTop;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private HeadLineNewsAdapter headLineNewsAdapter;
    private AnimationSet alphaAnimation;

    private View footView;
    private List<NewsVo.Data.NewsData> newsList;

    private FontInfoVo.FontData.BannersVo bannersVo;
    private FontInfoVo.FontData.UserVo userVo;
    private TextView mMainTemperature, mMainWeather;
    private WeatherNewsVo.WeatherNewsData.WeatherVo weatherNewsData;
    private ConvenientBanner mAdRotationBanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wireless, null);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        mTitleRightIv = findView(view, R.id.title_right_iv);
        mTitleRightIv.setVisibility(View.VISIBLE);
        mTitleRightIv.setOnClickListener(this);
        scrollView = findView(view, R.id.scrollView);
        scrollView.setScrollViewListener(this);
        mMainNewsLv = findView(view, R.id.main_news_lv);
        mNoticeTv = findView(view, R.id.notice_tv);
        mNoticeLay = findView(view, R.id.notice_lay);
        mShowMoreIv = findView(view, R.id.show_more_iv);
        mConnectTv = findView(view, R.id.connect_tv);
        mConnectTv.setOnClickListener(this);
        mCircleSecondTv = findView(view, R.id.circle_second_tv);
        mCircleThreeTv = findView(view, R.id.circle_three_tv);
        mMainWifiManager = findView(view, R.id.main_wifi_manager);
        mMainWifiManager.setOnClickListener(this);
        mMainMapLay = findView(view, R.id.main_map_lay);
        mMainMapLay.setOnClickListener(this);
        mAdRotationBanner = findView(view, R.id.banner_img);
        mConnectCountTv = findView(view, R.id.connect_count_tv);
        mEconomizeTv = findView(view, R.id.economize_tv);
        mMainSpeedtest = findView(view, R.id.main_speedtest_lay);
        mMainSpeedtest.setOnClickListener(this);
        mMainTemperature = findView(view, R.id.main_temperature);
        mMainWeather = findView(view, R.id.main_weather);
        mMainHeadImg = findView(view, R.id.main_head_img);

        mFontNewsTv = findView(view, R.id.font_news_tv);
        mFontNewsTv.setOnClickListener(this);
        mFontVideoTv = findView(view, R.id.font_video_tv);
        mFontVideoTv.setOnClickListener(this);
        mFontServiceTv = findView(view, R.id.font_service_tv);
        mFontServiceTv.setOnClickListener(this);
        mFontZhiTv = findView(view, R.id.font_zhi_tv);
        mFontZhiTv.setOnClickListener(this);
        mFontPlayingTv = findView(view, R.id.font_playing_tv);
        mFontPlayingTv.setOnClickListener(this);
        mFontBuyingTv = findView(view, R.id.font_buying_tv);
        mFontBuyingTv.setOnClickListener(this);
        mWeatherImgBottom = findView(view, R.id.weather_img_bottom);
        mWeatherImgTop = findView(view, R.id.weather_img_top);

        setAnimation(mWeatherImgBottom, 0, 60, 0, 0);
        setAnimation(mWeatherImgTop, 60, 0, 0, 0);

        alphaAnimation = (AnimationSet) AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        mCircleSecondTv.startAnimation(alphaAnimation);
        mCircleThreeTv.startAnimation(alphaAnimation);
    }

    public void initData() {
        fragmentManager = getFragmentManager();
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        mAdRotationBanner.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        iHeadLinePresenter.getHeadLine(HEAD_LINE_TYPE, HEAD_LINE_SEQ);
        iHeadLinePresenter.weatherNews();
    }

    @Override
    public void getHeadLineSuccess(NewsVo newsVo) {
        if (newsVo != null) {
            newsList = newsVo.getData().getInfos();
            mNoticeTv.setText(newsList.get(ITEM).getTitle());
        }
        headLineNewsAdapter = new HeadLineNewsAdapter(getActivity(), newsList, true);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_for_main_news, null);
        mMainNewsLv.addFooterView(footView);
        footView.setOnClickListener(this);
        mMainNewsLv.setAdapter(headLineNewsAdapter);
        Utility.setListViewHeight(mMainNewsLv, Constants.LISTVIEW_ITEM_HEIGHT);
        mMainNewsLv.setOnItemClickListener(this);
        //时间
        String hour = StringUtils.getTime();
        int h = Integer.parseInt(hour);
        if (h >= 6 && h < 19) {
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg);
        } else {
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg_night);
        }
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsVo weatherNewsVo) {
        weatherNewsData = weatherNewsVo.getData().getWeather();
        String info = weatherNewsData.getInfo();
        int type = weatherNewsData.getType();
        mMainTemperature.setText(weatherNewsData.getTemp() + "°C");
        mMainWeather.setText(info);
        //类型 0-晴 1-阴 2-雨 3-雪
        switch (type) {
            case Constants.SUNNY:
                mWeatherImgBottom.setImageResource(0);
                mWeatherImgTop.setImageResource(R.drawable.sunny);
                break;
            case Constants.CLOUDY:
                mWeatherImgBottom.setImageResource(R.drawable.cloudy);
                mWeatherImgTop.setImageResource(R.drawable.cloudy);
                break;
            case Constants.RAIN:
                mWeatherImgBottom.setImageResource(R.drawable.rain);
                mWeatherImgTop.setImageResource(R.drawable.rain);
                break;
            case Constants.SNOW:
                mWeatherImgBottom.setImageResource(R.drawable.snow);
                mWeatherImgTop.setImageResource(R.drawable.snow);
                break;
        }
        iHeadLinePresenter.getFontInfo();
    }

    @Override
    public void getFontInfoSuccess(FontInfoVo fontInfoVo) {
        bannersVo = fontInfoVo.getData().getBanner();
        userVo = fontInfoVo.getData().getUser();
        mConnectCountTv.setText(userVo.getTotal() + "");
        mEconomizeTv.setText(userVo.getSave() + "");

        if (bannersVo != null) {
            List<String> imageList = new ArrayList<String>();
            imageList.add(bannersVo.getImg());
            mAdRotationBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, imageList);
        }
        //banner图跳转
        mAdRotationBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!StringUtils.isEmpty(bannersVo.getImg())) {
                    startActivity(WebViewActivity.class, Constants.URL, bannersVo.getDst(), "", "");
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (newsList.get(position).getImages().size() == NEWS) {//图片size=1表示点击的是广告
            iHeadLinePresenter.clickCount(newsList.get(position).getId(), NEWS);
        }
        iHeadLinePresenter.clickCount(newsList.get(position).getId(), NEWS);
        startActivity(WebViewActivity.class, Constants.URL, newsList.get(position).getDst(), Constants.TITLE, newsList.get(position).getTitle());
    }

    public Handler registerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//注册成功
                WifiInterface.wifiLogon(validateHandler, MyApplication.sApplication.getUserName(), MyApplication.sApplication.getWifiPwd(), DG_SDK_TIME_OUT);//wifi认证
            } else {
                ToastUtil.showMiddle(getActivity(), R.string.register_faild);
            }
        }
    };

    public Handler validateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//认证成功
                ToastUtil.showMiddle(getActivity(), R.string.validate_success);

            } else {
                ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (mConnectTv == v) {//一键连接
            WifiInterface.init(getActivity());
            WifiInterface.initEnv("http://192.168.100.4:880/wsmp/interface", "无线东莞DG—FREE", "ROOT_VNO");
            int checkResult = WifiInterface.checkEnv(DG_SDK_TIME_OUT);
            switch (checkResult) {
                case Constants.NET_OK://0、网络正常，可以发起调用认证、下线等接口
                    WifiInterface.wifiRegister(registerHandler, MyApplication.sApplication.getUserName(), MyApplication.sApplication.getWifiPwd(), DG_SDK_TIME_OUT);
                    break;
                case Constants.VALIDATE_SUCCESS://1、已经认证成功。

                    break;
                default:
                    ToastUtil.showMiddle(getActivity(), "checkEnv:" + checkResult);
                    break;
            }

        } else if (footView == v) {//查看更多新闻
            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
        } else if (mMainWifiManager == v) {//wifi管理
            startActivity(WifiManagerActivity.class, "", "", "", "");
        } else if (mMainMapLay == v) {//wifi地图
            startActivity(WifiMapActivity.class, "", "", "", "");
        } else if (mTitleRightIv == v) {//扫码连接东莞wifi
            Intent intent = new Intent();
            intent.setClass(getActivity(), ScanCodeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        } else if (mMainSpeedtest == v) { // wifi 测速
            startActivity(SpeedTestActivity.class, "", "", "", "");
        } else if (mFontNewsTv == v) { // 东莞头条
            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
        } else if (mFontVideoTv == v) { //热门视频
            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE, Constants.VIDEO));
        } else if (mFontServiceTv == v) { //同城服务
            EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } else if (mFontZhiTv == v) { //智慧服务
            EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } else if (mFontPlayingTv == v) { //同城直播

        } else if (mFontBuyingTv == v) { //抢购

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String s = bundle.getString("result");
                    System.out.print(s);
                    //显示扫描到的内容
                    // mTextView.setText(bundle.getString("result"));
                    //显示
                    // mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y > PULL_HEIGHT) { //下拉高度大于10
            mNoticeLay.setVisibility(View.GONE);
            mShowMoreIv.setVisibility(View.INVISIBLE);
        } else if (y == HEIGHT) {
            mNoticeLay.setVisibility(View.VISIBLE);
            mNoticeTv.setText(R.string.head_line);
            mShowMoreIv.setVisibility(View.VISIBLE);
        }
    }

    public void setAnimation(View view, float fromX, float toX, float fromY, float toY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(2000);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillBefore(false);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        view.startAnimation(translateAnimation);
    }

    public void startActivity(Class activity, String key, String videoUrl, String titleKey, String title) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtra(key, videoUrl);
        intent.putExtra(titleKey, title);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdRotationBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdRotationBanner.startTurning(1500);
    }
}
