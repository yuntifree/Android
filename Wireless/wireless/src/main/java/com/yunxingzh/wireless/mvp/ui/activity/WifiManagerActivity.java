package com.yunxingzh.wireless.mvp.ui.activity;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IWifiManagerPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WifiManagerPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.WifiManagerAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.WifiPswDialog;
import com.yunxingzh.wireless.mvp.ui.utils.WifiUtils;
import com.yunxingzh.wireless.mvp.view.IWifiManagerView;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiVo;

import java.util.List;

/**
 * Created by stephon on 2016/11/12.
 * wifi管理
 */

public class WifiManagerActivity extends BaseActivity implements IWifiManagerView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        CompoundButton.OnCheckedChangeListener {

    private TextView mTitleNameTv;
    private ImageView mTitleReturnIv;
    private ToggleButton mSwitchBtn;
    private SwipeRefreshLayout mSwipeWifi;
    private RecyclerView mWifiRv;
    private WifiManagerAdapter wifiManagerAdapter;
    private List<ScanResult> scanResultList;
    private WifiUtils wifiMa;
    private List<WifiConfiguration> wifiConfigurationList;//已经输入密码连接过的wifi
    private String wifiPassword = null;

    private List<WifiVo.WifiData.MWifiInfo> mWifiInfos;
    private IWifiManagerPresenter iWifiManagerPresenter;


    //private LocationUtils locationUtils;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_manager);
        initView();
        initData();
        onCreats();
        initLocation();
        mLocationClient.start();
    }

    public void onCreats() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            ToastUtil.showMiddle(WifiManagerActivity.this, sb.toString());
        }
    }

    public void initView() {
        mTitleNameTv = findView(R.id.title_name_tv);
        mTitleNameTv.setVisibility(View.VISIBLE);
        mTitleNameTv.setText(R.string.wireless_list);
        mTitleReturnIv = findView(R.id.title_return_iv);
        mTitleReturnIv.setOnClickListener(this);
        mWifiRv = findView(R.id.wifi_rv);
        mSwipeWifi = findView(R.id.swipe_wifi);
        mSwitchBtn = findView(R.id.switch_btn);
        mSwitchBtn.setVisibility(View.VISIBLE);
        mSwitchBtn.setOnCheckedChangeListener(this);
        mWifiRv.setLayoutManager(new LinearLayoutManager(this));
        mWifiRv.addItemDecoration(new SpacesItemDecoration(Constants.ITEM_HEIGHT));
        mSwipeWifi.setOnRefreshListener(this);
    }


    public void initData() {
       // locationUtils = LocationUtils.getInstance(this);
        iWifiManagerPresenter = new WifiManagerPresenterImpl(this);

        wifiMa = new WifiUtils(this);
        if (wifiMa.getWlanState()) {
            mSwitchBtn.setChecked(true);
        }
        mWifiRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, final View view, int i) {
                List<ScanResult> scanResult = baseQuickAdapter.getData();
                final int netWorkId = wifiMa.isConfiguration(scanResult.get(i).SSID);

                if (netWorkId != -1) {
                    if (wifiMa.connectWifi(netWorkId)) {//连接指定WIFI
                        view.setBackgroundResource(R.color.blue_00A0FB);
                        ToastUtil.showMiddle(WifiManagerActivity.this, "显示当前wifi详情");
                    }
                } else {//没有配置好信息，配置
                    WifiPswDialog pswDialog = new WifiPswDialog(WifiManagerActivity.this, new WifiPswDialog.OnCustomDialogListener() {
                        @Override
                        public void back(String str) {
                            wifiPassword = str;
                            if (wifiPassword != null) {
                                int netId = wifiMa.addWifiConfig(scanResultList, String.valueOf(netWorkId), wifiPassword);
                                if (netId != -1) {
                                    wifiMa.getConfiguration();//添加了配置信息，要重新得到配置信息
                                    if (wifiMa.connectWifi(netId)) {
                                        view.setBackgroundResource(R.color.green_1fbd22);
                                    }
                                } else {
                                    ToastUtil.showMiddle(WifiManagerActivity.this, R.string.internet_error);
                                    view.setBackgroundResource(R.color.red);
                                }
                            } else {
                                view.setBackgroundResource(R.color.yellow_db5800);
                            }
                        }
                    });
                    pswDialog.show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mTitleReturnIv == v) {
            finish();
        }
    }

    @Override
    public void onRefresh() {
        if (scanResultList != null) {
            scanResultList.clear();
        }
        ToastUtil.showMiddle(this, R.string.refreshing);
        new Thread(new RefreshWifiThread()).start();
    }

    @Override
    public void getWifiSuccess(WifiVo wifiVo) {
        mWifiInfos = wifiVo.getData().getInfos();
    }

    public class RefreshWifiThread implements Runnable {
        @Override
        public void run() {
            try {
                //耗时操作
                wifiMa.wifiOpen();

                //iWifiManagerPresenter.getWifi(locationUtils.getBaseLocation().longitude,locationUtils.getBaseLocation().latitude);//从服务器获取附近wifi

                wifiMa.wifiStartScan();//开始扫描
                Thread.sleep(3000);
                Message message = new Message();
                message.what = 1;

                scanResultList = wifiMa.getScanResults();//得到扫描结果
                refreshWifiHandler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    final Handler refreshWifiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mSwipeWifi.setRefreshing(false);
                    wifiConfigurationList = wifiMa.getConfiguration();//得到已经配置好的列表
                    wifiManagerAdapter = new WifiManagerAdapter(scanResultList);
                    mWifiRv.setAdapter(wifiManagerAdapter);
                    mWifiRv.swapAdapter(wifiManagerAdapter, true);//刷新列表
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ToastUtil.showMiddle(this, R.string.opening);
            new Thread(new RefreshWifiThread()).start();
        } else {
            wifiMa.wifiClose();
            scanResultList.clear();
            mWifiRv.swapAdapter(wifiManagerAdapter, true);//刷新列表
        }
    }
}
