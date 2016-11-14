package com.yunxingzh.wireless.mvp.ui.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.yunxingzh.wireless.R;

import java.util.List;

/**
 * Created by stephon on 2016/11/12.
 * wifi工具
 */

public class WifiUtils {

    private Context context;
    private WifiManager localWifiManager;//提供Wifi管理的各种主要API，主要包含wifi的扫描、建立连接、配置信息等
    //private List<ScanResult> wifiScanList;//ScanResult用来描述已经检测出的接入点，包括接入的地址、名称、身份认证、频率、信号强度等
    private List<WifiConfiguration> wifiConfigList;//描述WIFI的链接信息，包括SSID、SSID隐藏、password等的设置
    private WifiInfo mWifiInfo;//已经建立好网络链接的信息
    private WifiManager.WifiLock wifiLock;//手机锁屏后，阻止WIFI也进入睡眠状态及WIFI的关闭

    public WifiUtils(Context context) {
        if (localWifiManager == null) {
            localWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }
       this.context = context;
    }

    /**
     * WIFI_STATE_DISABLED   WIFI网卡不可用  1
     * WIFI_STATE_ENABLING    WIFI网卡正在打开  2
     * WIFI_STATE_ENABLED     WIFI网卡可用  3
     * WIFI_STATE_UNKNOWN    WIFI网卡状态不可知 4
     */
    //获取网卡状态
    public boolean getWlanState() {
        if (localWifiManager.isWifiEnabled()) {
            return true;
        }
        return false;
    }

    //开启WIFI
    public void wifiOpen() {
        if (!localWifiManager.isWifiEnabled()) {
            ToastUtil.showMiddle(context, R.string.opening);
            localWifiManager.setWifiEnabled(true);
        }
    }

    //关闭WIFI
    public void wifiClose() {
        if (localWifiManager.isWifiEnabled()) {
            localWifiManager.setWifiEnabled(false);
        }
    }

    //扫描wifi
    public void wifiStartScan() {
        localWifiManager.startScan();
    }

    //得到扫描结果
    public List<ScanResult> getScanResults() {
        return localWifiManager.getScanResults();//得到扫描结果
    }

    //得到Wifi配置好的信息
    public void getConfiguration() {
        wifiConfigList = localWifiManager.getConfiguredNetworks();//得到配置好的网络信息
        for (int i = 0; i < wifiConfigList.size(); i++) {
//            Log.i("getConfiguration",wifiConfigList.get(i).SSID);
        }
    }

    //判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
    public int isConfiguration(String SSID) {
        for (int i = 0; i < wifiConfigList.size(); i++) {
            // Log.i(wifiConfigList.get(i).SSID,String.valueOf( wifiConfigList.get(i).networkId));
            if (wifiConfigList.get(i).SSID.equals(SSID)) {//地址相同
                return wifiConfigList.get(i).networkId;
            }
        }
        return -1;
    }

    //检查WIFI状态
    public int getWifiState() {
        return localWifiManager.getWifiState();
    }


    // 指定配置好的网络进行连接
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index >= wifiConfigList.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        localWifiManager.enableNetwork(wifiConfigList.get(index).networkId,
                true);
    }

    //添加指定WIFI的配置信息,原列表不存在此SSID
    public int addWifiConfig(List<ScanResult> wifiList, String ssid, String pwd) {
        int wifiId = -1;
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult wifi = wifiList.get(i);
            if (wifi.SSID.equals(ssid)) {
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\"" + wifi.SSID + "\"";//\"转义字符，代表"
                wifiCong.preSharedKey = "\"" + pwd + "\"";//WPA-PSK密码
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = localWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
                if (wifiId != -1) {
                    return wifiId;
                }
            }
        }
        return wifiId;
    }

    //连接指定Id的WIFI
    public boolean connectWifi(int wifiId) {
        for (int i = 0; i < wifiConfigList.size(); i++) {
            WifiConfiguration wifi = wifiConfigList.get(i);
            if (wifi.networkId == wifiId) {
                while (!(localWifiManager.enableNetwork(wifiId, true))) {//激活该Id，建立连接
                    //status:0--已经连接，1--不可连接，2--可以连接
                    //  Log.i("ConnectWifi",String.valueOf(wifiConfigList.get(wifiId).status));
                }
                return true;
            }
        }
        return false;
    }

    //获取当前连接上的wifi信息
    public WifiInfo getCurrentWifiInfo() {
        if (localWifiManager != null) {
            mWifiInfo = localWifiManager.getConnectionInfo();
        }
        return mWifiInfo;
    }

    //得到MAC地址
    public String getMacAddress() {
        return (mWifiInfo == null) ? "" : mWifiInfo.getMacAddress();
    }

    //得到接入点的BSSID
    public String getBSSID() {
        return (mWifiInfo == null) ? "" : mWifiInfo.getBSSID();
    }

    //得到IP地址
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    //断开指定ID的网络
    public void DisconnectWifi(int netId) {
        localWifiManager.disableNetwork(netId);
        localWifiManager.disconnect();
    }

    // wifi信号强度
    public int calculateSignalLevel() {
        return localWifiManager.calculateSignalLevel(mWifiInfo.getRssi(), 5);
    }

    //创建一个WIFILock
    public void createWifiLock(String lockName) {
        wifiLock = localWifiManager.createWifiLock(lockName);
    }

    //锁定wifilock
    public void acquireWifiLock() {
        wifiLock.acquire();
    }

    //解锁WIFI
    public void releaseWifiLock() {
        if (wifiLock.isHeld()) {//判定是否锁定
            wifiLock.release();
        }
    }
}
