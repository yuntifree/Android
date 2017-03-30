package com.yunxingzh.wireless.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.config.EventBusType;
import com.yunxingzh.wireless.mview.CircleWaveView;
import com.yunxingzh.wireless.mview.MyListview;
import com.yunxingzh.wireless.mview.StatusBarColor;
import com.yunxingzh.wireless.mview.alertdialog.AlertView;
import com.yunxingzh.wireless.mview.alertdialog.OnDismissListener;
import com.yunxingzh.wireless.mvp.presenter.IWirelessPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WirelessPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.activity.ScanCodeActivity;
import com.yunxingzh.wireless.mvp.ui.activity.SpeedTestActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WebViewActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiMapActivity;
import com.yunxingzh.wireless.mvp.ui.activity.WifiSpiritedActivity;
import com.yunxingzh.wireless.mvp.ui.adapter.MainNewsAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseFragment;
import com.yunxingzh.wireless.mvp.view.IWirelessView;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wireless.libs.bean.resp.WeatherNewsList;
import wireless.libs.bean.vo.MainNewsVo;
import wireless.libs.bean.vo.NoticeVo;
import wireless.libs.bean.vo.WeatherVo;
import wireless.libs.network.request.NetWorkWarpper;

/**
 * Created by stephon_ on 2016/11/1.
 * 无线
 */

public class WirelessFragment extends BaseFragment implements IWirelessView, View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, SwipeRefreshLayout.OnRefreshListener {

    private final static int NEWS = 1;//新闻点击上报
    private final static int SCANNIN_GREQUEST_CODE = 1;

    private LinearLayout mMachineErrorLay, mMainHeadImg, mWeatherLay, mTitleLay, mWirelessTimesLay, mTitleLeftLay,
            mTitleForWirelessLay, mMoreNewsLay, mWirelessConnectedBtnLay, mWirelessMineLay, mNoResourceLay;
    private FrameLayout mCenterCircleLay;
    private TextView mTitleNameTv, mWirelessNumTv, mConnectText, mWirelessNickTv, mConnectTv, mNoResourceTv;
    private ImageView mTitleRightIv, mWeatherImgBottom, mWeatherImgTop, mConnectIv, mTitleMainImg,
            mCircleIv, mWirelessCircleIv, mWirelessCircleBig, mWirelessCircleSmall, mMachineErrorIv,
            mSpeedIv, mSpiritedIv;
    private MyListview mMainNewsLv;
    private IWirelessPresenter iWirelessPresenter;
    private CircleWaveView mAnimationTv;
    private List<MainNewsVo> mainNewsVos;
    private MainNewsAdapter mainNewsAdapter;
    private SwipeRefreshLayout mMainRefreshLay;

    private TextView mMainTemperature, mMainWeather, mMachineErrorTv;
    private WeatherVo weatherNewsData;
    private NoticeVo noticeVo;

    private WifiUtils wifiUtils = null;
    private CheckEnvTask mCheckTask = null;
    private AccessPoint currentAp;

    private boolean mDGFreeConnected = false;//是否连接了东莞wifi
    private RotateAnimation animation;
    private int recLen = 5;
    private Handler handler = new Handler();
    private WindowManager wm;
    private WifiState wifiState;
    // private CircleImageView mWirelessHeadImgIv;

    private boolean isValidate = false;//东莞wifi是否认证通过
    private boolean isCountTime = false;//true打开（start），false关闭（stop）
    private AlertView alertView;
    private JumpHeadLine jumpHeadLine;
    public static boolean localClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wireless, null);
        initView(view);
        initData();
        if (isDGWifi()) {
            CheckAndLogon();
        }
        return view;
    }

    public void initView(View view) {
        mTitleLeftLay = findView(view, R.id.title_left_lay);
        mTitleLeftLay.setVisibility(View.GONE);
        //头像
//        mTitleForWirelessLay = findView(view, R.id.title_for_wireless_lay);
//        mTitleForWirelessLay.setVisibility(View.VISIBLE);
//        mTitleForWirelessLay.setOnClickListener(this);
//        mWirelessNickTv = findView(view, R.id.wireless_nick_tv);
//        mWirelessHeadImgIv = findView(view, R.id.wireless_head_img_iv);
        mWirelessMineLay = findView(view, R.id.wireless_mine_lay);
        mWirelessMineLay.setVisibility(View.VISIBLE);
        mWirelessMineLay.setOnClickListener(this);

        mTitleNameTv = findView(view, R.id.title_name_tv);
        mTitleNameTv.setVisibility(View.GONE);
        mTitleMainImg = findView(view, R.id.title_main_img);
        mTitleMainImg.setVisibility(View.VISIBLE);
        mTitleLay = findView(view, R.id.title_lay);
        mTitleRightIv = findView(view, R.id.title_right_iv);
        mTitleRightIv.setVisibility(View.VISIBLE);
        mTitleRightIv.setOnClickListener(this);

        mCenterCircleLay = findView(view, R.id.center_circle_lay);
        mWirelessConnectedBtnLay = findView(view, R.id.wireless_connected_btn_lay);
        mSpeedIv = findView(view, R.id.speed_iv);
        mSpeedIv.setOnClickListener(this);
        mSpiritedIv = findView(view, R.id.spirited_iv);
        mSpiritedIv.setOnClickListener(this);

        mNoResourceLay = findView(view, R.id.no_resource_lay);
        mNoResourceTv = findView(view, R.id.no_resource_tv);
        mNoResourceTv.setOnClickListener(this);

        mMainNewsLv = findView(view, R.id.main_news_lv);
        mAnimationTv = findView(view, R.id.animation_tv);
        mConnectTv = findView(view, R.id.connect_tv);
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

        mMainTemperature = findView(view, R.id.main_temperature);
        mMainWeather = findView(view, R.id.main_weather);
        mMainHeadImg = findView(view, R.id.main_head_img);
        mWeatherLay = findView(view, R.id.weather_lay);
        mWeatherLay.setOnClickListener(this);
        mMoreNewsLay = findView(view, R.id.more_news_lay);
        mMoreNewsLay.setOnClickListener(this);
        mConnectText = findView(view, R.id.connect_text);

        mWeatherImgBottom = findView(view, R.id.weather_img_bottom);
        mWeatherImgTop = findView(view, R.id.weather_img_top);

        setAnimation(mWeatherImgBottom, 0, 60, 0, 0);
        setAnimation(mWeatherImgTop, 60, 0, 0, 0);
    }

    public void initData() {
        if (isAdded() && getActivity() != null) {
            wifiUtils = new WifiUtils(getActivity());
            //注册EventBus
            EventBus.getDefault().register(this);
            // mBadgeView.set
            iWirelessPresenter = new WirelessPresenterImpl(this);
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
//        String nickName = MainApplication.get().getNick();
//        String headurl = MainApplication.get().getHeadUrl();
//        if (!StringUtils.isEmpty(nickName)) {
//            mWirelessNickTv.setText(nickName);
//        } else {
//            mWirelessNickTv.setText("东莞无限");
//        }
//        if (!StringUtils.isEmpty(headurl)) {
//        if (Util.isOnMainThread()) {
//            Glide.with(getActivity()).load(headurl).into(mWirelessHeadImgIv);
//       }
//        } else {
//        if (Util.isOnMainThread()) {
//            Glide.with(getActivity()).load(R.drawable.my_ico_pic).into(mWirelessHeadImgIv);
//        }
//        }
        }
    }

    @Override
    public void weatherNewsSuccess(WeatherNewsList weatherNewsVo) {
        mNoResourceLay.setVisibility(View.GONE);
        if (weatherNewsVo == null) {
            return;
        }
        weatherNewsData = weatherNewsVo.weather;
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
        if (isAdded() && getActivity() != null) {
            mainNewsAdapter = new MainNewsAdapter(getActivity(), mainNewsVos);
            mMainNewsLv.setAdapter(mainNewsAdapter);
            Utility.setListViewHeight(mMainNewsLv);
        }
    }

    @Override
    public void weatherNewsFailed() {
        mNoResourceLay.setVisibility(View.VISIBLE);
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
        if (isAdded() && getActivity() != null) {
            if (mConnectIv == v) {//一键连接
                if (wifiUtils.getWlanState()) {//是否打开wifi
                    checkDGWifi();
                } else {//是否跳转系统设置的wifi列表？
                    showDialog("请打开WiFi", "去设置");
                }
            } else if (mWeatherLay == v) {// 天气
                if (weatherNewsData != null) {
                    startActivity(WebViewActivity.class, Constants.URL, weatherNewsData.dst, Constants.TITLE, "东莞天气");
                }
            } else if (mMoreNewsLay == v) {//本地热点
                MobclickAgent.onEvent(getActivity(), "Index_QR");
               // EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
                jumpHeadLine.jumpDgNews();
                localClick = true;
            } /*else if (mMainWifiManager == v) {//wifi管理
            startActivity(WifiManagerActivity.class, "", "", "", "");
        } else if (mMainMapLay == v) {//wifi地图
            startActivity(WifiMapActivity.class, "", "", "", "");
        } */ else if (mTitleRightIv == v) {//扫码连接东莞wifi
                MobclickAgent.onEvent(getActivity(), "Index_QR");
                Intent intent = new Intent();
                intent.setClass(getActivity(), ScanCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            } else if (mSpeedIv == v) { // wifi 测速
                MobclickAgent.onEvent(getActivity(), "Index_speedtest");
                startActivity(SpeedTestActivity.class, "", "", "", "");
            } else if (mSpiritedIv == v) {//wifi共享
                MobclickAgent.onEvent(getActivity(), "Index_share_wifi");
                if (isDGWifi()) {
                    ToastUtil.showMiddle(getActivity(), "您连接的是东莞无限免费WIFI，无需分享噢");
                } else if (currentAp != null && !StringUtils.isEmpty(currentAp.ssid)) {
                    startActivity(WifiSpiritedActivity.class, "ssid", currentAp.ssid, "", "");
                } else {
                    startActivity(WifiSpiritedActivity.class, "", "", "", "");
                }
            } /*else if (mFontNewsTv == v) { // 东莞头条
            AppUtils.animation(mFontNewsTv);
            EventBus.getDefault().post(new EventBusType(Constants.HEAD_LINE));
        } else if (mFontVideoTv == v) { //热门视频
            AppUtils.animation(mFontVideoTv);
            EventBus.getDefault().post(new EventBusType(0, Constants.VIDEO));
        } else if (mFontServiceTv == v) { //同城服务
            AppUtils.animation(mFontServiceTv);
            // EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } else if (mFontZhiTv == v) { //智慧服务
            AppUtils.animation(mFontZhiTv);
            EventBus.getDefault().post(new EventBusType(Constants.SERVICE));
        } */ else if (mNoResourceTv == v) { //无网络时点击刷新底部新闻
                if (NetUtils.isNetworkAvailable(getActivity())) {
                    if (iWirelessPresenter != null) {
                        iWirelessPresenter.weatherNews();
                    }
                } else {
                    ToastUtil.showMiddle(getActivity(), R.string.net_error);
                }
            } else if (mWirelessMineLay == v) {//个人中心
                MobclickAgent.onEvent(getActivity(), "Index_userinfo");
                EventBus.getDefault().post(new EventBusType(Constants.MINE));
            } else if (mMachineErrorIv == v) {//不可抗力异常关闭
                mMachineErrorLay.setVisibility(View.GONE);
            } else if (mMachineErrorLay == v) {
                if (noticeVo != null) {
                    if (!StringUtils.isEmpty(noticeVo.content)) {
                        AlertView dialog = new AlertView("通知", noticeVo.content, null, new String[]{"我知道了"}, null, getActivity(), AlertView.Style.Alert, null);
                        dialog.show();
                    } else if (!StringUtils.isEmpty(noticeVo.dst)) {
                        startActivity(WebViewActivity.class, Constants.TITLE, noticeVo.title, Constants.URL, noticeVo.dst);
                    }
                }
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
            } else if (recLen == 1) {
                countDown();
                mConnectIv.setVisibility(View.VISIBLE);
                recLen = 5;
                mWirelessNumTv.setText(recLen + "");
                isCountTime = false;
                if (isAdded() && getActivity() != null) {
                    if (wifiState == WifiState.CONNECTING || wifiState == WifiState.IDLE) {
                        mConnectText.setText(R.string.has_dgwifi);
                        showDialog("连接失败，请手动在设置中连接", "去连接");
                        MobclickAgent.onEvent(getActivity(), "connect_error");
                    }
                }
                updateConnectState(false);
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
                if (isDGWifi()) {
                    countTime();//连接东莞wifi倒计时
                }
            } else if (new_state == WifiState.CONNECTED) {  // 连上网
                if (isAdded() && getActivity() != null && isDGWifi()) {
                   // FWServiceManager.createInform(getActivity(), Constants.VALIDATE_FLAG);//连上东莞wifi后创建通知栏进行wifi认证
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
                            if (isAdded() && getActivity() != null) {
                                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                intent.putExtra(Constants.URL, url);
                                startActivity(intent);
                            }
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
                        if (isAdded() && getActivity() != null) {
                            ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
                        }
                        // 判断下按钮的状态
                        updateConnectState(false);
                        break;
                    case Constants.NET_DISCONNECT://0：无网络
                        updateConnectState(false);
                        // ToastUtil.showMiddle(getActivity(), R.string.internet_error);
                        break;
                    case Constants.NET_OK://1：无须认证的网络（可以上网）
                        if (currentAp != null && currentAp.ssid.equals(Constants.SSID)) {
                            isValidate = true;
                        }
                        // 判断下按钮的状态
                        updateConnectState(true);
                        break;
                    case Constants.NEED_VALIDATE://2：须要认证的网络
                        if (isAdded() && getActivity() != null && iWirelessPresenter != null) {
                            String wlanacname = SPUtils.get(getActivity(), "wlanacname", "");
                            String wlanuserip = SPUtils.get(getActivity(), "wlanuserip", "");
                            String wlanacip = SPUtils.get(getActivity(), "wlanacip", "");
                            String wlanusermac = SPUtils.get(getActivity(), "wlanusermac", "");
                            iWirelessPresenter.wifiConnect(wlanacname, wlanuserip, wlanacip, wlanusermac, getCurrentWifiMacAddress());
                        }
                        break;
                    default:
                        if (isAdded() && getActivity() != null) {
                            ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
                        }
                        break;
                }
            } else {
                if (isAdded() && getActivity() != null) {
                    ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
                }
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
        mDGFreeConnected = true;
        // 判断下按钮的状态
        updateConnectState(true);
    }

    @Override
    public void wifiConnectFailed() {
        isValidate = false;
        if (isAdded() && getActivity() != null) {
            ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
        }
        // 判断下按钮的状态
        updateConnectState(false);
    }

    //连接后改变状态
    public void updateConnectState(boolean updateNews) {
        currentAp = FWManager.getInstance().getCurrent();//当前连接的wifi
        wifiState = FWManager.getInstance().getState();
        mDGFreeConnected = false;
        if (currentAp != null) {//当前已连接wifi
            if (currentAp.ssid.equals(Constants.SSID) && isValidate) {//当前已连接东莞wifi并且认证成功
                if (!isCountTime && wifiState == WifiState.CONNECTED) {//倒计时完毕
                    mWirelessConnectedBtnLay.setVisibility(View.VISIBLE);
                    mCenterCircleLay.setVisibility(View.GONE);//连接wifi后中间圆圈等按钮隐藏
                    mConnectText.setText("已连接免费WiFi");
                    mDGFreeConnected = true;
                    lineViewVisible(true);
                } else if (!isCountTime && !isValidate && isDGWifi()) {// 认证失败
                    mWirelessConnectedBtnLay.setVisibility(View.GONE);
                    mCenterCircleLay.setVisibility(View.VISIBLE);
                    lineViewVisible(false);
                    mConnectTv.setText("一键连接");
                    mConnectText.setText(R.string.has_dgwifi);
                }
            } else {//连接其他的wifi
                lineViewVisible(false);
                if (isAdded() && currentAp != null) {
                    if (wifiState != WifiState.CONNECTING) {//如果
                        mConnectText.setText(getResources().getString(R.string.connect_wifi) + currentAp.ssid);
                        mWirelessConnectedBtnLay.setVisibility(View.VISIBLE);
                        mCenterCircleLay.setVisibility(View.GONE);//连接wifi后中间圆圈等按钮隐藏
                    }
                }
            }
        } else {// 当前未连接wifi
            mWirelessConnectedBtnLay.setVisibility(View.GONE);
            mCenterCircleLay.setVisibility(View.VISIBLE);
            lineViewVisible(false);
            AccessPoint dgWifiAp = getDGWifiFromList();//找出附近wifi列表中的东莞wifi
            if (dgWifiAp != null) {//周围有东莞wifi
                mConnectTv.setText("一键连接");
                mConnectText.setText(R.string.has_dgwifi);
            } else {
                mConnectTv.setText("找WiFi");
                mConnectText.setText("寻找附近免费WiFi");
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

    public void checkDGWifi() {
        AccessPoint DGFreeAp = getDGWifiFromList();
        // 1. 未联网，有DG-Free: 连接DG-Free
        if (isAdded() && getActivity() != null) {
            if (!NetUtils.isWifi(getActivity())) {//true为已打开但未连接wifi
                if (DGFreeAp != null) {//不为空表示周围有东莞wifi
                    FWManager.getInstance().connect(DGFreeAp);//先连接上wifi,再认证
                } else { // 4. 未联网，没有DG-Free：跳转到wifi地图
                    startActivity(WifiMapActivity.class, "", "", "", "");
                }
            } else if (wifiState == WifiState.CONNECTING_IPADDR) {
                startActivity(WifiMapActivity.class, "", "", "", "");
            } else {
                //已连上wifi
                // 3. 已经连上DG-Free的情况
                if (isDGWifi()) {
                    CheckAndLogon();
                } else {
                    updateConnectState(false);
                }
            }
        }
    }

    public boolean isDGWifi() {
        currentAp = FWManager.getInstance().getCurrent();
        if (currentAp != null && !StringUtils.isEmpty(currentAp.ssid) && currentAp.ssid.equals(Constants.SSID)) {
            return true;
        }
        return false;
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
            //mConnectIv.setImageResource(R.drawable.need_connect);
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

    @Override
    public void onRefresh() {
        mMainRefreshLay.setRefreshing(false);
        if (isAdded() && getActivity() != null) {
            if (NetUtils.isNetworkAvailable(getActivity())) {
                if (iWirelessPresenter != null) {
                    iWirelessPresenter.weatherNews();
                }
            } else {
                ToastUtil.showMiddle(getActivity(), R.string.net_error);
            }
        }
    }

    public class RefreshNewsThread implements Runnable {
        @Override
        public void run() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if (isAdded() && getActivity() != null) {
                        if (NetUtils.isNetworkAvailable(getActivity())) {
                            Message message = new Message();
                            message.what = 1;
                            refreshNewsHandler.sendMessage(message);
                        }
                    }
                }
            }, 2000);
        }
    }

    private Handler refreshNewsHandler = new Handler() {
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

//    @Subscribe
//    public void onEventMainThread(MineHeadImg event) {
//        if (event.getmFlag() == Constants.HEAD_IMG_FLAG) {
//            Glide.with(getActivity()).load(event.getmMsg()).into(mWirelessHeadImgIv);
//        }
//        if (event.getmFlag() == Constants.NICK_NAME_FLAG) {//更换昵称
//            mWirelessNickTv.setText(event.getmMsg());
//        }
//        if (event.getmFlag() == Constants.USER_MINE_FLAG) {
//            if (event.getUserInfoVo() != null) {
//                Glide.with(getActivity()).load(event.getUserInfoVo().headurl).into(mWirelessHeadImgIv);
//                mWirelessNickTv.setText(event.getUserInfoVo().nickname);
//            } else {
//                Glide.with(getActivity()).load(R.drawable.my_ico_pic).into(mWirelessHeadImgIv);
//                mWirelessNickTv.setText("东莞无限");
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iWirelessPresenter != null) {
            iWirelessPresenter.onDestroy();
        }
        mAnimationTv.destroyBitMap();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (refreshNewsHandler != null) {
            refreshNewsHandler.removeCallbacksAndMessages(null);
        }
        FWManager.getInstance().removeWifiObserver(wifiObserver);
        EventBus.getDefault().unregister(this);//反注册EventBus
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
        if (isAdded() && getActivity() != null) {
            Intent intent = new Intent(getActivity(), activity);
            intent.putExtra(key, videoUrl);
            intent.putExtra(titleKey, title);
            startActivity(intent);
        }
    }

    public void timeChanged() {
        //时间
        String hour = StringUtils.getTime();
        int h = Integer.parseInt(hour);
        if (h >= 6 && h < 19) {
            if (isAdded() && getActivity() != null) {
                StatusBarColor.compat(getActivity(), getResources().getColor(R.color.blue_009CFB));
            }
            mTitleLay.setBackgroundColor(Color.parseColor("#009CFB"));
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg);
        } else {
            if (isAdded() && getActivity() != null) {
                StatusBarColor.compat(getActivity(), getResources().getColor(R.color.blue_236EC5));
            }
            mTitleLay.setBackgroundColor(Color.parseColor("#236EC5"));
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg_night);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConnectState(false);
    }

    public interface JumpHeadLine {
        void jumpDgNews();//跳转东莞新闻
    }

    public void setJumpListener(JumpHeadLine jumpHeadLine) {
        this.jumpHeadLine = jumpHeadLine;
    }

    public void showDialog(String content, String query) {
        alertView = new AlertView("温馨提示", content, "取消", new String[]{query}, null, getActivity(), AlertView.Style.Alert, new com.yunxingzh.wireless.mview.alertdialog.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position != AlertView.CANCELPOSITION) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                    alertView.dismiss();
                }
            }
        }).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
            }
        });
        alertView.show();
    }

}
