package com.yunxingzh.wireless.wifi;

import android.net.wifi.WifiManager;

public class WifiSignal {
    public int rssi;

    WifiSignal(int rssi) {
        this.rssi = rssi;
    }

    public WifiSignalLevel level(){
        return WifiSignalLevel.from(WifiManager.calculateSignalLevel(this.rssi, WifiSignalLevel.getMaxLevel()));
    }

    public int percent(){
        return WifiManager.calculateSignalLevel(this.rssi, 101);
    }

    public static WifiSignal DEFAULT = new WifiSignal(0);
}