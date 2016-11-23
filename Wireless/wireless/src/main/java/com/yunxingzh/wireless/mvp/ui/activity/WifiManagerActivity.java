package com.yunxingzh.wireless.mvp.ui.activity;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.yunxingzh.wireless.FWManager;
import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.config.Constants;
import com.yunxingzh.wireless.mvp.presenter.IWifiManagerPresenter;
import com.yunxingzh.wireless.mvp.presenter.impl.WifiManagerPresenterImpl;
import com.yunxingzh.wireless.mvp.ui.adapter.AccessPointAdapter;
import com.yunxingzh.wireless.mvp.ui.adapter.WifiManagerAdapter;
import com.yunxingzh.wireless.mvp.ui.base.BaseActivity;
import com.yunxingzh.wireless.mvp.ui.utils.LocationUtils;
import com.yunxingzh.wireless.mvp.ui.utils.SpacesItemDecoration;
import com.yunxingzh.wireless.mvp.ui.utils.ToastUtil;
import com.yunxingzh.wireless.mvp.ui.utils.SpeedTestDialog;
import com.yunxingzh.wireless.mvp.ui.utils.WifiUtils;
import com.yunxingzh.wireless.mvp.view.IWifiManagerView;
import com.yunxingzh.wireless.utility.Logg;
import com.yunxingzh.wireless.wifi.AccessPoint;
import com.yunxingzh.wireless.wifi.WifiState;
import com.yunxingzh.wirelesslibs.wireless.lib.bean.vo.WifiVo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by stephon on 2016/11/12.
 * wifi管理
 */

public class WifiManagerActivity extends BaseActivity implements IWifiManagerView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "WifiManagerActivity";

    private TextView mTitleNameTv;
    private ImageView mTitleReturnIv;
    private ToggleButton mSwitchBtn;
    private SwipeRefreshLayout mSwipeWifi;
    private RecyclerView mWifiRv;
    private WifiManagerAdapter wifiManagerAdapter;
    private AccessPointAdapter mAdapter;
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
       // initData();
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

        mAdapter = new AccessPointAdapter(this);
        mWifiRv.setAdapter(mAdapter);

        mHandler.removeMessages(MSG_REFRESH_LIST);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));

        FWManager.getInstance().addWifiObserver(wifiObserver);
    }


    public void initData() {
        locationUtils = LocationUtils.getInstance(this);
        locationUtils.startMonitor();//开始定位

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
//                    SpeedTestDialog pswDialog = new SpeedTestDialog(WifiManagerActivity.this, new SpeedTestDialog.OnCustomDialogListener() {
//                        @Override
//                        public void back(String str) {
//                            wifiPassword = str;
//                            if (wifiPassword != null) {
//                                int netId = wifiMa.addWifiConfig(scanResultList, String.valueOf(netWorkId), wifiPassword);
//                                if (netId != -1) {
//                                    wifiMa.getConfiguration();//添加了配置信息，要重新得到配置信息
//                                    if (wifiMa.connectWifi(netId)) {
//                                        view.setBackgroundResource(R.color.green_1fbd22);
//                                    }
//                                } else {
//                                    ToastUtil.showMiddle(WifiManagerActivity.this, R.string.internet_error);
//                                    view.setBackgroundResource(R.color.red);
//                                }
//                            } else {
//                                view.setBackgroundResource(R.color.yellow_db5800);
//                            }
//                        }
//                    });
//                    pswDialog.show();
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

//    @Override
//    public void onRefresh() {
//        if (scanResultList != null) {
//            scanResultList.clear();
//        }
//        ToastUtil.showMiddle(this, R.string.refreshing);
//        new Thread(new RefreshWifiThread()).start();
//    }

    @Override
    public void onRefresh() {
        FWManager.getInstance().scan();
        Observable.timer(1500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                mHandler.removeMessages(MSG_REFRESH_LIST);
                mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));
                mSwipeWifi.setRefreshing(false);
            }
        });
    }

    private FWManager.WifiObserver wifiObserver = new FWManager.WifiObserver() {
        @Override
        public void onStateChanged(WifiState new_state, WifiState old_state) {
            Logg.d(TAG, "onStateChanged");

            mHandler.removeMessages(MSG_REFRESH_LIST);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));
        }

        @Override
        public void onListChanged(List<AccessPoint> accessPoints) {
            Logg.d(TAG, "onListChanged");
            mHandler.removeMessages(MSG_REFRESH_LIST);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 0));
        }

        @Override
        public void onRSSIChanged(int rssi) {
            Logg.d(TAG, "onRSSIChanged");
            mHandler.removeMessages(MSG_REFRESH_LIST);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_REFRESH_LIST, 1));
        }

        @Override
        public void onAuthError(AccessPoint ap) {
            Logg.d(TAG, "onAuthError");
            mHandler.removeMessages(MSG_AUTH_ERROR);
            mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_AUTH_ERROR, ap));
        }
    };

    private static final int MSG_REFRESH_LIST = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_LIST:
                    refreshList(msg.arg1);
                    break;
                case MSG_AUTH_ERROR:
                    DialogActivity.showInuptPWD(WifiManagerActivity.this, (AccessPoint)msg.obj, true);
                    break;
            }
        }
    };

    private void refreshList(int forceRefresh) {
        WifiState state = FWManager.getInstance().getState();
        AccessPoint current;
        if (state == WifiState.IDLE || state == WifiState.DISABLED
                || state == WifiState.DISCONNECTED || state == WifiState.UNKOWN) {
            current = null;
        } else {
            current = FWManager.getInstance().getCurrent();
        }
        List<AccessPoint> list = FWManager.getInstance().getList();
        if (mAdapter != null){
            mAdapter.setData(state, current, list, forceRefresh == 1 ? true : false);
        }
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

                //setMyLocationEnabled(true)
                iWifiManagerPresenter.getWifi(locationUtils.getBaseLocation().longitude,locationUtils.getBaseLocation().latitude);//从服务器获取附近wifi

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
        //locationUtils.stopMonitor();
        FWManager.getInstance().removeWifiObserver(wifiObserver);
    }
}
