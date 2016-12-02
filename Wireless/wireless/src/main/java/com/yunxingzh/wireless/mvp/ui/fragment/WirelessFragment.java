package com.yunxingzh.wireless.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import com.yunxingzh.wireless.config.MyApplication;
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
import com.yunxingzh.wireless.mvp.ui.utils.MyScrollView;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.Utility;
import com.yunxingzh.wireless.mvp.ui.utils.WifiUtils;
import com.yunxingzh.wireless.mvp.view.CircleWaveView;
import com.yunxingzh.wireless.mvp.view.IConnectDGCountView;
import com.yunxingzh.wireless.mvp.view.IHeadLineView;
import com.yunxingzh.wireless.mvp.view.ScrollViewListener;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.WifiState;
import com.yunxingzh.wirelesslibs.convenientbanner.ConvenientBanner;
import com.yunxingzh.wirelesslibs.convenientbanner.holder.CBViewHolderCreator;
import com.yunxingzh.wirelesslibs.convenientbanner.listener.OnItemClickListener;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.FontInfoVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.NewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WeatherNewsVo;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.NetUtils;
import com.yunxingzh.wirelesslibs.wireless.lib.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephon_ on 2016/11/1.
 * 无线
 */

public class WirelessFragment extends BaseFragment implements IHeadLineView, IConnectDGCountView, View.OnClickListener, ScrollViewListener {

    private final static String TAG = "WirelessFragment";
    private final static int HEAD_LINE_TYPE = 0;//0-新闻 1-视频 2-应用 3-游戏
    private final static int HEAD_LINE_SEQ = 0;//序列号，分页拉取用
    private final static int PULL_HEIGHT = 10;
    private final static int HEIGHT = 0;
    private final static int ITEM = 0;
    private final static int NEWS = 1;//新闻点击上报
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int DG_SDK_TIME_OUT = 10 * 1000;

    private final static int SCROLL_STOP = 0;
    private final static int SCROLL_DOWN = 1;
    private final static int SCROLL_UP = -1;

    private int mPageHeight;
    private FragmentManager fragmentManager;

    private LinearLayout mNoticeLay, mMainWifiManager, mMainMapLay, mMainSpeedtest,
            mMainHeadImg, mWeatherLay, mMainSpiritedLay;
    private MyScrollView scrollView;
    private TextView mConnectCountTv,
            mEconomizeTv, mFontNewsTv, mFontVideoTv, mFontServiceTv, mFontZhiTv, mFontPlayingTv, mFontBuyingTv;
    private ImageView mShowMoreIv, mTitleRightIv, mWeatherImgBottom, mWeatherImgTop,mConnectIv;
    private ListView mMainNewsLv;
    private IHeadLinePresenter iHeadLinePresenter;
    private IConnectDGCountPresenter iConnectDGCountPresenter;

    private CircleWaveView mAnimationTv;
    private TextView footView, mConnectText;
    private List<WeatherNewsVo.WeatherNewsData.mainNewsVo> mainNewsVos;
    private MainNewsAdapter mainNewsAdapter;

    private List<FontInfoVo.FontData.BannersVo> bannersVo;
    private FontInfoVo.FontData.UserVo userVo;
    private TextView mMainTemperature, mMainWeather;
    private WeatherNewsVo.WeatherNewsData.WeatherVo weatherNewsData;
    private ConvenientBanner mAdRotationBanner;

    private int mScrollDirection = SCROLL_STOP;
    private WifiUtils wifiUtils = null;
    private CheckEnvTask mCheckTask = null;
    private AccessPoint currentAp;

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

//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    scrollView.startScrollerTask();
//                }
//                return false;
//            }
//        });
//        scrollView.setOnScrollStoppedListener(new MyScrollView.OnScrollStoppedListener() {
//            public void onScrollStopped() {
//                int scrollPos = scrollView.getScrollY();
//                // 向上滑且小于1屏，回到顶部
//                if (scrollPos < mPageHeight && mScrollDirection == SCROLL_UP) {
//                    scrollView.smoothScrollTo(0, 0);
//                } else if (scrollPos < mPageHeight && mScrollDirection == SCROLL_DOWN) {
//                    // 向下滑且小于1屏，去到新闻列表
//                    scrollView.smoothScrollTo(0, mPageHeight);
//                }
//            }
//        });
    }

    public void initData() {
        wifiUtils = new WifiUtils(getActivity());
        //注册EventBus
        EventBus.getDefault().register(this);
        fragmentManager = getFragmentManager();
        iConnectDGCountPresenter = new ConnectDGCountPresenterImpl(this);
        iHeadLinePresenter = new HeadLinePresenterImpl(this);
        mAdRotationBanner.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        iHeadLinePresenter.weatherNews();

        FWManager.getInstance().addWifiObserver(wifiObserver);
        //获取屏幕宽高
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//720,1536
        int height = wm.getDefaultDisplay().getHeight();//1280,2560
        if (width <= 720 && height <= 1280){
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
    public void getHeadLineSuccess(NewsVo newsVo) {

    }

    @Override
    public void weatherNewsSuccess(WeatherNewsVo weatherNewsVo) {
        weatherNewsData = weatherNewsVo.getData().getWeather();
        mainNewsVos = weatherNewsVo.getData().getNews();
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
        //新闻
        if (weatherNewsVo != null) {
            mainNewsVos = weatherNewsVo.getData().getNews();
        }
        mainNewsAdapter = new MainNewsAdapter(getActivity(), mainNewsVos);

        mMainNewsLv.setAdapter(mainNewsAdapter);
        Utility.setListViewHeight(mMainNewsLv, Constants.LISTVIEW_ITEM_HEIGHT);
        //时间
        String hour = StringUtils.getTime();
        int h = Integer.parseInt(hour);
        if (h >= 6 && h < 19) {
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg);
        } else {
            mMainHeadImg.setBackgroundResource(R.drawable.main_bg_night);
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
            List<String> imageList = new ArrayList<String>(bannersVo.size());
            for (FontInfoVo.FontData.BannersVo bannersList : bannersVo) {
                imageList.add(bannersList.getImg());
            }
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
                String url = bannersVo.get(position).getDst();
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

                currentAp = FWManager.getInstance().getCurrent();//当前连接的wifi
                if (currentAp != null) {
                    mAnimationTv.setCoreImage(R.drawable.main_connected);
                    mAnimationTv.stop();
                    String connText;
                    if (currentAp.equals(Constants.SSID)) {
                        connText = getResources().getString(R.string.connect_wifi) + getResources().getString(R.string.connect_dg_success);
                        mConnectText.setText(connText);
                    } else {
                        connText = getResources().getString(R.string.connect_wifi) + currentAp.ssid;
                        mConnectText.setText(connText);
                    }
                }
                // iConnectDGCountPresenter.connectDGCount();
            } else {
                ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
            }
        }
    };

    private void CheckAndLogon() {
        if (mCheckTask != null) {
            return;
        }
        mCheckTask = new CheckEnvTask();
        mCheckTask.execute((Void) null);
    }

    final Handler logoOutHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtil.showMiddle(getActivity(), "下线成功");
                    break;
                default:
                    ToastUtil.showMiddle(getActivity(), "下线失败");
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (mConnectIv == v) {//一键连接
            if (wifiUtils.getWlanState()) {//是否打开
                checkDGWifi();
            } else {
                ToastUtil.showMiddle(getActivity(), R.string.please_open_wifi);
            }
            //  WifiInterface.wifiLogout(logoOutHandler,MyApplication.sApplication.getUserName(),5000);
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

    private void onNetChange() {
        changeState();
        iHeadLinePresenter.weatherNews();
    }

    private FWManager.WifiObserver wifiObserver = new FWManager.WifiObserver() {
        @Override
        public void onStateChanged(WifiState new_state, WifiState old_state) {
            if (new_state == WifiState.CONNECTED) {
                currentAp = FWManager.getInstance().getCurrent();
                if (currentAp != null && currentAp.ssid.equals(Constants.SSID)) {
                    CheckAndLogon();
                } else {
                    // 先不处理
                }
                onNetChange();
            } else if (new_state == WifiState.DISABLED || new_state == WifiState.DISCONNECTED) {
                onNetChange();
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
                        CheckAndLogon();
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
                        WifiInterface.wifiLogon(validateHandler, MyApplication.getInstance().getUserName(),
                                MyApplication.getInstance().getWifiPwd(), DG_SDK_TIME_OUT);//wifi认证
                        break;
                    case Constants.VALIDATE_SUCCESS://1、已经认证成功。
                        ToastUtil.showMiddle(getActivity(), R.string.connect_success);
                        //iConnectDGCountPresenter.connectDGCount();
                        break;
                    default:
                        ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
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

    public class CheckEnvThread implements Runnable {
        @Override
        public void run() {
            try {
                int checkResult = WifiInterface.checkEnv(DG_SDK_TIME_OUT);
                Message message = new Message();
                message.what = checkResult;
                checkHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Handler checkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NET_OK://0、网络正常，可以发起调用认证、下线等接口
                    WifiInterface.wifiLogon(validateHandler, MyApplication.sApplication.getUserName(), MyApplication.sApplication.getWifiPwd(), DG_SDK_TIME_OUT);//wifi认证
                    break;
                case Constants.VALIDATE_SUCCESS://1、已经认证成功。
                    ToastUtil.showMiddle(getActivity(), R.string.connect_success);
                    //iConnectDGCountPresenter.connectDGCount();
                    break;
                default:
                    ToastUtil.showMiddle(getActivity(), R.string.validate_faild);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //连接后改变状态
    public void changeState() {
        currentAp = FWManager.getInstance().getCurrent();//当前连接的wifi
        if (currentAp != null) {
            mAnimationTv.setVisibility(View.GONE);
            mConnectIv.setVisibility(View.VISIBLE);
            mConnectIv.setImageResource(R.drawable.main_connected);
            mAnimationTv.stop();
            String ssidText;
            if (currentAp.ssid.equals(Constants.SSID)) {
                ssidText = getResources().getString(R.string.connect_wifi) + getResources().getString(R.string.connect_dg_success);
                mConnectText.setText(ssidText);
            } else {
                mConnectText.setText(getResources().getString(R.string.connect_wifi) + currentAp.ssid);
            }
        } else {
            mAnimationTv.setVisibility(View.VISIBLE);
            mConnectIv.setVisibility(View.VISIBLE);
            mConnectIv.setImageResource(R.drawable.need_connect);
            mAnimationTv.start();

            AccessPoint DGWifiAp = getDGWifiFromList();//找出附近wifi列表中的东莞wifi
            if (DGWifiAp != null) {
                mConnectText.setText(R.string.find_wifi);
            } else {
                mConnectText.setText(R.string.wireless_dg);
            }
        }
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
//        if (y < oldy) {
//            mScrollDirection = SCROLL_UP;
//        } else if (y > oldy) {
//            mScrollDirection = SCROLL_DOWN;
//        } else {
//            mScrollDirection = SCROLL_STOP;
//        }

        if (y > PULL_HEIGHT) { //下拉高度大于10
           // mNoticeLay.setVisibility(View.GONE);
            mShowMoreIv.setVisibility(View.INVISIBLE);
        } else if (y == HEIGHT) {
           // mNoticeLay.setVisibility(View.VISIBLE);
        }

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
        currentAp = FWManager.getInstance().getCurrent();
        AccessPoint DGFreeAp = getDGWifiFromList();
        // 已经连上wifi
        if (currentAp != null) {
            if (DGFreeAp != null && currentAp.ssid.equals(DGFreeAp.ssid)) {
                CheckAndLogon();
            } else {
                //已连上普通wifi
                ToastUtil.showMiddle(getActivity(), "已经连接 "+currentAp.ssid);
            }
        } else if (DGFreeAp != null) {
            FWManager.getInstance().connect(DGFreeAp);
        } else {
            startActivity(WifiManagerActivity.class, "", "", "", "");
        }
    }

    @Subscribe
    public void onEventMainThread(EventBusType event) {
        int index = event.getChildMsg();
        if (event.getMsg() == Constants.MAIN_NEWS_FLAG && index != -1) {
            iHeadLinePresenter.clickCount(mainNewsVos.get(index).getId(), NEWS);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdRotationBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        changeState();
        mAdRotationBanner.startTurning(1500);
    }

    @Override
    public void connectDGCountSuccess() {

    }
}
