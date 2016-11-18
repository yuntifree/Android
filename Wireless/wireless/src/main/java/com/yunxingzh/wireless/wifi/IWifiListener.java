package com.yunxingzh.wireless.wifi;


import java.util.List;

public interface IWifiListener {

    public void onStateChanged(WifiState new_state, WifiState old_state);

    public void onListChanged(List<AccessPoint> aps);

    public void onRSSIChanged(int rssi);

    public void onAuthError(AccessPoint ap);
}
