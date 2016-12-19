package com.yunxingzh.wireless.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dgwx.app.lib.bl.WifiInterface;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.CircleWaveView;
import com.yunxingzh.wireless.mview.MyScrollView;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mvp.presenter.IConnectDGCountPresenter;
import com.yunxingzh.wireless.mvp.presenter.IHeadLinePresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.ConnectDGCountPresenterImpl;
import com.yunxingzh.wireless.mvp.presenter.impl.HeadLinePresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.ScanCodeActivity;
import com.yunxingzh.wireless.mvp.ui.activity.SpeedTestActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiManagerActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiMapActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiSpiritedActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.MainNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.adapter.NetworkImageHolderView;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IConnectDGCountView;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.ScrollViewListener;
import com.yunxingzh.wireless.utils.LogUtils;
import com.yunxingzh.wireless.utils.NetUtil;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.StringUtils;
import com.yunxingzh.wireless.utils.ToastUtil;
import com.yunxingzh.wireless.utils.Utility;
import com.yunxingzh.wireless.utils.WifiUtils;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.WifiState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.HotInfoList;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.bean.vo.AdvertVo;
import wireless.libs.bean.vo.BannerVo;
import wireless.libs.bean.vo.MainNewsVo;
import wireless.libs.bean.vo.UserConnectVo;
import wireless.libs.bean.vo.WeatherVo;
import wireless.libs.convenientbanner.ConvenientBanner;
import wireless.libs.convenientbanner.holder.CBViewHolderCreator;
import wireless.libs.convenientbanner.listener.OnItemClickListener;

/**
 * Created by stephon_ on 2016/11/1.
 * 无线
 */

public class WirelessFragment extends BaseFragment implements IHeadLineView, IConnectDGCountView, View.OnClickListener, ScrollViewListener,
        ActivityCompat.OnRequestPermissionsResultCallback, SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "WirelessFragment";
    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int PULL_HEIGHT = 10;
    private final static int HEIGHT = 0;
    private final static int ITEM = 0;
    private final static int NEWS = 1;//新闻点击上报
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int DG_SDK_TIME_OUT = 10 * 1000;

    private LinearLayout mNoticeLay, mMainWifiManager, mMainMapLay, mMainSpeedtest, mTitleLeftLay,
            mMainHeadImg, mWeatherLay, mMainSpiritedLay, mTitleLay;
    private MyScrollView scrollView;
    private TextView mConnectCountTv, mTitleNameTv,
            mEconomizeTv, mFontNewsTv, mFontVideoTv, mFontServiceTv, mFontZhiTv, mFontPlayingTv, mFontBuyingTv;
    private ImageView mShowMoreIv, mTitleRightIv, mWeatherImgBottom, mWeatherImgTop, mConnectIv, mTitleMainImg;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private IConnectDGCountPresenter iConnectDGCountPresenter;
    private CircleWaveView mAnimationTv;
    private TextView footView, mConnectText;
    private List<MainNewsVo> mainNewsVos;
    private MainNewsAdapter mainNewsAdapter;
    private SwipeRefreshLayout mMainRefreshLay;

    private List<BannerVo> bannersVo;
    private UserConnectVo userVo;
    private TextView mMainTemperature, mMainWeather;
    private WeatherVo weatherNewsData;
    private ConvenientBanner mAdRotationBanner;

    private WifiUtils wifiUtils = null;
    private CheckEnvTask mCheckTask = null;
    private AccessPoint currentAp;

    private boolean mWifiConnected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wireless, null);
        initView(view);
        initData();
        // 进入主页判断下网络
        CheckAndLogon();
        return view;
    }

    public void initView(View view) {
        mTitleLeftLay = findView(view, R.id.title_left_lay);
        mTitleLeftLay.setVisibility(View.INVISIBLE);
        mTitleNameTv = findView(view, R.id.title_name_tv);
        mTitleNameTv.setVisibility(View.GONE);
        mTitleMainImg = findView(view, R.id.title_main_img);
        mTitleMainImg.setVisibility(View.VISIBLE);
        mTitleLay = findView(view, R.id.title_lay);
        mTitleRightIv = findView(view, R.id.title_right_iv);
        mTitleRightIv.setVisibility(View.VISIBLE);
        mTitleRightIv.setOnClickListener(this);
        scrollView = findView(view, R.id.scrollView);
        scrollView.setScrollViewListener(this);
        mMainNewsLv = findView(view, R.id.main_news_lv);
        mNoticeLay = findView(view, R.id.notice_lay);
        mShowMoreIv = findView(view, R.id.show_more_iv);
        mMainSpiritedLay = findView(view, R.id.main_spirited_lay);
        mMainSpiritedLay.setOnClickListener(this);
        mAnimationTv = findView(view, R.id.animation_tv);
        mConnectIv = findView(view, R.id.connect_iv);
        mConnectIv.setOnClickListener(this);
        mMainRefreshLay = findView(view, R.id.main_refresh_lay);
        mMainRefreshLay.setOnRefreshListener(this);

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
        mWeatherLay = findView(view, R.id.weather_lay);
        mWeatherLay.setOnClickListener(this);
        footView = findView(view, R.id.foot_view);
        footView.setOnClickListener(this);
        mConnectText = findView(view, R.id.connect_text);

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
    }

    public void initData() {
        wifiUtils = new WifiUtils(getActivity());
        //注册EventBus
        EventBus.getDefault().register(this);
        iConnectDGCountPresenter = new ConnectDGCountPresenterImpl(this);
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        mAdRotationBanner.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        iHeadLinePresenter.weatherNews();

        FWManager.getInstance().addWifiObserver(wifiObserver);
        //获取屏幕宽高
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//720,1536
        int height = wm.getDefaultDisplay().getHeight();//1280,2560
        if (width <= 720 && height <= 1280) {
            mAnimationTv.setCoreRadius(100);
            mAnimationTv.setMaxWidth(300);
            mAnimationTv.setDiffuseWidth(5);
        } else {
            mAnimationTv.setCoreRadius(208);
            mAnimationTv.setMaxWidth(2000);
            mAnimationTv.setDiffuseWidth(30);
        }
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsList weatherNewsVo) {
        weatherNewsData = weatherNewsVo.weather;
        mainNewsVos = weatherNewsVo.news;
        String info = weatherNewsData.info;
        int type = weatherNewsData.type;
        mMainTemperature.setText(weatherNewsData.temp + "°C");
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
        //新闻
        if (weatherNewsVo != null) {
            mainNewsVos = weatherNewsVo.news;
            mainNewsAdapter = new MainNewsAdapter(getActivity(), mainNewsVos);
        }

        mMainNewsLv.setAdapter(mainNewsAdapter);
        Utility.setListViewHeight(mMainNewsLv, Constants.LISTVIEW_ITEM_HEIGHT);

        timeChanged();

        iHeadLinePresenter.getFontInfo();
    }

    @Override
    public void getFontInfoSuccess(FontInfoList fontInfoVo) {
        bannersVo = fontInfoVo.banner;
        userVo = fontInfoVo.user;
        mConnectCountTv.setText(userVo.total + "");
        mEconomizeTv.setText(userVo.save + "");

        if (bannersVo != null) {
            List<String> imageList = new ArrayList<String>(bannersVo.size());
            for (BannerVo bannersList : bannersVo) {
                imageList.add(bannersList.img);
            }
            mAdRotationBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, imageList);

            bannersState();
        }
        //banner图跳转
        mAdRotationBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = bannersVo.get(position).dst;
                if (!StringUtils.isEmpty(url)) {
                    startActivity(WebViewActivity.class, Constants.URL, url, "", "");
                }
            }
        });
    }

    public Handler validateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//认证成功
                ToastUtil.showMiddle(getActivity(), R.string.validate_success);
                EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));//跳转新闻列表
                if (iHeadLinePresenter != null) {
                    iHeadLinePresenter.weatherNews();
                }
                iConnectDGCountPresenter.connectDGCount(getCurrentWifiMacAddress());//上报
            } else {
                ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
            }
            // 判断下按钮的状态
            updateConnectState();
        }
    };

    private void CheckAndLogon() {
        if (mCheckTask != null) {
            return;
        }
        mCheckTask = new CheckEnvTask();
        mCheckTask.execute((Void) null);
    }

    @Override
    public void onClick(View v) {
        if (mConnectIv == v) {//一键连接
            if (mWifiConnected) {
                EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));//跳转新闻列表
            } else {
                if (wifiUtils.getWlanState()) {//是否打开
                    checkDGWifi();
                } else {
                    ToastUtil.showMiddle(getActivity(), R.string.please_open_wifi);
                }
            }
            //  WifiInterface.wifiLogout(logoOutHandler,MainApplication.sApplication.getUserName(),5000);
        } else if (mWeatherLay == v) {
            startActivity(WebViewActivity.class, Constants.URL, Constants.URL_WEATHER, Constants.TITLE, "东莞天气");
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
        } else if (mMainSpiritedLay == v) {//wifi公益
            startActivity(WifiSpiritedActivity.class, "", "", "", "");
        } else if (mFontNewsTv == v) { // 东莞头条
            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
        } else if (mFontVideoTv == v) { //热门视频
            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE, Constants.VIDEO));
        } else if (mFontServiceTv == v) { //同城服务
            // EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } else if (mFontZhiTv == v) { //智慧服务
            EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } else if (mFontPlayingTv == v) { //同城直播

        } else if (mFontBuyingTv == v) { //抢购

        }
    }

    private FWManager.WifiObserver wifiObserver = new FWManager.WifiObserver() {
        @Override
        public void onStateChanged(WifiState new_state, WifiState old_state) {
            // 连上网
            if (new_state == WifiState.CONNECTED) {
                currentAp = FWManager.getInstance().getCurrent();
                if (currentAp != null && currentAp.ssid.equals(Constants.SSID)) {
                    CheckAndLogon();
                } else {
                    //连接的是其他wifi
                    if (NetUtils.isNetworkAvailable(getActivity())) {
                        if (iHeadLinePresenter != null) {
                            iHeadLinePresenter.weatherNews();
                        }
                    } else {
                        ToastUtil.showMiddle(getActivity(),R.string.network_error);
                    }
                    updateConnectState();
                }
            } else if (new_state == WifiState.DISABLED || new_state == WifiState.DISCONNECTED) {
                // 断开网
                updateConnectState();
            }
        }

        @Override
        public void onListChanged(List<AccessPoint> accessPoints) {
        }

        @Override
        public void onRSSIChanged(int rssi) {
        }

        @Override
        public void onAuthError(AccessPoint ap) {
        }
    };

    //扫码
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String ssid = "";
                    String url = "";
                    try {
                        url = bundle.getString("result");
                        ssid = url.substring(url.indexOf("ssid=") + 5);
                        ssid = java.net.URLDecoder.decode(ssid, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (ssid.equals(Constants.SSID)) {
                        checkDGWifi();
                    } else {
                        // 不是东莞无线
                        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                            intent.putExtra(Constants.URL, url);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    }
                }
                break;
        }
    }

    public class CheckEnvTask extends AsyncTask<Void, Void, Boolean> {
        private int mCheckRet = -1;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mCheckRet = WifiInterface.checkEnv(DG_SDK_TIME_OUT);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCheckTask = null;
            if (success) {
                switch (mCheckRet) {
                    case Constants.NET_OK://0、网络正常，可以发起调用认证、下线等接口
                        WifiInterface.wifiLogon(validateHandler, MainApplication.getInstance().getUserName(),
                                MainApplication.getInstance().getWifiPwd(), DG_SDK_TIME_OUT);//wifi认证
                        break;
                    case Constants.VALIDATE_SUCCESS://1、已经认证成功。
                        iConnectDGCountPresenter.connectDGCount(getCurrentWifiMacAddress());//上报
                        // 判断下按钮的状态
                        updateConnectState();
                        break;
                    default:
                        LogUtils.d("scan error:", mCheckRet + "");
                        break;
                }
            } else {
                ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
            }
        }

        @Override
        protected void onCancelled() {
            mCheckTask = null;
        }
    }

    //连接后改变状态
    public void updateConnectState() {
        currentAp = FWManager.getInstance().getCurrent();//当前连接的wifi
        if (currentAp != null) {
            stopAnimation();
            String ssidText;
            if (currentAp.ssid.equals(Constants.SSID)) {
                ssidText = getResources().getString(R.string.connect_wifi) + getResources().getString(R.string.connect_dg_success);
                mConnectText.setText(ssidText);
            } else {
                mConnectText.setText(getResources().getString(R.string.connect_wifi) + currentAp.ssid);
            }
            mWifiConnected = true;
        } else {
            startAnimation();
            AccessPoint DGWifiAp = getDGWifiFromList();//找出附近wifi列表中的东莞wifi
            if (DGWifiAp != null) {
                mConnectText.setText(R.string.find_wifi);
            } else {
                mConnectText.setText(R.string.wireless_dg);
            }
            mWifiConnected = false;
        }
    }

    private void startAnimation() {
        mAnimationTv.setVisibility(View.VISIBLE);
        mConnectIv.setVisibility(View.VISIBLE);
        mConnectIv.setImageResource(R.drawable.need_connect);
        mAnimationTv.start();
    }

    private void stopAnimation() {
        mAnimationTv.setVisibility(View.GONE);
        mConnectIv.setVisibility(View.VISIBLE);
        mConnectIv.setImageResource(R.drawable.main_connected);
        mAnimationTv.stop();
    }

    public AccessPoint getDGWifiFromList() {
        AccessPoint DGFreeAp = null;
        List<AccessPoint> apList = FWManager.getInstance().getList();//先拿到附近列表
        for (int i = 0; i < apList.size(); i++) {//找出是否有东莞wifi
            AccessPoint ap = apList.get(i);
            if (ap.ssid.equals(Constants.SSID)) {
                DGFreeAp = ap;
                break;
            }
        }
        return DGFreeAp;
    }

    public void checkDGWifi() {
        // 1. 未联网，有DG-Free: 连接DG-Free
        if (!NetUtils.isWifi(getActivity())) {//true为已打开但未连接wifi
            AccessPoint DGFreeAp = getDGWifiFromList();
            if (DGFreeAp != null) {//不为空表示周围有东莞wifi
                FWManager.getInstance().connect(DGFreeAp);//先连接上wifi
            } else { // 4. 未联网，没有DG-Free：跳转到wifi列表
                startActivity(WifiManagerActivity.class, "", "", "", "");
            }
        } else {
            //已连上wifi
            currentAp = FWManager.getInstance().getCurrent();
            // 3. 已经连上DG-Free的情况
            if (currentAp != null && currentAp.ssid.equals(Constants.SSID)) {
                CheckAndLogon();
            } else {
                // 2. 已经连上其它WiFi，周围有DG-Free的情况，无需去连接DG-Free
                if (iHeadLinePresenter != null) {
                    iHeadLinePresenter.weatherNews();
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        mMainRefreshLay.setRefreshing(false);
        if (NetUtils.isNetworkAvailable(getActivity())) {
            if (iHeadLinePresenter != null) {
                iHeadLinePresenter.weatherNews();
            }
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.net_error);
        }
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        int index = event.getChildMsg();
        if (event.getMsg() == Constants.MAIN_NEWS_FLAG && index != -1) {//上报
            iHeadLinePresenter.clickCount(mainNewsVos.get(index).id, NEWS);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        iHeadLinePresenter.onDestroy();
        FWManager.getInstance().removeWifiObserver(wifiObserver);
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y > PULL_HEIGHT) { //下拉高度大于10
            // mNoticeLay.setVisibility(View.GONE);
            mShowMoreIv.setVisibility(View.INVISIBLE);
        } else if (y == HEIGHT) {
            // mNoticeLay.setVisibility(View.VISIBLE);
        }
    }

    public String getCurrentWifiMacAddress() {
        return wifiUtils.getCurrentWifiInfo().getMacAddress();
    }

    public void setAnimation(View view, float fromX, float toX, float fromY, float toY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(4000);
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

    public void timeChanged() {
        //时间
        String hour = StringUtils.getTime();
        int h = Integer.parseInt(hour);
        if (h >= 6 && h < 19) {
            StatusBarColor.compat(getActivity(), getResources().getColor(R.color.blue_009CFB));
            mTitleLay.setBackgroundColor(Color.parseColor("#009CFB"));
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg);
        } else {
            StatusBarColor.compat(getActivity(), getResources().getColor(R.color.blue_236EC5));
            mTitleLay.setBackgroundColor(Color.parseColor("#236EC5"));
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg_night);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdRotationBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConnectState();
        if (bannersVo != null) {
            bannersState();
        }
    }

    public void bannersState() {
        if (bannersVo.size() > 1) {
            mAdRotationBanner.setCanLoop(true);
            mAdRotationBanner.setPointViewVisible(true);
            mAdRotationBanner.startTurning(2000);
        } else {
            mAdRotationBanner.setCanLoop(false);
            mAdRotationBanner.setPointViewVisible(false);
        }
    }

    @Override
    public void connectDGCountSuccess() {
    }

    @Override
    public void getHeadLineSuccess(HotInfoList newsVo) {
    }
}
