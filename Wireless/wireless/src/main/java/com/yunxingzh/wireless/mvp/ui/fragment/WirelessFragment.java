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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgwx.app.lib.bl.WifiInterface;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.config.MainApplication;
import com.yunxingzh.wireless.mview.BadgeView;
import com.yunxingzh.wireless.mview.CircleWaveView;
import com.yunxingzh.wireless.mview.MyListview;
import com.yunxingzh.wireless.mview.MyScrollView;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnDismissListener;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.IWirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.ScanCodeActivity;
import com.yunxingzh.wireless.mvp.ui.activity.SetActivity;
import com.yunxingzh.wireless.mvp.ui.activity.SpeedTestActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiManagerActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiMapActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiSpiritedActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.MainNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.adapter.NetworkImageHolderView;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IWirelessView;
import com.yunxingzh.wireless.mvp.view.ScrollViewListener;
import com.yunxingzh.wireless.utils.NetUtils;
import com.yunxingzh.wireless.utils.SPUtils;
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
import java.util.Timer;
import java.util.TimerTask;

import wireless.libs.bean.resp.FontInfoList;
import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.bean.vo.BannerVo;
import wireless.libs.bean.vo.MainNewsVo;
import wireless.libs.bean.vo.NoticeVo;
import wireless.libs.bean.vo.UserConnectVo;
import wireless.libs.bean.vo.WeatherVo;
import wireless.libs.convenientbanner.ConvenientBanner;
import wireless.libs.convenientbanner.holder.CBViewHolderCreator;
import wireless.libs.convenientbanner.listener.OnItemClickListener;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon_ on 2016/11/1.
 * 无线
 */

public class WirelessFragment extends BaseFragment implements IWirelessView, View.OnClickListener, ScrollViewListener,
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

    private LinearLayout mNoticeLay, mMainWifiManager, mMainMapLay, mMainSpeedtest, mMachineErrorLay,
            mMainHeadImg, mWeatherLay, mMainSpiritedLay, mTitleLay, mWirelessTimesLay;
    private MyScrollView scrollView;
    private TextView mConnectCountTv, mTitleNameTv, mWirelessNumTv,
            mEconomizeTv, mFontNewsTv, mFontVideoTv, mFontServiceTv, mFontZhiTv, mFontPlayingTv, mFontBuyingTv;
    private ImageView mShowMoreIv, mTitleRightIv, mWeatherImgBottom, mWeatherImgTop, mConnectIv, mTitleMainImg,
            mTitleReturnIv, mCircleIv, mWirelessCircleIv, mWirelessCircleBig, mWirelessCircleSmall, mMachineErrorIv;
    private MyListview mMainNewsLv;
    private IWirelessPresenter iWirelessPresenter;
    private CircleWaveView mAnimationTv;
    private TextView footView, mConnectText;
    private List<MainNewsVo> mainNewsVos;
    private MainNewsAdapter mainNewsAdapter;
    private SwipeRefreshLayout mMainRefreshLay;

    private List<BannerVo> bannersVo;
    private UserConnectVo userVo;
    private TextView mMainTemperature, mMainWeather, mMachineErrorTv;
    private WeatherVo weatherNewsData;
    private NoticeVo noticeVo;
    private ConvenientBanner mAdRotationBanner;

    private WifiUtils wifiUtils = null;
    private CheckEnvTask mCheckTask = null;
    private AccessPoint currentAp;

    private boolean mDGFreeConnected = false;
    private RotateAnimation animation;
    private int recLen = 5;
    private Handler handler = new Handler();
    private BadgeView mBadgeView;
    private WindowManager wm;
    private WifiState wifiState;

    private AlertView switchAlert;

    private boolean isValidate = false;//东莞wifi是否认证通过

    private boolean isCountTime = false;//true打开（start），false关闭（stop）

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wireless, null);
        initView(view);
        initData();
        currentAp = FWManager.getInstance().getCurrent();
        if (currentAp != null && !StringUtils.isEmpty(currentAp.ssid) && currentAp.ssid.equals(Constants.SSID)) {
            CheckAndLogon();
        }
        return view;
    }

    public void initView(View view) {
        mTitleReturnIv = findView(view, R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mTitleReturnIv.setImageResource(R.drawable.wireless_ico_setting);
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
        mMachineErrorTv = findView(view, R.id.machine_error_tv);
        mMachineErrorIv = findView(view, R.id.machine_error_iv);
        mMachineErrorIv.setOnClickListener(this);
        mMachineErrorLay = findView(view, R.id.machine_error_lay);
        mMachineErrorLay.setOnClickListener(this);

        mCircleIv = findView(view, R.id.circle_iv);
        mWirelessCircleBig = findView(view, R.id.wireless_circle_big);
        mWirelessCircleSmall = findView(view, R.id.wireless_circle_small);
        mWirelessTimesLay = findView(view, R.id.wireless_times_lay);
        mWirelessNumTv = findView(view, R.id.wireless_num_tv);
        mWirelessCircleIv = findView(view, R.id.wireless_circle_iv);
        //wifi连接中的旋转动画
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lir = new LinearInterpolator();//匀速旋转
        animation.setInterpolator(lir);
        animation.setDuration(4000);//设置动画持续时间
        animation.setRepeatCount(-1);//设置重复次数
        animation.setFillAfter(false);//动画执行完后是否停留在执行完的状态
        animation.setStartOffset(0);//执行前的等待时间

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
        mBadgeView = new BadgeView(getActivity());
        mBadgeView.setBadgeMargin(0, 35, 10, 0);
        mBadgeView.setTargetView(mMainWifiManager);
        // mBadgeView.set
        iWirelessPresenter = new IWirelessPresenterImpl(this);
        mAdRotationBanner.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        iWirelessPresenter.weatherNews();

        FWManager.getInstance().addWifiObserver(wifiObserver);
        //获取屏幕宽高
        if (getActivity() == null) {
            return;
        }
        wm = getActivity().getWindowManager();
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

        timeChanged();
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsList weatherNewsVo) {
        if (weatherNewsVo == null) {
            return;
        }
        weatherNewsData = weatherNewsVo.weather;
        mainNewsVos = weatherNewsVo.news;
        noticeVo = weatherNewsVo.notice;

        if (noticeVo != null) {
            mMachineErrorLay.setVisibility(View.VISIBLE);
            mMachineErrorLay.setAlpha(0.5f);
            mMachineErrorTv.setText(noticeVo.title);
        }

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
        mainNewsVos = weatherNewsVo.news;
        mainNewsAdapter = new MainNewsAdapter(getActivity(), mainNewsVos);

        mMainNewsLv.setAdapter(mainNewsAdapter);
        Utility.setListViewHeight(mMainNewsLv);

        iWirelessPresenter.getFontInfo();
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

    private void CheckAndLogon() {
        if (mCheckTask != null) {
            return;
        }
        isValidate = false;
        mCheckTask = new CheckEnvTask();
        mCheckTask.execute((Void) null);
    }

    @Override
    public void onClick(View v) {
        if (mConnectIv == v) {//一键连接
            if (mDGFreeConnected) {
                EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
            } else {
                if (wifiUtils.getWlanState()) {//是否打开
                    checkDGWifi();
                } else {
                    startActivity(WifiManagerActivity.class, "", "", "", "");
                }
            }
        } else if (mWeatherLay == v) {// 天气
            if (weatherNewsData != null) {
                startActivity(WebViewActivity.class, Constants.URL, weatherNewsData.dst, Constants.TITLE, "东莞天气");
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
        } else if (mMainSpiritedLay == v) {//wifi公益
            if (currentAp != null && !StringUtils.isEmpty(currentAp.ssid)) {
                startActivity(WifiSpiritedActivity.class, "ssid", currentAp.ssid, "", "");
            } else {
                startActivity(WifiSpiritedActivity.class, "", "", "", "");
            }
        } else if (mFontNewsTv == v) { // 东莞头条
            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
        } else if (mFontVideoTv == v) { //热门视频
            EventBus.getDefault().post(new EventBusType(0, Constants.VIDEO));
        } else if (mFontServiceTv == v) { //同城服务
            // EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } else if (mFontZhiTv == v) { //智慧服务
            EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } else if (mFontPlayingTv == v) { //同城直播

        } else if (mFontBuyingTv == v) { //抢购

        } else if (mTitleReturnIv == v) {
            startActivity(SetActivity.class, "", "", "", "");
        } else if (mMachineErrorIv == v) {//不可抗力异常关闭
            mMachineErrorLay.setVisibility(View.GONE);
        } else if (mMachineErrorLay == v) {
            if (!StringUtils.isEmpty(noticeVo.content)) {
                AlertView dialog = new AlertView("通知", noticeVo.content, null, new String[]{"我知道了"}, null, getActivity(), AlertView.Style.Alert, null);
                dialog.show();
            } else if (!StringUtils.isEmpty(noticeVo.dst)) {
                startActivity(WebViewActivity.class, Constants.TITLE, noticeVo.title, Constants.URL, noticeVo.dst);
            }
        }
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (recLen != 1) {
                isCountTime = true;
                recLen--;
                mWirelessNumTv.setText(recLen + "");//倒计时数字
                handler.postDelayed(this, 1000);
            } else {
                countDown();
                mConnectIv.setVisibility(View.VISIBLE);
                mConnectIv.setImageResource(R.drawable.main_connect_btn);
                recLen = 5;
                mWirelessNumTv.setText(recLen + "");
                isCountTime = false;
                updateConnectState(false);
                if (wifiState == WifiState.CONNECTING) {
                    ToastUtil.showMiddle(getActivity(), R.string.connect_time_out);
                }
            }
        }
    };

    //隐藏倒计时等view
    public void countDown() {
        mCircleIv.clearAnimation();
        mCircleIv.setVisibility(View.GONE);
        mWirelessTimesLay.setVisibility(View.GONE);
        mWirelessCircleIv.setVisibility(View.GONE);
    }

    //开始倒计时等view
    public void countTime() {
        stopAnimation();
        handler.postDelayed(runnable, 1000);// 倒计时
        mCircleIv.startAnimation(animation);//旋转的白线
        mCircleIv.setVisibility(View.VISIBLE);
        mConnectIv.setVisibility(View.GONE);//一键连接图
        mConnectText.setText(R.string.concect_dg_free);//底部连接提示
        mWirelessCircleSmall.setVisibility(View.VISIBLE);//白线中间的圈
        mWirelessCircleBig.setVisibility(View.VISIBLE);//白线最外部的圈
        mWirelessCircleIv.setVisibility(View.VISIBLE);//白色中间无文字的图
        mWirelessTimesLay.setVisibility(View.VISIBLE);//倒计时容器
    }

    private FWManager.WifiObserver wifiObserver = new FWManager.WifiObserver() {
        @Override
        public void onStateChanged(WifiState new_state, WifiState old_state) {
            if (new_state == WifiState.CONNECTING) { // 连接中
                currentAp = FWManager.getInstance().getCurrent();
                if (currentAp != null && currentAp.ssid.equals(Constants.SSID)) {
                    countTime();
                }
            } else if (new_state == WifiState.CONNECTED) {  // 连上网
                currentAp = FWManager.getInstance().getCurrent();
                if (currentAp != null && currentAp.ssid.equals(Constants.SSID)) {
                    CheckAndLogon();
                } else {
                    updateConnectState(true);
                }

            } else if (new_state == WifiState.DISABLED || new_state == WifiState.DISCONNECTED) {
                // 断开网
                updateConnectState(false);
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
                        } /*else {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
                            getActivity().startActivity(intent);
                        }*/
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
                Thread.sleep(1000);//防止出现500错误
                mCheckRet = NetWorkWarpper.checkEnv();
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
                    case Constants.UNKNOW_ERROR://-1:未知异常
                        ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
                        break;
                    case Constants.NET_DISCONNECT://0：无网络
                       // ToastUtil.showMiddle(getActivity(), R.string.internet_error);
                        break;
                    case Constants.NET_OK://1：无须认证的网络（可以上网）
                        isValidate = true;
                        // 判断下按钮的状态
                        updateConnectState(true);
                        break;
                    case Constants.NEED_VALIDATE://2：须要认证的网络
                        if (iWirelessPresenter != null) {
                            String wlanacname = SPUtils.get(getActivity(), "wlanacname", "");
                            String wlanuserip = SPUtils.get(getActivity(), "wlanuserip", "");
                            String wlanacip = SPUtils.get(getActivity(), "wlanacip", "");
                            String wlanusermac = SPUtils.get(getActivity(), "wlanusermac", "");
                            iWirelessPresenter.wifiConnect(wlanacname, wlanuserip, wlanacip, wlanusermac, getCurrentWifiMacAddress());
                        }
                        break;
                    default:
                        ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
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

    @Override
    public void wifiConnectSuccess() {
        isValidate = true;
        // 判断下按钮的状态
        updateConnectState(false);
    }

    @Override
    public void wifiConnectFailed() {
        ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
        // 判断下按钮的状态
        updateConnectState(false);
    }

    //连接后改变状态
    public void updateConnectState(boolean updateNews) {
        currentAp = FWManager.getInstance().getCurrent();//当前连接的wifi
        wifiState = FWManager.getInstance().getState();
        mDGFreeConnected = false;
        if (currentAp != null && wifiState == WifiState.CONNECTED) {
            String ssidText = "";
            if (currentAp.ssid.equals(Constants.SSID) && isValidate) {
                if (!isCountTime) {
                    mConnectIv.setVisibility(View.VISIBLE);
                    mConnectIv.setImageResource(R.drawable.main_connect_btn);
                }
                if (isAdded()) {
                    ssidText = getResources().getString(R.string.connect_wifi) + getResources().getString(R.string.connect_dg_success);
                }
                if (currentAp != null) {
                    mConnectText.setText(ssidText);
                }
                mDGFreeConnected = true;
                lineViewVisible(true);
            } else {
                lineViewVisible(false);
                if (isAdded()) {
                    if (currentAp != null) {
                        mConnectText.setText(getResources().getString(R.string.connect_wifi) + currentAp.ssid);
                    }
                }
            }

        } else {
            lineViewVisible(false);
            AccessPoint DGWifiAp = getDGWifiFromList();//找出附近wifi列表中的东莞wifi
            if (DGWifiAp != null) {
                mConnectText.setText(R.string.find_wifi);
            } else {
                mConnectText.setText(R.string.wireless_dg);
            }
        }
        if (mDGFreeConnected) {
            stopAnimation();
        } else {
            startAnimation();
        }
        if (updateNews) {
            // 网络更新时尝试刷新新闻
            new Thread(new RefreshNewsThread()).start();
        }
    }

    public void lineViewVisible(boolean isVisible) {
        mWirelessCircleSmall.setVisibility(isVisible ? View.VISIBLE : View.GONE);//白线中间的圈
        mWirelessCircleBig.setVisibility(isVisible ? View.VISIBLE : View.GONE);//白线最外部的圈
    }

    private void startAnimation() {
        if (isCountTime) {
            mAnimationTv.setVisibility(View.GONE);
            mAnimationTv.stop();
            mConnectIv.setVisibility(View.GONE);
        } else {
            mAnimationTv.setVisibility(View.VISIBLE);
            mConnectIv.setVisibility(View.VISIBLE);
            mConnectIv.setImageResource(R.drawable.need_connect);
            mAnimationTv.start();
        }
    }

    private void stopAnimation() {
        mAnimationTv.setVisibility(View.GONE);
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
        final AccessPoint DGFreeAp = getDGWifiFromList();
        // 1. 未联网，有DG-Free: 连接DG-Free
        if (!NetUtils.isWifi(getActivity())) {//true为已打开但未连接wifi
            if (DGFreeAp != null) {//不为空表示周围有东莞wifi
                FWManager.getInstance().connect(DGFreeAp);//先连接上wifi,再认证
            } else { // 4. 未联网，没有DG-Free：跳转到wifi列表
                startActivity(WifiManagerActivity.class, "", "", "", "");
            }
        } else {
            //已连上wifi
            currentAp = FWManager.getInstance().getCurrent();
            // 3. 已经连上DG-Free的情况
            if (currentAp != null && !StringUtils.isEmpty(currentAp.ssid) && currentAp.ssid.equals(Constants.SSID)) {
                CheckAndLogon();
            } else if (DGFreeAp != null) {
                // 2. 已经连上其它WiFi，周围有DG-Free的情况，询问是否连接DG-Free
                if (currentAp != null && !StringUtils.isEmpty(currentAp.ssid)) {
                    switchAlert = new AlertView("温馨提示", "您已连上" + currentAp.ssid + ",确定要切换吗？", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, new com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position != AlertView.CANCELPOSITION) {
                                FWManager.getInstance().connect(DGFreeAp);
                                switchAlert.dismiss();
                                //startActivity(WifiManagerActivity.class, "", "", "", "");
                            }
                        }
                    }).setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                        }
                    });
                    switchAlert.show();
                }
            } else {
                //已连上wifi，周围没有DG-free
                ToastUtil.showMiddle(getActivity(), R.string.notice_toast);
            }
        }
    }

    @Override
    public void onRefresh() {
        mMainRefreshLay.setRefreshing(false);
        if (NetUtils.isNetworkAvailable(getActivity())) {
            if (iWirelessPresenter != null) {
                iWirelessPresenter.weatherNews();
            }
        } else {
            ToastUtil.showMiddle(getActivity(), R.string.net_error);
        }
    }

    public class RefreshNewsThread implements Runnable {
        @Override
        public void run() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if (NetUtils.isNetworkAvailable(getActivity())) {
                        Message message = new Message();
                        message.what = 1;
                        refreshNewsHandler.sendMessage(message);
                    }
                }
            }, 2000);
        }
    }

    public Handler refreshNewsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//网络可用则刷新新闻列表
                if (iWirelessPresenter != null) {
                    iWirelessPresenter.weatherNews();
                }
            }
        }
    };

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        int index = event.getChildMsg();
        if (event.getMsg() == Constants.MAIN_NEWS_FLAG && index != -1) {//上报
            iWirelessPresenter.clickCount(mainNewsVos.get(index).id, NEWS, "");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iWirelessPresenter.onDestroy();
        mAnimationTv.destroyBitMap();
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
            if (isAdded()) {
                StatusBarColor.compat(getActivity(), getResources().getColor(R.color.blue_009CFB));
            }
            mTitleLay.setBackgroundColor(Color.parseColor("#009CFB"));
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg);
        } else {
            if (isAdded()) {
                StatusBarColor.compat(getActivity(), getResources().getColor(R.color.blue_236EC5));
            }
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
        List<AccessPoint> apList = FWManager.getInstance().getList();//先拿到附近列表
        if (mBadgeView != null) {
            mBadgeView.setBadgeCount(apList.size());
        }
        updateConnectState(false);
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
}
