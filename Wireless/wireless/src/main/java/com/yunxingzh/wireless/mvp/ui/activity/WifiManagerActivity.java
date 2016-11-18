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
import com.yunxingzh.wireless.mvp.ui.utils.LocationUtils;
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

    private LocationUtils locationUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_manager);
        initView();
        initData();
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
        locationUtils = LocationUtils.getInstance(this);
        locationUtils.getLocation();
        locationUtils.startMonitor();//开始定位

        iWifiManagerPresenter = new WifiManagerPresenterImpl(this);

        iWifiManagerPresenter.getWifi(locationUtils.getBaseLocation().longitude,locationUtils.getBaseLocation().latitude);//从服务器获取附近wifi

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationUtils.stopMonitor();
    }
}
