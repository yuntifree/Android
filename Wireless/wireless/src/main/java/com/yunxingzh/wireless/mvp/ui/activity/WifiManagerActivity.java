package com.yunxingzh.wireless.mvp.ui.activity;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.ui.adapter.WifiManagerAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.WifiUtils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by stephon on 2016/11/12.
 * wifi管理
 */

public class WifiManagerActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        CompoundButton.OnCheckedChangeListener {

    private TextView mTitleNameTv;
    private ImageView mTitleReturnIv;
    private ToggleButton mSwitchBtn;
    private SwipeRefreshLayout mSwipeWifi;
    private RecyclerView mWifiRv;
    private WifiManagerAdapter wifiManagerAdapter;
    private List<ScanResult> scanResultList;
    private WifiUtils wifiMa;

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
        wifiMa = new WifiUtils(this);
        if (wifiMa.getWlanState()) {
            mSwitchBtn.setChecked(true);
        }
        mWifiRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                List<ScanResult> scanResult = baseQuickAdapter.getData();
                WifiInfo info = wifiMa.getCurrentWifiInfo();

                if (scanResult.get(i).BSSID.equals(info.getBSSID())) {//是否当前连接的wifi
                    int netWorkId = wifiMa.isConfiguration(scanResult.get(i).BSSID);
                    ToastUtil.showMiddle(WifiManagerActivity.this, "显示当前wifi详情");
                } else {
                    if (netWorkId != -1) {
                        if (localWifiUtils.ConnectWifi(wifiItemId)) {//连接指定WIFI
                            arg1.setBackgroundResource(R.color.green);
                        }
                    } else {//没有配置好信息，配置
                        WifiPswDialog pswDialog = new WifiPswDialog(ControlPCMainActivity.this, new OnCustomDialogListener() {
                            @Override
                            public void back(String str) {
                                wifiPassword = str;
                                if (wifiPassword != null) {
                                    int netId = localWifiUtils.AddWifiConfig(wifiResultList, wifiItemSSID, wifiPassword);
                                    if (netId != -1) {
                                        localWifiUtils.getConfiguration();//添加了配置信息，要重新得到配置信息
                                        if (localWifiUtils.ConnectWifi(netId)) {
                                            selectedItem.setBackgroundResource(R.color.green);
                                        }
                                    } else {
                                        Toast.makeText(ControlPCMainActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                        selectedItem.setBackgroundResource(R.color.burlywood);
                                    }
                                } else {
                                    selectedItem.setBackgroundResource(R.color.burlywood);
                                }
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onClick (View v){
            if(mTitleReturnIv == v){
                finish();
            }
        }

        @Override
        public void onRefresh () {
            scanResultList.clear();
            mSwipeWifi.setRefreshing(false);
            initWifi();
        }

        @Override
        public void onCheckedChanged (CompoundButton buttonView,boolean isChecked){
            if (isChecked) {
                initWifi();
            } else {
                wifiMa.wifiClose();
                scanResultList.clear();
                mWifiRv.swapAdapter(wifiManagerAdapter, true);//刷新列表
            }
        }

    public void initWifi() {
        wifiMa.wifiOpen();
        wifiMa.wifiStartScan();//开始扫描
        //wifiMa.getWifiState():0正在关闭,1WIFi不可用,2正在打开,3可用,4状态不可用
        while (wifiMa.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {//等待Wifi开启
            Log.i("WifiState", String.valueOf(wifiMa.getWifiState()));
        }
        try {
            Thread.sleep(800);//休眠0.8s，不休眠则会在程序首次开启WIFI时候，处理getScanResults结果，wifiResultList.size()发生异常
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scanResultList = wifiMa.getScanResults();//得到扫描结果
        wifiMa.getConfiguration();
        wifiManagerAdapter = new WifiManagerAdapter(scanResultList);
        mWifiRv.setAdapter(wifiManagerAdapter);
    }
}
